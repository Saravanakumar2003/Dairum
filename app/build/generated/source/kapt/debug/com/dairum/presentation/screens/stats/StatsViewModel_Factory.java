package com.dairum.presentation.screens.stats;

import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.dao.TagDao;
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

  private final Provider<TagDao> tagDaoProvider;

  private final Provider<PersonDao> personDaoProvider;

  private final Provider<TagRepository> tagRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  private final Provider<TrackerRepository> trackerRepositoryProvider;

  public StatsViewModel_Factory(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagDao> tagDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    this.entryDaoProvider = entryDaoProvider;
    this.tagDaoProvider = tagDaoProvider;
    this.personDaoProvider = personDaoProvider;
    this.tagRepositoryProvider = tagRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
    this.trackerRepositoryProvider = trackerRepositoryProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(entryDaoProvider.get(), tagDaoProvider.get(), personDaoProvider.get(), tagRepositoryProvider.get(), personRepositoryProvider.get(), mediaRepositoryProvider.get(), trackerRepositoryProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagDao> tagDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider) {
    return new StatsViewModel_Factory(entryDaoProvider, tagDaoProvider, personDaoProvider, tagRepositoryProvider, personRepositoryProvider, mediaRepositoryProvider, trackerRepositoryProvider);
  }

  public static StatsViewModel newInstance(JournalEntryDao entryDao, TagDao tagDao,
      PersonDao personDao, TagRepository tagRepository, PersonRepository personRepository,
      MediaRepository mediaRepository, TrackerRepository trackerRepository) {
    return new StatsViewModel(entryDao, tagDao, personDao, tagRepository, personRepository, mediaRepository, trackerRepository);
  }
}
