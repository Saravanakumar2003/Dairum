package com.dairum.presentation.screens.ai;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.model.StorageUsage;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.ai.usecase.AIOrchestrator;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.AIContentRepository;
import com.dairum.domain.repository.AIModelRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import timber.log.Timber;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/dairum/presentation/screens/ai/AITab;", "", "(Ljava/lang/String;I)V", "CHAT", "REFLECTIONS", "PATTERNS", "MODELS", "app_debug"})
public enum AITab {
    /*public static final*/ CHAT /* = new CHAT() */,
    /*public static final*/ REFLECTIONS /* = new REFLECTIONS() */,
    /*public static final*/ PATTERNS /* = new PATTERNS() */,
    /*public static final*/ MODELS /* = new MODELS() */;
    
    AITab() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.screens.ai.AITab> getEntries() {
        return null;
    }
}