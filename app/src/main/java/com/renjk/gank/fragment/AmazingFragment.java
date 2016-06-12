package com.renjk.gank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.renjk.gank.Cons.Cons;
import com.renjk.gank.R;
import com.renjk.gank.activity.PhotoViewActivity;
import com.renjk.gank.activity.WebViewActivity;
import com.renjk.gank.adapter.AmazingAdapter;
import com.renjk.gank.bean.AndroidInfo;
import com.renjk.gank.bean.GankItem;
import com.renjk.gank.request.InfoRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by admin on 2016/6/7.
 */
public class AmazingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        AmazingAdapter.OnRecyclerViewItemClickListener,View.OnClickListener {
    private RecyclerView lv_android;
    private AmazingAdapter mAdapter;
    private List<GankItem> data;
    private SwipeRefreshLayout refreshLayout;
    private int pageIndex = 1;
    private RecyclerView.LayoutManager mLayoutManager;
    private Boolean isLoadingMore = false;
    public static final String TAB_KEY = "TAB";
    private String tab;
    private LinearLayout loadAgain,loadingView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_android;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        lv_android = (RecyclerView) view.findViewById(R.id.lv_android);
        mLayoutManager= new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);
        lv_android.setLayoutManager(mLayoutManager);
        mAdapter = new AmazingAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        data = new ArrayList<>();
        lv_android.setAdapter(mAdapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
        loadAgain = (LinearLayout) view.findViewById(R.id.view_load);
        loadingView = (LinearLayout) view.findViewById(R.id.loadingView);
        loadAgain.setOnClickListener(this);
        initScrollListener();
        tab = getArguments().getString(TAB_KEY);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        pageIndex = 1;
        requestData(pageIndex);
    }

    public static AmazingFragment newInstance(String item) {

        Bundle args = new Bundle();
        args.putString(TAB_KEY,item);
        AmazingFragment fragment = new AmazingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void requestData(int pageIndex) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Cons.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InfoRequest infoRequest = retrofit.create(InfoRequest.class);
        Call<AndroidInfo> call = infoRequest.getAndroidResult(tab,"30",String.valueOf(pageIndex));
        call.enqueue(new Callback<AndroidInfo>() {

            @Override
            public void onResponse(Call<AndroidInfo> call, Response<AndroidInfo> response) {
                AndroidInfo info = response.body();
                if (info.isError()) return;
                data = info.getResults();
                mAdapter.setData(data);
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AndroidInfo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                loadAgain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        requestData(pageIndex);
    }

    public void onLoadMore(){
        pageIndex++;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Cons.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InfoRequest infoRequest = retrofit.create(InfoRequest.class);
        Call<AndroidInfo> call = infoRequest.getAndroidResult(tab,"20",String.valueOf(pageIndex));
        call.enqueue(new Callback<AndroidInfo>() {

            @Override
            public void onResponse(Call<AndroidInfo> call, Response<AndroidInfo> response) {
                AndroidInfo info = response.body();
                if (info.isError()) return;
                data.addAll(info.getResults());
                mAdapter.setData(data);
                mAdapter.notifyDataSetChanged();
                isLoadingMore = false;
            }

            @Override
            public void onFailure(Call<AndroidInfo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void initScrollListener(){
        lv_android.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                if (!isLoadingMore&&lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    onLoadMore();
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, String data) {
        Intent i = new Intent(getActivity(), PhotoViewActivity.class);
        i.putExtra("url",data);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_load:
                loadAgain.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                requestData(pageIndex);
                break;
        }
    }
}
