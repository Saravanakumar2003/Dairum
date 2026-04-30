package com.dairum.presentation.screens.favorites;

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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<MemoryRepository> memoryRepositoryProvider;

  public FavoritesViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.memoryRepositoryProvider = memoryRepositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(journalRepositoryProvider.get(), memoryRepositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    return new FavoritesViewModel_Factory(journalRepositoryProvider, memoryRepositoryProvider);
  }

  public static FavoritesViewModel newInstance(JournalRepository journalRepository,
      MemoryRepository memoryRepository) {
    return new FavoritesViewModel(journalRepository, memoryRepository);
  }
}
