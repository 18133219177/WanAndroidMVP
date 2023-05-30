package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.RegisterContract
import com.example.wanandroidmvp.mvp.model.RegisterModel

class RegisterPresenter : BasePresenter<RegisterContract.Model, RegisterContract.View>(),
    RegisterContract.Presenter {
    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        mModel?.registerWanAndroid(username, password, repassword)?.ss(mModel, mView) {
            mView?.apply {
                if (it.errorCode != 0) {
                    registerFail()
                } else {
                    registerSuccess(it.data)
                }
            }
        }
    }

    override fun createModel(): RegisterContract.Model? = RegisterModel()
}