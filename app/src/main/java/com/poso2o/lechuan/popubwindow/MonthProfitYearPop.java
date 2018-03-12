package com.poso2o.lechuan.popubwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;

import java.util.Calendar;

/**
 * Created by mr zhang on 2017/8/16.
 */

public class MonthProfitYearPop extends PopupWindow implements View.OnClickListener{

    private Context context;

    //今年
    private TextView this_year;
    //去年
    private TextView last_year;
    //前年
    private TextView two_year_ago;

    public MonthProfitYearPop(Context context,String now) {
        super(context);
        this.context = context;

        initView();
        initData(now);
        initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.this_year:
                if (onYearClickListener != null)
                    onYearClickListener.onYearClick(this_year.getText().toString());
                dismiss();
                break;
            case R.id.last_year:
                if (onYearClickListener != null)
                    onYearClickListener.onYearClick(last_year.getText().toString());
                dismiss();
                break;
            case R.id.two_year_ago:
                if (onYearClickListener != null)
                    onYearClickListener.onYearClick(two_year_ago.getText().toString());
                dismiss();
                break;
        }
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_month_profit_year,null,false);
        this.setContentView(view);
        this_year = (TextView) view.findViewById(R.id.this_year);
        last_year = (TextView) view.findViewById(R.id.last_year);
        two_year_ago = (TextView) view.findViewById(R.id.two_year_ago);
    }

    private void initData(String now){
        this.setWidth(200);
        this.setHeight(300);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int last = year - 1;
        int the_year_last = year - 2;
        this_year.setText(year + "");
        last_year.setText(last + "");
        two_year_ago.setText(the_year_last + "");
        if (now.equals((year + ""))){
            this_year.setTextColor(0xFFFF6537);
            last_year.setTextColor(0xFF5E5E5E);
            two_year_ago.setTextColor(0xFF5E5E5E);
        }else if (now.equals((last + ""))){
            this_year.setTextColor(0xFF5E5E5E);
            last_year.setTextColor(0xFFFF6537);
            two_year_ago.setTextColor(0xFF5E5E5E);
        }else if (now.equals((the_year_last + ""))){
            this_year.setTextColor(0xFF5E5E5E);
            last_year.setTextColor(0xFF5E5E5E);
            two_year_ago.setTextColor(0xFFFF6537);
        }
    }

    private void initListener(){
        this_year.setOnClickListener(this);
        last_year.setOnClickListener(this);
        two_year_ago.setOnClickListener(this);
    }

    public void showPop(View parent,int offx,int offy){
        this.showAsDropDown(parent,offx,offy);
    }

    private OnYearClickListener onYearClickListener;
    public interface OnYearClickListener{
        void onYearClick(String year);
    }
    public void setOnYearClickListener(OnYearClickListener onYearClickListener) {
        this.onYearClickListener = onYearClickListener;
    }
}
