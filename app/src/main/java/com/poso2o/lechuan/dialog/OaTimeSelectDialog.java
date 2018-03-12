package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2018/2/3.
 *
 * 定时发布时间选择弹窗
 */

public class OaTimeSelectDialog extends BaseDialog {

    private Context context;
    private View view;

    //时间选择
    private TimePicker time_picker_select;
    //取消
    private TextView select_time_cancel;
    //确定
    private TextView select_time_confirm;

    private OnTimeSelect onTimeSelect;

    //初始化小时
    private int hour;
    //初始化分钟
    private int minute;

    public OaTimeSelectDialog(Context context,OnTimeSelect onTimeSelect) {
        super(context);
        this.context = context;
        this.onTimeSelect = onTimeSelect;
    }

    public void setTime(int hour,int minute){
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_oa_time_set,null);
        return view;
    }

    @Override
    public void initView() {
        time_picker_select = (TimePicker) view.findViewById(R.id.time_picker_select);
        select_time_cancel = (TextView) view.findViewById(R.id.select_time_cancel);
        select_time_confirm = (TextView) view.findViewById(R.id.select_time_confirm);
    }

    @Override
    public void initData() {
        time_picker_select.setIs24HourView(true);
        time_picker_select.setCurrentHour(hour);
        time_picker_select.setCurrentMinute(minute);

        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.8,0.4);
    }

    @Override
    public void initListener() {
        select_time_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        select_time_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = time_picker_select.getCurrentHour() == 0 ? 00 : time_picker_select.getCurrentHour();
                int minute = time_picker_select.getCurrentMinute() == 0 ? 00:time_picker_select.getCurrentMinute();
                onTimeSelect.onTimeSelect(hour,minute);
                dismiss();
            }
        });
    }

    public interface OnTimeSelect{
        void onTimeSelect(int hour,int minute);
    }
}
