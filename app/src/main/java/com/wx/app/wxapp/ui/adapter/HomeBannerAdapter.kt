package com.wx.app.wxapp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wx.app.wxapp.bean.HomeBean
import com.wx.app.wxapp.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.item_home_banner.view.*

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/31/031

 */
class HomeBannerAdapter(layoutId: Int, data: ArrayList<HomeBean.Issue.Item>?) : BaseQuickAdapter<HomeBean.Issue.Item, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: HomeBean.Issue.Item) {
//        var param = helper.itemView.cv_card.layoutParams as LinearLayout.LayoutParams // getStatusBarHeight(mContext)
//        param.topMargin= getStatusBarHeight(mContext)
//        helper.itemView.cv_card.layoutParams = param
        ImageLoaderUtil.LoadImage(mContext,item.data!!.cover.detail,helper.itemView.iv_banner)
    }
}