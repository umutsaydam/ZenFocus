package com.umutsaydam.zenfocus.presentation.policy

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.NotConnectedMessage
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun PolicyScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    policyViewModel: PolicyViewModel = hiltViewModel()
) {
    Scaffold(
        contentColor = SurfaceContainerLow,
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackOrIgnore()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = Outline
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (policyViewModel.isNetworkConnected()){
            AndroidView(
                modifier = modifier.padding(paddingValues),
                factory = {
                    WebView(it).apply {
                        webViewClient = WebViewClient()
                        loadUrl("https://www.privacypolicies.com/live/c96570eb-49c3-4ebb-b14d-421bd0d5edc6")
                    }
                }
            )
        }else{
            NotConnectedMessage()
        }
    }
}