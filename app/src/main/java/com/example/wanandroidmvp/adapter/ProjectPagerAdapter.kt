package com.example.wanandroidmvp.adapter

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.wanandroidmvp.mvp.model.bean.ProjectTreeBean
import com.example.wanandroidmvp.ui.fragment.ProjectListFragment

class ProjectPagerAdapter(private val list: MutableList<ProjectTreeBean>, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(ProjectListFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? =
        Html.fromHtml(list[position].id.toString())

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE

}