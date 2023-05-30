package com.example.wanandroidmvp.utils

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Field
import java.util.Random

object CommonUtil {
    init {
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelOffset(resourceId)
    }

    fun randomColor(): Int {
        val random = Random()
        var red = random.nextInt(190)
        var green = random.nextInt(190)
        var blue = random.nextInt(190)
        if (SettingUtil.getIsNightMode()) {
            //150-255
            red = random.nextInt(105) + 150
            green = random.nextInt(105) + 150
            blue = random.nextInt(105) + 150
        }
        return Color.rgb(red, green, blue)
    }

    fun fixInputMethodManagerLeak(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var field: Field? = null
        var objGet: Any? = null
        for (i in arr.indices) {
            val param = arr[i]
            try {
                field = imm.javaClass.getDeclaredField(param)
                if (field.isAccessible === false) {
                    field.isAccessible = true
                }
                objGet = field.get(imm)
                if (objGet != null && objGet is View) {
                    val view = objGet
                    if (view.context === context) {
                        field.set(imm, null)
                    } else {
                        break
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
    fun getProcessName(pid: Int): String {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ""
    }
}