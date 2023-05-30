package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface KnowledgeContract {
    interface View : CommonContract.View {
        fun scrollToTop()

        fun setKnowledgeList(article: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {
        fun requestKnowledgeList(page: Int, cid: Int)
    }

    interface Model : CommonContract.Model {
        fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>>
    }
}