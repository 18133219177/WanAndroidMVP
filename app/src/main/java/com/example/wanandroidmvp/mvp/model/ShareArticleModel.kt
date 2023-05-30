package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.ShareArticleContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class ShareArticleModel : BaseModel(), ShareArticleContract.Model {
    override fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.shareArticle(map)
    }
}