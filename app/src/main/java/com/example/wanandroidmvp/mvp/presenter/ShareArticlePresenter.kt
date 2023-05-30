package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.ShareArticleContract
import com.example.wanandroidmvp.mvp.model.ShareArticleModel

class ShareArticlePresenter :
    BasePresenter<ShareArticleContract.Model, ShareArticleContract.View>(),
    ShareArticleContract.Presenter {

    override fun createModel(): ShareArticleContract.Model? = ShareArticleModel()

    override fun shareArticle() {
        val title = mView?.getArticleTitle().toString()
        val link = mView?.getArticleLink().toString()

        if (title.isEmpty()) {
            mView?.showMsg("文章标题不能为空")
            return
        }
        if (link.isEmpty()) {
            mView?.showMsg("文章链接不能为空")
            return
        }
        val map = mutableMapOf<String, Any>()
        map["title"] = title
        map["link"] = link
        mModel?.shareArticle(map)?.ss(mModel, mView, false) {
            mView?.showShareArticle(true)
        }
    }
}