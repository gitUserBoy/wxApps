package com.wx.app.wxapp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wx.app.wxapp.R;
import com.wx.app.wxapp.bean.WifiBean;
import com.wx.app.wxapp.manager.wifi.AppContants;
import com.wx.app.wxapp.manager.wifi.WifiStateManager;
import com.wx.app.wxapp.manager.wifi.WifiSupport;
import com.wx.app.wxapp.ui.adapter.WifiListAdapter;
import com.wx.app.wxapp.ui.dialog.WifiLinkDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author wx
 * @date 2018/8/9/009
 */
public class WifiActivity extends Activity {
  RecyclerView recyclerView;
  List<WifiBean> wifiList = new ArrayList<>();
  WifiStateManager instance;
  WifiListAdapter wifiListAdapter;
  RelativeLayout loadView;
  //权限请求码
  private static final int PERMISSION_REQUEST_CODE = 0;
  //两个危险权限需要动态申请
  private static final String[] NEEDED_PERMISSIONS = new String[]{
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION
  };

  private boolean mHasPermission;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wifi);
    initView();
    if (checkPermission()){
      initRecycler();
    }else {
      requestPermission();
    }
  }

  /**
   * 检查是否已经授予权限
   *
   * @return
   */
  private boolean checkPermission() {
    for (String permission : NEEDED_PERMISSIONS) {
      if (ActivityCompat.checkSelfPermission(this, permission)
              != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  /**
   * 申请权限
   */
  private void requestPermission() {
    ActivityCompat.requestPermissions(this,
            NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    boolean hasAllPermission = true;
    if (requestCode == PERMISSION_REQUEST_CODE) {
      for (int i : grantResults) {
        if (i != PackageManager.PERMISSION_GRANTED) {
          hasAllPermission = false;   //判断用户是否同意获取权限
          break;
        }
      }

      //如果同意权限
      if (hasAllPermission) {
        mHasPermission = true;
        if (WifiSupport.isOpenWifi(this) && mHasPermission) {  //如果wifi开关是开 并且 已经获取权限
          initRecycler();
        } else {
          Toast.makeText(this, "WIFI处于关闭状态或权限获取失败1111", Toast.LENGTH_SHORT).show();
        }

      } else {  //用户不同意权限
        mHasPermission = false;
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void initRecycler() {
    instance = WifiStateManager.getInstance(this);
    wifiListAdapter = new WifiListAdapter(this, wifiList);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(wifiListAdapter);
    instance.setWifiListeners(new WifiStateManager.WifiListener() {
      @Override
      public void hidingProgressBar() {
        loadView.setVisibility(View.GONE);
      }

      @Override
      public void showProgressBar() {
        loadView.setVisibility(View.VISIBLE);
      }

      @Override
      public void updateList(List<WifiBean> wifiBeanArrayList) {
        wifiList.clear();
        wifiList.addAll(wifiBeanArrayList);
        wifiListAdapter.setData(wifiBeanArrayList);
      }
    });


    wifiListAdapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
      @Override
      public void onItemClick(View view, int postion, Object o) {
        WifiBean wifiBean = wifiList.get(postion);
        if (wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)) {
          String capabilities = wifiList.get(postion).getCapabilities();
          if (WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {//无需密码
            WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
            if (tempConfig == null) {
              WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
              WifiSupport.addNetWork(exsits,WifiActivity.this);
            } else {
              WifiSupport.addNetWork(tempConfig, WifiActivity.this);
            }
          } else {   //需要密码，弹出输入密码dialog
            WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
            if (tempConfig == null) {
              noConfigurationWifi(postion);
            } else {
              WifiSupport.addNetWork(tempConfig, WifiActivity.this);
            }
          }
        }
      }
    });
  }
  private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
    WifiLinkDialog linkDialog = new WifiLinkDialog(this, R.style.dialog_download, wifiList.get(position).getWifiName(), wifiList.get(position).getCapabilities());
    if (!linkDialog.isShowing()) {
      linkDialog.show();
    }
  }
  private void initView() {
    recyclerView = findViewById(R.id.rv_list);
    loadView = findViewById(R.id.rl_loading);
    loadView.setVisibility(View.VISIBLE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    instance.createWifiBroadcastReceiver();
  }

  @Override
  protected void onPause() {
    super.onPause();
    instance.unregisterReceiver();
  }
}
