package com.example.wanandroidmvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.HomeAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showSnackMsg
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.SearchListContract
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.presenter.SearchListPresenter
import com.example.wanandroidmvp.ui.activity.ContentActivity
import com.example.wanandroidmvp.ui.activity.LoginActivity
import com.example.wanandroidmvp.utils.NetWorkUtil
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_search_list.*

class SearchListFragment :
    BaseMvpListFragment<SearchListContract.View, SearchListContract.Presenter>(),
    SearchListContract.View {

    companion object {
        fun getInstance(bundle: Bundle): SearchListFragment {
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mKey = ""

    private val mAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun createPresenter(): SearchListContract.Presenter = SearchListPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_search_list

    override fun initView(view: View) {
        super.initView(view)
        mKey = arguments?.getString(Constant.SEARCH_KEY, "") ?: ""

        recyclerView.adapter = mAdapter

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener(onRequestLoadMoreListener)
        }

        floating_action_btn.setOnClickListener {
            scrollToTop()
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.queryBySearchKey(0, mKey)
    }

    override fun onRefreshList() {
        mPresenter?.queryBySearchKey(0, mKey)
    }

    override fun onLoadMoreList() {
        mPresenter?.queryBySearchKey(pageNum, mKey)
    }


    override fun showArticles(articles: ArticleResponseBody) {
        mAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    private fun itemClick(item: Article) {
        ContentActivity.start(activity, item.id, item.title, item.link)
    }

    private fun itemChildClick(item: Article, view: View, position: Int) {
        when (view.id) {
            R.id.iv_like -> {
                if (isLogin) {
                    if (!NetWorkUtil.isNetWorkAvailable(App.context)) {
                        showSnackMsg(resources.getString(R.string.no_network))
                        return
                    }
                    val collect = item.collect
                    item.collect = !collect
                    mAdapter.setData(position, item)
                    if (collect) {
                        mPresenter?.cancelCollectArticle(item.id)
                    } else {
                        mPresenter?.addCollectArticle(item.id)
                    }
                } else {
                    Intent(activity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showToast(resources.getString(R.string.login_tint))
                }
            }
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }
}