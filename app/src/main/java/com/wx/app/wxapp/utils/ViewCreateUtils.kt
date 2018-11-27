package com.wx.app.wxapp.utils

import android.content.Context
import android.view.View
import com.wx.app.wxapp.R

/**
 * @author: wu.xu
 * @data: 2018/11/26/026.

 * 毫无BUG
 *
 * 创造View所用工具类
 */

 fun loadMoreView(context: Context): View? =
        View.inflate(context, R.layout.loading_more, null)


fun loadMoreErrorView(context: Context, errorMsg: String, errorCode: Int): View {
    val inflate = View.inflate(context, R.layout.loading_more_error, null)
    when(errorCode){
    }
    return inflate
}
