package com.umutsaydam.zenfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.umutsaydam.zenfocus.presentation.appearance.AppearanceScreen
import com.umutsaydam.zenfocus.presentation.home.HomeScreen
import com.umutsaydam.zenfocus.ui.theme.ZenFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenFocusTheme {
                HomeScreen()
            }
        }
    }
}