plugins {
    id(Dependencies.Plugins.androidApplication)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp) version Dependencies.Plugins.kspVersion
    id(Dependencies.Plugins.kotlinParcelize)
    id(Dependencies.Plugins.kotlinKapt)
    id(Dependencies.Plugins.hiltAndroid)
}

android {
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "com.gsrocks.compose_easy_route.sample"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(":compose-easy-route"))
    ksp(project(":compose-easy-route-generator"))

    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.activityCompose)
    debugImplementation(Dependencies.Compose.preview)
    debugImplementation(Dependencies.Compose.uiTooling)
    debugImplementation(Dependencies.Compose.uiTestManifest)

    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Lifecycle.lifecycleKtx)

    implementation(Dependencies.Hilt.hiltAndroid)
    kapt(Dependencies.Hilt.hiltCompiler)
    implementation(Dependencies.Hilt.navigationCompose)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.androidExtJunit)
    androidTestImplementation(Dependencies.Test.androidEspresso)
    androidTestImplementation(Dependencies.Compose.junit)
}