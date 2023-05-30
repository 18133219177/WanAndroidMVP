package com.example.wanandroidmvp.ui.fragment

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.HomeAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseFragment
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showSnackMsg
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.SquareContract
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.presenter.SquarePresenter
import com.example.wanandroidmvp.ui.activity.CommonActivity
import com.example.wanandroidmvp.ui.activity.ContentActivity
import com.example.wanandroidmvp.ui.activity.LoginActivity
import com.example.wanandroidmvp.utils.NetWorkUtil
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class SquareFragment : BaseMvpListFragment<SquareContract.View, SquareContract.Presenter>(),
    SquareContract.View {
    companion object {
        fun getInstance(): SquareFragment = SquareFragment()
    }

    private val mAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    override fun createPresenter(): SquareContract.Presenter = SquarePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_square

    override fun useEventBus(): Boolean = true

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun onRefreshList() {
        mPresenter?.getSquareList(0)
    }

    override fun onLoadMoreList() {
        mPresenter?.getSquareList(pageNum)
    }

    override fun initView(view: View) {
        setHasOptionsMenu(true)
        super.initView(view)
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
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getSquareList(0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_square, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                Intent(activity, CommonActivity::class.java).run {
                    putExtra(Constant.TYPE_KEY, Constant.Type.SHARE_ARTICLE_TYPE_KEY)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun showSquareList(body: ArticleResponseBody) {
        mAdapter.setNewOrAddData(pageNum == 0, body.datas)
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
    }

}