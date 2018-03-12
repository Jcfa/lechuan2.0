package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;

/**
 * Created by mr zhang on 2017/9/13.
 * 日历弹窗
 */

public class MainDateDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //背景
    private LinearLayout dialog_main_date_root;
    //日
    private TextView main_date_day;
    //周
    private TextView main_date_week;
    //月
    private TextView main_date_month;
    //自定义
    private TextView main_date_diy;
    //类型
    private int dateType;

    //初始化开始时间
    private String beginTime;
    //初始化结束时间
    private String endTime;

    public MainDateDialog(Context context,int type,String beginTime ,String endTime) {
        super(context);
        this.context = context;
        dateType = type;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_main_date,null);
        return view;
    }

    @Override
    public void initView() {

        dialog_main_date_root = (LinearLayout) view.findViewById(R.id.dialog_main_date_root);

        main_date_day = (TextView) view.findViewById(R.id.main_date_day);

        main_date_week = (TextView) view.findViewById(R.id.main_date_week);

        main_date_month = (TextView) view.findViewById(R.id.main_date_month);

        main_date_diy = (TextView) view.findViewById(R.id.main_date_diy);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(1,1);

        if (dateType == 1){
            main_date_day.setTextColor(0xFFFF6537);
        }else if (dateType == 2){
            main_date_week.setTextColor(0xFFFF6537);
        }else if (dateType == 3){
            main_date_month.setTextColor(0xFFFF6537);
        }else if (dateType == 4){
            main_date_diy.setTextColor(0xFFFF6537);
            main_date_diy.setText(DateTimeUtil.StringToData(beginTime,"yyyy-MM-dd") + "至" + DateTimeUtil.StringToData(endTime,"yyyy-MM-dd"));
            main_date_diy.setTextSize(12);
        }
    }

    @Override
    public void initListener() {
        dialog_main_date_root.setOnClickListener(this);
        main_date_day.setOnClickListener(this);
        main_date_week.setOnClickListener(this);
        main_date_month.setOnClickListener(this);
        main_date_diy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onDateListener == null)return;
        switch (view.getId()){
            case R.id.dialog_main_date_root:
                dismiss();
                break;
            case R.id.main_date_day:
                onDateListener.onDateClick(1, CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 00:00:00"),CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59"));
                dismiss();
                break;
            case R.id.main_date_week:
                onDateListener.onDateClick(2,(Long.parseLong(CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 00:00:00")) - 86400000*6) + "",CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59"));
                dismiss();
                break;
            case R.id.main_date_month:
                onDateListener.onDateClick(3,CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00"),CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59"));
                dismiss();
                break;
            case R.id.main_date_diy:
                showDuringDialog();
                break;
        }
    }

    //自定义时间段弹窗
    private void showDuringDialog(){
        DateDuringDialog duringDialog = new DateDuringDialog(context, DateTimeUtil.StringToData(beginTime,"yyyy-MM-dd"),DateTimeUtil.StringToData(endTime,"yyyy-MM-dd"));
        duringDialog.show();
        duringDialog.setOnDuringDateListener(new DateDuringDialog.OnDuringDateListener() {
            @Override
            public void onDuringFinish(String begin, String end) {
                if (onDateListener != null)onDateListener.onDateClick(4,begin,end);
                dismiss();
            }
        });
    }

    private OnDateListener onDateListener;
    public interface OnDateListener{
        void onDateClick(int type,String begin,String end);
    }
    public void setOnDateListener(OnDateListener onDateListener){
        this.onDateListener = onDateListener;
    }
}
