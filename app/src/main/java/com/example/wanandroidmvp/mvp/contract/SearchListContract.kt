package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface SearchListContract {
    interface View : CommonContract.View {
        fun scrollToTop()

        fun showArticles(articles: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun queryBySearchKey(page: Int, key: String)
    }

    interface Model : CommonContract.Model {
        fun queryBySearchKey(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>>
    }
}