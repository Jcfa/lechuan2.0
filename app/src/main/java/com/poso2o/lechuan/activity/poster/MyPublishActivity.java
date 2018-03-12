package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.topup.TopUpActivity;
import com.poso2o.lechuan.adapter.MyPublishAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterBean;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.MyPublishDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 广告我的发布页面
 */
public class MyPublishActivity extends BaseActivity {
    private Context context;
    //网络管理
    private MyPublishDataManager mMyPublishDataManager;
    //页码数据
    private PosterTotalBean mPosterTotalBean = null;
    //返回
    private ImageView my_publish_back;
    //发布数据
    private RedbagDetailsBean redbagDetailsBean;
    /**
     * 列表
     */
    private RecyclerView my_publish_recycler;
    private RecyclerView.LayoutManager my_publish_manager;
    private MyPublishAdapter myPublishAdapter;
    private SwipeRefreshLayout my_publish_refresh;
    private TextView tvBalance;

    //充值
    private LinearLayout my_publish_balance_layout;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_publish;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        my_publish_back = (ImageView) findViewById(R.id.my_publish_back);
        //列表
        my_publish_recycler = (RecyclerView) findViewById(R.id.my_publish_recycler);
        my_publish_manager = new LinearLayoutManager(context);
        my_publish_recycler.setLayoutManager(my_publish_manager);
        my_publish_refresh = (SwipeRefreshLayout) findViewById(R.id.my_publish_refresh);
        my_publish_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
        //充值
        my_publish_balance_layout = (LinearLayout) findViewById(R.id.my_publish_balance_layout);
        tvBalance = findView(R.id.my_publish_balance);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        this.context = getApplicationContext();
        mMyPublishDataManager = MyPublishDataManager.getMyPublishDataManager();
        tvBalance.setText("￥"+SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT));
        //我的发布列表
        myPublishAdapter = new MyPublishAdapter(context, new MyPublishAdapter.OnItemListener() {
            @Override
            public void onClickItem(int type, PosterDTO posterData) {
                if (type == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("news_id", posterData.news_id.toString());
                    startActivity(new Intent(context, PosterDetailsActivity.class).putExtras(bundle));
                }else{
                    isDelPosterData(posterData);
                }
            }

            @Override
            public void onClickItemRedBag(PosterDTO posterData) {
                Bundle bundle = new Bundle();
                bundle.putString("news_id", posterData.news_id.toString());
                bundle.putString("news_img", posterData.news_img.toString());
                bundle.putString("news_title", posterData.news_title.toString());
                bundle.putLong("build_time", posterData.build_time);

                bundle.putInt("has_myget_red_envelopes", posterData.has_myget_red_envelopes);// 是否已抢红包 1=已抢红包,0=未抢红包
                startActivity(new Intent(context, PosterRedBagActivity.class).putExtras(bundle));

            }

            @Override
            public void onClickItemCommission(PosterDTO posterData) {
                Bundle bundle = new Bundle();
                bundle.putString("news_id", posterData.news_id.toString());
                bundle.putString("news_img", posterData.news_img.toString());
                bundle.putString("news_title", posterData.news_title.toString());
                bundle.putLong("build_time", posterData.build_time);

                bundle.putDouble("goods_price", posterData.goods_price);
                bundle.putDouble("goods_commission_rate", posterData.goods_commission_rate);
                bundle.putDouble("goods_commission_amount", posterData.goods_commission_amount);
                startActivity(new Intent(context, PosterCommissionActivity.class).putExtras(bundle));

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_publish_recycler.setLayoutManager(linearLayoutManager);
        my_publish_recycler.setAdapter(myPublishAdapter);

        //获取广告数据
        loadPosterData(1);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        my_publish_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        // 刷新
        my_publish_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        my_publish_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (my_publish_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && myPublishAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
                    Print.println("XXX:" + mPosterTotalBean.currPage);
                    Print.println("XXX:" + mPosterTotalBean.pages);
                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
                        loadPosterData(mPosterTotalBean.currPage + 1);
                    } else {
                        Toast.show(context, "没有更多数据了");
                    }
                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });
        //充值
        my_publish_balance_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                Toast.show(context,"跳转充值页面");
                startActivity(TopUpActivity.class);
            }
        });
    }

    /**
     * 获取我的发布数据
     */
    public void loadPosterData(int currPage) {
        my_publish_refresh.setRefreshing(true);
        mMyPublishDataManager.loadMySendNewsData(activity, "" + currPage, new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                my_publish_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                my_publish_refresh.setRefreshing(false);
                mPosterTotalBean = posterQueryDTO.total;
                if (posterQueryDTO.list != null) {
                    refreshItem(posterQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterQueryDTO.list.size() + " 条商品数据" + posterQueryDTO.total.currPage);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterDTO> posterData) {
        if (mPosterTotalBean.currPage == 1) {
            myPublishAdapter.notifyData(posterData);
        } else {
            myPublishAdapter.addItems(posterData);
        }
    }

    private void isDelPosterData(final PosterDTO posterData){

        CommonHintDialog dialog = new CommonHintDialog(activity, "是否删除 " + posterData.news_title + "？", new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                delPosterData(posterData);
            }
        });
        dialog.show();

    }

    /**
     * 删除我的发布
     */
    public void delPosterData(final PosterDTO posterData) {
        showLoading();
        mMyPublishDataManager.delMySendNewsData(activity, "" + posterData.news_id, new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                dismissLoading();
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                dismissLoading();
                myPublishAdapter.delItem(posterData);
            }
        });
    }

}
