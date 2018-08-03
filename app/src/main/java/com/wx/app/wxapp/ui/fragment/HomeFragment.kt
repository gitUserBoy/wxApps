package com.wx.app.wxapp.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.wx.app.wxapp.R
import com.wx.app.wxapp.R.layout.*
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.bean.HomeContentBean
import com.wx.app.wxapp.mvp.module.HomeModuleImpl
import com.wx.app.wxapp.mvp.presenter.HomePresenter
import com.wx.app.wxapp.mvp.view.HomeView
import com.wx.app.wxapp.ui.adapter.HomeBannerAdapter
import com.wx.app.wxapp.ui.adapter.HomeListAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.widget.recycler_pager.PagerGridLayoutManager
import com.wx.app.wxapp.widget.recycler_pager.PagerGridSnapHelper
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.banner_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/20/020

 */
class HomeFragment : BaseContentFragment<HomePresenter>(), HomeView {
    lateinit var banner: View

    override fun initView() {
        mPresenter.attachView()

        mPresenter.showLoading()

        banner = View.inflate(context, R.layout.banner_home,null)

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
    }

    override fun showBanner(list: ArrayList<HomeBean.Issue.Item>) {
        var adapter = HomeBannerAdapter(item_home_banner, list)
        banner.rc_main_banner.layoutManager = PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL)
        banner.rc_main_banner.adapter = adapter
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(banner.rc_main_banner)
    }

    override fun createPresenter(): HomePresenter = HomePresenter(this, HomeModuleImpl())

    override fun showError(msg: String,errorCode:Int) {
        multipleStatusView.showEmpty()
    }

    override fun showLoading() {
        if (multipleStatusView != null) {
            multipleStatusView!!.showLoading()
        }
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun loadMore() {
    }

    override fun showRvList(list: ArrayList<HomeContentBean>) {

    }

    override fun statusViewId(): MultipleStatusView = vw_multiple

    override fun layoutId(): Int = fragment_home

    override fun initData() {
        mPresenter.loadData(1)
    }
    override fun loadData() {
    }

    override fun loadFinish() {
        var list = mutableListOf<HomeContentBean>()
                for (i in 0..10){
                    list.add(HomeContentBean("--$i"))
                }

        var adapter2 = HomeListAdapter(item_home_content, list)
        adapter2.addHeaderView(banner)
        rc_main_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rc_main_list.adapter = adapter2
    }

    private fun createData() {
//        var view = View.inflate(context, banner_home,null)
//        var adapter = HomeBannerAdapter(item_home_banner, banner)
//        view.rc_main_banner.layoutManager = PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL)
//        view.rc_main_banner.adapter = adapter
//        val pageSnapHelper = PagerGridSnapHelper()
//        pageSnapHelper.attachToRecyclerView(view.rc_main_banner)
//        var adapter2 = HomeListAdapter(item_home_content, banner)
//        adapter2.addHeaderView(view)
//        rc_main_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        rc_main_list.adapter = adapter2
    }


}