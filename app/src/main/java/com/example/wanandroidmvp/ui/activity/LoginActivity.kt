package com.example.wanandroidmvp.ui.activity

import android.app.Dialog
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseMvpActivity
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.LoginEvent
import com.example.wanandroidmvp.ext.showToast
import com.example.wanandroidmvp.mvp.contract.LoginContract
import com.example.wanandroidmvp.mvp.model.bean.LoginData
import com.example.wanandroidmvp.mvp.presenter.LoginPresenter
import com.example.wanandroidmvp.utils.DialogUtil
import com.example.wanandroidmvp.utils.Preference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseMvpActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {

    private var user: String by Preference(Constant.USERNAME_KEY, "")

    private var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    private var token: String by Preference(Constant.TOKEN_KEY, "")

    override fun createPresenter(): LoginContract.Presenter = LoginPresenter()

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, resources.getString(R.string.login_ing))
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_login

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    override fun initData() {
    }

    override fun initView() {
        super.initView()

        et_username.setText(user)
        toolbar.run {
            title = resources.getString(R.string.login)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        btn_login.setOnClickListener(onClickListener)
        tv_sign_up.setOnClickListener(onClickListener)
    }

    override fun start() {
    }

    override fun loginSuccess(data: LoginData) {
        showToast(resources.getString(R.string.login_success))
        isLogin = true
        user = data.username
        pwd = data.password
        token = data.token

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    override fun loginFail() {
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_login -> {
                login()
            }
            R.id.tv_sign_up -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    private fun login() {
        if (validate()) {
            mPresenter?.loginWanAndroid(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()

        if (username.isEmpty()) {
            et_username.error = resources.getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            et_password.error = resources.getString(R.string.password_not_empty)
            valid = false
        }
        return valid
    }
}