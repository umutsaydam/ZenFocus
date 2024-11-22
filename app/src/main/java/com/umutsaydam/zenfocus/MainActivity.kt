package com.umutsaydam.zenfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.umutsaydam.zenfocus.presentation.navigation.MainNavHost
import com.umutsaydam.zenfocus.ui.theme.ZenFocusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenFocusTheme {
                MainNavHost()
            }
        }
    }
}