pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
    }
}
rootProject.name = "ComposeEasyRoute"
include(":sample")
include(":compose-easy-route")
include(":compose-easy-route-core")
include(":compose-easy-route-generator")
