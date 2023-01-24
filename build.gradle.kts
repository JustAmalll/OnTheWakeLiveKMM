buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(AndroidDeps.kotlinGradlePlugin)
        classpath(AndroidDeps.serializationPlugin)
        classpath(AndroidDeps.androidBuildTools)
        classpath(AndroidDeps.hiltGradlePlugin)
        classpath(AndroidDeps.firebasePlugin)
        classpath(SharedDeps.sqlDelightGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}