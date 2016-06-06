package com.renjk.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renjk.gank.R;
import com.renjk.gank.bean.AndroidInfo;

import java.util.List;

/**
 * Created by admin on 2016/6/6.
 */
public class AndroidAdapter extends RecyclerView.Adapter<AndroidAdapter.MyViewHolder> {
    private List<AndroidInfo.ResultsBean> data;
    private Context context;

    public AndroidAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AndroidInfo.ResultsBean> data){
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.list_item_android, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AndroidInfo.ResultsBean item = data.get(position);
        holder.desc.setText(item.getDesc());
        String date = item.getPublishedAt();
        holder.date.setText(date.substring(0,date.indexOf("T")));
        holder.who.setText("@ "+item.getWho());
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView desc;
        TextView who;
        TextView date;

        public MyViewHolder(View view)
        {
            super(view);
            desc = (TextView) view.findViewById(R.id.desc);
            who = (TextView) view.findViewById(R.id.who);
            date = (TextView) view.findViewById(R.id.date);
        }
    }


}
