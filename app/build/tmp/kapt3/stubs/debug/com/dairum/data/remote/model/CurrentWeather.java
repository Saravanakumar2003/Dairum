package com.dairum.data.remote.model;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

@com.squareup.moshi.JsonClass(generateAdapter = true)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b#\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B}\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\b\b\u0001\u0010\t\u001a\u00020\u0003\u0012\b\b\u0001\u0010\n\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u000b\u001a\u00020\f\u0012\b\b\u0001\u0010\r\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0012J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\t\u0010(\u001a\u00020\bH\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\fH\u00c6\u0003J\t\u0010,\u001a\u00020\u0006H\u00c6\u0003J\t\u0010-\u001a\u00020\u0006H\u00c6\u0003J\u0081\u0001\u0010.\u001a\u00020\u00002\b\b\u0003\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u00032\b\b\u0003\u0010\u0005\u001a\u00020\u00062\b\b\u0003\u0010\u0007\u001a\u00020\b2\b\b\u0003\u0010\t\u001a\u00020\u00032\b\b\u0003\u0010\n\u001a\u00020\u00032\b\b\u0003\u0010\u000b\u001a\u00020\f2\b\b\u0003\u0010\r\u001a\u00020\u00062\b\b\u0003\u0010\u000e\u001a\u00020\u00062\b\b\u0003\u0010\u000f\u001a\u00020\u00032\b\b\u0003\u0010\u0010\u001a\u00020\u00032\b\b\u0003\u0010\u0011\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00102\u001a\u00020\u0006H\u00d6\u0001J\t\u00103\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0018R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0018R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018\u00a8\u00064"}, d2 = {"Lcom/dairum/data/remote/model/CurrentWeather;", "", "tempCelsius", "", "tempFahrenheit", "isDay", "", "condition", "Lcom/dairum/data/remote/model/WeatherCondition;", "windKph", "windMph", "windDirection", "", "humidity", "cloudCover", "feelsLikeCelsius", "feelsLikeFahrenheit", "uvIndex", "(DDILcom/dairum/data/remote/model/WeatherCondition;DDLjava/lang/String;IIDDD)V", "getCloudCover", "()I", "getCondition", "()Lcom/dairum/data/remote/model/WeatherCondition;", "getFeelsLikeCelsius", "()D", "getFeelsLikeFahrenheit", "getHumidity", "getTempCelsius", "getTempFahrenheit", "getUvIndex", "getWindDirection", "()Ljava/lang/String;", "getWindKph", "getWindMph", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class CurrentWeather {
    private final double tempCelsius = 0.0;
    private final double tempFahrenheit = 0.0;
    private final int isDay = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.remote.model.WeatherCondition condition = null;
    private final double windKph = 0.0;
    private final double windMph = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String windDirection = null;
    private final int humidity = 0;
    private final int cloudCover = 0;
    private final double feelsLikeCelsius = 0.0;
    private final double feelsLikeFahrenheit = 0.0;
    private final double uvIndex = 0.0;
    
    public CurrentWeather(@com.squareup.moshi.Json(name = "temp_c")
    double tempCelsius, @com.squareup.moshi.Json(name = "temp_f")
    double tempFahrenheit, @com.squareup.moshi.Json(name = "is_day")
    int isDay, @com.squareup.moshi.Json(name = "condition")
    @org.jetbrains.annotations.NotNull()
    com.dairum.data.remote.model.WeatherCondition condition, @com.squareup.moshi.Json(name = "wind_kph")
    double windKph, @com.squareup.moshi.Json(name = "wind_mph")
    double windMph, @com.squareup.moshi.Json(name = "wind_dir")
    @org.jetbrains.annotations.NotNull()
    java.lang.String windDirection, @com.squareup.moshi.Json(name = "humidity")
    int humidity, @com.squareup.moshi.Json(name = "cloud")
    int cloudCover, @com.squareup.moshi.Json(name = "feelslike_c")
    double feelsLikeCelsius, @com.squareup.moshi.Json(name = "feelslike_f")
    double feelsLikeFahrenheit, @com.squareup.moshi.Json(name = "uv")
    double uvIndex) {
        super();
    }
    
    public final double getTempCelsius() {
        return 0.0;
    }
    
    public final double getTempFahrenheit() {
        return 0.0;
    }
    
    public final int isDay() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.remote.model.WeatherCondition getCondition() {
        return null;
    }
    
    public final double getWindKph() {
        return 0.0;
    }
    
    public final double getWindMph() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWindDirection() {
        return null;
    }
    
    public final int getHumidity() {
        return 0;
    }
    
    public final int getCloudCover() {
        return 0;
    }
    
    public final double getFeelsLikeCelsius() {
        return 0.0;
    }
    
    public final double getFeelsLikeFahrenheit() {
        return 0.0;
    }
    
    public final double getUvIndex() {
        return 0.0;
    }
    
    public final double component1() {
        return 0.0;
    }
    
    public final double component10() {
        return 0.0;
    }
    
    public final double component11() {
        return 0.0;
    }
    
    public final double component12() {
        return 0.0;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.remote.model.WeatherCondition component4() {
        return null;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dairum.data.remote.model.CurrentWeather copy(@com.squareup.moshi.Json(name = "temp_c")
    double tempCelsius, @com.squareup.moshi.Json(name = "temp_f")
    double tempFahrenheit, @com.squareup.moshi.Json(name = "is_day")
    int isDay, @com.squareup.moshi.Json(name = "condition")
    @org.jetbrains.annotations.NotNull()
    com.dairum.data.remote.model.WeatherCondition condition, @com.squareup.moshi.Json(name = "wind_kph")
    double windKph, @com.squareup.moshi.Json(name = "wind_mph")
    double windMph, @com.squareup.moshi.Json(name = "wind_dir")
    @org.jetbrains.annotations.NotNull()
    java.lang.String windDirection, @com.squareup.moshi.Json(name = "humidity")
    int humidity, @com.squareup.moshi.Json(name = "cloud")
    int cloudCover, @com.squareup.moshi.Json(name = "feelslike_c")
    double feelsLikeCelsius, @com.squareup.moshi.Json(name = "feelslike_f")
    double feelsLikeFahrenheit, @com.squareup.moshi.Json(name = "uv")
    double uvIndex) {
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