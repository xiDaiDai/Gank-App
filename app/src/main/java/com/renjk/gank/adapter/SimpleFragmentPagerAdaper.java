package com.renjk.gank.adapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.renjk.gank.fragment.AndroidFragment;
import com.renjk.gank.fragment.PageFragment;

/**
 * Created by admin on 2016/6/6.
 */
public class SimpleFragmentPagerAdaper extends FragmentPagerAdapter {

    final int PAGE_COUNT = 5;
    private String tabTitles[] = new String[]{"Android","iOS","前端","视频","^_^"};
    private Context context;

    public SimpleFragmentPagerAdaper(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = AndroidFragment.newInstance("Android");
                break;
            case 1:
                fragment = AndroidFragment.newInstance("iOS");
                break;
            case 2:
                fragment = AndroidFragment.newInstance("前端");
                break;
            case 3:
                fragment = AndroidFragment.newInstance("休息视频");
                break;
            case 4:
                fragment = AndroidFragment.newInstance("福利");
                break;
        }

        return fragment;

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
