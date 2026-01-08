package com.umutsaydam.zenfocus.presentation.paywall

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL2
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.STROKE_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.THICKNESS_LARGE
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.navigation.Route
import com.umutsaydam.zenfocus.presentation.viewmodels.PaywallViewModel
import com.umutsaydam.zenfocus.ui.theme.Black
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.Primary
import com.umutsaydam.zenfocus.ui.theme.Secondary
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore
import com.umutsaydam.zenfocus.util.safeNavigate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PaywallScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    paywallViewModel: PaywallViewModel = hiltViewModel()
) {
    val plans by paywallViewModel.plans.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        launch {
            paywallViewModel.sharedMessage.collectLatest { resId ->
                Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
            }
        }

        launch {
            paywallViewModel.shouldPopStack.collectLatest {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceContainerLow),
        containerColor = Transparent,
        topBar = { PaywallScreenTopBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_app_icon_circle),
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.paywall_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            )
            Text(
                modifier = Modifier.padding(PADDING_SMALL2),
                text = stringResource(R.string.paywall_description),
                style = MaterialTheme.typography.bodyMedium,
                color = Outline,
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(alignment = Alignment.CenterHorizontally),
                thickness = THICKNESS_LARGE,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            PremiumFeatures()
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(alignment = Alignment.CenterHorizontally),
                thickness = THICKNESS_LARGE,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            if (plans.isNotEmpty()) {
                plans.forEach { plan ->
                    PlanItem(
                        planTitle = plan.name,
                        planPrice = plan.oneTimePurchaseOfferDetails?.formattedPrice ?: ""
                    )
                }
            }

            TextButton(
                onClick = {
                    paywallViewModel.buySelectedPlan(context as Activity)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PADDING_SMALL2),
                shape = RoundedCornerShape(CORNER_SMALL),
                colors = ButtonColors(
                    containerColor = Primary,
                    contentColor = Secondary,
                    disabledContentColor = Secondary,
                    disabledContainerColor = Secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.message_continue),
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
            }
            Text(
                modifier = Modifier.padding(PADDING_SMALL),
                text = stringResource(R.string.paywall_sub_description),
                style = MaterialTheme.typography.labelMedium,
                color = Secondary,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.padding(PADDING_SMALL),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SubContent(stringResource(R.string.restore)) {
                    paywallViewModel.restorePreviousPurchaseIfAvailable()
                }
                SubContent(stringResource(R.string.privacy_policy)) {
                    navController.safeNavigate(Route.Policy.route)
                }
            }
        }
    }
}

@Composable
fun SubContent(
    title: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Secondary
        )
    }
}

@Composable
fun PaywallScreenTopBar(navController: NavController) {
    IconWithTopAppBar(
        navigationIcon = {
            IconButton(onClick = remember { { navController.popBackStackOrIgnore() } }) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.back_to_settings),
                    tint = Outline
                )
            }
        },
        containerColor = Transparent
    )
}

@Composable
fun PremiumFeatures() {
    val features = stringArrayResource(R.array.paywall_features)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        features.forEach { feature ->
            Row {
                Image(
                    painter = painterResource(R.drawable.ic_tick_primary),
                    contentDescription = ""
                )
                Spacer(Modifier.width(SPACE_SMALL))
                Text(
                    text = feature,
                    style = MaterialTheme.typography.titleMedium,
                    color = Black
                )
            }
        }
    }
}

@Composable
fun PlanItem(modifier: Modifier = Modifier, planTitle: String, planPrice: String) {
    Row(
        modifier = modifier
            .width(300.dp)
            .border(STROKE_SMALL, Primary, RoundedCornerShape(CORNER_MEDIUM)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = true,
            onClick = {},
            colors = RadioButtonColors(
                selectedColor = Primary,
                unselectedColor = Secondary,
                disabledSelectedColor = Secondary,
                disabledUnselectedColor = Secondary
            )
        )
        Text(
            text = planTitle,
            style = MaterialTheme.typography.titleMedium,
            color = Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(CORNER_SMALL))
                .background(Primary)
                .padding(PADDING_SMALL),
        ) {
            Text(
                text = planPrice,
                style = MaterialTheme.typography.titleMedium,
                color = White
            )
        }
        Spacer(modifier = Modifier.width(SPACE_SMALL))
    }
}
