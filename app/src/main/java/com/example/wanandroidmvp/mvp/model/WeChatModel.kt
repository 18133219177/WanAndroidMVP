package com.example.wanandroidmvp.mvp.model

import com.example.wanandroidmvp.base.BaseModel
import com.example.wanandroidmvp.http.RetrofitHelper
import com.example.wanandroidmvp.mvp.contract.WeChatContract
import com.example.wanandroidmvp.mvp.model.bean.HttpResult
import com.example.wanandroidmvp.mvp.model.bean.WXChapterBean
import io.reactivex.Observable

class WeChatModel : BaseModel(), WeChatContract.Model {
    override fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>> {
        return RetrofitHelper.service.getWXChapters()
    }
}