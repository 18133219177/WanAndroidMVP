package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.SearchListContract
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class SearchListModel : CommonModel(), SearchListContract.Model {
    override fun queryBySearchKey(
        page: Int,
        key: String
    ): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.queryBySearchKey(page, key)
    }
}