package com.renjk.gank.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.renjk.gank.R;
import com.renjk.gank.View.RatioImageView;
import com.renjk.gank.bean.AndroidInfo;
import com.renjk.gank.bean.GankItem;
import com.renjk.gank.util.AndroidTool;

import java.util.List;

/**
 * Created by admin on 2016/6/7.
 */
public class AmazingAdapter extends  RecyclerView.Adapter<AmazingAdapter.MyViewHolder> implements View.OnClickListener {
    private List<GankItem> data;
    private Context context;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }

    public void setData(List<GankItem> data){
        this.data = data;
    }

    public AmazingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_amazing, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        GankItem item = data.get(position);
        Glide.with(context).load(item.getUrl()).into(holder.amazing);
        holder.itemView.setTag(data.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view,(String)view.getTag());
        }
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        RatioImageView amazing;
        public MyViewHolder(View view)
        {
            super(view);
            amazing = (RatioImageView) view.findViewById(R.id.iv_amazing);
            int widthDp = AndroidTool.px2dip(context,AndroidTool.getScreenWidth((Activity)context)/2-10);
            amazing.setOriginalSize(widthDp,widthDp);
        }
    }


}
