package com.wx.app.wxapp.mvp.module

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.net.RetrofitManager
import io.reactivex.Observable

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/2/002

 */
class HomeListModuleImpl : BaseModuleImpl() {
    fun getHomeBannerObservable(num: Int): Observable<HomeBean> =
        RetrofitManager.apiService.getFirstHomeData(num).compose(SchedulerUtils.ioToMain())



    fun getHomeMoreDataObservable(url: String): Observable<HomeBean> =
            RetrofitManager.apiService.getMoreHomeData(url).compose(SchedulerUtils.ioToMain())
}