package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.ProjectContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

class ProjectModel:BaseModel(),ProjectContract.Model {
    override fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>> {
        return RetrofitHelper.service.getProjectTree()
    }
}