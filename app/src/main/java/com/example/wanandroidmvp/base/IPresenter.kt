package com.example.wanandroidmvp.base

interface IPresenter<in V : IView> {
    fun attachView(mView: V)

    fun detachView()
}