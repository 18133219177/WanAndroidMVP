package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.LoginContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.LoginData
import io.reactivex.Observable

class LoginModel:BaseModel(),LoginContract.Model {
    override fun loginWanAndroid(
        username: String,
        password: String
    ): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.loginWanAndroid(username, password)
    }
}