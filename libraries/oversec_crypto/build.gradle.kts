import ru.tinkoff.gradle.jarjar.JarJar

plugins {
    `com.android.library`
    `kotlin-android`
    `kotlin-android-extensions`
    id("com.google.protobuf")
    id("ru.tinkoff.gradle.jarjar")
    id("witness")
}

android {
    compileSdkVersion(36)
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(34)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            resValue("string", "prefill_password_fields", "lorem ipsum sic dolet") //spare inputing passwords while developing
        }
        getByName("release") {
            resValue("string", "prefill_password_fields", "")
        }
    }
}

configure<JarJar> {
    jarJarDependency.set("com.googlecode.jarjar:jarjar:1.3")
    rules.set(mapOf("commons-codec-1.10.jar" to "org.apache.commons.codec.** shadorg.apache.commons.codec.@1"))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                remove("javanano")
                create("java") {}
            }
        }
    }
}

dependencies {

    add("jarJar", "commons-codec:commons-codec:1.10")
    implementation(fileTree(mapOf("dir" to "./build/libs", "include" to listOf("*.jar")))) //to pick up the jarjared jar
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api("com.google.protobuf:protobuf-java:3.6.1")
    api("org.sufficientlysecure:openpgp-api:11.0") //TODO update ?
    api("com.afollestad.material-dialogs:core:3.3.0")
    api("com.github.rehacktive:waspdb:1.0")
    api("it.sephiroth.android.library.imagezoom:imagezoom:2.3.0")
    api("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("com.nulab-inc:zxcvbn:1.2.5")
    implementation("com.borjabravo:readmoretextview:2.1.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.legacy:legacy-support-v13:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.11.0")

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("org.mockito:mockito-core:3.12.4")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("com.google.dexmaker:dexmaker:1.2")
    androidTestImplementation("com.google.dexmaker:dexmaker-mockito:1.2")


    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("org.robolectric:robolectric:4.0.2")
    testImplementation("org.robolectric:shadows-supportv4:4.0.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
}
