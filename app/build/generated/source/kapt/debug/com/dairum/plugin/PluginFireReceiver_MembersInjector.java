package com.dairum.plugin;

import com.dairum.domain.repository.BackupRepository;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.SettingsRepository;
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
public final class PluginFireReceiver_MembersInjector implements MembersInjector<PluginFireReceiver> {
  private final Provider<ExportRepository> exportRepositoryProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public PluginFireReceiver_MembersInjector(Provider<ExportRepository> exportRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.exportRepositoryProvider = exportRepositoryProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public static MembersInjector<PluginFireReceiver> create(
      Provider<ExportRepository> exportRepositoryProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new PluginFireReceiver_MembersInjector(exportRepositoryProvider, journalRepositoryProvider, backupRepositoryProvider, settingsRepositoryProvider);
  }

  @Override
  public void injectMembers(PluginFireReceiver instance) {
    injectExportRepository(instance, exportRepositoryProvider.get());
    injectJournalRepository(instance, journalRepositoryProvider.get());
    injectBackupRepository(instance, backupRepositoryProvider.get());
    injectSettingsRepository(instance, settingsRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.dairum.plugin.PluginFireReceiver.exportRepository")
  public static void injectExportRepository(PluginFireReceiver instance,
      ExportRepository exportRepository) {
    instance.exportRepository = exportRepository;
  }

  @InjectedFieldSignature("com.dairum.plugin.PluginFireReceiver.journalRepository")
  public static void injectJournalRepository(PluginFireReceiver instance,
      JournalRepository journalRepository) {
    instance.journalRepository = journalRepository;
  }

  @InjectedFieldSignature("com.dairum.plugin.PluginFireReceiver.backupRepository")
  public static void injectBackupRepository(PluginFireReceiver instance,
      BackupRepository backupRepository) {
    instance.backupRepository = backupRepository;
  }

  @InjectedFieldSignature("com.dairum.plugin.PluginFireReceiver.settingsRepository")
  public static void injectSettingsRepository(PluginFireReceiver instance,
      SettingsRepository settingsRepository) {
    instance.settingsRepository = settingsRepository;
  }
}
