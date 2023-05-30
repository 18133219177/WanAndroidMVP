package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.CollectContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CollectionArticle
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class CollectModel : BaseModel(), CollectContract.Model {
    override fun getCollectList(page: Int): Observable<HttpResult<BaseListResponseBody<CollectionArticle>>> {
        return RetrofitHelper.service.getCollectList(page)
    }

    override fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.removeCollectArticle(id, originId)
    }
}