package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * 批量发货适配器
 * Created by Luo on 2017/02/10.
 */
public class BatchDeliveryAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<OrderDTO> orderDTOs;
    private RequestManager glideRequest;
    private int isCheckedNum = 0;

    public BatchDeliveryAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.orderDTOs = new ArrayList<>();
        if (orderDTOs != null) {
            this.orderDTOs.addAll(orderDTOs);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(context).inflate(R.layout.view_batch_delivery_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return orderDTOs.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OrderHolder) {
            OrderHolder orderHolder = (OrderHolder) holder;
            final OrderDTO item = orderDTOs.get(position);

            orderHolder.batch_delivery_item_no.setText("" + item.order_id);
            orderHolder.batch_delivery_item_name.setText("" + item.receipt_name + "," +item.receipt_mobile + ",");//收货人,1340000000,
            orderHolder.batch_delivery_item_address.setText("" + item.receipt_province_name + item.receipt_city_name +
                    item.receipt_area_name + item.receipt_address);
            if (item.isChecked){
                orderHolder.batch_delivery_item_rb.setImageResource(R.mipmap.icon_ad_selected_blue_48);
            }else {
                orderHolder.batch_delivery_item_rb.setImageResource(R.mipmap.icon_ad_select_48);
            }
            orderHolder.batch_delivery_item_group.setTag(orderHolder.batch_delivery_item_rb);
            orderHolder.batch_delivery_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ImageView checkBox = (ImageView) v.getTag();
                    if (item.isChecked){
                        checkBox.setImageResource(R.mipmap.icon_ad_select_48);
                        item.isChecked = false;
                    }else{
                        checkBox.setImageResource(R.mipmap.icon_ad_selected_blue_48);
                        item.isChecked = true;
                    }
                    if (onItemListener != null){
                        onItemListener.onClickItem();
                    }
                }
            });

        }
    }

    public void notifyData(ArrayList<OrderDTO> orderDTOs) {
        this.orderDTOs.clear();
        if (orderDTOs != null) {
            this.orderDTOs.addAll(orderDTOs);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<OrderDTO> posterData) {
        if (posterData != null) {
            this.orderDTOs.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置选中或取消选中
     * @param allCheckedTrue
     */
    public void setAllCheckedTrue(boolean allCheckedTrue) {
        if (orderDTOs != null) {
            for (OrderDTO orders : orderDTOs){
                orders.isChecked = allCheckedTrue;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 统计选中数量
     * @return
     */
    public int totalAllCheckedNum(){
        isCheckedNum = 0;
        if (orderDTOs != null) {
            for (OrderDTO orders : orderDTOs){
                if (orders.isChecked){
                    isCheckedNum++;
                }
            }
        }
        return isCheckedNum;
    }

    /**
     * 获取选中数据
     * @return
     */
    public ArrayList<OrderDTO> getCheckedOrderDTO(){
        ArrayList<OrderDTO> orderData = new ArrayList<OrderDTO>();
        if (orderDTOs != null) {
            for (OrderDTO orders : orderDTOs){
                if (orders.isChecked){
                    orderData.add(orders);
                }
            }
        }
        return orderData;
    }

    public interface OnItemListener {
        void onClickItem();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {

        View batch_delivery_item_group;
        ImageView batch_delivery_item_rb;
        TextView batch_delivery_item_no;
        TextView batch_delivery_item_name;
        TextView batch_delivery_item_address;

        OrderHolder(View itemView) {
            super(itemView);
            batch_delivery_item_group = itemView.findViewById(R.id.batch_delivery_item_group);

            batch_delivery_item_rb = (ImageView) itemView.findViewById(R.id.batch_delivery_item_rb);
            batch_delivery_item_no = (TextView) itemView.findViewById(R.id.batch_delivery_item_no);
            batch_delivery_item_name = (TextView) itemView.findViewById(R.id.batch_delivery_item_name);
            batch_delivery_item_address = (TextView) itemView.findViewById(R.id.batch_delivery_item_address);
        }
    }

}
