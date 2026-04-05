package com.dairum.data.repository;

import com.dairum.data.local.dao.CategoryDao;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.dao.TagDao;
import com.dairum.data.local.dao.TrackerDao;
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
public final class JournalRepositoryImpl_Factory implements Factory<JournalRepositoryImpl> {
  private final Provider<JournalEntryDao> entryDaoProvider;

  private final Provider<TagDao> tagDaoProvider;

  private final Provider<PersonDao> personDaoProvider;

  private final Provider<TrackerDao> trackerDaoProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public JournalRepositoryImpl_Factory(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagDao> tagDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<TrackerDao> trackerDaoProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    this.entryDaoProvider = entryDaoProvider;
    this.tagDaoProvider = tagDaoProvider;
    this.personDaoProvider = personDaoProvider;
    this.trackerDaoProvider = trackerDaoProvider;
    this.mediaDaoProvider = mediaDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public JournalRepositoryImpl get() {
    return newInstance(entryDaoProvider.get(), tagDaoProvider.get(), personDaoProvider.get(), trackerDaoProvider.get(), mediaDaoProvider.get(), categoryDaoProvider.get());
  }

  public static JournalRepositoryImpl_Factory create(Provider<JournalEntryDao> entryDaoProvider,
      Provider<TagDao> tagDaoProvider, Provider<PersonDao> personDaoProvider,
      Provider<TrackerDao> trackerDaoProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new JournalRepositoryImpl_Factory(entryDaoProvider, tagDaoProvider, personDaoProvider, trackerDaoProvider, mediaDaoProvider, categoryDaoProvider);
  }

  public static JournalRepositoryImpl newInstance(JournalEntryDao entryDao, TagDao tagDao,
      PersonDao personDao, TrackerDao trackerDao, MediaDao mediaDao, CategoryDao categoryDao) {
    return new JournalRepositoryImpl(entryDao, tagDao, personDao, trackerDao, mediaDao, categoryDao);
  }
}
