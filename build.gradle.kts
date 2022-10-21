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
    id(Dependencies.Plugins.mavenPublish)
}

allprojects {
    group = "com.lanars"
    version = "1.2.4"
}

subprojects {
    apply(plugin = Dependencies.Plugins.mavenPublish)

    project.afterEvaluate {
        if (!plugins.hasPlugin("android")) {
            publishing {
                publications {
                    register<MavenPublication>("release") {
                        artifactId = project.name

                        afterEvaluate {
                            if (plugins.hasPlugin("java")) {
                                from(components["java"])
                            } else if (plugins.hasPlugin("android-library")) {
                                from(components["release"])
                            }
                        }
                    }
                }
            }
        }
    }
}
