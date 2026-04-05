-if class com.dairum.data.remote.model.WeatherLocation
-keepnames class com.dairum.data.remote.model.WeatherLocation
-if class com.dairum.data.remote.model.WeatherLocation
-keep class com.dairum.data.remote.model.WeatherLocationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
