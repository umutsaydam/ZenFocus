package com.umutsaydam.zenfocus.presentation.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.model.GradientColor
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_SMALL
import com.umutsaydam.zenfocus.presentation.Dimens.SPACE_MEDIUM
import com.umutsaydam.zenfocus.presentation.common.IconWithTopAppBar
import com.umutsaydam.zenfocus.presentation.common.StatusBarSwitcher
import com.umutsaydam.zenfocus.ui.theme.Outline
import com.umutsaydam.zenfocus.ui.theme.SurfaceContainerLow
import com.umutsaydam.zenfocus.ui.theme.Transparent
import com.umutsaydam.zenfocus.ui.theme.White
import com.umutsaydam.zenfocus.util.popBackStackOrIgnore

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val options = listOf("Select dates", "This month", "Last week", "This week")
    var selectedIndex by remember { mutableIntStateOf(options.size - 1) }
    val rememberScrollState = rememberScrollState()
    val context = LocalContext.current

    StatusBarSwitcher(true)

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(
            color = SurfaceContainerLow
        ),
        containerColor = Transparent,
        topBar = { StatisticsScreenTopBar(navController) }
    ) { paddingValues ->
        val topPadding = paddingValues.calculateTopPadding()
        val bottomPadding = paddingValues.calculateBottomPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding,
                    bottom = bottomPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SPACE_MEDIUM)
        ) {
            SingleChoiceSegmentedButtons(
                rememberScrollState = rememberScrollState,
                options = options,
                selectedIndex = selectedIndex,
                onSelectedIndexChanged = { newIndex ->
                    selectedIndex = newIndex
                }
            )

            Card(
                modifier = Modifier
                    .padding(PADDING_SMALL)
                    .fillMaxWidth()
                    .background(White),
                shape = RoundedCornerShape(CORNER_MEDIUM),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
//              4CD9F2  5399E8
                val completedPomodoroThisWeek = listOf(3f, 5f, 1f, 8f, 3f, 2f, 4f)

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    factory = {
                        BarChart(context).apply {

                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            description.isEnabled = true
                            description.text = "Description"
                            setDrawGridBackground(true)
                            setDrawBarShadow(false)
                            legend.isEnabled = true

                            val entries = completedPomodoroThisWeek.mapIndexed { index, value ->
                                BarEntry(index.toFloat(), value)
                            }

                            val dataSet =
                                BarDataSet(entries, "Bu hafta tamamlanan pomodoro sayısı").apply {
                                    setGradientColor(
                                        Color.parseColor("#5399E8"),
                                        Color.parseColor("#4CD9F2")
                                    )
                                    valueTextSize = 12f
                                    valueTypeface = Typeface.DEFAULT
                                }

                            val barData = BarData(dataSet)
                            barData.barWidth = 0.7f
                            data = barData
                            renderer = CustomRoundedBarChart(this, animator, viewPortHandler, 30f)
                            invalidate()

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(true)
                                granularity = 1f
                            }

                            axisLeft.apply {
                                setDrawGridLines(true)
                                axisMinimum = 0f
                            }

                            axisRight.isEnabled = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StatisticsScreenTopBar(navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }

    IconWithTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStackOrIgnore()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.back_to_settings),
                    tint = Outline
                )
            }
        },
        containerColor = SurfaceContainerLow,
        actions = {
            IconButton(
                onClick = { isExpanded = !isExpanded }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Outline
                )
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Back up to cloud") },
                    onClick = {
                        // perform backing up to cloud...
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_cloud),
                            contentDescription = "Back up to cloud",
                            tint = Outline
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun SingleChoiceSegmentedButtons(
    rememberScrollState: ScrollState,
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChanged: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState)
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                modifier = Modifier
                    .wrapContentWidth(),
                selected = selectedIndex == index,
                onClick = {
                    onSelectedIndexChanged(index)
                },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                )
            )
        }
    }
}