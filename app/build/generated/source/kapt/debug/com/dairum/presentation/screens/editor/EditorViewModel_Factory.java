package com.dairum.presentation.screens.editor;

import android.content.Context;
import com.dairum.data.service.LocationService;
import com.dairum.data.service.VoiceRecorderService;
import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.SettingsRepository;
import com.dairum.domain.repository.TagRepository;
import com.dairum.domain.repository.TrackerRepository;
import com.dairum.domain.repository.WeatherRepository;
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
public final class EditorViewModel_Factory implements Factory<EditorViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<JournalRepository> journalRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<TagRepository> tagRepositoryProvider;

  private final Provider<PersonRepository> personRepositoryProvider;

  private final Provider<TrackerRepository> trackerRepositoryProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  private final Provider<WeatherRepository> weatherRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<VoiceRecorderService> voiceRecorderServiceProvider;

  public EditorViewModel_Factory(Provider<Context> contextProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<WeatherRepository> weatherRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<VoiceRecorderService> voiceRecorderServiceProvider) {
    this.contextProvider = contextProvider;
    this.journalRepositoryProvider = journalRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.tagRepositoryProvider = tagRepositoryProvider;
    this.personRepositoryProvider = personRepositoryProvider;
    this.trackerRepositoryProvider = trackerRepositoryProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
    this.weatherRepositoryProvider = weatherRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.voiceRecorderServiceProvider = voiceRecorderServiceProvider;
  }

  @Override
  public EditorViewModel get() {
    return newInstance(contextProvider.get(), journalRepositoryProvider.get(), categoryRepositoryProvider.get(), tagRepositoryProvider.get(), personRepositoryProvider.get(), trackerRepositoryProvider.get(), mediaRepositoryProvider.get(), weatherRepositoryProvider.get(), settingsRepositoryProvider.get(), locationServiceProvider.get(), voiceRecorderServiceProvider.get());
  }

  public static EditorViewModel_Factory create(Provider<Context> contextProvider,
      Provider<JournalRepository> journalRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<TagRepository> tagRepositoryProvider,
      Provider<PersonRepository> personRepositoryProvider,
      Provider<TrackerRepository> trackerRepositoryProvider,
      Provider<MediaRepository> mediaRepositoryProvider,
      Provider<WeatherRepository> weatherRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<VoiceRecorderService> voiceRecorderServiceProvider) {
    return new EditorViewModel_Factory(contextProvider, journalRepositoryProvider, categoryRepositoryProvider, tagRepositoryProvider, personRepositoryProvider, trackerRepositoryProvider, mediaRepositoryProvider, weatherRepositoryProvider, settingsRepositoryProvider, locationServiceProvider, voiceRecorderServiceProvider);
  }

  public static EditorViewModel newInstance(Context context, JournalRepository journalRepository,
      CategoryRepository categoryRepository, TagRepository tagRepository,
      PersonRepository personRepository, TrackerRepository trackerRepository,
      MediaRepository mediaRepository, WeatherRepository weatherRepository,
      SettingsRepository settingsRepository, LocationService locationService,
      VoiceRecorderService voiceRecorderService) {
    return new EditorViewModel(context, journalRepository, categoryRepository, tagRepository, personRepository, trackerRepository, mediaRepository, weatherRepository, settingsRepository, locationService, voiceRecorderService);
  }
}
