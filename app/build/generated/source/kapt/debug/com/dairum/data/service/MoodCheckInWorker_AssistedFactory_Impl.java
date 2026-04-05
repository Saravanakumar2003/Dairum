package com.dairum.data.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MoodCheckInWorker_AssistedFactory_Impl implements MoodCheckInWorker_AssistedFactory {
  private final MoodCheckInWorker_Factory delegateFactory;

  MoodCheckInWorker_AssistedFactory_Impl(MoodCheckInWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MoodCheckInWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<MoodCheckInWorker_AssistedFactory> create(
      MoodCheckInWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MoodCheckInWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<MoodCheckInWorker_AssistedFactory> createFactoryProvider(
      MoodCheckInWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MoodCheckInWorker_AssistedFactory_Impl(delegateFactory));
  }
}
