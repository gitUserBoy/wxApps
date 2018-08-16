package com.wx.app.wxapp.mvp.constract


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
interface BaseContract {

    interface BasePresenter {
        fun attachView()

        fun detachView()
    }

    interface BaseView {
        fun showLoading()

        fun dismissLoading()

        fun loadMore()

        fun loadData()

        fun loadFinish()

        fun loadMoreFinish()

        fun showError(msg:String,errorCode:Int)
    }
}