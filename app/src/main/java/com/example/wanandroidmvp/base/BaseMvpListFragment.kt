package com.example.wanandroidmvp.base

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.wanandroidmvp.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

abstract class BaseMvpListFragment<in V : IView, P : IPresenter<V>> : BaseMvpFragment<V, P>() {

    protected var pageSize = 20

    protected var pageNum = 0

    protected val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    protected val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        pageNum = 0
        onRefreshList()
    }

    protected val onRequestLoadMoreListener = OnLoadMoreListener {
        pageNum++
        swipeRefreshLayout.isRefreshing = false
        onLoadMoreList()
    }

    abstract fun onRefreshList()

    abstract fun onLoadMoreList()

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }
    }

    override fun showLoading() {
        //super.showLoading()
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }
}