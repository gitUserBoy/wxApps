package com.wx.app.wxapp.ui.activity

import com.wx.app.wxapp.R
import com.wx.app.wxapp.ui.activity.base.BaseActivity
import com.wx.app.wxapp.utils.ImageLoaderUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

/**

 * 描述：

 *

 * @author wx

 * @date 2018/7/18/018

 */
class SplashActivity : BaseActivity() {
    //必应每日壁纸 来源于 https://www.dujin.org/fenxiang/jiaocheng/3618.html.
    private val picUrl = "http://api.dujin.org/bing/1920.php"

    var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    override fun initView() {

        ImageLoaderUtil.LoadImage(this,picUrl,iv_bg)

        mCompositeDisposable?.add(getObservable(3).doOnSubscribe({tv_time.text=""}).subscribeWith(object : DisposableObserver<Int>() {
            override fun onComplete() {
                activity2main()
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(t: Int) {
                tv_time.text = "${t+1} s"
            }
        }))

        tv_time.setOnClickListener { toActivity(this,MainActivity::class.java,true) }
    }
    override fun initDate() {
    }
    override fun layoutId(): Int = R.layout.activity_splash

    fun getObservable(time: Int): Observable<Int> {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map{t->time - t.toInt()}//t 指的是秒数
                .take((time+1).toLong())//执行次数  4次  0 1 2 3
    }


    fun activity2main() {
        mCompositeDisposable?.dispose()
        toActivity(this,MainActivity::class.java,true)
    }

    override fun onDestroy() {
        mCompositeDisposable?.dispose()
        super.onDestroy()
    }

    override fun setStatusBar() {
    }
}