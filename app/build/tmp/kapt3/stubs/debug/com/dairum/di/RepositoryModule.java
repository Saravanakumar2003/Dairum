package com.dairum.di;

import com.dairum.data.repository.*;
import com.dairum.domain.repository.*;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\'J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0012H\'J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0015H\'J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0018H\'J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0005\u001a\u00020\u001bH\'J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0005\u001a\u00020\u001eH\'J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010\u0005\u001a\u00020!H\'J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u0005\u001a\u00020$H\'J\u0010\u0010%\u001a\u00020&2\u0006\u0010\u0005\u001a\u00020\'H\'J\u0010\u0010(\u001a\u00020)2\u0006\u0010\u0005\u001a\u00020*H\'\u00a8\u0006+"}, d2 = {"Lcom/dairum/di/RepositoryModule;", "", "()V", "bindApiKeyRepository", "Lcom/dairum/domain/repository/ApiKeyRepository;", "impl", "Lcom/dairum/data/repository/ApiKeyRepositoryImpl;", "bindBackupRepository", "Lcom/dairum/domain/repository/BackupRepository;", "Lcom/dairum/data/repository/BackupRepositoryImpl;", "bindCategoryRepository", "Lcom/dairum/domain/repository/CategoryRepository;", "Lcom/dairum/data/repository/CategoryRepositoryImpl;", "bindExportRepository", "Lcom/dairum/domain/repository/ExportRepository;", "Lcom/dairum/data/repository/ExportRepositoryImpl;", "bindJournalRepository", "Lcom/dairum/domain/repository/JournalRepository;", "Lcom/dairum/data/repository/JournalRepositoryImpl;", "bindMediaRepository", "Lcom/dairum/domain/repository/MediaRepository;", "Lcom/dairum/data/repository/MediaRepositoryImpl;", "bindMemoryRepository", "Lcom/dairum/domain/repository/MemoryRepository;", "Lcom/dairum/data/repository/MemoryRepositoryImpl;", "bindNotificationRepository", "Lcom/dairum/domain/repository/NotificationRepository;", "Lcom/dairum/data/repository/NotificationRepositoryImpl;", "bindPersonRepository", "Lcom/dairum/domain/repository/PersonRepository;", "Lcom/dairum/data/repository/PersonRepositoryImpl;", "bindSettingsRepository", "Lcom/dairum/domain/repository/SettingsRepository;", "Lcom/dairum/data/repository/SettingsRepositoryImpl;", "bindTagRepository", "Lcom/dairum/domain/repository/TagRepository;", "Lcom/dairum/data/repository/TagRepositoryImpl;", "bindTrackerRepository", "Lcom/dairum/domain/repository/TrackerRepository;", "Lcom/dairum/data/repository/TrackerRepositoryImpl;", "bindWeatherRepository", "Lcom/dairum/domain/repository/WeatherRepository;", "Lcom/dairum/data/repository/WeatherRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.JournalRepository bindJournalRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.JournalRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.CategoryRepository bindCategoryRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.CategoryRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.TagRepository bindTagRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.TagRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.MediaRepository bindMediaRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.MediaRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.PersonRepository bindPersonRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.PersonRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.TrackerRepository bindTrackerRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.TrackerRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.WeatherRepository bindWeatherRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.WeatherRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ExportRepository bindExportRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ExportRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.SettingsRepository bindSettingsRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.SettingsRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.NotificationRepository bindNotificationRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.NotificationRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.ApiKeyRepository bindApiKeyRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.ApiKeyRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.MemoryRepository bindMemoryRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.MemoryRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.dairum.domain.repository.BackupRepository bindBackupRepository(@org.jetbrains.annotations.NotNull()
    com.dairum.data.repository.BackupRepositoryImpl impl);
}