/**
 * JNI Bridge for llama.cpp on Android
 * 
 * This provides the native implementation for LlamaCppLLM.kt
 */

#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <thread>
#include <mutex>
#include <atomic>
#include <memory>

#include "llama.h"
#include "common.h"
#include "sampling.h"

#define TAG "LlamaAndroid"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

// Global state for a loaded model
struct LlamaState {
    llama_model* model = nullptr;
    llama_context* ctx = nullptr;
    common_sampler* sampler = nullptr;
    
    std::atomic<bool> is_generating{false};
    std::atomic<bool> cancel_requested{false};
    std::mutex generation_mutex;
    
    // Generation state for streaming
    std::string current_prompt;
    std::vector<llama_token> prompt_tokens;
    int n_past = 0;
    int n_generated = 0;
    
    // Streaming generation limits (previously parsed but never stored/enforced - caused
    // runaway generation that hallucinated fake "User:"/"Assistant:" turns indefinitely)
    int stream_max_tokens = 0;
    std::vector<std::string> stream_stop_tokens;
    std::string stream_generated_text;
    
    std::string model_name;
    int context_size = 0;
    
    ~LlamaState() {
        if (sampler) common_sampler_free(sampler);
        if (ctx) llama_free(ctx);
        if (model) llama_free_model(model);
    }
};

// Convert Java string to C++ string
static std::string jstring_to_string(JNIEnv* env, jstring jstr) {
    if (!jstr) return "";
    const char* chars = env->GetStringUTFChars(jstr, nullptr);
    std::string result(chars);
    env->ReleaseStringUTFChars(jstr, chars);
    return result;
}

// Convert C++ string to Java string
static jstring string_to_jstring(JNIEnv* env, const std::string& str) {
    return env->NewStringUTF(str.c_str());
}

extern "C" {

// Initialize native handle
JNIEXPORT jlong JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeInit(JNIEnv* env, jobject thiz) {
    LOGI("Initializing LlamaState");
    
    // Initialize llama backend
    llama_backend_init();
    
    auto* state = new LlamaState();
    return reinterpret_cast<jlong>(state);
}

// Destroy native handle
JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeDestroy(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return;
    
    LOGI("Destroying LlamaState");
    auto* state = reinterpret_cast<LlamaState*>(handle);
    delete state;
    
    llama_backend_free();
}

// Load a GGUF model
JNIEXPORT jboolean JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeLoadModel(
    JNIEnv* env, jobject thiz,
    jlong handle, jstring jpath, jint context_size, jint threads
) {
    if (handle == 0) return JNI_FALSE;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    std::string path = jstring_to_string(env, jpath);
    
    LOGI("Loading model from: %s (ctx=%d, threads=%d)", path.c_str(), context_size, threads);
    
    // Model parameters
    llama_model_params model_params = llama_model_default_params();
    model_params.n_gpu_layers = 0; // CPU only for Android
    
    // Load model
    state->model = llama_load_model_from_file(path.c_str(), model_params);
    if (!state->model) {
        LOGE("Failed to load model from: %s", path.c_str());
        return JNI_FALSE;
    }
    
    // Context parameters
    llama_context_params ctx_params = llama_context_default_params();
    ctx_params.n_ctx = context_size;
    ctx_params.n_threads = threads;
    ctx_params.n_threads_batch = threads;
    
    // Create context
    state->ctx = llama_new_context_with_model(state->model, ctx_params);
    if (!state->ctx) {
        LOGE("Failed to create context");
        llama_free_model(state->model);
        state->model = nullptr;
        return JNI_FALSE;
    }
    
    state->context_size = context_size;
    state->model_name = path.substr(path.find_last_of("/\\") + 1);
    
    LOGI("Model loaded successfully: %s", state->model_name.c_str());
    return JNI_TRUE;
}

// Unload model
JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeUnloadModel(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    
    LOGI("Unloading model");
    
    if (state->sampler) {
        common_sampler_free(state->sampler);
        state->sampler = nullptr;
    }
    if (state->ctx) {
        llama_free(state->ctx);
        state->ctx = nullptr;
    }
    if (state->model) {
        llama_free_model(state->model);
        state->model = nullptr;
    }
    
    state->n_past = 0;
    state->n_generated = 0;
}

// Check if model is loaded
JNIEXPORT jboolean JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeIsModelLoaded(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return JNI_FALSE;
    auto* state = reinterpret_cast<LlamaState*>(handle);
    return (state->model != nullptr && state->ctx != nullptr) ? JNI_TRUE : JNI_FALSE;
}

// Generate text (blocking)
JNIEXPORT jstring JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGenerate(
    JNIEnv* env, jobject thiz,
    jlong handle,
    jstring jprompt,
    jint max_tokens,
    jfloat temperature,
    jfloat top_p,
    jint top_k,
    jfloat repeat_penalty,
    jobjectArray jstop_tokens
) {
    if (handle == 0) return string_to_jstring(env, "");
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    
    if (!state->model || !state->ctx) {
        LOGE("Model not loaded");
        return string_to_jstring(env, "");
    }
    
    std::lock_guard<std::mutex> lock(state->generation_mutex);
    
    std::string prompt = jstring_to_string(env, jprompt);
    
    // Collect stop tokens
    std::vector<std::string> stop_tokens;
    if (jstop_tokens) {
        int len = env->GetArrayLength(jstop_tokens);
        for (int i = 0; i < len; i++) {
            auto jstr = (jstring)env->GetObjectArrayElement(jstop_tokens, i);
            stop_tokens.push_back(jstring_to_string(env, jstr));
        }
    }
    
    LOGD("Generating with prompt length: %zu, max_tokens: %d", prompt.length(), max_tokens);
    
    // Reset state
    state->is_generating = true;
    state->cancel_requested = false;
    
    // Tokenize prompt
    std::vector<llama_token> tokens = common_tokenize(state->ctx, prompt, true, true);
    
    if (tokens.empty()) {
        LOGE("Failed to tokenize prompt");
        state->is_generating = false;
        return string_to_jstring(env, "");
    }
    
    // Clear KV cache
    llama_kv_cache_clear(state->ctx);
    
    // Create sampler with parameters
    common_params_sampling sampling_params;
    sampling_params.temp = temperature;
    sampling_params.top_p = top_p;
    sampling_params.top_k = top_k;
    sampling_params.penalty_repeat = repeat_penalty;
    
    if (state->sampler) {
        common_sampler_free(state->sampler);
    }
    state->sampler = common_sampler_init(state->model, sampling_params);
    
    // Process prompt in batches
    llama_batch batch = llama_batch_get_one(tokens.data(), tokens.size());
    
    if (llama_decode(state->ctx, batch) != 0) {
        LOGE("Failed to decode prompt");
        state->is_generating = false;
        return string_to_jstring(env, "");
    }
    
    // Generate tokens
    std::string result;
    int n_generated = 0;
    
    while (n_generated < max_tokens && !state->cancel_requested) {
        // Sample next token
        llama_token new_token = common_sampler_sample(state->sampler, state->ctx, -1);
        
        // Check for EOS
        if (llama_token_is_eog(state->model, new_token)) {
            break;
        }
        
        // Decode token to text
        std::string piece = common_token_to_piece(state->ctx, new_token);
        result += piece;
        
        // Check stop tokens
        bool should_stop = false;
        for (const auto& stop : stop_tokens) {
            if (result.length() >= stop.length() &&
                result.substr(result.length() - stop.length()) == stop) {
                result = result.substr(0, result.length() - stop.length());
                should_stop = true;
                break;
            }
        }
        if (should_stop) break;
        
        // Accept token for next iteration
        common_sampler_accept(state->sampler, new_token, true);
        
        // Decode for next token
        batch = llama_batch_get_one(&new_token, 1);
        if (llama_decode(state->ctx, batch) != 0) {
            LOGE("Failed to decode token");
            break;
        }
        
        n_generated++;
    }
    
    state->is_generating = false;
    LOGD("Generated %d tokens, result length: %zu", n_generated, result.length());
    
    return string_to_jstring(env, result);
}

// Start streaming generation
JNIEXPORT jboolean JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeStartGeneration(
    JNIEnv* env, jobject thiz,
    jlong handle,
    jstring jprompt,
    jint max_tokens,
    jfloat temperature,
    jfloat top_p,
    jint top_k,
    jfloat repeat_penalty,
    jobjectArray jstop_tokens
) {
    if (handle == 0) return JNI_FALSE;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    
    if (!state->model || !state->ctx) {
        return JNI_FALSE;
    }
    
    std::lock_guard<std::mutex> lock(state->generation_mutex);
    
    state->current_prompt = jstring_to_string(env, jprompt);
    state->prompt_tokens = common_tokenize(state->ctx, state->current_prompt, true, true);
    
    if (state->prompt_tokens.empty()) {
        return JNI_FALSE;
    }
    
    // Store limits so nativeGenerateToken can actually enforce them
    state->stream_max_tokens = max_tokens;
    state->stream_generated_text.clear();
    state->stream_stop_tokens.clear();
    if (jstop_tokens) {
        int len = env->GetArrayLength(jstop_tokens);
        for (int i = 0; i < len; i++) {
            auto jstr = (jstring)env->GetObjectArrayElement(jstop_tokens, i);
            state->stream_stop_tokens.push_back(jstring_to_string(env, jstr));
        }
    }
    
    // Clear and reset
    llama_kv_cache_clear(state->ctx);
    state->n_past = 0;
    state->n_generated = 0;
    state->is_generating = true;
    state->cancel_requested = false;
    
    // Create sampler
    common_params_sampling sampling_params;
    sampling_params.temp = temperature;
    sampling_params.top_p = top_p;
    sampling_params.top_k = top_k;
    sampling_params.penalty_repeat = repeat_penalty;
    
    if (state->sampler) {
        common_sampler_free(state->sampler);
    }
    state->sampler = common_sampler_init(state->model, sampling_params);
    
    // Process prompt
    llama_batch batch = llama_batch_get_one(state->prompt_tokens.data(), state->prompt_tokens.size());
    
    if (llama_decode(state->ctx, batch) != 0) {
        state->is_generating = false;
        return JNI_FALSE;
    }
    
    state->n_past = state->prompt_tokens.size();
    
    return JNI_TRUE;
}

// Get next token in streaming mode
JNIEXPORT jstring JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGenerateToken(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return nullptr;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    
    if (!state->is_generating || state->cancel_requested) {
        state->is_generating = false;
        return nullptr;
    }
    
    if (!state->model || !state->ctx || !state->sampler) {
        state->is_generating = false;
        return nullptr;
    }
    
    // Enforce the token budget - previously ignored, letting generation run until EOS only
    if (state->stream_max_tokens > 0 && state->n_generated >= state->stream_max_tokens) {
        state->is_generating = false;
        return nullptr;
    }
    
    // Sample next token
    llama_token new_token = common_sampler_sample(state->sampler, state->ctx, -1);
    
    // Check for EOS
    if (llama_token_is_eog(state->model, new_token)) {
        state->is_generating = false;
        return nullptr;
    }
    
    // Decode token to text
    std::string piece = common_token_to_piece(state->ctx, new_token);
    
    // Accept and prepare for next
    common_sampler_accept(state->sampler, new_token, true);
    
    llama_batch batch = llama_batch_get_one(&new_token, 1);
    if (llama_decode(state->ctx, batch) != 0) {
        state->is_generating = false;
        return nullptr;
    }
    
    state->n_generated++;
    
    // Enforce stop sequences (e.g. "User:", "Assistant:") - previously never checked in the
    // streaming path, which is what allowed the model to keep fabricating whole fake dialogues
    state->stream_generated_text += piece;
    for (const auto& stop : state->stream_stop_tokens) {
        if (stop.empty()) continue;
        if (state->stream_generated_text.length() >= stop.length() &&
            state->stream_generated_text.compare(
                state->stream_generated_text.length() - stop.length(), stop.length(), stop) == 0) {
            state->is_generating = false;
            // If the whole stop sequence landed inside this piece, withhold it from output.
            // Otherwise it straddles an already-emitted piece; just stop emitting further text.
            if (piece.length() >= stop.length()) {
                piece = piece.substr(0, piece.length() - stop.length());
            } else {
                piece.clear();
            }
            return piece.empty() ? nullptr : string_to_jstring(env, piece);
        }
    }
    
    return string_to_jstring(env, piece);
}

// Cancel generation
JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeCancelGeneration(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    state->cancel_requested = true;
    
    LOGD("Generation cancelled");
}

// Get model info
JNIEXPORT jlongArray JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGetModelInfo(JNIEnv* env, jobject thiz, jlong handle) {
    if (handle == 0) return nullptr;
    
    auto* state = reinterpret_cast<LlamaState*>(handle);
    
    if (!state->model) return nullptr;
    
    jlongArray result = env->NewLongArray(3);
    if (!result) return nullptr;
    
    jlong info[3];
    info[0] = llama_n_ctx(state->ctx);
    info[1] = llama_n_embd(state->model);
    info[2] = llama_n_vocab(state->model);
    
    env->SetLongArrayRegion(result, 0, 3, info);
    
    return result;
}

} // extern "C"
