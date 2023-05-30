package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.UserScoreBean
import io.reactivex.Observable

interface ScoreContract {
    interface View : IView {
        fun shouUserScoreList(body: BaseListResponseBody<UserScoreBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getUserScoreList(page: Int)
    }

    interface Model : IModel {
        fun getUserScoreList(page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>>
    }
}