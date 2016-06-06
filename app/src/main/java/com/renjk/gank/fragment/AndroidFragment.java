package com.renjk.gank.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.renjk.gank.Cons.Cons;
import com.renjk.gank.R;
import com.renjk.gank.adapter.AndroidAdapter;
import com.renjk.gank.bean.AndroidInfo;
import com.renjk.gank.request.InfoRequest;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2016/6/6.
 */
public class AndroidFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView lv_android;
    private AndroidAdapter mAdapter;
    private List<AndroidInfo.ResultsBean> data;
    private SwipeRefreshLayout refreshLayout;
    private int pageIndex = 1;
    private RecyclerView.LayoutManager mLayoutManager;
    private Boolean isLoadingMore = false;
    public static final String TAB_KEY = "TAB";
    private String tab;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_android;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        lv_android = (RecyclerView) view.findViewById(R.id.lv_android);
        mLayoutManager= new LinearLayoutManager(getActivity());
        lv_android.setLayoutManager(mLayoutManager);
        mAdapter = new AndroidAdapter(getActivity());
        data = new ArrayList<>();
        lv_android.setAdapter(mAdapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(this);
        initScrollListener();
        tab = getArguments().getString(TAB_KEY);
    }

    public static AndroidFragment newInstance(String item) {

        Bundle args = new Bundle();
        args.putString(TAB_KEY, item);
        AndroidFragment fragment = new AndroidFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState){
        requestData(pageIndex);
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
            }

            @Override
            public void onFailure(Call<AndroidInfo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
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
}
