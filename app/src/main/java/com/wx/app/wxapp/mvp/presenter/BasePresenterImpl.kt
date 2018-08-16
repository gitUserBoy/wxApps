package com.wx.app.wxapp.mvp.presenter

import com.wx.app.wxapp.mvp.constract.BaseContract
import com.wx.app.wxapp.mvp.module.BaseModuleImpl
import com.wx.app.wxapp.mvp.presenter.`interface`.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
abstract class BasePresenterImpl<V : BaseContract.BaseView, M : BaseModuleImpl> : BasePresenter {
    var view: V? = null
    var module: M? = null

     var compositeDisposable = CompositeDisposable()//防止内存泄露

    constructor(v: V, m: M) {
        view = v
        module = m
    }

    override fun attachView() {
    }

    override fun detachView() {
        view = null
        module = null

        compositeDisposable.clear()
    }

    fun addSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }
}