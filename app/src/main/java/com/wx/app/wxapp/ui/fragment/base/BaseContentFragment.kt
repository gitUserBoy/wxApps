package com.wx.app.wxapp.ui.fragment.base

import com.wx.app.wxapp.mvp.constract.base.BaseContract
import com.wx.app.wxapp.mvp.presenter.base.BasePresenter

/**

 * 描述：

 * 需加载网络的fragment.base基类

 * @author wx

 * @date 2018/8/3/003

 */

abstract class BaseContentFragment<V : BaseContract.BaseView,M : BaseContract.BaseModule, out T : BasePresenter<V,M>>
    : BaseFragment(), BaseContract.BaseView {

    val mPresenter: T by lazy { createPresenter() }

    override fun attachView() {
        mPresenter!!.attachView(chileView(),chileModule())
    }
    override fun detachView() {
        mPresenter!!.detachView()
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun complete() {
        multipleStatusView.showContent()
    }


    abstract fun createPresenter(): T

    abstract fun chileView(): V

    abstract fun chileModule():M

}
