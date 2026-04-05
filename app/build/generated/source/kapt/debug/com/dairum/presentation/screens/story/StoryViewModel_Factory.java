package com.dairum.presentation.screens.story;

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
public final class StoryViewModel_Factory implements Factory<StoryViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  public StoryViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public StoryViewModel get() {
    return newInstance(journalRepositoryProvider.get());
  }

  public static StoryViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider) {
    return new StoryViewModel_Factory(journalRepositoryProvider);
  }

  public static StoryViewModel newInstance(JournalRepository journalRepository) {
    return new StoryViewModel(journalRepository);
  }
}
