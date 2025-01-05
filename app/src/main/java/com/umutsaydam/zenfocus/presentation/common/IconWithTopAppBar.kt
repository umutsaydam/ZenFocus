package com.umutsaydam.zenfocus.presentation.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconWithTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = SurfaceContainerLow,
    contentColor: Color = Outline,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = title ?: {},
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = containerColor,
            titleContentColor = contentColor
        ),
        navigationIcon = navigationIcon ?: {},
        actions = actions
    )
}

