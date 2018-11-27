package com.wx.app.wxapp.mvp.constract

import com.wx.app.wxapp.bean.ky.MediaBean
import com.wx.app.wxapp.mvp.constract.base.BaseContract
import io.reactivex.disposables.Disposable

/**
 * 作者: wuxu
 * 日期: 2018/10/24/024.

 * 说明:
 */
interface MediaListContract {
    interface MediaListPresenter<in V : BaseContract.BaseView, in M : BaseContract.BaseModule> : BaseContract.BasePresenter<V, M> {
        fun loadData(num:Int)

        fun loadNextData(url:String)
    }

    interface MediaListView : BaseContract.BaseListView {
        fun loadListData(t: MediaBean)

        fun showList()

        fun showNextLoadDataError(esg: String, errorType: Int)

        fun loadNextSuccess(t: MediaBean)
    }

    interface MediaListModule : BaseContract.BaseModule {

        fun loadData(num:Int,listener:MediaListener): Disposable

        fun loadNextData(url:String,listener:MediaListener):Disposable



        interface MediaListener{
            fun loadDataSuccess(t: MediaBean)

            fun loadNextDataSuccess(t: MediaBean)

            fun loadDataError(esg:String,errorType:Int)

            fun loadNextDataError(esg:String,errorType:Int)
        }
    }
}