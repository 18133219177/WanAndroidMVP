package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.HomeContract
import com.example.wanandroidmvp.mvp.model.HomeModel
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.utils.SettingUtil
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class HomePresenter : CommonPresenter<HomeContract.Model, HomeContract.View>(),
    HomeContract.Presenter {

    override fun createModel(): HomeContract.Model? = HomeModel()

    override fun requestBanner() {
        mModel?.requestBanner()?.ss(mModel, mView, false) {
            mView?.setBanner(it.data)
        }
    }


    override fun requestArticles(num: Int) {
        mModel?.requestArticles(num)?.ss(mModel, mView, num == 0) {
            mView?.setArticles(it.data)
        }
    }


    override fun requestHomeData() {
        requestBanner()
        val observable = if (SettingUtil.getIsShowTopArticle()) {
            mModel?.requestArticles(0)
        } else {
            Observable.zip(
                mModel?.requestTopArticles(), mModel?.requestArticles(0),
                BiFunction<HttpResult<MutableList<Article>>, HttpResult<ArticleResponseBody>,
                        HttpResult<ArticleResponseBody>> { t1, t2 ->
                    t1.data.forEach {
                        it.top = "1"
                    }
                    t2.data.datas.addAll(0, t1.data)
                    t2
                })
        }
        observable?.ss(mModel, mView, false) {
            mView?.setArticles(it.data)
        }
    }
}