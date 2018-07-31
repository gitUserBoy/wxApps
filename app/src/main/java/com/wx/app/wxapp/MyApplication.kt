package com.wx.app.wxapp

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
class MyApplication : Application() {
    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}