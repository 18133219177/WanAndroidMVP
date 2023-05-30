package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.LoginData
import io.reactivex.Observable

interface LoginContract {
    interface View : IView {
        fun loginSuccess(data: LoginData)

        fun loginFail()
    }

    interface Presenter : IPresenter<View> {
        fun loginWanAndroid(username: String, password: String)
    }

    interface Model : IModel {
        fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>>
    }
}