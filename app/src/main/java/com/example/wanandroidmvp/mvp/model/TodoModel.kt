package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.TodoContract
import com.example.wanandroidmvp.mvp.model.bean.AllTodoResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.TodoResponseBody
import io.reactivex.Observable

class TodoModel : BaseModel(), TodoContract.Model {
    override fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>> {
        return RetrofitHelper.service.getTodoList(type)
    }

    override fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getNoTodoList(page, type)
    }

    override fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getDoneList(page, type)
    }

    override fun deleteTodoById(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteTodoById(id)
    }

    override fun updateTodoById(id: Int, status: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodoById(id, status)
    }
}