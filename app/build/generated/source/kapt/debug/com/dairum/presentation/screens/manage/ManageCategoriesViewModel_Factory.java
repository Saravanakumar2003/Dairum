package com.dairum.presentation.screens.manage;

import com.dairum.domain.repository.CategoryRepository;
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
public final class ManageCategoriesViewModel_Factory implements Factory<ManageCategoriesViewModel> {
  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public ManageCategoriesViewModel_Factory(
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public ManageCategoriesViewModel get() {
    return newInstance(categoryRepositoryProvider.get());
  }

  public static ManageCategoriesViewModel_Factory create(
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new ManageCategoriesViewModel_Factory(categoryRepositoryProvider);
  }

  public static ManageCategoriesViewModel newInstance(CategoryRepository categoryRepository) {
    return new ManageCategoriesViewModel(categoryRepository);
  }
}
