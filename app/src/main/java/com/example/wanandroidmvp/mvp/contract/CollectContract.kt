package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CollectionArticle
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface CollectContract {
    interface View : IView {
        fun scrollToTop()

        fun setCollectList(articles: BaseListResponseBody<CollectionArticle>)

        fun showRemoveCollectSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View> {
        fun getCollectList(page: Int)

        fun removeCollectArticle(id: Int, originId: Int)
    }

    interface Model : IModel {
        fun getCollectList(page: Int): Observable<HttpResult<BaseListResponseBody<CollectionArticle>>>

        fun removeCollectArticle(id:Int,originId: Int):Observable<HttpResult<Any>>
    }
}