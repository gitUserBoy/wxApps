package com.wx.app.wxapp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wx.app.wxapp.bean.HomeBean

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
class HomeListAdapter(layoutId: Int, data: MutableList<HomeBean.Issue.Item>?) : BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
    }
}