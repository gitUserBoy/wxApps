package com.wx.app.wxapp.manager.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.wx.app.wxapp.bean.WifiBean;
import com.wx.app.wxapp.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述：
 *
 * @author wx
 * @date 2018/8/9/009
 */
public class WifiStateManager {
  private static WifiStateManager wifiStateManager = null;

  private static final String TAG = "WIFI";

  private WifiBroadcastReceiver wifiReceiver;

  private static Context context;

  List<WifiBean> realWifiList = new ArrayList<>();
  private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）
  private int GPS_REQUEST_CODE = 1022;

  public static WifiStateManager getInstance(Context contexts) {
    if (wifiStateManager == null) {
      wifiStateManager = new WifiStateManager(contexts);
    }
    return wifiStateManager;
  }

  private WifiStateManager(Context contexts) {
    this.context = contexts;
  }


  public void createWifiBroadcastReceiver() {
//    if (wifiReceiver == null){
    //注册广播
    if (CollectionUtils.isNullOrEmpty(realWifiList)) {
      sortScaResult();
    }

    wifiReceiver = new WifiBroadcastReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
    filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
    this.context.registerReceiver(wifiReceiver, filter);
//    }else {
//      IntentFilter filter = new IntentFilter();
//      filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
//      filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
//      filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
//      context.registerReceiver(wifiReceiver, filter);
//    }
  }

  public void unregisterReceiver() {
    if (wifiReceiver != null && this.context != null) {
      context.unregisterReceiver(wifiReceiver);
    }
  }

  //监听wifi状态
  private class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      switch (intent.getAction()) {
        /**
         * SUPPLICANT_STATE_CHANGED_ACTION    wifi 密码错误
         * WIFI_STATE_CHANGED_ACTION          wifi状态改变
         * NETWORK_STATE_CHANGED_ACTION       网络状态改变
         * SCAN_RESULTS_AVAILABLE_ACTION      wifi列表改变
         * WIFI_STATE_UNKNOWN                 未知
         */

        case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION: {
          int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
          if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
          }
          break;
        }
        case WifiManager.WIFI_STATE_CHANGED_ACTION: {
          int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
          switch (state) {
            /**
             * WIFI_STATE_DISABLED    WLAN已经关闭
             * WIFI_STATE_DISABLING   WLAN正在关闭
             * WIFI_STATE_ENABLED     WLAN已经打开
             * WIFI_STATE_ENABLING    WLAN正在打开
             * WIFI_STATE_UNKNOWN     未知
             */
            case WifiManager.WIFI_STATE_DISABLED: {
              Log.d(TAG, "已经关闭");
              Toast.makeText(context, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
              break;
            }
            case WifiManager.WIFI_STATE_DISABLING: {
              Log.d(TAG, "正在关闭");
              break;
            }
            case WifiManager.WIFI_STATE_ENABLED: {
              Log.d(TAG, "已经打开");
              sortScaResult();
              break;
            }
            case WifiManager.WIFI_STATE_ENABLING: {
              Log.d(TAG, "正在打开");
              break;
            }
            case WifiManager.WIFI_STATE_UNKNOWN: {
              Log.d(TAG, "未知状态");
              break;
            }
          }
          break;
        }
        case WifiManager.NETWORK_STATE_CHANGED_ACTION: {
          NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
          Log.d(TAG, "--NetworkInfo--" + info.toString());
          if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
            Log.d(TAG, "wifi没连接上");
            for (int i = 0; i < realWifiList.size(); i++) {//没连接上将 所有的连接状态都置为“未连接”
              realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
            }
            if (wifiListeners != null) {
              wifiListeners.hidingProgressBar();
              wifiListeners.updateList(realWifiList);
            }
          } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
            Log.d(TAG, "wifi连接上了");
            WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(context);

            //连接成功 跳转界面 传递ip地址
            Toast.makeText(context, "wifi连接上了", Toast.LENGTH_SHORT).show();
            connectType = 1;
            wifiListSet(connectedWifiInfo.getSSID(), connectType);
            if (wifiListeners != null) {
              wifiListeners.hidingProgressBar();
              wifiListeners.updateList(realWifiList);
            }
          } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
            Log.d(TAG, "wifi正在连接");

            WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(context);
            connectType = 2;
            wifiListSet(connectedWifiInfo.getSSID(), connectType);
            if (wifiListeners != null) {
              wifiListeners.showProgressBar();
              wifiListeners.updateList(realWifiList);
            }
          }
          break;
        }
        case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: {
          Log.d(TAG, "网络列表变化了");
          wifiListChange();
          if (wifiListeners != null) {
            wifiListeners.updateList(realWifiList);
          }
          break;
        }
        case WifiManager.EXTRA_SUPPLICANT_CONNECTED: {
          Toast.makeText(context, "连接上了", Toast.LENGTH_SHORT).show();
        }
        break;
        default:
          break;
      }
      if (WifiSupport.isOpenWifi(context)) {
        if (realWifiList.size() > 0) {
          WifiInfo currentWifi = WifiSupport.getCurrentWifi(context);
          if (!WifiSupport.currentIsBoxWifi(currentWifi.getSSID(), "gvmedia") && WifiSupport.containBoxWifi(realWifiList, "gvmedia")) {
            Toast.makeText(context, "发现盒子网络", Toast.LENGTH_SHORT).show();
          }
        }
      } else {
        Toast.makeText(context, "wifi网络未连接", Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * //网络状态发生改变 调用此方法！
   */
  public void wifiListChange() {
    sortScaResult();
    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(context);
    if (connectedWifiInfo != null) {
      wifiListSet(connectedWifiInfo.getSSID(), connectType);
    }
  }

  /**
   * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
   *
   * @param wifiName
   * @param type
   */
  public List<WifiBean> wifiListSet(String wifiName, int type) {
    int index = -1;
    WifiBean wifiInfo = new WifiBean();
    if (CollectionUtils.isNullOrEmpty(realWifiList)) {
      return realWifiList;
    }
    for (int i = 0; i < realWifiList.size(); i++) {
      realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
    }
    Collections.sort(realWifiList);//根据信号强度排序
    for (int i = 0; i < realWifiList.size(); i++) {
      WifiBean wifiBean = realWifiList.get(i);
      if (index == -1 && ("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)) {
        index = i;
        wifiInfo.setLevel(wifiBean.getLevel());
        wifiInfo.setWifiName(wifiBean.getWifiName());
        wifiInfo.setCapabilities(wifiBean.getCapabilities());
        if (type == 1) {
          wifiInfo.setState(AppContants.WIFI_STATE_CONNECT);
        } else {
          wifiInfo.setState(AppContants.WIFI_STATE_ON_CONNECTING);
        }
      }
    }
    if (index != -1) {
      realWifiList.remove(index);
      realWifiList.add(0, wifiInfo);
      //adapter.notifyDataSetChanged();
    }
    return realWifiList;
  }

  /**
   * 检查是否已经授予权限
   *
   * @return
   */
//  private boolean checkPermission() {
//    for (String permission : NEEDED_PERMISSIONS) {
//      if (ActivityCompat.checkSelfPermission(this, permission)
//              != PackageManager.PERMISSION_GRANTED) {
//        return false;
//      }
//    }
//    return true;
//  }

  /**
   * 申请权限
   */
//  private void requestPermission() {
//    ActivityCompat.requestPermissions(this,
//            NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
//  }

  /**
   * 获取wifi列表然后将bean转成自己定义的WifiBean
   */
  public List<WifiBean> sortScaResult() {
    List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(context));
    realWifiList.clear();
    if (!CollectionUtils.isNullOrEmpty(scanResults)) {
      for (int i = 0; i < scanResults.size(); i++) {
        WifiBean wifiBean = new WifiBean();
        wifiBean.setWifiName(scanResults.get(i).SSID);
        wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
        wifiBean.setCapabilities(scanResults.get(i).capabilities);
        wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level) + "");
        realWifiList.add(wifiBean);

        //排序
        Collections.sort(realWifiList);
        // adapter.notifyDataSetChanged();
      }
    }

    return realWifiList;
//    for (int i = 0; i < realWifiList.size(); i++) {
//      WifiBean wifiBean = realWifiList.get(i);
//      if (wifiBean.getWifiName().contains("gvmedia")){
//        Toast.makeText(context,i+"---"+wifiBean.getWifiName(),Toast.LENGTH_SHORT).show();
//      }
//    }
  }

//  @Override
//  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    boolean hasAllPermission = true;
//    if (requestCode == PERMISSION_REQUEST_CODE) {
//      for (int i : grantResults) {
//        if (i != PackageManager.PERMISSION_GRANTED) {
//          hasAllPermission = false;   //判断用户是否同意获取权限
//          break;
//        }
//      }
//
//      //如果同意权限
//      if (hasAllPermission) {
//        mHasPermission = true;
//        if (WifiSupport.isOpenWifi(WifiListActivity.this) && mHasPermission) {  //如果wifi开关是开 并且 已经获取权限
//          initRecycler();
//        } else {
//          Toast.makeText(WifiListActivity.this, "WIFI处于关闭状态或权限获取失败1111", Toast.LENGTH_SHORT).show();
//        }
//
//      } else {  //用户不同意权限
//        mHasPermission = false;
//        Toast.makeText(WifiListActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
//      }
//    }
//  }


//  public void showProgressBar() {
//    pbWifiLoading.setVisibility(View.VISIBLE);
//  }
//
//  public void hidingProgressBar() {
//    pbWifiLoading.setVisibility(View.GONE);
//  }

  WifiListener wifiListeners = null;

  public interface WifiListener {
    void hidingProgressBar();

    void showProgressBar();

    void updateList(List<WifiBean> wifiBeanArrayList);
  }

  public void setWifiListeners(WifiListener wifiListeners) {
    this.wifiListeners = wifiListeners;
  }
}
