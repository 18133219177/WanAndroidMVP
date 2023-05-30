package com.example.wanandroidmvp.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.wanandroidmvp.R
import com.example.wanandroidmvp.base.BaseMvpFragment
import com.example.wanandroidmvp.event.RefreshShareEvent
import com.example.wanandroidmvp.mvp.contract.ShareArticleContract
import com.example.wanandroidmvp.mvp.presenter.ShareArticlePresenter
import com.example.wanandroidmvp.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_share_article.*
import org.greenrobot.eventbus.EventBus

class ShareArticleFragment :
    BaseMvpFragment<ShareArticleContract.View, ShareArticleContract.Presenter>(),
    ShareArticleContract.View {

    companion object {
        fun getInstance(): ShareArticleFragment = ShareArticleFragment()
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(requireActivity(), getString(R.string.submit_ing))
    }

    override fun createPresenter(): ShareArticleContract.Presenter = ShareArticlePresenter()

    override fun attachLayoutRes(): Int = R.layout.fragment_share_article

    override fun lazyLoad() {
    }

    override fun getArticleTitle(): String = et_article_title.text.toString().trim()

    override fun getArticleLink(): String = et_article_link.text.toString().trim()

    override fun showLoading() {
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun initView(view: View) {
        setHasOptionsMenu(true)
        super.initView(view)
    }

    override fun showShareArticle(success: Boolean) {
        if (success) {
            showDefaultMsg(getString(R.string.share_success))
            EventBus.getDefault().post(RefreshShareEvent(false))
            activity?.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share_article, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share_article->{
                mPresenter?.shareArticle()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}