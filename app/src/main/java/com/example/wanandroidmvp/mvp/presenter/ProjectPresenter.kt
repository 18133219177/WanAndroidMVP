package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.base.BasePresenter
import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.ProjectContract
import com.example.wanandroidmvp.mvp.model.ProjectModel

class ProjectPresenter : BasePresenter<ProjectContract.Model, ProjectContract.View>(),
    ProjectContract.Presenter {

    override fun createModel(): ProjectContract.Model? = ProjectModel()

    override fun requestProjectTree() {
        mModel?.requestProjectTree()?.ss(mModel, mView) {
            mView?.setProjectTree(it.data)
        }
    }
}