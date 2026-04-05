package com.dairum.presentation.screens.home;

import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.TagRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<TagRepository> tagRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<MemoryRepository> memoryRepositoryProvider;

  public HomeViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.tagRepositoryProvider = tagRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
    this.memoryRepositoryProvider = memoryRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(journalRepositoryProvider.get(), categoryRepositoryProvider.get(), tagRepositoryProvider.get(), personRepositoryProvider.get(), memoryRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    return new HomeViewModel_Factory(journalRepositoryProvider, categoryRepositoryProvider, tagRepositoryProvider, personRepositoryProvider, memoryRepositoryProvider);
  }

  public static HomeViewModel newInstance(JournalRepository journalRepository,
      CategoryRepository categoryRepository, TagRepository tagRepository,
      PersonRepository personRepository, MemoryRepository memoryRepository) {
    return new HomeViewModel(journalRepository, categoryRepository, tagRepository, personRepository, memoryRepository);
  }
}
