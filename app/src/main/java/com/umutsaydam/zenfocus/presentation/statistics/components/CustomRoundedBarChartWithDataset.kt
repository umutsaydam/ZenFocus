package com.umutsaydam.zenfocus.presentation.statistics.components

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import com.umutsaydam.zenfocus.ui.theme.LightBlue
import com.umutsaydam.zenfocus.ui.theme.SemiLightBlue
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
                isScaleXEnabled = true
                isDragEnabled = true
                setPinchZoom(false)

                val entries = numberOfCompletedPomodoroDataset.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value)
                }

                val dataSet =
                    BarDataSet(entries, "").apply {
                        setGradientColor(
                            SemiLightBlue.toArgb(),
                            LightBlue.toArgb()
                        )
                        valueTextSize = 12f
                        valueTypeface = Typeface.DEFAULT
                    }
                dataSet.highLightAlpha = 0

                val barData = BarData(dataSet)
                barData.barWidth = 0.3f
                data = barData
                renderer =
                    CustomRoundedBarChart(this, animator, viewPortHandler, 30f)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(true)
                    isGranularityEnabled = true
                    granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return if (value.toInt() in datesOfCompletedPomodoroDataset.indices) datesOfCompletedPomodoroDataset[value.toInt()] else value.toString()
                        }
                    }
                    labelRotationAngle = -45f
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    axisMinimum = 0f
                }

                axisRight.isEnabled = false
                invalidate()
            }
        },
        update = { barChart ->
            val entries = numberOfCompletedPomodoroDataset.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }

            val dataSet = BarDataSet(entries, "").apply {
                setGradientColor(
                    SemiLightBlue.toArgb(),
                    LightBlue.toArgb()
                )
                valueTextSize = 12f
                valueTypeface = Typeface.DEFAULT
            }

            dataSet.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
            dataSet.highLightAlpha = 0

            val barData = BarData(dataSet)
            barData.barWidth = 0.3f
            barChart.data = barData

            barChart.xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value.toInt() in datesOfCompletedPomodoroDataset.indices) datesOfCompletedPomodoroDataset[value.toInt()] else value.toString()
                    }
                }
            }
            barChart.fitScreen()
            barChart.invalidate()
        }
    )
}