import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.gladed.androidgitversion") version "0.4.14"
}

data class AppTarget(
    val applicationId: String,
    val appName: String,
    val acsLabel: String,
    val targetApp: String,
    val website: String,
    val dlLink: String
)

val appTargets = mapOf(
    "oversec" to AppTarget(
        applicationId = "io.oversec.one",
        appName = "Oversec",
        acsLabel = "Oversec",
        targetApp = "",
        website = "https://www.oversec.io",
        dlLink = "http://dl.oversec.io"
    ),
    "intern" to AppTarget(
        applicationId = "io.oversec.one",
        appName = "INTERN",
        acsLabel = "INTERN",
        targetApp = "",
        website = "https://intern.oversec.io",
        dlLink = "http://dl-intern.oversec.io"
    )
)

val langs = listOf("","-de","-es","-fr","-pt","-ru","-tr","-zh-rCN","-it")

fun com.android.build.api.dsl.ApplicationProductFlavor.applyAppTgt(appTgt: AppTarget) {
    applicationId = appTgt.applicationId
    resValue("string", "app_name", appTgt.appName)
    resValue("string", "acs_label", appTgt.acsLabel)
    resValue("string", "targetapp", appTgt.targetApp)
    resValue("string", "website", appTgt.website)
    resValue("string", "dllink", appTgt.dlLink)
}


android {
    namespace = "io.oversec.one"
    applicationVariants.all {
        if (buildType.name == "release") {
            outputs.all {
                val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                output.outputFileName = output.outputFileName.replace(".apk", "-${versionCode}.apk")
            }
        }
    }

    applicationVariants.all {
        if (buildType.isMinifyEnabled) {
            assembleProvider.get().doLast {
                copy {
                    from(mappingFile)
                    into("${rootDir}/proguardMappings")
                    rename { "mapping-${name}-${versionCode}.txt" }
                }
            }
        }
    }


    compileSdkVersion(34)

    defaultConfig {
        applicationId = "io.oversec.one"
        minSdkVersion(26)
        targetSdkVersion(34)

        //hardcoded for F-droid bot
        versionCode = 1
        versionName = "1.0"

        buildConfigField("java.lang.Boolean", "IS_FRDOID", "new Boolean("+project.hasProperty("fdroid")+")")

    }

    buildTypes {

        getByName("release") {
            //debuggable=true //for IAB debugging
            //multiDexEnabled true  //for IAB debugging

            isMinifyEnabled = true
            proguardFiles("proguard-android-optimize-patched.txt", "proguard-rules.pro")
            buildConfigField("java.lang.Long", "X_BUILD_TIME", "new Long("+System.currentTimeMillis()+"L)")

        }

        getByName("debug") {
            multiDexEnabled = true
            buildConfigField("java.lang.Long", "X_BUILD_TIME", "new Long("+System.currentTimeMillis()+"L)")
        }
    }

    flavorDimensions.add("targetapp")

    productFlavors {

        create("oversec") {
            applyAppTgt(appTargets["oversec"]!!)
            buildConfigField("String", "GOOGLE_PLAY_PUBKEY", "\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoYCl8Ef7/5tRboyPcvcWqzVM1l3yTN28VepCzkTc1iHBJqlDd0d3k+ajZHKvyrvEK8JjjGqX/DkaESi7PNV03FptWI1HQP9P4J02Gm0nP/pwt6a4WAjbE2HSWlleBV/H66ZEQR6MhwKtI9rLFqPIxfNLJMDONYQ4/xIM6bMWNlvb59O0Yb9iEJOA+mJwMOZZoY9vadt5mUqi6bchjTXuOS3iCCrAixkhMIA8kpZSq40LI7ya3QSEnSRZJSRKccBagGxH12w3/5k/s1mnRIO7T/4cX9Kvi8+Q7pb0Zn0CpG0AEm078ON1+5dlJUHYMli0+J7JL2IL11txN/21FuhM4QIDAQAB\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_PROMO", "\"sku.oversec.one.fullversion.promo.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_A", "\"sku.oversec.one.fullversion.a.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_B", "\"sku.oversec.one.fullversion.b.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_C", "\"sku.oversec.one.fullversion.c.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_D", "\"sku.oversec.one.fullversion.d.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_E", "\"sku.oversec.one.fullversion.e.v0\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_F", "\"sku.oversec.one.fullversion.f.v1\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_G", "\"sku.oversec.one.fullversion.g.v1\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_H", "\"sku.oversec.one.fullversion.h.v1\"")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_I", "\"sku.oversec.one.fullversion.i.v1\"")

            buildConfigField("String", "DONATION_BTC", "\"16tan5fBNJ6n1QmVxwvvondyvuwgx1W6fE\"")
            buildConfigField("String", "DONATION_ETH", "\"0xE71c3b07dF8b13f3B99e537f541F91E40d09285a\"")
            buildConfigField("String", "DONATION_DASH", "\"5Jmj6oHucSZrQZ6JUiZu1sxHrX1adwfiJZURSRXJkDvZ6xAMrDg\"")
            buildConfigField("String", "DONATION_IOTA", "\"ZVFLHYRAJWGZDQVKJINUXDZQTXWR9GEMZSAIMNSDBWAYCQWHTFYFMILDVZORSZ9DEXKSLF9EKYSZHTAW9E9ROAZGLZ\"")
        }
        create("intern") {
            applyAppTgt(appTargets["intern"]!!)
            buildConfigField("String", "GOOGLE_PLAY_PUBKEY", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_PROMO", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_A", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_B", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_C", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_D", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_E", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_F", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_G", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_H", "null")
            buildConfigField("String", "GOOGLE_PLAY_SKU_FULLVERSION_I", "null")

            buildConfigField("String", "DONATION_BTC", "null")
            buildConfigField("String", "DONATION_ETH", "null")
            buildConfigField("String", "DONATION_DASH", "null")
            buildConfigField("String", "DONATION_IOTA", "null")
        }

    }

    val localProperties = Properties()
    val localPropertiesFile = project.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    val signingPropsFile = localProperties.getProperty("signing.properties")
    if (signingPropsFile != null && file(signingPropsFile).exists()) {
        val props = Properties()
        props.load(FileInputStream(file(signingPropsFile)))

        println("Loaded signing properties from $signingPropsFile")

        signingConfigs {
            create("release") {
                storeFile = file(props["keystore"].toString())
                storePassword = props["keystore.password"].toString()
                keyAlias = props["keyAlias"].toString()
                keyPassword = props["keyPassword"].toString()
            }
        }
        buildTypes.getByName("release").signingConfig = signingConfigs.getByName("release")
    } else {
        buildTypes.getByName("release").signingConfig = null
        println("No signing configuration provided!")
    }

    // NOTE: Lint is disabled because it slows down builds,
    // to enable it comment out the code at the bottom of this build.gradle
    lint {
        // Do not abort build if lint finds errors
        abortOnError = false
        checkAllWarnings = true
        htmlReport = true
        htmlOutput = file("lint-report.html")
    }


    packaging {
        resources.excludes.add("LICENSE.txt")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add(".readme")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    implementation(project(":libraries:oversec_crypto"))

    implementation("org.sufficientlysecure:html-textview:4.0")
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
    implementation("com.borjabravo:readmoretextview:2.1.0")

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.legacy:legacy-support-v13:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.11.0")

    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")
    implementation("com.github.skydoves:colorpicker-compose:1.1.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation(project(":libraries:oversec_crypto"))
}


// NOTE: This disables Lint!
tasks.whenTaskAdded {
    if (name.contains("lint")) {
        enabled = false
    }
}

// poor-man's preprocess to generate app-specific string resources
fun replaceEntities(ant: groovy.ant.AntBuilder, appTgt: AppTarget, sFile: java.io.File, tDir: java.io.File, tFile: java.io.File) {
    ant.withGroovyBuilder {
        "mkdir"("dir" to tDir)
        "copy"("file" to sFile.canonicalPath, "tofile" to tFile.canonicalPath)
        "replace"("file" to tFile.canonicalPath, "token" to "&appname;", "value" to appTgt.appName)
        "replace"("file" to tFile.canonicalPath, "token" to "&targetapp;", "value" to appTgt.targetApp)
        "replace"("file" to tFile.canonicalPath, "token" to "&acs_label;", "value" to appTgt.acsLabel)
        "replace"("file" to tFile.canonicalPath, "token" to "&website;", "value" to appTgt.website)
        "replace"("file" to tFile.canonicalPath, "token" to "&dllink;", "value" to appTgt.dlLink)
    }
}

tasks.register("preBuildMangleEntities") {
    doLast {
        val taskNames = project.gradle.startParameter.taskNames
        val taskName = if (taskNames.isNotEmpty()) taskNames[0] else ""
        var appTgtName = taskName.replace(":app:", "")
            .replace("assemble", "")
            .replace("test", "")
            .replace("generate", "")
            .replace("install", "")
            .replace("Debug", "")
            .replace("Release", "")
            .replace("Sources", "")
            .replace("UnitTest", "")
            .toLowerCase()
        if (appTgtName.isEmpty()) {
            appTgtName = "intern"
        }
        val appTgt = appTargets[appTgtName]!!
        val rootDir = project.projectDir.parentFile

        langs.forEach {
            val tDir = File(rootDir, "app/src/appTgtName/res/values$it")
            var sFile = File(rootDir, "app/src/appsec-common/res/values$it/strings.xml")
            var tFile = File(tDir, "strings_generated.xml")
            replaceEntities(ant, appTgt, sFile, tDir, tFile)

            sFile = File(rootDir, "libraries/oversec_crypto/src/main/res/values$it/strings.xml")
            tFile = File(tDir, "strings_crypto_generated.xml")
            replaceEntities(ant, appTgt, sFile, tDir, tFile)

            sFile = File(rootDir, "libraries/oversec_crypto/src/main/res/values$it/strings_core.xml")
            tFile = File(tDir, "strings_crypto_core_generated.xml")
            replaceEntities(ant, appTgt, sFile, tDir, tFile)
        }
    }
}

// tasks.whenTaskAdded {
//     if (name == "preBuild") {
//         dependsOn("preBuildMangleEntities")
//     }
// }

tasks.named("clean") {
    doFirst {
        ant.withGroovyBuilder {
            "delete"("dir" to "src", "includes" to "**/strings*_generated.xml")
        }
    }
}
