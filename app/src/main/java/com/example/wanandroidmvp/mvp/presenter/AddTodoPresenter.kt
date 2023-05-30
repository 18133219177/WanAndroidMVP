package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.AddTodoContract
import com.example.wanandroidmvp.mvp.model.AddTodoModel

class AddTodoPresenter : BasePresenter<AddTodoContract.Model, AddTodoContract.View>(),
    AddTodoContract.Presenter {

    override fun createModel(): AddTodoContract.Model? = AddTodoModel()

    override fun addTodo() {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val priority = mView?.getPriority().toString()

        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["priority"] = priority

        mModel?.addTodo(map)?.ss(mModel, mView) {
            mView?.showAddTodo(true)
        }
    }

    override fun updateTodo(id: Int) {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val priority = mView?.getPriority().toString()

        val map = mutableMapOf<String, Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["priority"] = priority

        mModel?.updateTodo(id, map)?.ss(mModel, mView) {
            mView?.showUpdateTodo(true)
        }
    }
}