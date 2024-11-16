package com.umutsaydam.zenfocus.presentation.policy

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.LINE_HEIGHT_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.TEXT_INDENT_SMALL
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar

@Composable
fun PolicyScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //:TODO perform click
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Text(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
                .padding(
                    vertical = paddingValues.calculateTopPadding(),
                    horizontal = PADDING_MEDIUM2
                ),
            text = stringResource(R.string.lorem_ipsum),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium.copy(
                textIndent = TextIndent(firstLine = TEXT_INDENT_SMALL),
                lineHeight = LINE_HEIGHT_MEDIUM
            )
        )
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PolicyScreenPreview(modifier: Modifier = Modifier) {
    PolicyScreen()
}