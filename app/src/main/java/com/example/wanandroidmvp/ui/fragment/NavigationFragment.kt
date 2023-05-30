package com.example.wanandroidmvp.ui.fragment

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.NavigationAdapter
import com.example.wanandroidmvp.adapter.NavigationTabAdapter
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.mvp.contract.NavigationContract
import com.example.wanandroidmvp.mvp.model.bean.NavigationBean
import com.example.wanandroidmvp.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.multiple_status_view
import kotlinx.android.synthetic.main.fragment_refresh_layout.recyclerView
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

class NavigationFragment : BaseMvpFragment<NavigationContract.View, NavigationContract.Presenter>(),
    NavigationContract.View {
    companion object {
        fun getInstance(): NavigationFragment = NavigationFragment()
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val navigationAdapter: NavigationAdapter by lazy {
        NavigationAdapter()
    }

    private var bScroll: Boolean = false
    private var currentIndex: Int = 0
    private var bClickTab: Boolean = false


    override fun createPresenter(): NavigationContract.Presenter = NavigationPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view_n
        recyclerView_n.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }
        leftRightLink()
    }

    private fun leftRightLink() {
        recyclerView_n.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (bScroll && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecyclerView()
                }
                rightLinkLeft(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (bScroll) {
                    scrollRecyclerView()
                }
            }
        })

        navigation_tab_layout.addOnTabSelectedListener(object :
            VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView?, position: Int) {
                bClickTab = true
                selectTab(position)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {
            }

        })
    }


    private fun scrollRecyclerView() {
        bScroll = false
        val indexDistance: Int = currentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < recyclerView_n!!.childCount) {
            val top: Int = recyclerView_n.getChildAt(indexDistance).top
            recyclerView_n.smoothScrollBy(0, top)
        }
    }

    private fun rightLinkLeft(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (bClickTab) {
                bClickTab = false
                return
            }
            val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
            if (firstPosition != currentIndex) {
                currentIndex = firstPosition
                setChecked(currentIndex)
            }
        }
    }

    private fun setChecked(position: Int) {
        if (bClickTab) {
            bClickTab = false
        } else {
            navigation_tab_layout.setTabSelected(currentIndex)
        }
        currentIndex = position
    }

    private fun selectTab(position: Int) {
        currentIndex = position
        recyclerView_n.stopScroll()
        smoothScrollToPosition(position)

    }

    private fun smoothScrollToPosition(position: Int) {
        val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPosition -> {
                recyclerView_n.smoothScrollToPosition(position)
            }
            position <= lastPosition -> {
                val top: Int = recyclerView_n.getChildAt(position - firstPosition).top
                recyclerView_n.smoothScrollBy(0, top)
            }
            else -> {
                recyclerView_n.smoothScrollToPosition(position)
                bScroll = true
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
        mPresenter?.requestNavigationList()
    }


    override fun setNavigationData(list: List<NavigationBean>) {
        list.let {
            navigation_tab_layout.run {
                setTabAdapter(NavigationTabAdapter(requireActivity().applicationContext, list))
            }
            navigationAdapter.run {
                setList(it)
            }
        }
        if (navigationAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    fun scrollToTop() {
        navigation_tab_layout.setTabSelected(0)
    }
}