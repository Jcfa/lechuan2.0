package com.poso2o.lechuan.fragment.publish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.publish.ArticleDetailsActivity;
import com.poso2o.lechuan.adapter.ArticleListAdapter;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.tool.recycler.OnSlideToBottomListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;
import static com.poso2o.lechuan.base.BaseManager.NEXT;

/**
 * 文章列表
 * Created by Jaydon on 2017/12/29.
 */
public class ArticleListFragment extends BaseFragment {

    private static final int REQUEST_DETAILS = 11;

    /**
     * 无数据时显示的视图
     */
    private TextView select_article_hint;

    /**
     * 列表
     */
    private RecyclerView select_article_list;
    private SwipeRefreshLayout select_article_swipe;
    private ArticleListAdapter articleListAdapter;

    /**
     * 文章类型
     */
    private int articlesType = ArticleDataManager.COLLECT;

    /**
     * 搜索关键词
     */
    private String keywords = "";

    public void setArticlesType(int articlesType) {
        this.articlesType = articlesType;
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void initView() {
        select_article_list = findView(R.id.select_article_list);
        LinearLayoutManager l = new LinearLayoutManager(context);
        l.setOrientation(LinearLayoutManager.VERTICAL);
        select_article_list.setLayoutManager(l);
        select_article_swipe = findView(R.id.select_article_swipe);
        select_article_swipe.setColorSchemeColors(Color.RED);

        select_article_hint = findView(R.id.select_article_hint);
    }

    @Override
    public void initData() {
        articleListAdapter = new ArticleListAdapter(context);
        select_article_list.setAdapter(articleListAdapter);
        articleListAdapter.setArticlesType(articlesType);

        select_article_swipe.setRefreshing(true);
        loadData(FIRST);
    }

    @Override
    public void initListener() {
        select_article_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData(FIRST);
            }
        });

        articleListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Article>() {

            @Override
            public void onItemClick(Article item) {
                startActivityForResult(ArticleDetailsActivity.class, item, REQUEST_DETAILS);
            }
        });

        select_article_list.addOnScrollListener(new OnSlideToBottomListener() {

            @Override
            protected void slideToBottom() {
                if (articleListAdapter.getItemCount() >= 20 && !select_article_swipe.isRefreshing()) {
                    loadData(NEXT);
                }
            }
        });

        select_article_hint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                select_article_hint.setVisibility(GONE);
                select_article_swipe.setRefreshing(true);
                loadData(FIRST);
            }
        });
    }

    public void search(String s) {
        if (!TextUtil.equals(keywords, s) && select_article_swipe != null) {
            keywords = s;
            select_article_swipe.setRefreshing(true);
            loadData(FIRST);
        }
    }

    public void loadData(final int pageType) {
        ArticleDataManager.getInstance().loadListData((BaseActivity) context, articlesType, keywords, pageType, new ArticleDataManager.OnLoadListDataCallback() {

            @Override
            public void onSucceed(ArrayList<Article> articles) {
                notifyDataSetChanged(pageType, articles);
                select_article_swipe.setRefreshing(false);
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(context, failMsg);
                notifyDataSetChanged(pageType, null);
                select_article_swipe.setRefreshing(false);
            }
        });
    }

    /**
     * 刷新数据
     * @param pageType
     * @param data
     */
    public void notifyDataSetChanged(int pageType, ArrayList<Article> data) {
        if (pageType == FIRST) {
            if (data == null) {
                select_article_hint.setVisibility(VISIBLE);
                select_article_hint.setText(R.string.hint_load_article_fail);
            } else if (ListUtils.isEmpty(data)) {
                select_article_hint.setVisibility(VISIBLE);
                if (articlesType == ArticleDataManager.COLLECT) {
                    select_article_hint.setText(R.string.hint_not_collect_article);
                } else {
                    select_article_hint.setText(R.string.hint_load_article_null);
                }
            } else {
                select_article_hint.setVisibility(GONE);
            }
        }
        select_article_hint.setVisibility(GONE);
        articleListAdapter.notifyDataSetChanged(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DETAILS:
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    break;
            }
        }
    }
}
