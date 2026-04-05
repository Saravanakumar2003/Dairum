-if class com.dairum.data.remote.model.CurrentWeather
-keepnames class com.dairum.data.remote.model.CurrentWeather
-if class com.dairum.data.remote.model.CurrentWeather
-keep class com.dairum.data.remote.model.CurrentWeatherJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
