package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.staffreport.AchieveDetItem;
import com.poso2o.lechuan.bean.staffreport.AchieveDetailData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * Created by mr zhang on 2017/8/4.
 */

public class AchieveDetailListAdapter extends RecyclerView.Adapter {

    private Context context;
    private AchieveDetailData achieveDetailData;

    public AchieveDetailListAdapter(Context context,AchieveDetailData achieveDetailData) {
        this.context = context;
        this.achieveDetailData = achieveDetailData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_achieve_detail_list,parent,false);
        return new AchieveDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (achieveDetailData == null)return;
        AchieveDetItem achieveDetItem = achieveDetailData.list.get(position);
        AchieveDetailViewHolder vh = (AchieveDetailViewHolder) holder;
        vh.item_achieve_detail_count.setText(achieveDetItem.months);
        vh.item_achieve_detail_amount.setText(NumberFormatUtils.format(achieveDetItem.order_amounts));
        vh.item_achieve_detail_target.setText(NumberFormatUtils.format(achieveDetItem.assignments));
        vh.item_achieve_detail_percent.setText(NumberFormatUtils.format(achieveDetItem.completion_rate) + "%");
        if (Double.parseDouble(achieveDetItem.completion_rate)>= 100){
            vh.item_achieve_detail_percent.setTextColor(0xFF3CB371);
        }else {
            vh.item_achieve_detail_percent.setTextColor(0xFFFFA54F);
        }
    }

    @Override
    public int getItemCount() {
        if (achieveDetailData == null)return 0;
        return achieveDetailData.list.size();
    }

    class AchieveDetailViewHolder extends RecyclerView.ViewHolder{

        TextView item_achieve_detail_count;
        TextView item_achieve_detail_amount;
        TextView item_achieve_detail_target;
        TextView item_achieve_detail_percent;

        public AchieveDetailViewHolder(View itemView) {
            super(itemView);
            item_achieve_detail_count = (TextView) itemView.findViewById(R.id.item_achieve_detail_count);
            item_achieve_detail_amount = (TextView) itemView.findViewById(R.id.item_achieve_detail_amount);
            item_achieve_detail_target = (TextView) itemView.findViewById(R.id.item_achieve_detail_target);
            item_achieve_detail_percent = (TextView) itemView.findViewById(R.id.item_achieve_detail_percent);
        }
    }
}
