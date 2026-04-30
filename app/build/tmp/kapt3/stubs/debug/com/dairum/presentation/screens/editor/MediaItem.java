package com.dairum.presentation.screens.editor;

import android.content.Context;
import android.net.Uri;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.lifecycle.ViewModel;
import com.dairum.data.service.LocationService;
import com.dairum.data.service.VoiceRecorderService;
import com.dairum.domain.model.*;
import com.dairum.domain.repository.*;
import com.dairum.presentation.theme.EntryBackgroundColor;
import com.dairum.presentation.theme.EntryBackgroundColors;
import com.dairum.widget.TodayWidgetProvider;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.*;
import org.json.JSONArray;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lcom/dairum/presentation/screens/editor/MediaItem;", "", "uri", "", "type", "Lcom/dairum/domain/model/MediaType;", "(Ljava/lang/String;Lcom/dairum/domain/model/MediaType;)V", "getType", "()Lcom/dairum/domain/model/MediaType;", "getUri", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class MediaItem {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String uri = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.domain.model.MediaType type = null;
    
    public MediaItem(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.MediaType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.domain.model.MediaType component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.presentation.screens.editor.MediaItem copy(@org.jetbrains.annotations.NotNull()
    java.lang.String uri, @org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.MediaType type) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}