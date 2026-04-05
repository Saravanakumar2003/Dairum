package com.dairum.presentation.screens.files;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.compose.foundation.gestures.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import coil.request.ImageRequest;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.SavedStateHandle;
import com.dairum.domain.model.Media;
import com.dairum.domain.repository.MediaRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a \u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0002\u001a\u0018\u0010\u000e\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006\u000f"}, d2 = {"PhotoViewerScreen", "", "mediaId", "", "onBackClick", "Lkotlin/Function0;", "viewModel", "Lcom/dairum/presentation/screens/files/PhotoViewerViewModel;", "downloadFile", "context", "Landroid/content/Context;", "uri", "", "fileName", "sharePhoto", "app_debug"})
public final class PhotoViewerScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void PhotoViewerScreen(long mediaId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull()
    com.dairum.presentation.screens.files.PhotoViewerViewModel viewModel) {
    }
    
    private static final void sharePhoto(android.content.Context context, java.lang.String uri) {
    }
    
    private static final void downloadFile(android.content.Context context, java.lang.String uri, java.lang.String fileName) {
    }
}