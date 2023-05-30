package com.example.wanandroidmvp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.mvp.model.bean.UserScoreBean

class ScoreAdapter : BaseQuickAdapter<UserScoreBean, BaseViewHolder>(R.layout.item_score_list),
    LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: UserScoreBean) {
        holder.setText(R.id.tv_reason, item.reason)
            .setText(R.id.tv_desc, item.desc)
            .setText(R.id.tv_score, "+${item.coinCount}")
    }
}