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
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;

import java.util.ArrayList;

/**
 * 广告评论列表适配器
 * Created by Luo on 2017/02/10.
 */
public class PosterCommentAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<PosterCommentsDTO> fansBeen;
    private RequestManager glideRequest;

    public PosterCommentAdapter(Context context, OnItemListener onItemListener) {
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
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_poster_comment_item, parent, false));
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
                //posterHolder.poster_item_logo.setTag(R.mipmap.logo_c);
                glideRequest.load("" + fansBeen.get(position).logo).transform(new GlideCircleTransform(context)).into(posterHolder.poster_comment_item_img);
                //glideRequest.load(posterData.get(position).logo).placeholder(R.mipmap.logo_d).error(R.mipmap.logo_d).into(posterHolder.poster_item_logo);
            }else{
                posterHolder.poster_comment_item_img.setImageResource(R.mipmap.logo_c);
                //posterHolder.poster_item_logo.setTag(R.mipmap.logo_c);
            }

            posterHolder.poster_comment_item_name.setText("" + fansBeen.get(position).nick);
            posterHolder.poster_comment_item_remark.setText("" + fansBeen.get(position).comments);
            String date = DateTimeUtil.LongToData(fansBeen.get(position).build_time, "yyyy-MM-dd HH:mm:ss");
            posterHolder.poster_comment_item_dat.setText("" + date);

            posterHolder.poster_comment_item_group.setTag(fansBeen.get(position));
            posterHolder.poster_comment_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((PosterCommentsDTO)v.getTag());
                    }
                }
            });
        }
    }

    public void notifyData(ArrayList<PosterCommentsDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<PosterCommentsDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(PosterCommentsDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View poster_comment_item_group;
        ImageView poster_comment_item_img;
        TextView poster_comment_item_name;
        TextView poster_comment_item_remark;
        TextView poster_comment_item_dat;

        PosterHolder(View itemView) {
            super(itemView);
            poster_comment_item_group = itemView.findViewById(R.id.poster_comment_item_group);
            poster_comment_item_img = (ImageView) itemView.findViewById(R.id.poster_comment_item_img);
            poster_comment_item_name = (TextView) itemView.findViewById(R.id.poster_comment_item_name);
            poster_comment_item_remark = (TextView) itemView.findViewById(R.id.poster_comment_item_remark);
            poster_comment_item_dat = (TextView) itemView.findViewById(R.id.poster_comment_item_dat);
        }
    }
}
