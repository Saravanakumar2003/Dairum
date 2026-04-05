package com.dairum.domain.model;

import java.time.LocalDateTime;

/**
 * Export format options
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/dairum/domain/model/ChatExportFormat;", "", "(Ljava/lang/String;I)V", "PLAIN_TEXT", "MARKDOWN", "PDF", "JSON", "app_debug"})
public enum ChatExportFormat {
    /*public static final*/ PLAIN_TEXT /* = new PLAIN_TEXT() */,
    /*public static final*/ MARKDOWN /* = new MARKDOWN() */,
    /*public static final*/ PDF /* = new PDF() */,
    /*public static final*/ JSON /* = new JSON() */;
    
    ChatExportFormat() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.domain.model.ChatExportFormat> getEntries() {
        return null;
    }
}