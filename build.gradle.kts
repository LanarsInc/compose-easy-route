buildscript {
    dependencies {
        classpath(Dependencies.Plugins.hiltAndroidGradlePlugin)
    }
}

plugins {
    id(Dependencies.Plugins.androidApplication) version Dependencies.Plugins.androidApplicationVersion apply false
    id(Dependencies.Plugins.androidLibrary) version Dependencies.Plugins.androidLibraryVersion apply false
    id(Dependencies.Plugins.kotlinAndroid) version Dependencies.Plugins.kotlinVersion apply false
    id(Dependencies.Plugins.kotlinJvm) version Dependencies.Plugins.kotlinVersion apply false
}
