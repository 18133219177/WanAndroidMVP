package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

interface ProjectContract {
    interface View : IView {
        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)
    }

    interface Presenter : IPresenter<View> {
        fun requestProjectTree()
    }

    interface Model : IModel {
        fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>
    }
}