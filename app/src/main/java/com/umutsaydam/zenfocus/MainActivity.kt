package com.umutsaydam.zenfocus

import android.app.Activity
import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
// These line is commented for the open source contribution.
//import com.google.android.gms.ads.MobileAds
import com.umutsaydam.zenfocus.presentation.navigation.MainNavHost
import com.umutsaydam.zenfocus.presentation.viewmodels.MainActivityViewModel
import com.umutsaydam.zenfocus.presentation.viewmodels.PomodoroViewModel
import com.umutsaydam.zenfocus.ui.theme.ZenFocusTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var pomodoroViewModel: PomodoroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        pomodoroViewModel = ViewModelProvider(this)[PomodoroViewModel::class.java]
        actionBar?.hide()
        enableEdgeToEdge()
        initAmplify()
// These line is commented for the open source contribution.
//        initGoogleAds(this)
        setContent {
            ZenFocusTheme {
                var isLoading by remember { mutableStateOf(true) }
                splash.setKeepOnScreenCondition {
                    isLoading
                }

                val mainActivityViewModel: MainActivityViewModel = hiltViewModel()
                mainActivityViewModel.startInitialSetupIfFirstEntry(Locale.getDefault())
                val appLang by mainActivityViewModel.defaultAppLang.collectAsState()
                LaunchedEffect(appLang) {
                    if (appLang != null) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            this@MainActivity.getSystemService(LocaleManager::class.java)
                                .applicationLocales = LocaleList.forLanguageTags(appLang)
                        } else {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(
                                    appLang
                                )
                            )
                        }
                        isLoading = false
                    }
                }
                MainNavHost()
            }
        }
    }
// These lines are commented for the open source contribution.
//    private fun initGoogleAds(activity: Activity) {
//        MobileAds.initialize(activity) {}
//    }

    private fun initAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
        } catch (error: AmplifyException) {
            error.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        pomodoroViewModel.startPomodoroService()
    }
}