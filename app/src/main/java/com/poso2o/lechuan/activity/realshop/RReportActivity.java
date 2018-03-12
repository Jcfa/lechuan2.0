package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.bosstotal.BossTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.ProgressChart;
import com.poso2o.lechuan.view.SaleTendencyChart;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RReportActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回
    private ImageView r_report_back;

    //销售额
    private RelativeLayout r_report_sale_amount;
    private TextView total_sale_amount;
    //完成率
    private RelativeLayout r_report_finish_rate;
    private TextView total_finish;
    //毛利润
    private RelativeLayout r_report_profit;
    private TextView total_profit;

    //视图容器
    private LinearLayout chart_view_root;
    //销售趋势图
    private SaleTendencyChart saleTendencyChart;
    //进度标尺图
    private ProgressChart progressChart;

    //交接明细
    private RelativeLayout r_report_transfer_layout;
    private TextView report_total_transfer;
    //商品库存
    private RelativeLayout r_report_stock_layout;
    private TextView report_total_stock;
    //月损益表
    private RelativeLayout r_report_profit_layout;
    private TextView report_total_profit;
    //人员业绩
    private RelativeLayout r_report_performance_layout;
    private TextView report_total_achieve;
    //热销排行
    private RelativeLayout r_report_hot_layout;
    private TextView report_total_hot;

    //是否微店
    private boolean is_online;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_r_report;
    }

    @Override
    protected void initView() {
        context = this;

        r_report_back = (ImageView) findViewById(R.id.r_report_back);

        r_report_sale_amount = (RelativeLayout) findViewById(R.id.r_report_sale_amount);
        total_sale_amount = (TextView) findViewById(R.id.total_sale_amount);

        r_report_finish_rate = (RelativeLayout) findViewById(R.id.r_report_finish_rate);
        total_finish = (TextView) findViewById(R.id.total_finish);

        r_report_profit = (RelativeLayout) findViewById(R.id.r_report_profit);
        total_profit = (TextView) findViewById(R.id.total_profit);

        chart_view_root = (LinearLayout) findViewById(R.id.chart_view_root);

        r_report_transfer_layout = (RelativeLayout) findViewById(R.id.r_report_transfer_layout);
        r_report_stock_layout = (RelativeLayout) findViewById(R.id.r_report_stock_layout);
        r_report_profit_layout = (RelativeLayout) findViewById(R.id.r_report_profit_layout);
        r_report_performance_layout = (RelativeLayout) findViewById(R.id.r_report_performance_layout);
        r_report_hot_layout = (RelativeLayout) findViewById(R.id.r_report_hot_layout);

        report_total_transfer = (TextView) findViewById(R.id.report_total_transfer);
        report_total_stock = (TextView) findViewById(R.id.report_total_stock);
        report_total_profit = (TextView) findViewById(R.id.report_total_profit);
        report_total_achieve = (TextView) findViewById(R.id.report_total_achieve);
        report_total_hot = (TextView) findViewById(R.id.report_total_hot);
    }

    @Override
    protected void initData() {
        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);
        saleTendencyChart = new SaleTendencyChart(context);
        progressChart = new ProgressChart(context);

        initTotalData();
    }

    @Override
    protected void initListener() {
        r_report_back.setOnClickListener(this);

        r_report_sale_amount.setOnClickListener(this);
        r_report_finish_rate.setOnClickListener(this);
        r_report_profit.setOnClickListener(this);

        r_report_transfer_layout.setOnClickListener(this);
        r_report_stock_layout.setOnClickListener(this);
        r_report_profit_layout.setOnClickListener(this);
        r_report_performance_layout.setOnClickListener(this);
        r_report_hot_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_report_back:
                finish();
                break;
            case R.id.r_report_sale_amount:
                r_report_finish_rate.setSelected(false);
                r_report_profit.setSelected(false);
                if (r_report_sale_amount.isSelected()){
                    chart_view_root.setVisibility(View.GONE);
                    r_report_sale_amount.setSelected(false);
                }else {
                    r_report_sale_amount.setSelected(true);
                    chart_view_root.setVisibility(View.VISIBLE);
                    chart_view_root.removeAllViews();
                    chart_view_root.addView(saleTendencyChart.getRootView());
                }
                break;
            case R.id.r_report_finish_rate:
                r_report_sale_amount.setSelected(false);
                r_report_profit.setSelected(false);
                if (r_report_finish_rate.isSelected()){
                    r_report_finish_rate.setSelected(false);
                    chart_view_root.setVisibility(View.GONE);
                }else {
                    r_report_finish_rate.setSelected(true);
                    chart_view_root.setVisibility(View.VISIBLE);
                    chart_view_root.removeAllViews();
                    chart_view_root.addView(progressChart.getRootView());
                }
                break;
            case R.id.r_report_profit:
                r_report_sale_amount.setSelected(false);
                r_report_finish_rate.setSelected(false);
                if (r_report_profit.isSelected()){
                    r_report_profit.setSelected(false);
                    chart_view_root.setVisibility(View.GONE);
                }else {
                    r_report_profit.setSelected(true);
                    chart_view_root.setVisibility(View.VISIBLE);
                    chart_view_root.removeAllViews();
                    chart_view_root.addView(saleTendencyChart.getRootView());
                }
                break;
            case R.id.r_report_transfer_layout:
                Intent report = new Intent();
                report.setClass(context,RTransferActivity.class);
                startActivity(report);
                break;
            case R.id.r_report_stock_layout:
                Intent stock = new Intent();
                stock.setClass(context,RStockActivity.class);
                startActivity(stock);
                break;
            case R.id.r_report_profit_layout:
                Intent profit = new Intent();
                profit.setClass(context,RMonthProfitActivity.class);
                startActivity(profit);
                break;
            case R.id.r_report_performance_layout:
                Intent achieve = new Intent();
                achieve.setClass(context,RAchievementActivity.class);
                startActivity(achieve);
                break;
            case R.id.r_report_hot_layout:
                Intent hotSale = new Intent();
                hotSale.setClass(context,RHotSaleActivity.class);
                startActivity(hotSale);
                break;
        }
    }

    private void initTotalData(){
        showLoading("正在加载数据...");
        if (is_online){

            r_report_transfer_layout.setVisibility(View.GONE);
            r_report_performance_layout.setVisibility(View.GONE);

            WReportManager.getwReportManager().wBossTotal(this, "", "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    BossTotalBean bossTotalBean = (BossTotalBean) object;
                    if (bossTotalBean == null)return;
                    total_sale_amount.setText(NumberFormatUtils.format(bossTotalBean.sale_amounts));
                    total_finish.setText(bossTotalBean.completion_rate + "%");
                    total_profit.setText(bossTotalBean.gross_profit + "");

                    report_total_transfer.setText(NumberFormatUtils.format(bossTotalBean.sale_amounts));
                    report_total_stock.setText(bossTotalBean.total_goods_number);
                    report_total_profit.setText(NumberFormatUtils.format(bossTotalBean.gross_profit));
                    report_total_achieve.setText(NumberFormatUtils.format(bossTotalBean.completion_rate) + "%");
                    report_total_hot.setText(bossTotalBean.today_sales_number + "件");
                }
            });
        }else {
            RReportManager.getRReportManger().totalReport(this,new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    BossTotalBean bossTotalBean = (BossTotalBean) object;
                    if (bossTotalBean == null)return;
                    total_sale_amount.setText(NumberFormatUtils.format(bossTotalBean.order_amounts));
                    total_finish.setText(NumberFormatUtils.format(bossTotalBean.completion_rate) + "%");
                    total_profit.setText(NumberFormatUtils.format(bossTotalBean.gross_profit) + "");

                    report_total_transfer.setText(NumberFormatUtils.format(bossTotalBean.order_amounts));
                    report_total_stock.setText(bossTotalBean.total_goods_number);
                    report_total_profit.setText(NumberFormatUtils.format(bossTotalBean.gross_profit));
                    report_total_achieve.setText(NumberFormatUtils.format(bossTotalBean.completion_rate) + "%");
                    report_total_hot.setText(bossTotalBean.today_sales_number + " 件");
                }
            });
        }
    }
}
