package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/4.
 */

public class AchievementListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<StaffReportData> mDatas;
    private boolean is_online ;

    public AchievementListAdapter(Context context,boolean is_online) {
        this.context = context;
        this.is_online = is_online;
    }

    public void notifyAdapter(ArrayList<StaffReportData> reportDatas){
        mDatas = reportDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_achievement,parent,false);
        return new AchievementVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final StaffReportData staffReportData = mDatas.get(position);
        if (staffReportData == null)return;

        AchievementVH vh = (AchievementVH) holder;
        if (is_online){
//            vh.achievement_staff_no.setText(staffReportData.staff_id);
//            vh.achievement_staff_name.setText(staffReportData.staff_name);
//            vh.item_achievement_sale.setText(NumberFormatUtils.format(staffReportData.staff_sale_amount));
//            vh.achievement_sale_target.setText(NumberFormatUtils.format(staffReportData.staff_target_assignments));
//            vh.item_achievement_achieve.setText(NumberFormatUtils.format(staffReportData.staff_target_assignments_rate) + "%");
        }else {
            vh.achievement_staff_no.setText(staffReportData.czy);
            vh.achievement_staff_name.setText(staffReportData.realname);
            vh.item_achievement_sale.setText(staffReportData.order_amounts);
            vh.achievement_sale_target.setText(staffReportData.assignments);
            vh.item_achievement_achieve.setText(NumberFormatUtils.format(staffReportData.completion_rate) + "%");
        }

        if (Double.parseDouble(staffReportData.completion_rate) >= 100){
            vh.item_achievement_achieve.setTextColor(0xFF3CB371);
        }else {
            vh.item_achievement_achieve.setTextColor(0xFFFFA54F);
        }

        vh.item_achievement_root.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if(onAchieveListListener != null){
                    onAchieveListListener.onAchieveClick(staffReportData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class AchievementVH extends RecyclerView.ViewHolder{

        LinearLayout item_achievement_root;
        TextView achievement_staff_no;
        TextView achievement_staff_name;
        TextView item_achievement_sale;
        TextView achievement_sale_target;
        TextView item_achievement_achieve;

        public AchievementVH(View itemView) {
            super(itemView);
            item_achievement_root = (LinearLayout) itemView.findViewById(R.id.item_achievement_root);
            achievement_staff_no = (TextView) itemView.findViewById(R.id.achievement_staff_no);
            achievement_staff_name = (TextView) itemView.findViewById(R.id.achievement_staff_name);
            item_achievement_sale = (TextView) itemView.findViewById(R.id.item_achievement_sale);
            achievement_sale_target = (TextView) itemView.findViewById(R.id.achievement_sale_target);
            item_achievement_achieve = (TextView) itemView.findViewById(R.id.item_achievement_achieve);
        }
    }

    private OnAchieveListListener onAchieveListListener;
    public interface OnAchieveListListener{
        void onAchieveClick(StaffReportData staffReportData);
    }
    public void setOnAchieveListListener(OnAchieveListListener onAchieveListListener){
        this.onAchieveListListener = onAchieveListListener;
    }
}
