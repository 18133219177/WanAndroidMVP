package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.mvp.contract.SystemContract

class SystemPresenter : BasePresenter<SystemContract.Model, SystemContract.View>(),
    SystemContract.Presenter {
}