package com.example.wanandroidmvp.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuItemCompat
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.BaseMvpActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.ColorEvent
import com.example.wanandroidmvp.event.LoginEvent
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.MainContract
import com.example.wanandroidmvp.mvp.model.bean.UserInfoBody
import com.example.wanandroidmvp.mvp.presenter.MainPresenter
import com.example.wanandroidmvp.ui.fragment.*
import com.example.wanandroidmvp.utils.DialogUtil
import com.example.wanandroidmvp.utils.Preference
import com.example.wanandroidmvp.utils.SettingUtil
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(),
    MainContract.View {

    private val BOTTOM_INDEX: String = "bottom_index"

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_SQUARE = 0x02
    private val FRAGMENT_WECHAT = 0x03
    private val FRAGMENT_SYSTEM = 0x04
    private val FRAGMENT_PROJECT = 0x05

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment: HomeFragment? = null
    private var mSquareFragment: SquareFragment? = null
    private var mWechatFragment: WechatFragment? = null
    private var mSystemFragment: SystemFragment? = null
    private var mProjectFragment: ProjectFragment? = null

    private var mExitTime: Long = 0

    private var username: String by Preference(Constant.USERNAME_KEY, "")

    private var nav_username: TextView? = null
    private var nav_user_id: TextView? = null
    private var nav_user_rank: TextView? = null
    private var nav_user_grade: TextView? = null
    private var nav_score: TextView? = null
    private var nav_rank: ImageView? = null

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun useEventBus(): Boolean = true

    override fun initData() {
        Beta.checkUpgrade(false, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }
        bottom_navigation.run {
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawerLayout()

        initNavView()

        showFragment(mIndex)

        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    override fun start() {
        mPresenter?.getUserInfo()
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initNavView() {
        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            nav_user_id = getHeaderView(0).findViewById(R.id.tv_user_id)
            nav_user_grade = getHeaderView(0).findViewById(R.id.tv_user_grade)
            nav_user_rank = getHeaderView(0).findViewById(R.id.tv_user_rank)
            nav_rank = getHeaderView(0).findViewById(R.id.iv_rank)
            nav_score =
                MenuItemCompat.getActionView(nav_view.menu.findItem(R.id.nav_score)) as TextView
            nav_score?.gravity = Gravity.CENTER_VERTICAL
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username?.run {
            text = if (!isLogin) getString(R.string.go_login) else username
            setOnClickListener {
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }
        nav_rank?.setOnClickListener {
            startActivity(Intent(this@MainActivity, RankActivity::class.java))
        }
    }


    override fun showUserInfo(bean: UserInfoBody) {
        App.userInfo = bean
        nav_user_id?.text = bean.userId.toString()
        nav_user_grade?.text = (bean.coinCount / 100 + 1).toString()
        nav_user_rank?.text = bean.rank.toString()
        nav_score?.text = bean.coinCount.toString()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        if (event.isLogin) {
            nav_username?.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
            mPresenter?.getUserInfo()
        } else {
            nav_username?.text = getString(R.string.go_login)
            nav_view.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoad()

            nav_user_id?.text = getString(R.string.nav_line_4)
            nav_user_grade?.text = getString(R.string.nav_line_2)
            nav_user_rank?.text = getString(R.string.nav_line_2)
            nav_score?.text = " "
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_INDEX, mIndex)
    }

    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index) {
            FRAGMENT_HOME -> {
                toolbar.title = getString(R.string.app_name)
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container, mHomeFragment!!, "home")
                } else {
                    transaction.show(mHomeFragment!!)
                }
            }
            FRAGMENT_SQUARE -> {
                toolbar.title = getString(R.string.square)
                if (mSquareFragment == null) {
                    mSquareFragment = SquareFragment.getInstance()
                    transaction.add(R.id.container, mSquareFragment!!, "square")
                } else {
                    transaction.show(mSquareFragment!!)
                }
            }
            FRAGMENT_WECHAT -> {
                toolbar.title = getString(R.string.wechat)
                if (mWechatFragment == null) {
                    mWechatFragment = WechatFragment.getInstance()
                    transaction.add(R.id.container, mWechatFragment!!, "wechat")
                } else {
                    transaction.show(mWechatFragment!!)
                }
            }
            FRAGMENT_SYSTEM -> {
                toolbar.title = getString(R.string.knowledge_system)
                if (mSystemFragment == null) {
                    mSystemFragment = SystemFragment.getInstance()
                    transaction.add(R.id.container, mSystemFragment!!, "system")
                } else {
                    transaction.show(mSystemFragment!!)
                }
            }
            FRAGMENT_PROJECT -> {
                toolbar.title = getString(R.string.project)
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container, mProjectFragment!!, "project")
                } else {
                    transaction.show(mProjectFragment!!)
                }
            }
        }
        transaction.commit()
    }

    private fun hideFragments(transaction: androidx.fragment.app.FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mSquareFragment?.let { transaction.hide(it) }
        mWechatFragment?.let { transaction.hide(it) }
        mSystemFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
    }


    private val onNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            return@OnItemSelectedListener when (item.itemId) {
                R.id.action_home -> {
                    showFragment(FRAGMENT_HOME)
                    true
                }
                R.id.action_square -> {
                    showFragment(FRAGMENT_SQUARE)
                    true
                }
                R.id.action_wechat -> {
                    showFragment(FRAGMENT_WECHAT)
                    true
                }
                R.id.action_system -> {
                    showFragment(FRAGMENT_SYSTEM)
                    true
                }
                R.id.action_project -> {
                    showFragment(FRAGMENT_PROJECT)
                    true
                }
                else -> {
                    false
                }
            }
        }


    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                mHomeFragment?.scrollToTop()
            }
            FRAGMENT_SQUARE -> {
                mSquareFragment?.scrollToTop()
            }
            FRAGMENT_WECHAT -> {
                mWechatFragment?.scrollToTop()
            }
            FRAGMENT_SYSTEM -> {
                mSystemFragment?.scrollToTop()
            }
            FRAGMENT_PROJECT -> {
                mProjectFragment?.scrollToTop()
            }
        }
    }

    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_score -> {
                    if (isLogin) {
                        Intent(this@MainActivity, ScoreActivity::class.java).run {
                            startActivity(this)
                        }
                    } else {
                        showToast(resources.getString(R.string.go_login))
                        goLogin()
                    }
                }
                R.id.nav_collect -> {
                    if (isLogin) {
                        goCommonActivity(Constant.Type.COLLECT_TYPE_KEY)
                    } else {
                        showToast(resources.getString(R.string.go_login))
                        goLogin()
                    }
                }
                R.id.nav_share -> {
                    if (isLogin) {
                        Intent(this@MainActivity, ShareActivity::class.java).run {
                            startActivity(this)
                        }
                    } else {
                        showToast(resources.getString(R.string.go_login))
                        goLogin()
                    }
                }
                R.id.nav_setting -> {
                    Intent(this@MainActivity, SettingActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_night_mode -> {
                    if (SettingUtil.getIsNightMode()) {
                        SettingUtil.setIsNightMode(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        SettingUtil.setIsNightMode(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                    recreate()
                }
                R.id.nav_todo -> {
                    if (isLogin) {
                        Intent(this@MainActivity, TodoActivity::class.java).run {
                            startActivity(this)
                        }
                    } else {
                        showToast(resources.getString(R.string.login_tint))
                        goLogin()
                    }
                }
            }
            true
        }

    private fun goCommonActivity(type: String) {
        Intent(this@MainActivity, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, type)
            startActivity(this)
        }
    }

    private fun goLogin() {
        Intent(this@MainActivity, LoginActivity::class.java).run {
            startActivity(this)
        }
    }

    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mHomeFragment != null) {
                fragmentTransaction.remove(mHomeFragment!!)
            }
            if (mSquareFragment != null) {
                fragmentTransaction.remove(mSquareFragment!!)
            }
            if (mWechatFragment != null) {
                fragmentTransaction.remove(mWechatFragment!!)
            }
            if (mSystemFragment != null) {
                fragmentTransaction.remove(mSystemFragment!!)
            }
            if (mProjectFragment != null) {
                fragmentTransaction.remove(mProjectFragment!!)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

    //退出登录Dialog
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this@MainActivity, resources.getString(R.string.logout_ing))
    }

    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
            DialogInterface.OnClickListener { _, _ ->
                mDialog.show()
                mPresenter?.logout()
            }).show()
    }

    override fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                Preference.clearPreference()
                uiThread {
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    username = nav_username?.text.toString().trim()
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mIndex != FRAGMENT_SQUARE) {
            menuInflater.inflate(R.menu.menu_activity_main, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeFragment = null
        mSquareFragment = null
        mWechatFragment = null
        mSystemFragment = null
        mProjectFragment = null
    }
}