package com.example.wanandroidmvp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.NetworkChangeEvent
import com.example.wanandroidmvp.utils.NetWorkUtil
import com.example.wanandroidmvp.utils.Preference
import org.greenrobot.eventbus.EventBus

class NetworkChangeReceiver : BroadcastReceiver() {

    private val hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))

        }
    }
}