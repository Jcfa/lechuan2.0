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
import com.poso2o.lechuan.activity.realshop.ArticleSearchActivity;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/26.
 */

public class OASearchArtAdapter extends RecyclerView.Adapter {

    private ArticleSearchActivity context;
    private ArrayList<ArticleData> mDatas;
    private ArrayList<ArticleData> selectData;

    public OASearchArtAdapter(ArticleSearchActivity context) {
        this.context = context;
    }

    public void notifyDatas(ArrayList<ArticleData> articleDatas,ArrayList<ArticleData> selectData) {
        mDatas = articleDatas;
        this.selectData = selectData;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_information, parent, false);
        return new ArticleVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final ArticleData articleData = mDatas.get(position);
        if (articleData == null) return;
        final ArticleVH vh = (ArticleVH) holder;
        if (articleData.pic != null && !articleData.pic.equals(""))
            Glide.with(context).load(articleData.pic).into(vh.oa_article_pic);
        vh.oa_article_title.setText(articleData.title);
        vh.oa_article_favorite.setText(articleData.collectnums + "");
        if (articleData.has_collect == 0) {
            Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_favorite_no_48);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            vh.oa_article_favorite.setCompoundDrawables(leftDrawable, null, null, null);
        } else if (articleData.has_collect == 1) {
            Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.icon_favorite_48);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            vh.oa_article_favorite.setCompoundDrawables(leftDrawable, null, null, null);
        }
        vh.oa_article_share.setText(articleData.sharenums + "");

        if (selectData != null && selectData.size() != 0) {
            boolean isIn = false;
            for (ArticleData art : selectData) {
                if (art.articles_id.equals(articleData.articles_id)) {
                    isIn = true;
                    break;
                }
            }
            if (isIn) {
                vh.oa_article_add.setImageResource(R.mipmap.icon_reduce_64_00bcb4);
                articleData.inLine = true;
            } else {
                articleData.inLine = false;
                vh.oa_article_add.setImageResource(R.mipmap.icon_plus_64_00bcb4);
            }
        } else {
            articleData.inLine = false;
            vh.oa_article_add.setImageResource(R.mipmap.icon_plus_64_00bcb4);
        }

        vh.oa_article_favorite.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onArticleListener != null)
                    onArticleListener.onFavoriteClick(position, articleData);
            }
        });

        vh.oa_article_add.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ArticleData article = null;
                for (ArticleData temp : selectData) {
                    if (temp.articles_id.equals(articleData.articles_id)) {
                        article = temp;
                        break;
                    }
                }
                if (article == null) {
                    if (selectData.size() == 8){
                        Toast.show(context,"发布文章不能超过八篇");
                        return;
                    }
                    articleData.inLine = true;
                    vh.oa_article_add.setImageResource(R.mipmap.icon_reduce_64_00bcb4);
                    if (onArticleListener != null)onArticleListener.onSendClick(position,articleData,true);
                } else {
                    articleData.inLine = false;
                    vh.oa_article_add.setImageResource(R.mipmap.icon_plus_64_00bcb4);
                    if (onArticleListener != null)onArticleListener.onSendClick(position,articleData,false);
                }
            }
        });

        vh.item_oa_article.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onArticleListener != null)
                    onArticleListener.onArticleClick(position, articleData);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size();
    }

    class ArticleVH extends RecyclerView.ViewHolder {

        RelativeLayout item_oa_article;
        ImageView oa_article_pic;
        TextView oa_article_title;
        TextView oa_article_focus;
        TextView oa_article_favorite;
        TextView oa_article_share;
        ImageButton oa_article_add;

        public ArticleVH(View itemView) {
            super(itemView);
            item_oa_article = (RelativeLayout) itemView.findViewById(R.id.item_oa_article);
            oa_article_pic = (ImageView) itemView.findViewById(R.id.oa_article_pic);
            oa_article_title = (TextView) itemView.findViewById(R.id.oa_article_title);
            oa_article_focus = (TextView) itemView.findViewById(R.id.oa_article_focus);
            oa_article_favorite = (TextView) itemView.findViewById(R.id.oa_article_favorite);
            oa_article_share = (TextView) itemView.findViewById(R.id.oa_article_share);
            oa_article_add = (ImageButton) itemView.findViewById(R.id.oa_article_add);
        }
    }

    private OnArticleListener onArticleListener;

    public interface OnArticleListener {
        void onFavoriteClick(int position, ArticleData articleData);

        void onSendClick(int position, ArticleData articleData,boolean isAdd);

        void onArticleClick(int position, ArticleData articleData);
    }

    public void setOnArticleListener(OnArticleListener onArticleListener) {
        this.onArticleListener = onArticleListener;
    }
}
