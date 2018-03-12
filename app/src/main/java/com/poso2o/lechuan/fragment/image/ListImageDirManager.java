/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.fragment.image;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.image.ImageFolder;
import com.poso2o.lechuan.fragment.common.PlaceholderFragment;
import com.poso2o.lechuan.fragment.manage.BaseFragmentManager;

import java.util.ArrayList;

/**
 * 图片目录模块管理模块管理
 * Created by 秋 on 2015/9/19.
 */
public class ListImageDirManager extends BaseFragmentManager {

    public ListImageDirManager(Activity activity) {
        super(activity);
    }
    
    /**
     * 显示
     */
    public void show(ArrayList<ImageFolder> data, ListImageDirFragment.OnImageDirSelected imageDirSelected) {
    	ListImageDirFragment fragment = getFragment(ListImageDirFragment.class);
    	fragment.setData(data);
    	fragment.setOnImageDirSelected(imageDirSelected);
		show(fragment, R.anim.down_sliding_show_obj);
    }

    @Override
    public void show(BaseFragment fragment, int anim) {
        if (fragment != null) {
            // 给全局变量赋值
            this.fragment = fragment;
            // 获取碎片事务对象
            FragmentTransaction ft;
            if(!this.fragment.isAdded()){
                ft = fragmentManager.beginTransaction();
                // 创建遮罩Fragment
                placeholderFragment = getFragment(PlaceholderFragment.class);
                // 设置碎片活动回调事件
                placeholderFragment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        hide(true);
                    }
                });
                // 设置碎片动画
                ft.setCustomAnimations(R.anim.visible_anim, R.anim.gone_anim);
                ft.add(R.id.layout_group, placeholderFragment, placeholderFragment.getClass().getName() + TAG);
                // 提交事务
                ft.commitAllowingStateLoss();
                // 再次获取碎片事务对象
                ft = fragmentManager.beginTransaction();
                // 设置碎片动画
                if (anim == 0) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                } else{
                    ft.setCustomAnimations(anim, 0);
                }
                // 新增选订单碎片
                ft.add(R.id.layout_group, fragment, fragment.getClass().getName() + TAG);
                // 提交事务
                ft.commitAllowingStateLoss();

                // 设置显示状态为true
                isShow = true;
            }
        }
    }

    /**
     * 隐藏
     */
    @Override
    public void hide(boolean isCancel) {
        super.hide(R.anim.down_sliding_hide_obj);
    }
}