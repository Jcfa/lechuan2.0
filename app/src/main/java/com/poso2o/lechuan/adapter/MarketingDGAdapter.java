package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.marketdata.MarketingDGData;
import com.poso2o.lechuan.util.DateTimeUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/4.
 */

public class MarketingDGAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MarketingDGData> mDatas;

    public MarketingDGAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapterData(ArrayList<MarketingDGData> marketingDGDatas){
        mDatas = marketingDGDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_marketing_dg,parent,false);
        return new DGVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MarketingDGData dgData = mDatas.get(position);
        if (dgData == null)return;
        DGVH vh = (DGVH) holder;
        vh.dg_name.setText(dgData.promotion_name);
        vh.dg_num.setText("共 " + dgData.goods_num + " 款");
        vh.dg_discount.setText("待处理");
        vh.dg_time.setText(DateTimeUtil.StringToData(dgData.begin_time,"yyyy.MM.dd") + " - " + DateTimeUtil.StringToData(dgData.end_time,"yyyy.MM.dd"));
        if (!dgData.promotionGoodsDetails.get(0).main_picture.equals("")) Glide.with(context).load(dgData.promotionGoodsDetails.get(0).main_picture).into(vh.dg_pic);

        vh.dg_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMDGListener != null)onMDGListener.onEditMDG(dgData);
            }
        });
        vh.dg_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMDGListener != null)onMDGListener.onDelMDG(dgData);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class DGVH extends RecyclerView.ViewHolder{

        ImageView dg_pic;
        TextView dg_name;
        TextView dg_num;
        TextView dg_discount;
        TextView dg_time;
        ImageButton dg_edit;
        ImageButton dg_del;

        public DGVH(View itemView) {
            super(itemView);
            dg_pic = (ImageView) itemView.findViewById(R.id.dg_pic);
            dg_name = (TextView) itemView.findViewById(R.id.dg_name);
            dg_num = (TextView) itemView.findViewById(R.id.dg_num);
            dg_discount = (TextView) itemView.findViewById(R.id.dg_discount);
            dg_time = (TextView) itemView.findViewById(R.id.dg_time);
            dg_edit = (ImageButton) itemView.findViewById(R.id.dg_edit);
            dg_del = (ImageButton) itemView.findViewById(R.id.dg_del);
        }
    }

    private OnMDGListener onMDGListener;
    public interface OnMDGListener{
        void onEditMDG(MarketingDGData marketingDGData);
        void onDelMDG(MarketingDGData marketingDGData);
    }
    public void setOnMDGListener(OnMDGListener onMDGListener){
        this.onMDGListener = onMDGListener;
    }
}
