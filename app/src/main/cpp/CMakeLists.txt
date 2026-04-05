cmake_minimum_required(VERSION 3.22.1)

project("llama-android")

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Build settings for performance
set(CMAKE_BUILD_TYPE Release)

# llama.cpp source directory (fetched via setup script)
set(LLAMA_DIR ${CMAKE_SOURCE_DIR}/llama.cpp)

# Check if llama.cpp exists (check for src/llama.cpp which is the main file)
if(NOT EXISTS ${LLAMA_DIR}/src/llama.cpp)
    message(WARNING "llama.cpp not found! Native AI features will be disabled.")
    message(WARNING "Run ./scripts/setup_llama.sh to enable native AI support.")
    
    # Create a stub library that just logs the error
    add_library(llama-android SHARED
        llama_stub.cpp
    )
    find_library(log-lib log)
    target_link_libraries(llama-android ${log-lib})
    return()
endif()

# Define GGML/llama compile options for Android
add_compile_definitions(
    GGML_USE_CPU
    _GNU_SOURCE
)

# Disable features not needed/supported on Android
add_compile_definitions(
    GGML_LLAMAFILE=OFF
)

# Optimization flags
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -O3 -ffast-math -fno-finite-math-only")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -ffast-math -fno-finite-math-only")

# ARM NEON optimizations for arm64-v8a
if(${ANDROID_ABI} STREQUAL "arm64-v8a")
    add_compile_definitions(GGML_USE_NEON)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -mcpu=generic+fp+simd")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mcpu=generic+fp+simd")
endif()

# GGML source files
set(GGML_SOURCES
    ${LLAMA_DIR}/ggml/src/ggml.c
    ${LLAMA_DIR}/ggml/src/ggml-alloc.c
    ${LLAMA_DIR}/ggml/src/ggml-backend.cpp
    ${LLAMA_DIR}/ggml/src/ggml-backend-reg.cpp
    ${LLAMA_DIR}/ggml/src/ggml-opt.cpp
    ${LLAMA_DIR}/ggml/src/ggml-quants.c
    ${LLAMA_DIR}/ggml/src/ggml-threading.cpp
    ${LLAMA_DIR}/ggml/src/ggml-cpu/ggml-cpu.c
    ${LLAMA_DIR}/ggml/src/ggml-cpu/ggml-cpu.cpp
    ${LLAMA_DIR}/ggml/src/ggml-cpu/ggml-cpu-quants.c
    ${LLAMA_DIR}/ggml/src/ggml-cpu/ggml-cpu-aarch64.cpp
    ${LLAMA_DIR}/ggml/src/ggml-cpu/ggml-cpu-traits.cpp
)

# llama.cpp source files (b4380 version)
set(LLAMA_SOURCES
    ${LLAMA_DIR}/src/llama.cpp
    ${LLAMA_DIR}/src/llama-vocab.cpp
    ${LLAMA_DIR}/src/llama-grammar.cpp
    ${LLAMA_DIR}/src/llama-sampling.cpp
    ${LLAMA_DIR}/src/unicode.cpp
    ${LLAMA_DIR}/src/unicode-data.cpp
    ${LLAMA_DIR}/common/common.cpp
    ${LLAMA_DIR}/common/sampling.cpp
    ${LLAMA_DIR}/common/json-schema-to-grammar.cpp
    ${LLAMA_DIR}/common/log.cpp
)

# Include directories
include_directories(
    ${LLAMA_DIR}/include
    ${LLAMA_DIR}/ggml/include
    ${LLAMA_DIR}/ggml/src
    ${LLAMA_DIR}/ggml/src/ggml-cpu
    ${LLAMA_DIR}/common
    ${LLAMA_DIR}/src
    ${CMAKE_SOURCE_DIR}
)

# Build the llama-android shared library
add_library(llama-android SHARED
    llama_jni.cpp
    build-info.cpp
    ${GGML_SOURCES}
    ${LLAMA_SOURCES}
)

# Find required libraries
find_library(log-lib log)
find_library(android-lib android)

# Link libraries
target_link_libraries(llama-android
    ${log-lib}
    ${android-lib}
)
