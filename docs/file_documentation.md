# Project File Documentation

This document provides a brief but thorough description of what each non-ignored file in the project is supposed to do.

## TODO
- Document the `ui/theme` package and its files.
- Document the `ui/viewModel` package and its files.
- Document the `util` package and its files.
- Document the `view` package and its files.
- Document the `libraries/oversec_crypto` module in more detail, including its sub-packages and files.

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
###### app/src/main/aidl/com/android/vending/billing/IInAppBillingService.aidl
This AIDL file defines the interface for communicating with the Google Play in-app billing service. It specifies the methods for checking billing support, getting SKU details, launching the purchase flow, retrieving user purchases, and consuming purchases.
###### app/src/main/aidl/io/oversec/one/IZxcvbnService.aidl
This AIDL file defines the interface for the Zxcvbn service, which is used to calculate the entropy of a password and provide an estimated strength score.
###### app/src/main/aidl/io/oversec/one/ZxcvbnResult.aidl
This AIDL file defines the `ZxcvbnResult` parcelable, which is used to pass the results of a password strength calculation from the Zxcvbn service to the client.
###### app/src/main/java/io/oversec/one/AccessibilityServiceStatusChangedListener.java
This file defines an interface for listening to changes in the status of the accessibility service. It provides a single method, `onStatusChanged`, which is called when the service is started or stopped.
###### app/src/main/java/io/oversec/one/App.java
This file is the main application class for Oversec. It extends `MultiDexApplication` and is responsible for initializing the app's core components, such as the crash handler, logging configuration, in-app billing, and the app rating prompt.
###### app/src/main/java/io/oversec/one/Core.java
This is the core singleton class of the application, responsible for orchestrating most of the app's functionality. It manages the UI event loop, interacts with the accessibility service, handles encryption and decryption operations, manages the state of the overlays, and coordinates communication between different components.
###### app/src/main/java/io/oversec/one/CrashActivity.java
This activity is displayed when the app crashes. It shows the user the stack trace of the crash and provides a button to send a crash report to the developers via email.
###### app/src/main/java/io/oversec/one/CrashHandler.kt
This object sets up a global uncaught exception handler for the application. When a crash occurs, it captures the stack trace and logcat output, and then schedules an alarm to launch the `CrashActivity` after a short delay. This mechanism helps to prevent the app from getting stuck in a crash loop.
###### app/src/main/java/io/oversec/one/EncryptionCache.java
This class provides an in-memory cache for decrypted text. It uses an `LruCache` to store the results of decryption operations, which helps to improve performance by avoiding the need to re-decrypt the same text multiple times. The cache is cleared under various conditions, such as when the screen is turned off or when the user switches to a different app.
###### app/src/main/java/io/oversec/one/ObservableCore.java
This class extends `java.util.Observable` to provide a concrete implementation that can be used to notify observers of changes in the application's core state. It exposes the `setChanged` method, which is protected in the base class, so that it can be called from the `Core` class.
###### app/src/main/java/io/oversec/one/OversecIntentService.java
This `IntentService` handles various background tasks for the app, primarily related to notifications. It is responsible for building and updating the app's persistent notification, as well as handling actions triggered by the user from that notification, such as temporarily hiding or showing the overlay, opening the app's configuration, or activating the panic mode.
###### app/src/main/java/io/oversec/one/SecretCodeReceiver.java
This `BroadcastReceiver` listens for a secret code that can be entered into the phone's dialer. When the correct code is received, it disables the app's panic mode, which re-enables the launcher icon and restores the accessibility service to its normal monitoring state.
###### app/src/main/java/io/oversec/one/Share.java
This class provides a simple utility for sharing a predefined text string via the Android sharing mechanism. It creates an `ACTION_SEND` intent and launches a chooser to allow the user to select an app to share the text with.
###### app/src/main/java/io/oversec/one/UiThreadVars.java
This class is a container for variables that are only accessed from the UI thread. This helps to ensure thread safety by centralizing the management of these variables and providing a clear indication of which thread they should be accessed from. The class stores information such as the current package name, the last known screen orientation, and various caches related to encryption and UI state.
###### app/src/main/java/io/oversec/one/Util.java
This class provides a collection of static utility methods used throughout the application. These methods perform various tasks, such as retrieving the label for a given package name, enabling or disabling the app's launcher icon, checking if a dialer app is available, and checking for the presence of specific features.
##### app/src/main/java/io/oversec/one/acs/
This package contains the core components of the Oversec accessibility service, which is responsible for interacting with other apps and performing encryption and decryption operations on their behalf.
###### app/src/main/java/io/oversec/one/acs/DisplayNodeVisitor.java
This file defines an interface for visiting `NodeTextView` objects. It is used to traverse the tree of UI nodes and perform actions on each text view.
###### app/src/main/java/io/oversec/one/acs/InvalidRefreshException.java
This file defines an exception that is thrown when an invalid refresh operation is attempted on a UI node. This can occur if the node is no longer valid or has been detached from the view hierarchy.
###### app/src/main/java/io/oversec/one/acs/NodeAndFlag.java
This is a helper class used within the accessibility service to associate an `AccessibilityNodeInfo` with a specific action (`PerformNodeAction`) and state flags (like `cancelled`). It is used to manage operations that need to be performed on UI nodes.
###### app/src/main/java/io/oversec/one/acs/OversecAccessibilityService.java
This is the main accessibility service for Oversec. It is the core component that allows the app to read the screen content of other applications. It listens for accessibility events (like window changes, text changes, and scrolls), builds an internal tree representation of the UI nodes on the screen, and communicates this information to the `Core` class to trigger encryption, decryption, and overlay updates.
###### app/src/main/java/io/oversec/one/acs/OversecAccessibilityService_1.java
This class is a simple subclass of `OversecAccessibilityService`. It is declared in the AndroidManifest.xml as the accessibility service. This approach is sometimes used for compatibility reasons or to provide multiple accessibility service configurations.
###### app/src/main/java/io/oversec/one/acs/Tree.java
This class maintains an in-memory representation of the UI view hierarchy, built from `AccessibilityNodeInfo` objects. It is a key performance optimization, avoiding the need to repeatedly query the entire view tree. It includes a `TreeNode` inner class that caches properties of each UI node. A crucial feature is its ability to traverse the tree and reconstruct multi-line encrypted blocks (like PGP ASCII Armor or Base64) that may be split across multiple UI elements, a common scenario in WebViews.
###### app/src/main/java/io/oversec/one/acs/TreeNodeVisitor.java
This file defines an interface for visiting `Tree.TreeNode` objects. It is used to traverse the internal representation of the UI tree and perform actions on each node.
##### app/src/main/java/io/oversec/one/acs/util/
This package contains utility classes that provide helper functions and data structures for the accessibility service.
###### app/src/main/java/io/oversec/one/acs/util/AccessibilityNodeInfoRef.java
This class is a helper that simplifies traversal of the accessibility node tree. It wraps an `AccessibilityNodeInfo` object and manages its lifecycle, particularly the recycling of nodes to prevent memory leaks. It provides a fluent API for navigating to parent, child, and sibling nodes, making the traversal logic cleaner and less error-prone.
###### app/src/main/java/io/oversec/one/acs/util/AccessibilityNodeInfoUtils.java
This class provides a collection of static utility methods for working with `AccessibilityNodeInfo` objects. These methods simplify common tasks such as getting the text from a node, finding the root of a node tree, checking if a node's class matches a specific type, and recycling nodes to prevent memory leaks.
###### app/src/main/java/io/oversec/one/acs/util/AndroidIntegration.java
This class provides static utility methods for checking the status of the app's accessibility service. It includes a method to determine if the accessibility service is enabled in the system settings and is currently running.
###### app/src/main/java/io/oversec/one/acs/util/Bag.java
This class implements a simple `Bag` data structure, which is an unordered collection of objects that allows duplicates. It is used in the accessibility service to hold and manage collections of `NodeAndFlag` objects for pending UI operations.
###### app/src/main/java/io/oversec/one/acs/util/BasePackageMonitor.java
This abstract class provides a convenient way to monitor changes to application packages on the device. It is a `BroadcastReceiver` that listens for package-related intents (`ACTION_PACKAGE_ADDED`, `ACTION_PACKAGE_REMOVED`, `ACTION_PACKAGE_CHANGED`) and provides abstract methods that can be overridden by subclasses to handle these events.
###### app/src/main/java/io/oversec/one/acs/util/ClassLoadingManager.java
This singleton class manages the loading and caching of classes from other application packages. This is a critical component for the accessibility service, as it allows it to inspect and interact with the UI elements of other apps. It maintains a cache of loaded classes and uses a `PackageMonitor` to keep track of installed packages, clearing the cache when a package is removed.
###### app/src/main/java/io/oversec/one/acs/util/SimplePool.java
This class provides a simple, non-synchronized object pool. It is used to manage a collection of reusable objects, which can be acquired from the pool when needed and released back to the pool when they are no longer in use. This helps to reduce the overhead of object creation and garbage collection.
###### app/src/main/java/io/oversec/one/acs/util/SynchronizedPool.java
This class extends `SimplePool` to provide a thread-safe object pool. It uses a lock to synchronize access to the pool, ensuring that it can be safely used by multiple threads.
##### app/src/main/java/io/oversec/one/common/
This package contains common utility classes and constants that are used throughout the application.
###### app/src/main/java/io/oversec/one/common/Consts.kt
This object provides a simple mechanism for generating unique notification IDs. It uses a counter to ensure that each notification has a distinct ID, which is necessary to prevent them from overwriting each other.
###### app/src/main/java/io/oversec/one/common/MainPreferences.kt
This object provides a set of static methods for accessing the app's main shared preferences. It encapsulates the logic for reading and writing various settings, such as whether to allow screenshots, the behavior of the panic mode, and the secret dialer code for re-enabling the launcher.
##### app/src/main/java/io/oversec/one/crypto/
This package contains classes related to the app's cryptographic functionality, including content providers and services.
###### app/src/main/java/io/oversec/one/crypto/TemporaryContentProvider.kt
This `ContentProvider` is used to temporarily share data with other applications. It allows the app to expose data through a content URI for a limited time, after which the data is automatically deleted. This is particularly useful for sharing sensitive information, such as decrypted images, without permanently storing them in a publicly accessible location.
##### app/src/main/java/io/oversec/one/crypto/images/
This package contains classes related to image encryption and decryption.
###### app/src/main/java/io/oversec/one/crypto/images/xcoder/
This package contains classes that handle the encoding and decoding of data within images.
####### app/src/main/java/io/oversec/one/crypto/images/xcoder/ImageXCoderFacade.kt
This object acts as a facade for the various `ImageXCoder` implementations. It provides a single point of access for retrieving a list of all available image encoders/decoders.
###### app/src/main/java/io/oversec/one/crypto/images/xcoder/blackandwhite/
This package contains an implementation of `ImageXCoder` that encodes data into black and white images.
####### app/src/main/java/io/oversec/one/crypto/images/xcoder/blackandwhite/BlackAndWhiteImageXCoder.kt
This class implements the `ImageXCoder` interface to encode data into black and white images. It represents each bit of data as a block of pixels (either black or white), which is a surprisingly robust method that can survive JPEG compression.
##### app/src/main/java/io/oversec/one/crypto/sym/
This package contains classes related to symmetric key cryptography.
###### app/src/main/java/io/oversec/one/crypto/sym/KeystoreIntentService.kt
This `IntentService` is responsible for handling background tasks related to the symmetric key store. Its primary function is to clear all cached keys from the keystore and the encryption cache when triggered by an intent. It also builds and manages the notification that is displayed when there are cached keys.
##### app/src/main/java/io/oversec/one/db/
This package contains classes for managing the app's database, which is used to store various settings and preferences.
###### app/src/main/java/io/oversec/one/db/Db.java
This class is the central manager for the application's persistent storage. It uses an SQLite database to store detailed, per-application settings that control the appearance and behavior of the Oversec overlays. This includes UI customizations (like button positions and colors), default encryption parameters (padders and xcoders), and app-specific behaviors. It also uses `SharedPreferences` for global settings like the 'panic mode'. To improve performance, it implements a caching layer using `LruCache`.
###### app/src/main/java/io/oversec/one/db/IDecryptOverlayLayoutParamsChangedListener.java
This file defines an interface for listening to changes in the layout parameters of the decrypt overlay. This allows other components to be notified when the overlay's size or position is changed, so they can update their own layouts accordingly.
###### app/src/main/java/io/oversec/one/db/PadderDb.java
This class manages a database of 'padders' using the WaspDB library. Padders are chunks of text that can be added to encrypted messages to obscure their true length, making them more resistant to analysis. This class handles the creation, retrieval, updating, and deletion of these padder entries.
##### app/src/main/java/io/oversec/one/iab/
This package contains classes for handling in-app billing through the Google Play Store. It includes helper classes for managing the connection to the billing service, processing purchases, and handling responses.
###### app/src/main/java/io/oversec/one/iab/Base64.java
This class provides a set of static methods for encoding and decoding data in Base64 format. It includes support for both standard and web-safe Base64 encoding.
###### app/src/main/java/io/oversec/one/iab/Base64DecoderException.java
This class defines an exception that is thrown when an error occurs during Base64 decoding. This can happen if the input data is not valid Base64.
###### app/src/main/java/io/oversec/one/iab/FullVersionListener.java
This file defines an interface for listening to changes in the app's full version status. It provides a single method, `onFullVersion_MAIN_THREAD`, which is called on the main thread when the full version status is determined or updated.
###### app/src/main/java/io/oversec/one/iab/IabException.java
This class defines an exception that is thrown when an error occurs during an in-app billing operation. It encapsulates an `IabResult` object that provides more details about the error.
###### app/src/main/java/io/oversec/one/iab/IabHelper.java
This is a helper class that provides convenience methods for handling in-app billing. It simplifies the process of communicating with the Google Play billing service by providing a higher-level API for starting the setup process, launching purchase flows, querying the user's inventory, and consuming purchases. It also includes automatic signature verification.
###### app/src/main/java/io/oversec/one/iab/IabResult.java
This class represents the result of an in-app billing operation. It encapsulates a response code and a descriptive message, and provides convenience methods (`isSuccess()` and `isFailure()`) to easily check the outcome of the operation.
###### app/src/main/java/io/oversec/one/iab/IabUtil.java
This singleton class provides a higher-level utility for managing in-app billing. It builds upon `IabHelper` to offer a simplified interface for checking the full version status of the app, showing purchase dialogs, and handling the purchase flow. It also includes logic for 'nagging' the user to upgrade after a certain period of time.
###### app/src/main/java/io/oversec/one/iab/Inventory.java
This class represents the user's inventory of in-app items. It holds a mapping of SKU strings to their `SkuDetails` and `Purchase` information. This provides a local, cached representation of the user's purchases and the available products, which is populated by calls to `IabHelper.queryInventory`.
###### app/src/main/java/io/oversec/one/iab/Purchase.java
This class represents a single in-app purchase. It is instantiated with the JSON string and signature returned from the Google Play billing service, and it parses the JSON to provide convenient getter methods for accessing the details of the purchase, such as the order ID, SKU, purchase time, and purchase state.
###### app/src/main/java/io/oversec/one/iab/PurchaseActivity.java
This activity presents the user with a list of available in-app purchases. It retrieves the list of SKUs from the `IabUtil` class, displays them in a `RecyclerView`, and initiates the purchase flow when the user selects an item. It also handles the result of the purchase flow.
###### app/src/main/java/io/oversec/one/iab/Security.java
This class provides static methods for verifying the digital signature of in-app billing purchases. This is a critical security measure to ensure that the purchase data has not been tampered with and originates from Google Play.
###### app/src/main/java/io/oversec/one/iab/SkuDetails.java
This class represents the details of an in-app product available for purchase. It is instantiated with the JSON string returned from the Google Play billing service and parses it to provide convenient getter methods for accessing the product's SKU, price, title, and description.
##### app/src/main/java/io/oversec/one/ovl/
This package contains the various custom view classes that make up the Oversec overlays. These include the buttons, dialogs, and text views that are displayed on top of other applications.
###### app/src/main/java/io/oversec/one/ovl/AbstractOverlayButtonFreeView.java
This abstract class extends `AbstractOverlayButtonView` to provide a base for overlay buttons that can be freely moved around the screen. It stores separate X/Y coordinates for landscape and portrait orientations and handles the logic for updating the button's position when the orientation changes.
###### app/src/main/java/io/oversec/one/ovl/AbstractOverlayButtonInputView.java
This abstract class extends `AbstractOverlayButtonView` to provide a base for overlay buttons that are positioned relative to the currently focused input field. It maintains horizontal and vertical anchors (`ANCHORH`, `ANCHORV`) and deltas to keep the button at a consistent offset from the focused node. It also handles separate positioning for when the input method (keyboard) is in fullscreen mode.
###### app/src/main/java/io/oversec/one/ovl/AbstractOverlayButtonView.java
This abstract class is the base class for all overlay buttons. It extends `AbstractOverlayTouchableView` and provides the common functionality for handling touch events, including dragging, single taps, long taps, and double taps. It also manages the button's appearance and position on the screen.
###### app/src/main/java/io/oversec/one/ovl/AbstractOverlayDialogView.java
This abstract class provides a base for creating dialog-like views that are displayed as overlays. It defines the basic structure of a dialog with a message, OK, Cancel, and Neutral buttons, and a container for custom content. It also handles the positioning of the dialog, allowing it to be anchored to a specific view or centered on the screen.
###### app/src/main/java/io/oversec/one/ovl/AbstractOverlayTouchableView.java
This abstract class serves as a base for overlay views that need to be touchable. It configures the `WindowManager.LayoutParams` for the view to be non-modal and not focusable, which allows touch events to pass through to the views underneath. Subclasses are responsible for defining the view's width and height.
###### app/src/main/java/io/oversec/one/ovl/IDragListener.java
This file defines an interface for listening to drag events on overlay views. It provides a single method, `onDrag`, which is called when the user drags a view, providing the new X and Y coordinates.
###### app/src/main/java/io/oversec/one/ovl/NodeGroupLayout.java
This class is a ViewGroup that acts as a container for other `NodeView`s. It is used to recursively build a view hierarchy that mirrors the structure of the `Tree.TreeNode` objects. It manages the layout of its children and updates them when the underlying node tree changes.
###### app/src/main/java/io/oversec/one/ovl/NodeTextView.java
This class is a custom `TextView` that represents a single text-based node (`TreeNode`) within the overlay. It is responsible for displaying the content of a UI element from the scraped application. Its key function is to manage the decryption of its text content. It interacts with the `EncryptionCache` and `CryptoHandlerFacade` to perform decryption asynchronously and updates its appearance (text, color, icons) based on the decryption result (e.g., success, failure, user interaction required).
###### app/src/main/java/io/oversec/one/ovl/NodeView.java
This file defines the common interface for views that represent a node from the accessibility tree within the overlay. It specifies methods for updating the view with new node data, checking if the view's type matches a given node, and managing the view's lifecycle (recycling).
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonCameraView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'camera' button in the overlay. When tapped, it triggers the `onButtonCameraSingleTap` method in the `Core` class, which in turn launches an activity to take a photo for encryption.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonComposeView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'compose' button in the overlay. When tapped, it triggers the `onButtonComposeSingleTap` method in the `Core` class, which launches a dedicated activity for composing and encrypting a new message.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonConfigView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'config' button in the overlay. When tapped, it triggers the `onButtonConfigSingleTap` method in the `Core` class, which opens the app's configuration screen for the current application.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonDecryptView.java
This class is a concrete implementation of `AbstractOverlayButtonInputView` that represents the 'decrypt' button. It is positioned relative to the focused input field and, when tapped, triggers the `onButtonDecryptSingleTap` method in the `Core` class. The icon of the button changes based on whether the text in the focused field is encrypted or not (e.g., showing a 'backspace' icon to clear decrypted text or an 'open lock' to decrypt).
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonEncryptView.java
This class is a concrete implementation of `AbstractOverlayButtonInputView` that represents the 'encrypt' button. It is positioned relative to the focused input field. A single tap triggers the encryption process, while a long tap typically opens the encryption parameters screen. The button is context-aware; it only appears when there is unencrypted text in the focused field and hides itself otherwise.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonHideView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'hide' button. A single tap on this button temporarily hides all the Oversec overlays for the current application. A long tap triggers the 'panic' mode, and a double tap opens the app's configuration.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonInfomodeView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'info mode' button. Tapping this button toggles the info mode, which displays detailed information about the encrypted text found on the screen. The button's icon changes to indicate whether info mode is active or not, and it hides itself when there are no encrypted nodes visible.
###### app/src/main/java/io/oversec/one/ovl/OverlayButtonUpgradeView.java
This class is a concrete implementation of `AbstractOverlayButtonFreeView` that represents the 'upgrade' button. This button is shown to users of the free version of the app and, when tapped, initiates the in-app purchase flow to upgrade to the full version. It appears after a delay, which is calculated based on the app's installation age.
###### app/src/main/java/io/oversec/one/ovl/OverlayDecryptView.java
This is the main view for the decryption overlay. It is a full-screen, transparent view that sits on top of other applications. It contains a hierarchy of `NodeGroupLayout` and `NodeTextView` objects that mirrors the structure of the accessibility nodes on the screen. It is responsible for displaying the decrypted text and any associated UI elements (like status icons).
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogCorruptedEncodingView.java
This class is a dialog view that is displayed as an overlay when the app encounters encrypted text that appears to be corrupted or invalid. It provides the user with options to clear the input field or get more information about the error.
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogInsufficientPaddingView.java
This class is a dialog view that is displayed as an overlay when there is not enough padding to securely encrypt a message. It informs the user about the issue and provides options to dismiss the dialog or get more information.
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogToast.java
This class implements a custom, toast-like notification that is displayed as an overlay. It is a simple dialog with a message and an icon, and it is used to show brief, non-intrusive messages to the user. It does not have any buttons and is typically dismissed automatically after a short duration.
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogTooltip.java
This class is a specialized dialog view that is used to display tooltips and other contextual help messages to the user. It is typically anchored to a specific view and can be dismissed by the user, with an option to permanently hide the tooltip by tapping a 'Got it' button.
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogUpgrade.java
This class is a dialog view that is displayed as an overlay to prompt the user to upgrade to the full version of the app. It provides an 'Upgrade' button that launches the in-app purchase flow and a 'Cancel' button to dismiss the dialog.
###### app/src/main/java/io/oversec/one/ovl/OverlayDialogUserInteractionRequired.java
This class is a dialog view that is displayed as an overlay when a decryption operation requires user interaction (e.g., entering a password). It informs the user that their action is needed and provides 'OK' and 'Cancel' buttons. Tapping 'OK' proceeds with the user interaction, while 'Cancel' aborts the decryption.
###### app/src/main/java/io/oversec/one/ovl/OverlayEditTextBorderView.java
This class is a view that displays a colored border around the currently focused editable text field. The color of the border changes to provide visual feedback to the user, for example, indicating whether there is sufficient padding for encryption or if the encrypted data is corrupted.
###### app/src/main/java/io/oversec/one/ovl/OverlayInfoButtonView.java
This class is a small, floating button that appears next to a block of encrypted text when the app is in 'info mode'. When the user taps this button, it launches the `EncryptionInfoActivity` to display detailed information about the encrypted text, such as the encryption algorithm, key ID, and signature status.
###### app/src/main/java/io/oversec/one/ovl/OverlayOutsideTouchView.java
This class is a small, invisible overlay view that is used to detect touch events that occur outside of the other overlay views. When a touch is detected outside of its bounds, it triggers a preemptive refresh of the accessibility tree, which helps to keep the overlays in sync with the underlying application.
###### app/src/main/java/io/oversec/one/ovl/OverlayView.java
This abstract class is the base class for all overlay views in the application. It extends `AbsoluteLayout` and handles the common logic for creating and managing the view's window, including setting the `WindowManager.LayoutParams`. It also provides a mechanism for controlling the visibility of the overlay.
###### app/src/main/java/io/oversec/one/ovl/SampleNodeTextView.java
This class is a custom `TextView` that is used in the app's settings screens to display a preview of how the decryption overlay will look. It implements the `IDecryptOverlayLayoutParamsChangedListener` interface, so it automatically updates its appearance whenever the user changes the overlay's settings (e.g., color, text size, corner radius).
##### app/src/main/java/io/oversec/one/ui/
This package contains the user interface components of the application, including activities, fragments, and custom views.
###### app/src/main/java/io/oversec/one/ui/screen/
This package contains the composable screens for the application's user interface.
####### app/src/main/java/io/oversec/one/ui/screen/AboutScreen.kt
This file defines the composable for the 'About' screen of the application. It uses a tabbed layout to display different sections: 'About', 'Changelog', and a third tab that shows either 'Purchases' or 'Donations' depending on whether the app was installed from the Google Play Store. The content for the 'About' and 'Changelog' tabs is loaded from markdown files in the app's resources.
####### app/src/main/java/io/oversec/one/ui/screen/AppConfigScreen.kt
This file defines the composable for the main configuration screen for a specific application. It uses a tabbed layout to organize the various settings into categories such as 'Stuff', 'Colors', 'Expert', and 'Padders'. It also includes a top app bar with navigation, help, and a menu for additional actions.
####### app/src/main/java/io/oversec/one/ui/screen/AppInfo.kt
This file defines a simple data class that holds information about an installed application, including its name, package name, icon, and whether it is enabled for Oversec.
####### app/src/main/java/io/oversec/one/ui/screen/EncryptionParamsScreen.kt
This file defines the composable for the encryption parameters screen. It uses a tabbed layout to allow the user to select and configure the parameters for the different encryption methods: PGP, Symmetric, and Simple Symmetric.
####### app/src/main/java/io/oversec/one/ui/screen/MainSettingsScreen.kt
This file defines the composable for the main 'Stuff' tab within the app's configuration screen. It displays a list of all installed applications on the device, allowing the user to enable or disable Oversec for each app individually. It also includes a section at the top for important status information and actions, such as enabling the accessibility service, disabling panic mode, and accessing help.
####### app/src/main/java/io/oversec/one/ui/screen/PadderDetailScreen.kt
This file defines the composable for the screen that allows users to create and edit 'padders'. A padder is a block of text that can be used to add random padding to encrypted messages, which helps to obscure the true length of the message. This screen provides text fields for the padder's name and content, and a 'Save' button to persist the changes.
####### app/src/main/java/io/oversec/one/ui/screen/PaddersScreen.kt
This file defines the composable for the 'Padders' screen. It displays a list of the user's custom padders, which are used to add random text to encrypted messages. The screen includes a floating action button to add new padders and allows the user to tap on an existing padder to edit or delete it.
####### app/src/main/java/io/oversec/one/ui/screen/TweaksScreen.kt
This file defines the composables for the 'Colors' and 'Expert' tabs within the app's configuration screen. The 'Colors' tab allows the user to customize the appearance of the decryption overlay, including the font size, corner radius, padding, and colors. The 'Expert' tab provides a list of advanced settings that can be enabled or disabled to fine-tune the app's behavior.
###### app/src/main/java/io/oversec/one/ui/theme/
This package contains the theming information for the application's user interface, including colors, shapes, and typography.
