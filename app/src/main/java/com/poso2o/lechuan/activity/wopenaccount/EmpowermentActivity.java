package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class EmpowermentActivity  extends BaseActivity implements View.OnClickListener{
    private LinearLayout lv_wopen_rabuty,lv_wopen_rabutz;
    private ImageView rb_wopen_z,rb_wopen_y;
    private TextView tv_wopen_lz,tv_wopen_hz,tv_wopen_ly,tv_wopen_hy;
    private ImageView iv_back;
    private TextView tv_title;
    private View rv_wopen_trim;

    private Context context;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_account_empowerment;
    }

    @Override
    protected void initView() {
        context=this;
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_wopen_lz=(TextView)findViewById(R.id.tv_wopen_lz);
        tv_wopen_hz=(TextView)findViewById(R.id.tv_wopen_hz);
        tv_wopen_ly=(TextView)findViewById(R.id.tv_wopen_ly);
        tv_wopen_hy=(TextView)findViewById(R.id.tv_wopen_hy);
        lv_wopen_rabutz=(LinearLayout)findViewById(R.id.lv_wopen_rabutz);
        lv_wopen_rabuty=(LinearLayout)findViewById(R.id.lv_wopen_rabuty);
        rb_wopen_z=(ImageView)findViewById(R.id.rb_wopen_z);
        rb_wopen_y=(ImageView)findViewById(R.id.rb_wopen_y);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        rv_wopen_trim=(View)findViewById(R.id.rv_wopen_trim);
    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.wopen_shouquan));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));
    }

    @Override
    protected void initListener() {
        lv_wopen_rabutz.setOnClickListener(this);
        lv_wopen_rabuty.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rv_wopen_trim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.lv_wopen_rabutz:

                lv_wopen_rabutz.setBackgroundResource(R.drawable.green_stroke_bg);
                lv_wopen_rabuty.setBackgroundResource(R.drawable.gray_stroke_bg);
                rb_wopen_z.setImageResource(R.drawable.selected);
                rb_wopen_y.setImageResource(R.drawable.notselected);
                tv_wopen_lz.setTextColor(getResources().getColor(R.color.colorMain));
                tv_wopen_hz.setTextColor(getResources().getColor(R.color.placeholder_70));

                tv_wopen_ly.setTextColor(getResources().getColor(R.color.textGray));
                tv_wopen_hy.setTextColor(getResources().getColor(R.color.placeholder_30));

                break;
            case R.id.lv_wopen_rabuty:

                lv_wopen_rabutz.setBackgroundResource(R.drawable.gray_stroke_bg);
                lv_wopen_rabuty.setBackgroundResource(R.drawable.green_stroke_bg);
                rb_wopen_z.setImageResource(R.drawable.notselected);
                rb_wopen_y.setImageResource(R.drawable.selected);

                tv_wopen_ly.setTextColor(getResources().getColor(R.color.colorMain));
                tv_wopen_hy.setTextColor(getResources().getColor(R.color.placeholder_70));

                tv_wopen_lz.setTextColor(getResources().getColor(R.color.textGray));
                tv_wopen_hz.setTextColor(getResources().getColor(R.color.placeholder_30));

                break;
            case R.id.rv_wopen_trim:
                Intent i=new Intent();
                i.setClass(EmpowermentActivity.this,ServiceOrderingTrialActivity.class);
                startActivity(i);
                break;

        }
    }
}
