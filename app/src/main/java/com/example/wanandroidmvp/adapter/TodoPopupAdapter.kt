package com.example.wanandroidmvp.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.mvp.model.bean.TodoTypeBean

class TodoPopupAdapter :
    BaseQuickAdapter<TodoTypeBean, BaseViewHolder>(R.layout.item_todo_popup_list) {
    override fun convert(holder: BaseViewHolder, item: TodoTypeBean) {
        val tv_popup = holder.getView<TextView>(R.id.tv_popup)
        tv_popup.text = item.name
        if (item.isSelected) {
            tv_popup.setTextColor(context.resources.getColor(R.color.colorAccent))
        } else {
            tv_popup.setTextColor(context.resources.getColor(R.color.common_color))
        }
    }
}