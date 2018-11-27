package com.wx.app.wxapp.widget.view

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.wx.app.wxapp.R

/**
 * @author: wu.xu
 * @data: 2018/11/27/027.

 * 毫无BUG
 */
class ListLoadMoreView : LoadMoreView() {
    override fun getLayoutId(): Int {
        return R.layout.loading_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.loading_progress
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }
}