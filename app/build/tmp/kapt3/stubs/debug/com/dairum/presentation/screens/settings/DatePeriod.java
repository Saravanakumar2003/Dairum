package com.dairum.presentation.screens.settings;

import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.FileProvider;
import com.dairum.domain.model.ExportFormat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/dairum/presentation/screens/settings/DatePeriod;", "", "label", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "ALL", "LAST_DAY", "LAST_WEEK", "LAST_MONTH", "CUSTOM", "app_debug"})
public enum DatePeriod {
    /*public static final*/ ALL /* = new ALL(null) */,
    /*public static final*/ LAST_DAY /* = new LAST_DAY(null) */,
    /*public static final*/ LAST_WEEK /* = new LAST_WEEK(null) */,
    /*public static final*/ LAST_MONTH /* = new LAST_MONTH(null) */,
    /*public static final*/ CUSTOM /* = new CUSTOM(null) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String label = null;
    
    DatePeriod(java.lang.String label) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.screens.settings.DatePeriod> getEntries() {
        return null;
    }
}