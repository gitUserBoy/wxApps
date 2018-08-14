package com.wx.app.wxapp.ui.fragment.base

import com.wx.app.wxapp.mvp.presenter.`interface`.BasePresenter

/**

 * 描述：

 * 需加载网络的fragment.base基类

 * @author wx

 * @date 2018/8/3/003

 */
abstract class BaseContentFragment<T : BasePresenter> : BaseFragment(){
//    @Inject
//    protected val mPresenter: T? = null//by lazy {  createPresenter() }

    protected val mPresenter: T by lazy {  createPresenter() }
    abstract fun createPresenter():T
}