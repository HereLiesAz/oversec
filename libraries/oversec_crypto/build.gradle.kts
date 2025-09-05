plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
}

android {
    compileSdkVersion(34)
    namespace = "io.oversec.one.crypto"

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(34)
    }
    buildTypes {
        getByName("debug") {
            resValue("string", "prefill_password_fields", "lorem ipsum sic dolet") //spare inputing passwords while developing
        }
        getByName("release") {
            resValue("string", "prefill_password_fields", "")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                // remove("javanano") // This seems to cause issues with newer gradle versions
                create("java") {}
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api("com.google.protobuf:protobuf-java:3.6.1")
    api("org.sufficientlysecure:openpgp-api:11.0") //TODO update ?
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

    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.9.0")

    //androidTestImplementation("junit:junit:4.13.2")
    //androidTestImplementation("org.mockito:mockito-core:3.12.4")
    //androidTestImplementation("androidx.test:runner:1.5.2")
    //androidTestImplementation("com.google.dexmaker:dexmaker:1.2")
    //androidTestImplementation("com.google.dexmaker:dexmaker-mockito:1.2")


    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("org.robolectric:robolectric:4.0.2")
    testImplementation("org.robolectric:shadows-supportv4:4.0.2")
}
