package com.wx.app.wxapp.mvp.view

import com.wx.app.wxapp.bean.HomeBean


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
interface HomeListView : BaseView{
    fun showBanner(list: ArrayList<HomeBean.Issue.Item>)
}