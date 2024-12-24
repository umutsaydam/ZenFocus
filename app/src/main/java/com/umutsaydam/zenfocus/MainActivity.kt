package com.umutsaydam.zenfocus

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.google.android.gms.ads.MobileAds
import com.umutsaydam.zenfocus.presentation.navigation.MainNavHost
import com.umutsaydam.zenfocus.ui.theme.ZenFocusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity(
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        enableEdgeToEdge()

        initAmplify()
        initGoogleAds(this)

        setContent {
            ZenFocusTheme {
                val mainActivityViewModel: MainActivityViewModel = hiltViewModel()
                MainNavHost()
            }
        }
    }

    private fun initGoogleAds(activity: Activity) {
        MobileAds.initialize(activity) {
            Log.i("A/D", "worked...")
        }
    }

    private fun initAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Amplify initialized")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }
}