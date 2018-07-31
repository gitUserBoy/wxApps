package com.wx.app.wxapp.ui.fragment

import android.os.Handler
import android.os.Message
import com.wx.app.wxapp.R.layout.fragment_home
import com.wx.app.wxapp.ui.fragment.base.BaseFragment
import com.wx.app.wxapp.widget.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_home.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/20/020

 */
class HomeFragment : BaseFragment() {


    val handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            multipleStatusView!!.showContent()
        }
    }
    override fun statusViewId(): MultipleStatusView  = vw_multiple

    override fun layoutId(): Int = fragment_home

    override fun initData() {
    }

    override fun initView() {
        multipleStatusView!!.showLoading()
        handler.sendEmptyMessageDelayed(0,3000)

    }


}