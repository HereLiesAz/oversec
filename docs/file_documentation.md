# Project File Documentation

This document provides a brief but thorough description of what each non-ignored file in the project is supposed to do.

## LICENSE
This file contains the GNU General Public License version 3, which governs the use of this software.
## MIGRATION.md
This file provides a guide for migrating the Oversec app from Java and Android Views to Kotlin and Jetpack Compose. It outlines the current status of the migration, known issues, and the next steps to complete the process.
## README.md
This file provides an overview of the Oversec app, its current status, technology stack, and instructions for building the project. It also includes links to the app's website and app store listings.
## build.gradle.kts
This is the top-level Gradle build file for the project. It defines the plugins and dependencies that are common to all sub-projects and modules.
## gradle.properties
This file contains project-wide Gradle settings, such as JVM arguments and other build options.
## gradlew and gradlew.bat
These are the Gradle wrapper scripts for Unix-like systems (`gradlew`) and Windows (`gradlew.bat`). They allow the project to be built with a specific version of Gradle without requiring it to be installed on the system.
## hunspell.custom
This file contains a custom dictionary for the Hunspell spell checker, which is used to check the spelling of words in the project's source code and documentation.
## settings.gradle.kts
This file defines the project's structure and includes the sub-projects that are part of the build. It also configures the repositories that are used to resolve dependencies.
## app/
This directory contains the main application module for the Oversec app.
### app/build.gradle.kts
This is the Gradle build file for the main application module. It defines the app's dependencies, build types, product flavors, and other build-related settings.
### app/proguard-android-optimize-patched.txt and app/proguard-rules.pro
These files contain the Proguard configuration for the app. Proguard is a tool that shrinks, optimizes, and obfuscates the code, which helps to reduce the size of the APK and protect it from reverse engineering.
## app/src/
This directory contains the source code for the Oversec app, which is organized into three subdirectories: `androidTest`, `appsec-common`, and `main`.
### app/src/androidTest/
This directory contains the Android instrumentation tests for the app, which are used to test the app's UI and functionality on an Android device or emulator.
#### app/src/androidTest/java/io/oversec/one/ApplicationTest.java
This file contains a basic application test case that can be used as a starting point for writing more comprehensive tests.
### app/src/appsec-common/
This directory contains common resources that are used by the app's security features, such as string translations and other localized content.
#### app/src/appsec-common/res/raw/
This directory contains raw resource files, such as markdown files for the 'about' and 'changelog' screens.
#### app/src/appsec-common/res/values/
This directory and its subdirectories (e.g., `values-de`, `values-es`) contain the localized string resources for the app. Each subdirectory contains a `strings.xml` file with the translated strings for a specific language.
### app/src/main/
This directory contains the main source code and resources for the Oversec app.
#### app/src/main/AndroidManifest.xml
This file is the Android manifest for the app, which declares the app's components, permissions, and other essential information that the Android system needs to run the app.
#### app/src/main/aidl/
This directory contains the Android Interface Definition Language (AIDL) files for the app. AIDL is used to define the programming interface for services that are exposed to other applications.
#### app/src/main/java/ and app/src/main/kotlin/
These directories contain the Java and Kotlin source code for the app, respectively. The code is organized into packages that correspond to the app's features and functionality.
#### app/src/main/res/
This directory contains all the non-code resources for the app, such as layouts, drawables, strings, and other assets. The resources are organized into subdirectories based on their type and configuration.
##### app/src/main/res/anim/
This directory contains XML files that define animations for the app.
##### app/src/main/res/drawable/
This directory and its density-specific subdirectories (e.g., `drawable-hdpi`, `drawable-xhdpi`) contain the drawable resources for the app, such as images and icons.
##### app/src/main/res/layout/
This directory contains the XML layout files that define the user interface for the app's activities and fragments.
##### app/src/main/res/menu/
This directory contains the XML files that define the menus for the app's activities.
##### app/src/main/res/mipmap/
This directory and its density-specific subdirectories (e.g., `mipmap-hdpi`, `mipmap-xhdpi`) contain the app's launcher icons.
##### app/src/main/res/raw/
This directory contains raw resource files that are compiled into the app as-is, such as the 'about' and 'changelog' markdown files.
##### app/src/main/res/values/
This directory and its locale-specific subdirectories (e.g., `values-de`, `values-es`) contain XML files that define simple values, such as strings, colors, dimensions, and styles.
##### app/src/main/res/xml/
This directory contains arbitrary XML files that can be read at runtime, such as the accessibility service configuration and the app's preferences.
## fastlane/
This directory contains the Fastlane configuration for the app. Fastlane is a tool that automates the process of building, testing, and releasing mobile apps.
### fastlane/metadata/android/
This directory contains the metadata for the app's Google Play Store listing, such as the app's description, summary, and screenshots. The metadata is organized into subdirectories for each supported language.
## gradle/
This directory contains the Gradle wrapper files, which allow the project to be built with a specific version of Gradle without requiring it to be installed on the system.
## libraries/
This directory contains reusable library modules that are used by the main application.
### libraries/oversec_crypto/
This directory contains the `oversec_crypto` library, which provides the core cryptographic functionality for the Oversec app.
#### libraries/oversec_crypto/build.gradle.kts
This is the Gradle build file for the `oversec_crypto` library. It defines the library's dependencies and other build-related settings.
#### libraries/oversec_crypto/src/
This directory contains the source code for the `oversec_crypto` library, which is organized into three subdirectories: `main`, `test`, and `proto`.
##### libraries/oversec_crypto/src/main/
This directory contains the main source code and resources for the `oversec_crypto` library.
###### libraries/oversec_crypto/src/main/AndroidManifest.xml
This file is the Android manifest for the `oversec_crypto` library.
###### libraries/oversec_crypto/src/main/assets/
This directory contains asset files that are compiled into the library as-is, such as the Diceware wordlists.
###### libraries/oversec_crypto/src/main/java/
This directory contains the Java source code for the library.
###### libraries/oversec_crypto/src/main/proto/
This directory contains the Protocol Buffer (Protobuf) definition files for the library. Protobuf is a language-neutral, platform-neutral, extensible mechanism for serializing structured data.
##### libraries/oversec_crypto/src/test/
This directory contains the unit tests for the `oversec_crypto` library.
