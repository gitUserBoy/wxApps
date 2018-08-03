package com.wx.app.wxapp.mvp.view

import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.bean.HomeContentBean


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
interface HomeView : BaseView{

    fun showRvList(list: ArrayList<HomeContentBean>)

    fun showBanner(list: ArrayList<HomeBean.Issue.Item>)
}