package com.example.wanandroidmvp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseSwipeBackActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.ui.fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class CommonActivity : BaseSwipeBackActivity() {
    private var mType = ""

    override fun attachLayoutRes(): Int = R.layout.activity_common

    override fun initData() {}

    override fun initView() {
        val extras = intent.extras ?: return
        mType = extras.getString(Constant.TYPE_KEY, "")
        toolbar.run {
            title = resources.getString(R.string.app_name)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        println(mType)
        val fragment = when (mType) {
            Constant.Type.COLLECT_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.collect)
                CollectFragment.getInstance(extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.about_us)
                AboutFragment.getInstance(extras)
            }
            Constant.Type.SETTING_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.setting)
                SearchListFragment.getInstance(extras)
            }
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.add)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SEARCH_TYPE_KEY -> {
                toolbar.title = extras.getString(Constant.SEARCH_KEY, "")
                SearchListFragment.getInstance(extras)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.edit)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.see)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SHARE_ARTICLE_TYPE_KEY -> {
                toolbar.title = resources.getString(R.string.share_article)
                ShareArticleFragment.getInstance()
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
            .commit()
    }

    override fun start() {
    }

    override fun initColor() {
        super.initColor()
        EventBus.getDefault().post(ColorEvent(true, mThemeColor))
    }
}