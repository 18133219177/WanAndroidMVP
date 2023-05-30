package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.RankContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CoinInfoBean
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import io.reactivex.Observable

class RankModel:BaseModel(),RankContract.Model {
    override fun getRankList(page: Int): Observable<HttpResult<BaseListResponseBody<CoinInfoBean>>> {
        return RetrofitHelper.service.getRankList(page)
    }
}