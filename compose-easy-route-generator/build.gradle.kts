plugins {
    id(Dependencies.Plugins.javaLibrary)
    id(Dependencies.Plugins.kotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":compose-easy-route-core"))

    implementation(Dependencies.CodeGen.ksp)
    implementation(Dependencies.CodeGen.kotlinPoet)
    implementation(Dependencies.CodeGen.kotlinPoetKsp)
}