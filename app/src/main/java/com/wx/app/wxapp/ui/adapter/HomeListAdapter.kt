package com.wx.app.wxapp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wx.app.wxapp.bean.ky.MediaBean

/**
 * @author: wu.xu
 * @data: 2018/11/1/001.

 * e-mail:W_spongeBob@163.com
 */
class HomeListAdapter(layoutResId: Int, data: ArrayList<MediaBean.Issue.Item>)
    : BaseQuickAdapter<MediaBean.Issue.Item, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: MediaBean.Issue.Item) {

    }
}