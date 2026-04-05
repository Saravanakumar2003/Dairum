package com.dairum.data.ai.llm;

import android.content.Context;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.TimeoutCancellationException;
import kotlinx.coroutines.flow.Flow;
import timber.log.Timber;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Exception thrown when native LLM library is not available
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/dairum/data/ai/llm/NativeLibraryNotAvailableException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "message", "", "(Ljava/lang/String;)V", "app_debug"})
public final class NativeLibraryNotAvailableException extends java.lang.Exception {
    
    public NativeLibraryNotAvailableException(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
        super();
    }
}