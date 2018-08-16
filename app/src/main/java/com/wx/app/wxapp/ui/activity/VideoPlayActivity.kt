package com.wx.app.wxapp.ui.activity

import android.os.Bundle
import android.view.View
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.wx.app.wxapp.R
import com.wx.app.wxapp.ui.activity.base.BaseActivity


/**

 * 描述：

 *

 * @author wx

 * @date 2018/8/6/006

 */
class VideoPlayActivity : BaseActivity() {

    lateinit var detailPlayer: StandardGSYVideoPlayer

    private var isPlay: Boolean = false
    private var isPause: Boolean = false

    private var orientationUtils: OrientationUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailPlayer = findViewById<View>(R.id.detail_player) as StandardGSYVideoPlayer

        val url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192"

        //增加封面
//        val imageView = ImageView(this)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.setImageResource(R.mipmap.xxx1)

        //增加title
        detailPlayer.titleTextView.visibility = View.GONE
        detailPlayer.backButton.visibility = View.GONE

        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detailPlayer)
        //初始化不打开外部的旋转
        orientationUtils!!.isEnable = false

        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption
//                .setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle("测试视频")
                .setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String?, vararg objects: Any) {
                        super.onPrepared(url, *objects)
                        //开始播放了才能旋转和全屏
                        orientationUtils!!.isEnable = true
                        isPlay = true
                    }

                    override fun onQuitFullscreen(url: String?, vararg objects: Any) {
                        super.onQuitFullscreen(url, *objects)
                        if (orientationUtils != null) {
                            orientationUtils!!.backToProtVideo()
                        }
                    }
                }).setLockClickListener { view, lock ->
                    if (orientationUtils != null) {
                        //配合下方的onConfigurationChanged
                        orientationUtils!!.isEnable = !lock
                    }
                }.build(detailPlayer)

        detailPlayer.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils!!.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            detailPlayer.startWindowFullscreen(this@VideoPlayActivity, true, true)
        }
    }

    override fun onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }


    override fun onPause() {
        detailPlayer.currentPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        detailPlayer.currentPlayer.onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            detailPlayer.currentPlayer.release()
        }
        if (orientationUtils != null)
            orientationUtils!!.releaseListener()
    }


//    fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
//        }
//    }

    override fun initView() {
    }

    override fun initDate() {
    }

    override fun layoutId(): Int = R.layout.activity_video_detail
}