package com.example.wanandroidmvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.ProjectAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.mvp.contract.ProjectListContract
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showSnackMsg
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.presenter.ProjectListPresenter
import com.example.wanandroidmvp.ui.activity.ContentActivity
import com.example.wanandroidmvp.ui.activity.LoginActivity
import com.example.wanandroidmvp.utils.NetWorkUtil
import kotlinx.android.synthetic.main.fragment_refresh_layout.*


class ProjectListFragment :
    BaseMvpListFragment<ProjectListContract.View, ProjectListContract.Presenter>(),
    ProjectListContract.View {


    companion object {
        fun getInstance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private var cid: Int = -1

    private val mAdapter: ProjectAdapter by lazy {
        ProjectAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun createPresenter(): ProjectListContract.Presenter = ProjectListPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)

        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: -1
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
            loadMoreModule.setOnLoadMoreListener { onRequestLoadMoreListener }
        }
    }

    override fun onRefreshList() {
        mPresenter?.requestProjectList(1, cid)
    }

    override fun onLoadMoreList() {
        mPresenter?.requestProjectList(pageNum + 1, cid)
    }


    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestProjectList(1, cid)
    }


    override fun setProjectList(articles: ArticleResponseBody) {
        mAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.login_success))
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
            R.id.item_project_list_like_iv -> {
                if (isLogin) {
                    if (!NetWorkUtil.isNetWorkAvailable(App.context)) {
                        showSnackMsg(resources.getString(R.string.no_network))
                        return
                    }
                    val collect = item.collect
                    item.collect = !collect
                    mAdapter.addData(position, item)
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