package com.umutsaydam.zenfocus.presentation.colorSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.ColorSelectionEnum
import com.umutsaydam.zenfocus.domain.model.SessionStateEnum
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE1
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.appearance.components.PreviewThemeWithProgressBar
import com.umutsaydam.zenfocus.presentation.auth.components.CustomTabButton
import com.umutsaydam.zenfocus.presentation.colorSelection.components.MockCircularProgressWithText
import com.umutsaydam.zenfocus.presentation.common.CenterFocusedCarouselGeneric
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.viewmodels.ColorSelectionViewModel
import com.umutsaydam.zenfocus.presentation.viewmodels.VideoPlayerViewModel
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun ColorSelection(
    modifier: Modifier = Modifier,
    navController: NavHostController?,
    colorSelectionViewModel: ColorSelectionViewModel = hiltViewModel()
) {
    val currentProgressTrackColor by colorSelectionViewModel.currentProgressTrackColor.collectAsState()
    val currentTextColor by colorSelectionViewModel.currentTextColor.collectAsState()
    val sessionStateEnum by colorSelectionViewModel.sessionStateEnum.collectAsState()
    val colorSelectionEnum by colorSelectionViewModel.colorSelectionEnum.collectAsState()
    val defaultThemeName by colorSelectionViewModel.defaultThemeName.collectAsState()
    val defaultTheme by colorSelectionViewModel.defaultTheme.collectAsState()
    var isThemeAnImage: Boolean? by remember { mutableStateOf(null) }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = remember { configuration.screenWidthDp.dp > 600.dp }
    val themeSpace = remember { if (isTablet) 380.dp else 150.dp }

    LaunchedEffect(defaultTheme) {
        isThemeAnImage = colorSelectionViewModel.isThemeAnImage()
    }

    Scaffold(modifier = modifier, containerColor = SurfaceContainerLow, topBar = {
        IconWithTopAppBar(navigationIcon = {
            val onClickAction = remember {
                { navController!!.popBackStackOrIgnore() }
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
                { colorSelectionViewModel.saveAllTrackColors() }
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
            if (defaultThemeName != null && isThemeAnImage != null) {
                Box(contentAlignment = Alignment.Center) {
                    if (isThemeAnImage!!) {
                        PreviewThemeWithProgressBar(defaultTheme!!)
                    } else {
                        val videoPlayerViewModel: VideoPlayerViewModel = hiltViewModel()
                        PreviewThemeWithProgressBar(
                            defaultThemeName!!,
                            context,
                            videoPlayerViewModel
                        )
                    }

                    if (currentProgressTrackColor != null && currentTextColor != null) {
                        MockClickablePomodoroProgressWithText(
                            colorSelectionEnum = colorSelectionEnum,
                            progressColor = currentProgressTrackColor!!.color,
                            textColor = currentTextColor!!.color,
                            onProgressClick = {
                                colorSelectionViewModel.setProgressColorSelectionEnum()
                            },
                            onTextClick = {
                                colorSelectionViewModel.setTextColorSelectionEnum()
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(CORNER_SMALL))
                            .padding(horizontal = PADDING_SMALL),
                    ) {
                        SessionStateEnum.entries.forEach { currSessionState ->
                            CustomTabButton(
                                isSelected = sessionStateEnum.name == currSessionState.name,
                                onClick = {
                                    colorSelectionViewModel.setSessionState(currSessionState)
                                    colorSelectionViewModel.setCurrentProgressTrackColorBySessionState(
                                        currSessionState
                                    )
                                    colorSelectionViewModel.setCurrentTextColorBySessionState(
                                        currSessionState
                                    )
                                    colorSelectionViewModel.setTextColorSelectionEnum()
                                },
                                buttonText = stringResource(
                                    if (currSessionState == SessionStateEnum.WORK_SESSION) {
                                        R.string.work_session
                                    } else {
                                        R.string.break_session
                                    }
                                )
                            )
                        }
                    }
                }

                CenterFocusedCarouselGeneric(
                    items = colorSelectionViewModel.colorList,
                    gridState = colorSelectionViewModel.getGridStateForSession(colorSelectionEnum),
                    isTablet = isTablet,
                    themeSpace = themeSpace,
                    coroutineScope = coroutine,
                    onItemSelected = { color ->
                        colorSelectionViewModel.setColorByEnumValue(color)
                    }
                ) { item, isFocused, animatedWidth, animatedHeight, onClick ->
                    item?.let {
                        Box(
                            modifier = Modifier
                                .width(animatedWidth)
                                .height(animatedHeight)
                                .padding(if (isFocused) 0.dp else 5.dp)
                                .clip(RoundedCornerShape(CORNER_MEDIUM))
                                .background(color = it.color)
                                .clickable {
                                    onClick()
                                }
                        ) {
                            Text("Debug Mode", color = it.color)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MockClickablePomodoroProgressWithText(
    colorSelectionEnum: ColorSelectionEnum,
    progressColor: Color,
    textColor: Color,
    onProgressClick: () -> Unit,
    onTextClick: () -> Unit
) {
    MockCircularProgressWithText(
        colorSelectionEnum = colorSelectionEnum,
        size = SIZE_LARGE1,
        progress = 0.7f,
        isWorking = true,
        progressColor = progressColor,
        strokeCap = StrokeCap.Round,
        text = "12:25",
        textColor = textColor,
        style = MaterialTheme.typography.headlineSmall,
        onProgressClick = onProgressClick,
        onTextClick = onTextClick
    )
}