package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.HomeContract
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.Banner
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class HomeModel : CommonModel(), HomeContract.Model {
    override fun requestBanner(): Observable<HttpResult<List<Banner>>> {
        return RetrofitHelper.service.getBanners()
    }

    override fun requestTopArticles(): Observable<HttpResult<MutableList<Article>>> {
        return RetrofitHelper.service.getTopArticles()
    }

    override fun requestArticles(num: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getArticles(num)
    }
}