package com.example.wanandroidmvp.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

object ColorUtil {
    fun alphaColor(
        @ColorInt color: Int,
        @androidx.annotation.IntRange(from = 0, to = 255) alpha: Int
    ): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    fun alphaColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return alphaColor(color, (alpha * 255).toInt())
    }
}