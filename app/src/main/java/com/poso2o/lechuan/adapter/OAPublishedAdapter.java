package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 已发布列表适配器
 * Created by Luo on 2017/02/10.
 */
public class OAPublishedAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<PosterDTO> posterData;
    private RequestManager glideRequest;

    public OAPublishedAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.posterData = new ArrayList<>();
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_oa_published_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return posterData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;
            final PosterDTO posterDTO = posterData.get(position);

            posterHolder.published_item_dat.setText("" + DateTimeUtil.LongToData(posterDTO.build_time, "yyyy-MM-dd HH:mm:ss"));

            if (posterDTO.news_img != null && !TextUtil.isEmpty(posterDTO.news_img) || posterDTO.news_img.length() > 0) {
                if (posterDTO.news_img.toLowerCase().contains(".gif")) {
                    glideRequest.load(posterDTO.news_img).placeholder(R.mipmap.logo_c).error(R.mipmap.logo_c).into(posterHolder.published_item_img);
                } else {
                    glideRequest.load(posterDTO.news_img).asBitmap().centerCrop().thumbnail(0.1f).placeholder(R.mipmap.logo_c).error(R.mipmap.logo_c).into(posterHolder.published_item_img);
                }
                posterHolder.published_item_img.setVisibility(View.VISIBLE);
            } else {
                posterHolder.published_item_img.setVisibility(View.GONE);
            }
            posterHolder.published_item_title.setText(posterDTO.news_title);

            posterHolder.poster_item_group.setTag(posterDTO);
            posterHolder.poster_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem(position, (PosterDTO) v.getTag());
                        return;
                    }
                }
            });



        }
    }

    public void notifyData(ArrayList<PosterDTO> posterData) {
        this.posterData.clear();
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<PosterDTO> posterData) {
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(int position, PosterDTO posterData);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View poster_item_group;
        TextView published_item_dat;
        ImageView published_item_img;
        TextView published_item_title;
        LinearLayout published_item_other;

        PosterHolder(View itemView) {
            super(itemView);
            poster_item_group = itemView.findViewById(R.id.poster_item_group);
            published_item_dat = (TextView) itemView.findViewById(R.id.published_item_dat);
            published_item_img = (ImageView) itemView.findViewById(R.id.published_item_img);
            published_item_title = (TextView) itemView.findViewById(R.id.published_item_title);
            published_item_other = (LinearLayout) itemView.findViewById(R.id.published_item_other);
        }
    }


}
