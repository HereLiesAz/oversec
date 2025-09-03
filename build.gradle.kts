import org.gradle.api.tasks.Delete

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version by extra("1.9.22")
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1")
        classpath("ru.tinkoff.gradle:jarjar:1.1.0")
        classpath("com.gladed.gradle.androidgitversion:gradle-android-git-version:0.4.14")
        classpath(files("gradle-witness.jar"))
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
