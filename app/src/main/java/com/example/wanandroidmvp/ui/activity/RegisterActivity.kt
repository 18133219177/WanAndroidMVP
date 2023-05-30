package com.example.wanandroidmvp.ui.activity

import android.content.Intent
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseMvpActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.LoginEvent
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.RegisterContract
import com.example.wanandroidmvp.mvp.model.bean.LoginData
import com.example.wanandroidmvp.mvp.presenter.RegisterPresenter
import com.example.wanandroidmvp.utils.DialogUtil
import com.example.wanandroidmvp.utils.Preference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_username
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : BaseMvpActivity<RegisterContract.View, RegisterContract.Presenter>(),
    RegisterContract.View {

    private var user: String by Preference(Constant.USERNAME_KEY, "")

    private var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(
            this,
            resources.getString(R.string.register_ing)
        )
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun createPresenter(): RegisterContract.Presenter = RegisterPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_register

    override fun initData() {
    }

    override fun start() {
    }

    override fun registerSuccess(data: LoginData) {
        showToast(resources.getString(R.string.register_success))
        isLogin = true
        user = data.username
        pwd = data.password

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    override fun registerFail() {
        isLogin = false
    }

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    override fun initView() {
        super.initView()
        toolbar.run {
            title = resources.getString(R.string.register)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        btn_register.setOnClickListener(onClickListener)
        tv_sign_in.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_register -> {
                register()
            }
            R.id.tv_sign_in -> {
                Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    private fun register() {
        if (validate()) {
            mPresenter?.registerWanAndroid(
                et_username.text.toString(),
                et_password_r.text.toString(),
                et_password2.text.toString()
            )
        }
    }

    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password_r.text.toString()
        val password2: String = et_password2.text.toString()

        if (username.isEmpty()) {
            et_username.error = resources.getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            et_password_r.error = resources.getString(R.string.password_not_empty)
            valid = false
        }
        if (password2.isEmpty()) {
            et_password2.error = resources.getString(R.string.confirm_password_not_empty)
            valid = false
        }
        if (password != password2) {
            et_password2.error = resources.getString(R.string.password_cannot_match)
            valid = false
        }
        return valid
    }
}