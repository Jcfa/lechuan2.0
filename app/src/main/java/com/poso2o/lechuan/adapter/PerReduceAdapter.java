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
import com.poso2o.lechuan.bean.marketdata.PerReduceData;
import com.poso2o.lechuan.util.DateTimeUtil;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/2.
 */

public class PerReduceAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<PerReduceData> mDatas;

    public PerReduceAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapterData(ArrayList<PerReduceData> perReduceDatas){
        mDatas = perReduceDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_per_reduce,parent,false);
        return new PerReduceVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PerReduceData reduceData = mDatas.get(position);
        if (reduceData == null)return;
        PerReduceVH vh = (PerReduceVH) holder;
        vh.per_reduce_name.setText(reduceData.promotion_name);
        String des = "";
        for (int i=0;i<reduceData.promotionDetails.size();i++){
            if (i==0 && i!=reduceData.promotionDetails.size() - 1){
                des = des + "满 " + reduceData.promotionDetails.get(i).amount_moq + " 减 " + reduceData.promotionDetails.get(i).delete_amount;
            }else if (i==0 && i == reduceData.promotionDetails.size() - 1){
                des = des +  "满 " + reduceData.promotionDetails.get(i).amount_moq + " 减 " + reduceData.promotionDetails.get(i).delete_amount + "。";
            }else if (reduceData.promotionDetails.size() > 1 && i == reduceData.promotionDetails.size() - 1){
                des = des + "，满 " + reduceData.promotionDetails.get(i).amount_moq + " 减 " + reduceData.promotionDetails.get(i).delete_amount + "。";
            }else {
                des = des + "，满 " + reduceData.promotionDetails.get(i).amount_moq + " 减 " + reduceData.promotionDetails.get(i).delete_amount;
            }
        }
        vh.per_reduce_des.setText(des);
        vh.per_reduce_time.setText(DateTimeUtil.StringToData(reduceData.begin_time,"yyyy-MM-dd") + " 至 " + DateTimeUtil.StringToData(reduceData.end_time,"yyyy-MM-dd"));

        vh.per_reduce_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (perReduceListener != null)perReduceListener.onPerReduceDel(reduceData);
            }
        });

        vh.per_reduce_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (perReduceListener != null)perReduceListener.onPerReduceEdit(reduceData);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class PerReduceVH extends RecyclerView.ViewHolder{

        RelativeLayout per_reduce_view;
        TextView per_reduce_name;
        TextView per_reduce_des;
        TextView per_reduce_time;
        ImageButton per_reduce_edit;
        ImageButton per_reduce_del;

        public PerReduceVH(View itemView) {
            super(itemView);
            per_reduce_view = (RelativeLayout) itemView.findViewById(R.id.per_reduce_view);
            per_reduce_name = (TextView) itemView.findViewById(R.id.per_reduce_name);
            per_reduce_des = (TextView) itemView.findViewById(R.id.per_reduce_des);
            per_reduce_time = (TextView) itemView.findViewById(R.id.per_reduce_time);
            per_reduce_edit = (ImageButton) itemView.findViewById(R.id.per_reduce_edit);
            per_reduce_del = (ImageButton) itemView.findViewById(R.id.per_reduce_del);
        }
    }

    private OnPerReduceListener perReduceListener;
    public interface OnPerReduceListener{
        void onPerReduceEdit(PerReduceData perReduceData);
        void onPerReduceDel(PerReduceData perReduceData);
    }
    public void setOnPerReduceListener(OnPerReduceListener onPerReduceListener){
        perReduceListener = onPerReduceListener;
    }
}
