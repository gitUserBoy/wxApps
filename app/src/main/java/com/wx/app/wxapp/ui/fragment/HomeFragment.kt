package com.wx.app.wxapp.ui.fragment

import com.wx.app.wxapp.R
import com.wx.app.wxapp.constant.Constant.array_media_online
import com.wx.app.wxapp.mvp.constract.MediaContract
import com.wx.app.wxapp.mvp.module.MediaModule
import com.wx.app.wxapp.mvp.presenter.MediaPresenter
import com.wx.app.wxapp.ui.adapter.HomeFragmentPagerAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.utils.ImageLoaderUtil
import com.wx.app.wxapp.utils.ScreenUtils
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home.*


/**

 * 描述：

 * @author wx

 * @date 2018/7/20/020

 */
class HomeFragment : BaseContentFragment<MediaContract.MediaView, MediaContract.MediaModule, MediaPresenter>(), MediaContract.MediaView {
    override fun chileModule(): MediaContract.MediaModule = MediaModule()

    override fun createPresenter(): MediaPresenter = MediaPresenter()

    override fun chileView(): MediaContract.MediaView = this

    override fun layoutId(): Int = R.layout.fragment_home

    override fun statusViewId(): MultipleStatusView = getView()!!.findViewById(R.id.vw_multiple)

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun initView() {
        initTab()
        initToolBar()
    }

    override fun initData() {
        showListData()
    }

    override fun showListData() {
//        mPresenter.loadData(0)
    }
    private val picUrl = "http://api.dujin.org/bing/1920.php"


    private fun initToolBar() {
        tb_home_title.setPadding(0, ScreenUtils.getStatusBarHeight(context),0,0)
        ImageLoaderUtil.LoadImage(context,picUrl,iv_bg)
    }
    private fun initTab() {
        var homeListFragment: ArrayList<HomeListFragment> = ArrayList()
        for (i in array_media_online.indices) {
            val newTab = tl_scroll.newTab()
            newTab.text=array_media_online[i]
            tl_scroll.addTab(newTab)
            homeListFragment.add(HomeListFragment.newInstance(i))
        }
        val pagerAdapter = HomeFragmentPagerAdapter(activity!!.supportFragmentManager)
        vp_container.adapter = pagerAdapter
        pagerAdapter.addData(homeListFragment)
        tl_scroll.setupWithViewPager(vp_container)
        vp_container.setNoScroll(false)
        for (i in array_media_online.indices) {
            tl_scroll.getTabAt(i)!!.text = array_media_online[i]
        }
    }
}