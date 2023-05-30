package com.example.wanandroidmvp.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtil {

    fun isHideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val bottom = top + view.height
            val right = left + view.width
            val isInt = (event.x > left && event.x < right && event.y > top && event.y < bottom)
            return !isInt
        }
        return false
    }

    fun hideKeyboard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun showKeyboard(context: Context, mEditText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}