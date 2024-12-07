package com.umutsaydam.zenfocus.presentation.appearance

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
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
                            appearanceViewModel.setDefaultTheme(selectedTheme)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding,
                    bottom = bottomPadding,
                    start = PADDING_MEDIUM2,
                    end = PADDING_MEDIUM2
                ),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                modifier = Modifier
                    .width(215.dp)
                    .height(385.dp)
                    .clip(RoundedCornerShape(CORNER_MEDIUM)),
                painter = if (selectedTheme != null) {
                    rememberAsyncImagePainter(selectedTheme!!.themeUrl)
                } else {
                    painterResource(R.drawable.tomato)
                },
                contentDescription = stringResource(R.string.selected_theme),
                contentScale = ContentScale.Crop
            )

            CenterFocusedCarousel(
                listOfTheme = themeList.value,
                gridState = gridState,
                content = { firstVisibleIndex, currentIndex ->
                    val theme = themeList.value[currentIndex]

                    if (theme != null) {
                        val isBigger = firstVisibleIndex + 1 == currentIndex
                        Log.i("R/T", "Have to be loaded image: $theme")
                        AsyncImage(
                            modifier = Modifier
                                .size(
                                    if (isBigger) 100.dp else 80.dp
                                )
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
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                }
            )
        }
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