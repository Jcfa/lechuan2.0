package com.poso2o.lechuan.fragment.oa;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.OAHelperActivity;
import com.poso2o.lechuan.adapter.OAFragmentPagerAdapter;
import com.poso2o.lechuan.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/22.
 */

public class OaSetFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    /**
     * 设置界面（公众号购买）
     */
    private OAServiceFragment oaServiceFragment;

    /**
     * 设置界面（模板）
     */
    private OaSetModelFragment oaSetModelFragment;

    private OAFragmentPagerAdapter fragmentPagerAdapter;

    private int p = 0;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oa_publish, container, false);
    }

    @Override
    public void initView() {
        mViewPager = findView(R.id.viewpager);
    }

    @Override
    public void initData() {
        oaServiceFragment = new OAServiceFragment();
        oaSetModelFragment = new OaSetModelFragment();
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(oaServiceFragment);
        fragments.add(oaSetModelFragment);
        fragmentPagerAdapter = new OAFragmentPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void initListener() {
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        p = position;
        if(position==0){
            ((OAHelperActivity) getActivity()).setPublishTitle();
        }else{
            ((OAHelperActivity) getActivity()).setDraftTitle();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setFragment(int position){
        mViewPager.setCurrentItem(position);
    }

    public int getPosition(){
        return p;
    }
}
