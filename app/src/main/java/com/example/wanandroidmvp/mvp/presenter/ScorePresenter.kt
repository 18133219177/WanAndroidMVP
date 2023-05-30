package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.ScoreContract
import com.example.wanandroidmvp.mvp.model.ScoreModel

class ScorePresenter : BasePresenter<ScoreContract.Model, ScoreContract.View>(),
    ScoreContract.Presenter {

    override fun createModel(): ScoreContract.Model? = ScoreModel()

    override fun getUserScoreList(page: Int) {
        mModel?.getUserScoreList(page)?.ss(mModel, mView) {
            mView?.shouUserScoreList(it.data)
        }
    }
}