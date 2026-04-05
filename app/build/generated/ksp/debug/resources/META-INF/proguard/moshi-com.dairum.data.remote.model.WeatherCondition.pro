-if class com.dairum.data.remote.model.WeatherCondition
-keepnames class com.dairum.data.remote.model.WeatherCondition
-if class com.dairum.data.remote.model.WeatherCondition
-keep class com.dairum.data.remote.model.WeatherConditionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
