package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.ScoreContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.UserScoreBean
import io.reactivex.Observable

class ScoreModel:BaseModel(),ScoreContract.Model {
    override fun getUserScoreList(page: Int): Observable<HttpResult<BaseListResponseBody<UserScoreBean>>> {
        return RetrofitHelper.service.getUserScoreList(page)
    }
}