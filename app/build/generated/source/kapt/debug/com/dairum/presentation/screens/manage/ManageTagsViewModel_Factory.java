package com.dairum.presentation.screens.manage;

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
public final class ManageTagsViewModel_Factory implements Factory<ManageTagsViewModel> {
  private final Provider<TagRepository> tagRepositoryProvider;

  public ManageTagsViewModel_Factory(Provider<TagRepository> tagRepositoryProvider) {
    this.tagRepositoryProvider = tagRepositoryProvider;
  }

  @Override
  public ManageTagsViewModel get() {
    return newInstance(tagRepositoryProvider.get());
  }

  public static ManageTagsViewModel_Factory create(Provider<TagRepository> tagRepositoryProvider) {
    return new ManageTagsViewModel_Factory(tagRepositoryProvider);
  }

  public static ManageTagsViewModel newInstance(TagRepository tagRepository) {
    return new ManageTagsViewModel(tagRepository);
  }
}
