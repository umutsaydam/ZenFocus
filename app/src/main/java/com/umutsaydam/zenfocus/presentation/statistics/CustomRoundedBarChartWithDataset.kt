package com.umutsaydam.zenfocus.presentation.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.umutsaydam.zenfocus.presentation.Dimens.PADDING_MEDIUM1
import com.umutsaydam.zenfocus.ui.theme.Transparent

@Composable
fun CustomRoundedBarChartWithDataset(
    numberOfCompletedPomodoroDataset: List<Float>,
    datesOfCompletedPomodoroDataset: List<String>
) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier
            .background(Transparent)
            .fillMaxWidth()
            .height(300.dp)
            .padding(PADDING_MEDIUM1),
        factory = {
            BarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = true
                setBackgroundColor(Color.WHITE)
                setDrawBarShadow(false)
                legend.isEnabled = false
                description.text = ""
                setScaleEnabled(false)

                val entries = numberOfCompletedPomodoroDataset.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value)
                }

                val dataSet =
                    BarDataSet(entries, "").apply {
                        setGradientColor(
                            Color.parseColor("#5399E8"),
                            Color.parseColor("#4CD9F2")
                        )
                        valueTextSize = 12f
                        valueTypeface = Typeface.DEFAULT
                    }
                dataSet.highLightAlpha = 0

                val barData = BarData(dataSet)
                barData.barWidth = 0.5f
                data = barData
                renderer =
                    CustomRoundedBarChart(this, animator, viewPortHandler, 30f)
                invalidate()

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(true)
                    granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return if (value.toInt() in datesOfCompletedPomodoroDataset.indices) datesOfCompletedPomodoroDataset[value.toInt()] else value.toString()
                        }
                    }
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    axisMinimum = 0f
                }

                axisRight.isEnabled = false
            }
        },
        update = { barChart ->
            val entries = numberOfCompletedPomodoroDataset.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }

            val dataSet = BarDataSet(entries, "").apply {
                setGradientColor(
                    Color.parseColor("#5399E8"),
                    Color.parseColor("#4CD9F2")
                )
                valueTextSize = 12f
                valueTypeface = Typeface.DEFAULT
            }
            dataSet.highLightAlpha = 0

            val barData = BarData(dataSet)
            barData.barWidth = 0.5f
            barChart.data = barData
            barChart.invalidate()
        }
    )
}