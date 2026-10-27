plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Release signing. Credentials come from ~/.gradle/gradle.properties (or -P / ORG_GRADLE_PROJECT_*),
// never from this repo. See docs/release-keystore.md.
val releaseSigningProperties = listOf(
    "DAIRUM_RELEASE_STORE_FILE",
    "DAIRUM_RELEASE_STORE_PASSWORD",
    "DAIRUM_RELEASE_KEY_ALIAS",
    "DAIRUM_RELEASE_KEY_PASSWORD"
)
fun releaseSigningProperty(name: String): String? =
    providers.gradleProperty(name).orNull?.takeIf { it.isNotBlank() }
val missingReleaseSigning = releaseSigningProperties.filter { releaseSigningProperty(it) == null }

android {
    namespace = "com.dairum"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dairum"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Room schema export
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        // Add your WeatherAPI key here
        buildConfigField("String", "WEATHER_API_KEY", "\"YOUR_WEATHER_API_KEY\"")
        buildConfigField("String", "WEATHER_API_BASE_URL", "\"https://api.weatherapi.com/v1/\"")
        
        // Native build configuration
        ndk {
            // Target ARM64 (most modern devices) and x86_64 (emulators)
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
        
        // Native build for llama.cpp
        externalNativeBuild {
            cmake {
                cppFlags += listOf("-std=c++17", "-O3", "-ffast-math")
                arguments += listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_PLATFORM=android-26"
                )
            }
        }
    }
    
    // External native build using CMake
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    signingConfigs {
        create("release") {
            // Left unset when a property is missing; verifyReleaseSigning then fails the build
            // with a clear message before anything can be packaged unsigned.
            if (missingReleaseSigning.isEmpty()) {
                storeFile = file(releaseSigningProperty("DAIRUM_RELEASE_STORE_FILE")!!)
                storePassword = releaseSigningProperty("DAIRUM_RELEASE_STORE_PASSWORD")
                keyAlias = releaseSigningProperty("DAIRUM_RELEASE_KEY_ALIAS")
                keyPassword = releaseSigningProperty("DAIRUM_RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    sourceSets {
        // Room schema JSONs double as androidTest assets so MigrationTestHelper can read them.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.activity.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)

    // Networking
    implementation(libs.bundles.networking)
    ksp(libs.moshi.codegen)

    // Media
    implementation(libs.bundles.media)

    // Maps
    implementation(libs.bundles.maps)

    // Charts
    implementation(libs.vico.compose.m3)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Security
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.security.crypto)

    // AppCompat (needed for FragmentActivity / biometric support)
    implementation(libs.androidx.appcompat)

    // Logging
    implementation(libs.timber)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Shimmer
    implementation(libs.shimmer.compose)

    // AI/ML - ONNX Runtime for embeddings
    implementation(libs.onnxruntime.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

kapt {
    correctErrorTypes = true
}

// Fail loudly instead of producing an unsigned (or wrongly signed) release. A task, not a
// taskGraph hook, so it still runs when the configuration cache is reused. Only the tasks that
// sign or package a release depend on it: debug builds and release compilation are unaffected.
val verifyReleaseSigning by tasks.registering {
    val missing = missingReleaseSigning
    val storePath = releaseSigningProperty("DAIRUM_RELEASE_STORE_FILE")
    val repoRoot = rootDir.canonicalPath
    doLast {
        if (missing.isNotEmpty()) {
            throw GradleException(
                "Release signing is not configured: missing " + missing.joinToString(", ") + ".\n" +
                    "Add them to ~/.gradle/gradle.properties (see docs/release-keystore.md). " +
                    "Refusing to build an unsigned release APK."
            )
        }
        val keystore = File(storePath!!)
        if (!keystore.isFile) {
            throw GradleException("DAIRUM_RELEASE_STORE_FILE does not point to a file: $storePath")
        }
        if (keystore.canonicalPath.startsWith(repoRoot + File.separator)) {
            throw GradleException("The release keystore must live outside the repository: ${keystore.canonicalPath}")
        }
    }
}
tasks.matching {
    it.name in setOf("validateSigningRelease", "packageRelease", "packageReleaseBundle", "signReleaseBundle")
}.configureEach { dependsOn(verifyReleaseSigning) }
// When verification is in the graph, run it before any release compilation so a missing property
// fails in seconds instead of after the native build.
tasks.matching { it.name == "preReleaseBuild" }.configureEach { mustRunAfter(verifyReleaseSigning) }
