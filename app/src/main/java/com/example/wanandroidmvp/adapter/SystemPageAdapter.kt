package com.example.wanandroidmvp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SystemPageAdapter(
    fm: FragmentManager,
    private val titleList: MutableList<String>,
    private val fragmentList: MutableList<Fragment>
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = titleList[position]
}