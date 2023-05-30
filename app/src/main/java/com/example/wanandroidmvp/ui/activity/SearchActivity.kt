package com.example.wanandroidmvp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanandroidmvp.R
import androidx.appcompat.widget.SearchView
import com.example.wanandroidmvp.adapter.SearchHistoryAdapter
import com.example.wanandroidmvp.base.BaseMvpSwipeBackActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.mvp.contract.SearchContract
import com.example.wanandroidmvp.mvp.model.bean.HotSearchBean
import com.example.wanandroidmvp.mvp.model.bean.SearchHistoryBean
import com.example.wanandroidmvp.mvp.presenter.SearchPresenter
import com.example.wanandroidmvp.utils.CommonUtil
import com.example.wanandroidmvp.utils.DisplayManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : BaseMvpSwipeBackActivity<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {

    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter()
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun createPresenter(): SearchContract.Presenter = SearchPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {}

    override fun initView() {
        super.initView()
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_search_flow_layout.run {
            setOnTagClickListener { view, position, parent ->
                if (mHotSearchDatas.size > 0) {
                    val hotSearchBean = mHotSearchDatas[position]
                    goToSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }
        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
        }

        searchHistoryAdapter.run {
            setEmptyView(R.layout.search_empty_view)
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as SearchHistoryBean
                itemClick(item)
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as SearchHistoryBean
                itemChildClick(item, view, position)
            }
        }

        search_history_clear_all_tv.setOnClickListener {
            searchHistoryAdapter.setList(mutableListOf())
            mPresenter?.clearAllHistory()
        }
        mPresenter?.getHotSearchData()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.queryHistory()
    }

    override fun start() {
    }

    private fun goToSearchList(key: String) {
        mPresenter?.saveSearchKey(key)
        Intent(this, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, Constant.Type.SEARCH_TYPE_KEY)
            putExtra(Constant.SEARCH_KEY, key)
            startActivity(this)
        }
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas) {
            override fun getView(
                parent: FlowLayout?,
                position: Int,
                hotSearchBean: HotSearchBean?
            ): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                    R.layout.flow_layout_tv,
                    hot_search_flow_layout, false
                ) as TextView
                val padding: Int = DisplayManager.dip2px(10F)
                tv.setPadding(padding, padding, padding, padding)
                tv.text = hotSearchBean?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }

        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.setOnQueryTextListener(queryTextListener)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    private val queryTextListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            goToSearchList(p0.toString())
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return true
        }

    }

    private fun itemClick(item: SearchHistoryBean) {
        goToSearchList(item.key)
    }

    private fun itemChildClick(item: SearchHistoryBean, view: View, position: Int) {
        when (view.id) {
            R.id.iv_clear -> {
                mPresenter?.deletedById(item.id)
                searchHistoryAdapter.removeAt(position)
            }
        }
    }
}