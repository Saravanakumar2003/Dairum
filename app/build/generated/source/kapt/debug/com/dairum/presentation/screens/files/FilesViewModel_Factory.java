package com.dairum.presentation.screens.files;

import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
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
public final class FilesViewModel_Factory implements Factory<FilesViewModel> {
  private final Provider<MediaRepository> mediaRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  public FilesViewModel_Factory(Provider<MediaRepository> mediaRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    this.mediaRepositoryProvider = mediaRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public FilesViewModel get() {
    return newInstance(mediaRepositoryProvider.get(), journalRepositoryProvider.get());
  }

  public static FilesViewModel_Factory create(Provider<MediaRepository> mediaRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    return new FilesViewModel_Factory(mediaRepositoryProvider, journalRepositoryProvider);
  }

  public static FilesViewModel newInstance(MediaRepository mediaRepository,
      JournalRepository journalRepository) {
    return new FilesViewModel(mediaRepository, journalRepository);
  }
}
