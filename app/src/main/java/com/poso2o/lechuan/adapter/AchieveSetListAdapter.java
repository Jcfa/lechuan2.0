package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.staffreport.StaffAssigSetData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/4.
 */

public class AchieveSetListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<StaffReportData> mDatas;
    private ArrayList<StaffAssigSetData> setDatas = new ArrayList<>();

    public AchieveSetListAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<StaffReportData> staffDatas){
        mDatas = staffDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_achieve_set,parent,false);
        return new AchieveSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StaffReportData staffData = mDatas.get(position);
        if (staffData == null)return;
        final StaffAssigSetData assigSetData = new StaffAssigSetData();
        assigSetData.czy = staffData.czy;
        assigSetData.assignments = staffData.assignments;
        setDatas.add(assigSetData);
        final AchieveSetViewHolder vh = (AchieveSetViewHolder) holder;
        vh.achieve_set_staff.setText(staffData.realname);
        vh.achieve_set_staff_no.setText(staffData.czy);

        if (vh.achieve_set_task.getTag() instanceof TextWatcher){
            vh.achieve_set_task.removeTextChangedListener((TextWatcher) vh.achieve_set_task.getTag());
        }
        vh.achieve_set_task.setText(staffData.assignments);

        TextWatcher myWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = vh.achieve_set_task.getText().toString();
                if ( str == null || str.equals("")){
                    assigSetData.assignments = "0";
                    staffData.order_amounts = "0";
                }else {
                    assigSetData.assignments = str;
                    staffData.order_amounts = str;
                }
            }
        };
        vh.achieve_set_task.addTextChangedListener(myWatcher);
        vh.achieve_set_task.setTag(myWatcher);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class AchieveSetViewHolder extends RecyclerView.ViewHolder{

        TextView achieve_set_staff;
        TextView achieve_set_staff_no;
        EditText achieve_set_task;

        public AchieveSetViewHolder(View itemView) {
            super(itemView);
            achieve_set_staff = (TextView) itemView.findViewById(R.id.achieve_set_staff);
            achieve_set_staff_no = (TextView) itemView.findViewById(R.id.achieve_set_staff_no);
            achieve_set_task = (EditText) itemView.findViewById(R.id.achieve_set_task);
        }
    }

    public ArrayList<StaffAssigSetData> getSetDatas(){
        return setDatas;
    }
}
