package com.wx.app.wxapp

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import kotlin.properties.Delegates



/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
class MyApplication : MultiDexApplication() {
    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}