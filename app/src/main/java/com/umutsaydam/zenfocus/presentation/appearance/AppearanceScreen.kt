package com.umutsaydam.zenfocus.presentation.appearance

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import coil3.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.umutsaydam.zenfocus.BuildConfig
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.NotConnectedMessage
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import kotlinx.coroutines.launch

@Composable
fun AppearanceScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appearanceViewModel: AppearanceViewModel = hiltViewModel()
) {
    val themeList = appearanceViewModel.themeList.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutine = rememberCoroutineScope()
    var selectedTheme by remember { mutableStateOf(appearanceViewModel.defaultTheme.value) }
    val context = LocalContext.current
    var rewardedAd: RewardedAd? by remember { mutableStateOf(null) }
    val isTablet = LocalConfiguration.current.screenWidthDp.dp > 600.dp
    val themeSpace = if (isTablet) 380.dp else 80.dp

    LaunchedEffect(rewardedAd) {
        rewardedAd?.let { rewardAd ->
            val activity = context as? Activity
            activity?.let {
                rewardAd.show(
                    it
                ) { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.i("A/D", "user earned the reward")
                    Log.i("A/D", "$rewardAmount, $rewardType")
                    appearanceViewModel.setDefaultTheme(selectedTheme)
                }
            } ?: run {
                Log.i("A/D", "activity is null")
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                containerColor = Color.Transparent,
                actions = {
                    IconButton(
                        onClick = {
                            val adRequest = AdRequest.Builder().build()
                            RewardedAd.load(
                                context,
                                BuildConfig.AD_REWARD_THEME_UNIT_ID,
                                adRequest,
                                object : RewardedAdLoadCallback() {
                                    override fun onAdLoaded(p0: RewardedAd) {
                                        super.onAdLoaded(p0)
                                        Log.i("A/D", "Reward Ad loaded")
                                        rewardedAd = p0
                                    }

                                    override fun onAdFailedToLoad(p0: LoadAdError) {
                                        super.onAdFailedToLoad(p0)
                                        Log.i("A/D", "Reward Ad failed to load")
                                        rewardedAd = null
                                    }
                                }
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_done),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = MaterialTheme.colorScheme.outline
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
                if (selectedTheme != null) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                            .clip(RoundedCornerShape(CORNER_MEDIUM)),
                        painter = rememberAsyncImagePainter(selectedTheme!!.themeUrl),
                        contentDescription = stringResource(R.string.selected_theme),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .width(205.dp)
                            .height(375.dp)
                            .clip(RoundedCornerShape(CORNER_MEDIUM)),
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = stringResource(R.string.selected_theme),
                        tint = Color.LightGray
                    )
                }
                CenterFocusedCarousel(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    listOfTheme = themeList.value,
                    gridState = gridState,
                    content = { firstVisibleIndex, currentIndex ->
                        val theme = themeList.value[currentIndex]

                        if (theme != null) {
                            val isBigger = firstVisibleIndex + 1 == currentIndex
                            Log.i("R/T", "Have to be loaded image: $theme")
                            val (imageWidth, imageHeight) = calculateImageSize(isTablet, isBigger)
                            AsyncImage(
                                modifier = Modifier
                                    .width(imageWidth)
                                    .height(imageHeight)
                                    .padding(if (isBigger) 0.dp else 5.dp)
                                    .clip(RoundedCornerShape(CORNER_MEDIUM))
                                    .clickable {
                                        coroutine.launch {
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
                                selectedTheme = theme
                            }
                        } else {
                            Spacer(modifier = Modifier.width(themeSpace))
                        }
                    }
                )
            }
        } else {
            NotConnectedMessage()
        }
    }
}

fun calculateImageSize(isTablet: Boolean, isBigger: Boolean): Pair<Dp, Dp> {
    return if (isTablet){
        if (isBigger) 180.dp to 190.dp else 170.dp to 180.dp
    }else{
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