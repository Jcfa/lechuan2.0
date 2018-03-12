package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.shopdata.ServiceBean;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class ServiceAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ServiceBean> mDatas ;

    //前一个选择的套餐
    private RelativeLayout pre_service_layout;
    private ImageView pre_service_tag;
    private ServiceBean preBean;

    public ServiceAdapter(Context context,ArrayList<ServiceBean> serviceBeen) {
        this.context = context;
        mDatas = serviceBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_list,parent,false);
        return new ServiceVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ServiceBean bean = mDatas.get(position);
        if (bean == null)return;
        final ServiceVH vh = (ServiceVH) holder;
        vh.service_name.setText(bean.service_name);
        vh.service_fee.setText(bean.amount);
        vh.service_unit.setText(" 元/" + bean.unit );
        vh.item_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preBean == null){
                    vh.item_service.setSelected(true);
                    vh.service_selected.setVisibility(View.VISIBLE);

                    pre_service_layout = vh.item_service;
                    pre_service_tag = vh.service_selected;
                }else {
                    pre_service_layout.setSelected(false);
                    pre_service_tag.setVisibility(View.INVISIBLE);

                    pre_service_layout = vh.item_service;
                    pre_service_tag = vh.service_selected;

                    vh.item_service.setSelected(true);
                    vh.service_selected.setVisibility(View.VISIBLE);
                }
                preBean = bean;

                if (onServiceSelectListener != null){
                    onServiceSelectListener.onServiceSelect(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class ServiceVH extends RecyclerView.ViewHolder{

        TextView service_name;
        ImageView service_selected;
        TextView service_fee;
        TextView service_unit;
        RelativeLayout item_service;

        public ServiceVH(View itemView) {
            super(itemView);
            service_name = (TextView) itemView.findViewById(R.id.service_name);
            service_selected = (ImageView) itemView.findViewById(R.id.service_selected);
            service_fee = (TextView) itemView.findViewById(R.id.service_fee);
            service_unit = (TextView) itemView.findViewById(R.id.service_unit);
            item_service = (RelativeLayout) itemView.findViewById(R.id.item_service);
        }
    }

    private OnServiceSelectListener onServiceSelectListener;
    public interface OnServiceSelectListener{
        void onServiceSelect(ServiceBean bean);
    }
    public void setOnServiceSelectListener(OnServiceSelectListener onServiceSelectListener){
        this.onServiceSelectListener = onServiceSelectListener;
    }
}
