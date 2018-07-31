package com.wx.app.wxapp.net.api

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/27/027

 */
object ApiConfig {

    const val BASE_RAW_API_URL = "http://www.tucao.tv/"
    const val BASE_JSON_API_URL = "http://www.tucao.tv/api_v2/"
    const val BASE_XML_API_URL = "http://www.tucao.tv/"


    /*
 * XML
 */
    const val PLAY_URL_API_URL = "http://api.tucao.tv/api/playurl"
    const val DANMU_API_URL = BASE_XML_API_URL+"index.php?m=mukio&c=index&a=init"
}