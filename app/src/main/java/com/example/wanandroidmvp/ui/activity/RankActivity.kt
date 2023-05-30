package com.example.wanandroidmvp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.RankAdapter
import com.example.wanandroidmvp.base.BaseMvpSwipeBackActivity
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.mvp.contract.RankContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.CoinInfoBean
import com.example.wanandroidmvp.mvp.presenter.RankPresenter
import com.example.wanandroidmvp.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_score.*

class RankActivity : BaseMvpSwipeBackActivity<RankContract.View, RankContract.Presenter>(),
    RankContract.View {

    private var pageSize = 20
    private var pageNum = 1

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val rankAdapter: RankAdapter by lazy {
        RankAdapter()
    }

    override fun createPresenter(): RankContract.Presenter = RankPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_rank

    override fun showLoading() {
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.score_list)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        swipeRefreshLayout.setOnRefreshListener {
            pageNum = 1
            mPresenter?.getRankList(pageNum)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@RankActivity)
            adapter = rankAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        rankAdapter.run {
            loadMoreModule.setOnLoadMoreListener {
                pageNum++
                swipeRefreshLayout.isRefreshing = false
                mPresenter?.getRankList(pageNum)
            }
        }
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getRankList(1)
    }

    override fun showRankList(body: BaseListResponseBody<CoinInfoBean>) {
        rankAdapter.setNewOrAddData(pageNum == 1, body.datas)
        if (rankAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }
}