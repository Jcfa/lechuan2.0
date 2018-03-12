package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-03.
 */

public class OAPublishEditAdapter extends RecyclerView.Adapter {
    private Context context;
    private static final int STICK_TYPE = 1;
    private static final int NOT_STICK_TYPE = 2;
    private static final int ADD_TYPE = 3;
    private ArrayList<Article> mData = new ArrayList<>();

    public OAPublishEditAdapter(Context context, ArrayList<Article> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STICK_TYPE) {
            return new OAPublishEditAdapter.ArticleHolder(LayoutInflater.from(context).inflate(R.layout.item_oa_publish_article_stick, parent, false));
        } else if (viewType == ADD_TYPE) {
            return new OAPublishEditAdapter.ArticleAddHolder(LayoutInflater.from(context).inflate(R.layout.item_oa_publish_article_add, parent, false));
        } else {
            return new OAPublishEditAdapter.ArticleHolder(LayoutInflater.from(context).inflate(R.layout.item_oa_publish_article, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (showAddView(position)) {
            final ArticleAddHolder articleAddHolder = (ArticleAddHolder) holder;
            articleAddHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (oaPublishListener != null) {
                        oaPublishListener.onEdit();
                    }
                }
            });
            articleAddHolder.tvArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (oaPublishListener != null) {
                        oaPublishListener.onArticle();
                    }
                }
            });
        } else {
            if (mData == null || mData.size() == 0)return;
            final ArticleHolder articleHolder = (ArticleHolder) holder;
            final Article article = mData.get(position);
            Glide.with(context).load(article.pic).thumbnail(0.1f).into(articleHolder.article_image);
            articleHolder.article_title.setText(article.title);
            articleHolder.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (oaPublishListener != null) {
                        oaPublishListener.onItemClick(position, article);
                    }
                }
            });
            articleHolder.layoutMain.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showLongClickWindow(view, position, article);
                    return true;
                }
            });
            articleHolder.article_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (oaPublishListener != null) {
                        oaPublishListener.onItemAddAdvertised(position, article);
                    }
                }
            });
            articleHolder.article_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (oaPublishListener != null) {
                        oaPublishListener.onItemDelete(position, article);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null && mData.size() != 0 && position == 0) {
            return STICK_TYPE;
        } else if (position == mData.size() && mData.size() < 8) {
            return ADD_TYPE;
        } else {
            return NOT_STICK_TYPE;
        }
    }

    private boolean showAddView(int position) {
        return getItemViewType(position) == ADD_TYPE;
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 1;
        } else if (mData.size() > 8) {
            return 8;
        }
        return mData.size() + 1;
    }

    public void notifyDataSetChanged(ArrayList<Article> articles) {
        this.mData = articles;
        notifyDataSetChanged();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        private ImageView article_image;
        private TextView article_title;
        private ImageView article_close;
        private TextView article_add;
        private RelativeLayout layoutMain;

        ArticleHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.item_main);
            article_image = (ImageView) itemView.findViewById(R.id.iv_article_image);
            article_title = (TextView) itemView.findViewById(R.id.tv_article_title);
            article_close = (ImageView) itemView.findViewById(R.id.iv_article_close);
            article_add = (TextView) itemView.findViewById(R.id.iv_add_advertised);
        }
    }

    class ArticleAddHolder extends RecyclerView.ViewHolder {
        private TextView tvArticle;
        private TextView tvEdit;

        ArticleAddHolder(View itemView) {
            super(itemView);
            tvArticle = (TextView) itemView.findViewById(R.id.tv_article);
            tvEdit = (TextView) itemView.findViewById(R.id.tv_edit);
        }
    }

    /**
     * 显示长按操作菜单
     */
    private void showLongClickWindow(View view, final int position, final Article article) {
        // 一个自定义的布局，作为显示的内容
        View contentView = View.inflate(context, R.layout.popup_oa_publish_menu, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ScreenInfo.dpCpx(32), true);
        // 设置封面
        contentView.findViewById(R.id.oa_publish_set_stick).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oaPublishListener != null) {
                    oaPublishListener.setStick(position, article);
                }
                popupWindow.dismiss();
            }
        });
        //存入草稿箱
        contentView.findViewById(R.id.oa_publish_save_draft).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oaPublishListener != null) {
                    oaPublishListener.saveDraft(position, article);
                }
                popupWindow.dismiss();
            }
        });
        //上移
        contentView.findViewById(R.id.oa_publish_move_up).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oaPublishListener != null) {
                    oaPublishListener.moveUp(position, article);
                }
                popupWindow.dismiss();
            }
        });
        //下移
        contentView.findViewById(R.id.oa_publish_move_down).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oaPublishListener != null) {
                    oaPublishListener.moveDown(position, article);
                }
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));

        // 计算偏移量
//        int x = view.getWidth() - ScreenInfo.dpCpx(128);
//        if (x < 0) x = 0;
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public OAPublishListener oaPublishListener;

    public void setOaPublishListener(OAPublishListener oaPublishListener) {
        this.oaPublishListener = oaPublishListener;
    }

    public interface OAPublishListener {
        public void onEdit();//自由编辑

        public void onArticle();//选文发布

        public void onItemClick(int position, Article article);//item点击

        public void onItemDelete(int position, Article article);//删除item

        public void onItemAddAdvertised(int position, Article article);//item添加广告

        public void moveUp(int position, Article article);//上移

        public void moveDown(int position, Article article);//下移

        public void setStick(int position, Article article);//设置封面

        public void saveDraft(int position, Article article);//保存草稿箱
    }
}
