package com.poso2o.lechuan.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.broadcast.BroadcastManager;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.LogUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import static com.poso2o.lechuan.configs.Constant.BROADCAST_COLLECT;
import static com.poso2o.lechuan.configs.Constant.BROADCAST_UNCOLLECT;

/**
 * 选择文章列表适配器
 * Created by Jaydon on 2017/12/1.
 */
public class ArticleListAdapter extends BaseAdapter<ArticleListAdapter.ArticleHolder, Article> {

    /**
     * 文章类型
     */
    private int articlesType = ArticleDataManager.COLLECT;

    public ArticleListAdapter(Context context) {
        super(context, null);
    }

    public void setArticlesType(int articlesType) {
        this.articlesType = articlesType;
        initBroadcast();
    }

    private void initBroadcast() {
        BroadcastManager.registerReceiver(broadcastReceiver, BROADCAST_COLLECT, BROADCAST_UNCOLLECT);
        ((BaseActivity) context).addOnDestroyListener(new BaseActivity.OnDestroyListener() {

            @Override
            public void onDestroy() {
                BroadcastManager.unregisterReceiver(broadcastReceiver);
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtil.equals(intent.getAction(), BROADCAST_COLLECT)) {// 收藏
                Article item = (Article) intent.getSerializableExtra(Constant.DATA);
                if (item != null) {
                    if (articlesType == ArticleDataManager.COLLECT) {
                        addItem(item);
                    }
                }
            } else if (TextUtil.equals(intent.getAction(), BROADCAST_UNCOLLECT)) {// 取消收藏
                Article item = (Article) intent.getSerializableExtra(Constant.DATA);
                if (item != null) {
                    if (articlesType == ArticleDataManager.COLLECT) {
                        removeItem(item);
                    } else {
                        updateItem(item);
                    }
                }
            }
        }
    };

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArticleHolder(getItemView(R.layout.item_select_article, parent));
    }

    @Override
    public void initItemView(ArticleHolder holder, final Article item, final int position) {
        // 图片
        if (TextUtil.isNotEmpty(item.pic)) {
            Glide.with(context).load(item.pic).listener(requestListener).dontAnimate().into(holder.article_item_image);
        }
        // 标题
        holder.article_item_title.setText(item.title);
        // 阅读
        holder.article_item_look.setText(Long.toString(item.readnums));
        // 收藏
        holder.article_item_collect.setText(Long.toString(item.collectnums));
        if (item.isCollect()) {
            holder.article_item_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_collect_selected, 0, 0, 0);
        } else {
            holder.article_item_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_collect, 0, 0, 0);
        }
        holder.article_item_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                collect(item, (TextView) v, position);
            }
        });
        // 分享
        holder.article_item_share.setText(Long.toString(item.sharenums));
    }

    private RequestListener requestListener = new RequestListener() {

        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            LogUtils.d("onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            LogUtils.e("model:" + model + " isFirstResource: " + isFirstResource);
            return false;
        }
    };

    private void collect(final Article item, final TextView tv_collect, final int position) {
        ((BaseActivity) context).showLoading();
        ArticleDataManager.getInstance().collect((BaseActivity) context, item.articles_id, item.has_collect, new ArticleDataManager.OnCollectCallback() {

            @Override
            public void onSucceed(Article article) {
                item.has_collect = item.has_collect == 1 ? 0 : 1;
                // 发送广播
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, item);
                if (item.isCollect()) {
                    item.collectnums++;
                    tv_collect.setText(Long.toString(item.collectnums));
                    tv_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_collect_selected, 0, 0, 0);
                    intent.setAction(BROADCAST_COLLECT);
                } else {
                    item.collectnums--;
                    if (articlesType == ArticleDataManager.COLLECT) {
                        data.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        tv_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_collect, 0, 0, 0);
                        tv_collect.setText(Long.toString(item.collectnums));
                    }
                    intent.setAction(BROADCAST_UNCOLLECT);
                }
                BroadcastManager.sendBroadcast(intent);
                ((BaseActivity) context).dismissLoading();
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(context, failMsg);
                ((BaseActivity) context).dismissLoading();
            }
        });
    }

    class ArticleHolder extends BaseViewHolder {

        private ImageView article_item_image;

        private TextView article_item_title;

        private TextView article_item_look;

        private TextView article_item_collect;

        private TextView article_item_share;

        ArticleHolder(View itemView) {
            super(itemView);
            article_item_image = getView(R.id.article_item_image);
            article_item_title = getView(R.id.article_item_title);
            article_item_look = getView(R.id.article_item_look);
            article_item_collect = getView(R.id.article_item_collect);
            article_item_share = getView(R.id.article_item_share);
        }
    }
}