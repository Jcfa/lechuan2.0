package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.GoodsDetailsActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.util.TextUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 商品详情搭配条目
 * Created by tenac on 2017/1/6.
 */
public class GoodsSupplierItemView extends BaseView {

    private View view;

    /**
     * 套餐数据
     */
    private GoodsSupplier supplier;

    /**
     * 数据变动监听
     */
    private OnDataUpdateListener onDataUpdateListener;

    /**
     * 预览图
     */
    private ImageView edit_goods_supplier_img;

    /**
     * 标签
     */
    private ImageView edit_goods_supplier_tag;

    /**
     * 商品名称
     */
    private TextView edit_goods_supplier_name;

    /**
     * 联系人
     */
    private TextView edit_goods_supplier_contact;

    /**
     * 手机
     */
    private TextView edit_goods_supplier_mobile;

    /**
     * 手机
     */
    private TextView edit_goods_supplier_mail;

    /**
     * 删除按钮
     */
    private View edit_goods_supplier_delete;

    public GoodsSupplierItemView(Context context, GoodsSupplier supplier) {
        super(context);
        this.context = context;
        this.supplier = supplier;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_edit_goods_supplier_item, null);
        return view;
    }

    @Override
    public void initView() {
        edit_goods_supplier_img = (ImageView) view.findViewById(R.id.edit_goods_supplier_img);
        edit_goods_supplier_tag = (ImageView) view.findViewById(R.id.edit_goods_supplier_tag);
        edit_goods_supplier_name = (TextView) view.findViewById(R.id.edit_goods_supplier_name);
        edit_goods_supplier_contact = (TextView) view.findViewById(R.id.edit_goods_supplier_contact);
        edit_goods_supplier_mobile = (TextView) view.findViewById(R.id.edit_goods_supplier_mobile);
        edit_goods_supplier_mail = (TextView) view.findViewById(R.id.edit_goods_supplier_mail);
        edit_goods_supplier_delete = view.findViewById(R.id.edit_goods_supplier_delete);
    }

    @Override
    public void initData() {
        if (supplier != null) {
            edit_goods_supplier_name.setText(TextUtil.trimToEmpty(supplier.supplier_shortname));
            edit_goods_supplier_contact.setText(TextUtil.trimToEmpty(supplier.supplier_contacts));
            edit_goods_supplier_mobile.setText(TextUtil.trimToEmpty(supplier.supplier_contacts_mobile));
            edit_goods_supplier_mail.setText(TextUtil.trimToEmpty(supplier.supplier_mail));
            setTagSelect(supplier.has_default == 1);
        }
        initListener();
    }

    public void initListener() {
        if (context instanceof GoodsDetailsActivity) {
            edit_goods_supplier_delete.setVisibility(GONE);
            view.findViewById(R.id.edit_goods_supplier_separator).setVisibility(GONE);
        } else {
            // 删除本条目
            edit_goods_supplier_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onDataUpdateListener != null) {
                        onDataUpdateListener.onDeleteItem(supplier, getRootView());
                    }
                }
            });

            // 点击标签
            edit_goods_supplier_tag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onDataUpdateListener.onTagClick(supplier, getRootView());
                }
            });
        }
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public void setTagSelect(boolean b) {
        if (b) {
            supplier.has_default = 1;
            edit_goods_supplier_tag.setImageResource(R.mipmap.supplier_tag_on);
            edit_goods_supplier_tag.setVisibility(VISIBLE);
        } else {
            supplier.has_default = 0;
            edit_goods_supplier_tag.setImageResource(R.mipmap.supplier_tag_off);

            if (context instanceof GoodsDetailsActivity) {
                edit_goods_supplier_tag.setVisibility(GONE);
            }
        }
    }

    public interface OnDataUpdateListener {

        void onTagClick(GoodsSupplier supplier, View view);

        void onDeleteItem(GoodsSupplier supplierData, View view);
    }
}