package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.ProjectListContract
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class ProjectListModel:CommonModel(),ProjectListContract.Model {
    override fun requestProjectList(
        page: Int,
        cid: Int
    ): Observable<HttpResult<ArticleResponseBody>> {
    return RetrofitHelper.service.getProjectList(page, cid)
    }
}