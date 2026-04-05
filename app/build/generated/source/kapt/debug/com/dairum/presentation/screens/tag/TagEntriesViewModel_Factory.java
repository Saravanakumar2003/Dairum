package com.dairum.presentation.screens.tag;

import com.dairum.domain.repository.JournalRepository;
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
public final class TagEntriesViewModel_Factory implements Factory<TagEntriesViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<TagRepository> tagRepositoryProvider;

  public TagEntriesViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.tagRepositoryProvider = tagRepositoryProvider;
  }

  @Override
  public TagEntriesViewModel get() {
    return newInstance(journalRepositoryProvider.get(), tagRepositoryProvider.get());
  }

  public static TagEntriesViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider) {
    return new TagEntriesViewModel_Factory(journalRepositoryProvider, tagRepositoryProvider);
  }

  public static TagEntriesViewModel newInstance(JournalRepository journalRepository,
      TagRepository tagRepository) {
    return new TagEntriesViewModel(journalRepository, tagRepository);
  }
}
