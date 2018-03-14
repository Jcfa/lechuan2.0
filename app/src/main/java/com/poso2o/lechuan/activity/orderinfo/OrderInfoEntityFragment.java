package com.poso2o.lechuan.activity.orderinfo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoManager;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店列表数据
 */

public class OrderInfoEntityFragment extends BaseFragment {
    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("空界面");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        return tv;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), "2018-01-01", "2018-03-14", new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object infoBean) {

            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });

    }

    @Override
    public void initListener() {

    }
}
