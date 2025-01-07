package com.umutsaydam.zenfocus.presentation.appearance

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.NotConnectedMessage
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppearanceScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appearanceViewModel: AppearanceViewModel = hiltViewModel()
) {
    val uiState by appearanceViewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = remember { configuration.screenWidthDp.dp > 600.dp }
    val themeSpace = remember { if (isTablet) 380.dp else 80.dp }

    LaunchedEffect(uiState.uiMessage, uiState.rewardedAd) {
        uiState.uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
        }

        uiState.rewardedAd?.let { rewardAd ->
            val activity = context as? Activity
            activity?.let {
                rewardAd.show(
                    it
                ) { rewardItem ->
//                    val rewardAmount = rewardItem.amount
//                    val rewardType = rewardItem.type
//                    Log.i("A/D", "user earned the reward")
//                    Log.i("A/D", "$rewardAmount, $rewardType")
                    appearanceViewModel.setDefaultTheme(uiState.selectedTheme)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = SurfaceContainerLow,
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
                },
                containerColor = Color.Transparent,
                actions = {
                    IconButton(
                        onClick = {
                            if (appearanceViewModel.willShowAd()) {
                                appearanceViewModel.showRewardedAd()
                            } else {
                                appearanceViewModel.saveTheme()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_done),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = Outline
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()
        if (appearanceViewModel.isConnected()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = topPadding,
                        bottom = bottomPadding,
                        start = PADDING_MEDIUM2,
                        end = PADDING_MEDIUM2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)
            ) {
                ThemeImage(uiState.selectedTheme)
                CenterFocusedCarousel(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    listOfTheme = uiState.themeList,
                    gridState = gridState,
                    isTablet = isTablet,
                    themeSpace = themeSpace,
                    coroutineScope = coroutine,
                    onThemeSelected = { theme ->
                        appearanceViewModel.setDefaultTheme(theme)
                    }
                )

            }
        } else {
            NotConnectedMessage()
        }
    }
}

@Composable
fun CenterFocusedCarousel(
    modifier: Modifier = Modifier,
    listOfTheme: List<ThemeInfo?>,
    gridState: LazyGridState,
    isTablet: Boolean,
    themeSpace: Dp,
    coroutineScope: CoroutineScope,
    onThemeSelected: (ThemeInfo) -> Unit
) {
    CenterFocusedCarousel(
        modifier = modifier,
        listOfTheme = listOfTheme,
        gridState = gridState,
        content = { firstVisibleIndex, currentIndex ->
            val theme = listOfTheme[currentIndex]

            if (theme != null) {
                val isBigger = firstVisibleIndex + 1 == currentIndex
                val (imageWidth, imageHeight) = calculateImageSize(isTablet, isBigger)
                AsyncImage(
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                        .padding(if (isBigger) 0.dp else 5.dp)
                        .clip(RoundedCornerShape(CORNER_MEDIUM))
                        .clickable {
                            coroutineScope.launch {
                                gridState.animateScrollToItem(
                                    index = maxOf(0, currentIndex - 1)
                                )
                            }
                        },
                    model = theme.themeUrl,
                    contentDescription = theme.themeName,
                    contentScale = ContentScale.Crop
                )
                if (isBigger) {
                    onThemeSelected(theme)
                }
            } else {
                Spacer(modifier = Modifier.width(themeSpace))
            }
        }
    )
}

private fun calculateImageSize(isTablet: Boolean, isBigger: Boolean): Pair<Dp, Dp> {
    return if (isTablet) {
        if (isBigger) 180.dp to 190.dp else 170.dp to 180.dp
    } else {
        if (isBigger) 100.dp to 110.dp else 80.dp to 90.dp
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AppearanceScreenPreview(modifier: Modifier = Modifier) {
    AppearanceScreen(navController = rememberNavController())
}