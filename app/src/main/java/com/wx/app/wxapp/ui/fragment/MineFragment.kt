package com.wx.app.wxapp.ui.fragment

import com.wx.app.wxapp.R.layout.fragment_mine
import com.wx.app.wxapp.ui.fragment.base.BaseFragment
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/20/020

 */
class MineFragment : BaseFragment() {
    override fun statusViewId(): MultipleStatusView = vw_multiple

    override fun layoutId(): Int = fragment_mine

    override fun initData() {
    }

    override fun initView() {
    }
}