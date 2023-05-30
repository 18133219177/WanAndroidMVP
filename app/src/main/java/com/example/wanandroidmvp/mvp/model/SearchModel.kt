package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.SearchContract
import com.example.wanandroidmvp.mvp.model.bean.HotSearchBean
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class SearchModel : BaseModel(), SearchContract.Model {
    override fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>> {
        return RetrofitHelper.service.getHotSearchData()
    }
}