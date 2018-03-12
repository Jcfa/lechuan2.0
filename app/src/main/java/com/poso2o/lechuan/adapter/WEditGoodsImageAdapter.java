package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.EditGoodsActivity;
import com.poso2o.lechuan.activity.wshop.WEditGoodsActivity;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/23.
 */

public class WEditGoodsImageAdapter extends BaseAdapter<WEditGoodsImageAdapter.WImageHolder, Bitmap>{

    public WEditGoodsImageAdapter(Context context, ArrayList<Bitmap> data) {
        super(context, data);
    }

    @Override
    public void initItemView(WImageHolder holder, Bitmap item, int position) {

    }

    @Override
    public WImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WImageHolder(getItemView(R.layout.recycle_edit_goods_photo_item, null));
    }

    @Override
    public void onBindViewHolder(final WImageHolder holder, int position) {
        if (data.size() == position) {
            holder.recycle_item_photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.recycle_item_photo.setImageResource(R.mipmap.edit_goods_add_photo);
            holder.recycle_delete_photo.setVisibility(View.GONE);
            holder.recycle_add_goods_photo_group.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    ((WEditGoodsActivity) context).toPhoto();
                    holder.touchFlg = false;
                }
            });
        } else {
            final Bitmap item  = getItem(position);

            holder.touchFlg = true;

            holder.recycle_delete_photo.setVisibility(View.VISIBLE);
            holder.recycle_delete_photo.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    ((WEditGoodsActivity) context).deleteImage(item, holder.getAdapterPosition());
                }
            });
            holder.recycle_item_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.recycle_item_photo.setImageBitmap(item);

            holder.recycle_add_goods_photo_group.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    ((WEditGoodsActivity) context).shearPhoto(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() == 8 || data.size() > 8) {
            return 8;
        } else {
            return data.size() + 1;
        }
    }

    public class WImageHolder extends BaseViewHolder {
        RelativeLayout recycle_add_goods_photo_group;
        ImageView recycle_item_photo;
        ImageView recycle_delete_photo;

        WImageHolder(View itemView) {
            super(itemView);
            recycle_add_goods_photo_group = (RelativeLayout) itemView.findViewById(R.id.recycle_add_goods_photo_group);
            recycle_item_photo = (ImageView) itemView.findViewById(R.id.recycle_item_photo);
            recycle_delete_photo = (ImageView) itemView.findViewById(R.id.recycle_delete_photo);
        }
    }
}
