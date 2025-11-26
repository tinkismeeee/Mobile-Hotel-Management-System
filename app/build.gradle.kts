import java.util.Properties
import java.io.FileInputStream

// 1. KHAI BÁO PLUGINS
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.kapt)
}

// 2. CẤU HÌNH ANDROID
android {
    namespace = "com.example.androidproject"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.androidproject"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

// 3. THƯ VIỆN (ĐÃ SỬA LỖI XUNG ĐỘT)
dependencies {
    // --- Cơ bản ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.recyclerview)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("androidx.security:security-crypto:1.1.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.guava:guava:31.1-android")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("com.github.CameraKit:blurkit-android:v1.1.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.google.ai.client.generativeai" && requested.name == "generativeai") {
            useVersion("0.9.0")
        }
        if (requested.group == "com.google.guava" && requested.name == "listenablefuture") {
            useVersion("9999.0-empty-to-avoid-conflict-with-guava")
        }
    }
}