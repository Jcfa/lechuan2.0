package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.bean.supplier.Supplier;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 目录列表适配器
 * Created by lenovo on 2016/12/14.
 */
public class SupplierAdapter extends BaseAdapter<SupplierAdapter.SupplierViewHolder, Supplier> {

    private ArrayList<GoodsSupplier> selects;

    private OnEditListener onEditListener;

    private OnDeleteListener onDeleteListener;

    public SupplierAdapter(Context context, ArrayList<Supplier> suppliers) {
        super(context, suppliers);
        selects = new ArrayList<>();
    }

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public SupplierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_supplier_item, parent, false));
    }

    @Override
    public void initItemView(SupplierViewHolder holder, final Supplier item, int position) {
        holder.supplier_item_select.setSelected(findSelect(item) != null);

//        Glide.with(context).load(item.supplier_logo).into(holder.supplier_item_img);
        holder.supplier_item_name.setText(TextUtil.trimToEmpty(item.supplier_shortname));
        holder.supplier_item_contact.setText(TextUtil.trimToEmpty(item.supplier_contacts));
        holder.supplier_item_mobile.setText(TextUtil.trimToEmpty(item.supplier_contacts_mobile));
        holder.supplier_item_mail.setText(TextUtil.trimToEmpty(item.supplier_mail));

        holder.supplier_item_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoodsSupplier select = findSelect(item);
                if (select != null) {
                    selects.remove(select);
                } else {
                    GoodsSupplier goodsSupplier = new GoodsSupplier();
                    goodsSupplier.supplier_id = item.supplier_id;
                    goodsSupplier.supplier_shortname = item.supplier_shortname;
                    goodsSupplier.supplier_logo = item.supplier_logo;
                    goodsSupplier.supplier_contacts = item.supplier_contacts;
                    goodsSupplier.supplier_contacts_mobile = item.supplier_contacts_mobile;
                    goodsSupplier.supplier_mail = item.supplier_mail;
                    selects.add(goodsSupplier);
                }
                notifyDataSetChanged();
            }
        });

        holder.supplier_item_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.onEdit(item);
                }
            }
        });

        holder.supplier_item_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(item);
                }
            }
        });

        if (position == getItemCount() - 1) {// 最后一条
            holder.supplier_item_group.setPadding(0, 0, 0, 150);
        } else if (position == 0) {// 第一条
            holder.supplier_item_group.setPadding(0, 0, 0, 0);
        } else {
            holder.supplier_item_group.setPadding(0, 0, 0, 0);
        }
    }

    protected GoodsSupplier findSelect(Supplier d) {
        for (GoodsSupplier select : selects) {
            if (TextUtil.equals(select.supplier_id, d.supplier_id)) {
                return select;
            }
        }
        return null;
    }

    public void setSelects(ArrayList<GoodsSupplier> selects) {
        this.selects.clear();
        if (selects != null) {
            this.selects.addAll(selects);
        }
    }

    public ArrayList<GoodsSupplier> getSelects() {
        return selects;
    }

    public interface OnEditListener {

        void onEdit(Supplier item);
    }

    public interface OnDeleteListener {

        void onDelete(Supplier item);
    }

    public class SupplierViewHolder extends BaseViewHolder {

        LinearLayout supplier_item_group;
        ImageView supplier_item_select;
        ImageView supplier_item_img;// 头像
        TextView supplier_item_name;// 名称
        TextView supplier_item_contact;// 联系人
        TextView supplier_item_mobile;// 手机
        TextView supplier_item_mail;// 邮箱
        ImageView supplier_item_edit;// 编辑
        ImageView supplier_item_delete;// 删除

        SupplierViewHolder(View itemView) {
            super(itemView);

            supplier_item_group = getView(R.id.supplier_item_group);
            supplier_item_select = getView(R.id.supplier_item_select);
            supplier_item_img = getView(R.id.supplier_item_img);
            supplier_item_name = getView(R.id.supplier_item_name);
            supplier_item_contact = getView(R.id.supplier_item_contact);
            supplier_item_mobile = getView(R.id.supplier_item_mobile);
            supplier_item_mail = getView(R.id.supplier_item_mail);
            supplier_item_edit = getView(R.id.supplier_item_edit);
            supplier_item_delete = getView(R.id.supplier_item_delete);

        }
    }

}
