package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView

interface SystemContract {
    interface View : IView {
        fun scrollToTop()
    }

    interface Presenter : IPresenter<View> {

    }

    interface Model : IModel {}
}