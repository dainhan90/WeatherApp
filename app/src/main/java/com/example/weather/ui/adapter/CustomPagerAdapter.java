package com.example.weather.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weather.api.response.Data;
import com.example.weather.ui.fragment.WeatherDayFragment;
import com.example.weather.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by thanhle on 4/13/16.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<Data> mDatas;

    public CustomPagerAdapter(FragmentManager fm, Context context, ArrayList<Data> datas) {
        super(fm);
        mContext = context;
        mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = WeatherDayFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ViewUtils.getDate(mDatas.get(position).time);
    }
}