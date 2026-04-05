package com.dairum.presentation.screens.manage;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.Tag;
import com.dairum.domain.repository.TagRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\rH\u0002J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/dairum/presentation/screens/manage/ManageTagsViewModel;", "Landroidx/lifecycle/ViewModel;", "tagRepository", "Lcom/dairum/domain/repository/TagRepository;", "(Lcom/dairum/domain/repository/TagRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dairum/presentation/screens/manage/ManageTagsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "createTag", "", "name", "", "deleteTag", "id", "", "loadTags", "updateTag", "tag", "Lcom/dairum/domain/model/Tag;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ManageTagsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.repository.TagRepository tagRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dairum.presentation.screens.manage.ManageTagsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.manage.ManageTagsUiState> uiState = null;
    
    @javax.inject.Inject()
    public ManageTagsViewModel(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.repository.TagRepository tagRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dairum.presentation.screens.manage.ManageTagsUiState> getUiState() {
        return null;
    }
    
    private final void loadTags() {
    }
    
    public final void createTag(@org.jetbrains.annotations.NotNull()
    java.lang.String name) {
    }
    
    public final void updateTag(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Tag tag) {
    }
    
    public final void deleteTag(long id) {
    }
}