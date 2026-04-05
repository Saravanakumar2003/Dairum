package com.dairum;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DairumApplication_MembersInjector implements MembersInjector<DairumApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public DairumApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<DairumApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new DairumApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(DairumApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.dairum.DairumApplication.workerFactory")
  public static void injectWorkerFactory(DairumApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
