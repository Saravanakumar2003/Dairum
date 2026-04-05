package com.dairum.presentation.screens.settings;

import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/dairum/presentation/screens/settings/ChangePinStep;", "", "(Ljava/lang/String;I)V", "VERIFY_CURRENT", "ENTER_NEW", "CONFIRM_NEW", "app_debug"})
enum ChangePinStep {
    /*public static final*/ VERIFY_CURRENT /* = new VERIFY_CURRENT() */,
    /*public static final*/ ENTER_NEW /* = new ENTER_NEW() */,
    /*public static final*/ CONFIRM_NEW /* = new CONFIRM_NEW() */;
    
    ChangePinStep() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.screens.settings.ChangePinStep> getEntries() {
        return null;
    }
}