package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenNumber;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenNumberBe;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenNumberBean;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrialBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.ServiceOrderinTrialManager;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class OpenNumberActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private Button bt_wopen_number_pay;
    //服务的项目
    private String service_name,amount,service_id,service_type;
    //联系方式
    private EditText et_wopen_number_mobile,ed_wopen_number_attn;
    //服务金额
    private TextView tv_wopen_number_wechar_authentication_amount;
    private TextView tv_wopen_number_agency_amount;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_open_number;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        bt_wopen_number_pay=(Button)findViewById(R.id.bt_wopen_number_pay);
        ed_wopen_number_attn=(EditText)findViewById(R.id.ed_wopen_number_attn);
        et_wopen_number_mobile=(EditText)findViewById(R.id.et_wopen_number_mobile);
        tv_wopen_number_wechar_authentication_amount=(TextView)findViewById(R.id.tv_wopen_number_wechar_authentication_amount);
        tv_wopen_number_agency_amount=(TextView)findViewById(R.id.tv_wopen_number_agency_amount);
    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.wopen_number));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));

        //获取intent信息
        String attn=getIntent().getStringExtra("attn");
        String mobile=getIntent().getStringExtra("mobile");
        ed_wopen_number_attn.setText(attn);
        et_wopen_number_mobile.setText(mobile);

        //获取缴费的服务id
        ServiceOrderinTrialManager manager=new ServiceOrderinTrialManager();
        manager.UnpaidDate(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                Gson gson=new Gson();
                OpenNumberBe order=gson.fromJson(result.toString(),OpenNumberBe.class);
                OpenNumber o=gson.fromJson(order.list.get(0).getRemark(),OpenNumber.class);
                service_id=order.list.get(0).getService_id();
                amount=order.list.get(0).getAmount();
                service_name=order.list.get(0).getService_name();
                tv_wopen_number_wechar_authentication_amount.setText(o.getWechar_authentication_amount());
                tv_wopen_number_agency_amount.setText(o.getAgency_amount());
                service_type=order.list.get(0).getService_type();
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    @Override
    protected void initListener() {
        bt_wopen_number_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //发起缴费
            case R.id.bt_wopen_number_pay:
                Intent i=new Intent();
                i.putExtra("service_id", service_id);
                i.putExtra("amount", amount);
                i.putExtra("service_name", service_name);
                i.putExtra("service_type",service_type);
                i.setClass(OpenNumberActivity.this,ServiceOrderActivity.class);
                startActivity(i);
                break;
        }
    }
}
