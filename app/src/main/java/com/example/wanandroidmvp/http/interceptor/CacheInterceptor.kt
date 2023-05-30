package com.example.wanandroidmvp.http.interceptor

import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.utils.NetWorkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetWorkUtil.isNetWorkAvailable(App.context)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (NetWorkUtil.isNetWorkAvailable(App.context)) {
            val maxAge = 60 * 3
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Retrofit")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("nyn")
                .build()
        }
        return response
    }
}