package com.umutsaydam.zenfocus.presentation.statistics.components

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomRoundedBarChart(
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val radius: Float
) : BarChartRenderer(chart, animator, viewPortHandler) {

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        for (i in 0 until buffer.size() step 4) {
            val gradientColor = dataSet.gradientColor
            mRenderPaint.setShader(
                LinearGradient(
                    buffer.buffer[i],
                    buffer.buffer[i + 3],
                    buffer.buffer[i],
                    buffer.buffer[i + 1],
                    gradientColor.startColor,
                    gradientColor.endColor,
                    Shader.TileMode.MIRROR
                )
            )

            c.drawRoundRect(
                buffer.buffer[i], buffer.buffer[i + 1], buffer.buffer[i + 2],
                buffer.buffer[i + 3], radius, radius, mRenderPaint
            )
        }
    }
}