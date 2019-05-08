package com.wx.app.wxapp.ui.fragment

import android.util.Log
import android.view.ViewGroup
import com.wx.app.wxapp.R
import com.wx.app.wxapp.constant.Constant.array_media_online
import com.wx.app.wxapp.mvp.constract.MediaContract
import com.wx.app.wxapp.mvp.module.MediaModule
import com.wx.app.wxapp.mvp.presenter.MediaPresenter
import com.wx.app.wxapp.ui.adapter.HomeFragmentPagerAdapter
import com.wx.app.wxapp.ui.fragment.base.BaseContentFragment
import com.wx.app.wxapp.utils.DensityUtils
import com.wx.app.wxapp.utils.ScreenUtils
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat


/**

 * 描述：

 * @author wx

 * @date 2018/7/20/020

 */
class HomeFragment : BaseContentFragment<MediaContract.MediaView, MediaContract.MediaModule, MediaPresenter>(), MediaContract.MediaView {
    val TAG: String = "HomeFragment"

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
    }


//    private var mCollapsingTextHelper: MyCollapsingTextHelper? = null

    private fun initToolBar() {
        val statusBarHeight = ScreenUtils.getStatusBarHeight(context)
        cl_ctb.setPadding(0, statusBarHeight, 0, 0)
        tb_toolbar.setPadding(0, statusBarHeight, 0, 0)
        val lp = layout_search.layoutParams as ViewGroup.MarginLayoutParams
        val marginValues = DensityUtils.dp2px(context,15f)
        var currentValues : Int

// 创建一个数值格式化对象
        val numberFormat = NumberFormat.getInstance()
// 设置精确到小数点后2位
        numberFormat.maximumFractionDigits = 2
        ab_home.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = ab_home.totalScrollRange.toFloat()
            val offset = Math.abs(verticalOffset).toFloat()
            val percent = 1 - Math.max(offset / totalScrollRange, 0f)
            currentValues = (percent * marginValues).toInt()
            Log.e(TAG,"---$currentValues")
            if (currentValues > 30 && lp.leftMargin != currentValues) {
                lp.leftMargin = currentValues
                lp.rightMargin = currentValues
                layout_search.layoutParams = lp
            }
        }
    }

    private fun initTab() {
        var homeListFragment: ArrayList<HomeListFragment> = ArrayList()
        for (i in array_media_online.indices) {
            val newTab = tl_scroll.newTab()
            newTab.text = array_media_online[i]
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