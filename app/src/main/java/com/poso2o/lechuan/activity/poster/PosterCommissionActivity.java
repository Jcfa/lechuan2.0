package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PosterCommissionAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterCommissionDTO;
import com.poso2o.lechuan.bean.poster.PosterCommissionQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterCommissionTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.bean.poster.PosterBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterCommissionDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.glide.GlideRoundTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 佣金详情页面
 */
public class PosterCommissionActivity extends BaseActivity {
    private Context context;
    //网络管理
    private PosterCommissionDataManager mPosterCommissionDataManager;
    private RequestManager glideRequest;
    //页码数据
    private PosterCommissionTotalBean mPosterTotalBean = null;
    //返回
    private ImageView commission_details_back;
    //分享赚佣金
    private LinearLayout commission_details_share_layout;
    //文章ID
    private String news_id;
    private ImageView commission_details_goods_img;
    private TextView commission_details_goods_name,commission_details_goods_price,commission_details_goods_rate,commission_details_goods_amount;
    private TextView total_settle_commission_amount,total_commission_amount;
    /**
     * 列表
     */
    private RecyclerView commission_details_recycler;
    private RecyclerView.LayoutManager commission_details_manager;
    private PosterCommissionAdapter commission_detailsAdapter;
    private SwipeRefreshLayout commission_details_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_commission_details;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        commission_details_back = (ImageView) findViewById(R.id.commission_details_back);
        //分享赚佣金
        commission_details_share_layout = (LinearLayout) findViewById(R.id.commission_details_share_layout);
        //列表
        commission_details_recycler = (RecyclerView) findViewById(R.id.commission_details_recycler);
        commission_details_manager = new LinearLayoutManager(context);
        commission_details_recycler.setLayoutManager(commission_details_manager);
        commission_details_refresh = (SwipeRefreshLayout) findViewById(R.id.commission_details_refresh);
        commission_details_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        commission_details_goods_img = (ImageView) findViewById(R.id.commission_details_goods_img);
        commission_details_goods_name = (TextView) findViewById(R.id.commission_details_goods_name);
        commission_details_goods_price = (TextView) findViewById(R.id.commission_details_goods_price);
        commission_details_goods_rate = (TextView) findViewById(R.id.commission_details_goods_rate);
        commission_details_goods_amount = (TextView) findViewById(R.id.commission_details_goods_amount);

        total_settle_commission_amount = (TextView) findViewById(R.id.total_settle_commission_amount);
        total_commission_amount = (TextView) findViewById(R.id.total_commission_amount);
    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        this.context = getApplicationContext();
        mPosterCommissionDataManager = PosterCommissionDataManager.getPosterCommissionDataManager();
        glideRequest = Glide.with(context);

        // 上一级页面传来的数据
        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");
        String news_img = intent.getStringExtra("news_img");
        String news_title = intent.getStringExtra("news_title");
        Double goods_price = intent.getDoubleExtra("goods_price",0.00);
        Double goods_commission_rate = intent.getDoubleExtra("goods_commission_rate",0.00);
        Double goods_commission_amount = intent.getDoubleExtra("goods_commission_amount",0.00);

        commission_details_goods_img = (ImageView) findViewById(R.id.commission_details_goods_img);
        commission_details_goods_name = (TextView) findViewById(R.id.commission_details_goods_name);
        commission_details_goods_price = (TextView) findViewById(R.id.commission_details_goods_price);
        commission_details_goods_rate = (TextView) findViewById(R.id.commission_details_goods_rate);
        commission_details_goods_amount = (TextView) findViewById(R.id.commission_details_goods_amount);

        if (news_img.isEmpty()){
            commission_details_goods_img.setImageResource(R.mipmap.logo_c);
        }else{
            glideRequest.load("" + news_img).transform(new GlideRoundTransform(context, 10)).into(commission_details_goods_img);
            //glideRequest.load("" + news_img).transform(new GlideCircleTransform(context)).into(commission_details_goods_img);
        }
        commission_details_goods_name.setText("" + news_title);
        commission_details_goods_price.setText("" + goods_price);
        commission_details_goods_rate.setText("" + goods_commission_rate);
        commission_details_goods_amount.setText("" + goods_commission_amount);

        //广告列表
        commission_detailsAdapter = new PosterCommissionAdapter(context, new PosterCommissionAdapter.OnItemListener() {
            @Override
            public void onClickItem(PosterCommissionDTO posterData) {
                //itemOnClickListenner(posterData);
            }@Override
            public void onClickReward(PosterCommissionDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        commission_details_recycler.setLayoutManager(linearLayoutManager);
        commission_details_recycler.setAdapter(commission_detailsAdapter);

        //获取广告数据
        loadPosterData(1);

    }
    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        commission_details_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //分享赚佣金
        commission_details_share_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("news_id",news_id.toString());
                startActivity(new Intent(context, PosterDetailsActivity.class).putExtras(bundle));
            }
        });
        // 刷新
        commission_details_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        commission_details_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (commission_details_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && commission_detailsAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
                    Print.println("XXX:" + mPosterTotalBean.currPage);
                    Print.println("XXX:" + mPosterTotalBean.pages);
                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
                        loadPosterData(mPosterTotalBean.currPage + 1);
                    } else {
                        Toast.show(activity, "没有更多数据了");
                    }
                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData(int currPage){
        commission_details_refresh.setRefreshing(true);
        mPosterCommissionDataManager.loadPosterCommissionList(activity, news_id, "" + currPage, new IRequestCallBack<PosterCommissionQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                commission_details_refresh.setRefreshing(false);
            }
            @Override
            public void onResult(int tag, PosterCommissionQueryDTO posterCommissionQueryDTO) {
                commission_details_refresh.setRefreshing(false);
                mPosterTotalBean = posterCommissionQueryDTO.total;
                if (posterCommissionQueryDTO.list != null) {
                    refreshItem(posterCommissionQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterCommissionQueryDTO.list.size() + " 条商品数据" + posterCommissionQueryDTO.total.currPage);

                    if (mPosterTotalBean.currPage == 1){
                        total_settle_commission_amount.setText("" + posterCommissionQueryDTO.total.total_settle_commission_amount);
                        total_commission_amount.setText("" + posterCommissionQueryDTO.total.total_commission_amount);
                    }

                }else{
                    Toast.show(activity,"没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterCommissionDTO> posterData){
        commission_details_refresh.setRefreshing(false);
        commission_detailsAdapter.notifyData(posterData);
    }

}
