package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.OfficalAccountActivity;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/23.
 */

public class OAArticleAdapter extends RecyclerView.Adapter {

    private OfficalAccountActivity context;
    private ArrayList<ArticleData> mDatas;
    private ArrayList<ArticleData> selectData;

    public OAArticleAdapter(Context context) {
        this.context = (OfficalAccountActivity) context;
    }

    public void notifyDatas(ArrayList<ArticleData> articleDatas) {
        mDatas = articleDatas;
        selectData = context.getSelectedArt();
        notifyDataSetChanged();
    }

    public void notifyAdapter() {
        //不刷新数据，刷新view的状态
        selectData = context.getSelectedArt();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_information, parent, false);
        return new ArticleVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            if (onArticleListener != null)onArticleListener.onLoadMore(vh.load_tips,vh.load_progress);
            return;
        }
        final ArticleData articleData = mDatas.get(position);
        if (articleData == null) return;
        //同步刷新前的本地字段数据
        for (ArticleData articleData1 : context.getSelectedArt()){
            if (articleData1.articles_id.equals(articleData.articles_id)){
                articleData.inLine = articleData1.inLine;
                articleData.isAd = articleData1.isAd;
                articleData.goods = articleData1.goods;
                articleData.ad_des = articleData1.ad_des;
                articleData.pic_path = articleData1.pic_path;
                articleData.cover_pic_url = articleData1.cover_pic_url;
                break;
            }
        }
        final ArticleVH vh = (ArticleVH) holder;
        if (articleData.pic != null && !articleData.pic.equals(""))
            Glide.with(context).load(articleData.pic).into(vh.oa_article_pic);
        vh.oa_article_title.setText(articleData.title);
        vh.oa_article_favorite.setText(articleData.collectnums + "");

        //本地处理，收藏状态改变时，不用重新拿网络数据也可以实时更新
        articleData.has_collect = 0;
        for (ArticleData art : context.getFavoriteArt()){
            Print.println("测试收藏：" + art.articles_id + " : " + articleData.articles_id);
            if (art.articles_id.equals(articleData.articles_id)){
                articleData.has_collect = 1;
                break;
            }
        }

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

        if (selectData != null || selectData.size() != 0) {
            boolean isIn = false;
            for (ArticleData art : selectData) {
                if (art.articles_id.equals(articleData.articles_id)) {
                    isIn = true;
                    break;
                }
            }
            if (isIn) {
                vh.oa_article_add.setImageResource(R.mipmap.icon_reduce_64_00bcb4);
            } else {
                vh.oa_article_add.setImageResource(R.mipmap.icon_plus_64_00bcb4);
            }
        } else {
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
                    if (context.getSelectedArt().size() == 8) {
                        Toast.show(context, "发布文章不能超过8篇");
                    } else {
                        context.addSelectArt(articleData);
                        vh.oa_article_add.setImageResource(R.mipmap.icon_reduce_64_00bcb4);
                    }
                } else {
                    context.removeSelectArt(article);
                    vh.oa_article_add.setImageResource(R.mipmap.icon_plus_64_00bcb4);
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
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || position == mDatas.size())return 1;
        return 0;
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

        void onSendClick(int position, ArticleData articleData);

        void onArticleClick(int position, ArticleData articleData);

        void onLoadMore(TextView textView, ProgressBar progressBar);
    }

    public void setOnArticleListener(OnArticleListener onArticleListener) {
        this.onArticleListener = onArticleListener;
    }

}
