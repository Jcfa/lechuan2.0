package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.member.MemberOrder;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class MemberDetailAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MemberOrder> mDatas;
    //是否微店
    private boolean is_online;

    public MemberDetailAdapter(Context context, ArrayList<MemberOrder> memberOrders,boolean is_online) {
        this.context = context;
        mDatas = memberOrders;
        this.is_online = is_online;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member_order,parent,false);
        return new MemberDetailVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MemberOrder memberOrder = mDatas.get(position);
        if (memberOrder == null)return;
        MemberDetailVH vh = (MemberDetailVH) holder;
        if (is_online){
            vh.member_order_time.setText(DateTimeUtil.StringToData(memberOrder.finish_time,"yyyy-MM-dd HH:mm:ss"));
            vh.member_order_no.setText(memberOrder.sale_order_id);
            vh.member_order_count.setText(memberOrder.order_total_munber + "");
            vh.member_order_amount.setText(NumberFormatUtils.format(memberOrder.order_paid_amount));
        }else {
            String time = memberOrder.order_date;
            if (memberOrder.order_date.contains(".0")){
                time = memberOrder.order_date.substring(0,memberOrder.order_date.length()-2);
            }
            vh.member_order_time.setText(time);
            vh.member_order_no.setText(memberOrder.order_id);
            vh.member_order_count.setText(memberOrder.order_num);
            vh.member_order_amount.setText(NumberFormatUtils.format(memberOrder.payment_amount));
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class MemberDetailVH extends RecyclerView.ViewHolder{

        TextView member_order_time;
        TextView member_order_no;
        TextView member_order_count;
        TextView member_order_amount;

        public MemberDetailVH(View itemView) {
            super(itemView);
            member_order_time = (TextView) itemView.findViewById(R.id.member_order_time);
            member_order_no = (TextView) itemView.findViewById(R.id.member_order_no);
            member_order_count = (TextView) itemView.findViewById(R.id.member_order_count);
            member_order_amount = (TextView) itemView.findViewById(R.id.member_order_amount);
        }
    }
}
