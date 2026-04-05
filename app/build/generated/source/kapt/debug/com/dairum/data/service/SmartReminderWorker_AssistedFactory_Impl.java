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
public final class SmartReminderWorker_AssistedFactory_Impl implements SmartReminderWorker_AssistedFactory {
  private final SmartReminderWorker_Factory delegateFactory;

  SmartReminderWorker_AssistedFactory_Impl(SmartReminderWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public SmartReminderWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<SmartReminderWorker_AssistedFactory> create(
      SmartReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new SmartReminderWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<SmartReminderWorker_AssistedFactory> createFactoryProvider(
      SmartReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new SmartReminderWorker_AssistedFactory_Impl(delegateFactory));
  }
}
