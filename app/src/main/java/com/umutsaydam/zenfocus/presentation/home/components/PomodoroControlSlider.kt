package com.umutsaydam.zenfocus.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BUTTON_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.ui.theme.SecondaryContainer
import com.umutsaydam.zenfocus.ui.theme.TomatoRed
import com.umutsaydam.zenfocus.ui.theme.Transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroControlSlider(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    steps: Int,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onClick: (Float) -> Unit
) {
    var defaultSliderPosition by remember {
        mutableFloatStateOf(sliderPosition)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PADDING_MEDIUM2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1)
    ) {
        Slider(
            value = defaultSliderPosition,
            onValueChange = { defaultSliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = Transparent,
                activeTrackColor = TomatoRed,
                inactiveTrackColor = SecondaryContainer
            ),
            steps = steps,
            valueRange = valueRange,
            thumb = {
                Image(
                    painter = painterResource(R.drawable.tomato),
                    contentDescription = "",
                    contentScale = ContentScale.Fit
                )
            }
        )
        Text(text = defaultSliderPosition.toInt().toString())

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT_MEDIUM),
            onClick = {
                onClick(defaultSliderPosition)
            }
        ) {
            Text(
                text = stringResource(R.string.select)
            )
        }
    }
}