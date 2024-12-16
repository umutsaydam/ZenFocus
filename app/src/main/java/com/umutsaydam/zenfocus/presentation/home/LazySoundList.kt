package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL

@Composable
fun LazySoundList(
    modifier: Modifier = Modifier,
    soundList: Array<String>,
    content: @Composable (Int) -> Unit,
    fixedContent: @Composable (Int) -> Unit
) {
    var selectedSoundIndex by remember {
        mutableStateOf(0)
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            soundList.forEachIndexed { index, _ ->
                content(index)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(PADDING_SMALL)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.BottomCenter
        ) {
            fixedContent(selectedSoundIndex)
        }
    }
}