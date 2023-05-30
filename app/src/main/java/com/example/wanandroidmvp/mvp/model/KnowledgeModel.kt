package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.KnowledgeContract
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class KnowledgeModel : CommonModel(), KnowledgeContract.Model {
    override fun requestKnowledgeList(
        page: Int,
        cid: Int
    ): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getKnowledgeList(page, cid)
    }
}