package com.example.wanandroidmvp.http

import android.net.Uri.Builder
import com.example.wanandroidmvp.BuildConfig
import com.example.wanandroidmvp.api.ApiService
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.constant.HttpConstant
import com.example.wanandroidmvp.http.interceptor.CacheInterceptor
import com.example.wanandroidmvp.http.interceptor.HeaderInterceptor
import com.example.wanandroidmvp.http.interceptor.SavedCookieInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private var retofit: Retrofit? = null

    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }


    private fun getRetrofit(): Retrofit? {
        if (retofit == null) {
            retofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        val cacheFile = File(App.context.cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)
        builder.run {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(CacheInterceptor())
            addInterceptor(SavedCookieInterceptor())
            cache(cache)
            connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
        return builder.build()
    }
}