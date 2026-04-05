package com.dairum.presentation.screens.entry;

import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.TrackerRepository;
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
public final class EntryViewViewModel_Factory implements Factory<EntryViewViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  private final Provider<TrackerRepository> trackerRepositoryProvider;

  public EntryViewViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
    this.trackerRepositoryProvider = trackerRepositoryProvider;
  }

  @Override
  public EntryViewViewModel get() {
    return newInstance(journalRepositoryProvider.get(), mediaRepositoryProvider.get(), trackerRepositoryProvider.get());
  }

  public static EntryViewViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    return new EntryViewViewModel_Factory(journalRepositoryProvider, mediaRepositoryProvider, trackerRepositoryProvider);
  }

  public static EntryViewViewModel newInstance(JournalRepository journalRepository,
      MediaRepository mediaRepository, TrackerRepository trackerRepository) {
    return new EntryViewViewModel(journalRepository, mediaRepository, trackerRepository);
  }
}
