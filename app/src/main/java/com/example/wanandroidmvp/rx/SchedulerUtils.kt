package com.example.wanandroidmvp.rx

import com.example.wanandroidmvp.rx.scheduler.IoMainScheduler

object SchedulerUtils {
    fun <T> inToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}