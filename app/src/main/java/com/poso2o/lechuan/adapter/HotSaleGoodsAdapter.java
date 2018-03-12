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
import com.poso2o.lechuan.bean.goodsdata.HotSaleProduct;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/2.
 */

public class HotSaleGoodsAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<HotSaleProduct> mDatas;
    //是否微店
    private boolean isOnline;

    public HotSaleGoodsAdapter(Context context,boolean isOnline) {
        this.context = context;
        this.isOnline = isOnline;
    }

    public void notifyAdapter(ArrayList<HotSaleProduct> hotSaleProducts){
        mDatas = hotSaleProducts;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_sale_goods,parent,false);
        return new HotSaleGoodsVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==1 && onHotSaleItemListener != null){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            onHotSaleItemListener.onLoadMore(vh.load_tips,vh.load_progress);
        }else {
            final HotSaleProduct hotSaleProduct = mDatas.get(position);
            if (hotSaleProduct == null)return;

            HotSaleGoodsVH vh = (HotSaleGoodsVH) holder;

            if (isOnline){
                vh.order_detail_goods_name.setText(hotSaleProduct.goods_name);
                vh.hot_sale_goods_price.setText(NumberFormatUtils.format(hotSaleProduct.spec_price));
                vh.hot_sale_goods_spec.setText(hotSaleProduct.goods_spec_name);
                vh.hot_sale_goods_count.setText(hotSaleProduct.sale_munber + "");
                vh.hot_sale_goods_no.setText(hotSaleProduct.goods_no);

                if (hotSaleProduct.main_picture != null && !hotSaleProduct.main_picture.equals(""))
                    Glide.with(context).load(hotSaleProduct.main_picture).into(vh.hot_sale_goods_image);
            }else {
                vh.order_detail_goods_name.setText(hotSaleProduct.name);
                vh.hot_sale_goods_price.setText(NumberFormatUtils.format(hotSaleProduct.price));
                vh.hot_sale_goods_spec.setText(hotSaleProduct.colorid + "/" + hotSaleProduct.sizeid);
                vh.hot_sale_goods_count.setText(hotSaleProduct.totalnum);
                vh.hot_sale_goods_no.setText(hotSaleProduct.bh);

                if (hotSaleProduct.image222 != null && !hotSaleProduct.image222.equals(""))
                    Glide.with(context).load(hotSaleProduct.image222).into(vh.hot_sale_goods_image);
            }

            vh.hot_sale_goods_root.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if(onHotSaleItemListener != null){
                        onHotSaleItemListener.onHotSaleClick(hotSaleProduct);
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

    class HotSaleGoodsVH extends RecyclerView.ViewHolder{

        LinearLayout hot_sale_goods_root;
        ImageView hot_sale_goods_image;
        TextView order_detail_goods_name;
        TextView hot_sale_goods_price;
        TextView hot_sale_goods_spec;
        TextView hot_sale_goods_count;
        TextView hot_sale_goods_no;

        public HotSaleGoodsVH(View itemView) {
            super(itemView);
            hot_sale_goods_root = (LinearLayout) itemView.findViewById(R.id.hot_sale_goods_root);
            hot_sale_goods_image = (ImageView) itemView.findViewById(R.id.hot_sale_goods_image);
            order_detail_goods_name = (TextView) itemView.findViewById(R.id.order_detail_goods_name);
            hot_sale_goods_price = (TextView) itemView.findViewById(R.id.hot_sale_goods_price);
            hot_sale_goods_spec = (TextView) itemView.findViewById(R.id.hot_sale_goods_spec);
            hot_sale_goods_count = (TextView) itemView.findViewById(R.id.hot_sale_goods_count);
            hot_sale_goods_no = (TextView) itemView.findViewById(R.id.hot_sale_goods_no);
        }
    }

    private OnHotSaleItemListener onHotSaleItemListener;
    public interface OnHotSaleItemListener{
        void onHotSaleClick(HotSaleProduct hotSaleProduct);
        void onLoadMore(TextView textView, ProgressBar progressBar);
    }
    public void setOnHotSaleItemListener(OnHotSaleItemListener onHotSaleItemListener){
        this.onHotSaleItemListener = onHotSaleItemListener;
    }
}
