package com.example.wanandroidmvp.utils

import android.content.Context
import android.net.ConnectivityManager
import java.net.NetworkInterface
import java.net.SocketException

object NetWorkUtil {
    private const val TIMEOUT = 3000

    //check NetworkAvailable
    fun isNetWorkAvailable(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }

    //check NetworkConnected
    fun isNetworkConnected(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isConnected)
    }

    //获取本地IP地址
    fun getLocalIPAddress(): String {
        var ret = ""
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val enumIpAddress = en.nextElement().inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val netAddress = enumIpAddress.nextElement()
                    if (!netAddress.isLoopbackAddress) {
                        ret = netAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ret
    }

    fun isWifi(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
    }
}