package com.poso2o.lechuan.broadcast.wopenbroad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.poso2o.lechuan.activity.vdian.EmpowermentActivity;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class WeiXinKaiReceived extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent();
        i.setClass(context,EmpowermentActivity.class);
        context.startActivity(i);



    }
}
