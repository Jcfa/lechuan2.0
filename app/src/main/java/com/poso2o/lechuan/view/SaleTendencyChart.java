package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.main_menu.SaleTendencyData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by mr zhang on 2017/8/10.
 * 销售趋势柱状图
 */

public class SaleTendencyChart extends BaseView implements View.OnClickListener{

    private View view;
    private Context context;
    //销售趋势曲线图容器
    private RelativeLayout chart_sale_tendency;

    //当前展示的图表数据分类，1为日，2为月，3为年,默认是日
    private int dataType = 1;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public SaleTendencyChart(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View initGroupView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_sale_tendency,null,false);
        return view;
    }

    @Override
    public void initView() {
        chart_sale_tendency = (RelativeLayout) view.findViewById(R.id.chart_sale_tendency);
    }

    @Override
    public void initData() {
        requestChartDay();
    }

    @Override
    public void initListenner() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    private void requestChartDay(){
        dataType = 1;
        //模拟数据
        ArrayList<SaleTendencyData> tendencyDatas = new ArrayList<>();

        SaleTendencyData tendencyData = new SaleTendencyData();
        tendencyData.amount = 20000;
        tendencyData.cost = 8000;
        tendencyData.profit = 10000;
        tendencyData.id = "20";

        SaleTendencyData tendencyData1 = new SaleTendencyData();
        tendencyData1.amount = 20800;
        tendencyData1.cost = 7000;
        tendencyData1.profit = 12000;
        tendencyData1.id = "21";

        SaleTendencyData tendencyData2 = new SaleTendencyData();
        tendencyData2.amount = 18000;
        tendencyData2.cost = 6000;
        tendencyData2.profit = 9000;
        tendencyData2.id = "22";

        SaleTendencyData tendencyData3 = new SaleTendencyData();
        tendencyData3.amount = 50000;
        tendencyData3.cost = 18000;
        tendencyData3.profit = 20000;
        tendencyData3.id = "23";

        SaleTendencyData tendencyData4 = new SaleTendencyData();
        tendencyData4.amount = 120000;
        tendencyData4.cost = 30000;
        tendencyData4.profit = 60000;
        tendencyData4.id = "24";

        SaleTendencyData tendencyData5 = new SaleTendencyData();
        tendencyData5.amount = 40009;
        tendencyData5.cost = 18000;
        tendencyData5.profit = 13056;
        tendencyData5.id = "25";

        SaleTendencyData tendencyData6 = new SaleTendencyData();
        tendencyData6.amount = 10000;
        tendencyData6.cost = 6000;
        tendencyData6.profit = 2000;
        tendencyData6.id = "26";

        SaleTendencyData tendencyData7 = new SaleTendencyData();
        tendencyData7.amount = 8000;
        tendencyData7.cost = 12000;
        tendencyData7.profit = -16000;
        tendencyData7.id = "27";

        tendencyDatas.add(tendencyData);
        tendencyDatas.add(tendencyData1);
        tendencyDatas.add(tendencyData2);
        tendencyDatas.add(tendencyData3);
        tendencyDatas.add(tendencyData4);
        tendencyDatas.add(tendencyData5);
        tendencyDatas.add(tendencyData6);
        tendencyDatas.add(tendencyData7);

        sortDayData(tendencyDatas);

        /*ChartManager.getChartManager().saleTendencyDayChart((BaseActivity) context, branch_id, "", "", new ChartManager.OnSaleTendencyDayListener() {
            @Override
            public void onSaleTendencyDaySuccess(BaseActivity baseActivity, AllSaleTendencyData allSaleTendencyData) {
                sortDayData(allSaleTendencyData.list);
            }

            @Override
            public void onSaleTendencyDayFail(BaseActivity baseActivity, String failStr, boolean fromServer) {
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    private void requestChartMonth(){
        dataType = 2;
        /*ChartManager.getChartManager().saleTendencyMonthChart((BaseActivity) context, branch_id, "", "", new ChartManager.OnSaleTendencyMonthListener() {
            @Override
            public void onSaleTendencyMonthSuccess(BaseActivity baseActivity,AllSaleTendencyData allSaleTendencyData) {
                sortMonthData(allSaleTendencyData.list);
            }

            @Override
            public void onSaleTendencyMonthFail(BaseActivity baseActivity, String failStr, boolean fromServer) {
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    private void requestChartYear(){
        dataType = 3;
        /*ChartManager.getChartManager().saleTendencyYearChart((BaseActivity) context, branch_id, "", "", new ChartManager.OnSaleTendencyYearListener() {
            @Override
            public void onSaleTendencyYearSuccess(BaseActivity baseActivity, AllSaleTendencyData allSaleTendencyData) {
                sortMonthData(allSaleTendencyData.list);
            }

            @Override
            public void onSaleTendencyYearFail(BaseActivity baseActivity, String failStr, boolean fromServer) {
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    private void sortDayData(ArrayList<SaleTendencyData> list){
        if (list == null)return;

        List<PointValue> sales = new ArrayList<>();
        List<PointValue> costs = new ArrayList<>();
        List<PointValue> profits = new ArrayList<>();

        List<AxisValue> axisXValueLine = new ArrayList<>();

        for (int i=0 ; i<list.size(); i++){
            sales.add(new PointValue(i,doubleToInt(list.get(i).amount)));
            costs.add(new PointValue(i,doubleToInt(list.get(i).cost)));
            profits.add(new PointValue(i,doubleToInt(list.get(i).profit)));
            String str = list.get(i).id;
            str = str.substring(str.indexOf("-") + 1,str.length());
            axisXValueLine.add(new AxisValue(i,str.toCharArray()));
        }

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(sales).setColor(0xFF882211).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));
        lines.add(new Line(costs).setColor(0xFFFF6537).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));
        lines.add(new Line(profits).setColor(0xFF00BCB4).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));

        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);
        lineChartData.setBaseValue(Float.NEGATIVE_INFINITY);

        LineChartView sale_tendency_chart = new LineChartView(context);

        Axis axisX = new Axis();
        axisX.setTextSize(10);
        axisX.setValues(axisXValueLine);
        axisX.setHasTiltedLabels(false);
        axisX.setHasLines(true);
        axisX.setLineColor(0xFFAAAAAA);
        axisX.setMaxLabelChars(2);
        lineChartData.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setName(".");
        axisY.setTextSize(10);
        axisY.setHasLines(true);
        axisY.setLineColor(0xFFAAAAAA);
        lineChartData.setAxisYLeft(axisY);

        Axis axisYR = new Axis();
        axisYR.setName(".");
        axisYR.setTextSize(10);
        axisYR.setHasLines(true);
        axisYR.setLineColor(0xFFAAAAAA);
        lineChartData.setAxisYRight(axisYR);

        sale_tendency_chart.setInteractive(false);
        sale_tendency_chart.setLineChartData(lineChartData);

        chart_sale_tendency.removeAllViews();
        chart_sale_tendency.addView(sale_tendency_chart);

    }

    private void sortMonthData(ArrayList<SaleTendencyData> list){
        if (list == null)return;

        List<PointValue> sales = new ArrayList<>();
        List<PointValue> costs = new ArrayList<>();
        List<PointValue> profits = new ArrayList<>();

        List<AxisValue> axisXValueLine = new ArrayList<>();

        for (int i=0 ; i<list.size(); i++){
            sales.add(new PointValue(i,doubleToInt(list.get(i).amount)));
            costs.add(new PointValue(i,doubleToInt(list.get(i).cost)));
            profits.add(new PointValue(i,doubleToInt(list.get(i).profit)));
            axisXValueLine.add(new AxisValue(i,list.get(i).id.toCharArray()));
        }

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(sales).setColor(0xFF882211).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));
        lines.add(new Line(costs).setColor(0xFFFF6537).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));
        lines.add(new Line(profits).setColor(0xFF00BCB4).setCubic(true).setStrokeWidth(2).setHasPoints(false).setHasLabels(true));

        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);
        lineChartData.setBaseValue(Float.NEGATIVE_INFINITY);

        LineChartView sale_tendency_chart = new LineChartView(context);

        Axis axisX = new Axis();
        axisX.setTextSize(10);
        axisX.setValues(axisXValueLine);
        axisX.setHasTiltedLabels(false);
        axisX.setHasLines(true);
        axisX.setLineColor(0xFFAAAAAA);
        lineChartData.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setName(".");
        axisY.setTextSize(10);
        axisY.setHasLines(true);
        axisY.setLineColor(0xFFAAAAAA);
        lineChartData.setAxisYLeft(axisY);

        sale_tendency_chart.setInteractive(false);
        sale_tendency_chart.setLineChartData(lineChartData);

        chart_sale_tendency.removeAllViews();
        chart_sale_tendency.addView(sale_tendency_chart);
    }

    private int doubleToInt(double num){
        String str = num + "";
        str = str.substring(0,str.indexOf("."));
        return Integer.parseInt(str);
    }

    //刷新数据
    public void refreshData(){
        if (view.getVisibility() != View.VISIBLE)return;
        if (dataType == 1){
            requestChartDay();
        }else if (dataType == 2){
            requestChartMonth();
        }else {
            requestChartYear();
        }
    }
}
