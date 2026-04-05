package com.dairum.data.service;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = SmartReminderWorker.class
)
public interface SmartReminderWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.dairum.data.service.SmartReminderWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      SmartReminderWorker_AssistedFactory factory);
}
