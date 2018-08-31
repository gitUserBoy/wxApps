package com.wx.app.wxapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wx.app.wxapp.R;
import com.wx.app.wxapp.bean.WifiBean;
import com.wx.app.wxapp.manager.wifi.AppContants;

import java.util.List;


/**
 * Created by ${GuoZhaoHui} on 2017/11/7.
 * Email:guozhaohui628@gmail.com
 */

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

  private Context mContext;
  private List<WifiBean> resultList;
  private onItemClickListener onItemClickListener;

  public void setOnItemClickListener(WifiListAdapter.onItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public WifiListAdapter(Context mContext, List<WifiBean> resultList) {
    this.mContext = mContext;
    this.resultList = resultList;
  }


  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi, parent, false);
    MyViewHolder vh = new MyViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, final int position) {
    final WifiBean bean = resultList.get(position);
    holder.tvItemWifiName.setText(bean.getWifiName());

    //可以传递给adapter的数据都是经过处理的，已连接或者正在连接状态的wifi都是处于集合中的首位，所以可以写出如下判断
    if (position == 0 && (AppContants.WIFI_STATE_CONNECT.equals(bean.getState()) || AppContants.WIFI_STATE_ON_CONNECTING.equals(bean.getState()))) {
      holder.flLinkingIv.setVisibility(View.VISIBLE);
      if (AppContants.WIFI_STATE_CONNECT.equals(bean.getState())) {
        holder.ivHook.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.GONE);
      } else {
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.ivHook.setVisibility(View.GONE);
      }
    } else {
      holder.flLinkingIv.setVisibility(View.INVISIBLE);
    }


    holder.ivLock.setVisibility(bean.getLock() ? View.VISIBLE : View.GONE);
    holder.ivWifi.setImageResource(getLevel(bean.getLevel()));
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(v, position, bean);
      }
    });
  }

  public void setData(List<WifiBean> data) {
    if (resultList.size() > 0) {
      resultList.clear();
    }
    resultList.addAll(data);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return resultList.size();
  }


  static class MyViewHolder extends RecyclerView.ViewHolder {

    View itemview;
    FrameLayout flLinkingIv;
    TextView tvItemWifiName;
    ImageView ivHook;
    ImageView ivWifi;
    ImageView ivLock;
    ProgressBar progressBar;

    public MyViewHolder(View itemView) {
      super(itemView);
      itemview = itemView;
      tvItemWifiName = (TextView) itemView.findViewById(R.id.tv_item_wifi_name);
      flLinkingIv = itemView.findViewById(R.id.fl_link_iv);
      progressBar = itemView.findViewById(R.id.pb_linking);
      ivHook = itemView.findViewById(R.id.iv_hook);
      ivWifi = itemView.findViewById(R.id.iv_wifi);
      ivLock = itemView.findViewById(R.id.iv_lock);

    }

  }

  public interface onItemClickListener {
    void onItemClick(View view, int postion, Object o);
  }


  private int getLevel(int level) {
    switch (level) {
      case 4:
        return R.drawable.wifi_icon0;
      case 3:
        return R.drawable.wifi_icon1;
      case 2:
        return R.drawable.wifi_icon2;
      case 1:
        return R.drawable.wifi_icon3;
    }
    return R.drawable.wifi_icon3;
  }
}
