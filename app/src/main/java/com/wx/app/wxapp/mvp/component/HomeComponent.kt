package com.wx.app.wxapp.mvp.component

import com.wx.app.wxapp.ui.fragment.HomeFragment

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/6/006

 */
//@Component(modules= [HomeModuleImpl::class])
interface HomeComponent{
    fun inject(home:HomeFragment)
}