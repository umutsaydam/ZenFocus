package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.ui.theme.LightBackground
import com.umutsaydam.zenfocus.ui.theme.LightGray

@Composable
fun NotConnectedMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_no_connection),
                contentDescription = stringResource(R.string.no_connection),
                tint = Color.LightGray
            )
            Text(
                text = stringResource(R.string.no_connection),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = LightGray,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}