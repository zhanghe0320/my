package com.onesmock.activity.main.administratorInformation.wifi.wifi2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onesmock.R;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.app.AppContants;
import com.onesmock.activity.main.administratorInformation.wifi.wifi2.bean.WifiBean;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_list, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WifiBean bean = resultList.get(position);
        holder.tvItemWifiName.setText(bean.getWifiName());
        holder.tvItemWifiStatus.setText("("+bean.getState()+")");
        holder.tvItemWifiLevel.setText("["+bean.getLevel()+"]");
        //可以传递给adapter的数据都是经过处理的，已连接或者正在连接状态的wifi都是处于集合中的首位，所以可以写出如下判断
        if(position == 0  && (AppContants.WIFI_STATE_ON_CONNECTING.equals(bean.getState()) || AppContants.WIFI_STATE_CONNECT.equals(bean.getState()))){
            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.black));
        }else{
            holder.tvItemWifiName.setTextColor(mContext.getResources().getColor(R.color.whitesmoke));
            holder.tvItemWifiStatus.setTextColor(mContext.getResources().getColor(R.color.whitesmoke));
        }

        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view,position,bean);
            }
        });
    }

    public void replaceAll(List<WifiBean> datas) {
        if (resultList.size() > 0) {
            resultList.clear();
        }
        resultList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        View itemview;
        TextView tvItemWifiName, tvItemWifiStatus,tvItemWifiLevel;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tvItemWifiName = (TextView) itemView.findViewById(R.id.tv_item_wifi_name);
            tvItemWifiStatus = (TextView) itemView.findViewById(R.id.tv_item_wifi_status);
            tvItemWifiLevel = (TextView) itemView.findViewById(R.id.tv_item_wifi_level);

            itemview.setBackgroundColor(Color.parseColor("#2f7d67"));
            tvItemWifiName.setBackgroundColor(Color.parseColor("#2f7d67"));
            tvItemWifiStatus.setBackgroundColor(Color.parseColor("#2f7d67"));
            tvItemWifiLevel.setBackgroundColor(Color.parseColor("#2f7d67"));
        }

    }

    public interface onItemClickListener{
        void onItemClick(View view, int postion, Object o);
    }

}
