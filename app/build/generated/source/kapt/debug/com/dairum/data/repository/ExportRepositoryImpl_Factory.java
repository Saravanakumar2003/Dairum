package com.dairum.data.repository;

import android.content.Context;
import com.dairum.data.local.dao.ExportDao;
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
public final class ExportRepositoryImpl_Factory implements Factory<ExportRepositoryImpl> {
  private final Provider<ExportDao> exportDaoProvider;

  private final Provider<Context> contextProvider;

  public ExportRepositoryImpl_Factory(Provider<ExportDao> exportDaoProvider,
      Provider<Context> contextProvider) {
    this.exportDaoProvider = exportDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ExportRepositoryImpl get() {
    return newInstance(exportDaoProvider.get(), contextProvider.get());
  }

  public static ExportRepositoryImpl_Factory create(Provider<ExportDao> exportDaoProvider,
      Provider<Context> contextProvider) {
    return new ExportRepositoryImpl_Factory(exportDaoProvider, contextProvider);
  }

  public static ExportRepositoryImpl newInstance(ExportDao exportDao, Context context) {
    return new ExportRepositoryImpl(exportDao, context);
  }
}
