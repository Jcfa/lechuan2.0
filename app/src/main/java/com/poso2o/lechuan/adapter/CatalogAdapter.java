package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.TextUtil;

/**
 * 目录列表适配器
 * Created by lenovo on 2016/12/14.
 */
public class CatalogAdapter extends BaseAdapter<CatalogAdapter.GroupsViewHolder, Catalog> {

    private boolean isEdit = false;

    private String selectId;

    private boolean isVdian;

    public CatalogAdapter(Context context, boolean isVdian) {
        super(context, null);
        this.isVdian = isVdian;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupsViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_catalog_item, parent, false));
    }

    @Override
    public void initItemView(GroupsViewHolder holder, final Catalog item, int position) {
        String catalog_id;
        if (isVdian) {
            catalog_id = item.catalog_id;
            holder.catalog_item_name.setText(item.catalog_name);
            holder.catalog_item_number.setText(Integer.toString(item.catalog_goods_number));
            holder.catalog_item_discount.setText(NumberFormatUtils.format(item.catalog_discount) + "%");
        } else {
            catalog_id = item.fid;
            holder.catalog_item_name.setText(item.directory);
            holder.catalog_item_number.setText(item.productNum);
        }

        if (TextUtil.equals(selectId, catalog_id)) {
            holder.catalog_item_name.setTextColor(context.getResources().getColor(R.color.textGreen));
        } else {
            holder.catalog_item_name.setTextColor(context.getResources().getColor(R.color.textBlack));
        }

        if (isEdit) {
            holder.catalog_item_edit.setVisibility(View.VISIBLE);
        } else {
            holder.catalog_item_edit.setVisibility(View.GONE);
        }

        if (position == getItemCount() - 1 && !isEdit) {// 最后一条
            holder.catalog_item_group.setPadding(0, 0, 0, 150);
        } else if (position == 0) {// 第一条
            holder.catalog_item_group.setPadding(0, 0, 0, 0);
        } else {
            holder.catalog_item_group.setPadding(0, 0, 0, 0);
        }
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void updateItemData(Catalog catalogData) {
        Catalog catalog = findDataById(catalogData);
        if (catalog != null) {
            catalog.catalog_name = catalogData.catalog_name;
            catalog.catalog_discount = catalogData.catalog_discount;
            catalog.directory = catalogData.directory;
        }
        notifyDataSetChanged();
    }

    public void deleteItemData(Catalog catalogData) {
        Catalog catalog = findDataById(catalogData);
        if (catalog != null) {
            data.remove(catalog);
        }
        notifyDataSetChanged();
    }

    private Catalog findDataById(Catalog c) {
        String id = isVdian ? c.catalog_id : c.fid;
        return findDataById(id);
    }

    private Catalog findDataById(String id) {
        if (isVdian) {
            for (Catalog catalog : data) {
                if (TextUtil.equals(catalog.catalog_id, id)) {
                    return catalog;
                }
            }
        } else {
            for (Catalog catalog : data) {
                if (TextUtil.equals(catalog.fid, id)) {
                    return catalog;
                }
            }
        }
        return null;
    }

    public Catalog getSelectItem() {
        return findDataById(selectId);
    }

    public class GroupsViewHolder extends BaseViewHolder {

        LinearLayout catalog_item_group;
        TextView catalog_item_name;
        TextView catalog_item_number;
        TextView catalog_item_discount;
        TextView catalog_item_edit;

        GroupsViewHolder(View itemView) {
            super(itemView);

            catalog_item_group = getView(R.id.catalog_item_group);
            catalog_item_name = getView(R.id.catalog_item_name);
            catalog_item_number = getView(R.id.catalog_item_number);
            catalog_item_discount = getView(R.id.catalog_item_discount);
            catalog_item_edit = getView(R.id.catalog_item_edit);

            if (!isVdian) {
                catalog_item_discount.setVisibility(View.INVISIBLE);
            }

        }

        public void onItemSelected() {
            catalog_item_group.setBackgroundColor(Color.GRAY);
        }

        public void onItemClear() {
            catalog_item_group.setBackgroundColor(Color.WHITE);
        }
    }

}
