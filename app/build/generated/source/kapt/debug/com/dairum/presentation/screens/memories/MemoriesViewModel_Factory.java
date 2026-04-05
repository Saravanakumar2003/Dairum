package com.dairum.presentation.screens.memories;

import com.dairum.domain.repository.JournalRepository;
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
public final class MemoriesViewModel_Factory implements Factory<MemoriesViewModel> {
  private final Provider<MemoryRepository> memoryRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  public MemoriesViewModel_Factory(Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    this.memoryRepositoryProvider = memoryRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public MemoriesViewModel get() {
    return newInstance(memoryRepositoryProvider.get(), journalRepositoryProvider.get());
  }

  public static MemoriesViewModel_Factory create(
      Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    return new MemoriesViewModel_Factory(memoryRepositoryProvider, journalRepositoryProvider);
  }

  public static MemoriesViewModel newInstance(MemoryRepository memoryRepository,
      JournalRepository journalRepository) {
    return new MemoriesViewModel(memoryRepository, journalRepository);
  }
}
