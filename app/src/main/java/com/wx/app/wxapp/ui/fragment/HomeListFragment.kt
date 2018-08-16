package com.wx.app.wxapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.will.weiyuekotlin.utils.toast
import com.wx.app.wxapp.R
import com.wx.app.wxapp.R.layout.item_home_banner
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.mvp.module.HomeListModuleImpl
import com.wx.app.wxapp.mvp.presenter.HomeListPresenterImpl
import com.wx.app.wxapp.mvp.presenter.`interface`.HomeListPresenter
import com.wx.app.wxapp.mvp.view.HomeListView
import com.wx.app.wxapp.ui.activity.VideoPlayActivity
import com.wx.app.wxapp.ui.adapter.HomeBannerAdapter
import com.wx.app.wxapp.ui.adapter.HomeListAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.widget.recycler_pager.PagerGridLayoutManager
import com.wx.app.wxapp.widget.recycler_pager.PagerGridSnapHelper
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.banner_home.view.*
import kotlinx.android.synthetic.main.fragment_home_list.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/7/007

 */
class HomeListFragment : BaseContentFragment<HomeListPresenter>() ,HomeListView{
    private lateinit var homeList: ArrayList<HomeBean.Issue.Item>
    private lateinit var banner: View
    private lateinit var parentViewPager: ViewPager
    private var pageUrl:String = ""
    companion object {
        private const val keys:String="key"
        fun getInstance(type:Int):HomeListFragment{
            val homeListFragment = HomeListFragment()
            val bundle = Bundle()
            bundle.putInt(keys,type)
            homeListFragment.arguments = bundle
            return homeListFragment
        }
    }

    fun setParentViewPager(view: ViewPager){
        parentViewPager = view
    }

    override fun homeNextPageUrl(url: String) {
        pageUrl = url
    }

    override fun showContentList(list: ArrayList<HomeBean.Issue.Item>) {
        homeList.addAll(list)
    }

    override fun loadMoreFinish() {
        if (rc_main_list==null)return
        (rc_main_list.adapter as HomeListAdapter).setNewData(homeList)
    }

    override fun initComponent() {
    }

    override fun initEvent() {
    }

    override fun showLoading() {
        srf_refresh.autoRefresh()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun loadMore() {
    }

    override fun loadData() {
    }

    override fun loadFinish() {
    }

    override fun showError(msg: String, errorCode: Int) {

    }

    override fun createPresenter(): HomeListPresenter = HomeListPresenterImpl(this, HomeListModuleImpl())

    override fun layoutId(): Int = R.layout.fragment_home_list

    override fun statusViewId(): MultipleStatusView = vw_multiple

    override fun showData(list: ArrayList<HomeBean.Issue.Item>) {
        homeList.clear()
        homeList.addAll(list)

        var adapter = HomeBannerAdapter(item_home_banner, list)
        banner.rc_main_banner.layoutManager = PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL)
        val pageSnapHelper = PagerGridSnapHelper()
        banner.rc_main_banner.adapter = adapter
        pageSnapHelper.attachToRecyclerView(banner.rc_main_banner)
        banner.rc_main_banner.setParentPager(parentViewPager)
        adapter.setOnItemClickListener { adapter, view, position ->
            if (position == 3){
                val intent = Intent(activity, VideoPlayActivity::class.java)
                startActivity(intent)
                return@setOnItemClickListener
            }
            toast(context!!, "position : $position",Toast.LENGTH_SHORT)
        }

        if (rc_main_list==null)return

        val homeListAdapter = HomeListAdapter(R.layout.item_home_content, homeList)
        rc_main_list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
        homeListAdapter.removeAllHeaderView()
        homeListAdapter.addHeaderView(banner)
        rc_main_list.adapter = homeListAdapter
        homeListAdapter.setOnLoadMoreListener({
            if (!pageUrl.isEmpty()){
                mPresenter.loadMore(pageUrl)
            }
        },rc_main_list)
    }

    override fun initData() {
        homeList =  ArrayList()
        mPresenter.loadData(this.arguments!!.getInt(keys))


    }

    override fun initView() {
        mPresenter.attachView()

        banner = View.inflate(context, R.layout.banner_home, null)
    }
}