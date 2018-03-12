package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;

import java.util.ArrayList;

/**
 * 图片列表
 * Created by lenovo on 2016/12/18.
 */
public class UploadImageAdapter extends BaseAdapter<UploadImageAdapter.ImageHolder, String> {

    /**
     * 点击事件回调
     */
    private OnCallbackListener onCallbackListener;

    public UploadImageAdapter(Context context, ArrayList<String> photoDatas) {
        super(context, photoDatas);
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(getItemView(R.layout.recycler_upload_image_item, null));
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void initItemView(final ImageHolder holder, final String item, int position) {
        if (data.size() == position) {
            holder.recycle_item_photo.setScaleType(ScaleType.FIT_CENTER);
            holder.recycle_item_photo.setImageResource(R.mipmap.icon_upload_image_add);
            holder.recycle_delete_photo.setVisibility(View.GONE);
            holder.recycle_add_goods_photo_group.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onCallbackListener.openPhoto();
                    holder.touchFlg = false;
                }
            });
        } else {
            holder.touchFlg = true;

            holder.recycle_delete_photo.setVisibility(View.VISIBLE);
            holder.recycle_delete_photo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onCallbackListener.deleteItem(item, holder.getAdapterPosition());
                }
            });
            holder.recycle_item_photo.setScaleType(ScaleType.CENTER_CROP);
            Glide.with(context).load(item).into(holder.recycle_item_photo);
//            holder.recycle_item_photo.setImageBitmap(item);

            holder.recycle_add_goods_photo_group.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onCallbackListener.shearImage(holder.getAdapterPosition());
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

    public void setOnCallbackListener(OnCallbackListener onCallbackListener) {
        this.onCallbackListener = onCallbackListener;
    }

    public interface OnCallbackListener {

        void openPhoto();

        void deleteItem(String item, int position);

        void shearImage(int position);
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