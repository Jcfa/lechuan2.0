package com.poso2o.lechuan.activity.vdian;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenNumber;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenNumberBe;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.EmpowermentManager;

/**
 * 开通公众号说明
 *
 * Created by Administrator on 2018/3/14 0014.
 */
public class ApplyOAActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 前往支付
     */
    private Button apply_oa_to_pay;

    /**
     * 联系方式:名称，电话
     */
    private EditText apply_oa_contacts, apply_oa_mobile;

    /**
     * 认证费
     */
    private TextView apply_oa_approve_money;

    /**
     * 代理费
     */
    private TextView apply_oa_agency_money;

    /**
     * 服务的项目
     */
    private String service_name, amount, service_id, service_type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_oa;
    }

    @Override
    protected void initView() {
        apply_oa_to_pay = (Button) findViewById(R.id.apply_oa_to_pay);
        apply_oa_contacts = (EditText) findViewById(R.id.apply_oa_contacts);
        apply_oa_mobile = (EditText) findViewById(R.id.apply_oa_mobile);
        apply_oa_approve_money = (TextView) findViewById(R.id.apply_oa_approve_money);
        apply_oa_agency_money = (TextView) findViewById(R.id.apply_oa_agency_money);
    }

    @Override
    protected void initData() {
        setTitle("开通公众号");

        // 获取intent信息
        String attn = getIntent().getStringExtra("attn");
        String mobile = getIntent().getStringExtra("mobile");
        apply_oa_contacts.setText(attn);
        apply_oa_mobile.setText(mobile);

        // 获取缴费的服务id
        EmpowermentManager.getInstance().UnpaidDate(this, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                Gson gson = new Gson();
                OpenNumberBe order = gson.fromJson(result.toString(), OpenNumberBe.class);
                OpenNumber o = gson.fromJson(order.list.get(0).getRemark(), OpenNumber.class);
                service_id = order.list.get(0).getService_id();
                amount = order.list.get(0).getAmount();
                service_name = order.list.get(0).getService_name();
                apply_oa_approve_money.setText(o.getWechar_authentication_amount());
                apply_oa_agency_money.setText(o.getAgency_amount());
                service_type = order.list.get(0).getService_type();
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    @Override
    protected void initListener() {
        apply_oa_to_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_oa_to_pay:// 发起缴费
                Intent i = new Intent();
                i.putExtra("service_id", service_id);
                i.putExtra("amount", amount);
                i.putExtra("service_name", service_name);
                i.putExtra("service_type", service_type);
                i.setClass(ApplyOAActivity.this, VdianPaymentActivity.class);
                startActivity(i);
                break;
        }
    }
}
