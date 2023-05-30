package com.example.wanandroidmvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.KnowledgeAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showSnackMsg
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.KnowledgeContract
import com.example.wanandroidmvp.mvp.model.KnowledgeModel
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.presenter.KnowledgePresenter
import com.example.wanandroidmvp.ui.activity.ContentActivity
import com.example.wanandroidmvp.ui.activity.LoginActivity
import com.example.wanandroidmvp.utils.NetWorkUtil
import com.example.wanandroidmvp.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class KnowledgeFragment :
    BaseMvpListFragment<KnowledgeContract.View, KnowledgeContract.Presenter>(),
    KnowledgeContract.View {

    companion object {
        fun getInstance(cid: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private var cid: Int = 0

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }


    private val mAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun createPresenter(): KnowledgeContract.Presenter = KnowledgePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemChildClick(item, view, position)
            }
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeList(0, cid)
    }

    override fun onRefreshList() {
        mPresenter?.requestKnowledgeList(0, cid)
    }

    override fun onLoadMoreList() {
        mPresenter?.requestKnowledgeList(pageNum, cid)
    }


    override fun setKnowledgeList(article: ArticleResponseBody) {
        mAdapter.setNewOrAddData(pageNum == 0, article.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
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
        TODO("Not yet implemented")
    }

}