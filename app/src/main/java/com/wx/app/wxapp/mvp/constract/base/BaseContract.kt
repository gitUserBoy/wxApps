package com.wx.app.wxapp.mvp.constract.base


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
interface BaseContract {

    interface BasePresenter<in T, in M>{
        fun attachView(view: T,module: M)

        fun detachView()
    }

    interface BaseView {
        fun showLoading()

        fun complete()

        fun showError(msg: String, errorCode: Int)

    }

    interface BaseListView : BaseView{

        fun loadMoreLoading()

        fun loadMoreComplete()
    }

    interface BaseModule{}
}