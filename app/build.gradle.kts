plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.1.21"
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")

}

android {
    namespace = "ru.eafedorova.recipesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.eafedorova.recipesapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.core)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    implementation (libs.glide)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

}