package com.wx.app.wxapp.mvp.presenter.`interface`

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/8/008

 */
interface HomeListPresenter : BasePresenter {
    fun loadData(num:Int)

    fun loadMore(url:String)
}