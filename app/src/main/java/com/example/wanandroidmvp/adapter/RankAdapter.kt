package com.example.wanandroidmvp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.mvp.model.bean.CoinInfoBean

class RankAdapter : BaseQuickAdapter<CoinInfoBean, BaseViewHolder>(R.layout.item_rank_list),
    LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: CoinInfoBean) {
        val index = holder.layoutPosition
        holder.setText(R.id.tv_username, item.username)
            .setText(R.id.tv_score, item.coinCount.toString())
            .setText(R.id.tv_ranking, (index + 1).toString())
    }
}