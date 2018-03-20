package com.poso2o.lechuan.activity.wopenaccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenStandBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.ServiceOrderinTrialManager;

/**
 * 开通授权界面
 * <p>
 * Created by Administrator on 2018/3/13 0013.
 */
public class EmpowermentActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout lv_wopen_rabuty, lv_wopen_rabutz;
    private ImageView rb_wopen_z, rb_wopen_y;
    private TextView tv_wopen_lz, tv_wopen_hz, tv_wopen_ly, tv_wopen_hy;
    private ImageView iv_back;
    private TextView tv_title;
    //步骤的点击
    private View rv_wopen_trim, rv_wopen_empo_auth, rv_wopen_empo_number;
    private TextView tv_wopen_empo_two, tv_wopen_empo_three, tv_wopen_empo_auth;
    //开通状态
    private TextView tv_wopen_wei_stand;
    private int state;
    //联系方式
    private String attn, mobile;
    //服务信息
    private String service_name, amount, payment_time;

    private Context context;
    public static Activity eactiviyt;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_account_empowerment;
    }

    @Override
    protected void initView() {
        context = this;
        eactiviyt = this;
        tv_title = findView(R.id.tv_title);
        tv_wopen_lz = findView(R.id.tv_wopen_lz);
        tv_wopen_hz = findView(R.id.tv_wopen_hz);
        tv_wopen_ly = findView(R.id.tv_wopen_ly);
        tv_wopen_hy = findView(R.id.tv_wopen_hy);
        lv_wopen_rabutz = findView(R.id.lv_wopen_rabutz);
        lv_wopen_rabuty = findView(R.id.lv_wopen_rabuty);
        rb_wopen_z = findView(R.id.rb_wopen_z);
        rb_wopen_y = findView(R.id.rb_wopen_y);
        iv_back = findView(R.id.iv_back);
        rv_wopen_trim = findView(R.id.rv_wopen_trim);
        rv_wopen_empo_auth = findView(R.id.rv_wopen_empo_auth);
        rv_wopen_empo_number = findView(R.id.rv_wopen_empo_number);
        tv_wopen_empo_two = findView(R.id.tv_wopen_empo_two);
        tv_wopen_empo_three = findView(R.id.tv_wopen_empo_three);
        tv_wopen_empo_auth = findView(R.id.tv_wopen_empo_auth);
        tv_wopen_wei_stand = findView(R.id.tv_wopen_wei_stand);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.wopen_shouquan));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));

        load();
    }

    public void load() {
        //获取开通状态
        ServiceOrderinTrialManager manager = new ServiceOrderinTrialManager();
        manager.OpenStateDate(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                Gson gson = new Gson();
                OpenStandBean osb = gson.fromJson(result.toString(), OpenStandBean.class);
                attn = osb.getAttn();
                mobile = osb.getMobile();
                service_name = osb.getService_name();
                amount = osb.getAmount();
                payment_time = osb.getPayment_time();
                if (Integer.valueOf(osb.getState()) != null) {
                    //点击无公众号
                    lv_wopen_rabutz.setBackgroundResource(R.drawable.gray_stroke_bg);
                    lv_wopen_rabuty.setBackgroundResource(R.drawable.green_stroke_bg);
                    rb_wopen_z.setImageResource(R.drawable.notselected);
                    rb_wopen_y.setImageResource(R.drawable.selected);

                    tv_wopen_ly.setTextColor(getResources().getColor(R.color.colorMain));
                    tv_wopen_hy.setTextColor(getResources().getColor(R.color.placeholder_70));

                    tv_wopen_lz.setTextColor(getResources().getColor(R.color.textGray));
                    tv_wopen_hz.setTextColor(getResources().getColor(R.color.placeholder_30));

                    rv_wopen_empo_number.setVisibility(View.VISIBLE);
                    tv_wopen_empo_two.setText("步骤二：");
                    tv_wopen_empo_three.setText("步骤三：");
                    rv_wopen_empo_auth.setBackgroundColor(getResources().getColor(R.color.common_background));
//                    if (Integer.valueOf(osb.getState())==0){
//                        state=Integer.valueOf(osb.getState());
//                        tv_wopen_wei_stand.setText("去开通");
//                        rv_wopen_empo_auth.setClickable(false);
//                        rv_wopen_trim.setClickable(false);
//                    }else if (Integer.valueOf(osb.getState())==1){
//                        state=Integer.valueOf(osb.getState());
//                        tv_wopen_wei_stand.setText("已缴费，待开通");
//                        rv_wopen_empo_auth.setClickable(false);
//                        rv_wopen_trim.setClickable(false);
//                    }else if (Integer.valueOf(osb.getState())==2){
//                        state=Integer.valueOf(osb.getState());
//                        tv_wopen_wei_stand.setText("已开通");
//                        rv_wopen_empo_auth.setClickable(true);
//                        rv_wopen_trim.setClickable(false);
//                    }
                }

            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }


    @Override
    protected void initListener() {
        lv_wopen_rabutz.setOnClickListener(this);
        lv_wopen_rabuty.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rv_wopen_trim.setOnClickListener(this);
        rv_wopen_empo_auth.setOnClickListener(this);
        rv_wopen_empo_number.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.lv_wopen_rabutz:

                //点击有公众号
                lv_wopen_rabutz.setBackgroundResource(R.drawable.green_stroke_bg);
                lv_wopen_rabuty.setBackgroundResource(R.drawable.gray_stroke_bg);
                rb_wopen_z.setImageResource(R.drawable.selected);
                rb_wopen_y.setImageResource(R.drawable.notselected);
                tv_wopen_lz.setTextColor(getResources().getColor(R.color.colorMain));
                tv_wopen_hz.setTextColor(getResources().getColor(R.color.placeholder_70));

                tv_wopen_ly.setTextColor(getResources().getColor(R.color.textGray));
                tv_wopen_hy.setTextColor(getResources().getColor(R.color.placeholder_30));
                rv_wopen_empo_number.setVisibility(View.GONE);
                tv_wopen_empo_two.setText("步骤一：");
                tv_wopen_empo_three.setText("步骤二：");
                rv_wopen_empo_auth.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                break;
            case R.id.lv_wopen_rabuty:

                //点击无公众号
                lv_wopen_rabutz.setBackgroundResource(R.drawable.gray_stroke_bg);
                lv_wopen_rabuty.setBackgroundResource(R.drawable.green_stroke_bg);
                rb_wopen_z.setImageResource(R.drawable.notselected);
                rb_wopen_y.setImageResource(R.drawable.selected);

                tv_wopen_ly.setTextColor(getResources().getColor(R.color.colorMain));
                tv_wopen_hy.setTextColor(getResources().getColor(R.color.placeholder_70));

                tv_wopen_lz.setTextColor(getResources().getColor(R.color.textGray));
                tv_wopen_hz.setTextColor(getResources().getColor(R.color.placeholder_30));

                rv_wopen_empo_number.setVisibility(View.VISIBLE);
                tv_wopen_empo_two.setText("步骤二：");
                tv_wopen_empo_three.setText("步骤三：");
                rv_wopen_empo_auth.setBackgroundColor(getResources().getColor(R.color.common_background));

                break;
            case R.id.rv_wopen_trim:
                //进入服务订购/试用
                Intent i = new Intent();
                i.setClass(EmpowermentActivity.this, ServiceOrderingTrialActivity.class);
                startActivity(i);
                break;
            case R.id.rv_wopen_empo_auth:
                //进入授权说明
                Intent ia = new Intent();
                ia.setClass(EmpowermentActivity.this, AuthorizationActivity.class);
                startActivity(ia);
                break;
            case R.id.rv_wopen_empo_number:

                if (state == 0) {
                    //进入未开通状态公众号
                    Intent in = new Intent();
                    in.putExtra("attn", attn);
                    in.putExtra("mobile", mobile);
                    in.setClass(EmpowermentActivity.this, OpenNumberActivity.class);
                    startActivity(in);
                } else if (state == 1) {
                    //进入已缴费，待开通状态公众号
                    Intent in = new Intent();
                    in.putExtra("amount", amount);
                    in.putExtra("service_name", service_name);
                    in.putExtra("payment_time", payment_time);
                    in.putExtra("attn", attn);
                    in.putExtra("mobile", mobile);
                    in.setClass(EmpowermentActivity.this, StardOpenActivity.class);
                    startActivity(in);
                } else if (state == 2) {
                    //已开通状态
                    rv_wopen_empo_number.setClickable(false);
                }
                break;

        }
    }
}
