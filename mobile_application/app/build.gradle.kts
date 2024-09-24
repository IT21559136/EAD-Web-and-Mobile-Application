plugins {
    // Use alias from version catalog for Android and Kotlin plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.example.mobile_application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mobile_application"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Specify test instrumentation runner
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Use vector drawables for backward compatibility
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Java compatibility options for building the project
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Kotlin compatibility options for building the project
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Enable Jetpack Compose in this project
    buildFeatures {
        compose = true
    }

    // Specify Kotlin Compiler Extension version for Jetpack Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    // Exclude unnecessary files to reduce APK size
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    // AndroidX Core library for Kotlin extensions
    implementation(libs.androidx.core.ktx)

    // Lifecycle runtime for managing app states
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Lifecycle runtime livedata
    implementation(libs.androidx.runtime.livedata)

    // Jetpack Compose activity support
    implementation(libs.androidx.activity.compose)

    // Navigation for Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // Jetpack Compose BOM (Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))

    // Core Compose libraries
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design 3 for Compose UI
    implementation(libs.androidx.material3)

    // Material Icons
    implementation("androidx.compose.material:material-icons-core:1.0.1")
    implementation("androidx.compose.material:material-icons-extended:1.0.1")

    // Animation
    //implementation(libs.androidx.animation)
    implementation("androidx.compose.animation:animation:1.7.2")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.1.0")

    // System UI Controller for Jetpack Compose
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.31.0-alpha")

    // DataStore (Preferences)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences.core.jvm)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.8.9")

    // Dagger Hilt for Dependency Injection
    implementation(libs.google.hilt.android)
    kapt(libs.google.hilt.compiler)
    implementation(libs.google.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation("com.squareup.retrofit2:converter-gson:${libs.versions.retrofit.get()}") // Gson Converter
    implementation(libs.okhttp)
    implementation("com.squareup.okhttp3:logging-interceptor:${libs.versions.okhttp.get()}")
    implementation("com.squareup:javapoet:1.13.0")

    // Rating Bar
    implementation("com.github.a914-gowtham:compose-ratingbar:1.2.3")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1") // Check for the latest version
    kapt("androidx.room:room-compiler:2.6.1") // For annotation processing

    // Optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Timber for logging
    implementation(libs.timber)

    // Unit testing with JUnit
    testImplementation(libs.junit)

    // Android UI tests with JUnit and Espresso
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug tools for Compose previews and UI testing
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
hilt {
    enableAggregatingTask = false
}