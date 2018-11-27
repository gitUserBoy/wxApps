package com.wx.app.wxapp.net

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.wx.app.wxapp.MyApplication
import com.wx.app.wxapp.constant.UriConstant
import com.wx.app.wxapp.net.api.ApiService
import com.wx.app.wxapp.net.support.InterceptorManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
object RetrofitManager {
    const val KY_TYPE: Int = 20181
    const val BOOK_TYPE: Int = 20182

    private var kyRetrofit: Retrofit? = null
    private var bookRetrofit: Retrofit? = null

    private lateinit var apiService: ApiService

    /**
     * 设定retrofit类型，避免多个类型数据查询混乱
     */
    fun apiServiceInstance(type: Int): ApiService {
        when (type) {
            KY_TYPE -> {
                apiService = getKaiYanRetrofit()!!.create(ApiService::class.java)
            }
            BOOK_TYPE -> {
                apiService = getBookRetrofit()!!.create(ApiService::class.java)
            }
        }
        return apiService
    }


    private fun getKaiYanRetrofit(): Retrofit? {
        synchronized(RetrofitManager::class.java) {
            if (kyRetrofit == null) {
                kyRetrofit = Retrofit.Builder()
                        .baseUrl(UriConstant.BASE_URL_KY)  //自己配置
                        .client(kaiYanClient())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return kyRetrofit
        }
    }

    private fun getBookRetrofit(): Retrofit? {
        synchronized(RetrofitManager::class.java) {
            if (bookRetrofit == null) {
                return Retrofit.Builder()
                        .baseUrl(UriConstant.BASE_URL_BOOK)  //自己配置
                        .client(kaiYanClient())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
        }
        return bookRetrofit
    }

    /**
     * 开眼网络client
     */
    private fun kaiYanClient(): OkHttpClient {
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(MyApplication.context.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

        return OkHttpClient.Builder()
                .addInterceptor(InterceptorManager.addQueryParameterInterceptor())  //参数添加
                .addInterceptor(InterceptorManager.addHeaderInterceptor()) // token过滤
                .addInterceptor(InterceptorManager.addCacheInterceptor())
                .addInterceptor(InterceptorManager.addLoggingInterceptor()) //日志,所有的请求响应度看到
                .cache(cache)  //添加缓存
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }

    /**
     * 追书网络client
     */
    private fun BookClient(): OkHttpClient {
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(MyApplication.context.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

        return OkHttpClient.Builder()
                .addInterceptor(InterceptorManager.addQueryParameterInterceptor())  //参数添加
                .addInterceptor(InterceptorManager.addHeaderInterceptor()) // token过滤
                .addInterceptor(InterceptorManager.addCacheInterceptor())
                .addInterceptor(InterceptorManager.addLoggingInterceptor()) //日志,所有的请求响应度看到
                .cache(cache)  //添加缓存
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }

}