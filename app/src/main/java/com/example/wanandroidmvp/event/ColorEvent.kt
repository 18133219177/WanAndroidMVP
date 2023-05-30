package com.example.wanandroidmvp.event

import com.example.wanandroidmvp.utils.SettingUtil

class ColorEvent(var isRefresh: Boolean, var color: Int = SettingUtil.getColor()) {
}