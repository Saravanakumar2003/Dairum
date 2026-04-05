package com.dairum.presentation.screens.stats;

import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.TagRepository;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<JournalEntryDao> entryDaoProvider;

  private final Provider<TagRepository> tagRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  private final Provider<TrackerRepository> trackerRepositoryProvider;

  public StatsViewModel_Factory(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    this.entryDaoProvider = entryDaoProvider;
    this.tagRepositoryProvider = tagRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
    this.trackerRepositoryProvider = trackerRepositoryProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(entryDaoProvider.get(), tagRepositoryProvider.get(), personRepositoryProvider.get(), mediaRepositoryProvider.get(), trackerRepositoryProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    return new StatsViewModel_Factory(entryDaoProvider, tagRepositoryProvider, personRepositoryProvider, mediaRepositoryProvider, trackerRepositoryProvider);
  }

  public static StatsViewModel newInstance(JournalEntryDao entryDao, TagRepository tagRepository,
      PersonRepository personRepository, MediaRepository mediaRepository,
      TrackerRepository trackerRepository) {
    return new StatsViewModel(entryDao, tagRepository, personRepository, mediaRepository, trackerRepository);
  }
}
