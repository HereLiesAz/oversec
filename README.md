# The Oversec App

Oversec is a unique app for Android that adds secure text and image-encryption to any other Android app. Oversec is an overlay on top of other apps that transparently sends encrypted text, and shows decrypted text, in place.

## Current Status

**This project is currently in a transitional state.**

We are in the process of a significant migration from Java to Kotlin, and from traditional Android Views to Jetpack Compose. As a result, the codebase is a mix of both old and new technologies, and the project is **not currently building successfully**.

We are actively working on resolving the remaining build errors and completing the migration. We appreciate your patience and welcome any contributions to help us stabilize the project.

## Technology Stack

The current technology stack is a hybrid of the following:

*   **Languages:** Java, Kotlin
*   **UI:** Android Views, Jetpack Compose
*   **Build Tool:** Gradle

## Building the Project

To build the project, you can use the included Gradle wrapper.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/oversec/oversec.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd oversec
    ```
3.  **Build the app:**
    ```bash
    ./gradlew assembleDebug
    ```
    This will build the debug version of the app. The output APK can be found in `app/build/outputs/apk/`.

## Build Flavors

The project has two product flavors:

*   `oversec`: The standard, public version of the app.
*   `intern`: A version for internal testing and development.

You can build a specific flavor by using the following Gradle tasks:

*   `./gradlew assembleOversecDebug`
*   `./gradlew assembleInternDebug`

### More info on our [website](http://oversec.io)

[<img src="https://f-droid.org/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/io.oversec.one/)
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=io.oversec.one)

<a href="http://www.youtube.com/watch?feature=player_embedded&v=VHZ9dA5ELXE
" target="_blank">
Teaser Video
<br/>
<img src="http://img.youtube.com/vi/VHZ9dA5ELXE/0.jpg" 
alt="Oversec Teaser" width="240" height="180" border="10" />
</a>