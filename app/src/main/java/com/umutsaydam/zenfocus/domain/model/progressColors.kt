package com.umutsaydam.zenfocus.domain.model

import androidx.compose.ui.graphics.Color

sealed class ProgressColor(
    val colorId: Int,
    val color: Color
) {
    data object Color0 : ProgressColor(0, Color(202, 196, 208))
    data object Color1 : ProgressColor(1, Color(255, 236, 204))
    data object Color2 : ProgressColor(2, Color(222, 231, 145))
    data object Color3 : ProgressColor(3, Color(163, 220, 154))
    data object Color4 : ProgressColor(4, Color(220, 208, 168))
    data object Color5 : ProgressColor(5, Color(74, 151, 130))
    data object Color6 : ProgressColor(6, Color(251, 219, 147))
    data object Color7 : ProgressColor(7, Color(0, 64, 48))
    data object Color8 : ProgressColor(8, Color(254, 119, 67))
    data object Color9 : ProgressColor(9, Color(230, 117, 20))
    data object Color10 : ProgressColor(10, Color(71, 19, 150))
    data object Color11 : ProgressColor(11, Color(84, 89, 172))
    data object Color12 : ProgressColor(12, Color(0, 0, 0))

    companion object {
        val colorList: List<ProgressColor> by lazy {
            listOf(Color0, Color1, Color2, Color3, Color4, Color5, Color6, Color7, Color8, Color9,
                Color10, Color11, Color12)
        }

        fun fromId(colorId: Int): ProgressColor {
            return colorList.find { it.colorId == colorId } ?: Color1
        }
    }
}