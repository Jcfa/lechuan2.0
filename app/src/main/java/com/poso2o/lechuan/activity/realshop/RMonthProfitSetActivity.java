package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.monthprofit.MonthProfitData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/8/5.
 * 月度消费设置页
 */

public class RMonthProfitSetActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //月度消费设置页跳转请求码
    public static final int MONTH_PROFIT_REQUEST_CODE = 1002;
    //月度消费设置页数据
    public static final String MONTH_PROFIT_DATA = "month_profit_data";

    //返回
    private ImageView month_profit_set_back;
    //确定按钮
    private Button month_profit_set_confirm;
    //铺租
    private EditText value_profit_rent;
    //水电
    private EditText value_profit_water;
    //管理费
    private EditText value_profit_manager;
    //税收
    private EditText value_profit_tax;
    //工资
    private EditText value_profit_salary;
    //提成
    private EditText value_profit_percentage;
    //杂费
    private EditText value_profit_anther_fee;
    //推广
    private EditText value_profit_spread;

    //月度消费数据
    private MonthProfitData monthProfitData;

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_month_profit_set;
    }

    @Override
    public void initView() {

        context = this;

        month_profit_set_back = (ImageView) findViewById(R.id.month_profit_set_back);

        month_profit_set_confirm = (Button) findViewById(R.id.month_profit_set_confirm);

        value_profit_rent = (EditText) findViewById(R.id.value_profit_rent);

        value_profit_water = (EditText) findViewById(R.id.value_profit_water);

        value_profit_manager = (EditText) findViewById(R.id.value_profit_manager);

        value_profit_tax = (EditText) findViewById(R.id.value_profit_tax);

        value_profit_salary = (EditText) findViewById(R.id.value_profit_salary);

        value_profit_percentage = (EditText) findViewById(R.id.value_profit_percentage);

        value_profit_anther_fee = (EditText) findViewById(R.id.value_profit_anther_fee);

        value_profit_spread = (EditText) findViewById(R.id.value_profit_spread);
    }

    @Override
    public void initData() {
        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);
        monthProfitData = (MonthProfitData) getIntent().getExtras().get(MONTH_PROFIT_DATA);
        if (monthProfitData == null) return;
        if (is_online){
            value_profit_rent.setText(NumberFormatUtils.format(monthProfitData.shop_rent));
            value_profit_water.setText(NumberFormatUtils.format(monthProfitData.water_electricity));
            value_profit_manager.setText(NumberFormatUtils.format(monthProfitData.property_management_fee));
            value_profit_tax.setText(NumberFormatUtils.format(monthProfitData.tax));
            value_profit_salary.setText(NumberFormatUtils.format(monthProfitData.staff_wages));
            value_profit_percentage.setText(NumberFormatUtils.format(monthProfitData.staff_commmission));
            value_profit_anther_fee.setText(NumberFormatUtils.format(monthProfitData.other_expenses));
            value_profit_spread.setText(NumberFormatUtils.format(monthProfitData.promotion_fee));
        }else {
            requestRProfit();
        }
    }

    @Override
    public void initListener() {
        month_profit_set_back.setOnClickListener(this);
        month_profit_set_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.month_profit_set_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.month_profit_set_confirm:
                setMonthProfit();
                break;
        }
    }

    private void setMonthProfit(){
        showLoading("正在提交数据...");
        String rent = value_profit_rent.getText().toString();
        rent = (rent == null || rent.equals("")) ? "0" : rent;

        String we = value_profit_water.getText().toString();
        we = (we == null || we.equals("")) ? "0" : we;

        String pmf = value_profit_manager.getText().toString();
        pmf = (pmf == null || pmf.equals("")) ? "0" : pmf;

        String tax = value_profit_tax.getText().toString();
        tax = (tax == null || tax.equals("")) ? "0" : tax;

        String pf = value_profit_spread.getText().toString();
        pf = (pf == null || pf.equals("")) ? "0" : pf;

        String wages = value_profit_salary.getText().toString();
        wages = (wages == null || wages.equals("")) ? "0" : wages;

        String commission = value_profit_percentage.getText().toString();
        commission = (commission == null || commission.equals("")) ? "0" : commission;

        String otherfee = value_profit_anther_fee.getText().toString();
        otherfee = (otherfee == null || otherfee.equals("")) ? "0" : otherfee;

        if (is_online){
            MonthProfitData data = new MonthProfitData();
            data.months = monthProfitData.months;
            data.total_amount = monthProfitData.total_amount;
            data.total_cost = monthProfitData.total_cost;
            data.shop_rent = Double.parseDouble(rent);
            data.water_electricity = Double.parseDouble(we);
            data.property_management_fee = Double.parseDouble(pmf);
            data.tax = Double.parseDouble(tax);
            data.promotion_fee = Double.parseDouble(pf);
            data.staff_wages = Double.parseDouble(wages);
            data.staff_commmission = Double.parseDouble(commission);
            data.zs_cost = otherfee;
            WReportManager.getwReportManager().wMonthProfitSet(this, data, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }else {
            MonthProfitData data = new MonthProfitData();
            data.months = monthProfitData.months;
            data.total_amount = monthProfitData.total_amount;
            data.total_cost = monthProfitData.total_cost;
            data.zj_cost = rent;
            data.sd_cost = we;
            data.gl_cost = pmf;
            data.sl_cost = tax;
            data.tg_cost = pf;
            data.gz_cost = wages;
            data.tc_cost = commission;
            data.zs_cost = otherfee;

            RReportManager.getRReportManger().rMonthProfitSet(this, data, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    private void requestRProfit(){
        showLoading("正在加载数据...");
        RReportManager.getRReportManger().monthProfitInfo(this, monthProfitData.months, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                monthProfitData = (MonthProfitData) object;
                if (monthProfitData != null){
                    value_profit_rent.setText(NumberFormatUtils.format(monthProfitData.zj_cost));
                    value_profit_water.setText(NumberFormatUtils.format(monthProfitData.sd_cost));
                    value_profit_manager.setText(NumberFormatUtils.format(monthProfitData.gl_cost));
                    value_profit_tax.setText(NumberFormatUtils.format(monthProfitData.sl_cost));
                    value_profit_salary.setText(NumberFormatUtils.format(monthProfitData.gz_cost));
                    value_profit_percentage.setText(NumberFormatUtils.format(monthProfitData.tc_cost));
                    value_profit_anther_fee.setText(NumberFormatUtils.format(monthProfitData.zs_cost));
                    value_profit_spread.setText(NumberFormatUtils.format(monthProfitData.tg_cost));
                }
            }
        });
    }
}
