package com.poso2o.lechuan.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.activity.realshop.ArticleSearchActivity;
import com.poso2o.lechuan.activity.realshop.OfficalAccountActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.adapter.ArtHistoryAdapter;
import com.poso2o.lechuan.adapter.PublishingArtAdapter;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.articledata.AllArticleHistory;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.bean.articledata.ArticleHistory;
import com.poso2o.lechuan.bean.shopdata.BangDingData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.ArticleDataManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by Jaydon on 2017/10/20.
 */
public class OfficialAccountsIssueView extends BaseView implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    public static final int CODE_TO_WC_AUTHORITY = 1001;
    public static final int CODE_TO_ART_AD = 1002;

    private View view;
    private OfficalAccountActivity context;

    //返回
    private ImageView oa_publish_back;
    //发布
    private TextView oa_publish;
    //绑定公众号
    private LinearLayout binging_wechat;
    //店铺图片
    private ImageView oa_shop_pic;
    //店铺名称
    private TextView oa_shop_name;
    //要发布的文章的列表
    private RecyclerView going_publish_recycler;
    //发布历史
    private LinearLayout publish_history;
    //历史发布数量
    private TextView history_publish_num;
    //历史发布刷新
    private SwipeRefreshLayout history_publish_swipe;
    //历史发布列表
    private RecyclerView history_publish_recycler;

    //待发布文章适配器
    private PublishingArtAdapter publishingArtAdapter ;
    //历史发布适配器
    private ArtHistoryAdapter artHistoryAdapter;

    //历史发布总页数
    private int mPages = 1;
    //历史发布当前页数
    private int mPage = 1;
    //绑定公众号信息
    private BangDingData bangDingData;

    public OfficialAccountsIssueView(Context context) {
        super(context);
        this.context = (OfficalAccountActivity) context;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_oa_publish,null);
        return view;
    }

    @Override
    public void initView() {
        oa_publish_back = (ImageView) view.findViewById(R.id.oa_publish_back);
        oa_publish = (TextView) view.findViewById(R.id.oa_publish);

        binging_wechat = (LinearLayout) view.findViewById(R.id.binging_wechat);
        oa_shop_pic = (ImageView) view.findViewById(R.id.oa_shop_pic);
        oa_shop_name = (TextView) view.findViewById(R.id.oa_shop_name);

        going_publish_recycler = (RecyclerView) view.findViewById(R.id.going_publish_recycler);

        publish_history = (LinearLayout) view.findViewById(R.id.publish_history);
        history_publish_num = (TextView) view.findViewById(R.id.history_publish_num);
        history_publish_swipe = (SwipeRefreshLayout) view.findViewById(R.id.history_publish_swipe);
        history_publish_recycler = (RecyclerView) view.findViewById(R.id.history_publish_recycler);

        history_publish_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        initMyListener();

        authorizeState();

        notifyArtList();
    }

    private void initMyListener(){
        oa_publish_back.setOnClickListener(this);
        oa_publish.setOnClickListener(this);
        binging_wechat.setOnClickListener(this);
        history_publish_swipe.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oa_publish_back:
                context.finish();
                break;
            case R.id.oa_publish:
                sendArticles();
                break;
            case R.id.binging_wechat:
                Intent intent = new Intent();
                intent.setClass(context, WCAuthorityActivity.class);
                intent.putExtra(WCAuthorityActivity.BIND_TYPE,1);
                context.startActivityForResult(intent,CODE_TO_WC_AUTHORITY);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadHistoryArticle();
    }

    //判断展示的是要发布的文章还是历史发布文章
    private void initArtData(){
        if (context.getSelectedArt().size() == 0){
            publish_history.setVisibility(View.VISIBLE);
            going_publish_recycler.setVisibility(View.GONE);
            if (publishingArtAdapter != null)publishingArtAdapter.notifyData(null);
            loadHistoryArticle();
        }else {
            publish_history.setVisibility(View.GONE);
            going_publish_recycler.setVisibility(View.VISIBLE);
            if (publishingArtAdapter == null)initPublishingAdapter();
            publishingArtAdapter.notifyData(context.getSelectedArt());
        }
    }

    private void initPublishingAdapter(){
        publishingArtAdapter = new PublishingArtAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        going_publish_recycler.setLayoutManager(linearLayoutManager);
        going_publish_recycler.setAdapter(publishingArtAdapter);

        publishingArtAdapter.setOnPublishingListener(new PublishingArtAdapter.OnPublishingListener() {
            @Override
            public void onPublishingDel(ArticleData articleData) {
                context.removeSelectArt(articleData);
                notifyArtList();
//                publishingArtAdapter.notifyData(context.getSelectedArt());
            }

            @Override
            public void onPublishing(ArticleData articleData) {
                Intent intent = new Intent();
                intent.setClass(context,ArticleAdActivity.class);
                intent.putExtra(ArticleAdActivity.ART_DATA,articleData);
                intent.putExtra(ArticleSearchActivity.FROM_ART_SEARCH,false);
//                intent.putExtra(ArticleAdActivity.SENDING_ART_NUM,context.getSelectedArt().size());
                context.startActivityForResult(intent,CODE_TO_ART_AD);
            }
        });
    }

    private void initHistoryAdapter(){
        artHistoryAdapter = new ArtHistoryAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        history_publish_recycler.setLayoutManager(linearLayoutManager);
        history_publish_recycler.setAdapter(artHistoryAdapter);

        artHistoryAdapter.setOnDelAhAllListener(new ArtHistoryAdapter.OnDelAhAllListener() {
            @Override
            public void onDelAh(ArticleHistory articleHistory) {

                /*context.showReadDialog();
                ArticleManager.getArticleManager().articleDelete(context, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), articleHistory.news_id + "", new ArticleManager.OnArtDelListener() {
                    @Override
                    public void onArtDelSuccess(BaseActivity baseActivity) {
                        context.dismissReadDialog();
                        loadHistoryArticle();
                    }

                    @Override
                    public void onArtDelFail(BaseActivity baseActivity, String failStr) {
                        context.dismissReadDialog();
                        new ToastView(context,failStr).show();
                    }
                });*/
            }
        });
    }

    //刷新文章列表
    public void notifyArtList(){
        initArtData();
    }

    //发布文章
    private void sendArticles(){
        if (bangDingData == null || bangDingData.authorized.equals("0")){
            Toast.show(context,"请先绑定公众号");
            return;
        }
        if (context.getSelectedArt().size() < 1){
            Toast.show(context,"请选择发布文章");
            return;
        }
        context.showLoading("正在提交数据...");
        ArticleDataManager.getInstance().sendArticle(context, context.getSelectedArt(), new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                context.dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                context.dismissLoading();
                context.getSelectedArt().clear();
                context.showOfficialAccountsIssueView();
            }
        });
    }

    //加载历史发布
    private void loadHistoryArticle(){
        history_publish_swipe.setRefreshing(true);
        ArticleDataManager.getInstance().sendArtHistory(context, mPage + "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                history_publish_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                history_publish_swipe.setRefreshing(false);
                AllArticleHistory allArticleHistory = new Gson().fromJson((String)object,AllArticleHistory.class);
                if (allArticleHistory == null)return;
                if (artHistoryAdapter == null)initHistoryAdapter();
                if (allArticleHistory.total != null){
                    mPages = allArticleHistory.total.pages;
                    history_publish_num.setText(allArticleHistory.total.rowcount + "");
                }
                artHistoryAdapter.notifyDatas(allArticleHistory.list);
            }
        });
    }

    public void notifyAdapterData(){
        if (context.getSelectedArt().size() == 0){
            if (publishingArtAdapter != null)publishingArtAdapter.notifyData(null);
        }else {
            if (publishingArtAdapter != null)publishingArtAdapter.notifyData(context.getSelectedArt());
        }
    }

    //获取绑定公众号的状态
    public void authorizeState(){
        WShopManager.getrShopManager().authorizeState(context, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                bangDingData = (BangDingData) result;
                if (bangDingData != null && bangDingData.authorized.equals("1")){
                    Glide.with(context).load(bangDingData.head_img).placeholder(R.mipmap.background_image).into(oa_shop_pic);
                    oa_shop_name.setText(bangDingData.nick_name);
                }else{
                    oa_shop_pic.setImageResource(R.mipmap.background_image);
                    oa_shop_name.setText("请绑定公众号");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context,msg);
            }
        });
    }
}
