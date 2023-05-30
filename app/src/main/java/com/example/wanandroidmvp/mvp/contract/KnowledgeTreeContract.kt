package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

interface KnowledgeTreeContract {
    interface View : IView {
        fun scrollToTop()

        fun setKnowledgeTree(lists: List<KnowledgeTreeBody>)
    }

    interface Presenter : IPresenter<View> {
        fun requestKnowledgeTree()
    }

    interface Model : IModel {
        fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>
    }
}