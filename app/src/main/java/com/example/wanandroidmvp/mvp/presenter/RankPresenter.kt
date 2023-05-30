package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.RankContract
import com.example.wanandroidmvp.mvp.model.RankModel

class RankPresenter : BasePresenter<RankContract.Model, RankContract.View>(),
    RankContract.Presenter {

    override fun createModel(): RankContract.Model? = RankModel()

    override fun getRankList(page: Int) {
        mModel?.getRankList(page)?.ss(mModel, mView) {
            mView?.showRankList(it.data)
        }
    }
}