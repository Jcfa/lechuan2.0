package com.poso2o.lechuan.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.topup.TopUpActivity;
import com.poso2o.lechuan.activity.topup.WithdrawalActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.AmountTotalBean;
import com.poso2o.lechuan.bean.mine.BrokerageDetailItemBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 红包详情
 * Created by Administrator on 2017-11-30.
 */

public class RedPacketDetailActivity extends BaseActivity{
    private TextView tvAmount, tvIncome, tvRecharge, tvExpend;

    @Override
    protected int getLayoutResId() {

        return R.layout.activity_redpacket_detail;
    }

    @Override
    protected void initView() {
        setTitle("红包详情");
        tvAmount = findView(R.id.tv_amount);
        tvIncome = findView(R.id.tv_income);
        tvRecharge = findView(R.id.tv_recharge);
        tvExpend = findView(R.id.tv_expend);

    }

    @Override
    protected void initData() {
        tvAmount.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
    }

    @Override
    protected void initListener() {
        findView(R.id.layout_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收入记录
                Bundle bundle = new Bundle();
                bundle.putInt(RedPacketRecordActivity.KEY_TYPE, RedPacketRecordActivity.INCOME_ID);
                startActivity(RedPacketRecordActivity.class, bundle);
            }
        });
        findView(R.id.layout_expend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//支出记录
                Bundle bundle = new Bundle();
                bundle.putInt(RedPacketRecordActivity.KEY_TYPE, RedPacketRecordActivity.EXPEND_ID);
                startActivity(RedPacketRecordActivity.class, bundle);
            }
        });
        findView(R.id.layout_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//充值记录
                Bundle bundle = new Bundle();
                bundle.putInt(RedPacketRecordActivity.KEY_TYPE, RedPacketRecordActivity.RECHARGE_ID);
                startActivity(RedPacketRecordActivity.class, bundle);
            }
        });
        findView(R.id.tv_withdrawal).setOnClickListener(new View.OnClickListener() {//提现
            @Override
            public void onClick(View view) {
                startActivity(WithdrawalActivity.class);
            }
        });
        findView(R.id.tv_top_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//充值
                startActivity(TopUpActivity.class);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAmountTotal();
    }

    private void getAmountTotal(){
        MineDataManager.getMineDataManager().getAmountTotal(activity, new IRequestCallBack<AmountTotalBean>() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, AmountTotalBean object) {
                tvAmount.setText(NumberUtils.format2(object.getTotal_amount()));
                tvRecharge.setText(NumberUtils.format2(object.getTotal_amount_add()));
                tvExpend.setText(NumberUtils.format2(object.getTotal_amount_del()));
                tvIncome.setText(NumberUtils.format2(object.getTotal_red_envelopes_amount()));
            }
        });
    }

}
