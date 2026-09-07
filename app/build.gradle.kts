plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hatup"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hatup"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room (База данных)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // ВАЖНО: для Java используем annotationProcessor, а не ksp/kapt

    // Navigation Component (Переходы между экранами)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Glide (Загрузка картинок)
    implementation(libs.glide)

    // ViewModel & LiveData (Архитектура MVVM)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
}