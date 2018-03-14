package com.poso2o.lechuan.activity.wopenaccount;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class ServiceOrderingTrialActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private View rv_wopen_trial_gao,rv_wopen_trial_gs,rv_wopen_trial_gz;
    private RadioButton rb_wopen_trial_one,rb_wopen_trial_two,rb_wopen_trial_three;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_service_ordering_trial;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        rv_wopen_trial_gao=(View)findViewById(R.id.rv_wopen_trial_gao);
        rv_wopen_trial_gs=(View)findViewById(R.id.rv_wopen_trial_gs);
        rv_wopen_trial_gz=(View)findViewById(R.id.rv_wopen_trial_gz);
        rb_wopen_trial_one=(RadioButton)findViewById(R.id.rb_wopen_trial_one);
        rb_wopen_trial_two=(RadioButton)findViewById(R.id.rb_wopen_trial_two);
        rb_wopen_trial_three=(RadioButton)findViewById(R.id.rb_wopen_trial_three);

    }

    @Override
    protected void initData() {
        tv_title.setText(getResources().getString(R.string.service_orderint_trial));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));
    }

    @Override
    protected void initListener() {
        rv_wopen_trial_gao.setOnClickListener(this);
        rv_wopen_trial_gs.setOnClickListener(this);
        rv_wopen_trial_gz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rv_wopen_trial_gao:
                rb_wopen_trial_one.setChecked(true);
                rb_wopen_trial_two.setChecked(false);
                rb_wopen_trial_three.setChecked(false);
                break;
            case R.id.rv_wopen_trial_gs:
                rb_wopen_trial_one.setChecked(false);
                rb_wopen_trial_two.setChecked(true);
                rb_wopen_trial_three.setChecked(false);
                break;
            case R.id.rv_wopen_trial_gz:
                rb_wopen_trial_one.setChecked(false);
                rb_wopen_trial_two.setChecked(false);
                rb_wopen_trial_three.setChecked(true);
                break;
        }
    }
}
