package com.example.wanandroidmvp.ext

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.wanandroidmvp.BuildConfig
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.widget.CustomToast
import com.google.android.material.snackbar.Snackbar
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.acos

fun Any.loge(content: String?) {
    loge(this.javaClass.simpleName ?: App.TAG, content ?: "")
}

fun loge(tag: String, content: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, content ?: "")
    }
}

fun showToast(content: String) {
    CustomToast(App.context, content).show()
}

fun Fragment.showToast(content: String) {
    CustomToast(this.requireContext(), content).show()
}

fun Context.showToast(content: String) {
    CustomToast(this, content).show()
}

fun Activity.showSnackMsg(msg: String) {
    val snackbar = Snackbar.make(this.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColor(ContextCompat.getColor(this, R.color.white))
    snackbar.show()
}

fun Fragment.showSnackMsg(msg: String) {
    this.activity ?: return
    val snackbar =
        Snackbar.make(this.requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColor(ContextCompat.getColor(this.requireActivity(), R.color.white))
    snackbar.show()
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0


fun String.getAgentWeb(
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webViewClient: WebViewClient?,
    webChromeClient: WebChromeClient?,
    indicatorColor: Int
): AgentWeb = AgentWeb.with(activity)
    .setAgentWebParent(webContent, 1, layoutParams)
    .useDefaultIndicator(indicatorColor, 2)
    .setWebView(webView)
    .setWebViewClient(webViewClient)
    .setWebChromeClient(webChromeClient)
    .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
    .interceptUnkownUrl()
    .createAgentWeb()
    .ready()
    .go(this)

fun formatCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

fun String.stringToCalendar(): Calendar {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val date=sdf.parse(this)
    val calendar=Calendar.getInstance()
    calendar.time=date
    return calendar
}
