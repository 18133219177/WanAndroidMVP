package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HotSearchBean
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.SearchHistoryBean
import io.reactivex.Observable

interface SearchContract {
    interface View : IView {
        fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>)

        fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>)
    }

    interface Presenter : IPresenter<View> {

        fun queryHistory()

        fun saveSearchKey(key:String)

        fun deletedById(id: Long)

        fun clearAllHistory()

        fun getHotSearchData()
    }

    interface Model : IModel {
        fun getHotSearchData():Observable<HttpResult<MutableList<HotSearchBean>>>
    }
}