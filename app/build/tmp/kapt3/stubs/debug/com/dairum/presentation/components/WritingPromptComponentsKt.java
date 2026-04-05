package com.dairum.presentation.components;

import androidx.compose.animation.*;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material.icons.outlined.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import com.dairum.domain.model.PromptCategory;
import com.dairum.domain.model.WritingPrompt;
import com.dairum.domain.model.WritingPrompts;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a4\u0010\u0006\u001a\u00020\u00012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a4\u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a(\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\t2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a2\u0010\u0013\u001a\u00020\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0014\u0010\u0015\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a(\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\t2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a<\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0015\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u00a2\u0006\u0002\u0010\u001d\u001a\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a8\u0006 "}, d2 = {"CategoryBadgeSmall", "", "category", "Lcom/dairum/domain/model/PromptCategory;", "modifier", "Landroidx/compose/ui/Modifier;", "DailyPromptCard", "onUsePrompt", "Lkotlin/Function1;", "Lcom/dairum/domain/model/WritingPrompt;", "onRefresh", "Lkotlin/Function0;", "PromptCarousel", "prompts", "", "onPromptSelect", "PromptCarouselCard", "prompt", "onClick", "PromptCategoryFilter", "selectedCategory", "onCategorySelect", "PromptChip", "PromptSuggestionsPanel", "isExpanded", "", "onToggle", "getCategoryColor", "Landroidx/compose/ui/graphics/Color;", "(Lcom/dairum/domain/model/PromptCategory;)J", "getCategoryIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "app_debug"})
public final class WritingPromptComponentsKt {
    
    /**
     * Daily prompt card displayed on home screen
     */
    @androidx.compose.runtime.Composable()
    public static final void DailyPromptCard(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.WritingPrompt, kotlin.Unit> onUsePrompt, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Compact prompt chip for inline display
     */
    @androidx.compose.runtime.Composable()
    public static final void PromptChip(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.WritingPrompt prompt, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Prompt selector carousel
     */
    @androidx.compose.runtime.Composable()
    public static final void PromptCarousel(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dairum.domain.model.WritingPrompt> prompts, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.WritingPrompt, kotlin.Unit> onPromptSelect, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Individual prompt card in carousel
     */
    @androidx.compose.runtime.Composable()
    private static final void PromptCarouselCard(com.dairum.domain.model.WritingPrompt prompt, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Category filter chips for prompts
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void PromptCategoryFilter(@org.jetbrains.annotations.Nullable()
    com.dairum.domain.model.PromptCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.PromptCategory, kotlin.Unit> onCategorySelect, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Small category badge
     */
    @androidx.compose.runtime.Composable()
    private static final void CategoryBadgeSmall(com.dairum.domain.model.PromptCategory category, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Get icon for prompt category
     */
    private static final androidx.compose.ui.graphics.vector.ImageVector getCategoryIcon(com.dairum.domain.model.PromptCategory category) {
        return null;
    }
    
    /**
     * Get color for prompt category
     */
    @androidx.compose.runtime.Composable()
    private static final long getCategoryColor(com.dairum.domain.model.PromptCategory category) {
        return 0L;
    }
    
    /**
     * Expandable prompt suggestions panel
     */
    @androidx.compose.runtime.Composable()
    public static final void PromptSuggestionsPanel(boolean isExpanded, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.dairum.domain.model.WritingPrompt, kotlin.Unit> onPromptSelect, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}