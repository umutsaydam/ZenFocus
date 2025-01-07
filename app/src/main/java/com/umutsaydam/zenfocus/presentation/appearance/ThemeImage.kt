package com.umutsaydam.zenfocus.presentation.appearance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.data.remote.dto.ThemeInfo

@Composable
fun ThemeImage(selectedTheme: ThemeInfo?) {
    if (selectedTheme != null) {
        Image(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .clip(RoundedCornerShape(16.dp)),
            painter = rememberAsyncImagePainter(selectedTheme.themeUrl),
            contentDescription = "Selected Theme",
            contentScale = ContentScale.Crop
        )
    } else {
        Icon(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            painter = painterResource(R.drawable.ic_image),
            contentDescription = "Default Theme",
            tint = Color.LightGray
        )
    }
}