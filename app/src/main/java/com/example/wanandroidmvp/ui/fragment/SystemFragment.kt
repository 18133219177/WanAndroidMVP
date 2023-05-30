package com.example.wanandroidmvp.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.SystemPageAdapter
import com.example.wanandroidmvp.base.BaseFragment
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.mvp.contract.SystemContract
import com.example.wanandroidmvp.mvp.presenter.SystemPresenter
import com.example.wanandroidmvp.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_system.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SystemFragment : BaseMvpFragment<SystemContract.View, SystemContract.Presenter>(),
    SystemContract.View {
    companion object {
        fun getInstance(): SystemFragment = SystemFragment()
    }

    private val titleList = mutableListOf<String>()
    private val fragmentList = mutableListOf<Fragment>()
    private val systemPageAdapter: SystemPageAdapter by lazy {
        SystemPageAdapter(childFragmentManager, titleList, fragmentList)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_system

    override fun createPresenter(): SystemContract.Presenter = SystemPresenter()

    override fun initView(view: View) {
        super.initView(view)

        titleList.add(resources.getString(R.string.knowledge_system))
        titleList.add(resources.getString(R.string.navigation))
        fragmentList.add(KnowledgeTreeFragment.getInstance())
        fragmentList.add(NavigationFragment.getInstance())

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            adapter = systemPageAdapter
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun lazyLoad() {
    }


    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }

    override fun scrollToTop() {
        if (viewPager.currentItem == 0) {
            (systemPageAdapter.getItem(0) as KnowledgeTreeFragment).scrollToTop()
        } else if (viewPager.currentItem == 1) {
            (systemPageAdapter.getItem(1) as NavigationFragment).scrollToTop()
        }
    }
}