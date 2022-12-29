plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version Deps.kotlinVersion
    id("dagger.hilt.android.plugin")
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
        kotlinCompilerExtensionVersion = Deps.composeVersion
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
    implementation(Deps.composeUi)
    implementation(Deps.composeUiTooling)
    implementation(Deps.composeUiToolingPreview)
    implementation(Deps.composeFoundation)
    implementation(Deps.composeMaterial)
    implementation(Deps.activityCompose)
    implementation(Deps.composeIconsExtended)
    implementation(Deps.composeNavigation)
    implementation(Deps.coilCompose)

    implementation(Deps.systemUiController)

    implementation(Deps.hiltAndroid)
    kapt(Deps.hiltAndroidCompiler)
    kapt(Deps.hiltCompiler)
    implementation(Deps.hiltNavigationCompose)

    implementation(Deps.ktorAndroid)
    implementation(Deps.ktorSerialization)
    implementation(Deps.ktorSerializationJson)

    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.jUnit)
    androidTestImplementation(Deps.composeTesting)
    androidTestImplementation(Deps.rules)
    debugImplementation(Deps.composeTestManifest)

    kaptAndroidTest(Deps.hiltAndroidCompiler)
    androidTestImplementation(Deps.hiltTesting)
}