package com.dairum;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.dairum.data.ai.conversation.ConversationAIOrchestrator;
import com.dairum.data.ai.embedding.EmbeddingService;
import com.dairum.data.ai.llm.LocalLLM;
import com.dairum.data.ai.model.ModelManager;
import com.dairum.data.ai.reflection.ReflectionGenerationService;
import com.dairum.data.ai.retrieval.ConversationMemoryService;
import com.dairum.data.ai.retrieval.RetrievalService;
import com.dairum.data.ai.usecase.AIOrchestrator;
import com.dairum.data.local.dao.AIModelDao;
import com.dairum.data.local.dao.CategoryDao;
import com.dairum.data.local.dao.ChatEmbeddingDao;
import com.dairum.data.local.dao.ChatMessageDao;
import com.dairum.data.local.dao.ChatSessionDao;
import com.dairum.data.local.dao.ConversationContextDao;
import com.dairum.data.local.dao.ExportDao;
import com.dairum.data.local.dao.JournalEmbeddingDao;
import com.dairum.data.local.dao.JournalEntryDao;
import com.dairum.data.local.dao.MediaDao;
import com.dairum.data.local.dao.MemoryDao;
import com.dairum.data.local.dao.NotificationDao;
import com.dairum.data.local.dao.PatternResultDao;
import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.dao.ReflectionMemoryDao;
import com.dairum.data.local.dao.SmartSummaryDao;
import com.dairum.data.local.dao.TagDao;
import com.dairum.data.local.dao.TrackerDao;
import com.dairum.data.local.dao.WeeklyReflectionDao;
import com.dairum.data.local.database.DairumDatabase;
import com.dairum.data.remote.api.WeatherApi;
import com.dairum.data.repository.AIContentRepositoryImpl;
import com.dairum.data.repository.AIModelRepositoryImpl;
import com.dairum.data.repository.ApiKeyRepositoryImpl;
import com.dairum.data.repository.BackupRepositoryImpl;
import com.dairum.data.repository.CategoryRepositoryImpl;
import com.dairum.data.repository.ChatEmbeddingRepositoryImpl;
import com.dairum.data.repository.ChatMessageRepositoryImpl;
import com.dairum.data.repository.ChatSessionRepositoryImpl;
import com.dairum.data.repository.ConversationContextRepositoryImpl;
import com.dairum.data.repository.EmbeddingRepositoryImpl;
import com.dairum.data.repository.ExportRepositoryImpl;
import com.dairum.data.repository.JournalRepositoryImpl;
import com.dairum.data.repository.MediaRepositoryImpl;
import com.dairum.data.repository.MemoryRepositoryImpl;
import com.dairum.data.repository.NotificationRepositoryImpl;
import com.dairum.data.repository.PersonRepositoryImpl;
import com.dairum.data.repository.ReflectionMemoryRepositoryImpl;
import com.dairum.data.repository.SettingsRepositoryImpl;
import com.dairum.data.repository.TagRepositoryImpl;
import com.dairum.data.repository.TrackerRepositoryImpl;
import com.dairum.data.repository.WeatherRepositoryImpl;
import com.dairum.data.security.AppLockManager;
import com.dairum.data.service.LocationService;
import com.dairum.data.service.MoodCheckInWorker;
import com.dairum.data.service.MoodCheckInWorker_AssistedFactory;
import com.dairum.data.service.ReminderWorker;
import com.dairum.data.service.ReminderWorker_AssistedFactory;
import com.dairum.data.service.SmartReminderWorker;
import com.dairum.data.service.SmartReminderWorker_AssistedFactory;
import com.dairum.data.service.VoiceRecorderService;
import com.dairum.di.AIModule_ProvideEmbeddingServiceFactory;
import com.dairum.di.AIModule_ProvideLocalLLMFactory;
import com.dairum.di.AIModule_ProvideRetrievalServiceFactory;
import com.dairum.di.ConversationModule_ProvideConversationAIOrchestratorFactory;
import com.dairum.di.ConversationModule_ProvideConversationMemoryServiceFactory;
import com.dairum.di.ConversationModule_ProvideReflectionGenerationServiceFactory;
import com.dairum.di.DatabaseModule_ProvideAIModelDaoFactory;
import com.dairum.di.DatabaseModule_ProvideCategoryDaoFactory;
import com.dairum.di.DatabaseModule_ProvideChatEmbeddingDaoFactory;
import com.dairum.di.DatabaseModule_ProvideChatMessageDaoFactory;
import com.dairum.di.DatabaseModule_ProvideChatSessionDaoFactory;
import com.dairum.di.DatabaseModule_ProvideConversationContextDaoFactory;
import com.dairum.di.DatabaseModule_ProvideDatabaseFactory;
import com.dairum.di.DatabaseModule_ProvideExportDaoFactory;
import com.dairum.di.DatabaseModule_ProvideJournalEmbeddingDaoFactory;
import com.dairum.di.DatabaseModule_ProvideJournalEntryDaoFactory;
import com.dairum.di.DatabaseModule_ProvideMediaDaoFactory;
import com.dairum.di.DatabaseModule_ProvideMemoryDaoFactory;
import com.dairum.di.DatabaseModule_ProvideNotificationDaoFactory;
import com.dairum.di.DatabaseModule_ProvidePatternResultDaoFactory;
import com.dairum.di.DatabaseModule_ProvidePersonDaoFactory;
import com.dairum.di.DatabaseModule_ProvideReflectionMemoryDaoFactory;
import com.dairum.di.DatabaseModule_ProvideSmartSummaryDaoFactory;
import com.dairum.di.DatabaseModule_ProvideTagDaoFactory;
import com.dairum.di.DatabaseModule_ProvideTrackerDaoFactory;
import com.dairum.di.DatabaseModule_ProvideWeeklyReflectionDaoFactory;
import com.dairum.di.NetworkModule_ProvideMoshiFactory;
import com.dairum.di.NetworkModule_ProvideOkHttpClientFactory;
import com.dairum.di.NetworkModule_ProvideWeatherApiFactory;
import com.dairum.domain.repository.BackupRepository;
import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.ExportRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MediaRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.SettingsRepository;
import com.dairum.domain.repository.TagRepository;
import com.dairum.domain.repository.TrackerRepository;
import com.dairum.domain.repository.WeatherRepository;
import com.dairum.plugin.PluginEditActivity;
import com.dairum.plugin.PluginFireReceiver;
import com.dairum.plugin.PluginFireReceiver_MembersInjector;
import com.dairum.presentation.MainActivity;
import com.dairum.presentation.MainActivity_MembersInjector;
import com.dairum.presentation.screens.ai.AIViewModel;
import com.dairum.presentation.screens.ai.AIViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.category.CategoryEntriesViewModel;
import com.dairum.presentation.screens.category.CategoryEntriesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.chat.ChatViewModel;
import com.dairum.presentation.screens.chat.ChatViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.editor.EditorViewModel;
import com.dairum.presentation.screens.editor.EditorViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.entry.EntryViewViewModel;
import com.dairum.presentation.screens.entry.EntryViewViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.favorites.FavoritesViewModel;
import com.dairum.presentation.screens.favorites.FavoritesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.files.FilesViewModel;
import com.dairum.presentation.screens.files.FilesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.files.PhotoViewerViewModel;
import com.dairum.presentation.screens.files.PhotoViewerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.home.HomeViewModel;
import com.dairum.presentation.screens.home.HomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.manage.ManageCategoriesViewModel;
import com.dairum.presentation.screens.manage.ManageCategoriesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.manage.ManagePeopleViewModel;
import com.dairum.presentation.screens.manage.ManagePeopleViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.manage.ManageTagsViewModel;
import com.dairum.presentation.screens.manage.ManageTagsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.manage.ManageTrackersViewModel;
import com.dairum.presentation.screens.manage.ManageTrackersViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.map.LocationMapViewModel;
import com.dairum.presentation.screens.map.LocationMapViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.memories.MemoriesViewModel;
import com.dairum.presentation.screens.memories.MemoriesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.memories.MemoryDetailViewModel;
import com.dairum.presentation.screens.memories.MemoryDetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.ApiKeySettingsViewModel;
import com.dairum.presentation.screens.settings.ApiKeySettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.BackupRestoreViewModel;
import com.dairum.presentation.screens.settings.BackupRestoreViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.ExportSettingsViewModel;
import com.dairum.presentation.screens.settings.ExportSettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.ManualExportViewModel;
import com.dairum.presentation.screens.settings.ManualExportViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.NotificationSettingsViewModel;
import com.dairum.presentation.screens.settings.NotificationSettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.SecuritySettingsViewModel;
import com.dairum.presentation.screens.settings.SecuritySettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.SettingsViewModel;
import com.dairum.presentation.screens.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.TaskerIntegrationViewModel;
import com.dairum.presentation.screens.settings.TaskerIntegrationViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.settings.TemplateSettingsViewModel;
import com.dairum.presentation.screens.settings.TemplateSettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.stats.StatsViewModel;
import com.dairum.presentation.screens.stats.StatsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.story.StoryViewModel;
import com.dairum.presentation.screens.story.StoryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.presentation.screens.tag.TagEntriesViewModel;
import com.dairum.presentation.screens.tag.TagEntriesViewModel_HiltModules_KeyModule_ProvideFactory;
import com.dairum.receiver.AutomationReceiver;
import com.dairum.receiver.AutomationReceiver_MembersInjector;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.squareup.moshi.Moshi;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerDairumApplication_HiltComponents_SingletonC {
  private DaggerDairumApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public DairumApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements DairumApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements DairumApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements DairumApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements DairumApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements DairumApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements DairumApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements DairumApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public DairumApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends DairumApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends DairumApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends DairumApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends DairumApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectPluginEditActivity(PluginEditActivity pluginEditActivity) {
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(AIViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ApiKeySettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), BackupRestoreViewModel_HiltModules_KeyModule_ProvideFactory.provide(), CategoryEntriesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ChatViewModel_HiltModules_KeyModule_ProvideFactory.provide(), EditorViewModel_HiltModules_KeyModule_ProvideFactory.provide(), EntryViewViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ExportSettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), FavoritesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), FilesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), HomeViewModel_HiltModules_KeyModule_ProvideFactory.provide(), LocationMapViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ManageCategoriesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ManagePeopleViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ManageTagsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ManageTrackersViewModel_HiltModules_KeyModule_ProvideFactory.provide(), ManualExportViewModel_HiltModules_KeyModule_ProvideFactory.provide(), MemoriesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), MemoryDetailViewModel_HiltModules_KeyModule_ProvideFactory.provide(), NotificationSettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PhotoViewerViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SecuritySettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), StatsViewModel_HiltModules_KeyModule_ProvideFactory.provide(), StoryViewModel_HiltModules_KeyModule_ProvideFactory.provide(), TagEntriesViewModel_HiltModules_KeyModule_ProvideFactory.provide(), TaskerIntegrationViewModel_HiltModules_KeyModule_ProvideFactory.provide(), TemplateSettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectAppLockManager(instance, singletonCImpl.appLockManagerProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends DairumApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AIViewModel> aIViewModelProvider;

    private Provider<ApiKeySettingsViewModel> apiKeySettingsViewModelProvider;

    private Provider<BackupRestoreViewModel> backupRestoreViewModelProvider;

    private Provider<CategoryEntriesViewModel> categoryEntriesViewModelProvider;

    private Provider<ChatViewModel> chatViewModelProvider;

    private Provider<EditorViewModel> editorViewModelProvider;

    private Provider<EntryViewViewModel> entryViewViewModelProvider;

    private Provider<ExportSettingsViewModel> exportSettingsViewModelProvider;

    private Provider<FavoritesViewModel> favoritesViewModelProvider;

    private Provider<FilesViewModel> filesViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LocationMapViewModel> locationMapViewModelProvider;

    private Provider<ManageCategoriesViewModel> manageCategoriesViewModelProvider;

    private Provider<ManagePeopleViewModel> managePeopleViewModelProvider;

    private Provider<ManageTagsViewModel> manageTagsViewModelProvider;

    private Provider<ManageTrackersViewModel> manageTrackersViewModelProvider;

    private Provider<ManualExportViewModel> manualExportViewModelProvider;

    private Provider<MemoriesViewModel> memoriesViewModelProvider;

    private Provider<MemoryDetailViewModel> memoryDetailViewModelProvider;

    private Provider<NotificationSettingsViewModel> notificationSettingsViewModelProvider;

    private Provider<PhotoViewerViewModel> photoViewerViewModelProvider;

    private Provider<SecuritySettingsViewModel> securitySettingsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StatsViewModel> statsViewModelProvider;

    private Provider<StoryViewModel> storyViewModelProvider;

    private Provider<TagEntriesViewModel> tagEntriesViewModelProvider;

    private Provider<TaskerIntegrationViewModel> taskerIntegrationViewModelProvider;

    private Provider<TemplateSettingsViewModel> templateSettingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.aIViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.apiKeySettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.backupRestoreViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.categoryEntriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.editorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.entryViewViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.exportSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.favoritesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.filesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.locationMapViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.manageCategoriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.managePeopleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.manageTagsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.manageTrackersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.manualExportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
      this.memoriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 17);
      this.memoryDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 18);
      this.notificationSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 19);
      this.photoViewerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 20);
      this.securitySettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 21);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 22);
      this.statsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 23);
      this.storyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 24);
      this.tagEntriesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 25);
      this.taskerIntegrationViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 26);
      this.templateSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 27);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(28).put("com.dairum.presentation.screens.ai.AIViewModel", ((Provider) aIViewModelProvider)).put("com.dairum.presentation.screens.settings.ApiKeySettingsViewModel", ((Provider) apiKeySettingsViewModelProvider)).put("com.dairum.presentation.screens.settings.BackupRestoreViewModel", ((Provider) backupRestoreViewModelProvider)).put("com.dairum.presentation.screens.category.CategoryEntriesViewModel", ((Provider) categoryEntriesViewModelProvider)).put("com.dairum.presentation.screens.chat.ChatViewModel", ((Provider) chatViewModelProvider)).put("com.dairum.presentation.screens.editor.EditorViewModel", ((Provider) editorViewModelProvider)).put("com.dairum.presentation.screens.entry.EntryViewViewModel", ((Provider) entryViewViewModelProvider)).put("com.dairum.presentation.screens.settings.ExportSettingsViewModel", ((Provider) exportSettingsViewModelProvider)).put("com.dairum.presentation.screens.favorites.FavoritesViewModel", ((Provider) favoritesViewModelProvider)).put("com.dairum.presentation.screens.files.FilesViewModel", ((Provider) filesViewModelProvider)).put("com.dairum.presentation.screens.home.HomeViewModel", ((Provider) homeViewModelProvider)).put("com.dairum.presentation.screens.map.LocationMapViewModel", ((Provider) locationMapViewModelProvider)).put("com.dairum.presentation.screens.manage.ManageCategoriesViewModel", ((Provider) manageCategoriesViewModelProvider)).put("com.dairum.presentation.screens.manage.ManagePeopleViewModel", ((Provider) managePeopleViewModelProvider)).put("com.dairum.presentation.screens.manage.ManageTagsViewModel", ((Provider) manageTagsViewModelProvider)).put("com.dairum.presentation.screens.manage.ManageTrackersViewModel", ((Provider) manageTrackersViewModelProvider)).put("com.dairum.presentation.screens.settings.ManualExportViewModel", ((Provider) manualExportViewModelProvider)).put("com.dairum.presentation.screens.memories.MemoriesViewModel", ((Provider) memoriesViewModelProvider)).put("com.dairum.presentation.screens.memories.MemoryDetailViewModel", ((Provider) memoryDetailViewModelProvider)).put("com.dairum.presentation.screens.settings.NotificationSettingsViewModel", ((Provider) notificationSettingsViewModelProvider)).put("com.dairum.presentation.screens.files.PhotoViewerViewModel", ((Provider) photoViewerViewModelProvider)).put("com.dairum.presentation.screens.settings.SecuritySettingsViewModel", ((Provider) securitySettingsViewModelProvider)).put("com.dairum.presentation.screens.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).put("com.dairum.presentation.screens.stats.StatsViewModel", ((Provider) statsViewModelProvider)).put("com.dairum.presentation.screens.story.StoryViewModel", ((Provider) storyViewModelProvider)).put("com.dairum.presentation.screens.tag.TagEntriesViewModel", ((Provider) tagEntriesViewModelProvider)).put("com.dairum.presentation.screens.settings.TaskerIntegrationViewModel", ((Provider) taskerIntegrationViewModelProvider)).put("com.dairum.presentation.screens.settings.TemplateSettingsViewModel", ((Provider) templateSettingsViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<String, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.dairum.presentation.screens.ai.AIViewModel 
          return (T) new AIViewModel(singletonCImpl.aIOrchestratorProvider.get(), singletonCImpl.modelManagerProvider.get(), singletonCImpl.aIModelRepositoryImplProvider.get(), singletonCImpl.aIContentRepositoryImplProvider.get(), singletonCImpl.provideRetrievalServiceProvider.get(), singletonCImpl.provideLocalLLMProvider.get(), singletonCImpl.embeddingRepositoryImplProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get());

          case 1: // com.dairum.presentation.screens.settings.ApiKeySettingsViewModel 
          return (T) new ApiKeySettingsViewModel(singletonCImpl.apiKeyRepositoryImplProvider.get());

          case 2: // com.dairum.presentation.screens.settings.BackupRestoreViewModel 
          return (T) new BackupRestoreViewModel(singletonCImpl.bindBackupRepositoryProvider.get());

          case 3: // com.dairum.presentation.screens.category.CategoryEntriesViewModel 
          return (T) new CategoryEntriesViewModel(singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindCategoryRepositoryProvider.get());

          case 4: // com.dairum.presentation.screens.chat.ChatViewModel 
          return (T) new ChatViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideConversationAIOrchestratorProvider.get(), singletonCImpl.chatSessionRepositoryImplProvider.get(), singletonCImpl.chatMessageRepositoryImplProvider.get(), singletonCImpl.reflectionMemoryRepositoryImplProvider.get(), singletonCImpl.modelManagerProvider.get(), singletonCImpl.provideLocalLLMProvider.get());

          case 5: // com.dairum.presentation.screens.editor.EditorViewModel 
          return (T) new EditorViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindCategoryRepositoryProvider.get(), singletonCImpl.bindTagRepositoryProvider.get(), singletonCImpl.bindPersonRepositoryProvider.get(), singletonCImpl.bindTrackerRepositoryProvider.get(), singletonCImpl.bindMediaRepositoryProvider.get(), singletonCImpl.bindWeatherRepositoryProvider.get(), singletonCImpl.bindSettingsRepositoryProvider.get(), singletonCImpl.locationServiceProvider.get(), singletonCImpl.voiceRecorderServiceProvider.get());

          case 6: // com.dairum.presentation.screens.entry.EntryViewViewModel 
          return (T) new EntryViewViewModel(singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindMediaRepositoryProvider.get(), singletonCImpl.bindTrackerRepositoryProvider.get());

          case 7: // com.dairum.presentation.screens.settings.ExportSettingsViewModel 
          return (T) new ExportSettingsViewModel(singletonCImpl.bindExportRepositoryProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get());

          case 8: // com.dairum.presentation.screens.favorites.FavoritesViewModel 
          return (T) new FavoritesViewModel(singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.memoryRepositoryImplProvider.get());

          case 9: // com.dairum.presentation.screens.files.FilesViewModel 
          return (T) new FilesViewModel(singletonCImpl.bindMediaRepositoryProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get());

          case 10: // com.dairum.presentation.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindCategoryRepositoryProvider.get(), singletonCImpl.bindTagRepositoryProvider.get(), singletonCImpl.bindPersonRepositoryProvider.get(), singletonCImpl.memoryRepositoryImplProvider.get());

          case 11: // com.dairum.presentation.screens.map.LocationMapViewModel 
          return (T) new LocationMapViewModel(singletonCImpl.bindJournalRepositoryProvider.get());

          case 12: // com.dairum.presentation.screens.manage.ManageCategoriesViewModel 
          return (T) new ManageCategoriesViewModel(singletonCImpl.bindCategoryRepositoryProvider.get());

          case 13: // com.dairum.presentation.screens.manage.ManagePeopleViewModel 
          return (T) new ManagePeopleViewModel(singletonCImpl.bindPersonRepositoryProvider.get());

          case 14: // com.dairum.presentation.screens.manage.ManageTagsViewModel 
          return (T) new ManageTagsViewModel(singletonCImpl.bindTagRepositoryProvider.get());

          case 15: // com.dairum.presentation.screens.manage.ManageTrackersViewModel 
          return (T) new ManageTrackersViewModel(singletonCImpl.bindTrackerRepositoryProvider.get());

          case 16: // com.dairum.presentation.screens.settings.ManualExportViewModel 
          return (T) new ManualExportViewModel(singletonCImpl.bindExportRepositoryProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get());

          case 17: // com.dairum.presentation.screens.memories.MemoriesViewModel 
          return (T) new MemoriesViewModel(singletonCImpl.memoryRepositoryImplProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get());

          case 18: // com.dairum.presentation.screens.memories.MemoryDetailViewModel 
          return (T) new MemoryDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.memoryRepositoryImplProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindMediaRepositoryProvider.get());

          case 19: // com.dairum.presentation.screens.settings.NotificationSettingsViewModel 
          return (T) new NotificationSettingsViewModel(singletonCImpl.notificationRepositoryImplProvider.get(), singletonCImpl.bindPersonRepositoryProvider.get());

          case 20: // com.dairum.presentation.screens.files.PhotoViewerViewModel 
          return (T) new PhotoViewerViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.bindMediaRepositoryProvider.get());

          case 21: // com.dairum.presentation.screens.settings.SecuritySettingsViewModel 
          return (T) new SecuritySettingsViewModel(singletonCImpl.appLockManagerProvider.get());

          case 22: // com.dairum.presentation.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.bindSettingsRepositoryProvider.get(), singletonCImpl.bindCategoryRepositoryProvider.get());

          case 23: // com.dairum.presentation.screens.stats.StatsViewModel 
          return (T) new StatsViewModel(singletonCImpl.provideJournalEntryDaoProvider.get(), singletonCImpl.provideTagDaoProvider.get(), singletonCImpl.providePersonDaoProvider.get(), singletonCImpl.bindTagRepositoryProvider.get(), singletonCImpl.bindPersonRepositoryProvider.get(), singletonCImpl.bindMediaRepositoryProvider.get(), singletonCImpl.bindTrackerRepositoryProvider.get());

          case 24: // com.dairum.presentation.screens.story.StoryViewModel 
          return (T) new StoryViewModel(singletonCImpl.bindJournalRepositoryProvider.get());

          case 25: // com.dairum.presentation.screens.tag.TagEntriesViewModel 
          return (T) new TagEntriesViewModel(singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.bindTagRepositoryProvider.get());

          case 26: // com.dairum.presentation.screens.settings.TaskerIntegrationViewModel 
          return (T) new TaskerIntegrationViewModel(singletonCImpl.bindSettingsRepositoryProvider.get());

          case 27: // com.dairum.presentation.screens.settings.TemplateSettingsViewModel 
          return (T) new TemplateSettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends DairumApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends DairumApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends DairumApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<DairumDatabase> provideDatabaseProvider;

    private Provider<NotificationDao> provideNotificationDaoProvider;

    private Provider<MoodCheckInWorker_AssistedFactory> moodCheckInWorker_AssistedFactoryProvider;

    private Provider<ReminderWorker_AssistedFactory> reminderWorker_AssistedFactoryProvider;

    private Provider<JournalEntryDao> provideJournalEntryDaoProvider;

    private Provider<SmartReminderWorker_AssistedFactory> smartReminderWorker_AssistedFactoryProvider;

    private Provider<ExportDao> provideExportDaoProvider;

    private Provider<ExportRepositoryImpl> exportRepositoryImplProvider;

    private Provider<ExportRepository> bindExportRepositoryProvider;

    private Provider<TagDao> provideTagDaoProvider;

    private Provider<PersonDao> providePersonDaoProvider;

    private Provider<TrackerDao> provideTrackerDaoProvider;

    private Provider<MediaDao> provideMediaDaoProvider;

    private Provider<CategoryDao> provideCategoryDaoProvider;

    private Provider<JournalRepositoryImpl> journalRepositoryImplProvider;

    private Provider<JournalRepository> bindJournalRepositoryProvider;

    private Provider<Moshi> provideMoshiProvider;

    private Provider<BackupRepositoryImpl> backupRepositoryImplProvider;

    private Provider<BackupRepository> bindBackupRepositoryProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<SettingsRepository> bindSettingsRepositoryProvider;

    private Provider<AppLockManager> appLockManagerProvider;

    private Provider<EmbeddingService> provideEmbeddingServiceProvider;

    private Provider<JournalEmbeddingDao> provideJournalEmbeddingDaoProvider;

    private Provider<EmbeddingRepositoryImpl> embeddingRepositoryImplProvider;

    private Provider<AIModelDao> provideAIModelDaoProvider;

    private Provider<AIModelRepositoryImpl> aIModelRepositoryImplProvider;

    private Provider<LocalLLM> provideLocalLLMProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<ModelManager> modelManagerProvider;

    private Provider<RetrievalService> provideRetrievalServiceProvider;

    private Provider<WeeklyReflectionDao> provideWeeklyReflectionDaoProvider;

    private Provider<PatternResultDao> providePatternResultDaoProvider;

    private Provider<SmartSummaryDao> provideSmartSummaryDaoProvider;

    private Provider<AIContentRepositoryImpl> aIContentRepositoryImplProvider;

    private Provider<AIOrchestrator> aIOrchestratorProvider;

    private Provider<ApiKeyRepositoryImpl> apiKeyRepositoryImplProvider;

    private Provider<CategoryRepositoryImpl> categoryRepositoryImplProvider;

    private Provider<CategoryRepository> bindCategoryRepositoryProvider;

    private Provider<ChatMessageDao> provideChatMessageDaoProvider;

    private Provider<ChatSessionDao> provideChatSessionDaoProvider;

    private Provider<ChatMessageRepositoryImpl> chatMessageRepositoryImplProvider;

    private Provider<ChatEmbeddingDao> provideChatEmbeddingDaoProvider;

    private Provider<ChatEmbeddingRepositoryImpl> chatEmbeddingRepositoryImplProvider;

    private Provider<ReflectionMemoryDao> provideReflectionMemoryDaoProvider;

    private Provider<ReflectionMemoryRepositoryImpl> reflectionMemoryRepositoryImplProvider;

    private Provider<ConversationMemoryService> provideConversationMemoryServiceProvider;

    private Provider<ConversationContextDao> provideConversationContextDaoProvider;

    private Provider<ConversationContextRepositoryImpl> conversationContextRepositoryImplProvider;

    private Provider<ReflectionGenerationService> provideReflectionGenerationServiceProvider;

    private Provider<ChatSessionRepositoryImpl> chatSessionRepositoryImplProvider;

    private Provider<ConversationAIOrchestrator> provideConversationAIOrchestratorProvider;

    private Provider<TagRepositoryImpl> tagRepositoryImplProvider;

    private Provider<TagRepository> bindTagRepositoryProvider;

    private Provider<PersonRepositoryImpl> personRepositoryImplProvider;

    private Provider<PersonRepository> bindPersonRepositoryProvider;

    private Provider<TrackerRepositoryImpl> trackerRepositoryImplProvider;

    private Provider<TrackerRepository> bindTrackerRepositoryProvider;

    private Provider<MediaRepositoryImpl> mediaRepositoryImplProvider;

    private Provider<MediaRepository> bindMediaRepositoryProvider;

    private Provider<WeatherApi> provideWeatherApiProvider;

    private Provider<WeatherRepositoryImpl> weatherRepositoryImplProvider;

    private Provider<WeatherRepository> bindWeatherRepositoryProvider;

    private Provider<LocationService> locationServiceProvider;

    private Provider<VoiceRecorderService> voiceRecorderServiceProvider;

    private Provider<MemoryDao> provideMemoryDaoProvider;

    private Provider<MemoryRepositoryImpl> memoryRepositoryImplProvider;

    private Provider<NotificationRepositoryImpl> notificationRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return ImmutableMap.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>of("com.dairum.data.service.MoodCheckInWorker", ((Provider) moodCheckInWorker_AssistedFactoryProvider), "com.dairum.data.service.ReminderWorker", ((Provider) reminderWorker_AssistedFactoryProvider), "com.dairum.data.service.SmartReminderWorker", ((Provider) smartReminderWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<DairumDatabase>(singletonCImpl, 2));
      this.provideNotificationDaoProvider = DoubleCheck.provider(new SwitchingProvider<NotificationDao>(singletonCImpl, 1));
      this.moodCheckInWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<MoodCheckInWorker_AssistedFactory>(singletonCImpl, 0));
      this.reminderWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<ReminderWorker_AssistedFactory>(singletonCImpl, 3));
      this.provideJournalEntryDaoProvider = DoubleCheck.provider(new SwitchingProvider<JournalEntryDao>(singletonCImpl, 5));
      this.smartReminderWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<SmartReminderWorker_AssistedFactory>(singletonCImpl, 4));
      this.provideExportDaoProvider = DoubleCheck.provider(new SwitchingProvider<ExportDao>(singletonCImpl, 7));
      this.exportRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 6);
      this.bindExportRepositoryProvider = DoubleCheck.provider((Provider) exportRepositoryImplProvider);
      this.provideTagDaoProvider = DoubleCheck.provider(new SwitchingProvider<TagDao>(singletonCImpl, 9));
      this.providePersonDaoProvider = DoubleCheck.provider(new SwitchingProvider<PersonDao>(singletonCImpl, 10));
      this.provideTrackerDaoProvider = DoubleCheck.provider(new SwitchingProvider<TrackerDao>(singletonCImpl, 11));
      this.provideMediaDaoProvider = DoubleCheck.provider(new SwitchingProvider<MediaDao>(singletonCImpl, 12));
      this.provideCategoryDaoProvider = DoubleCheck.provider(new SwitchingProvider<CategoryDao>(singletonCImpl, 13));
      this.journalRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 8);
      this.bindJournalRepositoryProvider = DoubleCheck.provider((Provider) journalRepositoryImplProvider);
      this.provideMoshiProvider = DoubleCheck.provider(new SwitchingProvider<Moshi>(singletonCImpl, 15));
      this.backupRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 14);
      this.bindBackupRepositoryProvider = DoubleCheck.provider((Provider) backupRepositoryImplProvider);
      this.settingsRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 16);
      this.bindSettingsRepositoryProvider = DoubleCheck.provider((Provider) settingsRepositoryImplProvider);
      this.appLockManagerProvider = DoubleCheck.provider(new SwitchingProvider<AppLockManager>(singletonCImpl, 17));
      this.provideEmbeddingServiceProvider = DoubleCheck.provider(new SwitchingProvider<EmbeddingService>(singletonCImpl, 20));
      this.provideJournalEmbeddingDaoProvider = DoubleCheck.provider(new SwitchingProvider<JournalEmbeddingDao>(singletonCImpl, 22));
      this.embeddingRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<EmbeddingRepositoryImpl>(singletonCImpl, 21));
      this.provideAIModelDaoProvider = DoubleCheck.provider(new SwitchingProvider<AIModelDao>(singletonCImpl, 25));
      this.aIModelRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AIModelRepositoryImpl>(singletonCImpl, 24));
      this.provideLocalLLMProvider = DoubleCheck.provider(new SwitchingProvider<LocalLLM>(singletonCImpl, 26));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 27));
      this.modelManagerProvider = DoubleCheck.provider(new SwitchingProvider<ModelManager>(singletonCImpl, 23));
      this.provideRetrievalServiceProvider = DoubleCheck.provider(new SwitchingProvider<RetrievalService>(singletonCImpl, 19));
      this.provideWeeklyReflectionDaoProvider = DoubleCheck.provider(new SwitchingProvider<WeeklyReflectionDao>(singletonCImpl, 29));
      this.providePatternResultDaoProvider = DoubleCheck.provider(new SwitchingProvider<PatternResultDao>(singletonCImpl, 30));
      this.provideSmartSummaryDaoProvider = DoubleCheck.provider(new SwitchingProvider<SmartSummaryDao>(singletonCImpl, 31));
      this.aIContentRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AIContentRepositoryImpl>(singletonCImpl, 28));
      this.aIOrchestratorProvider = DoubleCheck.provider(new SwitchingProvider<AIOrchestrator>(singletonCImpl, 18));
      this.apiKeyRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ApiKeyRepositoryImpl>(singletonCImpl, 32));
      this.categoryRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 33);
      this.bindCategoryRepositoryProvider = DoubleCheck.provider((Provider) categoryRepositoryImplProvider);
      this.provideChatMessageDaoProvider = DoubleCheck.provider(new SwitchingProvider<ChatMessageDao>(singletonCImpl, 37));
      this.provideChatSessionDaoProvider = DoubleCheck.provider(new SwitchingProvider<ChatSessionDao>(singletonCImpl, 38));
      this.chatMessageRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChatMessageRepositoryImpl>(singletonCImpl, 36));
      this.provideChatEmbeddingDaoProvider = DoubleCheck.provider(new SwitchingProvider<ChatEmbeddingDao>(singletonCImpl, 40));
      this.chatEmbeddingRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChatEmbeddingRepositoryImpl>(singletonCImpl, 39));
      this.provideReflectionMemoryDaoProvider = DoubleCheck.provider(new SwitchingProvider<ReflectionMemoryDao>(singletonCImpl, 42));
      this.reflectionMemoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ReflectionMemoryRepositoryImpl>(singletonCImpl, 41));
      this.provideConversationMemoryServiceProvider = DoubleCheck.provider(new SwitchingProvider<ConversationMemoryService>(singletonCImpl, 35));
      this.provideConversationContextDaoProvider = DoubleCheck.provider(new SwitchingProvider<ConversationContextDao>(singletonCImpl, 45));
      this.conversationContextRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ConversationContextRepositoryImpl>(singletonCImpl, 44));
      this.provideReflectionGenerationServiceProvider = DoubleCheck.provider(new SwitchingProvider<ReflectionGenerationService>(singletonCImpl, 43));
      this.chatSessionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChatSessionRepositoryImpl>(singletonCImpl, 46));
      this.provideConversationAIOrchestratorProvider = DoubleCheck.provider(new SwitchingProvider<ConversationAIOrchestrator>(singletonCImpl, 34));
      this.tagRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 47);
      this.bindTagRepositoryProvider = DoubleCheck.provider((Provider) tagRepositoryImplProvider);
      this.personRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 48);
      this.bindPersonRepositoryProvider = DoubleCheck.provider((Provider) personRepositoryImplProvider);
      this.trackerRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 49);
      this.bindTrackerRepositoryProvider = DoubleCheck.provider((Provider) trackerRepositoryImplProvider);
      this.mediaRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 50);
      this.bindMediaRepositoryProvider = DoubleCheck.provider((Provider) mediaRepositoryImplProvider);
      this.provideWeatherApiProvider = DoubleCheck.provider(new SwitchingProvider<WeatherApi>(singletonCImpl, 52));
      this.weatherRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 51);
      this.bindWeatherRepositoryProvider = DoubleCheck.provider((Provider) weatherRepositoryImplProvider);
      this.locationServiceProvider = DoubleCheck.provider(new SwitchingProvider<LocationService>(singletonCImpl, 53));
      this.voiceRecorderServiceProvider = DoubleCheck.provider(new SwitchingProvider<VoiceRecorderService>(singletonCImpl, 54));
      this.provideMemoryDaoProvider = DoubleCheck.provider(new SwitchingProvider<MemoryDao>(singletonCImpl, 56));
      this.memoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MemoryRepositoryImpl>(singletonCImpl, 55));
      this.notificationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<NotificationRepositoryImpl>(singletonCImpl, 57));
    }

    @Override
    public void injectDairumApplication(DairumApplication dairumApplication) {
      injectDairumApplication2(dairumApplication);
    }

    @Override
    public void injectPluginFireReceiver(PluginFireReceiver pluginFireReceiver) {
      injectPluginFireReceiver2(pluginFireReceiver);
    }

    @Override
    public void injectAutomationReceiver(AutomationReceiver automationReceiver) {
      injectAutomationReceiver2(automationReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private DairumApplication injectDairumApplication2(DairumApplication instance) {
      DairumApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private PluginFireReceiver injectPluginFireReceiver2(PluginFireReceiver instance) {
      PluginFireReceiver_MembersInjector.injectExportRepository(instance, bindExportRepositoryProvider.get());
      PluginFireReceiver_MembersInjector.injectJournalRepository(instance, bindJournalRepositoryProvider.get());
      PluginFireReceiver_MembersInjector.injectBackupRepository(instance, bindBackupRepositoryProvider.get());
      PluginFireReceiver_MembersInjector.injectSettingsRepository(instance, bindSettingsRepositoryProvider.get());
      return instance;
    }

    private AutomationReceiver injectAutomationReceiver2(AutomationReceiver instance) {
      AutomationReceiver_MembersInjector.injectExportRepository(instance, bindExportRepositoryProvider.get());
      AutomationReceiver_MembersInjector.injectJournalRepository(instance, bindJournalRepositoryProvider.get());
      AutomationReceiver_MembersInjector.injectBackupRepository(instance, bindBackupRepositoryProvider.get());
      AutomationReceiver_MembersInjector.injectSettingsRepository(instance, bindSettingsRepositoryProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.dairum.data.service.MoodCheckInWorker_AssistedFactory 
          return (T) new MoodCheckInWorker_AssistedFactory() {
            @Override
            public MoodCheckInWorker create(Context context, WorkerParameters workerParams) {
              return new MoodCheckInWorker(context, workerParams, singletonCImpl.provideNotificationDaoProvider.get());
            }
          };

          case 1: // com.dairum.data.local.dao.NotificationDao 
          return (T) DatabaseModule_ProvideNotificationDaoFactory.provideNotificationDao(singletonCImpl.provideDatabaseProvider.get());

          case 2: // com.dairum.data.local.database.DairumDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.dairum.data.service.ReminderWorker_AssistedFactory 
          return (T) new ReminderWorker_AssistedFactory() {
            @Override
            public ReminderWorker create(Context context2, WorkerParameters workerParams2) {
              return new ReminderWorker(context2, workerParams2, singletonCImpl.provideNotificationDaoProvider.get());
            }
          };

          case 4: // com.dairum.data.service.SmartReminderWorker_AssistedFactory 
          return (T) new SmartReminderWorker_AssistedFactory() {
            @Override
            public SmartReminderWorker create(Context context3, WorkerParameters workerParams3) {
              return new SmartReminderWorker(context3, workerParams3, singletonCImpl.provideNotificationDaoProvider.get(), singletonCImpl.provideJournalEntryDaoProvider.get());
            }
          };

          case 5: // com.dairum.data.local.dao.JournalEntryDao 
          return (T) DatabaseModule_ProvideJournalEntryDaoFactory.provideJournalEntryDao(singletonCImpl.provideDatabaseProvider.get());

          case 6: // com.dairum.data.repository.ExportRepositoryImpl 
          return (T) new ExportRepositoryImpl(singletonCImpl.provideExportDaoProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.dairum.data.local.dao.ExportDao 
          return (T) DatabaseModule_ProvideExportDaoFactory.provideExportDao(singletonCImpl.provideDatabaseProvider.get());

          case 8: // com.dairum.data.repository.JournalRepositoryImpl 
          return (T) new JournalRepositoryImpl(singletonCImpl.provideJournalEntryDaoProvider.get(), singletonCImpl.provideTagDaoProvider.get(), singletonCImpl.providePersonDaoProvider.get(), singletonCImpl.provideTrackerDaoProvider.get(), singletonCImpl.provideMediaDaoProvider.get(), singletonCImpl.provideCategoryDaoProvider.get());

          case 9: // com.dairum.data.local.dao.TagDao 
          return (T) DatabaseModule_ProvideTagDaoFactory.provideTagDao(singletonCImpl.provideDatabaseProvider.get());

          case 10: // com.dairum.data.local.dao.PersonDao 
          return (T) DatabaseModule_ProvidePersonDaoFactory.providePersonDao(singletonCImpl.provideDatabaseProvider.get());

          case 11: // com.dairum.data.local.dao.TrackerDao 
          return (T) DatabaseModule_ProvideTrackerDaoFactory.provideTrackerDao(singletonCImpl.provideDatabaseProvider.get());

          case 12: // com.dairum.data.local.dao.MediaDao 
          return (T) DatabaseModule_ProvideMediaDaoFactory.provideMediaDao(singletonCImpl.provideDatabaseProvider.get());

          case 13: // com.dairum.data.local.dao.CategoryDao 
          return (T) DatabaseModule_ProvideCategoryDaoFactory.provideCategoryDao(singletonCImpl.provideDatabaseProvider.get());

          case 14: // com.dairum.data.repository.BackupRepositoryImpl 
          return (T) new BackupRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 15: // com.squareup.moshi.Moshi 
          return (T) NetworkModule_ProvideMoshiFactory.provideMoshi();

          case 16: // com.dairum.data.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 17: // com.dairum.data.security.AppLockManager 
          return (T) new AppLockManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindSettingsRepositoryProvider.get());

          case 18: // com.dairum.data.ai.usecase.AIOrchestrator 
          return (T) new AIOrchestrator(singletonCImpl.provideRetrievalServiceProvider.get(), singletonCImpl.provideLocalLLMProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.aIContentRepositoryImplProvider.get(), singletonCImpl.embeddingRepositoryImplProvider.get());

          case 19: // com.dairum.data.ai.retrieval.RetrievalService 
          return (T) AIModule_ProvideRetrievalServiceFactory.provideRetrievalService(singletonCImpl.provideEmbeddingServiceProvider.get(), singletonCImpl.embeddingRepositoryImplProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.modelManagerProvider.get(), singletonCImpl.aIModelRepositoryImplProvider.get());

          case 20: // com.dairum.data.ai.embedding.EmbeddingService 
          return (T) AIModule_ProvideEmbeddingServiceFactory.provideEmbeddingService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 21: // com.dairum.data.repository.EmbeddingRepositoryImpl 
          return (T) new EmbeddingRepositoryImpl(singletonCImpl.provideJournalEmbeddingDaoProvider.get());

          case 22: // com.dairum.data.local.dao.JournalEmbeddingDao 
          return (T) DatabaseModule_ProvideJournalEmbeddingDaoFactory.provideJournalEmbeddingDao(singletonCImpl.provideDatabaseProvider.get());

          case 23: // com.dairum.data.ai.model.ModelManager 
          return (T) new ModelManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.aIModelRepositoryImplProvider.get(), singletonCImpl.provideEmbeddingServiceProvider.get(), singletonCImpl.provideLocalLLMProvider.get(), singletonCImpl.provideOkHttpClientProvider.get());

          case 24: // com.dairum.data.repository.AIModelRepositoryImpl 
          return (T) new AIModelRepositoryImpl(singletonCImpl.provideAIModelDaoProvider.get());

          case 25: // com.dairum.data.local.dao.AIModelDao 
          return (T) DatabaseModule_ProvideAIModelDaoFactory.provideAIModelDao(singletonCImpl.provideDatabaseProvider.get());

          case 26: // com.dairum.data.ai.llm.LocalLLM 
          return (T) AIModule_ProvideLocalLLMFactory.provideLocalLLM(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 27: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 28: // com.dairum.data.repository.AIContentRepositoryImpl 
          return (T) new AIContentRepositoryImpl(singletonCImpl.provideWeeklyReflectionDaoProvider.get(), singletonCImpl.providePatternResultDaoProvider.get(), singletonCImpl.provideSmartSummaryDaoProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 29: // com.dairum.data.local.dao.WeeklyReflectionDao 
          return (T) DatabaseModule_ProvideWeeklyReflectionDaoFactory.provideWeeklyReflectionDao(singletonCImpl.provideDatabaseProvider.get());

          case 30: // com.dairum.data.local.dao.PatternResultDao 
          return (T) DatabaseModule_ProvidePatternResultDaoFactory.providePatternResultDao(singletonCImpl.provideDatabaseProvider.get());

          case 31: // com.dairum.data.local.dao.SmartSummaryDao 
          return (T) DatabaseModule_ProvideSmartSummaryDaoFactory.provideSmartSummaryDao(singletonCImpl.provideDatabaseProvider.get());

          case 32: // com.dairum.data.repository.ApiKeyRepositoryImpl 
          return (T) new ApiKeyRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 33: // com.dairum.data.repository.CategoryRepositoryImpl 
          return (T) new CategoryRepositoryImpl(singletonCImpl.provideCategoryDaoProvider.get());

          case 34: // com.dairum.data.ai.conversation.ConversationAIOrchestrator 
          return (T) ConversationModule_ProvideConversationAIOrchestratorFactory.provideConversationAIOrchestrator(singletonCImpl.provideLocalLLMProvider.get(), singletonCImpl.provideEmbeddingServiceProvider.get(), singletonCImpl.provideConversationMemoryServiceProvider.get(), singletonCImpl.provideReflectionGenerationServiceProvider.get(), singletonCImpl.chatSessionRepositoryImplProvider.get(), singletonCImpl.chatMessageRepositoryImplProvider.get(), singletonCImpl.conversationContextRepositoryImplProvider.get());

          case 35: // com.dairum.data.ai.retrieval.ConversationMemoryService 
          return (T) ConversationModule_ProvideConversationMemoryServiceFactory.provideConversationMemoryService(singletonCImpl.provideEmbeddingServiceProvider.get(), singletonCImpl.chatMessageRepositoryImplProvider.get(), singletonCImpl.chatEmbeddingRepositoryImplProvider.get(), singletonCImpl.reflectionMemoryRepositoryImplProvider.get(), singletonCImpl.bindJournalRepositoryProvider.get(), singletonCImpl.embeddingRepositoryImplProvider.get());

          case 36: // com.dairum.data.repository.ChatMessageRepositoryImpl 
          return (T) new ChatMessageRepositoryImpl(singletonCImpl.provideChatMessageDaoProvider.get(), singletonCImpl.provideChatSessionDaoProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 37: // com.dairum.data.local.dao.ChatMessageDao 
          return (T) DatabaseModule_ProvideChatMessageDaoFactory.provideChatMessageDao(singletonCImpl.provideDatabaseProvider.get());

          case 38: // com.dairum.data.local.dao.ChatSessionDao 
          return (T) DatabaseModule_ProvideChatSessionDaoFactory.provideChatSessionDao(singletonCImpl.provideDatabaseProvider.get());

          case 39: // com.dairum.data.repository.ChatEmbeddingRepositoryImpl 
          return (T) new ChatEmbeddingRepositoryImpl(singletonCImpl.provideChatEmbeddingDaoProvider.get());

          case 40: // com.dairum.data.local.dao.ChatEmbeddingDao 
          return (T) DatabaseModule_ProvideChatEmbeddingDaoFactory.provideChatEmbeddingDao(singletonCImpl.provideDatabaseProvider.get());

          case 41: // com.dairum.data.repository.ReflectionMemoryRepositoryImpl 
          return (T) new ReflectionMemoryRepositoryImpl(singletonCImpl.provideReflectionMemoryDaoProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 42: // com.dairum.data.local.dao.ReflectionMemoryDao 
          return (T) DatabaseModule_ProvideReflectionMemoryDaoFactory.provideReflectionMemoryDao(singletonCImpl.provideDatabaseProvider.get());

          case 43: // com.dairum.data.ai.reflection.ReflectionGenerationService 
          return (T) ConversationModule_ProvideReflectionGenerationServiceFactory.provideReflectionGenerationService(singletonCImpl.provideLocalLLMProvider.get(), singletonCImpl.provideEmbeddingServiceProvider.get(), singletonCImpl.reflectionMemoryRepositoryImplProvider.get(), singletonCImpl.chatMessageRepositoryImplProvider.get(), singletonCImpl.conversationContextRepositoryImplProvider.get());

          case 44: // com.dairum.data.repository.ConversationContextRepositoryImpl 
          return (T) new ConversationContextRepositoryImpl(singletonCImpl.provideConversationContextDaoProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 45: // com.dairum.data.local.dao.ConversationContextDao 
          return (T) DatabaseModule_ProvideConversationContextDaoFactory.provideConversationContextDao(singletonCImpl.provideDatabaseProvider.get());

          case 46: // com.dairum.data.repository.ChatSessionRepositoryImpl 
          return (T) new ChatSessionRepositoryImpl(singletonCImpl.provideChatSessionDaoProvider.get(), singletonCImpl.provideChatMessageDaoProvider.get());

          case 47: // com.dairum.data.repository.TagRepositoryImpl 
          return (T) new TagRepositoryImpl(singletonCImpl.provideTagDaoProvider.get());

          case 48: // com.dairum.data.repository.PersonRepositoryImpl 
          return (T) new PersonRepositoryImpl(singletonCImpl.providePersonDaoProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 49: // com.dairum.data.repository.TrackerRepositoryImpl 
          return (T) new TrackerRepositoryImpl(singletonCImpl.provideTrackerDaoProvider.get());

          case 50: // com.dairum.data.repository.MediaRepositoryImpl 
          return (T) new MediaRepositoryImpl(singletonCImpl.provideMediaDaoProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 51: // com.dairum.data.repository.WeatherRepositoryImpl 
          return (T) new WeatherRepositoryImpl(singletonCImpl.provideWeatherApiProvider.get(), singletonCImpl.apiKeyRepositoryImplProvider.get());

          case 52: // com.dairum.data.remote.api.WeatherApi 
          return (T) NetworkModule_ProvideWeatherApiFactory.provideWeatherApi(singletonCImpl.provideMoshiProvider.get(), singletonCImpl.provideOkHttpClientProvider.get());

          case 53: // com.dairum.data.service.LocationService 
          return (T) new LocationService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 54: // com.dairum.data.service.VoiceRecorderService 
          return (T) new VoiceRecorderService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 55: // com.dairum.data.repository.MemoryRepositoryImpl 
          return (T) new MemoryRepositoryImpl(singletonCImpl.provideMemoryDaoProvider.get(), singletonCImpl.provideJournalEntryDaoProvider.get(), singletonCImpl.provideMediaDaoProvider.get());

          case 56: // com.dairum.data.local.dao.MemoryDao 
          return (T) DatabaseModule_ProvideMemoryDaoFactory.provideMemoryDao(singletonCImpl.provideDatabaseProvider.get());

          case 57: // com.dairum.data.repository.NotificationRepositoryImpl 
          return (T) new NotificationRepositoryImpl(singletonCImpl.provideNotificationDaoProvider.get(), singletonCImpl.provideJournalEntryDaoProvider.get(), singletonCImpl.providePersonDaoProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
