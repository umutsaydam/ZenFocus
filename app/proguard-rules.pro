# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Hilt
-keep class dagger.hilt.** {
    <init>();
    *;
}
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }

# Room
-keep class androidx.room.** {
    <init>();
    *;
}
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.** class * { *; }
-keepclasseswithmembernames class * {
    @androidx.room.Dao <methods>;
}

# Amplify
-keep class com.amplifyframework.** {
    <init>();
    *;
}
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.amazonaws.mobileconnectors.**
-dontwarn org.apache.commons.logging.**

# AWS SDK
-keep class com.amazonaws.** {
    <init>();
    *;
}
-keepattributes Signature
-dontwarn com.amazonaws.**

# Google Play (Ads, Billing)
-keep class com.google.android.gms.** {
    <init>();
    *;
}
-dontwarn com.google.android.gms.**