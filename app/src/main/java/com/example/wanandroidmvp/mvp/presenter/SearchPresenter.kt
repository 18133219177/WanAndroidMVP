package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.SearchContract
import com.example.wanandroidmvp.mvp.model.SearchModel
import com.example.wanandroidmvp.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

class SearchPresenter : BasePresenter<SearchContract.Model, SearchContract.View>(),
    SearchContract.Presenter {

    override fun createModel(): SearchContract.Model? = SearchModel()

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                mView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size == 0) {
                historyBean.save()
            } else {
                deletedById(beans[0].id)
                historyBean.save()
            }
        }
    }

    override fun deletedById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }
    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
        }
    }

    override fun getHotSearchData() {
        mModel?.getHotSearchData()?.ss(mModel, mView) {
            mView?.showHotSearchData(it.data)
        }
    }
}