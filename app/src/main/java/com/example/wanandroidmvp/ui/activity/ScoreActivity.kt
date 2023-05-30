package com.example.wanandroidmvp.ui.activity

import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.ScoreAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseMvpSwipeBackActivity
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.mvp.contract.ScoreContract
import com.example.wanandroidmvp.mvp.model.bean.BaseListResponseBody
import com.example.wanandroidmvp.mvp.model.bean.UserScoreBean
import com.example.wanandroidmvp.mvp.presenter.ScorePresenter
import com.example.wanandroidmvp.widget.SpaceItemDecoration
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : BaseMvpSwipeBackActivity<ScoreContract.View, ScoreContract.Presenter>(),
    ScoreContract.View {

    private var pageSize = 20
    private var pageNum = 1

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val scoreAdapter: ScoreAdapter by lazy {
        ScoreAdapter()
    }

    private var contentHeight = 0F

    override fun createPresenter(): ScoreContract.Presenter = ScorePresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_score

    override fun initData() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun initView() {
        super.initView()
        mLayoutStatusView = multiple_status_view
        toolbar.run {
            title = getString(R.string.score_detail)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        App.userInfo?.let {
            tv_score.text = it.coinCount.toString()
        }
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@ScoreActivity)
            adapter = scoreAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        scoreAdapter.run {
            loadMoreModule.setOnLoadMoreListener(onRequestLoadMoreListener)
        }
        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            contentHeight = rl_content.height.toFloat()
            val alpha = 1 - (-verticalOffset) / (contentHeight)
            rl_content.alpha = alpha
        })
    }

    override fun initColor() {
        super.initColor()
        rl_content.setBackgroundColor(mThemeColor)
    }

    override fun start() {
        mLayoutStatusView?.showLoading()
        mPresenter?.getUserScoreList(1)
    }

    override fun shouUserScoreList(body: BaseListResponseBody<UserScoreBean>) {
        scoreAdapter.setNewOrAddData(pageNum == 1, body.datas)
        if (scoreAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        pageNum = 1
        mPresenter?.getUserScoreList(pageNum)
    }

    private val onRequestLoadMoreListener = OnLoadMoreListener {
        pageNum++
        swipeRefreshLayout.isRefreshing = false
        mPresenter?.getUserScoreList(pageNum)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}