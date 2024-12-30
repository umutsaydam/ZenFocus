package com.umutsaydam.zenfocus

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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
import java.util.Locale

@AndroidEntryPoint
class MainActivity(
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        actionBar?.hide()
        enableEdgeToEdge()
        initAmplify()
        initGoogleAds(this)
        setContent {
            ZenFocusTheme {
                val mainActivityViewModel: MainActivityViewModel = hiltViewModel()
                mainActivityViewModel.startInitialSetupIfFirstEntry(Locale.getDefault())
                val appLang by mainActivityViewModel.defaultAppLang.collectAsState()
                LaunchedEffect(appLang) {
                    if (appLang != null) {
                        Log.i("R/T", appLang!!)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Log.i("R/T", appLang!!)

                            this@MainActivity.getSystemService(LocaleManager::class.java)
                                .applicationLocales = LocaleList.forLanguageTags(appLang)
                        } else {
                            Log.i("R/T", "sad $appLang")
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(
                                    appLang
                                )
                            )
                        }
                    }
                }
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