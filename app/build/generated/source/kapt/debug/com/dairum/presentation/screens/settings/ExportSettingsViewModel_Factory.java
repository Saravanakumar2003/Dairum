package com.dairum.presentation.screens.settings;

import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
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
public final class ExportSettingsViewModel_Factory implements Factory<ExportSettingsViewModel> {
  private final Provider<ExportRepository> exportRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  public ExportSettingsViewModel_Factory(Provider<ExportRepository> exportRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    this.exportRepositoryProvider = exportRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
  }

  @Override
  public ExportSettingsViewModel get() {
    return newInstance(exportRepositoryProvider.get(), journalRepositoryProvider.get());
  }

  public static ExportSettingsViewModel_Factory create(
      Provider<ExportRepository> exportRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider) {
    return new ExportSettingsViewModel_Factory(exportRepositoryProvider, journalRepositoryProvider);
  }

  public static ExportSettingsViewModel newInstance(ExportRepository exportRepository,
      JournalRepository journalRepository) {
    return new ExportSettingsViewModel(exportRepository, journalRepository);
  }
}
