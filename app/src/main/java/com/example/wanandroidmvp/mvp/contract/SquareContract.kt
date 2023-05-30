package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface SquareContract {
    interface View : CommonContract.View {
        fun scrollToTop()
        fun showSquareList(body: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {
        fun getSquareList(page: Int)
    }

    interface Model : CommonContract.Model {
        fun getSquareList(page: Int): Observable<HttpResult<ArticleResponseBody>>
    }
}