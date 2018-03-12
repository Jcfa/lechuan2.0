package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/8.
 */

public class PositionListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<PowerData> mDatas;

    public PositionListAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<PowerData> powerDatas){
        mDatas = powerDatas ;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_position_list,parent,false);
        return new PositionVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PowerData powerData = mDatas.get(position);
        if (powerData == null)return;

        PositionVH vh = (PositionVH) holder;
        //新版数据
//        vh.item_position_name.setText(powerData.power_name);
        //旧版数据
        vh.item_position_name.setText(powerData.positionname);

        vh.item_position_name.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if(onPositionClickListener != null){
                    onPositionClickListener.onPositionListener(powerData);
                }
            }
        });
        vh.item_position_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onPositionClickListener != null)
                    onPositionClickListener.onPositionLongClick(powerData);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class PositionVH extends RecyclerView.ViewHolder{

        TextView item_position_name;

        public PositionVH(View itemView) {
            super(itemView);
            item_position_name = (TextView) itemView.findViewById(R.id.item_position_name);
        }
    }

    private OnPositionClickListener onPositionClickListener;
    public interface OnPositionClickListener{
        void onPositionListener(PowerData powerData);
        void onPositionLongClick(PowerData powerData);
    }
    public void setOnPositionClickListener(OnPositionClickListener onPositionClickListener){
        this.onPositionClickListener = onPositionClickListener;
    }
}
