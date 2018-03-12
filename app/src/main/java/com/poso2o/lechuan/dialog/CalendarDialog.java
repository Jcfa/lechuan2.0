package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.view.customcalendar.CustomDate;
import com.poso2o.lechuan.view.customcalendar.JCalendarView;

/**
 * Created by mr zhang on 2017/8/9.
 * 日历选择
 */

public class CalendarDialog extends BaseDialog implements JCalendarView.OnCalendarDateListener{

    private View view;
    private Context context;
    //容器根布局
    private LinearLayout dialog_calendar_root;
    //日历控件
    private JCalendarView calendarView;

    //选中时间
    private String setDate = null;

    public CalendarDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CalendarDialog(Context context,String setDate) {
        super(context);
        this.context = context;
        this.setDate = setDate;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_select_time,null);
        return view;
    }

    @Override
    public void initView() {
        dialog_calendar_root = (LinearLayout) view.findViewById(R.id.dialog_calendar_root);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.9,0.5);

        if (setDate == null){
            calendarView = new JCalendarView(context);
        }else {
            calendarView = new JCalendarView(context,setDate);
        }
        dialog_calendar_root.removeAllViews();
        dialog_calendar_root.addView(calendarView.getRootView());
    }

    @Override
    public void initListener() {
        calendarView.setOnCalendarDateListener(this);
    }

    @Override
    public void onCalendarDate(CustomDate date) {
        if (onDateSelectListener == null)return;
        onDateSelectListener.onDateSelect(date);
    }

    private OnDateSelectListener onDateSelectListener;

    public interface OnDateSelectListener{
        void onDateSelect(CustomDate date);
    }
    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener){
        this.onDateSelectListener = onDateSelectListener;
    }
}
