package com.wx.app.wxapp.ui.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.util.Log
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.wx.app.wxapp.R
import com.wx.app.wxapp.ui.activity.base.BaseActivity
import com.wx.app.wxapp.ui.fragment.HomeFragment
import com.wx.app.wxapp.ui.fragment.MineFragment
import com.wx.app.wxapp.ui.fragment.RecommendFragment
import com.wx.app.wxapp.ui.fragment.RecreationFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var currIndex: Int = 0

    private val mainBottomName: Array<String> = arrayOf("首页", "娱乐", "推荐", "我的")

    private val mainBottomIcon: IntArray = intArrayOf()

    override fun initView() {
        initBottomTab()

        initSlide()
    }

    override fun initDate() {
    }

    override fun layoutId(): Int = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            currIndex = savedInstanceState.getInt("currTabIndex")
        }
        switchFragment(currIndex)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt("curr_index", currIndex)
        super.onSaveInstanceState(outState)
    }

    private fun initBottomTab() {
        for (i in 0 until mainBottomName.size) {
            tab_button.addTab(tab_button.newTab().setText(mainBottomName[i]).setIcon(R.drawable.message_tip_bg))
        }
        tab_button.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                currIndex = tab.position
                Log.i("11", TAG)
                Log.i(TAG, " tab.position:" + tab.position)
                switchFragment(currIndex)
            }
        })
    }

    private fun switchFragment(position: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (position) {
            0 -> {
                fragmentTransaction.replace(R.id.fl_container, HomeFragment())
            }
            1 -> {
                fragmentTransaction.replace(R.id.fl_container, RecreationFragment())
            }
            2 -> {
                fragmentTransaction.replace(R.id.fl_container, RecommendFragment())
            }
            else -> {
                fragmentTransaction.replace(R.id.fl_container, MineFragment())
            }
        }
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun initSlide() {

    }

    override fun setStatusBar() {
        var mStatusBarColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setColorForDrawerLayout(this,  drawer_main, mStatusBarColor, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }
}
