package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.view.DuringCalendarView;


/**
 * Created by mr zhang on 2017/9/21.
 * 首页自定义时间段弹窗
 */

public class DateDuringDialog extends BaseDialog implements DuringCalendarView.OnDuringListener{

    private View view;
    private Context context;

    private LinearLayout dialog_date_during_root;

    private DuringCalendarView duringCalendarView;

    //初始化开始时间
    private String beginTime;
    //初始化结束时间
    private String endTime;

    public DateDuringDialog(Context context,String beginTime,String endTime) {
        super(context);
        this.context = context;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_date_during,null);
        return view;
    }

    @Override
    public void initView() {
        dialog_date_during_root = (LinearLayout) view.findViewById(R.id.dialog_date_during_root);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.9,0.55);

        duringCalendarView = new DuringCalendarView(context,beginTime,endTime);
        dialog_date_during_root.removeAllViews();
        dialog_date_during_root.addView(duringCalendarView.getRootView());
    }

    @Override
    public void initListener() {
        duringCalendarView.setOnDuringListener(this);
    }

    @Override
    public void onDuringClick(String begin, String end) {
        if (onDuringDateListener != null)onDuringDateListener.onDuringFinish(begin,end);
        dismiss();
    }

    private OnDuringDateListener onDuringDateListener;
    public interface OnDuringDateListener{
        void onDuringFinish(String begin, String end);
    }
    public void setOnDuringDateListener(OnDuringDateListener onDuringDateListener){
        this.onDuringDateListener = onDuringDateListener;
    }
}
