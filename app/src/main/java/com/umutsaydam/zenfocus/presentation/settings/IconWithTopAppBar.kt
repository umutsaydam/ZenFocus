package com.umutsaydam.zenfocus.presentation.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconWithTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.outline
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    onClick()
                }
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}

