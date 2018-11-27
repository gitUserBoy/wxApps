package com.wx.app.wxapp.mvp.presenter

import com.hazz.kotlinmvp.net.exception.ExceptionHandle.Companion.errorCode
import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.MediaListContract
import com.wx.app.wxapp.mvp.presenter.base.BasePresenter

/**
 * 作者: wu xu
 * 日期: 2018/10/24/024.

 * 说明:
 */
class MediaListPresenter : BasePresenter<MediaListContract.MediaListView, MediaListContract.MediaListModule>()
        , MediaListContract.MediaListPresenter<MediaListContract.MediaListView, MediaListContract.MediaListModule>, MediaListContract.MediaListModule.MediaListener {
    override fun loadNextDataError(esg: String, errorType: Int) {
        view!!.showError(esg, errorCode)
    }

    override fun loadNextDataSuccess(t: MediaBean) {

    }

    override fun loadDataSuccess(t: MediaBean) {
        view!!.complete()
    }


    override fun loadDataError(esg: String, errorCode: Int) {
        view!!.showError(esg, errorCode)
    }

    override fun loadNextData(url: String) {
        this.addSubscribe(module!!.loadNextData(url, this))
    }

    override fun loadData(num: Int) {
        view!!.showLoading()
        this.addSubscribe(module!!.loadData(num, this))
    }
}