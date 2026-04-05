package com.dairum.data.repository;

import android.content.Context;
import com.dairum.data.local.dao.MediaDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class MediaRepositoryImpl_Factory implements Factory<MediaRepositoryImpl> {
  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<Context> contextProvider;

  public MediaRepositoryImpl_Factory(Provider<MediaDao> mediaDaoProvider,
      Provider<Context> contextProvider) {
    this.mediaDaoProvider = mediaDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public MediaRepositoryImpl get() {
    return newInstance(mediaDaoProvider.get(), contextProvider.get());
  }

  public static MediaRepositoryImpl_Factory create(Provider<MediaDao> mediaDaoProvider,
      Provider<Context> contextProvider) {
    return new MediaRepositoryImpl_Factory(mediaDaoProvider, contextProvider);
  }

  public static MediaRepositoryImpl newInstance(MediaDao mediaDao, Context context) {
    return new MediaRepositoryImpl(mediaDao, context);
  }
}
