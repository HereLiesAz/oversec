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
    api("com.github.open-keychain.open-keychain:openpgp-api:v5.7.1")
    api("com.github.rehacktive:waspdb:1.0")

    implementation("com.nulab-inc:zxcvbn:1.2.5")
    implementation("com.borjabravo:readmoretextview:2.1.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")
    implementation("org.bouncycastle:bcpg-jdk18on:1.78.1")
    implementation("org.bouncycastle:bcutil-jdk18on:1.78.1")


    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.legacy:legacy-support-v13:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.collection:collection-ktx:1.4.0")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("org.robolectric:robolectric:4.0.2")
    testImplementation("org.robolectric:shadows-supportv4:4.0.2")
}
