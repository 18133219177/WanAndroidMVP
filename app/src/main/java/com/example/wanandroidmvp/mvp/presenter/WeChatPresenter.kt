package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.WeChatContract
import com.example.wanandroidmvp.mvp.model.WeChatModel

class WeChatPresenter : BasePresenter<WeChatContract.Model, WeChatContract.View>(),
    WeChatContract.Presenter {
    override fun createModel(): WeChatContract.Model? = WeChatModel()

    override fun getWXChapters() {
        mModel?.getWXChapters()?.ss(mModel, mView) {
            mView?.showWXChapters(it.data)
        }

    }
}