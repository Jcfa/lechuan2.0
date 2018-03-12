package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.staff.StaffData;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/7.
 */

public class StaffListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<StaffData> mDatas;

    public StaffListAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<StaffData> staffDatas){
        mDatas = staffDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_staff,parent,false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StaffData staffData = mDatas.get(position);
        if (staffData == null)return;
        StaffViewHolder vh = (StaffViewHolder) holder;
//        vh.menu_staff_no.setText(staffData.staff_id);
//        vh.menu_staff_name.setText(staffData.staff_name);
//        vh.menu_staff_task.setText(staffData.staff_power_name);
//        vh.menu_staff_phone.setText(staffData.staff_mobile);
        vh.menu_staff_no.setText(staffData.czy);
        vh.menu_staff_name.setText(staffData.realname);
        vh.menu_staff_task.setText(staffData.positionname);
        vh.menu_staff_phone.setText(staffData.mobile);
        vh.item_staff_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onStaffClickListener != null){
                    onStaffClickListener.onStaffClick(staffData);
                }
            }
        });
        vh.item_staff_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onStaffClickListener != null)
                    onStaffClickListener.onStaffLongClick(staffData);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder{

        LinearLayout item_staff_root;
        TextView menu_staff_no;
        TextView menu_staff_name;
        TextView menu_staff_task;
        TextView menu_staff_phone;

        public StaffViewHolder(View itemView) {
            super(itemView);
            item_staff_root = (LinearLayout) itemView.findViewById(R.id.item_staff_root);
            menu_staff_no = (TextView) itemView.findViewById(R.id.menu_staff_no);
            menu_staff_name = (TextView) itemView.findViewById(R.id.menu_staff_name);
            menu_staff_task = (TextView) itemView.findViewById(R.id.menu_staff_task);
            menu_staff_phone = (TextView) itemView.findViewById(R.id.menu_staff_phone);
        }
    }

    private OnStaffClickListener onStaffClickListener;
    public interface OnStaffClickListener{
        void onStaffClick(StaffData staffData);
        void onStaffLongClick(StaffData staffData);
    }
    public void setOnStaffClickListener(OnStaffClickListener onStaffClickListener){
        this.onStaffClickListener = onStaffClickListener;
    }
}
