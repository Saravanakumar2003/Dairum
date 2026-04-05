/**
 * Stub JNI implementation when llama.cpp is not available.
 * All methods return failure/null to signal native library is not functional.
 */

#include <jni.h>
#include <android/log.h>

#define LOG_TAG "LlamaStub"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jlong JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeInit(JNIEnv *env, jobject thiz) {
    LOGE("Native LLM library not built. Run ./scripts/setup_llama.sh and rebuild.");
    return 0; // Return 0 to indicate failure
}

JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeDestroy(JNIEnv *env, jobject thiz, jlong handle) {
    // No-op
}

JNIEXPORT jboolean JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeLoadModel(JNIEnv *env, jobject thiz, jlong handle,
                                                         jstring model_path, jint context_size, jint threads) {
    LOGE("Cannot load model: native LLM library not built");
    return JNI_FALSE;
}

JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeUnloadModel(JNIEnv *env, jobject thiz, jlong handle) {
    // No-op
}

JNIEXPORT jstring JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGenerate(JNIEnv *env, jobject thiz, jlong handle,
                                                        jstring prompt, jint max_tokens,
                                                        jfloat temperature, jfloat top_p, jint top_k,
                                                        jfloat repeat_penalty, jobjectArray stop_tokens) {
    return env->NewStringUTF("Error: Native LLM library not available");
}

JNIEXPORT jboolean JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeStartGeneration(JNIEnv *env, jobject thiz, jlong handle,
                                                               jstring prompt, jint max_tokens,
                                                               jfloat temperature, jfloat top_p, jint top_k,
                                                               jfloat repeat_penalty, jobjectArray stop_tokens) {
    LOGE("Cannot start generation: native LLM library not built");
    return JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGenerateToken(JNIEnv *env, jobject thiz, jlong handle) {
    return nullptr;
}

JNIEXPORT void JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeCancelGeneration(JNIEnv *env, jobject thiz, jlong handle) {
    // No-op
}

JNIEXPORT jlongArray JNICALL
Java_com_dairum_data_ai_llm_LlamaCppLLM_nativeGetModelInfo(JNIEnv *env, jobject thiz, jlong handle) {
    return nullptr;
}

} // extern "C"
