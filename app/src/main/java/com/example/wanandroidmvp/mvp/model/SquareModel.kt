package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.SquareContract
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class SquareModel : CommonModel(), SquareContract.Model {
    override fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getSquareList(page)
    }
}