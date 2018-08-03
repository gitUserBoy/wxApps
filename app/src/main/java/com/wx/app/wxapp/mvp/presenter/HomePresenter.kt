package com.wx.app.wxapp.mvp.presenter

import com.wx.app.wxapp.bean.HomeContentBean
import com.wx.app.wxapp.mvp.module.HomeModuleImpl
import com.wx.app.wxapp.mvp.view.HomeView

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
class HomePresenter(v: HomeView, m: HomeModuleImpl) : BasePresenterImpl<HomeView, HomeModuleImpl>(v, m) {
    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    fun loadData(num: Int) {
        view!!.showLoading()
        addSubscribe(module!!.getHomeBannerDisposable(num)
                .subscribe({ homeBean ->
                    view!!.dismissLoading()
                    //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                    val bannerItemList = homeBean.issueList[0].itemList
                    val bannerHomeBean = homeBean

                    bannerItemList.filter { item ->
                        item.type == "banner2" || item.type == "horizontalScrollCard"
                    }.forEach { item ->
                        //移除 item
                        bannerItemList.remove(item)
                    }

//                    bannerHomeBean.issueList[0].count = bannerItemList.size
//                    bannerHomeBean.issueList[0].itemList.clear()
//                    bannerHomeBean.issueList[0].itemList.addAll(bannerItemList)
                    view!!.showBanner(bannerItemList)
                    view!!.loadFinish()
                }))

    }

    fun showRvList() {
        var rv = ArrayList<HomeContentBean>()
        for (i in 0..5) {
            rv.add(HomeContentBean("名字-$i"))
        }
        view!!.showRvList(rv)
    }
}