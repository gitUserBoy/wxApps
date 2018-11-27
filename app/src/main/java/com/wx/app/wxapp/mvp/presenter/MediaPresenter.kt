package com.wx.app.wxapp.mvp.presenter

import android.os.Handler
import android.os.Message
import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.MediaContract
import com.wx.app.wxapp.mvp.presenter.base.BasePresenter

/**
 * 作者: wu xu
 * 日期: 2018/10/24/024.

 * 说明:
 */
class MediaPresenter : BasePresenter<MediaContract.MediaView, MediaContract.MediaModule>()
        , MediaContract.MediaPresenter<MediaContract.MediaView, MediaContract.MediaModule>, MediaContract.MediaModule.MediaListener {
    override fun showBanner(itemList: ArrayList<MediaBean.Issue.Item>) {
    }

    val handler:Handler= object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            view!!.complete()
        }
    }

    override fun loadDataError(esg:String) {
    }

    override fun loadData(num:Int) {
        view!!.showLoading()
        this.addSubscribe(module!!.loadData(num,this))
    }
}