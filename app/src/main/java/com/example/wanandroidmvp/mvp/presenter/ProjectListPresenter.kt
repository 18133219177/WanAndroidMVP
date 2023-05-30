package com.example.wanandroidmvp.mvp.presenter

import com.example.wanandroidmvp.ext.ss
import com.example.wanandroidmvp.mvp.contract.ProjectListContract
import com.example.wanandroidmvp.mvp.model.ProjectListModel

class ProjectListPresenter : CommonPresenter<ProjectListContract.Model, ProjectListContract.View>(),
    ProjectListContract.Presenter {

    override fun createModel(): ProjectListContract.Model? = ProjectListModel()
    override fun requestProjectList(page: Int, cid: Int) {
        mModel?.requestProjectList(page, cid)?.ss(mModel, mView, page == 1) {
            mView?.setProjectList(it.data)
        }

    }
}