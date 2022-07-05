object Dependencies {
    object Android {
        const val coreKtx = "androidx.core:core-ktx:1.8.0"
    }

    object Compose {
        const val composeVersion = "1.1.1"
        const val compilerVersion = "1.2.0"

        const val ui = "androidx.compose.ui:ui:$composeVersion"
        const val material = "androidx.compose.material:material:$composeVersion"
        const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
        const val activityCompose = "androidx.activity:activity-compose:1.5.0"

        const val uiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$composeVersion"

        const val junit = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    }

    object Lifecycle {
        private const val version = "2.5.0"
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
    }

    object Plugins {
        const val androidApplication = "com.android.application"
        const val androidApplicationVersion = "7.2.1"

        const val androidLibrary = "com.android.library"
        const val androidLibraryVersion = "7.2.1"

        const val kotlinAndroid = "org.jetbrains.kotlin.android"
        const val kotlinAndroidVersion = "1.7.0"

        const val ksp = "com.google.devtools.ksp"
        const val kspVersion = "1.7.0-1.0.6"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val androidExtJunit = "androidx.test.ext:junit:1.1.3"
        const val androidEspresso = "androidx.test.espresso:espresso-core:3.4.0"
    }
}