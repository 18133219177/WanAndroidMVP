package com.example.wanandroidmvp.mvp.contract

import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IPresenter
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

interface AddTodoContract {

    interface View : IView {
        fun showAddTodo(success: Boolean)

        fun showUpdateTodo(success: Boolean)

        fun getType(): Int
        fun getCurrentDate(): String
        fun getTitle(): String
        fun getContent(): String
        fun getStatus(): Int
        fun getItemId(): Int
        fun getPriority(): String
    }

    interface Presenter : IPresenter<View> {
        fun addTodo()
        fun updateTodo(id: Int)
    }

    interface Model : IModel {
        fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>>

        fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>>
    }

}