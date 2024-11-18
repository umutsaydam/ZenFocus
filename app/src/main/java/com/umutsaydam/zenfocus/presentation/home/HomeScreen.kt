package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_XLARGE
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RectangleShape
            )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.lofi1),
            contentDescription = "Selected theme",
            contentScale = ContentScale.Fit
        )

        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                IconWithTopAppBar(
                    title = {

                    },
                    containerColor = Color.Transparent,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                //:TODO perform navigate
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = stringResource(R.string.open_side_menu),
                                tint = Color.White
                            )
                        }
                    }
                ) {

                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(SIZE_XLARGE),
                        progress = {
                            0.6f
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        strokeWidth = STROKE_MEDIUM,
                        trackColor = MaterialTheme.colorScheme.outlineVariant,
                        strokeCap = StrokeCap.Round,
                    )
                    Text(
                        text = "25:00",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(SPACE_MEDIUM))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PADDING_SMALL, Alignment.CenterHorizontally)
                ) {
                    FocusControlButtons(
                        onClick = {
                            //TODO: perform click
                        },
                        painterResource = painterResource(R.drawable.ic_time),
                        contentDescription = stringResource(R.string.pomodoro_times)
                    )
                    FocusControlButtons(
                        onClick = {
                            //TODO: perform click
                        },
                        painterResource = painterResource(R.drawable.ic_play_arrow),
                        contentDescription = stringResource(R.string.pomodoro_start)
                    )
                    FocusControlButtons(
                        onClick = {
                            //TODO: perform click
                        },
                        painterResource = painterResource(R.drawable.ic_music),
                        contentDescription = stringResource(R.string.pomodoro_sounds)
                    )
                }
            }
        }
    }
}