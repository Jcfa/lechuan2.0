package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.RenewalsBean;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/12.
 *
 * A稿件箱列表
 */

public class RenewalsAdapter extends BaseAdapter<RenewalsAdapter.RenewalsVH,RenewalsBean> {

    private Context context;

    public RenewalsAdapter(Context context, ArrayList data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public void initItemView(RenewalsVH holder,final RenewalsBean item, int position) {
        if (item == null || item.articles == null)return;
        Glide.with(context).load(item.articles.pic).into(holder.articleImage);
        holder.articleTitle.setText(item.articles.title);
        holder.articleLook.setText(item.articles.readnums + "");
        holder.articleCollect.setText(item.articles.collectnums + "");
        holder.articleShare.setText(item.articles.sharenums + "");

        holder.icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRenewalDelListener != null)onRenewalDelListener.onRenewalDel(item);
            }
        });
    }

    @Override
    public RenewalsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_publish_draft,parent,false);
        return new RenewalsVH(view);
    }

    class RenewalsVH extends BaseViewHolder{

        ImageView articleImage;
        TextView articleTitle;
        TextView articleLook;
        TextView articleCollect;
        TextView articleShare;
        ImageView icon_delete;

        public RenewalsVH(View itemView) {
            super(itemView);
            articleImage =  (ImageView) itemView.findViewById(R.id.article_item_image);
            articleTitle = (TextView) itemView.findViewById(R.id.article_item_title);
            articleLook = (TextView) itemView.findViewById(R.id.article_item_look);
            articleCollect = (TextView) itemView.findViewById(R.id.article_item_collect);
            articleShare = (TextView) itemView.findViewById(R.id.article_item_share);
            icon_delete = (ImageView) itemView.findViewById(R.id.icon_delete);
        }
    }

    private OnRenewalDelListener onRenewalDelListener;
    public interface OnRenewalDelListener{
        void onRenewalDel(RenewalsBean renewalsBean);
    }
    public void setOnRenewalDelListener(OnRenewalDelListener onRenewalDelListener){
        this.onRenewalDelListener = onRenewalDelListener;
    }
}
