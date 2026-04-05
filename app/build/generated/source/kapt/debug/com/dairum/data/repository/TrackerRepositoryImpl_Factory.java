package com.dairum.data.repository;

import com.dairum.data.local.dao.TrackerDao;
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
public final class TrackerRepositoryImpl_Factory implements Factory<TrackerRepositoryImpl> {
  private final Provider<TrackerDao> trackerDaoProvider;

  public TrackerRepositoryImpl_Factory(Provider<TrackerDao> trackerDaoProvider) {
    this.trackerDaoProvider = trackerDaoProvider;
  }

  @Override
  public TrackerRepositoryImpl get() {
    return newInstance(trackerDaoProvider.get());
  }

  public static TrackerRepositoryImpl_Factory create(Provider<TrackerDao> trackerDaoProvider) {
    return new TrackerRepositoryImpl_Factory(trackerDaoProvider);
  }

  public static TrackerRepositoryImpl newInstance(TrackerDao trackerDao) {
    return new TrackerRepositoryImpl(trackerDao);
  }
}
