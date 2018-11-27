package com.wx.app.wxapp.constant

/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/15/015

 */
object Constant {
    /*开眼相关常量设置*/
    const val HOME_TYPE_BANNER2: String = "banner2"
    const val HOME_TYPE_NORMAL: String = "NORMAL"
    const val HOME_TYPE_VIDEO: String = "video"

    /*error_code网络错误异常*/
    const val ERROR_LOADMORE_FAILD:Int= 0x00  //加载更多失败

    /**
     * 对话框标题
     */
    const val DIALOG_TITLE = "dialog_title"
    /**
     * 对话框信息
     */
    const  val DIALOG_MESSAGE = "dialog_message"
    /**
     * 对话框能否点击外部消失
     */
    const val DIALOG_CANCEL_ABLE = "dialog_cancel_able"
    /**
     * 对话框确认按钮名字
     */
    const val DIALOG_POSITIVE = "dialog_positive"
    /**
     * 对话框取消按钮名字
     */
    const val DIALOG_NEGATIVE = "dialog_negative"
    /**
     * 对话框二维码地址
     */
    const val DIALOG_QR_CODE = "dialog_qr_code"

    /**
     *视频类型数据：online（线上）、live（直播）、local（本地视频）
     */
    val array_media_online : Array<String> = arrayOf("游戏","搞笑","动漫","电视剧","电影")
    val array_media_live : Array<String> = arrayOf("游戏","影视","娱乐","其他")
    val array_media_local : Array<String> = arrayOf("本地视频")
}
