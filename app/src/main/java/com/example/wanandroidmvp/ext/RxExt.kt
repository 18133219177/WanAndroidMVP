package com.example.wanandroidmvp.ext

import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.app.App
import com.example.wanandroidmvp.base.IModel
import com.example.wanandroidmvp.base.IView
import com.example.wanandroidmvp.http.exception.ErrorState
import com.example.wanandroidmvp.http.exception.ExceptionHandle
import com.example.wanandroidmvp.http.fuction.RetryWithDelay
import com.example.wanandroidmvp.mvp.model.bean.BaseBean
import com.example.wanandroidmvp.rx.SchedulerUtils
import com.example.wanandroidmvp.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

fun <T : BaseBean> Observable<T>.ss(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit
) {
    this.compose(SchedulerUtils.inToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) view?.showLoading()
                model?.addDisposable(d)
                if (!NetWorkUtil.isNetworkConnected(App.instance)) {
                    view?.showDefaultMsg(App.instance.resources.getString(R.string.network_unavailable_tip))
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                when {
                    t.errorCode == ErrorState.SUCCESS -> onSuccess.invoke(t)
                    t.errorCode == ErrorState.TOKEN_INVALID -> {
                        //TOKEN过期
                    }
                    else -> view?.showDefaultMsg(t.errorMsg)
                }
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(e))
            }
        })
}

fun <T : BaseBean> Observable<T>.sss(
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) view?.showLoading()
    return this.compose(SchedulerUtils.inToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            when {
                it.errorCode == ErrorState.SUCCESS -> onSuccess.invoke(it)
                it.errorCode == ErrorState.TOKEN_INVALID -> {
//TOKEN过期
                }
                else -> {
                    if (onError != null) {
                        onError.invoke(it)
                    } else {
                        if (it.errorMsg.isNotEmpty())
                            view?.showDefaultMsg(it.errorMsg)
                    }
                }
            }
            view?.hideLoading()
        }, {
            view?.hideLoading()
            view?.showError(ExceptionHandle.handleException(it))
        })
}