package com.example.wanandroidmvp.utils

import android.graphics.Color
import android.preference.PreferenceManager
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App

object SettingUtil {
    private val setting = PreferenceManager.getDefaultSharedPreferences(App.context)

    fun getIsNoPhotoMode(): Boolean {
        return setting.getBoolean("switch_noPhotoMode", false)
    }

    fun getIsShowTopArticle(): Boolean {
        return setting.getBoolean("switch_show_top", false)
    }

    fun getColor(): Int {
        val defaultColor = App.context.resources.getColor(R.color.colorPrimary)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else color
    }

    fun setColor(color: Int) {
        setting.edit().putInt("color", color).apply()
    }

    fun getNavBar(): Boolean {
        return setting.getBoolean("nav_bar", false)
    }

    fun setIsNightMode(flag: Boolean) {
        setting.edit().putBoolean("switch_nightMode", flag).apply()
    }

    fun getIsNightMode(): Boolean {
        return setting.getBoolean("switch_nightMode", false)
    }

    fun getIsAutoNightMode(): Boolean {
        return setting.getBoolean("auto_nightMode", false)
    }

    fun getNightStartHour(): String {
        return setting.getString("night_StartHour", "22") ?: "22"
    }

    fun setNightStartHour(nightStartHour: String) {
        setting.edit().putString("night_startHour", nightStartHour).apply()
    }

    fun getNightStartMinute(): String {
        return setting.getString("night_startMinute", "00") ?: "00"
    }

    fun setNightStartMinute(nightStartMinute: String) {
        setting.edit().putString("night_startMinute", nightStartMinute).apply()
    }

    fun getDayStartHour(): String {
        return setting.getString("day_startHour", "06") ?: "06"
    }

    fun setDarStartHour(dayStartHour: String) {
        setting.edit().putString("day_startHour", dayStartHour).apply()
    }

    fun getDayStartMinute(): String {
        return setting.getString("day_startMinute", "00") ?: "00"
    }

    fun setDayStartMinute(dayStartMinute: String) {
        setting.edit().putString("day_startMinute", dayStartMinute).apply()
    }
}