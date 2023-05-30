package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.MainContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.UserInfoBody
import io.reactivex.Observable

class MainModel : BaseModel(), MainContract.Model {
    override fun logout(): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.logout()
    }

    override fun getUserInfo(): Observable<HttpResult<UserInfoBody>> {
        return RetrofitHelper.service.getUserInfo()
    }
}