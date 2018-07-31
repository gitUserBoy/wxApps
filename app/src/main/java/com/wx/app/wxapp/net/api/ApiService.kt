package com.wx.app.wxapp.net.api

import retrofit2.http.GET
import retrofit2.http.Path

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
interface ApiService {

    @GET("/ss/")
    fun getId(@Path("id") id:Int)

}