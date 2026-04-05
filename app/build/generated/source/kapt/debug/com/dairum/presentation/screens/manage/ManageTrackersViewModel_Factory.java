package com.dairum.presentation.screens.manage;

import com.dairum.domain.repository.TrackerRepository;
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
public final class ManageTrackersViewModel_Factory implements Factory<ManageTrackersViewModel> {
  private final Provider<TrackerRepository> trackerRepositoryProvider;

  public ManageTrackersViewModel_Factory(Provider<TrackerRepository> trackerRepositoryProvider) {
    this.trackerRepositoryProvider = trackerRepositoryProvider;
  }

  @Override
  public ManageTrackersViewModel get() {
    return newInstance(trackerRepositoryProvider.get());
  }

  public static ManageTrackersViewModel_Factory create(
      Provider<TrackerRepository> trackerRepositoryProvider) {
    return new ManageTrackersViewModel_Factory(trackerRepositoryProvider);
  }

  public static ManageTrackersViewModel newInstance(TrackerRepository trackerRepository) {
    return new ManageTrackersViewModel(trackerRepository);
  }
}
