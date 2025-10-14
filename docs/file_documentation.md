# Project File Documentation

## `LICENSE`

This file contains the GNU General Public License version 3, under which this project is licensed.

## `MIGRATION.md`

This document outlines the project's ongoing migration from Java and Android Views to Kotlin and Jetpack Compose. It details the current status of the migration, known issues with the build, and the next steps to complete the transition.

## `README.md`

This is the main README file for the Oversec app. It provides a general overview of the project, its current status, and instructions on how to build it.

## `app/`

This directory contains all the source code and resources for the main Oversec Android application.

## `fastlane/`

This directory contains configuration files for Fastlane, a tool for automating the release process of the app.

## `gradle/`

This directory contains the Gradle wrapper, which allows the project to be built with a specific version of Gradle without requiring it to be installed on the system.

## `gradle.properties`

This file contains project-wide Gradle settings, such as JVM arguments and AndroidX configuration.

## `gradlew*` and `gradlew.bat`

These are the Gradle wrapper scripts for Unix-based systems (`gradlew`) and Windows (`gradlew.bat`). They allow the project to be built with a specific version of Gradle without requiring it to be installed on the system.

## `hunspell.custom`

This file contains a custom dictionary for Hunspell, a spell checker. It includes project-specific terms that should not be flagged as misspellings.

## `libraries/`

This directory contains local library modules that are used by the main application.

## `settings.gradle.kts`

This file defines the project structure and includes the modules that are part of the build.

## `app/build.gradle.kts`

This is the main build script for the Oversec application. It defines the application's dependencies, build types, product flavors, and other build-related configurations.

## `app/proguard-android-optimize-patched.txt`

This file is a ProGuard configuration file that has been patched to include optimizations. It is used to shrink, obfuscate, and optimize the application's code.

## `app/proguard-rules.pro`

This file contains additional ProGuard rules that are specific to the application. It is used to keep certain classes and methods from being obfuscated or removed.

## `app/src/`

This directory is the root of the application's source code.

## `app/src/androidTest/`

This directory contains Android instrumentation tests for the application.

## `app/src/appsec-common/`

This directory contains common resources that are shared across different build flavors of the application.

## `app/src/main/`

This directory contains the main source code and resources for the application.

## `app/src/androidTest/java/`

This directory contains the Java source code for the Android instrumentation tests.

## `app/src/androidTest/java/io/`

This is the root package for the application's test code.

## `app/src/androidTest/java/io/oversec/`

This is the package containing the test code for the Oversec application.

## `app/src/androidTest/java/io/oversec/one/`

This package contains the application-specific test code.

## `app/src/androidTest/java/io/oversec/one/ApplicationTest.java`

This file contains a basic application test case.

## `app/src/appsec-common/res/`

This directory contains the common resources for the application.

## `app/src/appsec-common/res/raw/`

This directory contains raw resource files, such as markdown files for the about and changelog screens.

## `app/src/appsec-common/res/values/`

This directory contains the default resource values, such as strings and dimensions.

## `app/src/appsec-common/res/values-de/`

This directory contains German translations for the application's strings.

## `app/src/appsec-common/res/values-es/`

This directory contains Spanish translations for the application's strings.

## `app/src/appsec-common/res/values-fr/`

This directory contains French translations for the application's strings.

## `app/src/appsec-common/res/values-it/`

This directory contains Italian translations for the application's strings.

## `app/src/appsec-common/res/values-pt/`

This directory contains Portuguese translations for the application's strings.

## `app/src/appsec-common/res/values-ru/`

This directory contains Russian translations for the application's strings.

## `app/src/appsec-common/res/values-tr/`

This directory contains Turkish translations for the application's strings.

## `app/src/appsec-common/res/values-zh-rCN/`

This directory contains Chinese (Simplified) translations for the application's strings.

## `app/src/appsec-common/res/raw/about.md`

This file contains the content for the "About" screen of the application, including links to the project's website, privacy policy, and open-source libraries.

## `app/src/appsec-common/res/raw/changelog.md`

This file contains the changelog for the application, which is displayed on the "Changelog" screen.

## `app/src/appsec-common/res/values*/strings.xml`

These files contain the localized string resources for the application. Each file corresponds to a specific language, as indicated by the directory name.

## `app/src/main/AndroidManifest.xml`

This is the main manifest file for the application. It declares the application's components, such as activities, services, and broadcast receivers, as well as the permissions it requires.

## `app/src/main/aidl/`

This directory contains Android Interface Definition Language (AIDL) files, which are used to define the programming interface for inter-process communication (IPC).

## `app/src/main/java/`

This directory contains the Java source code for the application.

## `app/src/main/kotlin/`

This directory contains the Kotlin source code for the application.

## `app/src/main/res/`

This directory contains the resources for the application, such as layouts, drawables, and strings.

## `app/src/main/aidl/com/`

This is the root package for the AIDL files.

## `app/src/main/aidl/io/`

This is the root package for the application's AIDL files.

## `app/src/main/aidl/com/android/`

This package contains AIDL files related to Android services.

## `app/src/main/aidl/com/android/vending/`

This package contains AIDL files related to the Google Play Store.

## `app/src/main/aidl/com/android/vending/billing/`

This package contains AIDL files related to in-app billing.

## `app/src/main/aidl/com/android/vending/billing/IInAppBillingService.aidl`

This file defines the interface for the in-app billing service.

## `app/src/main/aidl/io/oversec/`

This is the package containing the AIDL files for the Oversec application.

## `app/src/main/aidl/io/oversec/one/`

This package contains the application-specific AIDL files.

## `app/src/main/aidl/io/oversec/one/IZxcvbnService.aidl`

This file defines the interface for the Zxcvbn password strength estimation service.

## `app/src/main/aidl/io/oversec/one/ZxcvbnResult.aidl`

This file defines a parcelable class that represents the result of a Zxcvbn password strength estimation.

## `app/src/main/java/io/`

This is the root package for the application's Java source code.

## `app/src/main/java/io/oversec/`

This is the package containing the Java source code for the Oversec application.

## `app/src/main/java/io/oversec/one/`

This package contains the application-specific Java source code.

## `app/src/main/java/io/oversec/one/AccessibilityServiceStatusChangedListener.java`

This file defines an interface for listening to changes in the status of the accessibility service.

## `app/src/main/java/io/oversec/one/App.java`

This is the main application class. It initializes various components of the application, such as the crash handler, logging, and in-app billing.

## `app/src/main/java/io/oversec/one/Core.java`

This is the core class of the application. It is a singleton that manages the application's main functionality, including the accessibility service, overlays, and encryption.

## `app/src/main/java/io/oversec/one/CrashActivity.java`

This activity is displayed when the application crashes. It provides the user with the option to send a crash report to the developers.

## `app/src/main/java/io/oversec/one/CrashHandler.kt`

This file contains a crash handler that catches uncaught exceptions and displays the `CrashActivity`.

## `app/src/main/java/io/oversec/one/EncryptionCache.java`

This file contains a cache for encrypted and decrypted text. It is used to avoid having to re-decrypt text that has already been decrypted.

## `app/src/main/java/io/oversec/one/ObservableCore.java`

This file contains an observable version of the `Core` class. It is used to notify observers when the state of the `Core` class changes.

## `app/src/main/java/io/oversec/one/OversecIntentService.java`

This service handles various actions that can be triggered from the application's notification.

## `app/src/main/java/io/oversec/one/SecretCodeReceiver.java`

This broadcast receiver listens for a secret code that is dialed in the phone app. When the correct code is dialed, it disables panic mode.

## `app/src/main/java/io/oversec/one/Share.java`

This class provides a simple way to share text with other applications.

## `app/src/main/java/io/oversec/one/UiThreadVars.java`

This class is a container for variables that are only accessed from the UI thread. This is done to avoid synchronization issues.

## `app/src/main/java/io/oversec/one/Util.java`

This file contains various utility methods that are used throughout the application.

## `app/src/main/java/io/oversec/one/acs/`

This package contains classes related to the accessibility service, which is used to interact with other applications.

## `app/src/main/java/io/oversec/one/common/`

This package contains common classes that are used throughout the application.

## `app/src/main/java/io/oversec/one/crypto/`

This package contains classes related to cryptography, such as encryption and decryption.

## `app/src/main/java/io/oversec/one/db/`

This package contains classes for interacting with the application's database.

## `app/src/main/java/io/oversec/one/iab/`

This package contains classes related to in-app billing.

## `app/src/main/java/io/oversec/one/ovl/`

This package contains classes for creating and managing overlays.

## `app/src/main/java/io/oversec/one/ui/`

This package contains the user interface classes for the application, such as activities and fragments.

## `app/src/main/java/io/oversec/one/util/`

This package contains utility classes that are used throughout the application.

## `app/src/main/java/io/oversec/one/view/`

This package contains custom views that are used in the application's user interface.

## `app/src/main/java/io/oversec/one/acs/OversecAccessibilityService.java`

This is the main accessibility service for the application. It is responsible for monitoring the screen for encrypted text, and for providing the user with the ability to decrypt it. It also provides the user with the ability to encrypt text in any application.

This class is the core of the application's functionality. It uses the Android Accessibility API to read the text that is displayed on the screen, and to identify text that is encrypted. When it finds encrypted text, it displays an overlay that allows the user to decrypt it.

The service also provides a global overlay that can be used to encrypt text in any application. When the user taps on the encrypt button, the service gets the text from the focused input field, encrypts it, and then pastes the encrypted text back into the input field.

## `app/src/main/java/io/oversec/one/acs/OversecAccessibilityService_1.java`

This is a subclass of `OversecAccessibilityService` that is used on devices running Android 4.1 and below. It provides the same functionality as the main service, but it uses a different method for getting the text from the screen.

## `app/src/main/java/io/oversec/one/acs/Tree.java` and `app/src/main/java/io/oversec/one/acs/TreeNodeVisitor.java`

These classes are used to represent the view hierarchy of the screen as a tree. The `Tree` class is a container for the tree, and the `TreeNodeVisitor` class is used to traverse the tree.

## `app/src/main/java/io/oversec/one/acs/DisplayNodeVisitor.java`

This class is a `TreeNodeVisitor` that is used to find all the encrypted nodes in the view hierarchy.

## `app/src/main/java/io/oversec/one/acs/InvalidRefreshException.java`

This exception is thrown when an invalid refresh is attempted.

## `app/src/main/java/io/oversec/one/acs/NodeAndFlag.java`

This class is a container for a node and a flag. It is used to pass information about a node to the accessibility service.

## `app/src/main/java/io/oversec/one/acs/util/`

This package contains utility classes for the accessibility service.

## `app/src/main/java/io/oversec/one/acs/util/AccessibilityNodeInfoRef.java`

This class is a reference to an `AccessibilityNodeInfo` object. It is used to avoid holding a direct reference to the object, which can cause memory leaks.

## `app/src/main/java/io/oversec/one/acs/util/AccessibilityNodeInfoUtils.java`

This class contains utility methods for working with `AccessibilityNodeInfo` objects.

## `app/src/main/java/io/oversec/one/acs/util/AndroidIntegration.java`

This class provides an integration point for Android-specific functionality.

## `app/src/main/java/io/oversec/one/acs/util/Bag.java`

This class is a simple collection that is used to store a bag of items.

## `app/src/main/java/io/oversec/one/acs/util/BasePackageMonitor.java`

This class is a base class for package monitors. It provides a simple way to monitor for changes to packages.

## `app/src/main/java/io/oversec/one/acs/util/ClassLoadingManager.java`

This class is used to manage the loading of classes.

## `app/src/main/java/io/oversec/one/acs/util/SimplePool.java` and `app/src/main/java/io/oversec/one/acs/util/SynchronizedPool.java`

These classes are simple object pools. They are used to reuse objects instead of creating new ones, which can improve performance.

## `app/src/main/java/io/oversec/one/common/Consts.kt`

This file contains constants that are used throughout the application.

## `app/src/main/java/io/oversec/one/common/MainPreferences.kt`

This file contains the main preferences for the application. It is used to store and retrieve user preferences.

## `app/src/main/java/io/oversec/one/crypto/TemporaryContentProvider.kt`

This content provider is used to temporarily store data that is shared between applications.

## `app/src/main/java/io/oversec/one/crypto/images/`

This package contains classes for encrypting and decrypting images.

## `app/src/main/java/io/oversec/one/crypto/images/xcoder/`

This package contains classes for encoding and decoding images.

## `app/src/main/java/io/oversec/one/crypto/images/xcoder/ImageXCoderFacade.kt`

This class is a facade for the image encoding and decoding functionality.

## `app/src/main/java/io/oversec/one/crypto/images/xcoder/blackandwhite/`

This package contains classes for encoding and decoding black and white images.

## `app/src/main/java/io/oversec/one/crypto/images/xcoder/blackandwhite/BlackAndWhiteImageXCoder.kt`

This class is an image encoder/decoder that uses a black and white color space.

## `app/src/main/java/io/oversec/one/crypto/sym/`

This package contains classes for symmetric key cryptography.

## `app/src/main/java/io/oversec/one/crypto/sym/KeystoreIntentService.kt`

This service is used to store and retrieve symmetric keys from the Android Keystore.

## `app/src/main/java/io/oversec/one/db/Db.java`

This class is a helper class for interacting with the application's database. It provides methods for creating, reading, updating, and deleting data.

## `app/src/main/java/io/oversec/one/db/IDecryptOverlayLayoutParamsChangedListener.java`

This interface is a listener for changes to the decrypt overlay's layout params.

## `app/src/main/java/io/oversec/one/db/PadderDb.java`

This class is a helper class for interacting with the padder database. It provides methods for creating, reading, updating, and deleting padder data.

## `app/src/main/java/io/oversec/one/iab/IabHelper.java`

This is the main class for the in-app billing functionality. It provides a simple interface for querying for owned items, purchasing items, and consuming items.

This class is a wrapper around the Google Play In-app Billing API. It provides a number of convenience methods that make it easier to work with the API. It also provides automatic signature verification, which helps to prevent fraudulent purchases.

## `app/src/main/java/io/oversec/one/iab/IabUtil.java`

This class contains utility methods for in-app billing.

## `app/src/main/java/io/oversec/one/iab/PurchaseActivity.java`

This activity is used to display the in-app billing purchase dialog.

## `app/src/main/java/io/oversec/one/iab/Purchase.java` and `app/src/main/java/io/oversec/one/iab/SkuDetails.java`

These classes are data models that represent a purchase and a SKU, respectively.

## `app/src/main/java/io/oversec/one/iab/Inventory.java`

This class is a data model that represents the user's inventory of owned items.

## `app/src/main/java/io/oversec/one/iab/IabResult.java` and `app/src/main/java/io/oversec/one/iab/IabException.java`

These classes are used to represent the result of an in-app billing operation.

## `app/src/main/java/io/oversec/one/iab/Security.java`

This class contains methods for verifying the signature of a purchase.

## `app/src/main/java/io/oversec/one/iab/Base64.java` and `app/src/main/java/io/oversec/one/iab/Base64DecoderException.java`

These classes are used for Base64 encoding and decoding.

## `app/src/main/java/io/oversec/one/iab/FullVersionListener.java`

This interface is a listener for when the user purchases the full version of the app.

## `app/src/main/java/io/oversec/one/ovl/OverlayView.java`

This is the base class for all overlays. It provides a simple way to create and manage overlays.

## `app/src/main/java/io/oversec/one/ovl/OverlayDecryptView.java`

This class is an overlay that is used to decrypt text. It displays a list of encrypted nodes, and it allows the user to decrypt them.

## `app/src/main/java/io/oversec/one/ovl/AbstractOverlayButtonView.java` and its subclasses

These classes are overlays that are used to display buttons. The `AbstractOverlayButtonView` class is an abstract base class that provides common functionality for all button overlays. The subclasses of `AbstractOverlayButtonView` are used to display specific types of buttons, such as the encrypt button, the decrypt button, and the hide button.

## `app/src/main/java/io/oversec/one/ovl/AbstractOverlayDialogView.java` and its subclasses

These classes are overlays that are used to display dialogs. The `AbstractOverlayDialogView` class is an abstract base class that provides common functionality for all dialog overlays. The subclasses of `AbstractOverlayDialogView` are used to display specific types of dialogs, such as the insufficient padding dialog and the corrupted encoding dialog.

## `app/src/main/java/io/oversec/one/ovl/NodeView.java` and its subclasses

These classes are views that are used to display nodes in the view hierarchy. The `NodeView` class is a base class that provides common functionality for all node views. The subclasses of `NodeView` are used to display specific types of nodes, such as text nodes and group nodes.

## `app/src/main/java/io/oversec/one/ui/screen/`

This package contains the screens for the application. Each screen is implemented as a Jetpack Compose composable function.

## `app/src/main/java/io/oversec/one/ui/screen/AboutScreen.kt`

This screen displays information about the application.

## `app/src/main/java/io/oversec/one/ui/screen/AppConfigScreen.kt`

This screen allows the user to configure the application's settings.

## `app/src/main/java/io/oversec/one/ui/screen/AppInfo.kt`

This screen displays information about a specific application.

## `app/src/main/java/io/oversec/one/ui/screen/EncryptionParamsScreen.kt`

This screen allows the user to configure the encryption parameters for a specific application.

## `app/src/main/java/io/oversec/one/ui/screen/MainSettingsScreen.kt`

This screen is the main settings screen for the application.

## `app/src/main/java/io/oversec/one/ui/screen/PadderDetailScreen.kt`

This screen allows the user to view and edit the details of a padder.

## `app/src/main/java/io/oversec/one/ui/screen/PaddersScreen.kt`

This screen displays a list of padders.

## `app/src/main/java/io/oversec/one/ui/screen/TweaksScreen.kt`

This screen allows the user to configure various tweaks for the application.

## `app/src/main/java/io/oversec/one/ui/theme/`

This package contains the theme for the application. It defines the colors, shapes, and typography that are used throughout the application.

## `app/src/main/java/io/oversec/one/ui/theme/Color.kt`

This file defines the colors that are used in the application.

## `app/src/main/java/io/oversec/one/ui/theme/Shapes.kt`

This file defines the shapes that are used in the application.

## `app/src/main/java/io/oversec/one/ui/theme/Theme.kt`

This file defines the theme for the application.

## `app/src/main/java/io/oversec/one/ui/theme/Typography.kt`

This file defines the typography that is used in the application.

## `app/src/main/java/io/oversec/one/ui/viewModel/`

This package contains the view models for the application. Each view model is responsible for preparing and managing the data for a particular screen.

## `app/src/main/java/io/oversec/one/ui/viewModel/EncryptionParamsViewModel.kt`

This view model is responsible for preparing and managing the data for the `EncryptionParamsScreen`.

## `app/src/main/java/io/oversec/one/ui/viewModel/MainSettingsViewModel.kt`

This view model is responsible for preparing and managing the data for the `MainSettingsScreen`.

## `app/src/main/java/io/oversec/one/ui/viewModel/PadderDetailViewModel.kt`

This view model is responsible for preparing and managing the data for the `PadderDetailScreen`.

## `app/src/main/java/io/oversec/one/ui/viewModel/PaddersViewModel.kt`

This view model is responsible for preparing and managing the data for the `PaddersScreen`.

## `app/src/main/java/io/oversec/one/ui/viewModel/TweaksViewModel.kt`

This view model is responsible for preparing and managing the data for the `TweaksScreen`.

## `app/src/main/java/io/oversec/one/util/WrappedWindowManager.java`

This class is a wrapper around the `WindowManager` class. It is used to work around a bug in Android that can cause a `BadTokenException` to be thrown when adding a view to the window manager.

## `app/src/main/java/io/oversec/one/view/MainActivity.kt`

This is the main activity for the application. It displays the main screen of the application, which is composed of a tabbed interface. The tabs are: Help, Apps, Keys, Settings, and Padder.

## Other Activities and Fragments

The `view` package also contains a number of other activities and fragments that are used to display the various screens of the application. These include:

*   `AboutActivity.kt`: This activity displays the "About" screen.
*   `AppConfigActivity.kt`: This activity allows the user to configure the settings for a specific application.
*   `EncryptionInfoActivity.kt`: This activity displays information about an encrypted message.
*   `EncryptionParamsActivity.kt`: This activity allows the user to configure the encryption parameters for a specific application.
*   `ImageDecryptActivity.kt` and `ImageEncryptActivity.kt`: These activities are used to decrypt and encrypt images, respectively.
*   `KeyDetailsActivity.kt` and `KeyImportCreateActivity.kt`: These activities are used to manage symmetric keys.
*   `PadderDetailActivity.kt`: This activity allows the user to view and edit the details of a padder.

## Custom Views and Preferences

The `view` package also contains a number of custom views and preferences that are used in the application's user interface. These include:

*   `DbCheckBoxPreference.java`: This is a custom preference that displays a checkbox.
*   `DialercodeEditTextPreference.java`: This is a custom preference that allows the user to enter a dialer code.
*   `MaxHeightScrollView.java`: This is a custom scroll view that has a maximum height.
*   `ValidatedDialogPreference.java` and `ValidatedEditTextPreference.java`: These are custom preferences that validate the user's input.

## `app/src/main/java/io/oversec/one/view/compose/`

This package contains composable functions that are used to build the application's user interface.

## `app/src/main/java/io/oversec/one/view/compose/KeystoreTTLDropDown.kt`

This composable function displays a dropdown menu that allows the user to select the time-to-live (TTL) for a key in the keystore.

## `app/src/main/java/io/oversec/one/view/compose/PassphraseDialog.kt`

This composable function displays a dialog that prompts the user to enter a passphrase.

## `app/src/main/java/io/oversec/one/view/compose/SimpleItem.kt`

This composable function displays a simple item in a list.

## `app/src/main/java/io/oversec/one/view/encparams/`

This package contains fragments that are used to configure the encryption parameters for different encryption methods.

## `app/src/main/java/io/oversec/one/view/encparams/GpgEncryptionParamsFragment.java`

This fragment allows the user to configure the encryption parameters for GPG encryption.

## `app/src/main/java/io/oversec/one/view/encparams/SimpleSymmetricEncryptionParamsFragment.java`

This fragment allows the user to configure the encryption parameters for simple symmetric encryption.

## `app/src/main/java/io/oversec/one/view/encparams/SymmetricEncryptionParamsFragment.java`

This fragment allows the user to configure the encryption parameters for symmetric encryption.

## `app/src/main/java/io/oversec/one/view/encparams/ActivityResultWrapper.java`

This class is a wrapper around an `ActivityResult`. It is used to pass the result of an activity to a fragment.

## `app/src/main/java/io/oversec/one/view/util/`

This package contains utility classes that are used in the application's user interface.

## `app/src/main/java/io/oversec/one/view/util/EditTextPasswordWithVisibilityToggle.kt`

This class is a custom `EditText` that allows the user to toggle the visibility of the password.

## `app/src/main/java/io/oversec/one/view/util/GpgCryptoUIUtil.kt`

This class contains utility methods for the GPG crypto UI.

## `app/src/main/java/io/oversec/one/view/util/KeystoreTTLSpinner.kt`

This class is a custom spinner that allows the user to select the time-to-live (TTL) for a key in the keystore.

## `app/src/main/java/io/oversec/one/view/util/SimpleDividerItemDecoration.kt`

This class is an `ItemDecoration` that displays a simple divider between items in a `RecyclerView`.

## `app/src/main/java/io/oversec/one/view/util/SymUIUtil.kt`

This class contains utility methods for the symmetric crypto UI.

## `app/src/main/java/io/oversec/one/view/util/Util.kt`

This class contains utility methods that are used throughout the application's user interface.

## `app/src/main/kotlin/`

This directory contains the Kotlin source code for the application.

## `app/src/main/kotlin/io/`

This is the root package for the application's Kotlin source code.

## `app/src/main/kotlin/io/oversec/`

This is the package containing the Kotlin source code for the Oversec application.

## `app/src/main/kotlin/io/oversec/one/`

This package contains the application-specific Kotlin source code.

## `app/src/main/kotlin/io/oversec/one/ZxcvbnService.kt`

This service is used to estimate the strength of passwords. It uses the Zxcvbn library to perform the estimation.

## `app/src/main/res/anim/`

This directory contains the animation resources for the application.

## `app/src/main/res/drawable/` and `app/src/main/res/drawable-*/`

These directories contain the drawable resources for the application. Drawables are images and other graphical assets that are used in the application's user interface. The different `drawable-*` directories contain the same drawables at different resolutions, which allows the drawables to be displayed correctly on devices with different screen densities.

## `app/src/main/res/layout/`

*(No description available yet)*

## `app/src/main/res/menu/`

*(No description available yet)*

## `app/src/main/res/mipmap-hdpi/`

*(No description available yet)*

## `app/src/main/res/mipmap-mdpi/`

*(No description available yet)*

## `app/src/main/res/mipmap-xhdpi/`

*(No description available yet)*

## `app/src/main/res/mipmap-xxhdpi/`

*(No description available yet)*

## `app/src/main/res/mipmap-xxxhdpi/`

*(No description available yet)*

## `app/src/main/res/raw/`

*(No description available yet)*

## `app/src/main/res/values/`

*(No description available yet)*

## `app/src/main/res/values-de/`

*(No description available yet)*

## `app/src/main/res/values-es/`

*(No description available yet)*

## `app/src/main/res/values-fr/`

*(No description available yet)*

## `app/src/main/res/values-it/`

*(No description available yet)*

## `app/src/main/res/values-jp/`

*(No description available yet)*

## `app/src/main/res/values-land/`

*(No description available yet)*

## `app/src/main/res/values-nl/`

*(No description available yet)*

## `app/src/main/res/values-pl/`

*(No description available yet)*

## `app/src/main/res/values-pt/`

*(No description available yet)*

## `app/src/main/res/values-ru/`

*(No description available yet)*

## `app/src/main/res/values-tr/`

*(No description available yet)*

## `app/src/main/res/values-v21/`

*(No description available yet)*

## `app/src/main/res/values-w820dp/`

*(No description available yet)*

## `app/src/main/res/values-zh-rCN/`

*(No description available yet)*

## `app/src/main/res/xml/`

This directory contains the animation resources for the application.

## `app/src/main/res/anim/activity_out.xml`

This file defines the animation that is used when an activity slides out of the screen.

## `app/src/main/res/drawable/fab_bg.xml`

This file is a drawable resource that defines the background for a floating action button.

## `app/src/main/res/drawable/line_divider.xml`

This file is a drawable resource that defines a line divider.

## `app/src/main/res/drawable/tooltip_bg.xml`

This file is a drawable resource that defines the background for a tooltip.

## `app/src/main/res/drawable/tooltip_bg_primary_headline.xml`

This file is a drawable resource that defines the background for a tooltip with a primary headline.

## `app/src/main/res/drawable/tooltip_bg_transparent.xml`

This file is a drawable resource that defines a transparent background for a tooltip.

## `app/src/main/res/drawable-hdpi/ic_account_box_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_add_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_backspace_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_backspace_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_cachedkey_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_camera_alt_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_cancel_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_cancel_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_clear_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_clear_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_create_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_delete_forever_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_all_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_all_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_all_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_done_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_error_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_error_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_error_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_font_download_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_get_app_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_help_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_help_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_info_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_info_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_info_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_lock_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_lock_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_lock_open_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_loop_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_mail_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_memory_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_not_interested_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_not_interested_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_question_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_remove_red_eye_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_save_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_search_black_48dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_settings_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_share_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_share_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_shop_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_shutup_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_vpn_key_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_warning_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_warning_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_warning_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_warning_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-hdpi/ic_warning_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_account_box_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_add_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_backspace_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_backspace_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_cachedkey_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_camera_alt_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_cancel_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_cancel_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_clear_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_clear_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_create_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_delete_forever_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_all_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_all_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_all_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_done_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_error_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_error_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_error_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_font_download_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_get_app_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_help_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_help_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_info_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_info_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_info_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_lock_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_lock_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_lock_open_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_loop_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_mail_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_memory_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_not_interested_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_not_interested_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_question_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_remove_red_eye_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_save_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_search_black_48dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_settings_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_share_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_share_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_shop_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_shutup_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_vpn_key_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_warning_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_warning_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_warning_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_warning_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-mdpi/ic_warning_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-v21/fab_bg.xml`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_account_box_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_add_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_backspace_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_backspace_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_cachedkey_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_camera_alt_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_cancel_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_cancel_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_clear_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_clear_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_create_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_delete_forever_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_all_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_all_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_all_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_done_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_error_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_error_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_error_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_font_download_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_get_app_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_help_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_help_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_info_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_info_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_info_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_lock_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_lock_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_lock_open_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_loop_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_mail_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_memory_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_not_interested_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_not_interested_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_question_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_remove_red_eye_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_save_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_search_black_48dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_settings_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_share_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_share_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_shop_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_shutup_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_vpn_key_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_warning_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_warning_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_warning_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_warning_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xhdpi/ic_warning_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_account_box_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_add_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_backspace_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_backspace_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_cachedkey_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_camera_alt_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_cancel_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_cancel_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_clear_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_clear_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_create_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_delete_forever_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_all_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_all_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_all_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_done_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_error_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_error_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_error_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_font_download_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_get_app_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_help_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_help_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_info_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_info_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_info_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_lock_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_lock_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_lock_open_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_loop_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_mail_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_memory_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_not_interested_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_not_interested_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_question_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_remove_red_eye_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_save_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_search_black_48dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_settings_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_share_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_share_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_shop_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_shutup_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_vpn_key_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_warning_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_warning_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_warning_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_warning_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxhdpi/ic_warning_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_account_box_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_add_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_backspace_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_backspace_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_cachedkey_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_camera_alt_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_cancel_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_cancel_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_clear_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_clear_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_create_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_delete_forever_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_all_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_all_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_all_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_green_a700_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_green_a700_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_done_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_error_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_error_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_error_red_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_font_download_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_get_app_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_help_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_help_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_info_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_info_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_info_outline_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_lock_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_lock_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_lock_open_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_loop_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_mail_outline_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_memory_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_not_interested_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_not_interested_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_question_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_remove_red_eye_black_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_save_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_search_black_48dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_settings_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_share_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_share_white_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_shop_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_shutup_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_vpn_key_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_warning_black_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_warning_orange_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_warning_orange_24dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_warning_red_18dp.png`

*(No description available yet)*

## `app/src/main/res/drawable-xxxhdpi/ic_warning_red_24dp.png`

This directory contains the layout files for the application. Each layout file defines the user interface for a particular screen or component.

## `app/src/main/res/layout/activity_main.xml`

This is the main layout file for the application. It defines the layout for the main screen, which is composed of a tabbed interface.

## Other Layout Files

The `layout` directory also contains a number of other layout files that are used to define the user interface for the various screens and components of the application. These include layouts for activities, dialogs, and list items.

## `app/src/main/res/layout/checkbox_pref.xml`

*(No description available yet)*

## `app/src/main/res/layout/content_crash.xml`

*(No description available yet)*

## `app/src/main/res/layout/content_encryption_info.xml`

*(No description available yet)*

## `app/src/main/res/layout/content_encryption_params.xml`

*(No description available yet)*

## `app/src/main/res/layout/content_main.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_binary_base.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_binary_gpg.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_binary_simplesym.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_binary_sym.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_text_base.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_text_gpg.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_text_simplesym.xml`

*(No description available yet)*

## `app/src/main/res/layout/encryption_info_text_sym.xml`

*(No description available yet)*

## `app/src/main/res/layout/gpg_encryption_params.xml`

*(No description available yet)*

## `app/src/main/res/layout/gpg_encryption_params_nookc.xml`

*(No description available yet)*

## `app/src/main/res/layout/gpg_encryption_params_okcok.xml`

*(No description available yet)*

## `app/src/main/res/layout/gpg_listitem_recipient.xml`

*(No description available yet)*

## `app/src/main/res/layout/ignore_text_dialog.xml`

*(No description available yet)*

## `app/src/main/res/layout/intspinner_item.xml`

*(No description available yet)*

## `app/src/main/res/layout/intspinner_pref.xml`

*(No description available yet)*

## `app/src/main/res/layout/listitem_app_only.xml`

*(No description available yet)*

## `app/src/main/res/layout/listitem_iab_purchase.xml`

*(No description available yet)*

## `app/src/main/res/layout/listitem_iab_sku.xml`

*(No description available yet)*

## `app/src/main/res/layout/listitem_title_and_body.xml`

*(No description available yet)*

## `app/src/main/res/layout/new_password_input_dialog.xml`

*(No description available yet)*

## `app/src/main/res/layout/overlay_button.xml`

*(No description available yet)*

## `app/src/main/res/layout/overlay_dialog.xml`

*(No description available yet)*

## `app/src/main/res/layout/overlay_info_button.xml`

*(No description available yet)*

## `app/src/main/res/layout/passphrase_dialog.xml`

*(No description available yet)*

## `app/src/main/res/layout/simple_item.xml`

*(No description available yet)*

## `app/src/main/res/layout/simplesym_encryption_params.xml`

*(No description available yet)*

## `app/src/main/res/layout/simplesym_listitem.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_activity_createkey_random.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_activity_key_details.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_content_create_key_random.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_content_key_details.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_encryption_params.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_fragment_keys.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_listitem.xml`

*(No description available yet)*

## `app/src/main/res/layout/sym_listitem_with_checkbox.xml`

*(No description available yet)*

## `app/src/main/res/layout/tooltip_custom.xml`

*(No description available yet)*

## `app/src/main/res/layout/tooltip_standalone.xml`

This directory contains the menu files for the application. Each menu file defines a menu that is displayed in the application.

## `app/src/main/res/menu/menu_main.xml`

This is the main menu file for the application. It defines the menu that is displayed on the main screen.

## Other Menu Files

The `menu` directory also contains a number of other menu files that are used to define the menus for the various screens and components of the application. These include menus for the app config screen, the encryption params screen, and the key details screen.

## `app/src/main/res/mipmap-*/ic_launcher.png`

These directories contain the launcher icon for the application. The different directories contain the same icon at different resolutions, which allows the icon to be displayed correctly on devices with different screen densities.

## `app/src/main/res/raw/about.md`

This file contains the content for the "About" screen of the application, including links to the project's website, privacy policy, and open-source libraries.

## `app/src/main/res/raw/changelog.md`

This file contains the changelog for the application, which is displayed on the "Changelog" screen.

## `app/src/main/res/values/`

This directory contains the resource values for the application, such as strings, colors, and dimensions.

## `app/src/main/res/values/attrs.xml`

This file defines the custom attributes that are used in the application's layouts.

## `app/src/main/res/values/colors.xml`

This file defines the colors that are used in the application.

## `app/src/main/res/values/config.xml`

This file contains configuration values for the application.

## `app/src/main/res/values/dimens.xml`

This file defines the dimensions that are used in the application's layouts.

## `app/src/main/res/values/ids.xml`

This file defines the IDs that are used to identify views in the application's layouts.

## `app/src/main/res/values/integers.xml`

This file defines the integers that are used in the application.

## `app/src/main/res/values/strings.xml`

This file contains the string resources for the application.

## `app/src/main/res/values/strings_donottranslate.xml`

This file contains string resources that should not be translated.

## `app/src/main/res/values/styles.xml`

This file defines the styles that are used in the application.

## `app/src/main/res/values-*/`

These directories contain localized resources for the application. Each directory corresponds to a specific language or configuration, as indicated by the directory name.

## `app/src/main/res/xml/`

This directory contains XML resource files for the application.

## `app/src/main/res/xml/acs_config.xml`

This file is the configuration file for the accessibility service. It specifies the capabilities of the service, such as the types of events it can receive and the packages it can interact with.

## `app/src/main/res/xml/empty_preferences.xml`

This file is an empty preferences file. It is used to create a preferences screen that has no preferences.

## `app/src/main/res/xml/main_preferences.xml`

This file is the main preferences file for the application. It defines the preferences that are displayed on the main settings screen.

## `fastlane/`

This directory contains configuration files for Fastlane, a tool for automating the release process of the app.

## `fastlane/metadata/`

This directory contains metadata for the application, such as the application's name, description, and screenshots. This metadata is used to generate the application's listing on the Google Play Store.

## `gradle/wrapper/`

This directory contains the Gradle wrapper files. The Gradle wrapper allows the project to be built with a specific version of Gradle without requiring Gradle to be installed on the system.

## `gradle/wrapper/gradle-wrapper.jar`

This is the Gradle wrapper JAR file. It contains the code that is used to download and run Gradle.

## `gradle/wrapper/gradle-wrapper.properties`

This file contains the configuration for the Gradle wrapper. It specifies the version of Gradle that should be used to build the project.

## `libraries/oversec_crypto/`

This directory contains the source code for the `oversec_crypto` library. This library provides the core cryptography functionality for the application.

## `libraries/oversec_crypto/build.gradle.kts`

This is the build script for the `oversec_crypto` library. It defines the library's dependencies and other build-related configurations.

## `libraries/oversec_crypto/src/`

This directory is the root of the library's source code.

## `libraries/oversec_crypto/src/main/`

This directory contains the main source code and resources for the library.

## `libraries/oversec_crypto/src/test/`

This directory contains the tests for the library.

## `libraries/oversec_crypto/src/main/AndroidManifest.xml`

This is the manifest file for the library.

## `libraries/oversec_crypto/src/main/assets/`

This directory contains the assets for the library.

## `libraries/oversec_crypto/src/main/assets/diceware.*`

These files contain word lists that are used to generate random passphrases.

## `libraries/oversec_crypto/src/main/java/`

This directory contains the Java source code for the library.

## `libraries/oversec_crypto/src/main/proto/`

This directory contains the protocol buffer files for the library.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/`

This package contains the core cryptography functionality for the application. It includes classes for encrypting and decrypting data, as well as classes for managing keys and other cryptographic parameters.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/CryptoHandlerFacade.kt`

This is the main class for the cryptography functionality. It provides a simple interface for encrypting and decrypting data.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/AbstractCryptoHandler.kt`

This is an abstract base class for all crypto handlers. It provides common functionality for all crypto handlers, such as the ability to encrypt and decrypt data.

## Other Crypto Classes

The `crypto` package also contains a number of other classes that are used to provide the cryptography functionality for the application. These include classes for managing keys, handling encryption parameters, and representing the results of decryption operations.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/`

This package contains classes for encoding and decoding data.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/AbstractXCoder.kt`

This is an abstract base class for all encoders and decoders. It provides common functionality for all encoders and decoders, such as the ability to encode and decode data.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/XCoderFactory.kt`

This class is a factory for creating encoders and decoders.

## Other Encoding Classes

The `encoding` package also contains a number of other classes that are used to provide the encoding and decoding functionality for the application. These include classes for Base64 encoding, ASCII-armored GPG encoding, and zero-width encoding.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/pad/`

This package contains classes for padding data. Padding is used to ensure that the data to be encrypted is a multiple of the block size of the cipher.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/pad/AbstractPadder.kt`

This is an abstract base class for all padders. It provides common functionality for all padders, such as the ability to add and remove padding.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/encoding/pad/XCoderAndPadderFactory.kt`

This class is a factory for creating encoders, decoders, and padders.

## Other Padding Classes

The `pad` package also contains a number of other classes that are used to provide the padding functionality for the application. These include classes for Gutenberg padding, manual padding, and Oversec padding.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/gpg/`

This package contains classes for GPG encryption.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/gpg/GpgCryptoHandler.kt`

This is the main class for the GPG cryptography functionality. It provides a simple interface for encrypting and decrypting data using GPG.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/gpg/OpenKeychainConnector.kt`

This class is used to connect to the OpenKeychain application. OpenKeychain is a third-party application that provides GPG functionality.

## Other GPG Classes

The `gpg` package also contains a number of other classes that are used to provide the GPG cryptography functionality for the application. These include classes for managing GPG keys, handling GPG encryption parameters, and representing the results of GPG decryption operations.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/images/`

This package contains classes for encrypting and decrypting images.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/images/ImagePreferences.kt`

This class contains the preferences for image encryption.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/images/xcoder/`

This package contains classes for encoding and decoding images.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/images/xcoder/ImageXCoder.kt`

This is the main class for the image encoding and decoding functionality. It provides a simple interface for encoding and decoding images.

## Other Image Classes

The `images` package also contains a number of other classes that are used to provide the image encryption and decryption functionality for the application. These include classes for handling different image formats, and for managing image encryption parameters.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/sym/`

This package contains classes for symmetric key cryptography.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/sym/SymmetricCryptoHandler.kt`

This is the main class for the symmetric key cryptography functionality. It provides a simple interface for encrypting and decrypting data using symmetric keys.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/sym/OversecKeystore2.kt`

This class is used to store and retrieve symmetric keys from the Android Keystore.

## Other Symmetric Crypto Classes

The `sym` package also contains a number of other classes that are used to provide the symmetric key cryptography functionality for the application. These include classes for managing symmetric keys, handling symmetric encryption parameters, and representing symmetric keys in both encrypted and plaintext form.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symbase/`

This package contains base classes for symmetric key cryptography.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symbase/BaseSymmetricCryptoHandler.kt`

This is an abstract base class for all symmetric crypto handlers. It provides common functionality for all symmetric crypto handlers, such as the ability to encrypt and decrypt data.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symbase/KeyCache.kt`

This class is a cache for symmetric keys. It is used to avoid having to re-derive symmetric keys from a passphrase every time they are needed.

## Other Symmetric Crypto Base Classes

The `symbase` package also contains a number of other classes that are used to provide the base functionality for symmetric key cryptography. These include classes for managing symmetric keys, handling symmetric encryption parameters, and representing the results of symmetric decryption operations.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symsimple/`

This package contains classes for simple symmetric key cryptography.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symsimple/SimpleSymmetricCryptoHandler.kt`

This is the main class for the simple symmetric key cryptography functionality. It provides a simple interface for encrypting and decrypting data using a passphrase.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symsimple/PasswordRequiredException.kt` and `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symsimple/PasswordCantDecryptException.kt`

These exceptions are thrown when a password is required to decrypt a message, but no password is provided, or when the provided password is incorrect.

## `libraries/oversec_crypto/src/main/java/io/oversec/one/crypto/symsimple/SimpleSymmetricEncryptionParams.kt`

This class is a data model that represents the parameters for simple symmetric encryption.

## `libraries/oversec_crypto/src/main/java/roboguice/util/`

This package contains utility classes from the RoboGuice library.

## `libraries/oversec_crypto/src/main/java/roboguice/util/Ln.java`

This class is a logging utility that is similar to the `android.util.Log` class. It provides a number of convenience methods that make it easier to log messages.

## `libraries/oversec_crypto/src/main/java/roboguice/util/Strings.java`

This class contains utility methods for working with strings.

## `libraries/oversec_crypto/src/main/java/uk/co/biddell/diceware/`

This package contains classes for generating random passphrases using the Diceware method.

## `libraries/oversec_crypto/src/main/java/uk/co/biddell/diceware/dictionaries/`

This package contains dictionaries that are used to generate Diceware passphrases.

## `libraries/oversec_crypto/src/main/java/uk/co/biddell/diceware/dictionaries/DiceWare.java`

This class is the main class for the Diceware functionality. It provides a simple interface for generating Diceware passphrases.

## `libraries/oversec_crypto/src/main/java/uk/co/biddell/diceware/dictionaries/DiceWords.java`

This class is a data model that represents a Diceware word list.

## `libraries/oversec_crypto/src/main/java/uk/co/biddell/diceware/dictionaries/Dictionary.java`

This class is a data model that represents a dictionary.

## `libraries/oversec_crypto/src/main/proto/`

This directory contains the protocol buffer files for the library. Protocol buffers are a language-neutral, platform-neutral, extensible mechanism for serializing structured data.

## `libraries/oversec_crypto/src/main/proto/inner.proto`

This file defines the inner protocol buffer messages. These messages are used to represent the encrypted data.

## `libraries/oversec_crypto/src/main/proto/kex.proto`

This file defines the protocol buffer messages for key exchange.

## `libraries/oversec_crypto/src/main/proto/outer.proto`

This file defines the outer protocol buffer messages. These messages are used to wrap the encrypted data and provide additional metadata.

## `libraries/oversec_crypto/src/test/`

This directory contains the tests for the `oversec_crypto` library.

## `libraries/oversec_crypto/src/test/java/`

This directory contains the Java source code for the tests.

## `libraries/oversec_crypto/src/test/java/io/oversec/one/`

This package contains the tests for the `io.oversec.one` package.

## `libraries/oversec_crypto/src/test/java/io/oversec/one/crypto/`

This package contains the tests for the `io.oversec.one.crypto` package.

## Test Classes

The `test` directory also contains a number of other classes that are used to test the functionality of the `oversec_crypto` library. These include test cases for the various crypto handlers, encoders, and decoders.
