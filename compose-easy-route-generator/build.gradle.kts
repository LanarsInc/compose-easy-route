plugins {
    id(Dependencies.Plugins.javaLibrary)
    id(Dependencies.Plugins.kotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":compose-easy-route-core"))

    implementation(Dependencies.CodeGen.ksp)
    implementation(Dependencies.CodeGen.kotlinPoet)
    implementation(Dependencies.CodeGen.kotlinPoetKsp)
}