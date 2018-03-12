/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.fragment.redbag;

import android.app.Activity;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.redbag.Redbag;
import com.poso2o.lechuan.fragment.manage.BaseFragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 添加红包模块管理
 * Created by 秋 on 2015/9/19.
 */
public class AddRedbagManager extends BaseFragmentManager {

    private AddRedbagFragment fragment;

    public AddRedbagManager(Activity activity) {
        super(activity);
    }
    
    /**
     * 显示
     */
    public void show(AddRedbagFragment.Callback callback) {
    	show(null, callback);
    }
    
    /**
     * 显示
     */
    public void show(Redbag redbag, AddRedbagFragment.Callback callback) {
        EventBus.getDefault().register(this);
    	fragment = getFragment(AddRedbagFragment.class);
    	fragment.setRedMoney(redbag);
    	fragment.setCallback(callback);
    	fragment.setIsRelease(true);
    	show(fragment, R.anim.down_sliding_show_obj, false);
    }

    /**
     * 隐藏
     */
    @Override
    public void hide(boolean isCancel) {
        EventBus.getDefault().unregister(this);
        super.hide(R.anim.down_sliding_hide_obj);
    }

    public boolean confirm() {
        return fragment.confirm();
    }

    /**
     *
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUserResult(UserInfoBean event) {
        fragment.refreshRedEnvelopesAmount();
    }
}