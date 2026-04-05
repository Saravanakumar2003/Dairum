package com.dairum.data.service;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class VoiceRecorderService_Factory implements Factory<VoiceRecorderService> {
  private final Provider<Context> contextProvider;

  public VoiceRecorderService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceRecorderService get() {
    return newInstance(contextProvider.get());
  }

  public static VoiceRecorderService_Factory create(Provider<Context> contextProvider) {
    return new VoiceRecorderService_Factory(contextProvider);
  }

  public static VoiceRecorderService newInstance(Context context) {
    return new VoiceRecorderService(context);
  }
}
