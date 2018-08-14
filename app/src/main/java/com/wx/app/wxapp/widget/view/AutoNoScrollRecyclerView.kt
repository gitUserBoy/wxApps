package com.wx.app.wxapp.widget.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import com.wx.app.wxapp.widget.recycler_pager.PagerGridLayoutManager


/**

 * 描述：

 *  父ViewGroup 如果是viewpager，横向滑动事件分发处理

 * @author wx

 * @date 2018/8/2/002

 */
class AutoNoScrollRecyclerView : RecyclerView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
    }
    constructor(context: Context) : super(context) {
    }
    private var mPager: ViewPager? = null
    private var abc = 1
    private var mLastMotionX: Float = 0.toFloat()
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (layoutManager is PagerGridLayoutManager){
            val  layoutManagers = layoutManager as PagerGridLayoutManager
            if (mPager != null) {
                val x = ev.x
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mPager!!.requestDisallowInterceptTouchEvent(true)
                        abc = 1
                        mLastMotionX = x
                    }
                    MotionEvent.ACTION_MOVE -> if (abc == 1) {
                        if (x - mLastMotionX > 5 && layoutManagers.currentIndex=== 0) {
                            abc = 0
                            mPager!!.requestDisallowInterceptTouchEvent(false)
                        }


                        if (x - mLastMotionX < -5 && layoutManagers.currentIndex === layoutManagers.count - 1) {
                            abc = 0
                            mPager!!.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mPager!!.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        return super.onTouchEvent(event)
    }

    fun setParentPager(viewPager: ViewPager) {
        this.mPager = viewPager
    }

}

