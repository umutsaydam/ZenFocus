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
-keep class com.amplifyframework.api.** { *; }
-keep class com.amplifyframework.storage.** { *; }
-keep class com.amplifyframework.auth.cognito.** { *; }
-keepattributes *Annotation*
-dontwarn com.amazonaws.**

# AWS SDK
-keep class com.amazonaws.services.** { *; }
-keep class com.amazonaws.mobileconnectors.s3.** { *; }
-dontwarn com.amazonaws.services.**
-dontwarn software.amazon.awssdk.**

# Google Services
-keep class com.google.android.gms.** {
    <init>();
    *;
}
-dontwarn com.google.android.gms.**

# Google Mobile Ads SDK
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Google Billing
-keep class com.android.billingclient.** { *; }
-keepclassmembers class * extends java.util.ListResourceBundle {
        *;
}
-keep public class * extends android.app.Activity
-keep class com.android.billingclient.api.** { *; }
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**
-keepattributes Exceptions
-keepattributes Signature

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.internal.bind.** { *; }

# Gson Model Classes
-keep class com.umutsaydam.zenfocus.data.remote.dto.** { *; }
-keep class com.umutsaydam.zenfocus.domain.model.** { *; }

# Google Products In App
-keep class com.umutsaydam.zenfocus.data.remote.repository.GoogleProductsInAppRepositoryImpl {
    <init>();
    *;
 }

 # Google Ads Service
 -keep class com.umutsaydam.zenfocus.data.remote.service.GoogleAdServiceImpl {
     <init>();
     *;
  }

# Google Integrate In App Reviews
-keep class com.umutsaydam.zenfocus.data.remote.repository.IntegrateInAppReviewsRepositoryImpl { *; }

# Google Play Core
#-keep public class com.google.android.play.core.** { *; }
#-keepclassmembers class com.google.android.play.core.** { *; }
#-dontwarn com.google.android.play.core.**
