package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/6.
 */

public class MSelectDGAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Goods> mDatas;
    private ArrayList<Goods> mSelected;

    public MSelectDGAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapterData(ArrayList<Goods> goodses,ArrayList<Goods> selectGoods){
        mDatas = goodses;
        mSelected = selectGoods;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_m_select_dg,parent,false);
        return new MSelectDGVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Goods goods = mDatas.get(position);
        if (goods == null)return;
        final MSelectDGVH vh = (MSelectDGVH) holder;
        if (!goods.main_picture.equals("")) Glide.with(context).load(goods.main_picture).into(vh.m_dg_item_iv);
        vh.m_dg_name.setText(goods.goods_name);
        vh.m_dg_number.setText(goods.goods_no);
        vh.m_dg_money.setText(goods.goods_price_section);
        vh.m_dg_sales_volume_tv.setText(goods.goods_sale_number + "");
        vh.m_dg_stock_tv.setText(goods.goods_number + "");
        if (isIn(goods)){
            vh.m_dg_item_select.setSelected(true);
        }else {
            vh.m_dg_item_select.setSelected(false);
        }
        if (goods.goodsPromotionInfo != null && !goods.goodsPromotionInfo.goods_name.equals("")){
            vh.has_discount_name.setText(goods.goodsPromotionInfo.promotion_name);
            vh.m_dg_item_select.setImageResource(R.mipmap.icon_no_select_48);
        }else {
            vh.has_discount_name.setText(null);
            vh.m_dg_item_select.setImageResource(R.drawable.selector_home_recycle_item_select_img);
        }
        vh.m_dg_item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goods.goodsPromotionInfo != null && !goods.goodsPromotionInfo.goods_name.equals("")){
                    Toast.show(context,"该商品已参加 " + goods.goodsPromotionInfo.promotion_name + "活动");
                    return;
                }
                if (onDGListener != null)onDGListener.onDGClick(goods);
                if (vh.m_dg_item_select.isSelected()){
                    vh.m_dg_item_select.setSelected(false);
                }else {
                    vh.m_dg_item_select.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class MSelectDGVH extends RecyclerView.ViewHolder{

        LinearLayout m_dg_item_root;
        ImageView m_dg_item_select;
        ImageView m_dg_item_iv;
        TextView m_dg_name;
        TextView m_dg_number;
        TextView m_dg_money;
        TextView m_dg_sales_volume_tv;
        TextView m_dg_stock_tv;

        TextView has_discount_name;

        public MSelectDGVH(View itemView) {
            super(itemView);
            m_dg_item_root = (LinearLayout) itemView.findViewById(R.id.m_dg_item_root);
            m_dg_item_select = (ImageView) itemView.findViewById(R.id.m_dg_item_select);
            m_dg_item_iv = (ImageView) itemView.findViewById(R.id.m_dg_item_iv);
            m_dg_name = (TextView) itemView.findViewById(R.id.m_dg_name);
            m_dg_number = (TextView) itemView.findViewById(R.id.m_dg_number);
            m_dg_money = (TextView) itemView.findViewById(R.id.m_dg_money);
            m_dg_sales_volume_tv = (TextView) itemView.findViewById(R.id.m_dg_sales_volume_tv);
            m_dg_stock_tv = (TextView) itemView.findViewById(R.id.m_dg_stock_tv);

            has_discount_name = (TextView) itemView.findViewById(R.id.has_discount_name);
        }
    }

    private OnDGListener onDGListener;
    public interface OnDGListener{
        void onDGClick(Goods goods);
    }
    public void setOnDGListener(OnDGListener onDGListener){
        this.onDGListener = onDGListener;
    }

    private boolean isIn(Goods goods){
        for (Goods item : mSelected){
            if (item.goods_id.equals(goods.goods_id))return true;
        }
        return false;
    }
}
