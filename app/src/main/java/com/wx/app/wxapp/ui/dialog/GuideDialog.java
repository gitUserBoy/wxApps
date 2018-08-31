package com.wx.app.wxapp.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wx.app.wxapp.R;
import com.wx.app.wxapp.utils.ScreenUtils;


public class GuideDialog {
  private static GuideDialog netStateDialog = null;

  private static Activity activity = null;

  public GuideDialog() {
  }

  private static synchronized GuideDialog getNetStateDialog() {
    if (netStateDialog == null) {
      netStateDialog = new GuideDialog();
    }
    return netStateDialog;
  }

  public static GuideDialog newInstance() {
    return getNetStateDialog();
  }

  public GuideDialog setActivity(Activity activity) {
    this.activity = activity;
    return this;
  }

  /**
   * 第一次打开音频播放页引导
   */
  public GuideDialog setAudioGuide() {
    final Dialog dialog = new Dialog(activity, R.style.dialog_guide);
//    View view = View.inflate(activity, R.layout.view_guide_audio, null);
//    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT);
//    dialog.addContentView(view, layoutParams);
//    dialog.show();
//    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//    lp.width = (int) (ScreenUtils.getScreenWidth(activity)); // 设置宽度
//    lp.height = (int) (ScreenUtils.getScreenHeight(activity));
//    dialog.getWindow().
//            setAttributes(lp);
//
//    view.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View arg0) {
//        if (dialog.isShowing())
//          dialog.dismiss();
//      }
//    });
    return this;
  }

  /**
   * 第一次打开视频播放页引导
   * 暂定：横版是全屏，竖版是非全屏
   *
   * @param isFull 判断当前页是否全屏，暂未使用到
   */
  public GuideDialog setVideoGuide(Boolean isFull) {
    final Dialog dialog = new Dialog(activity, R.style.dialog_guide);
//    View view = View.inflate(activity, R.layout.view_guide_video, null);
//    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT);
//    dialog.addContentView(view, layoutParams);
//    dialog.show();
//    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//    lp.width = (int) (ScreenUtils.getScreenWidth(activity)); // 设置宽度
//    lp.height = (int) (ScreenUtils.getScreenHeight(activity));
//    dialog.getWindow().
//            setAttributes(lp);
//
//    view.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View arg0) {
//        if (dialog.isShowing())
//          dialog.dismiss();
//
//        if (dialogClickListener != null) {
//          dialogClickListener.onClick();
//        }
//      }
//    });
    return this;
  }

  public GuideDialog setContentView(View view) {
    final Dialog dialog = new Dialog(activity, R.style.dialog_guide);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    dialog.addContentView(view, layoutParams);
    dialog.show();
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.width = (int) (ScreenUtils.getScreenWidth(activity)*0.5f); // 设置宽度
    lp.height = (int) (ScreenUtils.getScreenWidth(activity)*0.5f);
    dialog.getWindow().
            setAttributes(lp);

//    view.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View arg0) {
//        if (dialog.isShowing())
//          dialog.dismiss();
//      }
//    });
    return this;
  }


  private DialogClickListener dialogClickListener = null;

  public interface DialogClickListener {
    void onClick();
  }

  public void setListener(DialogClickListener listener) {
    dialogClickListener = listener;
  }
}
