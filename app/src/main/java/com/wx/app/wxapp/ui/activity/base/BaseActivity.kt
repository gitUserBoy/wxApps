package com.wx.app.wxapp.ui.activity.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.wx.app.wxapp.R
import com.wx.app.wxapp.ui.activity.MainActivity


/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
abstract class BaseActivity : AppCompatActivity() {
    val TAG: String = javaClass.simpleName + " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(layoutId())
        setStatusBar()

        initView()
        initDate()
    }


    open fun setStatusBar() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimary))
    }

    public fun replaceFragment(viewId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(viewId, fragment)
    }

    fun toActivity(context: Context, clazz: Class<MainActivity>, finish: Boolean) {
        val intent = Intent(context, clazz)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }

    abstract fun initView()

    abstract fun initDate()

    abstract fun layoutId(): Int
}