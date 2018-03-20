package com.poso2o.lechuan.broadcast.wopenbroad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wopenaccount.EmpowermentActivity;
import com.poso2o.lechuan.activity.wopenaccount.ServiceOrderActivity;
import com.poso2o.lechuan.activity.wshop.WShopActivity;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class WeiXinFuWuReceived extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i=new Intent();
        intent.putExtra(RShopMainActivity.DATA_SHOP,"");
        i.setClass(context,WShopActivity.class);
        context.startActivity(i);

        EmpowermentActivity.eactiviyt.finish();
//        ServiceOrderingTrialActivity.order.finish();
        ServiceOrderActivity.sactivity.finish();

    }
}
