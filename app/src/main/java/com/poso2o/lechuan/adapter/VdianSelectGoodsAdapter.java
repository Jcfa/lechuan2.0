package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by mr zhang on 2017/11/13.
 */
public class VdianSelectGoodsAdapter extends RecyclerView.Adapter<VdianSelectGoodsAdapter.GoodsViewHolder> {

    private Context context;
    private ArrayList<Goods> selects;
    private ArrayList<Goods> mDatas;

    public VdianSelectGoodsAdapter(Context context) {
        super();
        this.context = context;
        selects = new ArrayList<>();
    }

    public void notifyDatas(ArrayList<Goods> goodses) {
        mDatas = goodses;
        notifyDataSetChanged();
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_goods, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder vh, int position) {
        if (mDatas == null) return;
        final Goods item = mDatas.get(position);
        // 设置高亮
//        if (item.isNameLight) {
//            vh.shop_goods_name.setText(Html.fromHtml(item.light_name));
//        } else {
//            vh.shop_goods_name.setText(item.name);
//        }
        vh.shop_goods_name.setText(item.goods_name);

//        if (item.isNoLight) {
//            vh.shop_goods_number.setText(Html.fromHtml(item.light_no));
//        } else {
//            vh.shop_goods_number.setText(item.bh);
//        }
        vh.shop_goods_number.setText(item.goods_no);

        vh.shop_goods_sold_out_hint.setVisibility(GONE);//已下架
        vh.shop_recycle_item_select.setSelected(findSelectData(item.guid) != null);//多选，是否选中
//        Glide.with(context).load(item.image222).into(vh.shop_recycle_item_iv);
        Glide.with(context).load(item.main_picture).into(vh.shop_recycle_item_iv);
        vh.shop_goods_money.setText(item.price);
//        vh.shop_goods_sales_volume_tv.setText(item.salesnum);
        vh.shop_goods_sales_volume_tv.setText(item.goods_sale_number + "");//销量
//        vh.shop_goods_stock_tv.setText(item.kcnum);
        vh.shop_goods_stock_tv.setText(item.goods_number + "");//库存
        if (item.has_online == 1) {//是否微店商品，1=微店，0=实体店
            vh.tv_vdian_goods.setVisibility(View.VISIBLE);
        } else {
            vh.tv_vdian_goods.setVisibility(GONE);
        }
        // 点击事件
//        vh.shop_recycle_item_select.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                selectItem(vh, item);
//            }
//        });
        // 列表项点击事件
        vh.shop_recycle_item_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectItem(vh, item);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size();
    }

    public void selectItem(GoodsViewHolder holder, Goods item) {
        Goods selectGoods = findSelectData(item.guid);
        if (selectGoods != null) {
            holder.shop_recycle_item_select.setSelected(false);
            selects.remove(selectGoods);
        } else {
            holder.shop_recycle_item_select.setSelected(true);
            selects.add(item);
        }

        if (onItemSelectListener != null) {
            if (selects.size() == getItemCount()) {
                onItemSelectListener.onItemSelect(true);
            } else if (selects.size() == getItemCount() - 1) {
                onItemSelectListener.onItemSelect(false);
            }
        }
    }

    private Goods findSelectData(String goods_id) {
        if (!TextUtils.isEmpty(goods_id) || selects != null) {
            for (Goods goods : selects) {
                if (TextUtils.equals(goods.guid, goods_id)) {
                    return goods;
                }
            }
        }
        return null;
    }

    public void allSelect(boolean selected) {
        if (mDatas == null || mDatas.size() == 0) return;
        selects.clear();
        if (selected) {
            selects.addAll(mDatas);
        }
        notifyDataSetChanged();
    }

    public ArrayList<Goods> getItems() {
        return mDatas;
    }

    public ArrayList<Goods> getSelects() {
        return selects;
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout shop_recycle_item_group;

        ImageView shop_recycle_item_select;
        ImageView shop_recycle_item_iv;
        ImageView shop_goods_sort;
        TextView shop_goods_sold_out_hint;
        TextView shop_goods_name;
        TextView shop_goods_number;
        TextView shop_goods_money;
        TextView shop_goods_sales_volume_tv;
        TextView shop_goods_stock_tv;
        TextView tv_vdian_goods;

        GoodsViewHolder(View itemView) {
            super(itemView);
            shop_recycle_item_select = (ImageView) itemView.findViewById(R.id.shop_recycle_item_select);

            shop_recycle_item_group = (LinearLayout) itemView.findViewById(R.id.shop_recycle_item_group);
            shop_recycle_item_iv = (ImageView) itemView.findViewById(R.id.shop_recycle_item_iv);
            shop_goods_name = (TextView) itemView.findViewById(R.id.shop_goods_name);
            shop_goods_sold_out_hint = (TextView) itemView.findViewById(R.id.shop_goods_sold_out_hint);
            shop_goods_number = (TextView) itemView.findViewById(R.id.shop_goods_number);
            shop_goods_money = (TextView) itemView.findViewById(R.id.shop_goods_money);
            shop_goods_sales_volume_tv = (TextView) itemView.findViewById(R.id.shop_goods_sales_volume_tv);
            shop_goods_stock_tv = (TextView) itemView.findViewById(R.id.shop_goods_stock_tv);
            tv_vdian_goods = (TextView) itemView.findViewById(R.id.tv_vdian_goods);

            shop_goods_sort = (ImageView) itemView.findViewById(R.id.shop_goods_sort);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Goods item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemSelectListener onItemSelectListener;

    public interface OnItemSelectListener {
        void onItemSelect(boolean is_all_select);
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }
}
