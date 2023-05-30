package com.example.wanandroidmvp.ui.fragment

import android.view.MotionEvent
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.ProjectPagerAdapter
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.mvp.contract.ProjectContract
import com.example.wanandroidmvp.mvp.model.bean.ProjectTreeBean
import com.example.wanandroidmvp.mvp.presenter.ProjectPresenter
import com.example.wanandroidmvp.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.fragment_project.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProjectFragment : BaseMvpFragment<ProjectContract.View, ProjectContract.Presenter>(), ProjectContract.View {

    companion object {
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    override fun createPresenter(): ProjectContract.Presenter = ProjectPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_project

    /**
     * ProjectTreeBean
     */
    private var projectTree = mutableListOf<ProjectTreeBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTree, childFragmentManager)
    }

    override fun useEventBus(): Boolean = true

    override fun initView(view: View) {
        super.initView(view)
        //mLayoutStatusView = multiple_status_view

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            //TabLayoutHelper.setUpIndicatorWidth(tabLayout)
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

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.requestProjectTree()
    }

    override fun doReConnected() {
        if (projectTree.size == 0) {
            super.doReConnected()
        }
    }

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            println(it[0].name)
            projectTree.addAll(it)
            doAsync {
                Thread.sleep(10)
                uiThread {
                    viewPager.run {
                        adapter = viewPagerAdapter
                        offscreenPageLimit = projectTree.size
                    }
                }
            }
        }
        if (list.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) {
            return
        }
        val fragment: ProjectListFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

}