package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.marketdata.CouponData;
import com.poso2o.lechuan.util.DateTimeUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/3.
 */

public class CouponAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<CouponData> mDatas;

    public CouponAdapter(Context context) {
        this.context = context;
    }

    public void notifyListData(ArrayList<CouponData> couponDatas){
        mDatas = couponDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon,parent,false);
        return new CouponVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CouponData couponData = mDatas.get(position);
        if (couponData == null)return;
        CouponVH vh = (CouponVH) holder;
        vh.coupon_amount.setText(couponData.card_amount);
        vh.coupon_use_need.setText("满" + couponData.use_limit + "元可用");
        vh.coupon_total.setText("总数" + couponData.card_nums);
        vh.coupon_receive.setText("已领" + couponData.card_issued_num);
        vh.coupon_time.setText(DateTimeUtil.StringToData(couponData.begin_time,"yyyy-MM-dd") + " 至 " + DateTimeUtil.StringToData(couponData.end_time,"yyyy-MM-dd"));

        vh.coupon_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCouponListener != null)onCouponListener.onCouponDelClick(couponData);
            }
        });
        vh.coupon_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCouponListener != null)onCouponListener.onCouponShareClick(couponData);
            }
        });
        vh.coupon_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCouponListener != null)onCouponListener.onCouponInfoClick(couponData);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class CouponVH extends RecyclerView.ViewHolder{

        RelativeLayout coupon_root;
        TextView coupon_amount;
        TextView coupon_use_need;
        TextView coupon_total;
        TextView coupon_receive;
        TextView coupon_time;
        ImageButton coupon_del;
        ImageButton coupon_share;

        public CouponVH(View itemView) {
            super(itemView);
            coupon_root = (RelativeLayout) itemView.findViewById(R.id.coupon_root);
            coupon_amount = (TextView) itemView.findViewById(R.id.coupon_amount);
            coupon_use_need = (TextView) itemView.findViewById(R.id.coupon_use_need);
            coupon_total = (TextView) itemView.findViewById(R.id.coupon_total);
            coupon_receive = (TextView) itemView.findViewById(R.id.coupon_receive);
            coupon_time = (TextView) itemView.findViewById(R.id.coupon_time);
            coupon_del = (ImageButton) itemView.findViewById(R.id.coupon_del);
            coupon_share = (ImageButton) itemView.findViewById(R.id.coupon_share);
        }
    }

    private OnCouponListener onCouponListener;
    public interface OnCouponListener{
        void onCouponInfoClick(CouponData couponData);
        void onCouponDelClick(CouponData couponData);
        void onCouponShareClick(CouponData couponData);
    }
    public void setOnCouponListener(OnCouponListener onCouponListener){
        this.onCouponListener = onCouponListener;
    }
}
