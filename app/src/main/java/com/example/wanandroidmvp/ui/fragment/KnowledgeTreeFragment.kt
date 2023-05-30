package com.example.wanandroidmvp.ui.fragment

import android.content.Intent
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.KnowledgeAdapter
import com.example.wanandroidmvp.adapter.KnowledgeTreeAdapter
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.mvp.contract.KnowledgeTreeContract
import com.example.wanandroidmvp.mvp.model.bean.KnowledgeTreeBody
import com.example.wanandroidmvp.mvp.presenter.KnowledgeTreePresenter
import com.example.wanandroidmvp.ui.activity.KnowledgeActivity
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class KnowledgeTreeFragment :
    BaseMvpListFragment<KnowledgeTreeContract.View, KnowledgeTreeContract.Presenter>(),
    KnowledgeTreeContract.View {

    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    private val mAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter()
    }

    override fun createPresenter(): KnowledgeTreeContract.Presenter = KnowledgeTreePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)

        recyclerView.adapter = mAdapter
        mAdapter.run {
            setOnItemClickListener { adapter, _, position ->
                val item = adapter.data[position] as KnowledgeTreeBody
                itemClick(item)
            }
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeTree()
    }

    override fun onRefreshList() {
        mPresenter?.requestKnowledgeTree()
    }

    override fun onLoadMoreList() {
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }


    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        mAdapter.setList(lists)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    private fun itemClick(item: KnowledgeTreeBody) {
        Intent(activity, KnowledgeActivity::class.java).run {
            putExtra(Constant.CONTENT_TITLE_KEY, item.name)
            putExtra(Constant.CONTENT_DATA_KEY, item)
            startActivity(this)
        }
    }

    override fun scrollToTop() {
    }
}