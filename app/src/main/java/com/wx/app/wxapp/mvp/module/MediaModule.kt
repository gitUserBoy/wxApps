package com.wx.app.wxapp.mvp.module

import android.text.TextUtils
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.constant.Constant.HOME_TYPE_BANNER2
import com.wx.app.wxapp.mvp.constract.MediaContract
import com.wx.app.wxapp.net.AllApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 作者: wuxu
 * 日期: 2018/10/25/025.

 * 说明:
 */
class MediaModule : MediaContract.MediaModule {
    private lateinit var disposable: Disposable

    override fun loadData(num:Int,listener: MediaContract.MediaModule.MediaListener):Disposable {
        AllApi.getHomeObservable(num).subscribeWith(object : Observer<MediaBean>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: MediaBean) {
                val let = t.let {
                    val issue = it.issueList[0]
                    for (item in issue.itemList) {
                        if (TextUtils.equals(item.type,HOME_TYPE_BANNER2)){

                        }
                    }
                    return@let issue.itemList
                }
//                listener.loadDataSuccess()
            }

            override fun onError(e: Throwable) {
                val esg = ExceptionHandle.handleException(e)
                listener.loadDataError(esg)
            }
        })
        return disposable
    }
}