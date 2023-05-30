package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.UserInfoBody
import io.reactivex.Observable

interface MainContract {
    interface View : IView {
        fun showLogoutSuccess(success: Boolean)
        fun showUserInfo(bean: UserInfoBody)
    }

    interface Presenter : IPresenter<View> {
        fun logout()
        fun getUserInfo()
    }

    interface Model : IModel {
        fun logout(): Observable<HttpResult<Any>>
        fun getUserInfo(): Observable<HttpResult<UserInfoBody>>
    }

}