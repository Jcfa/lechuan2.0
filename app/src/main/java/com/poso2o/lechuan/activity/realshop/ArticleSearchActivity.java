package com.poso2o.lechuan.activity.realshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.activity.oa.ArticleInfoActivity;
import com.poso2o.lechuan.adapter.ArticleSearchHistoryAdapter;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.OAArticleListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.dialog.ClearHistoryDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;

/**
 * Created by mr zhang on 2017/10/24.
 */

public class ArticleSearchActivity extends BaseActivity implements View.OnClickListener ,ArticleSearchHistoryAdapter.OnHistoryListener{

    public static final String SEARCH_ARTICLE_CHANGE_BROAD = "search_art_change";
    public static final String ACTION_ART_CHANGE = "search_art_change";
    //搜索文章跳转到温航详情页标识
    public static final String FROM_ART_SEARCH = "from_search";

    private Context context;

    //返回
    private ImageView article_search_back;
    //搜索输入框
    private EditText article_search_input;
    //清除输入
    private ImageButton clear_input;
    //搜索按钮
    private TextView article_search;

    //显示历史记录
    private LinearLayout search_history;
    //清除历史搜索
    private ImageView article_history_clear;
    //搜索历史列表
    private RecyclerView search_history_recycler;

    //显示搜索结果
    private RelativeLayout article_search_result;
    //搜索结果列表
    private RecyclerView article_search_recycler;
    //无返回结果提示
    private TextView tips_no_article;

    //搜索历史列表适配器
    private ArticleSearchHistoryAdapter historyAdapter;
    //搜索结果列表适配器
    private OAArticleListAdapter articleAdapter;
//    private OASearchArtAdapter articleAdapter;

    //历史缓存列表
    private ArrayList<String> historyList ;

    //已选中的将要发布的文章
    private ArrayList<ArticleData> selectArt ;
    //跳转到详情页添加或取消发布变动通知
    private SearchArtDetailBroadcase broadcase;

    private int articlesType = ArticleDataManager.FASHION;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_search;
    }

    @Override
    public void initView() {
        context = this;

        article_search_back = (ImageView) findViewById(R.id.article_search_back);

        article_search_input = (EditText) findViewById(R.id.article_search_input);

        clear_input = (ImageButton) findViewById(R.id.clear_input);

        article_search = (TextView) findViewById(R.id.article_search);

        search_history = (LinearLayout) findViewById(R.id.search_history);

        article_history_clear = (ImageView) findViewById(R.id.article_history_clear);

        search_history_recycler = (RecyclerView) findViewById(R.id.search_history_recycler);

        article_search_result = (RelativeLayout) findViewById(R.id.article_search_result);

        article_search_recycler = (RecyclerView) findViewById(R.id.article_search_recycler);

        tips_no_article = (TextView) findViewById(R.id.tips_no_article);
    }

    @Override
    public void initData() {
        broadcase = new SearchArtDetailBroadcase();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ART_CHANGE);
        registerReceiver(broadcase,filter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            selectArt = (ArrayList<ArticleData>) bundle.get(OfficalAccountActivity.SENDING_ARTICLE_DATA);
        }else {
            selectArt = new ArrayList<>();
        }
        initHistory();
    }

    @Override
    public void initListener() {
        article_search_back.setOnClickListener(this);
        article_search.setOnClickListener(this);
        clear_input.setOnClickListener(this);
        article_history_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_search_back:
                finish();
                break;
            case R.id.clear_input:
                article_search_input.setText(null);
                break;
            case R.id.article_search:
                judgeSearch();
                break;
            case R.id.article_history_clear:
                showClearDialog();
                break;
        }
    }

    @Override
    public void onHistoryClick(String keyword) {
        article_search_input.setText(keyword);
        article_search_input.setSelection(keyword.length());
        toSearchArticle(keyword,FIRST);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (articleAdapter != null)articleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcase);
        super.onDestroy();
    }

    //点击搜索
    private void judgeSearch() {
        String str = article_search_input.getText().toString();
        if (str == null || str.equals("")) {
            Toast.show(this, "请输入搜索内容");
        } else {
            saveHistory(str);
            toSearchArticle(str,FIRST);
        }
    }

    //搜索
    private void toSearchArticle(String keywords,final int pageType) {
        showLoading("正在搜索 " + keywords + " 相关文章...");
        ArticleDataManager.getInstance().loadListData((BaseActivity) context, articlesType, keywords, pageType, new com.poso2o.lechuan.manager.article.ArticleDataManager.OnLoadListDataCallback() {

            @Override
            public void onSucceed(ArrayList<Article> articles) {
                search_history.setVisibility(View.GONE);
                article_search_result.setVisibility(View.VISIBLE);
                notifyDataSetChanged(pageType, articles);
                dismissLoading();
            }

            @Override
            public void onFail(String failMsg) {
                dismissLoading();
                Toast.show(context, failMsg);
                notifyDataSetChanged(pageType, null);
            }
        });
    }

    private void initHistory() {
        String str = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_ARTICLE_SEARCH_HISTORY);
        if (str == null || str.equals(""))return;
        historyList = new ArrayList<>();
        if (str.contains("")){
            String[] history = str.split("");
            for (String st : history){
                historyList.add(st);
            }
        }else {
            historyList.add(str);
        }
        initHistoryAdapter();
    }

    private void initHistoryAdapter(){
        historyAdapter = new ArticleSearchHistoryAdapter(context,historyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        search_history_recycler.setLayoutManager(linearLayoutManager);
        search_history_recycler.setAdapter(historyAdapter);
        historyAdapter.setOnHistoryListener(this);
    }

    private void initArticleAdapter() {
        articleAdapter = new OAArticleListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        article_search_recycler.setLayoutManager(linearLayoutManager);
        article_search_recycler.setAdapter(articleAdapter);

        articleAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Article>() {
            @Override
            public void onItemClick(Article item) {
                Article article = ArticleDataManager.getInstance().findSelectData((Article) item);
                if (article == null) article = (Article) item;
                Intent intent = new Intent();
                intent.putExtra(ArticleAdActivity.ART_DATA,article);
                intent.setClass(getApplication(),ArticleInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    //保存搜索历史
    private void saveHistory(String keyword){
        if (historyList == null){
            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_ARTICLE_SEARCH_HISTORY,keyword);
        }else {
            String str = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_ARTICLE_SEARCH_HISTORY);
            for (String item : historyList){
                if (item.equals(keyword))return;
            }
            str = keyword + "" + str;
            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_ARTICLE_SEARCH_HISTORY,str);
        }
    }

    //清除历史弹窗
    private void showClearDialog(){
        ClearHistoryDialog historyDialog = new ClearHistoryDialog(context);
        historyDialog.show();
        historyDialog.setOnClearHistoryListener(new ClearHistoryDialog.OnClearHistoryListener() {
            @Override
            public void onClearHistory() {
                historyList = null;
                if (historyAdapter != null)historyAdapter.notifyAdapter(historyList);
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_ARTICLE_SEARCH_HISTORY,"");
            }
        });
    }

    //收藏文章
    private void favoriteArticle(final int position, final ArticleData articleData){
        showLoading();
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().collect(this, articleData.articles_id, 0, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                articleData.has_collect = 1;
//                context.addFavoriteArt(articleData);
                if (articleAdapter != null)articleAdapter.notifyItemChanged(position);
            }
        });
    }

    //取消收藏
    private void cancelFavorite(final int position,final ArticleData articleData){
        showLoading();
        com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager.getInstance().collect(this, articleData.articles_id, 1, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                articleData.has_collect = 0;
//                context.cancelFavoriteArt(articleData);
                if (articleAdapter != null)articleAdapter.notifyItemChanged(position);
            }
        });
    }

    class SearchArtDetailBroadcase extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null)return;
            if (bundle.get(ArticleAdActivity.AD_ARTICLE) == null)return;
            ArticleData articleData = (ArticleData) bundle.get(ArticleAdActivity.AD_ARTICLE);
            ArticleData temp = null;
            for (ArticleData art : selectArt){
                if (art.articles_id.equals(articleData.articles_id)){
                    temp = art;
                    break;
                }
            }
            if (temp == null){
                //添加
                selectArt.add(articleData);
                articleData.inLine = true;
            }else {
                //删除
                selectArt.remove(temp);
                temp.inLine = false;
            }
            articleAdapter.notifyDataSetChanged();

            Intent broad = new Intent();
            broad.setAction(SEARCH_ARTICLE_CHANGE_BROAD);
            broad.putExtra(OfficalAccountActivity.SENDING_ARTICLE_DATA,articleData);
            sendBroadcast(broad);
        }
    }

    /**
     * 刷新数据
     * @param pageType
     * @param data
     */
    public void notifyDataSetChanged(int pageType, ArrayList<Article> data) {
        if (pageType == FIRST) {
            if (data == null) {
                tips_no_article.setVisibility(VISIBLE);
                tips_no_article.setText(R.string.hint_load_article_fail);
            } else if (ListUtils.isEmpty(data)) {
                tips_no_article.setVisibility(VISIBLE);
                tips_no_article.setText(R.string.hint_load_article_null);
            } else {
                tips_no_article.setVisibility(GONE);
            }
        }
        if (articleAdapter == null)initArticleAdapter();
        articleAdapter.setArticlesType(articlesType);
        articleAdapter.notifyDataSetChanged(data);
    }
}
