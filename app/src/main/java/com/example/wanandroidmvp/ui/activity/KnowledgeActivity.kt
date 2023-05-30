package com.example.wanandroidmvp.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.adapter.KnowledgePagerAdapter
import com.example.wanandroidmvp.base.BaseSwipeBackActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.mvp.model.bean.Knowledge
import com.example.wanandroidmvp.mvp.model.bean.KnowledgeTreeBody
import com.example.wanandroidmvp.utils.SettingUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_knowledge.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class KnowledgeActivity : BaseSwipeBackActivity() {

    private var knowledgs = mutableListOf<Knowledge>()

    private lateinit var toolbarTitle: String

    private val viewPageAdapter: KnowledgePagerAdapter by lazy {
        KnowledgePagerAdapter(knowledgs, supportFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_knowledge

    override fun initData() {
        intent.extras?.let {
            toolbarTitle = it.getString(Constant.CONTENT_TITLE_KEY) ?: ""
            it.getSerializable(Constant.CONTENT_DATA_KEY)?.let {
                val data = it as KnowledgeTreeBody
                data.children.let { children ->
                    knowledgs.addAll(children)
                }
            }
        }
    }

    override fun useEventBus(): Boolean = true

    override fun initView() {
        toolbar.run {
            title = toolbarTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewPager.run {
            adapter = viewPageAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = knowledgs.size
        }
        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }
        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    override fun start() {
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
                floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
            }
        }
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }
    private val onFABClickListener = View.OnClickListener {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_type_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.share_article_url,
                            getString(R.string.app_name),
                            knowledgs[tabLayout.selectedTabPosition].name,
                            knowledgs[tabLayout.selectedTabPosition].id.toString()
                        )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}