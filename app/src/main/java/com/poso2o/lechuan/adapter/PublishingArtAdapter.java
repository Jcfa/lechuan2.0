package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.dialog.DelPublishingDialog;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/26.
 */

public class PublishingArtAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ArticleData> mDatas ;

    public PublishingArtAdapter(Context context) {
        this.context = context;
    }

    public void notifyData(ArrayList<ArticleData> articleDatas){
        mDatas = articleDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.item_oa_publishing_head,parent,false);
            return new PublishingArtHeadVH(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_oa_publishing,parent,false);
            return new PublishingArtVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ArticleData article = mDatas.get(position);
        if (article == null)return;
        if (getItemViewType(position) == 0){
            PublishingArtHeadVH vh = (PublishingArtHeadVH) holder;
            vh.head_art_title.setText(article.title);
            if (article.pic != null && !article.pic.equals("")) Glide.with(context).load(article.pic).into(vh.publishing_head_pic);
            if (article.isAd){
                vh.head_is_add_ad.setTextColor(0xFF00BCB4);
                Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_ad_selected_blue_48);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                vh.head_is_add_ad.setCompoundDrawables(leftDrawable,null, null, null);
            }else {
                vh.head_is_add_ad.setTextColor(0xFF979797);

                Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_ad_select_48);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                vh.head_is_add_ad.setCompoundDrawables(leftDrawable,null, null, null);
            }
            vh.publishing_head_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelPublishingDialog publishingDialog = new DelPublishingDialog(context);
                    publishingDialog.show();
                    publishingDialog.setOnDelPublishingListener(new DelPublishingDialog.OnDelPublishingListener() {
                        @Override
                        public void onDelPublishing() {
                            if (onPublishingListener != null)onPublishingListener.onPublishingDel(article);
                        }
                    });
                }
            });
            vh.publishing_head_pic.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onPublishingListener != null)onPublishingListener.onPublishing(article);
                }
            });
        }else {
            PublishingArtVH vh = (PublishingArtVH) holder;
            vh.art_publishing_title.setText(article.title);
            if (article.pic != null && !article.pic.equals("")) Glide.with(context).load(article.pic).into(vh.art_publishing_pic);
            if (article.isAd){
                vh.art_publishing_ad.setTextColor(0xFF00BCB4);

                Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_ad_selected_blue_48);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                vh.art_publishing_ad.setCompoundDrawables(leftDrawable,null, null, null);
            } else {
                vh.art_publishing_ad.setTextColor(0xFF979797);

                Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_ad_select_48);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                vh.art_publishing_ad.setCompoundDrawables(leftDrawable,null, null, null);
            }

            vh.art_publishing_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelPublishingDialog publishingDialog = new DelPublishingDialog(context);
                    publishingDialog.show();
                    publishingDialog.setOnDelPublishingListener(new DelPublishingDialog.OnDelPublishingListener() {
                        @Override
                        public void onDelPublishing() {
                            if (onPublishingListener != null)onPublishingListener.onPublishingDel(article);
                        }
                    });
                }
            });
            vh.oa_publishing.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onPublishingListener != null)onPublishingListener.onPublishing(article);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)return 0;
        return 1;
    }

    class PublishingArtHeadVH extends RecyclerView.ViewHolder{

        ImageView publishing_head_pic;
        ImageButton publishing_head_del;
        TextView head_art_title;
        TextView head_is_add_ad;

        public PublishingArtHeadVH(View itemView) {
            super(itemView);
            publishing_head_pic = (ImageView) itemView.findViewById(R.id.publishing_head_pic);
            publishing_head_del = (ImageButton) itemView.findViewById(R.id.publishing_head_del);
            head_art_title = (TextView) itemView.findViewById(R.id.head_art_title);
            head_is_add_ad = (TextView) itemView.findViewById(R.id.head_is_add_ad);
        }
    }

    class PublishingArtVH extends RecyclerView.ViewHolder{

        RelativeLayout oa_publishing;
        ImageView art_publishing_pic;
        TextView art_publishing_title;
        ImageButton art_publishing_del;
        TextView art_publishing_ad;

        public PublishingArtVH(View itemView) {
            super(itemView);
            oa_publishing = (RelativeLayout) itemView.findViewById(R.id.oa_publishing);
            art_publishing_pic = (ImageView) itemView.findViewById(R.id.art_publishing_pic);
            art_publishing_title = (TextView) itemView.findViewById(R.id.art_publishing_title);
            art_publishing_del = (ImageButton) itemView.findViewById(R.id.art_publishing_del);
            art_publishing_ad = (TextView) itemView.findViewById(R.id.art_publishing_ad);
        }
    }

    private OnPublishingListener onPublishingListener;
    public interface OnPublishingListener{
        void onPublishingDel(ArticleData articleData);
        void onPublishing(ArticleData articleData);
    }
    public void setOnPublishingListener(OnPublishingListener onPublishingListener){
        this.onPublishingListener = onPublishingListener;
    }
}
