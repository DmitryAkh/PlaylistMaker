plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.playlistmaker"
        minSdk = 29
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

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Unit Testing
    testImplementation(libs.junit)

    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Gson
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Moxy
    implementation(libs.moxy)
    implementation(libs.moxy.android)
    kapt(libs.moxy.compiler)
}
