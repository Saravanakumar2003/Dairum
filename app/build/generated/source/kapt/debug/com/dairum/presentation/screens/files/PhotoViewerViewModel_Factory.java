package com.dairum.presentation.screens.files;

import androidx.lifecycle.SavedStateHandle;
import com.dairum.domain.repository.MediaRepository;
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
public final class PhotoViewerViewModel_Factory implements Factory<PhotoViewerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MediaRepository> mediaRepositoryProvider;

  public PhotoViewerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> mediaRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.mediaRepositoryProvider = mediaRepositoryProvider;
  }

  @Override
  public PhotoViewerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), mediaRepositoryProvider.get());
  }

  public static PhotoViewerViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> mediaRepositoryProvider) {
    return new PhotoViewerViewModel_Factory(savedStateHandleProvider, mediaRepositoryProvider);
  }

  public static PhotoViewerViewModel newInstance(SavedStateHandle savedStateHandle,
      MediaRepository mediaRepository) {
    return new PhotoViewerViewModel(savedStateHandle, mediaRepository);
  }
}
