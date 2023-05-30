package com.example.wanandroidmvp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App

object ImageLoader {

    private val isLoadImage = !SettingUtil.getIsNoPhotoMode() || NetWorkUtil.isWifi(App.context)

    fun load(context: Context?, url: String?, iv: ImageView?) {
        if (isLoadImage) {
            iv?.apply {
                Glide.with(context!!).clear(iv)
                val options = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.bg_placeholder)
                Glide.with(context!!)
                    .load(url)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(iv)
            }
        }
    }
}