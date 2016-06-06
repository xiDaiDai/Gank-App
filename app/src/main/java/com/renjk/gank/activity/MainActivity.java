package com.renjk.gank.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.renjk.gank.adapter.SimpleFragmentPagerAdaper;
import com.renjk.gank.request.InfoRequest;
import com.renjk.gank.Cons.Cons;
import com.renjk.gank.R;
import com.renjk.gank.bean.AndroidInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private SimpleFragmentPagerAdaper pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView tv_result;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intView();

    }

    private void intView() {
        pagerAdapter = new SimpleFragmentPagerAdaper(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        toolbar.setTitle(R.string.app_name);

    }




}
