package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/3.
 */

public class StockGoodsAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Goods> mDatas;

    private boolean is_online;

    public StockGoodsAdapter(Context context,boolean is_online) {
        this.context = context;
        this.is_online = is_online;
    }

    public void notifyAdapter(ArrayList<Goods> goodses){
        mDatas = goodses;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock_list,parent,false);
        return new StockGoodsVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            if (onStockSpecListener != null)
                onStockSpecListener.onLoadMore(vh.load_tips,vh.load_progress);
        }else {
            final Goods goods = mDatas.get(position);
            if (goods == null)return;

            StockGoodsVH vh = (StockGoodsVH) holder;

            if (is_online){
                if (goods.main_picture != null || !goods.main_picture.equals(""))
                    Glide.with(context).load(goods.main_picture).placeholder(R.mipmap.background_image).into(vh.stock_goods_picture);
                vh.stock_goods_name.setText(goods.goods_name);
                vh.stock_goods_price.setText(goods.goods_cost_section);
                vh.goods_stock_cost.setText(goods.goods_cost_amount);
                vh.goods_stock_number.setText(goods.goods_number + "");
                vh.stock_goods_no.setText(goods.goods_no);
            }else {
                if (goods.image222 != null || !goods.image222.equals(""))
                    Glide.with(context).load(goods.image222).placeholder(R.mipmap.background_image).into(vh.stock_goods_picture);
                vh.stock_goods_name.setText(goods.name);
                vh.stock_goods_price.setText(goods.price);
                vh.goods_stock_cost.setText(NumberFormatUtils.format(ArithmeticUtils.mul(Double.parseDouble(goods.fprice),Double.parseDouble(goods.totalnum))));
                vh.goods_stock_number.setText(goods.totalnum);
                vh.stock_goods_no.setText(goods.bh);
            }

            vh.stock_goods_root.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if(onStockSpecListener != null){
                        onStockSpecListener.onStockSpecClick(goods);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 1;
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas == null || position == mDatas.size())return 1;
        return 0;
    }

    class StockGoodsVH extends RecyclerView.ViewHolder{

        LinearLayout stock_goods_root;
        ImageView stock_goods_picture;
        TextView stock_goods_name;
        TextView stock_goods_price;
        TextView goods_stock_number;
        TextView goods_stock_cost;
        TextView stock_goods_no;

        public StockGoodsVH(View itemView) {
            super(itemView);
            stock_goods_root = (LinearLayout) itemView.findViewById(R.id.stock_goods_root);
            stock_goods_picture = (ImageView) itemView.findViewById(R.id.stock_goods_picture);
            stock_goods_name = (TextView) itemView.findViewById(R.id.stock_goods_name);
            stock_goods_price = (TextView) itemView.findViewById(R.id.stock_goods_price);
            goods_stock_number = (TextView) itemView.findViewById(R.id.goods_stock_number);
            goods_stock_cost = (TextView) itemView.findViewById(R.id.goods_stock_cost);
            stock_goods_no = (TextView) itemView.findViewById(R.id.stock_goods_no);
        }
    }

    private OnStockSpecListener onStockSpecListener;
    public interface OnStockSpecListener{
        void onStockSpecClick(Goods goods);
        void onLoadMore(TextView textView, ProgressBar progressBar);
    }
    public void setOnStockSpecListener(OnStockSpecListener onStockSpecListener){
        this.onStockSpecListener = onStockSpecListener;
    }
}
