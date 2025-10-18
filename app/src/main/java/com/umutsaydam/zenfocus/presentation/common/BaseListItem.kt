package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BaseListItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    onClick: () -> Unit,
    iconRes: Int,
    contentDesc: String,
    trailingContent: @Composable () -> Unit = {}
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        headlineContent = { Text(title) },
        supportingContent = { if(description.isNotEmpty()) Text(description) },
        leadingContent = {
            Icon(painter = painterResource(iconRes), contentDescription = contentDesc)
        },
        trailingContent = trailingContent
    )
    HorizontalDivider(
        thickness = 0.3.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}