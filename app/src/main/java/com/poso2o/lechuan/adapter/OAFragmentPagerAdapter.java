package com.poso2o.lechuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.poso2o.lechuan.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-03.
 */

public class OAFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public OAFragmentPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
