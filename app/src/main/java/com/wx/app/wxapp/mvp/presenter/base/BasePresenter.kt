package com.wx.app.wxapp.mvp.presenter.base

import com.wx.app.wxapp.mvp.constract.base.BaseContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 作者: wu xu
 * 日期: 2018/10/24/024.

 * 说明:
 */
abstract class BasePresenter<V : BaseContract.BaseView, M : BaseContract.BaseModule> : BaseContract.BasePresenter<V,M> {
    var view: V? = null
    var module: M? = null
    private var compositeDisposable = CompositeDisposable()//防止内存泄露
    override fun attachView(view: V,module:M) {
        this.view = view
        this.module = module
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