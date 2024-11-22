package com.umutsaydam.zenfocus.presentation.auth

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.BORDER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM2
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SIZE_LARGE
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val currPage = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            IconWithTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.back_to_settings),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
    ) { paddingValues ->
        val verticalPadding = paddingValues.calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(
                    vertical = verticalPadding,
                    horizontal = PADDING_MEDIUM2
                ),
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM),
        ) {
            Spacer(modifier = Modifier.height(SPACE_MEDIUM))
            AuthSection {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Login to access your account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(CORNER_SMALL))
                        .border(
                            width = BORDER_SMALL,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = PADDING_SMALL),
                ) {
                    CustomTabButton(
                        isSelected = currPage == 0,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(0)
                            }
                        },
                        buttonText = stringResource(R.string.sign_in)
                    )
                    Spacer(modifier = Modifier.width(SPACE_SMALL))
                    CustomTabButton(
                        isSelected = currPage == 1,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(1)
                            }
                        },
                        buttonText = stringResource(R.string.sign_up)
                    )
                }
            }


            HorizontalPager(
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> AuthSection(
                        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1),
                        content = {
                            AuthForm(
                                buttonText = stringResource(R.string.sign_in),
                                onClick = { email, password ->
                                    //:TODO perform click

                                })
                        }
                    )

                    1 -> AuthSection(
                        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM1),
                        content = {
                            AuthForm(
                                buttonText = stringResource(R.string.sign_up),
                                onClick = { email, password ->
                                    //:TODO perform click

                                })
                        }
                    )
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (currPage == 0)
                    stringResource(R.string.or_sign_in_with)
                else stringResource(R.string.or_sign_up_with),
                textAlign = TextAlign.Center
            )

            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {

                },
                shape = RoundedCornerShape(CORNER_SMALL),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Image(
                    modifier = Modifier
                        .size(SIZE_LARGE)
                        .padding(horizontal = PADDING_SMALL),
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = stringResource(R.string.join_with_google)
                )
                Text(
                    text = stringResource(R.string.google),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun AuthSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        content()
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AuthScreenPreview(modifier: Modifier = Modifier) {
    AuthScreen(navController = rememberNavController())
}