plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "dev.amal.onthewakelivekmm.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "dev.amal.onthewakelivekmm.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidDeps.composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(AndroidDeps.composeUi)
    implementation(AndroidDeps.composeUiTooling)
    implementation(AndroidDeps.composeUiToolingPreview)
    implementation(AndroidDeps.composeFoundation)
    implementation(AndroidDeps.composeMaterial)
    implementation(AndroidDeps.activityCompose)
    implementation(AndroidDeps.composeIconsExtended)
    implementation(AndroidDeps.composeNavigation)
    implementation(AndroidDeps.coilCompose)

    implementation(AndroidDeps.systemUiController)

    implementation(AndroidDeps.ktorCIO)

    implementation(SharedDeps.serialization)

    implementation(AndroidDeps.tabs)
    implementation(AndroidDeps.tabIndicators)

    implementation(platform(AndroidDeps.firebaseBOM))
    implementation(AndroidDeps.firebaseAuth)
    implementation(AndroidDeps.firebaseStorage)

    implementation(AndroidDeps.swipeToDelete)
    implementation(AndroidDeps.uCrop)
    implementation(AndroidDeps.lottie)

    implementation(AndroidDeps.hiltAndroid)
    kapt(AndroidDeps.hiltAndroidCompiler)
    kapt(AndroidDeps.hiltCompiler)
    implementation(AndroidDeps.hiltNavigationCompose)
}