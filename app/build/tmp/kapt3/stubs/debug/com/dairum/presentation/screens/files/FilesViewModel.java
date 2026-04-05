package com.dairum.presentation.screens.files;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Location;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.MediaWithEntryInfo;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\b\u0010\u0011\u001a\u00020\u000fH\u0002J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\b\u0010\u0013\u001a\u00020\u000fH\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0014"}, d2 = {"Lcom/dairum/presentation/screens/files/FilesViewModel;", "Landroidx/lifecycle/ViewModel;", "mediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "journalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "(Lcom/dairum/domain/repository/MediaRepository;Lcom/dairum/domain/repository/JournalRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/files/FilesUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadAttachments", "", "loadLocations", "loadPhotos", "loadVideos", "loadVoiceNotes", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FilesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.MediaRepository mediaRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.JournalRepository journalRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.files.FilesUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.files.FilesUiState> uiState = null;
    
    @javax.inject.Inject()
    public FilesViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.MediaRepository mediaRepository, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.JournalRepository journalRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.files.FilesUiState> getUiState() {
        return null;
    }
    
    private final void loadPhotos() {
    }
    
    private final void loadVideos() {
    }
    
    private final void loadVoiceNotes() {
    }
    
    private final void loadAttachments() {
    }
    
    private final void loadLocations() {
    }
}