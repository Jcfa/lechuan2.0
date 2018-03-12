package com.poso2o.lechuan.activity.poster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PosterAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.MyCollectionDataManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 广告我的收藏页面
 */
public class MyCollectionActivity extends BaseActivity {
    //网络管理
    private MyCollectionDataManager mMyCollectionDataManager;
    private PosterDataManager mPosterDataManager;
    //页码数据
    private PosterTotalBean mPosterTotalBean = null;

    //返回
    private ImageView my_collection_back;
    /**
     * 列表
     */
    private RecyclerView my_collection_recycler;
    private RecyclerView.LayoutManager my_collection_manager;
    private PosterAdapter myPublishAdapter;
    private SwipeRefreshLayout my_collection_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_collection;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        my_collection_back = (ImageView) findViewById(R.id.my_collection_back);
        //列表
        my_collection_recycler = (RecyclerView) findViewById(R.id.my_collection_recycler);
        my_collection_manager = new LinearLayoutManager(activity);
        my_collection_recycler.setLayoutManager(my_collection_manager);
        my_collection_refresh = (SwipeRefreshLayout) findViewById(R.id.my_collection_refresh);
        my_collection_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mMyCollectionDataManager = MyCollectionDataManager.getMyCollectionDataManager();
        mPosterDataManager = PosterDataManager.getPosterDataManager();

        //我的发布列表
        myPublishAdapter = new PosterAdapter(activity, new PosterAdapter.OnItemListener() {
            @Override
            public void onClickItem(int position,PosterDTO posterData) {
                itemOnClickListenner(posterData);
            }
            @Override
            public void onClickUser(PosterDTO posterData) {
                itemOnClickUserListenner(posterData);
            }
            @Override
            public void onClickReward(int position,PosterDTO posterData) {
                itemOnClickRewardListenner(posterData);
            }
            @Override
            public void onClickBrowse(PosterDTO posterData) {
                itemOnClickBrowseListenner(posterData);
            }
            @Override
            public void onClickLike(PosterDTO posterData, int position) {
                itemOnClickLikeListenner(posterData, position);
            }
            @Override
            public void onClickComment(int position,PosterDTO posterData) {
                itemOnClickCommentListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_collection_recycler.setLayoutManager(linearLayoutManager);
        my_collection_recycler.setAdapter(myPublishAdapter);

        //获取广告数据
        loadPosterData(1);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        my_collection_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        // 刷新
        my_collection_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        my_collection_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (my_collection_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && myPublishAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
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
     * 获取我的发布数据
     */
    public void loadPosterData(int currPage){
        my_collection_refresh.setRefreshing(true);
        mMyCollectionDataManager.loadCollectionData(activity, "" + currPage, new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                my_collection_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                my_collection_refresh.setRefreshing(false);
                mPosterTotalBean = posterQueryDTO.total;
                if (posterQueryDTO.list != null) {
                    refreshItem(posterQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterQueryDTO.list.size() + " 条商品数据" + posterQueryDTO.total.currPage);
                }else{
                    Toast.show(activity,"没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterDTO> posterData){
        if (mPosterTotalBean.currPage == 1) {
            myPublishAdapter.notifyData(posterData);
        }else{
            myPublishAdapter.addItems(posterData);
        }
    }

    /**
     * 点击广告列表监听
     */
    public void itemOnClickListenner(PosterDTO posterData){
        Bundle bundle=new Bundle();
        bundle.putString("news_id",posterData.news_id.toString());
        startActivity(new Intent(activity, PosterDetailsActivity.class).putExtras(bundle));
    }

    /**
     * 点击用户监听
     */
    public void itemOnClickUserListenner(PosterDTO posterData){
        Bundle bundle=new Bundle();
        bundle.putString("uid",posterData.uid.toString());
        bundle.putInt("user_type",posterData.user_type);
        bundle.putString("nick",posterData.nick.toString());
        bundle.putString("logo",posterData.logo.toString());
        bundle.putInt("has_flow",posterData.has_flow);
        startActivity(new Intent(activity, OpenHomeActivity.class).putExtras(bundle));
    }

    /**
     * 点击佣金、红包监听
     */
    public void itemOnClickRewardListenner(PosterDTO posterData){
        Bundle bundle=new Bundle();
        bundle.putString("news_id",posterData.news_id.toString());
        bundle.putString("news_img",posterData.news_img.toString());
        bundle.putString("news_title",posterData.news_title.toString());
        bundle.putLong("build_time",posterData.build_time);
        if (posterData.has_red_envelopes > 0){//0=没有红包,1=有红包
            bundle.putInt("has_myget_red_envelopes",posterData.has_myget_red_envelopes);// 是否已抢红包 1=已抢红包,0=未抢红包
            startActivity(new Intent(activity, PosterRedBagActivity.class).putExtras(bundle));
        }else{
            if (posterData.has_commission > 0){//0=没有佣金,1=有佣金
                bundle.putDouble("goods_price",posterData.goods_price);
                bundle.putDouble("goods_commission_rate",posterData.goods_commission_rate);
                bundle.putDouble("goods_commission_amount",posterData.goods_commission_amount);
                startActivity(new Intent(activity, PosterCommissionActivity.class).putExtras(bundle));
            }
        }
    }

    /**
     * 点击浏览监听
     */
    public void itemOnClickBrowseListenner(PosterDTO posterData){
        //Toast.show(context,"点击浏览监听");
    }

    /**
     * 点击赞监听
     */
    public void itemOnClickLikeListenner(final PosterDTO posterData, final int position){
        //Toast.show(context,"点击赞监听");
        if (posterData.has_like == 0){//1=已点赞 ,0=未点赞
            mPosterDataManager.loadLikePoster(activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity,msg + "");//点赞失败
                }
                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity,"点赞成功");
                    posterData.has_like = 1;
                    posterData.like_num ++;
                    myPublishAdapter.notifyItemChanged(position, "poster");
                }
            });
        }else{
            mPosterDataManager.loadUnLikePoster((BaseActivity) activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity,msg + "");//取消点赞失败
                }
                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity,"取消点赞成功");
                    posterData.has_like = 0;
                    posterData.like_num --;
                    myPublishAdapter.notifyItemChanged(position, "poster");
                }
            });
        }
    }

    /**
     * 点击评价监听
     */
    public void itemOnClickCommentListenner(PosterDTO posterData){
        Bundle bundle=new Bundle();
        bundle.putString("news_id","" + posterData.news_id);
        startActivity(new Intent(activity, PosterCommentActivity.class).putExtras(bundle));
    }

}
