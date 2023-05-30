package com.example.wanandroidmvp.mvp.model

import io.reactivex.Observable
import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.RegisterContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.LoginData

class RegisterModel : BaseModel(), RegisterContract.Model {
    override fun registerWanAndroid(
        username: String,
        password: String,
        repassword: String
    ): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.registerWanAndroid(username, password, repassword)
    }
}