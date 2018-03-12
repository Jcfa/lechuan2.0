package com.poso2o.lechuan.fragment.oa;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.OAHelperActivity;
import com.poso2o.lechuan.adapter.OAFragmentPagerAdapter;
import com.poso2o.lechuan.base.BaseFragment;

import java.util.ArrayList;

/**
 * 公众号发布界面
 * <p>
 * Created by Jaydon on 2018/1/26.
 */
public class OAPublishFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private PublishEditFragment mEditFragment;
    private PublishDraftFragment mDraftFragment;
    private OAFragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oa_publish, container, false);
    }

    @Override
    public void initView() {
        mViewPager = findView(R.id.viewpager);
        mEditFragment = new PublishEditFragment();
        mDraftFragment = new PublishDraftFragment();
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(mEditFragment);
        fragments.add(mDraftFragment);
        fragmentPagerAdapter = new OAFragmentPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    /**
     * 显示编辑器视图
     */
    public void showEditView() {
        mViewPager.setCurrentItem(0);
    }

    /**
     * 显示稿件箱视图
     */
    public void showDraftView() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position==0){
            ((OAHelperActivity) getActivity()).setPublishTitle();
        }else{
            ((OAHelperActivity) getActivity()).setDraftTitle();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}