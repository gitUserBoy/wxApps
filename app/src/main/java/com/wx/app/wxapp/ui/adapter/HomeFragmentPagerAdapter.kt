package com.wx.app.wxapp.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wx.app.wxapp.ui.fragment.HomeListFragment
import com.wx.app.wxapp.ui.fragment.base.BaseFragment



/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/7/007

 */
class HomeFragmentPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    private var fragments: ArrayList<BaseFragment> = ArrayList()

    fun addData(newData: ArrayList<HomeListFragment>) {
        if (fragments.size > 0) {
            fragments.clear()
        }
        fragments.addAll(newData)
        notifyDataSetChanged()
    }
}