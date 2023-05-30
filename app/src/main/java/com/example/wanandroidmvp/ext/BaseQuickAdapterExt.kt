package com.example.wanandroidmvp.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setNewOrAddData(
    isRefreshAllData: Boolean,
    expectedDataSize: Int,
    data: MutableList<T>?,
) {
    if (data == null) {
        return
    }
    if (isRefreshAllData) {
        setNewInstance(data)
        loadMoreModule.checkDisableLoadMoreIfNotFullPage()
    } else {
        addData(data)
        val isEnableLoadMore = loadMoreModule.isEnableLoadMore
        if (!isEnableLoadMore) {
            return
        }
    }
    if (data.size < expectedDataSize) {
        loadMoreModule.loadMoreEnd()
    } else {
        loadMoreModule.loadMoreComplete()
    }
}

fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setNewOrAddData(
    isSetNewData: Boolean,
    data: MutableList<T>?
) {
    if (isSetNewData) {
        setNewInstance(data)
        loadMoreModule.checkDisableLoadMoreIfNotFullPage()
    } else {
        if (data != null) {
            addData(data)
        }
        val isEnableLoadMore = loadMoreModule.isEnableLoadMore
        if (!isEnableLoadMore) {
            return
        }
    }
    if (data == null || data.isEmpty()) {
        loadMoreModule.loadMoreEnd(false)
    } else {
        loadMoreModule.loadMoreComplete()
    }
}