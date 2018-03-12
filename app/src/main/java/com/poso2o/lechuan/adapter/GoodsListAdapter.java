package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 商品列表适配器
 * Created by lenovo on 2016/12/9.
 */
public class GoodsListAdapter extends BaseAdapter<GoodsListAdapter.HomeGoodsHolder, Goods> {

    private ArrayList<Goods> selects;

    private boolean isMainEdit;

    private boolean isVdian;

    public GoodsListAdapter(Context context, boolean isVdian) {
        super(context, null);
        this.isVdian = isVdian;
        selects = new ArrayList<>();
    }

    public void setMainEdit(boolean mainEdit) {
        isMainEdit = mainEdit;
    }

    public boolean isMainEdit() {
        return isMainEdit;
    }

    @Override
    public HomeGoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeGoodsHolder(getItemView(R.layout.item_goods_list, parent));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void initItemView(final HomeGoodsHolder holder, final Goods item, int position) {
        // 批量编辑
        if (isMainEdit) {
            holder.home_recycle_item_select.setVisibility(View.VISIBLE);
            holder.home_to_goods_detail_prompt.setVisibility(View.INVISIBLE);
            holder.home_recycle_item_select.setSelected(findSelectData(item) != null);
            holder.home_recycle_item_select.setTag(item);
            holder.home_recycle_item_group.setTag(holder.home_recycle_item_select);
        } else {
            holder.home_recycle_item_select.setVisibility(View.GONE);
            holder.home_to_goods_detail_prompt.setVisibility(View.VISIBLE);
        }
        // 点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMainEdit) {
                    selectItem(holder, item);
                } else if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
            }
        });
        // 售罄商品名称显示为灰色
        if (item.sale_type == 0) {
            holder.home_goods_name.setTextColor(0xff9b9b9b);
        } else {
            holder.home_goods_name.setTextColor(0xff000000);
        }

        // 设置数据
        if (isVdian) {
            // 设置高亮
            if (item.isNameLight) {
                holder.home_goods_name.setText(Html.fromHtml(item.light_name));
            } else {
                holder.home_goods_name.setText(item.goods_name);
            }

            if (item.isNoLight) {
                holder.home_goods_number.setText(Html.fromHtml(item.light_no));
            } else {
                holder.home_goods_number.setText(item.goods_no);
            }

            holder.setTag("" + position);

            Glide.with(context).load(item.main_picture).placeholder(R.mipmap.background_image).into(holder.home_recycle_item_iv);// 设置图片

            holder.home_goods_money.setText(item.goods_price_section);
            holder.home_goods_sales_volume_tv.setText(Integer.toString(item.goods_sale_number));
            holder.home_goods_stock_tv.setText(Integer.toString(item.goods_number));
        } else {
            // 设置高亮
            if (item.isNameLight) {
                holder.home_goods_name.setText(Html.fromHtml(item.light_name));
            } else {
                holder.home_goods_name.setText(item.name);
            }

            if (item.isNoLight) {
                holder.home_goods_number.setText(Html.fromHtml(item.light_no));
            } else {
                holder.home_goods_number.setText(item.bh);
            }

            holder.setTag("" + position);

            Glide.with(context).load(item.image222).placeholder(R.mipmap.background_image).into(holder.home_recycle_item_iv);// 设置图片

            holder.home_goods_money.setText(item.price);
            holder.home_goods_sales_volume_tv.setText(item.salesnum);
            holder.home_goods_stock_tv.setText(item.kcnum);
        }

        if (position == getItemCount() - 1) {
            holder.home_recycle_item_group.setPadding(0, 0, 0, 150);
        } else {
            holder.home_recycle_item_group.setPadding(0, 0, 0, 0);
        }
    }

    private void selectItem(HomeGoodsHolder holder, Goods item) {
        if (isVdian) {
            Goods selectGoods = findSelectData(item);
            if (selectGoods != null) {
                holder.home_recycle_item_select.setSelected(false);
                selects.remove(selectGoods);
            } else {
                holder.home_recycle_item_select.setSelected(true);
                selects.add(item);
            }
        } else {
            Goods selectGoods = findSelectData(item);
            if (selectGoods != null) {
                holder.home_recycle_item_select.setSelected(false);
                selects.remove(selectGoods);
            } else {
                holder.home_recycle_item_select.setSelected(true);
                selects.add(item);
            }
        }
        if (onItemSelectListener != null){
            if (selects.size() == getItemCount()){
                onItemSelectListener.onItemSelect(true);
            }else if (selects.size() == getItemCount() - 1){
                onItemSelectListener.onItemSelect(false);
            }
        }
    }

    private Goods findSelectData(Goods g) {
        if (isVdian) {
            if (TextUtil.isNotEmpty(g.goods_id) || ListUtils.isNotEmpty(selects)) {
                for (Goods goods : selects) {
                    if (TextUtil.equals(goods.goods_id, g.goods_id)) {
                        return goods;
                    }
                }
            }
        } else {
            if (TextUtil.isNotEmpty(g.guid) || ListUtils.isNotEmpty(selects)) {
                for (Goods goods : selects) {
                    if (TextUtil.equals(goods.guid, g.guid)) {
                        return goods;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Goods> getSelects() {
        return selects;
    }

    /**
     * 全选
     *
     * @param b
     */
    public void allSelect(boolean b) {
        selects.clear();
        if (b) {
            selects.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void removeGoods() {
        data.removeAll(selects);
        allSelect(false);
        notifyDataSetChanged();
    }


    class HomeGoodsHolder extends BaseViewHolder {
        String tag = "-1";

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        LinearLayout home_recycle_item_group;

        ImageView home_recycle_item_select;
        ImageView home_recycle_item_iv;
        TextView home_goods_name;
        TextView home_goods_number;
        TextView home_goods_money;
        TextView home_goods_sales_volume_tv;
        TextView home_goods_stock_tv;

        TextView home_to_goods_detail_prompt;

        HomeGoodsHolder(View itemView) {
            super(itemView);
            home_recycle_item_group = getView(R.id.home_recycle_item_group);
            home_recycle_item_iv = getView(R.id.home_recycle_item_iv);
            home_goods_name = getView(R.id.home_goods_name);
            home_goods_number = getView(R.id.home_goods_number);
            home_goods_money = getView(R.id.home_goods_money);
            home_goods_sales_volume_tv = getView(R.id.home_goods_sales_volume_tv);
            home_goods_stock_tv = getView(R.id.home_goods_stock_tv);

            home_recycle_item_select = getView(R.id.home_recycle_item_select);

            home_to_goods_detail_prompt = getView(R.id.home_to_goods_detail_prompt);
        }
    }

    public OnItemSelectListener onItemSelectListener;
    public interface OnItemSelectListener{
        void onItemSelect(boolean is_all_select);
    }
    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener){
        this.onItemSelectListener = onItemSelectListener;
    }
}
