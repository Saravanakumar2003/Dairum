package com.dairum.data.repository;

import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.dao.MemoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MemoryRepositoryImpl_Factory implements Factory<MemoryRepositoryImpl> {
  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<JournalEntryDao> journalEntryDaoProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  public MemoryRepositoryImpl_Factory(Provider<MemoryDao> memoryDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider, Provider<MediaDao> mediaDaoProvider) {
    this.memoryDaoProvider = memoryDaoProvider;
    this.journalEntryDaoProvider = journalEntryDaoProvider;
    this.mediaDaoProvider = mediaDaoProvider;
  }

  @Override
  public MemoryRepositoryImpl get() {
    return newInstance(memoryDaoProvider.get(), journalEntryDaoProvider.get(), mediaDaoProvider.get());
  }

  public static MemoryRepositoryImpl_Factory create(Provider<MemoryDao> memoryDaoProvider,
      Provider<JournalEntryDao> journalEntryDaoProvider, Provider<MediaDao> mediaDaoProvider) {
    return new MemoryRepositoryImpl_Factory(memoryDaoProvider, journalEntryDaoProvider, mediaDaoProvider);
  }

  public static MemoryRepositoryImpl newInstance(MemoryDao memoryDao,
      JournalEntryDao journalEntryDao, MediaDao mediaDao) {
    return new MemoryRepositoryImpl(memoryDao, journalEntryDao, mediaDao);
  }
}
