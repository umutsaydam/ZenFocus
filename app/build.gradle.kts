import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.umutsaydam.zenfocus"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.umutsaydam.zenfocus"
        minSdk = 24
        targetSdk = 35
        versionCode = 22
        versionName = "1.0.1"

        val properties: Properties = Properties()
        properties.load(
            project.rootProject.file("local.properties").inputStream()
        )
// These lines are commented for the open source contribution.
//        buildConfigField(
//            type = "String",
//            name = "GOOGLE_ADS_APP_ID",
//            value = "\"${properties.getProperty("GOOGLE_ADS_APP_ID")}\""
//        )

        bundle {
            language {
                enableSplit = false
            }
        }
// These lines are commented for the open source contribution.
//        buildConfigField(
//            type = "String",
//            name = "AD_BANNER_UNIT_ID",
//            value = "\"${properties.getProperty("AD_BANNER_UNIT_ID")}\""
//        )
//
//        buildConfigField(
//            type = "String",
//            name = "AD_REWARD_THEME_UNIT_ID",
//            value = "\"${properties.getProperty("AD_REWARD_THEME_UNIT_ID")}\""
//        )
//
//        manifestPlaceholders["GOOGLE_ADS_APP_ID"] = properties.getProperty("GOOGLE_ADS_APP_ID")
//        manifestPlaceholders["AD_BANNER_UNIT_ID"] = properties.getProperty("AD_BANNER_UNIT_ID")
//        manifestPlaceholders["AD_REWARD_THEME_UNIT_ID"] = properties.getProperty("AD_REWARD_THEME_UNIT_ID")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
//            isDebuggable false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    android.buildFeatures.buildConfig = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    // Hilt Dagger
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Navigation Animation
    implementation(libs.accompanist.navigation.animation)
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    // Amplify
    implementation(libs.amplifyframework.core)
    // Cognito auth
    implementation(libs.amplifyframework.aws.auth.cognito)
    // S3 storage
    implementation(libs.aws.storage.s3)
    // AWS dynamo
    implementation(libs.amazonaws.aws.android.sdk.ddb)
    // AWS api
    implementation(libs.aws.api)
    // AWS SDK
    implementation(libs.aws.android.sdk.mobile.client)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Google Credentials Play Services
    implementation(libs.androidx.credentials.play.services.auth)
// These lines are commented for the open source contribution.
//    // Google Mobile Ads SDK
//    implementation(libs.play.services.ads)
//    // Google Billing
//    implementation(libs.billing)
    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
}