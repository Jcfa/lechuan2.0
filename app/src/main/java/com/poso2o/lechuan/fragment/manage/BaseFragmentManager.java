/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.fragment.manage;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.base.BaseFragment.FragmentAction;
import com.poso2o.lechuan.fragment.common.PlaceholderFragment;


/**
 * Fragment管理基础类
 * 
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
public class BaseFragmentManager implements FragmentAction {

    /**
     * 标签
     */
	protected String TAG = getClass().getSimpleName();

    /**
     * 依附的Activity
     */
    protected BaseActivity activity;

    /**
     * 碎片管理
     */
    protected FragmentManager fragmentManager;

    /**
     * 半透明遮罩
     */
    protected PlaceholderFragment placeholderFragment;

    /**
     * 当前显示的Fragment
     */
    protected BaseFragment fragment;

    /**
     * 记录显示状态
     */
    protected boolean isShow;

    public BaseFragmentManager(Activity activity) {
        this.activity = (BaseActivity)activity;
        // 获取Fragment管理对象
        fragmentManager = this.activity.getSupportFragmentManager();
    }

    public boolean isShow() {
        return isShow;
    }

    public void show(BaseFragment fragment) {
        show(fragment, 0);
    }
    
    public void show(BaseFragment fragment, int anim) {
        show(fragment, anim, true);
    }

    public void show(BaseFragment fragment, int anim, boolean isAddPlaceholder) {
        if (fragment != null) {
            // 获取父布局
            View decorView = activity.getWindow().getDecorView();
            // 给父布局设置ID
            decorView.setId(R.id.layout_context);
            // 给全局变量赋值
            this.fragment = fragment;
            // 设置碎片活动回调事件
            fragment.setFragmentAction(this);
            // 获取碎片事务对象
            FragmentTransaction ft;
            if(!this.fragment.isAdded()){
                if (isAddPlaceholder) {
                    ft = fragmentManager.beginTransaction();
                    // 创建遮罩Fragment
                    placeholderFragment = getFragment(PlaceholderFragment.class);
                    // 设置碎片活动回调事件
                    placeholderFragment.setFragmentAction(this);
                    // 设置碎片活动回调事件
                    placeholderFragment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            hide(true);
                        }
                    });
                    // 设置碎片动画
                    ft.setCustomAnimations(R.anim.visible_anim, R.anim.gone_anim);
                    ft.add(R.id.layout_context, placeholderFragment, placeholderFragment.getClass().getName() + TAG);
                    // 提交事务
                    ft.commitAllowingStateLoss();
                }
                // 再次获取碎片事务对象
                ft = fragmentManager.beginTransaction();
                // 设置碎片动画
                if (anim == 0) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                } else{
                    ft.setCustomAnimations(anim, 0);
                }
                // 新增选订单碎片
                ft.add(R.id.layout_context, fragment, fragment.getClass().getName() + TAG);
                // 提交事务
                ft.commitAllowingStateLoss();

                // 设置显示状态为true
                isShow = true;
            }
        }
    }

    /**
     * isCancel:是否撤销窗口
     */
    public void hide(boolean isCancel) {
        hide(0);
    }

    public void hide(int anim) {
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (anim == 0) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            } else {
                ft.setCustomAnimations(0, anim);
            }
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
            if (placeholderFragment != null) {
                ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.visible_anim, R.anim.gone_anim);
                ft.remove(placeholderFragment);
                ft.commitAllowingStateLoss();
            }
            isShow = false;
//            mActivity.removeFragmentAction();// 关闭当前Fr
            fragment = null;
        }
    }

    /**
     * 获取已经存在的Fragment
     */
    @SuppressWarnings("unchecked")
    public <T extends Fragment> T getFragment(Class<? extends Fragment> cla) {
        try {
            String name = cla.getName();
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(name);
            if (fragment != null) {
                return (T) fragment;
            }
            return (T) cla.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
