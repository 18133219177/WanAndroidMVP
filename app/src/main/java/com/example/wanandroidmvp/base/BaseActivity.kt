package com.example.wanandroidmvp.base

import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import  androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.color.CircleView
import com.cxz.multipleview.MultipleStatusView
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.NetworkChangeEvent
import com.example.wanandroidmvp.receiver.NetworkChangeReceiver
import com.example.wanandroidmvp.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {

    //check login
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    //缓存上一次的网络状态
    protected var hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    //网络状态变化的广播
    protected var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    //theme color
    protected var mThemeColor: Int = SettingUtil.getColor()

    //多种状态View的切换
    protected var mLayoutStatusView: MultipleStatusView? = null

    protected lateinit var mTipView: View
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    //布局文件id
    protected abstract fun attachLayoutRes(): Int

    //初始化数据
    abstract fun initData()

    //初始化View
    abstract fun initView()

    abstract fun start()

    open fun useEventBus(): Boolean = true

    open fun enableNetworkTip(): Boolean = true

    open fun doReConnected() {
        start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initData()
        initTipView()
        initView()
        start()
        initListener()
    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction("android.net.com.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver, filter)
        super.onResume()

        initColor()
    }

    open fun initColor() {
        mThemeColor = if (!SettingUtil.getIsNightMode()) {
            SettingUtil.getColor()
        } else {
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = CircleView.shiftColorDown(mThemeColor)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }

    private fun initTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip, null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.windowAnimations = R.style.anim_float_view
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    protected fun initToolBar(toolbar: Toolbar, homeAsUpEnable: Boolean, title: String) {
        toolbar?.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        hasNetwork = event.isConnected
        checkNetwork(event.isConnected)
    }

    private fun checkNetwork(isConnected: Boolean) {
        if (enableNetworkTip()) {
            if (isConnected) {
                doReConnected()
                if (mTipView != null && mTipView.parent != null) {
                    mWindowManager.removeView(mTipView)
                }
            } else {
                if (mTipView.parent == null) {
                    mWindowManager.addView(mTipView, mLayoutParams)
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyboard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onPause() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        CommonUtil.fixInputMethodManagerLeak(this)
        //App.getRefWatcher(this)?.watch(this)
    }

    override fun finish() {
        super.finish()
        if (mTipView != null && mTipView.parent != null) {
            mWindowManager.removeView(mTipView)
        }
    }
}