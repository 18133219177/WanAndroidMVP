package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.ext.sss
import com.example.wanandroidmvp.mvp.contract.MainContract
import com.example.wanandroidmvp.mvp.model.MainModel

class MainPresenter : BasePresenter<MainContract.Model, MainContract.View>(),
    MainContract.Presenter {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun logout() {
        mModel?.logout()?.ss(mModel, mView) {
            mView?.showLogoutSuccess(success = true)
        }
    }

    override fun getUserInfo() {
        mModel?.getUserInfo()?.sss(mView, false, {
            mView?.showUserInfo(it.data)
        }, {})
    }
}