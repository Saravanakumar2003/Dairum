# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

#-----------------------------------------
# Application Models & Entities
#-----------------------------------------

# Keep Moshi adapters
-keep class com.dairum.data.remote.model.** { *; }
-keepclassmembers class com.dairum.data.remote.model.** { *; }

# Keep Room entities
-keep class com.dairum.data.local.entity.** { *; }

# Keep domain models
-keep class com.dairum.domain.model.** { *; }

# Keep enum classes used in database converters
-keepclassmembers enum com.dairum.** {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#-----------------------------------------
# Networking - Retrofit & OkHttp
#-----------------------------------------

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-dontwarn javax.annotation.**

# Keep generic signature of Call, Response (for Retrofit)
-keepattributes Signature
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#-----------------------------------------
# Moshi JSON
#-----------------------------------------

-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep,allowobfuscation,allowshrinking @com.squareup.moshi.JsonClass class *
-keepclassmembers,allowobfuscation class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

#-----------------------------------------
# Coroutines
#-----------------------------------------

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

#-----------------------------------------
# Room Database
#-----------------------------------------

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

#-----------------------------------------
# Hilt / Dagger
#-----------------------------------------

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

#-----------------------------------------
# Compose
#-----------------------------------------

-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

#-----------------------------------------
# Coil Image Loading
#-----------------------------------------

-dontwarn coil.**
-keep class coil.** { *; }

#-----------------------------------------
# OSMDroid Maps
#-----------------------------------------

-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

#-----------------------------------------
# Google Maps
#-----------------------------------------

-keep class com.google.android.gms.maps.** { *; }

#-----------------------------------------
# Timber Logging
#-----------------------------------------

-assumenosideeffects class timber.log.Timber {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
}

#-----------------------------------------
# Shimmer
#-----------------------------------------

-keep class com.valentinilk.shimmer.** { *; }
-dontwarn com.valentinilk.shimmer.**

#-----------------------------------------
# Widget
#-----------------------------------------

-keep class com.dairum.widget.** { *; }

#-----------------------------------------
# Keep Parcelables
#-----------------------------------------

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#-----------------------------------------
# Keep Serializable classes
#-----------------------------------------

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#-----------------------------------------
# Keep line numbers for crash reports
#-----------------------------------------

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

#-----------------------------------------
# Miscellaneous
#-----------------------------------------

# Remove verbose logging in release
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
}

# Keep R class members
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom Application class
-keep class com.dairum.DairumApplication { *; }

#-----------------------------------------
# Release-build fixes (found by building and running the signed release)
#-----------------------------------------

# Moshi reflects over this private data class (KotlinJsonAdapterFactory). R8 renamed its fields, so
# backup metadata.json was written as {"a":..,"b":..} and could not be read by another build.
-keep class com.dairum.data.repository.BackupMetadata { *; }

# Same cause: people.importantDates was stored as [{"a":..,"b":..}] instead of label/date, which a
# later release (with different obfuscated names) could not parse, silently dropping the dates.
-keep class com.dairum.data.repository.ImportantDateJson { *; }

# ONNX Runtime's native library finds its Java classes and methods by name through JNI and the AAR
# ships no consumer rules (none appear in the merged R8 configuration).
-keep class ai.onnxruntime.** { *; }
