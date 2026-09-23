# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK's default ProGuard rules.

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep Compose
-keep class androidx.compose.** { *; }

# Keep app entry points
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
