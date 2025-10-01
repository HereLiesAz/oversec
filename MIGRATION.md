# Oversec Migration Guide

This document provides an overview of the ongoing migration of the Oversec app from its original Java and Android Views implementation to a more modern technology stack based on Kotlin and Jetpack Compose.

## 1. Migration from Java to Kotlin

The project is currently in a hybrid state, with a mix of both Java and Kotlin code. The long-term goal is to migrate the entire codebase to Kotlin, but this process is still ongoing.

### Current Status

*   **Core Logic:** A significant portion of the core logic has been migrated to Kotlin, but there are still some legacy Java components that need to be updated.
*   **UI:** The UI layer is also a mix of Java and Kotlin, with some of the older Android Views still implemented in Java.
*   **Build Scripts:** The build scripts have been updated to support both Java and Kotlin, but there are still some issues that need to be resolved.

## 2. Migration to Jetpack Compose

The user interface is being transitioned from traditional Android Views to Jetpack Compose. This is a major undertaking that will result in a more modern, declarative, and maintainable UI.

### Current Status

*   **Hybrid UI:** The UI is currently a mix of Android Views and Jetpack Compose. Some of the older screens are still implemented using Android Views, while newer screens are being built with Jetpack Compose.
*   **Component Migration:** We are in the process of migrating individual UI components to Jetpack Compose. This is a gradual process that will continue until the entire UI is implemented using Jetpack Compose.
*   **Build Issues:** The transition to Jetpack Compose has introduced some build issues that we are actively working to resolve.

## 3. Current Build Status

**The project is not currently building successfully.**

The primary reason for the build failures is a combination of missing dependencies and corrupted files that resulted from a problematic merge. We are actively working to resolve these issues, but it will take some time to stabilize the project.

### Known Issues

*   **Missing Dependencies:** The build is failing to find several required libraries, including `zxing`, `jwetherell`, `bouncycastle`, and `nulabinc`.
*   **Corrupted Files:** There are still a few corrupted files in the codebase that need to be cleaned up.
*   **Unresolved References:** There are still a number of unresolved references in the code that need to be fixed.

## 4. Next Steps

The following steps are required to stabilize the project and complete the migration:

1.  **Resolve Missing Dependencies:** Add the missing dependencies to the `app/build.gradle.kts` file.
2.  **Clean Up Corrupted Files:** Identify and fix the remaining corrupted files in the codebase.
3.  **Resolve Unresolved References:** Fix all the unresolved references in the code.
4.  **Complete the Migration to Kotlin:** Migrate the remaining Java code to Kotlin.
5.  **Complete the Migration to Jetpack Compose:** Migrate the remaining Android Views to Jetpack Compose.

We welcome any contributions to help us achieve these goals. If you are interested in helping, please feel free to open a pull request or contact us on our website.