package com.wx.app.wxapp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wx.app.wxapp.bean.HomeContentBean
import kotlinx.android.synthetic.main.item_home_banner.view.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
class HomeListAdapter(layoutId: Int, data: MutableList<HomeContentBean>?) : BaseQuickAdapter<HomeContentBean, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: HomeContentBean) {
        helper.itemView.tv_banner_title.text = item.name
    }
}