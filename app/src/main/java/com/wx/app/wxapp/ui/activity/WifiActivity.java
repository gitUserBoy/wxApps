package com.wx.app.wxapp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.wx.app.wxapp.ui.dialog.GuideDialog;
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
  private int GPS_REQUEST_CODE = 1022;
  private WifiManager wifiManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wifi);
    initView();
    wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    if (!wifiManager.isWifiEnabled()) {
      wifiManager.setWifiEnabled(true);
    }
    mHasPermission = checkPermission();
    if (!mHasPermission && WifiSupport.isOpenWifi(this)) {  //未获取权限，申请权限
      requestPermission();
    } else if (mHasPermission && WifiSupport.isOpenWifi(this)) {  //已经获取权限
      initRecycler();
    } else {
      Toast.makeText(this, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
    }
    if (Build.VERSION.SDK_INT >= 23 && !isGpsOpen()) {
      alertOpenGps();
    } else {
      mHasPermission = checkPermission();
      if (!mHasPermission && WifiSupport.isOpenWifi(this)) {  //未获取权限，申请权限
        requestPermission();
      } else if (mHasPermission && WifiSupport.isOpenWifi(this)) {  //已经获取权限
        initRecycler();
      } else {
        Toast.makeText(this, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void alertOpenGps() {
    new AlertDialog.Builder(this).setMessage("请打开手机GPS").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsIntent, GPS_REQUEST_CODE);
      }
    }).show();
  }

  private boolean isGpsOpen() {
    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
    instance =new WifiStateManager(this);
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

        setData(wifiBeanArrayList);

        wifiList.clear();
        wifiList.addAll(wifiBeanArrayList);
        wifiListAdapter.setData(wifiBeanArrayList);
      }

      @Override
      public void action(Context context, Intent intent) {
      }
    });


    wifiListAdapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
      @Override
      public void onItemClick(View view, int position, Object o) {
        WifiBean wifiBean = wifiList.get(position);
        if (wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)) {
          String capabilities = wifiList.get(position).getCapabilities();
          if (WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {//无需密码
            WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
            if (tempConfig == null) {
              WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
              WifiSupport.addNetWork(exsits, WifiActivity.this);
            } else {
              WifiSupport.addNetWork(tempConfig, WifiActivity.this);
            }
          } else {   //需要密码，弹出输入密码dialog
            WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
            if (tempConfig == null) {
              noConfigurationWifi(position);
            } else {
              WifiSupport.addNetWork(tempConfig, WifiActivity.this);
            }
          }
        }
      }
    });
    instance.createWifiBroadcastReceiver();
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
  protected void onDestroy() {
    super.onDestroy();
    if (instance != null) {
      instance.unregisterReceiver();
    }
  }

  RecyclerView recyclerViewWifi;
  WifiListAdapter wifiListAdapterWifi;
  boolean isFirsShowDialog = false;

  private void setData(List<WifiBean> wifiBeanArrayList) {
    List<WifiBean> list = new ArrayList();
    for (WifiBean bean : wifiBeanArrayList) {
      if (bean.getWifiName().contains("Cisco")) {
        list.add(bean);
      }
    }

    if (!isFirsShowDialog) {
      isFirsShowDialog = true;
      View view = View.inflate(this, R.layout.dialog_wifi, null);
      recyclerViewWifi = view.findViewById(R.id.recycler_wifi);
      wifiListAdapterWifi = new WifiListAdapter(this, list);
      recyclerViewWifi.setLayoutManager(new LinearLayoutManager(this));
      recyclerViewWifi.setAdapter(wifiListAdapterWifi);
      GuideDialog.newInstance().setActivity(this).setContentView(view);
    } else {
      if (null != wifiListAdapterWifi)
        wifiListAdapterWifi.setData(list);
    }
  }
}
