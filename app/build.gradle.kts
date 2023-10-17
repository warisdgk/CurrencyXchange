import org.gradle.kotlin.dsl.*
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

val keystoreProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "key.properties")))
}

val currentFlavorProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "flavor.properties")))
}

val flavorProperties = Properties().apply {
    val currentFlavorProperties =
        File(rootProject.rootDir, currentFlavorProperties["flavorPropertiesFileName"] as String)
    load(FileInputStream(currentFlavorProperties))
}

android {
    namespace = "mwaris.dev.currencyxchange"
    compileSdk = 33

    defaultConfig {
        applicationId = flavorProperties["applicationId"] as String
        minSdk = 24
        //noinspection OldTargetApi No such requirements for now
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        versionNameSuffix = flavorProperties["versionNameSuffix"] as String

        buildConfigField("String", "BASE_API_URL", "\"${flavorProperties["baseApiUrl"]}\"")
        buildConfigField("String", "APP_ID", "\"${flavorProperties["appId"]}\"")

        manifestPlaceholders["appName"] = flavorProperties["appName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("debug")

            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")

            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Base
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.ktx.lifecycle)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    //Testing
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    api(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.test.core)

    //LeakCanary For Memory Leaks
    debugImplementation(libs.leakcanary)

    //Room For Local DB
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    //noinspection KaptUsageInsteadOfKsp As compiler is have issue with latest gradle plugin
    kapt(libs.room.compiler)

    //Work Manager x Hilt
    implementation(libs.androidx.work.ktx)
    kapt(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    //Chucker For Network Testing
    implementation(libs.chucker)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker)

    //Hilt For DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit & Okhttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.hilt.gson.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
}

kapt {
    correctErrorTypes = true
}