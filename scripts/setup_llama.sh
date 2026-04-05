#!/bin/bash

# Setup script for llama.cpp native library
# Run this before building the app

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
CPP_DIR="$PROJECT_DIR/app/src/main/cpp"
LLAMA_DIR="$CPP_DIR/llama.cpp"

# llama.cpp version/commit to use (use a stable release)
LLAMA_VERSION="b4380"

echo "============================================"
echo "  Dairum - llama.cpp Native Library Setup"
echo "============================================"
echo ""

# Check if llama.cpp already exists
if [ -d "$LLAMA_DIR" ]; then
    echo "llama.cpp already exists at: $LLAMA_DIR"
    read -p "Do you want to update it? (y/n) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Skipping llama.cpp update."
        exit 0
    fi
    echo "Removing existing llama.cpp..."
    rm -rf "$LLAMA_DIR"
fi

# Clone llama.cpp
echo "Cloning llama.cpp (version: $LLAMA_VERSION)..."
cd "$CPP_DIR"
git clone --depth 1 --branch "$LLAMA_VERSION" https://github.com/ggerganov/llama.cpp.git

if [ ! -d "$LLAMA_DIR" ]; then
    echo "ERROR: Failed to clone llama.cpp"
    exit 1
fi

echo ""
echo "============================================"
echo "  Setup Complete!"
echo "============================================"
echo ""
echo "Next steps:"
echo "1. Open the project in Android Studio"
echo "2. Sync Gradle files"
echo "3. Build the project (this will compile the native library)"
echo ""
echo "Note: The first build will take longer as it compiles llama.cpp"
echo ""
echo "To download a GGUF model for testing:"
echo "  - Recommended: Qwen2.5-1.5B-Instruct-GGUF (Q4_K_M)"
echo "  - URL: https://huggingface.co/Qwen/Qwen2.5-1.5B-Instruct-GGUF"
echo "  - Import the .gguf file using the in-app model import feature"
echo ""
