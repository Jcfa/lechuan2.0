package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.EditGoodsActivity;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 *
 * Created by lenovo on 2016/12/18.
 */
public class EditGoodsImageAdapter extends BaseAdapter<EditGoodsImageAdapter.ImageHolder, Bitmap> {

    public EditGoodsImageAdapter(Context context, ArrayList<Bitmap> photoDatas) {
        super(context, photoDatas);
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(getItemView(R.layout.recycle_edit_goods_photo_item, null));
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, int position) {
        if (data.size() == position) {
            holder.recycle_item_photo.setScaleType(ScaleType.FIT_CENTER);
            holder.recycle_item_photo.setImageResource(R.mipmap.edit_goods_add_photo);
            holder.recycle_delete_photo.setVisibility(View.GONE);
            holder.recycle_add_goods_photo_group.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    ((EditGoodsActivity) context).toPhoto();
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
                    ((EditGoodsActivity) context).deleteImage(item, holder.getAdapterPosition());
                }
            });
            holder.recycle_item_photo.setScaleType(ScaleType.CENTER_CROP);
            holder.recycle_item_photo.setImageBitmap(item);

            holder.recycle_add_goods_photo_group.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    ((EditGoodsActivity) context).shearPhoto(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void initItemView(final ImageHolder holder, final Bitmap item, int position) {
    }

    @Override
    public int getItemCount() {
        if (data.size() == 8 || data.size() > 8) {
            return 8;
        } else {
            return data.size() + 1;
        }
    }

    public class ImageHolder extends BaseViewHolder {
        RelativeLayout recycle_add_goods_photo_group;
        ImageView recycle_item_photo;
        ImageView recycle_delete_photo;

        ImageHolder(View itemView) {
            super(itemView);
            recycle_add_goods_photo_group = (RelativeLayout) itemView.findViewById(R.id.recycle_add_goods_photo_group);
            recycle_item_photo = (ImageView) itemView.findViewById(R.id.recycle_item_photo);
            recycle_delete_photo = (ImageView) itemView.findViewById(R.id.recycle_delete_photo);
        }
    }
}