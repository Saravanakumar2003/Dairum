package com.dairum.presentation.screens.map;

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
public final class LocationMapViewModel_Factory implements Factory<LocationMapViewModel> {
  private final Provider<JournalRepository> journalRepositoryProvider;

  public LocationMapViewModel_Factory(Provider<JournalRepository> journalRepositoryProvider) {
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public LocationMapViewModel get() {
    return newInstance(journalRepositoryProvider.get());
  }

  public static LocationMapViewModel_Factory create(
      Provider<JournalRepository> journalRepositoryProvider) {
    return new LocationMapViewModel_Factory(journalRepositoryProvider);
  }

  public static LocationMapViewModel newInstance(JournalRepository journalRepository) {
    return new LocationMapViewModel(journalRepository);
  }
}
