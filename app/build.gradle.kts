plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "1.9.10"
}

android {
    namespace = "studio.lemniscate.greeen"
    compileSdk = 36

    defaultConfig {
        applicationId = "studio.lemniscate.greeen"
        minSdk = 26
        targetSdk = 36
        versionCode = 34
        versionName = "1.34"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.0")
    implementation("androidx.activity:activity-compose:1.9.3") // Lifecycle-aware Compose Activity
    implementation("androidx.compose.ui:ui:1.7.4")            // Compose UI Core
    implementation("androidx.compose.material3:material3:1.3.0") // Material Design 3
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7") // Für LifecycleOwner in Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("com.google.android.gms:play-services-ads:23.1.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("androidx.work:work-runtime-ktx:2.11.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("com.android.billingclient:billing-ktx:6.1.0")
}