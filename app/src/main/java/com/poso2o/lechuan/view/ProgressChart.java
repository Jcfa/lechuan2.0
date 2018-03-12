package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;

/**
 * Created by mr zhang on 2017/8/10.
 * 进度标尺
 */

public class ProgressChart extends BaseView implements View.OnClickListener {

    private View view ;
    private Context context;

    //标尺容器
    private RelativeLayout chart_progress;
    //标尺
    private MyProgressBar progressBar;

    //销售
    private TextView ruler_sale;
    //目标
    private TextView ruler_target;
    //保本
    private TextView ruler_bottom_money;
    //完成
    private TextView ruler_rate;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public ProgressChart(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View initGroupView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_progress,null,false);
        return view;
    }

    @Override
    public void initView() {
        chart_progress = (RelativeLayout) view.findViewById(R.id.chart_progress);
        ruler_sale = (TextView) view.findViewById(R.id.ruler_sale);
        ruler_target = (TextView) view.findViewById(R.id.ruler_target);
        ruler_bottom_money = (TextView) view.findViewById(R.id.ruler_bottom_money);
        ruler_rate = (TextView) view.findViewById(R.id.ruler_rate);
    }

    @Override
    public void initData() {
        requestData();
    }

    @Override
    public void initListenner() {
    }

    @Override
    public void onClick(View view) {
    }

    private void requestData(){
        progressBar = new MyProgressBar(context);
        progressBar.setValue(8506,100000,123062,120.26);
        chart_progress.removeAllViews();
        chart_progress.addView(progressBar);
        ruler_sale.setText("123062");
        ruler_target.setText("100000");
        ruler_bottom_money.setText("8506");
        ruler_rate.setText("120.26%");
        /*SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);
        String branch_id = sharedPreferencesUtil.getString(LoginMemory.SHOP_BRANCH_ID);
        ChartManager.getChartManager().progressRuler((BaseActivity) context, branch_id, CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00"), new ChartManager.OnProgressRulerListener() {
            @Override
            public void progressRulerSuccess(BaseActivity baseActivity, ProgressData progressData) {
                if (progressData == null)return;
                progressBar = new MyProgressBar(context);
                progressBar.setValue(progressData.sale_cost,progressData.assignments,progressData.sale_amounts,progressData.completion_rate);
                chart_progress.removeAllViews();
                chart_progress.addView(progressBar);
                ruler_sale.setText(NumberFormatUtils.format(progressData.sale_amounts));
                ruler_target.setText(NumberFormatUtils.format(progressData.assignments));
                ruler_bottom_money.setText(NumberFormatUtils.format(progressData.sale_cost));
                ruler_rate.setText(NumberFormatUtils.format(progressData.completion_rate) + "%");
            }

            @Override
            public void progressRulerFail(BaseActivity baseActivity, String failStr, boolean fromServer) {
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

}
