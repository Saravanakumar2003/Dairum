package com.dairum.presentation.screens.memories;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.MediaRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/dairum/presentation/screens/memories/MemoryTab;", "", "(Ljava/lang/String;I)V", "OVERVIEW", "ENTRIES", "GALLERY", "INSIGHTS", "TIMELINE", "app_debug"})
public enum MemoryTab {
    /*public static final*/ OVERVIEW /* = new OVERVIEW() */,
    /*public static final*/ ENTRIES /* = new ENTRIES() */,
    /*public static final*/ GALLERY /* = new GALLERY() */,
    /*public static final*/ INSIGHTS /* = new INSIGHTS() */,
    /*public static final*/ TIMELINE /* = new TIMELINE() */;
    
    MemoryTab() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.screens.memories.MemoryTab> getEntries() {
        return null;
    }
}