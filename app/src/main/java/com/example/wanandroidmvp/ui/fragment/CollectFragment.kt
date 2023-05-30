package com.example.wanandroidmvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.CollectAdapter
import com.example.wanandroidmvp.base.BaseMvpListFragment
import com.example.wanandroidmvp.event.RefreshHomeEvent
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.CollectContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CollectionArticle
import com.example.wanandroidmvp.mvp.presenter.CollectPresenter
import com.example.wanandroidmvp.ui.activity.ContentActivity
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.EventBus

class CollectFragment : BaseMvpListFragment<CollectContract.View, CollectContract.Presenter>(),
    CollectContract.View {

    companion object {
        fun getInstance(bundle: Bundle): CollectFragment {
            val fragment = CollectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mAdapter: CollectAdapter by lazy {
        CollectAdapter()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
    }

    override fun createPresenter(): CollectContract.Presenter = CollectPresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_collect

    override fun useEventBus(): Boolean = true

    override fun initView(view: View) {
        super.initView(view)
        recyclerView.adapter = mAdapter

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as CollectionArticle
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as CollectionArticle
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener { onRequestLoadMoreListener }
        }
        floating_action_btn_c.setOnClickListener {
            scrollToTop()
        }
    }

    override fun onRefreshList() {
        mPresenter?.getCollectList(0)
    }

    override fun onLoadMoreList() {
        mPresenter?.getCollectList(pageNum)
    }


    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getCollectList(0)
    }

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }

    override fun setCollectList(articles: BaseListResponseBody<CollectionArticle>) {
        mAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (mAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showRemoveCollectSuccess(success: Boolean) {
        if (success){
            showToast(resources.getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    private fun itemClick(item: CollectionArticle) {
        ContentActivity.start(activity, item.id, item.title, item.link)
    }

    private fun itemChildClick(item: CollectionArticle, view: View, position: Int) {
        when (view.id) {
            R.id.iv_like -> {
                mAdapter.removeAt(position)
                mPresenter?.removeCollectArticle(item.id, item.originId)
            }
        }
    }
}