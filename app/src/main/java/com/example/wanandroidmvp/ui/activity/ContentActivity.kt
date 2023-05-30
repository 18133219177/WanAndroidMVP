package com.example.wanandroidmvp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseActivity
import com.example.wanandroidmvp.base.BaseMvpSwipeBackActivity
import com.example.wanandroidmvp.base.BaseSwipeBackActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.RefreshHomeEvent
import com.example.wanandroidmvp.ext.getAgentWeb
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.ContentContract
import com.example.wanandroidmvp.mvp.presenter.ContentPresenter
import com.example.wanandroidmvp.webclient.WebClientFactory
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class ContentActivity : BaseMvpSwipeBackActivity<ContentContract.View, ContentContract.Presenter>(),
    ContentContract.View {

    private var mAgentWeb: AgentWeb? = null
    private var shareTitle: String = ""
    private var shareUrl: String = ""
    private var shareId: Int = -1

    companion object {
        fun start(context: Context?, id: Int, title: String, url: String, bundle: Bundle? = null) {
            Intent(context, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_ID_KEY, id)
                putExtra(Constant.CONTENT_TITLE_KEY, title)
                putExtra(Constant.CONTENT_URL_KEY, url)
                context?.startActivity(this, bundle)
            }
        }
    }

    override fun createPresenter(): ContentContract.Presenter = ContentPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_content

    override fun initData() {}

    override fun initView() {
        super.initView()

        intent.extras?.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, -1)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY, "")
            shareUrl = it.getString(Constant.CONTENT_URL_KEY, "")
        }
        toolbar.apply {
            title = ""
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tv_title.apply {
            text = getString(R.string.loading)
            visibility = View.VISIBLE
            postDelayed({
                tv_title.isSelected = true
            }, 2000)
        }

        initWebView()
    }

    private fun initWebView() {
        val webView = NestedScrollAgentWebView(this)

        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        mAgentWeb = shareUrl.getAgentWeb(
            this,
            cl_main,
            layoutParams,
            webView,
            WebClientFactory.create(shareUrl),
            mWebChromeClient,
            mThemeColor
        )

        mAgentWeb?.webCreator?.webView?.apply {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    override fun start() {
    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            tv_title?.text = title
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_article_url,
                            getString(R.string.app_name), shareTitle, shareUrl
                        )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like -> {
                if (isLogin) {
                    if (shareId == -1) return true
                    mPresenter?.addCollectArticle(shareId)
                } else {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    showToast(resources.getString(R.string.login_tint))
                }
                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mAgentWeb?.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb?.handleKeyEvent(keyCode, event)!!) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}