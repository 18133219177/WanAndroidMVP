package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.SquareContract
import com.example.wanandroidmvp.mvp.model.SquareModel

class SquarePresenter : CommonPresenter<SquareContract.Model, SquareContract.View>(),
    SquareContract.Presenter {
    override fun getSquareList(page: Int) {
        mModel?.getSquareList(page)?.ss(mModel, mView, page == 0) {
            mView?.showSquareList(it.data)
        }
    }

    override fun createModel(): SquareContract.Model? = SquareModel()
}