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
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.broadcast.BroadcastManager;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.LogUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

import static com.poso2o.lechuan.configs.Constant.BROADCAST_COLLECT;
import static com.poso2o.lechuan.configs.Constant.BROADCAST_SELECT_ARTICLE;
import static com.poso2o.lechuan.configs.Constant.BROADCAST_UNCOLLECT;

/**
 * 选择文章列表适配器
 * Created by Jaydon on 2017/12/1.
 */
public class OAArticleListAdapter extends BaseAdapter<OAArticleListAdapter.ArticleHolder, Article> {

    /**
     * 文章类型
     */
    private int articlesType = ArticleDataManager.COLLECT;

    private ArticleDataManager manager;

    private Context context;

    public OAArticleListAdapter(Context context) {
        super(context, null);
        this.context = context;
        manager = ArticleDataManager.getInstance();
    }

    public void setArticlesType(int articlesType) {
        this.articlesType = articlesType;
        initBroadcast();
    }

    private void initBroadcast() {
        BroadcastManager.registerReceiver(broadcastReceiver, BROADCAST_COLLECT, BROADCAST_UNCOLLECT, BROADCAST_SELECT_ARTICLE);
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
        return new ArticleHolder(getItemView(R.layout.item_oa_article, parent));
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
        // 选择/取消
        if (manager.findSelectData(item) != null) {
            holder.article_item_select.setBackgroundResource(R.drawable.selector_frame_cancel);
            holder.article_item_select.setTextColor(0xfff17604);
            holder.article_item_select.setText("- 取消");
        } else {
            holder.article_item_select.setBackgroundResource(R.drawable.selector_frame_confirm);
            holder.article_item_select.setTextColor(0xff00bcb4);
            holder.article_item_select.setText("+ 选择");
        }
        holder.article_item_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (manager.findSelectData(item) == null) {
                    manager.addSelectData(item);
                } else {
                    manager.removeSelectData(item);
                }
                notifyDataSetChanged();
            }
        });
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

        private TextView article_item_select;

        ArticleHolder(View itemView) {
            super(itemView);
            article_item_image = getView(R.id.article_item_image);
            article_item_title = getView(R.id.article_item_title);
            article_item_look = getView(R.id.article_item_look);
            article_item_collect = getView(R.id.article_item_collect);
            article_item_share = getView(R.id.article_item_share);
            article_item_select = getView(R.id.article_item_select);
        }
    }

}