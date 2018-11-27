package com.wx.app.wxapp.mvp.constract

import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.base.BaseContract
import io.reactivex.disposables.Disposable

/**
 * 作者: wuxu
 * 日期: 2018/10/24/024.

 * 说明:
 */
interface MediaContract {
    interface MediaPresenter<in V : BaseContract.BaseView, in M : BaseContract.BaseModule> : BaseContract.BasePresenter<V, M> {
        fun loadData(num:Int)
    }

    interface MediaView : BaseContract.BaseView {
        fun showListData()
    }

    interface MediaModule : BaseContract.BaseModule {
        interface MediaListener{
            fun showBanner(itemList:ArrayList<MediaBean.Issue.Item>)

            fun loadDataError(esg:String)
        }

        fun loadData(num:Int,listener:MediaListener): Disposable
    }
}