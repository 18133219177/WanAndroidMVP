package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.NavigationContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.NavigationBean
import io.reactivex.Observable

class NavigationModel : BaseModel(), NavigationContract.Model {
    override fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> {
        return RetrofitHelper.service.getNavigationList()
    }
}