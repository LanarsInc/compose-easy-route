object Dependencies {
    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.8.0"
        const val appcompat = "androidx.appcompat:appcompat:1.4.2"
    }

    object Compose {
        const val composeVersion = "1.1.1"
        const val compilerVersion = "1.2.0"

        const val ui = "androidx.compose.ui:ui:$composeVersion"
        const val material = "androidx.compose.material:material:$composeVersion"
        const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
        const val activityCompose = "androidx.activity:activity-compose:1.5.0"
        const val navigation = "androidx.navigation:navigation-compose:2.5.0"

        const val uiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$composeVersion"

        const val junit = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    }

    object Lifecycle {
        private const val version = "2.5.0"
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
    }

    object Hilt {
        const val version = "2.42"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object CodeGen {
        const val ksp = "com.google.devtools.ksp:symbol-processing-api:1.7.0-1.0.6"

        const val kotlinPoetVersion = "1.12.0"
        const val kotlinPoet = "com.squareup:kotlinpoet:$kotlinPoetVersion"
        const val kotlinPoetKsp = "com.squareup:kotlinpoet-ksp:$kotlinPoetVersion"
    }

    object Plugins {
        const val androidApplication = "com.android.application"
        const val androidApplicationVersion = "7.2.2"

        const val androidLibrary = "com.android.library"
        const val androidLibraryVersion = "7.2.2"

        const val kotlinAndroid = "org.jetbrains.kotlin.android"
        const val kotlinVersion = "1.7.0"

        const val ksp = "com.google.devtools.ksp"
        const val kspVersion = "1.7.0-1.0.6"

        const val javaLibrary = "java-library"

        const val kotlinJvm = "org.jetbrains.kotlin.jvm"

        const val kotlinParcelize = "kotlin-parcelize"

        const val kotlinKapt = "kotlin-kapt"

        const val hiltAndroid = "dagger.hilt.android.plugin"

        const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:2.42"

        const val mavenPublish = "maven-publish"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val androidExtJunit = "androidx.test.ext:junit:1.1.3"
        const val androidEspresso = "androidx.test.espresso:espresso-core:3.4.0"
    }
}