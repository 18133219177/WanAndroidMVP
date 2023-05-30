package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.CollectContract
import com.example.wanandroidmvp.mvp.model.CollectModel

class CollectPresenter : BasePresenter<CollectContract.Model, CollectContract.View>(),
    CollectContract.Presenter {

    override fun createModel(): CollectContract.Model? = CollectModel()

    override fun getCollectList(page: Int) {
        mModel?.getCollectList(page)?.ss(mModel, mView, page == 0) {
            mView?.setCollectList(it.data)
        }
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        mModel?.removeCollectArticle(id, originId)?.ss(mModel, mView) {
            mView?.showRemoveCollectSuccess(true)
        }
    }
}