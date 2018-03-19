package com.poso2o.lechuan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.broadcast.BroadcastManager;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.show(this, "微信支付成功！");

                Intent i=new Intent();
                i.setAction(Constant.BROADCAST_WEIXIN_TOP_UP);
                sendBroadcast(i);
               // BroadcastManager.sendBroadcast(Constant.BROADCAST_WEIXIN_TOP_UP);
                EventBus.getDefault().post(new PayEvent(1,PayEvent.WEIXIN_TYPE));
            } else if (resp.errCode == -1) {
//                Toast.show(this, R.string.toast_top_up_fail);
                Toast.show(this, "微信支付失败！");


//                EventBus.getDefault().post(new PayEvent(0,PayEvent.WEIXIN_TYPE));
            }
        }
        finish();
    }
}