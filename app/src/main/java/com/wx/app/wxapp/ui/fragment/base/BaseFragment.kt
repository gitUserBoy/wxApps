package com.wx.app.wxapp.ui.fragment.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wx.app.wxapp.widget.view.MultipleStatusView


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/20/020

 */
abstract class BaseFragment : Fragment() {
    lateinit var view: ViewGroup
    lateinit var multipleStatusView: MultipleStatusView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(layoutId(), container, false) as ViewGroup
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        multipleStatusView = statusViewId()
        initData()
        initView()
        initEvent()
    }

    abstract fun layoutId(): Int
    abstract fun statusViewId(): MultipleStatusView
    abstract fun initData()
    abstract fun initView()
    abstract fun initEvent()
}