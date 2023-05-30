package com.example.wanandroidmvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseFragment
import com.example.wanandroidmvp.utils.SettingUtil

class SettingFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): SettingFragment {
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int= R.layout.fragment_setting

    override fun initView(view: View) {
    }

    override fun lazyLoad() {
    }


}