package com.wx.app.wxapp.mvp.presenter

import android.util.Log
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.constant.Constant
import com.wx.app.wxapp.mvp.module.HomeListModuleImpl
import com.wx.app.wxapp.mvp.presenter.`interface`.HomeListPresenter
import com.wx.app.wxapp.mvp.view.HomeListView
import io.reactivex.observers.DisposableObserver

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
class HomeListPresenterImpl(v: HomeListView, m: HomeListModuleImpl) : HomeListPresenter, BasePresenterImpl<HomeListView, HomeListModuleImpl>(v, m) {
    override fun loadMore(url:String) {
        view!!.loadMore()
        var observer = object : DisposableObserver<HomeBean>() {
            override fun onComplete() {
                Log.e("home", "onComplete")
                view!!.loadMoreFinish()
            }

            override fun onNext(homeBean: HomeBean) {
                Log.e("home", "onNext")
                view!!.dismissLoading()
                //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                val bannerItemList = homeBean.issueList[0].itemList
                bannerItemList.filter { item ->
                    item.type != "video"
                }.forEach { item ->
                    //移除 item
                    bannerItemList.remove(item)
                }
                view!!.showContentList(bannerItemList)

                view!!.homeNextPageUrl(homeBean.nextPageUrl)
            }

            override fun onError(e: Throwable) {
                Log.e("home", "Throwable$e")
                view!!.showError("--加载失败--", Constant.ERROR_LOADMORE_FAILD)
            }
        }
        module!!.getHomeMoreDataObservable(url).subscribe(observer)
        addSubscribe(observer)
    }

    override fun loadData(num: Int) {
        view!!.showLoading()
        var observer = object : DisposableObserver<HomeBean>() {
            override fun onComplete() {
                Log.e("home", "onComplete")
                view!!.loadFinish()
            }
            override fun onNext(homeBean: HomeBean) {
                Log.e("home", "onNext")
                view!!.dismissLoading()
                //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                val bannerItemList = homeBean.issueList[0].itemList

                bannerItemList.filter { item ->
                    item.type == "banner2" || item.type == "horizontalScrollCard"
                }.forEach { item ->
                    //移除 item
                    bannerItemList.remove(item)
                }
                view!!.showData(bannerItemList)

                view!!.showContentList(bannerItemList)

                view!!.homeNextPageUrl(homeBean.nextPageUrl)
            }

            override fun onError(e: Throwable) {
                Log.e("home", "Throwable$e")
            }
        }
        module!!.getHomeBannerObservable(num).subscribe(observer)
        addSubscribe(observer)
    }


}