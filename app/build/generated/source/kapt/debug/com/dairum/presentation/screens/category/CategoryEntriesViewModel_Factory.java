package com.dairum.presentation.screens.category;

import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.JournalRepository;
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
public final class CategoryEntriesViewModel_Factory implements Factory<CategoryEntriesViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public CategoryEntriesViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public CategoryEntriesViewModel get() {
    return newInstance(journalRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static CategoryEntriesViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new CategoryEntriesViewModel_Factory(journalRepositoryProvider, categoryRepositoryProvider);
  }

  public static CategoryEntriesViewModel newInstance(JournalRepository journalRepository,
      CategoryRepository categoryRepository) {
    return new CategoryEntriesViewModel(journalRepository, categoryRepository);
  }
}
