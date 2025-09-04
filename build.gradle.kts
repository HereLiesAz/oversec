// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version = "2.2.0"
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("ru.tinkoff.gradle:jarjar:1.1.0")
        classpath(files("gradle-witness.jar"))

    }
}

plugins {
    id("com.android.application") version "8.7.0" apply false
    id("com.android.library") version "8.7.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("com.google.protobuf") version "0.8.7" apply false
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}