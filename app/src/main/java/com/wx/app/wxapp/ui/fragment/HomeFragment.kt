package com.wx.app.wxapp.ui.fragment

import android.support.v4.content.ContextCompat
import android.view.View
import com.wx.app.wxapp.R
import com.wx.app.wxapp.R.layout.fragment_home
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.bean.HomeContentBean
import com.wx.app.wxapp.mvp.module.HomeModuleImpl
import com.wx.app.wxapp.mvp.presenter.HomePresenterImpl
import com.wx.app.wxapp.mvp.presenter.`interface`.HomePresenter
import com.wx.app.wxapp.mvp.view.HomeView
import com.wx.app.wxapp.ui.adapter.HomeFragmentPagerAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.utils.ScreenUtils
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home.*


/**

 * 描述：
状态栏透明和间距处理
 *  StatusBarUtil.darkMode(activity)
StatusBarUtil.setPaddingSmart(activity, toolbar)

 * @author wx

 * @date 2018/7/20/020

 */
class HomeFragment : BaseContentFragment<HomePresenter>(), HomeView {
    override fun initEvent() {
        val PENDING_ACTION_NONE = 0x0
        val PENDING_ACTION_EXPANDED = 0x1
        val PENDING_ACTION_COLLAPSED = 0x2
        val PENDING_ACTION_ANIMATE_ENABLED = 0x4
        val PENDING_ACTION_FORCE = 0x8
        ab_home.addOnOffsetChangedListener { appBarLayout, verticalOffset ->


        }
    }

    private lateinit var banner: View

    private lateinit var tabTitle: ArrayList<String>

    override fun initView() {
        mPresenter.attachView()

        banner = View.inflate(context, R.layout.banner_home, null)
        initToolBar()
    }

    private fun initToolBar() {
        tb_home_title.setPadding(0,ScreenUtils.getStatusBarHeight(context),0,0)
        tb_home_title.logo = ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher)
    }

    override fun showBanner(list: ArrayList<HomeBean.Issue.Item>) {
//        var adapter = HomeBannerAdapter(item_home_banner, list)
//        banner.rc_main_banner.layoutManager = PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL)
//        val pageSnapHelper = PagerGridSnapHelper()
//        banner.rc_main_banner.adapter = adapter
//        pageSnapHelper.attachToRecyclerView(banner.rc_main_banner)
//
//        adapter.setOnItemClickListener { adapter, view, position ->
//        }
    }

    override fun createPresenter(): HomePresenter = HomePresenterImpl(this, HomeModuleImpl())

    override fun showError(msg: String, errorCode: Int) {
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
        var homeListFragments: ArrayList<HomeListFragment> = ArrayList()
        for (i in 0..10) {
            val newTab = tl_scroll.newTab()
            newTab.text = "--pp---$i"
            tl_scroll.addTab(newTab)
            val instance = HomeListFragment.getInstance(i)
            instance.setParentViewPager(vp_container)
            homeListFragments.add(instance)
        }
        val pagerAdapter = HomeFragmentPagerAdapter(activity!!.supportFragmentManager)
        vp_container.adapter = pagerAdapter
        pagerAdapter.addData(homeListFragments)
        tl_scroll.setupWithViewPager(vp_container)
        vp_container.setNoScroll(false)

        for (i in 0..10) {
            tl_scroll.getTabAt(i)?.text = "--pp---$i"
        }
    }

}