package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/11/3.
 */

public class CouponRecordAdapter extends RecyclerView.Adapter {

    private Context context;

    public CouponRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon_record,parent,false);
        return new CouponRecordVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CouponRecordVH extends RecyclerView.ViewHolder{

        TextView coupon_record_time;
        TextView coupon_record_no;
        TextView coupon_record_num;
        TextView coupon_record_phone;
        TextView coupon_record_state;

        public CouponRecordVH(View itemView) {
            super(itemView);
            coupon_record_time = (TextView) itemView.findViewById(R.id.coupon_record_time);
            coupon_record_no = (TextView) itemView.findViewById(R.id.coupon_record_no);
            coupon_record_num = (TextView) itemView.findViewById(R.id.coupon_record_num);
            coupon_record_phone = (TextView) itemView.findViewById(R.id.coupon_record_phone);
            coupon_record_state = (TextView) itemView.findViewById(R.id.coupon_record_state);
        }
    }
}
