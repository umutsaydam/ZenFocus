package com.umutsaydam.zenfocus.presentation.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    containerColor: Color = White,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        onDismissRequest = {
            onDismissRequest()
        },
        shape = RoundedCornerShape(CORNER_SMALL),
        containerColor = containerColor,
        contentWindowInsets = {
            WindowInsets.ime
        }
    ) {
        content()
    }
}