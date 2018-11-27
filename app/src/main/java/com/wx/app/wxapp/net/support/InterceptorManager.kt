package com.wx.app.wxapp.net.support

import com.wx.app.wxapp.MyApplication
import com.wx.app.wxapp.utils.NetworkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 作者: wuxu
 * 日期: 2018/10/23/023.

 * 说明:
 */
object InterceptorManager {
    private var token: String = ""

    fun addLoggingInterceptor() : HttpLoggingInterceptor{
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

     fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val requestBuilder = request.newBuilder().header("token", token)
                    .method(request.method(), request.body())

            chain.proceed(requestBuilder.build())
        }
    }

     fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val requestUrl = request.url().newBuilder()
                    .addQueryParameter("phoneSystem", "")
                    .addQueryParameter("phoneModel", "")
                    .build()

            chain.proceed(request.newBuilder().url(requestUrl).build())


        }
    }

    /**
     * 设置缓存
     */
     fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("nyn")
                        .build()
            }
            response
        }
    }


}