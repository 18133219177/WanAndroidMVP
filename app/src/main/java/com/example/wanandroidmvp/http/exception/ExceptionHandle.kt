package com.example.wanandroidmvp.http.exception

import com.google.gson.JsonParseException
import com.orhanobut.logger.Logger
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class ExceptionHandle {
    companion object {
        private const val TAG = "ExceptionHandle"
        var errorCode = ErrorState.UNKNOWN_ERROR
        var errorMsg = "请求失败，请稍后重试"

        fun handleException(e: Throwable): String {
            e.printStackTrace()
            if (e is SocketTimeoutException
                || e is ConnectException
                || e is HttpException
            ) {
                Logger.e(TAG, "网络连接异常:" + e.message)
                errorMsg = "网络连接异常"
                errorCode = ErrorState.NETWORK_ERROR
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                Logger.e(TAG, "数据解析异常: " + e.message)
                errorMsg = "数据解析异常"
                errorCode = ErrorState.SERVER_ERROR
            } else if (e is ApiException) {
                errorMsg = e.message.toString()
                errorCode = ErrorState.SERVER_ERROR
            } else if (e is UnknownHostException) {
                Logger.e(TAG, "网络连接异常: " + e.message)
                errorMsg = "网络连接异常"
                errorCode = ErrorState.SERVER_ERROR
            } else {
                try {
                    Logger.e(TAG, "错误: " + e.message)
                } catch (e1: Exception) {
                    Logger.e(TAG, "未知错误")
                }
                errorMsg = "未知错误，可能抛锚了吧~"
                errorCode = ErrorState.UNKNOWN_ERROR
            }
            return errorMsg
        }
    }
}