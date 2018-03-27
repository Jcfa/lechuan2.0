package com.poso2o.lechuan.fragment.oa;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.OAArticleListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.tool.recycler.OnSlideToBottomListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;
import static com.poso2o.lechuan.base.BaseManager.NEXT;

/**
 * 公众号文章列表
 *
 * Created by Jaydon on 2018/1/26.
 */
public class OAArticleListFragment extends BaseFragment {

    private static final int REQUEST_DETAILS = 11;

    /**
     * 无数据时显示的视图
     */
    private TextView oa_article_list_hint;

    /**
     * 列表
     */
    private RecyclerView oa_article_list_list;
    private SwipeRefreshLayout oa_article_list_swipe;
    private OAArticleListAdapter oaArticleListAdapter;

    private ArrayList<Article> article_datas = new ArrayList<>();

    /**
     * 文章类型
     */
    private int articlesType = ArticleDataManager.COLLECT;

    private BaseAdapter.OnItemClickListener onItemClickListener;

    public void setArticlesType(int articlesType) {
        this.articlesType = articlesType;
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oa_article_list, container, false);
    }

    @Override
    public void initView() {
        oa_article_list_list = findView(R.id.oa_article_list_list);
        oa_article_list_list.setLayoutManager(new LinearLayoutManager(context));
        oa_article_list_swipe = findView(R.id.oa_article_list_swipe);
        oa_article_list_hint = findView(R.id.oa_article_list_hint);
    }

    @Override
    public void initData() {
        oaArticleListAdapter = new OAArticleListAdapter(context);
        oa_article_list_list.setAdapter(oaArticleListAdapter);
        oaArticleListAdapter.setArticlesType(articlesType);

        oa_article_list_swipe.setRefreshing(true);
        loadData(FIRST);
    }

    @Override
    public void initListener() {
        oa_article_list_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData(FIRST);
            }
        });

        oaArticleListAdapter.setOnItemClickListener(onItemClickListener);

        oa_article_list_list.addOnScrollListener(new OnSlideToBottomListener() {

            @Override
            protected void slideToBottom() {
                if (oaArticleListAdapter.getItemCount() >= 20 && !oa_article_list_swipe.isRefreshing()) {
                    loadData(NEXT);
                }
            }
        });

        oa_article_list_hint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oa_article_list_hint.setVisibility(GONE);
                oa_article_list_swipe.setRefreshing(true);
                loadData(FIRST);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyList();
    }

    public void loadData(final int pageType) {
        ArticleDataManager.getInstance().loadListData((BaseActivity) context, articlesType, "", pageType, new ArticleDataManager.OnLoadListDataCallback() {

            @Override
            public void onSucceed(ArrayList<Article> articles) {
                notifyDataSetChanged(pageType, articles);
                oa_article_list_swipe.setRefreshing(false);
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(context, failMsg);
                notifyDataSetChanged(pageType, null);
                oa_article_list_swipe.setRefreshing(false);
            }
        });
    }

    public void reLoadData(){
        oa_article_list_swipe.setRefreshing(false);
        oa_article_list_swipe.setRefreshing(true);
        loadData(FIRST);
    }

    //刷新本地数据
    public void notifyList(){
        if (oaArticleListAdapter != null)oaArticleListAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * @param pageType
     * @param data
     */
    public void notifyDataSetChanged(int pageType, ArrayList<Article> data) {
        if (pageType == FIRST) {
            if (data == null) {
                oa_article_list_hint.setVisibility(VISIBLE);
                oa_article_list_hint.setText(R.string.hint_load_article_fail);
            } else if (ListUtils.isEmpty(data)) {
                oa_article_list_hint.setVisibility(VISIBLE);
                if (articlesType == ArticleDataManager.COLLECT) {
                    oa_article_list_hint.setText(R.string.hint_not_collect_article);
                } else {
                    oa_article_list_hint.setText(R.string.hint_load_article_null);
                }
            } else {
                oa_article_list_hint.setVisibility(GONE);
            }
            article_datas.clear();
        }
        if (data != null)article_datas.addAll(data);
        oaArticleListAdapter.notifyDataSetChanged(article_datas);
    }

    public void setOnItemClickListener(BaseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
