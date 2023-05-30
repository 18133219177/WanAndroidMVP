package com.example.wanandroidmvp.adapter

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.wanandroidmvp.mvp.model.bean.Knowledge
import com.example.wanandroidmvp.ui.fragment.KnowledgeFragment

class KnowledgePagerAdapter(val list: List<Knowledge>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = Html.fromHtml(list[position].name)

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE
}