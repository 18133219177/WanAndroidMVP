package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface ShareArticleContract {
    interface View : IView {
        fun getArticleTitle(): String
        fun getArticleLink(): String

        fun showShareArticle(success: Boolean)
    }

    interface Presenter : IPresenter<View> {
        fun shareArticle()
    }

    interface Model : IModel {
        fun shareArticle(map: MutableMap<String, Any>): Observable<HttpResult<Any>>
    }
}