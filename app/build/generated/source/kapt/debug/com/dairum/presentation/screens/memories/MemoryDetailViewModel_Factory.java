package com.dairum.presentation.screens.memories;

import androidx.lifecycle.SavedStateHandle;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.MemoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MemoryDetailViewModel_Factory implements Factory<MemoryDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MemoryRepository> memoryRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  public MemoryDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.memoryRepositoryProvider = memoryRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
  }

  @Override
  public MemoryDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), memoryRepositoryProvider.get(), journalRepositoryProvider.get(), mediaRepositoryProvider.get());
  }

  public static MemoryDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider) {
    return new MemoryDetailViewModel_Factory(savedStateHandleProvider, memoryRepositoryProvider, journalRepositoryProvider, mediaRepositoryProvider);
  }

  public static MemoryDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      MemoryRepository memoryRepository, JournalRepository journalRepository,
      MediaRepository mediaRepository) {
    return new MemoryDetailViewModel(savedStateHandle, memoryRepository, journalRepository, mediaRepository);
  }
}
