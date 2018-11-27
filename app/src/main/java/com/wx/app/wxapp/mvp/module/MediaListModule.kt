package com.wx.app.wxapp.mvp.module

import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.MediaListContract
import com.wx.app.wxapp.net.AllApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 作者: wuxu
 * 日期: 2018/10/25/025.

 * 说明:
 */
class MediaListModule : MediaListContract.MediaListModule {


    private lateinit var disposable: Disposable
    private lateinit var nextDisposable: Disposable

    override fun loadData(num:Int,listener: MediaListContract.MediaListModule.MediaListener):Disposable {
        AllApi.getHomeObservable(num).subscribeWith(object : Observer<MediaBean>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: MediaBean) {
//                val itemList = t.let {
//                    val issue = it.issueList[0]
//                    return@let issue.itemList
//                }
                listener.loadDataSuccess(t)
            }

            override fun onError(e: Throwable) {
                ExceptionHandle.handleException(e)
                listener.loadDataError(ExceptionHandle.errorMsg,ExceptionHandle.errorCode)
            }
        })
        return disposable
    }

    override fun loadNextData(url: String, listener: MediaListContract.MediaListModule.MediaListener): Disposable {
        AllApi.getHomeMoreDataObservable(url).subscribeWith(object : Observer<MediaBean>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                nextDisposable = d
            }

            override fun onNext(t: MediaBean) {
                listener.loadNextDataSuccess(t)
            }

            override fun onError(e: Throwable) {
                ExceptionHandle.handleException(e)
                listener.loadDataError(ExceptionHandle.errorMsg,ExceptionHandle.errorCode)
            }
        })
        return nextDisposable
    }
}