package com.wx.app.wxapp.widget.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent





/**

 * 描述：

 *  NoScrollViewPager.setNoScroll(true)

 * @author wx

 * @date 2018/8/2/002

 */
class NoScrollViewPager : ViewPager {
    private var noScroll = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context) : super(context) {}

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        /* return false;//super.onTouchEvent(arg0); */
        when (arg0.action) {

        }
        return !noScroll && super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return !noScroll && super.onInterceptTouchEvent(arg0)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        //false 去除滚动效果
        super.setCurrentItem(item, false)
    }

}

