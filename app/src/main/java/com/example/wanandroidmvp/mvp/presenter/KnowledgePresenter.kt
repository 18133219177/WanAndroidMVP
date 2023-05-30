package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.KnowledgeContract
import com.example.wanandroidmvp.mvp.model.KnowledgeModel

class KnowledgePresenter : CommonPresenter<KnowledgeContract.Model, KnowledgeContract.View>(),
    KnowledgeContract.Presenter {
    override fun createModel(): KnowledgeContract.Model? = KnowledgeModel()
    override fun requestKnowledgeList(page: Int, cid: Int) {
        mModel?.requestKnowledgeList(page, cid)?.ss(mModel, mView) {
            mView?.setKnowledgeList(it.data)
        }
    }
}