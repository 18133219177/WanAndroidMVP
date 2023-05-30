package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CoinInfoBean
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface RankContract {
    interface View : IView {
        fun showRankList(body: BaseListResponseBody<CoinInfoBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getRankList(page: Int)
    }

    interface Model : IModel {
        fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>>
    }
}