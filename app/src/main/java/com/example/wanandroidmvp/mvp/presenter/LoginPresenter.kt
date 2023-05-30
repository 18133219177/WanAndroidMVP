package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.LoginContract
import com.example.wanandroidmvp.mvp.model.LoginModel

class LoginPresenter : BasePresenter<LoginContract.Model, LoginContract.View>(),
    LoginContract.Presenter {
    override fun loginWanAndroid(username: String, password: String) {
        mModel?.loginWanAndroid(username, password)?.ss(mModel, mView) {
            mView?.loginSuccess(it.data)
        }
    }

    override fun createModel(): LoginContract.Model? = LoginModel()
}