package com.example.wanandroidmvp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.cxz.multipleview.MultipleStatusView
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.constant.Constant
import com.example.wanandroidmvp.event.NetworkChangeEvent
import com.example.wanandroidmvp.utils.Preference
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment : Fragment() {

    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    protected var hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    private var isViewPrepare = false

    private var hasLoadData = false

    protected var mLayoutStatusView: MultipleStatusView? = null

    @LayoutRes
    abstract fun attachLayoutRes(): Int

    abstract fun initView(view: View)

    abstract fun lazyLoad()

    open fun useEventBus(): Boolean = true

    open fun doReConnected() {
        lazyLoad()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(attachLayoutRes(), null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        isViewPrepare = true
        initView(view)
        lazyLoadDataIfPrepared()
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        if (event.isConnected) {
            doReConnected()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
      //  activity?.let { App.getRefWatcher(it)?.watch(activity) }
    }
}