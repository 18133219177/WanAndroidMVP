package com.example.wanandroidmvp.ui.fragment

import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.WeChatPagerAdapter
import com.example.wanandroidmvp.base.BaseFragment
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.mvp.contract.WeChatContract
import com.example.wanandroidmvp.mvp.model.bean.WXChapterBean
import com.example.wanandroidmvp.mvp.presenter.WeChatPresenter
import com.example.wanandroidmvp.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.multiple_status_view
import kotlinx.android.synthetic.main.fragment_system.*
import kotlinx.android.synthetic.main.fragment_wechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class WechatFragment : BaseMvpFragment<WeChatContract.View, WeChatContract.Presenter>(),
    WeChatContract.View {
    companion object {
        fun getInstance(): WechatFragment = WechatFragment()
    }

    private val datas = mutableListOf<WXChapterBean>()

    private val viewPagerAdapter: WeChatPagerAdapter by lazy {
        WeChatPagerAdapter(datas, childFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_wechat

    override fun createPresenter(): WeChatContract.Presenter = WeChatPresenter()

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view_w
        viewPager_w.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout_w))
        }
        tabLayout_w.run {
            setupWithViewPager(viewPager_w)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager_w))
            addOnTabSelectedListener(onTabSelectedListener)
        }
        refreshColor(ColorEvent(true))
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.getWXChapters()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout_w.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun doReConnected() {
        if (datas.size == 0) {
            super.doReConnected()
        }
    }


    override fun showWXChapters(chapters: MutableList<WXChapterBean>) {
        chapters.let {
            datas.addAll(it)
            doAsync {
                Thread.sleep(10)
                uiThread {
                    viewPager_w.run {
                        adapter = viewPagerAdapter
                        offscreenPageLimit = datas.size
                    }
                }
            }
        }
        if (chapters.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                viewPager_w.setCurrentItem(it.position, false)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }
    }


    override fun scrollToTop() {
    }

}