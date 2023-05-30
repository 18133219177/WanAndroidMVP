package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.KnowledgeTreeContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

class KnowledgeTreeModel : BaseModel(), KnowledgeTreeContract.Model {
    override fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>> {
        return RetrofitHelper.service.getKnowledgeTree()
    }
}