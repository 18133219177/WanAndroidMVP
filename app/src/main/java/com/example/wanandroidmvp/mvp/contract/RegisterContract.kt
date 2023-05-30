package com.example.wanandroidmvp.mvp.contract

import io.reactivex.Observable
import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.LoginData

interface RegisterContract {
    interface View : IView {
        fun registerSuccess(data: LoginData)

        fun registerFail()
    }

    interface Presenter : IPresenter<View> {
        fun registerWanAndroid(username: String, password: String, repassword: String)
    }

    interface Model : IModel {
        fun registerWanAndroid(
            username: String,
            password: String,
            repassword: String
        ): Observable<HttpResult<LoginData>>
    }
}