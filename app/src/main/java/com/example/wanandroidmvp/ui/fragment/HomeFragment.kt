package com.example.wanandroidmvp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.bgabanner.BGABanner
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.HomeAdapter
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseFragment
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.ext.setNewOrAddData
import com.example.wanandroidmvp.ext.showSnackMsg
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.HomeContract
import com.example.wanandroidmvp.mvp.model.bean.Article
import com.example.wanandroidmvp.mvp.model.bean.ArticleResponseBody
import com.example.wanandroidmvp.mvp.model.bean.Banner
import com.example.wanandroidmvp.mvp.presenter.HomePresenter
import com.example.wanandroidmvp.ui.activity.ContentActivity
import com.example.wanandroidmvp.ui.activity.LoginActivity
import com.example.wanandroidmvp.utils.ImageLoader
import com.example.wanandroidmvp.utils.NetWorkUtil
import com.example.wanandroidmvp.widget.SpaceItemDecoration
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.item_home_banner.*
import kotlinx.android.synthetic.main.item_home_banner.view.*

class HomeFragment : BaseMvpFragment<HomeContract.View, HomeContract.Presenter>(),
    HomeContract.View {
    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }

    override fun createPresenter(): HomeContract.Presenter = HomePresenter()

    private lateinit var bannerDatas: ArrayList<Banner>

    private var bannerView: View? = null

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    private val bannerAdapter: BGABanner.Adapter<ImageView, String> by lazy {
        BGABanner.Adapter<ImageView, String> { _, imageView, feedImgUrl, _ ->
            ImageLoader.load(activity, feedImgUrl, imageView)
        }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private var pageNum = 0

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout


    @SuppressLint("InflateParams")
    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        swipeRefreshLayout.setOnRefreshListener {
            pageNum = 0
            mPresenter?.requestHomeData()
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        bannerView = layoutInflater.inflate(R.layout.item_home_banner, null)
        bannerView?.banner?.run {
            setDelegate(bannerDelegate)
        }

        homeAdapter.run {
            addHeaderView(bannerView!!)
            setOnItemChildClickListener { adapter, _, position ->
                val item = adapter.data[position] as Article
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Article
                itemChildClick(item, view, position)
            }
            loadMoreModule.setOnLoadMoreListener {
                swipeRefreshLayout.isRefreshing = false
                pageNum++
                mPresenter?.requestArticles(pageNum)
            }
        }
    }


    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestHomeData()
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
                    homeAdapter.setData(position, item)
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


    @SuppressLint("CheckResult")
    override fun setBanner(banners: List<Banner>) {
        bannerDatas = banners as ArrayList<Banner>
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        Observable.fromIterable(banners)
            .subscribe { list ->
                bannerFeedList.add(list.imagePath)
                bannerTitleList.add(list.title)
            }
        bannerView?.banner?.run {
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList, bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    override fun setArticles(articles: ArticleResponseBody) {
        homeAdapter.setNewOrAddData(pageNum == 0, articles.datas)
        if (homeAdapter.data.isEmpty()) {
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

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }

    private val bannerDelegate =
        BGABanner.Delegate<ImageView, String> { banner, itemView, model, position ->
            if (bannerDatas.size > 0) {
                val data = bannerDatas[position]
                ContentActivity.start(activity, data.id, data.title, data.url)
            }
        }
}