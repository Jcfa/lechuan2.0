package com.poso2o.lechuan.broadcast.wopenbroad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wopenaccount.EmpowermentActivity;
import com.poso2o.lechuan.activity.wshop.WShopActivity;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealShopManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.util.TextUtil;

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
