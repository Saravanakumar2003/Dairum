-if class com.dairum.data.remote.model.WeatherResponse
-keepnames class com.dairum.data.remote.model.WeatherResponse
-if class com.dairum.data.remote.model.WeatherResponse
-keep class com.dairum.data.remote.model.WeatherResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
