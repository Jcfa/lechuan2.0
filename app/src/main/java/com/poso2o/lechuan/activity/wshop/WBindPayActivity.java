package com.poso2o.lechuan.activity.wshop;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.shopdata.BindPayData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2018/1/17.
 */

public class WBindPayActivity extends BaseActivity implements View.OnClickListener{

    //是否已绑定微信支付
    public static final String HAD_BIND_PAY = "bind_pay_data";

    //提交
    private Button bind_pay_commit;
    //注意事项1
    private TextView bind_pay_warn_one;
    //注意事项2
    private TextView bind_pay_warn_two;

    //appkey
    private EditText bind_pay_appid;
    //mch_id
    private EditText bind_pay_mch_id;

    //修改还是提交
    private boolean isEdit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind_pay;
    }

    @Override
    protected void initView() {
        bind_pay_commit = (Button) findViewById(R.id.bind_pay_commit);
        bind_pay_warn_one = (TextView) findViewById(R.id.bind_pay_warn_one);
        bind_pay_warn_two = (TextView) findViewById(R.id.bind_pay_warn_two);

        bind_pay_appid = (EditText) findViewById(R.id.bind_pay_appid);
        bind_pay_mch_id = (EditText) findViewById(R.id.bind_pay_mch_id);
    }

    @Override
    protected void initData() {
        bind_pay_warn_one.setText(Html.fromHtml("1、请联系我们的客服上传" +"<font color=\"#ff0000\">公钥证书文件</font>" + "。"));
        bind_pay_warn_two.setText(Html.fromHtml("2、查看【微信支付】账户信息，找到" + "<font color=\"#ff0000\">app_id</font>和" + "<font color=\"#ff0000\">商户号：mch_id</font>填入以上对应地方，点击提交即可。"));
        switchType();
    }

    @Override
    protected void initListener() {
        //返回
        findViewById(R.id.bind_pay_back).setOnClickListener(this);

        bind_pay_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bind_pay_back:
                finish();
                break;
            case R.id.bind_pay_commit:
                commitInfo();
                break;
        }
    }

    //提交还是修改
    private void switchType(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        Object o = bundle.get(HAD_BIND_PAY);
        if (o == null){
            isEdit = false;
        }else {
            isEdit = true;
            BindPayData bindPayData = (BindPayData) o;
            bind_pay_appid.setText(bindPayData.appkey);
            bind_pay_mch_id.setText(bindPayData.mch_id);
            if (bindPayData.appkey.length() > 0)bind_pay_appid.setSelection(bindPayData.appkey.length());
        }
        if (isEdit){
            bind_pay_commit.setText("修改");
        }
    }

    //提交或者修改都用该方法
    private void commitInfo(){
        String appKey = bind_pay_appid.getText().toString();
        if (TextUtil.isEmpty(appKey)){
            Toast.show(WBindPayActivity.this,"请输入公众号appkey");
            bind_pay_appid.requestFocus();
            return;
        }
        String mch_id = bind_pay_mch_id.getText().toString();
        if (TextUtil.isEmpty(mch_id)){
            Toast.show(WBindPayActivity.this,"请输入商户号mch_id");
            bind_pay_mch_id.requestFocus();
            return;
        }
        showLoading("正在提交数据...");
        WShopManager.getrShopManager().bindPay(WBindPayActivity.this, appKey, mch_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(WBindPayActivity.this,"绑定成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(WBindPayActivity.this,msg);
            }
        });
    }
}
