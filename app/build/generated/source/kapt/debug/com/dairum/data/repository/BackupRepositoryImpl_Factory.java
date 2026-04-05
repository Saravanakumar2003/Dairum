package com.dairum.data.repository;

import android.content.Context;
import com.dairum.data.local.database.DairumDatabase;
import com.squareup.moshi.Moshi;
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
public final class BackupRepositoryImpl_Factory implements Factory<BackupRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<DairumDatabase> databaseProvider;

  private final Provider<Moshi> moshiProvider;

  public BackupRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<DairumDatabase> databaseProvider, Provider<Moshi> moshiProvider) {
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public BackupRepositoryImpl get() {
    return newInstance(contextProvider.get(), databaseProvider.get(), moshiProvider.get());
  }

  public static BackupRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<DairumDatabase> databaseProvider, Provider<Moshi> moshiProvider) {
    return new BackupRepositoryImpl_Factory(contextProvider, databaseProvider, moshiProvider);
  }

  public static BackupRepositoryImpl newInstance(Context context, DairumDatabase database,
      Moshi moshi) {
    return new BackupRepositoryImpl(context, database, moshi);
  }
}
