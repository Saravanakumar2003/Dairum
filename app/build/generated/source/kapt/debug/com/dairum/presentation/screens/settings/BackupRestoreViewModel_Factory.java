package com.dairum.presentation.screens.settings;

import com.dairum.domain.repository.BackupRepository;
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
public final class BackupRestoreViewModel_Factory implements Factory<BackupRestoreViewModel> {
  private final Provider<BackupRepository> backupRepositoryProvider;

  public BackupRestoreViewModel_Factory(Provider<BackupRepository> backupRepositoryProvider) {
    this.backupRepositoryProvider = backupRepositoryProvider;
  }

  @Override
  public BackupRestoreViewModel get() {
    return newInstance(backupRepositoryProvider.get());
  }

  public static BackupRestoreViewModel_Factory create(
      Provider<BackupRepository> backupRepositoryProvider) {
    return new BackupRestoreViewModel_Factory(backupRepositoryProvider);
  }

  public static BackupRestoreViewModel newInstance(BackupRepository backupRepository) {
    return new BackupRestoreViewModel(backupRepository);
  }
}
