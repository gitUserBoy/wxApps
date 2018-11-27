package com.wx.app.wxapp.net

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.wx.app.wxapp.bean.ky.MediaBean
import io.reactivex.Observable

/**
 * 作者: wuxu
 * 日期: 2018/10/24/024.

 * 说明:
 */
object AllApi {
    fun getHomeObservable(num: Int): Observable<MediaBean> =
            RetrofitManager.apiServiceInstance(RetrofitManager.KY_TYPE).getFirstHomeData(num).compose(SchedulerUtils.ioToMain())

    fun getHomeMoreDataObservable(url: String): Observable<MediaBean> =
            RetrofitManager.apiServiceInstance(RetrofitManager.KY_TYPE).getMoreHomeData(url).compose(SchedulerUtils.ioToMain())
}