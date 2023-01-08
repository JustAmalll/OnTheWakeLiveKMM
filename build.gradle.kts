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
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}