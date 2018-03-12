package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.AchieveDetailListAdapter;
import com.poso2o.lechuan.bean.staffreport.AchieveDetailData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * Created by mr zhang on 2017/8/4.
 */

public class AchieveDetailDialog extends BaseDialog implements View.OnClickListener {

    private View view;
    private Context context;

    //关闭窗口
    private ImageView achieve_detail_close;
    //列表
    private RecyclerView achieve_detail_recycle;
    //营业员名称
    private TextView staff_name;
    //营业额
    private TextView value_sale_amount;
    //目标额
    private TextView value_target_amount;
    //销售比
    private TextView value_sale_percent;

    //列表适配器
    private AchieveDetailListAdapter achieveDetailListAdapter;

    private StaffReportData staffReportData;
    private AchieveDetailData achieveDetailData;

    public AchieveDetailDialog(Context context, StaffReportData staffReportData, AchieveDetailData achieveDetailData) {
        super(context);
        this.context = context;
        this.staffReportData = staffReportData;
        this.achieveDetailData = achieveDetailData;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_achievement_detail, null);
        return view;
    }

    @Override
    public void initView() {

        achieve_detail_close = (ImageView) view.findViewById(R.id.achieve_detail_close);

        achieve_detail_recycle = (RecyclerView) view.findViewById(R.id.achieve_detail_recycle);

        staff_name = (TextView) view.findViewById(R.id.staff_name);

        value_sale_amount = (TextView) view.findViewById(R.id.value_sale_amount);

        value_target_amount = (TextView) view.findViewById(R.id.value_target_amount);

        value_sale_percent = (TextView) view.findViewById(R.id.value_sale_percent);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1.0f, 0.7f);
        this.getWindow().setWindowAnimations(R.style.BottomInAnimation);

        staff_name.setText(staffReportData.realname);
        value_sale_amount.setText(NumberFormatUtils.format(staffReportData.order_amounts));
        value_target_amount.setText(NumberFormatUtils.format(staffReportData.assignments));
        value_sale_percent.setText(NumberFormatUtils.format(staffReportData.completion_rate) + "%");
        if (Double.parseDouble(staffReportData.completion_rate) >= 100){
            value_sale_percent.setTextColor(0xFF3CB371);
        }else {
            value_sale_percent.setTextColor(0xFFFFA54F);
        }

        initAdapter();
    }

    @Override
    public void initListener() {
        achieve_detail_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.achieve_detail_close:
                dismiss();
                break;
        }
    }

    private void initAdapter() {
        achieveDetailListAdapter = new AchieveDetailListAdapter(context,achieveDetailData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        achieve_detail_recycle.setLayoutManager(linearLayoutManager);
        achieve_detail_recycle.setAdapter(achieveDetailListAdapter);
    }
}
