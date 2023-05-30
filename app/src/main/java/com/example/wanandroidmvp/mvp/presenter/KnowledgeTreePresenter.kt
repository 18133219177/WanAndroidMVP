package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.KnowledgeTreeContract
import com.example.wanandroidmvp.mvp.model.KnowledgeTreeModel

class KnowledgeTreePresenter :
    BasePresenter<KnowledgeTreeContract.Model, KnowledgeTreeContract.View>(),
    KnowledgeTreeContract.Presenter {
    override fun createModel(): KnowledgeTreeContract.Model? = KnowledgeTreeModel()
    override fun requestKnowledgeTree() {
        mModel?.requestKnowledgeTree()?.ss(mModel, mView) {
            mView?.setKnowledgeTree(it.data)
        }
    }
}