package com.umutsaydam.zenfocus.presentation.appearance

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_SMALL1
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_SMALL2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.appearance.components.CenterFocusedCarouselList
import com.umutsaydam.zenfocus.presentation.appearance.components.PreviewTheme
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.NotConnectedMessage
import com.umutsaydam.zenfocus.presentation.viewmodels.AppearanceViewModel
import com.umutsaydam.zenfocus.presentation.viewmodels.VideoPlayerViewModel
import com.umutsaydam.zenfocus.ui.theme.OnPrimary
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.Constants.SELECTING_ANIM_DUR
import com.umutsaydam.zenfocus.util.Constants.THEME_TYPE_IMAGE
import com.umutsaydam.zenfocus.util.Constants.THEME_TYPE_VIDEO
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppearanceScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appearanceViewModel: AppearanceViewModel = hiltViewModel(),
    videoPlayerViewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val uiState by appearanceViewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = remember { configuration.screenWidthDp.dp > 600.dp }
    val themeSpace = remember { if (isTablet) 380.dp else 150.dp }

    LaunchedEffect(uiState.uiMessage, uiState.rewardedAd) {
        uiState.uiMessage?.let { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
            appearanceViewModel.clearUiMessage()
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
                    appearanceViewModel.saveTheme()
                }
            }
        }
    }

    Scaffold(modifier = modifier, containerColor = SurfaceContainerLow, topBar = {
        IconWithTopAppBar(navigationIcon = {
            val onClickAction = remember {
                { navController.popBackStackOrIgnore() }
            }
            IconButton(
                onClick = onClickAction
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.back_to_settings),
                    tint = Outline
                )
            }
        }, containerColor = Color.Transparent, actions = {
            IconButton(onClick = remember {
                {
                    if (appearanceViewModel.isConnected()) {
                        if (appearanceViewModel.willShowAd()) {
                            appearanceViewModel.showRewardedAd()
                        } else {
                            appearanceViewModel.saveTheme()
                        }
                    }
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_done),
                    contentDescription = stringResource(R.string.back_to_settings),
                    tint = Outline
                )
            }
        })
    }) { paddingValues ->
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

                uiState.selectedTheme?.let {
                    if (it.themeType == THEME_TYPE_IMAGE) {
                        PreviewTheme(it)
                    } else {
                        PreviewTheme(
                            it, context, videoPlayerViewModel = videoPlayerViewModel
                        )
                    }
                } ?: Icon(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = painterResource(R.drawable.ic_image),
                    contentDescription = "Default Theme",
                    tint = Color.LightGray
                )


                CenterFocusedCarousel(modifier = Modifier.align(Alignment.CenterHorizontally),
                    listOfTheme = uiState.themeList,
                    gridState = gridState,
                    isTablet = isTablet,
                    themeSpace = themeSpace,
                    coroutineScope = coroutine,
                    onThemeSelected = { theme ->
                        appearanceViewModel.setDefaultTheme(theme)
                    })

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
    CenterFocusedCarouselList(modifier = modifier,
        listOfTheme = listOfTheme,
        gridState = gridState,
        content = { firstVisibleIndex, currentIndex ->
            val theme = listOfTheme[currentIndex]
            if (theme != null) {
                val isBigger = firstVisibleIndex + 1 == currentIndex
                val (imageWidth, imageHeight) = calculateImageSize(isTablet, isBigger)
                val animatedWith by animateDpAsState(
                    targetValue = imageWidth, animationSpec = tween(
                        durationMillis = SELECTING_ANIM_DUR, easing = LinearEasing
                    ), label = "Animated Width"
                )
                val animatedHeight by animateDpAsState(
                    targetValue = imageHeight, animationSpec = tween(
                        durationMillis = SELECTING_ANIM_DUR, easing = LinearEasing
                    ), label = "Animated Height"
                )
                val isVideo = theme.themeType == THEME_TYPE_VIDEO
                Box() {
                    AsyncImage(
                        modifier = Modifier
                            .width(animatedWith)
                            .height(animatedHeight)
                            .padding(if (isBigger) 0.dp else 5.dp)
                            .clip(RoundedCornerShape(CORNER_MEDIUM))
                            .clickable {
                                coroutineScope.launch {
                                    gridState.animateScrollToItem(
                                        index = maxOf(0, currentIndex - 1)
                                    )
                                }
                            },
                        model = if (isVideo) theme.videoThumbnailUrl else theme.themeUrl,
                        contentDescription = theme.themeName,
                        contentScale = ContentScale.Crop
                    )
                    if (isVideo) {
                        Icon(
                            modifier = Modifier
                                .size(if (isBigger) SIZE_SMALL2 else SIZE_SMALL1)
                                .offset(
                                    x = if (isBigger) 5.dp else 0.dp,
                                    y = if (isBigger) (-5).dp else 0.dp
                                )
                                .background(OnPrimary)
                                .clip(RoundedCornerShape(CORNER_SMALL))
                                .align(Alignment.TopEnd),
                            painter = painterResource(R.drawable.ic_videocam),
                            tint = White,
                            contentDescription = "Playable a theme"
                        )
                    }
                }
                if (isBigger) {
                    onThemeSelected(theme)
                }
            } else {
                Spacer(modifier = Modifier.width(themeSpace))
            }
        })
}

private fun calculateImageSize(isTablet: Boolean, isBigger: Boolean): Pair<Dp, Dp> {
    return if (isTablet) {
        if (isBigger) 180.dp to 190.dp else 170.dp to 180.dp
    } else {
        if (isBigger) 100.dp to 110.dp else 80.dp to 90.dp
    }
}