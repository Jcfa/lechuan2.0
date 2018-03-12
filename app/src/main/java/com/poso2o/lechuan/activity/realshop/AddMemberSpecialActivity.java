package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.marketdata.MemberBirthdayData;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/11/8.
 *
 * 添加会员生日优惠
 */

public class AddMemberSpecialActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回
    private ImageView add_m_member_back;
    //确定
    private Button add_m_member_ok;

    //活动名称
    private EditText member_special_name;
    //活动时间
    private EditText member_special_date;
    //活动优惠折扣
    private EditText member_special_discount;

//    private SharedPreferencesUtil sharedPreferencesUtil ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_m_member;
    }

    @Override
    public void initView() {
        context = this;

        add_m_member_back = (ImageView) findViewById(R.id.add_m_member_back);
        add_m_member_ok = (Button) findViewById(R.id.add_m_member_ok);

        member_special_name = (EditText) findViewById(R.id.member_special_name);
        member_special_date = (EditText) findViewById(R.id.member_special_date);
        member_special_discount = (EditText) findViewById(R.id.member_special_discount);

        add_m_member_ok.setSelected(true); 
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        MemberBirthdayData birthdayData = (MemberBirthdayData) bundle.get(RMarketActivity.MEMBER_BIRTHDAY_INFO);
        if (birthdayData != null){
            member_special_name.setText(birthdayData.promotion_name);
            member_special_date.setText(birthdayData.days);
            member_special_discount.setText(birthdayData.member_discount);
        }
    }

    @Override
    public void initListener() {
        add_m_member_back.setOnClickListener(this);
        add_m_member_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_m_member_back:
                finish();
                break;
            case R.id.add_m_member_ok:
                saveDatas();
                break;
        }
    }

    private void saveDatas(){
        String name = member_special_name.getText().toString();
        if (name == null || name.equals("")){
            Toast.show(context,"请输入活动名称");
            member_special_name.requestFocus();
            return;
        }
        String days = member_special_date.getText().toString();
        if (days == null || days.equals("")){
            Toast.show(context,"请输入活动天数");
            member_special_date.requestFocus();
            return;
        }
        String discount = member_special_discount.getText().toString();
        if (discount == null || discount.equals("")){
            Toast.show(context,"请输入会员生日折扣");
            member_special_discount.requestFocus();
            return;
        }
        /*showReadDialog();
        MemberBirthdayManager.getBirthdayManager().saveMemBirthday(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), name, days, discount, new MemberBirthdayManager.OnSaveMemBirthdayListener() {
            @Override
            public void onSaveBirthdaySuc(BaseActivity baseActivity) {
                dismissReadDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onSaveBirthdayFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(context,failStr).show();
            }
        });*/
    }
}
