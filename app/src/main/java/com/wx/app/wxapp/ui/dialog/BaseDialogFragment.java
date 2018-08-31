package com.wx.app.wxapp.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.wx.app.wxapp.R;
import com.wx.app.wxapp.constant.Constant;



public class BaseDialogFragment extends DialogFragment {
  protected String title;
  protected String message;
  protected String positive;
  protected String negative;
  protected String qrcode;
  protected boolean cancelable;
  protected View dialogView;

  protected OnDialogFragmentListener listener;
  protected OnDismissListener onDismissListener;

  public void setOnDialogFragmentListener(OnDialogFragmentListener listener) {
    this.listener = listener;
  }
  public void setOnDismissListener(OnDismissListener onDismissListener) {
    this.onDismissListener = onDismissListener;
  }
  public interface OnDialogFragmentListener {
    void dialogPositiveListener(View rootView);

    void dialogNegativeListener(View rootView);
  }
  public interface OnDismissListener {
    void onDismiss(DialogInterface dialog);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() == null) {
      return;
    }
    //解析
    parseArgs(getArguments());
    //设置是否可以点击消失
    setCancelable(cancelable);
  }

  /**
   * 从bundle中解析参数，args有可能来自fragment被系统回收，然后界面又重新被启动系统保存的参数；也有可能是其他使用者带过来的
   * ，具体实现交给子类
   */
  protected void parseArgs(Bundle args) {
    title = args.getString(Constant.DIALOG_TITLE,getString(R.string.title));
    message = args.getString(Constant.DIALOG_MESSAGE,getString(R.string.message));
    positive = args.getString(Constant.DIALOG_POSITIVE,getString(R.string.positive));
    negative = args.getString(Constant.DIALOG_NEGATIVE,getString(R.string.negative));
    cancelable = args.getBoolean(Constant.DIALOG_CANCEL_ABLE,true);
    qrcode = args.getString(Constant.DIALOG_QR_CODE,"");
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if(onDismissListener!=null){
      onDismissListener.onDismiss(dialog);
    }
  }

  @Override
  public void show(FragmentManager manager, String tag) {
    try {
      //在每个add事务前增加一个remove事务，防止连续的add
      manager.beginTransaction().remove(this).commit();
      super.show(manager, tag);
    } catch (Exception e) {
      //同一实例使用不同的tag会异常,这里捕获一下
      e.printStackTrace();
    }
  }
}
