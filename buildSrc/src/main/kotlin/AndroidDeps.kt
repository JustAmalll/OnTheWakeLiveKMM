object AndroidDeps {

    // COMPOSE
    private const val activityComposeVersion = "1.6.1"
    const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"

    const val composeVersion = "1.4.0-alpha02"
    const val composeUi = "androidx.compose.ui:ui:$composeVersion"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
    const val composeFoundation = "androidx.compose.foundation:foundation:$composeVersion"
    const val composeIconsExtended = "androidx.compose.material:material-icons-extended:$composeVersion"

    // Material 3
    const val composeMaterial = "androidx.compose.material3:material3:1.1.0-alpha03"

    // Tabs
    const val tabs = "com.google.accompanist:accompanist-pager:0.13.0"
    const val tabIndicators = "com.google.accompanist:accompanist-pager-indicators:0.13.0"

    // Serialization
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"

    // Accompanist System UI Controller
    const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:0.17.0"

    private const val composeNavigationVersion = "2.5.3"
    const val composeNavigation = "androidx.navigation:navigation-compose:$composeNavigationVersion"

    private const val coilComposeVersion = "2.1.0"
    const val coilCompose = "io.coil-kt:coil-compose:$coilComposeVersion"

    // HILT
    private const val hiltVersion = "2.42"
    private const val hiltCompilerVersion = "1.0.0"
    const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:$hiltCompilerVersion"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:$hiltCompilerVersion"

    // KTOR
    private const val ktorVersion = "2.2.1"
    const val ktorCIO = "io.ktor:ktor-client-cio:$ktorVersion"

    // Firebase
    const val firebaseBOM = "com.google.firebase:firebase-bom:31.0.2"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"

    // GRADLE PLUGINS
    const val kotlinVersion = "1.7.21"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

    private const val gradleVersion = "7.2.2"
    const val androidBuildTools = "com.android.tools.build:gradle:$gradleVersion"

    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"

    const val firebasePlugin = "com.google.gms:google-services:4.3.14"

    const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:1.7.21"
}