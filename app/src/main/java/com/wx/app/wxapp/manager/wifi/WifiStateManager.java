package com.wx.app.wxapp.manager.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
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
//  private static WifiStateManager wifiStateManager = null;

  private static final String TAG = "WifiStateManager";

  private WifiBroadcastReceiver wifiReceiver;

  private  Context context;

  List<WifiBean> realWifiList = new ArrayList<>();
  private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）
  private int GPS_REQUEST_CODE = 1022;

  /*单例设置*/
//  public static WifiStateManager getInstance(Context contexts) {
//    synchronized (WindowManager.class) {
//      if (wifiStateManager == null) {
//        wifiStateManager = new WifiStateManager(contexts);
//      }
//      return wifiStateManager;
//    }
//  }
//
//  private WifiStateManager(Context contexts) {
//    this.context = contexts;
//  }

  public WifiStateManager(Context contexts) {
    this.context = contexts;
    localPermission = checkLocationPermission();
  }

  public void createWifiBroadcastReceiver() {
    try {
      if (wifiReceiver == null) {
        wifiReceiver = new WifiBroadcastReceiver();
      }
      IntentFilter filter = new IntentFilter();
      filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
      filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
      filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
      filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);//监听密码连接
      if (context != null)
        this.context.registerReceiver(wifiReceiver, filter);

//      WifiSupport.scanWifi(context);
    } catch (Exception e) {
      Log.e(TAG, "createWifiBroadcastReceiver-----" + e.toString());
    }
  }

  public void unregisterReceiver() {
    try {
      if (wifiReceiver != null && this.context != null) {
        context.unregisterReceiver(wifiReceiver);
      }
    } catch (Exception e) {
      Log.e(TAG, "unregisterReceiver-----" + e.toString());
    }
  }

  //监听wifi状态
  private class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      wifiListeners.action(context, intent);
      if (!localPermission) return;
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
              break;
            }
            case WifiManager.WIFI_STATE_DISABLING: {
              Log.d(TAG, "正在关闭");
              break;
            }
            case WifiManager.WIFI_STATE_ENABLED: {
              Log.d(TAG, "已经打开");
              WifiSupport.scanWifi(context);
              updateList();
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
          if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
            for (int i = 0; i < realWifiList.size(); i++) {//没连接上将 所有的连接状态都置为“未连接”
              realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
            }
            if (wifiListeners != null) {
              wifiListeners.hidingProgressBar();
              wifiListeners.updateList(realWifiList);
            }
          } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
            WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(context);
            //连接成功 跳转界面 传递ip地址
            connectType = 1;
            wifiListSet(connectedWifiInfo.getSSID(), connectType);
            if (wifiListeners != null) {
              wifiListeners.hidingProgressBar();
              wifiListeners.updateList(realWifiList);
            }
          } else {
            NetworkInfo.DetailedState state = info.getDetailedState();
            if (state == state.CONNECTING) {
              WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(context);
              connectType = 2;
              wifiListSet(connectedWifiInfo.getSSID(), connectType);
              if (wifiListeners != null) {
                wifiListeners.showProgressBar();
                wifiListeners.updateList(realWifiList);
              }
            } else if (state == state.AUTHENTICATING) {
              Log.d(TAG, "正在验证身份信息...");
            } else if (state == state.OBTAINING_IPADDR) {
              Log.d(TAG, "正在获取IP地址...");
            } else if (state == state.FAILED) {
              Log.d(TAG, "连接失败");
            }
          }
          break;
        }

        case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: {
          Log.d(TAG, "网络列表变化了");
          wifiListChange();

          List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(context));
          Log.e(TAG, "onReceive: "+scanResults.size() );
          if (wifiListeners != null) {
            wifiListeners.updateList(realWifiList);
          }
          break;
        }
        case WifiManager.EXTRA_SUPPLICANT_CONNECTED: {
        }
        break;
        default:
          break;
      }
    }
  }

  /**
   * //网络状态发生改变 调用此方法！
   */
  private void wifiListChange() {
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
  private List<WifiBean> wifiListSet(String wifiName, int type) {
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

  private static boolean localPermission=false;
  /**
   * 获取wifi列表然后将bean转成自己定义的WifiBean
   */
  private List<WifiBean> sortScaResult() {
    if (!localPermission) return realWifiList;
    List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(context));
    realWifiList.clear();
    if (!CollectionUtils.isNullOrEmpty(scanResults)) {
      for (int i = 0; i < scanResults.size(); i++) {
        WifiBean wifiBean = new WifiBean();
        wifiBean.setWifiName(scanResults.get(i).SSID);
        wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
        wifiBean.setCapabilities(scanResults.get(i).capabilities);
        wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level));
        wifiBean.setLock(setPwdState(wifiBean));
        realWifiList.add(wifiBean);

        //排序
        Collections.sort(realWifiList);
      }
    }
    return realWifiList;
  }

  WifiListener wifiListeners = null;

  public interface WifiListener {
    void hidingProgressBar();

    void showProgressBar();

    void updateList(List<WifiBean> wifiBeanArrayList);

    void action(Context context, Intent intent);
  }

  public void setWifiListeners(WifiListener wifiListeners) {
    this.wifiListeners = wifiListeners;
  }

  public void updateList() {
    sortScaResult();
    if (wifiListeners != null) {
      wifiListeners.updateList(realWifiList);
    }
  }

  /**
   * 判断是否需要密码
   * @param wifiBean
   * @return
   */
  public boolean setPwdState(WifiBean wifiBean) {
    if (wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)) {
      String capabilities = wifiBean.getCapabilities();
      if (WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {//无需密码
        return false;
      } else {   //需要密码，弹出输入密码dialog
        WifiConfiguration tempConfig = WifiSupport.isExsits(wifiBean.getWifiName(), context);
        if (tempConfig == null) {
          return true;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  //两个危险权限需要动态申请
  private static final String[] NEEDED_PERMISSIONS = new String[]{
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION
  };

  /**
   * 检查是否已经授予位置权限
   *
   * @return
   */
  private boolean checkLocationPermission() {
    for (String permission : NEEDED_PERMISSIONS) {
      if (ActivityCompat.checkSelfPermission(context, permission)
              != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }
}
