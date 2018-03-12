package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.ShopDetailsCommentsDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;

import java.util.ArrayList;

/**
 * 广告店铺评论列表适配器
 * Created by Luo on 2017/02/10.
 */
public class ShopCommentAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<ShopDetailsCommentsDTO> fansBeen;
    private RequestManager glideRequest;

    public ShopCommentAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_shop_comment_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            if (!fansBeen.get(position).logo.isEmpty()){
                //posterHolder.shop_item_logo.setTag(R.mipmap.logo_c);
                glideRequest.load("" + fansBeen.get(position).logo).transform(new GlideCircleTransform(context)).into(posterHolder.shop_comment_item_img);
                //glideRequest.load(posterData.get(position).logo).placeholder(R.mipmap.logo_d).error(R.mipmap.logo_d).into(posterHolder.shop_item_logo);
            }else{
                posterHolder.shop_comment_item_img.setImageResource(R.mipmap.logo_c);
                //posterHolder.shop_item_logo.setTag(R.mipmap.logo_c);
            }

            posterHolder.shop_comment_item_name.setText("" + fansBeen.get(position).nick);
            posterHolder.shop_comment_item_remark.setText("" + fansBeen.get(position).comments);
            String date = DateTimeUtil.LongToData(fansBeen.get(position).build_time, "yyyy-MM-dd HH:mm:ss");
            posterHolder.shop_comment_item_dat.setText("" + date);

            posterHolder.shop_comment_item_group.setTag(fansBeen.get(position));
            posterHolder.shop_comment_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((ShopDetailsCommentsDTO)v.getTag());
                    }
                }
            });
        }
    }

    public void notifyData(ArrayList<ShopDetailsCommentsDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<ShopDetailsCommentsDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(ShopDetailsCommentsDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View shop_comment_item_group;
        ImageView shop_comment_item_img;
        TextView shop_comment_item_name;
        TextView shop_comment_item_remark;
        TextView shop_comment_item_dat;

        PosterHolder(View itemView) {
            super(itemView);
            shop_comment_item_group = itemView.findViewById(R.id.shop_comment_item_group);
            shop_comment_item_img = (ImageView) itemView.findViewById(R.id.shop_comment_item_img);
            shop_comment_item_name = (TextView) itemView.findViewById(R.id.shop_comment_item_name);
            shop_comment_item_remark = (TextView) itemView.findViewById(R.id.shop_comment_item_remark);
            shop_comment_item_dat = (TextView) itemView.findViewById(R.id.shop_comment_item_dat);
        }
    }
}
