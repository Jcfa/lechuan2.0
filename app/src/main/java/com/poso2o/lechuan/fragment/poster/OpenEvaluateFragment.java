package com.poso2o.lechuan.fragment.poster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.poster.ShopCommentActivity;
import com.poso2o.lechuan.adapter.OpenEvaluaterAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.order.GoodsAppraiseDTO;
import com.poso2o.lechuan.bean.order.GoodsAppraiseQueryDTO;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.ShopCommentDataManager;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PullZoomScrollView;
import com.poso2o.lechuan.views.MyViewPager;

import java.util.ArrayList;

/**
 * Created by Luo on 2017-11-25.
 */

public class OpenEvaluateFragment extends BaseFragment {
    //网络管理
    private ShopCommentDataManager mShopCommentDataManager;
    //页码数据
//    private ShopCommentTotalBean mPosterTotalBean = null;
    private String uid = "";
    private int totalPage = 1;
    private int currentPage = 1;
    //加载图片
    private RequestManager glideRequest;
    private ArrayList<GoodsAppraiseDTO> shopCommentDTOs = new ArrayList<>();
    /**
     * 列表
     */
    private RecyclerView user_home_evaluate_recycler;
    private RecyclerView.LayoutManager user_home_evaluate_manager;
    private OpenEvaluaterAdapter posterAdapter;
    //    private SwipeRefreshLayout user_home_evaluate_refresh;
    private MyViewPager myViewPager;

    public void setViewPager(MyViewPager myViewPager) {
        this.myViewPager = myViewPager;
    }


    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_evaluate, container, false);
    }

    /**
     * 初始化视图
     */
    public void initView() {
        //列表
        user_home_evaluate_recycler = (RecyclerView) view.findViewById(R.id.user_home_evaluate_recycler);
        user_home_evaluate_manager = new LinearLayoutManager(context);
        user_home_evaluate_recycler.setLayoutManager(user_home_evaluate_manager);
//        user_home_evaluate_refresh = (SwipeRefreshLayout) view.findViewById(R.id.user_home_evaluate_refresh);
//        user_home_evaluate_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
        myViewPager.setObjectForPosition(view, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OpenEvaluateFragment", "OpenEvaluateFragment_onCreate()");
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mShopCommentDataManager = ShopCommentDataManager.getShopCommentDataManager();
        uid = getArguments().getString("uid");
        //加载图片
        glideRequest = Glide.with(context);
        //广告列表
        posterAdapter = new OpenEvaluaterAdapter(context, new OpenEvaluaterAdapter.OnItemListener() {

            @Override
            public void onClickItem(GoodsAppraiseDTO posterData) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ShopComment", posterData);
                //bundle.putString("appraise_id","" + posterData.appraise_id);
                startActivity(new Intent(context, ShopCommentActivity.class).putExtras(bundle));

            }

            @Override
            public void onClickUser(GoodsAppraiseDTO posterData) {

            }

            @Override
            public void onClickGoods(GoodsAppraiseDTO posterData) {

            }

            @Override
            public void onClickComment(GoodsAppraiseDTO posterData) {

            }

            @Override
            public void onClickShare(GoodsAppraiseDTO posterData) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        user_home_evaluate_recycler.setLayoutManager(linearLayoutManager);
        user_home_evaluate_recycler.setAdapter(posterAdapter);

        //获取广告数据
        //loadPosterData(1);

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
//        // 刷新
//        user_home_evaluate_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPosterData(1);
//            }
//        });
        // 滚动监听
        user_home_evaluate_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 滚动到底部加载更多
//                if (user_home_evaluate_refresh.isRefreshing() != true &&isSlideToBottom(recyclerView)
//                        && posterAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
//                    Print.println("XXX:" + mPosterTotalBean.currPage);
//                    Print.println("XXX:" + mPosterTotalBean.pages);
//                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
//                        loadPosterData(mPosterTotalBean.currPage + 1);
//                    } else {
//                        Toast.show(context, "没有更多数据了");
//                    }
//                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });
    }

    public void loadNext() {
        if (currentPage < totalPage) {
            ++currentPage;
            loadPosterData(currentPage);
        }
    }

    public void loadPosterData(final int currPage) {
        loadPosterData(currPage, null);
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData(final int currPage, final PullZoomScrollView scrollView) {
        WaitDialog.showLoaddingDialog(context);
        currentPage = currPage;
        mShopCommentDataManager.loadShopCommentData((BaseActivity) context, uid, "" + currPage, new IRequestCallBack<GoodsAppraiseQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
//                user_home_evaluate_refresh.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, GoodsAppraiseQueryDTO shopCommentQueryDTO) {
//                user_home_evaluate_refresh.setRefreshing(false);
//                mPosterTotalBean = shopCommentQueryDTO.total;
                if (shopCommentQueryDTO != null && shopCommentQueryDTO.total != null) {
                    totalPage = shopCommentQueryDTO.total.pages;
                }
                if (shopCommentQueryDTO.list != null) {
                    refreshItem(shopCommentQueryDTO.list);
//                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + shopCommentQueryDTO.list.size() + " 条商品数据" + shopCommentQueryDTO.total.currPage);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
                if (scrollView != null && currPage == 1) {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<GoodsAppraiseDTO> list) {
        if (currentPage == 1) {
            shopCommentDTOs.clear();
//            posterAdapter.notifyData(shopCommentDTOs);
//        } else {
//            posterAdapter.addItems(shopCommentDTOs);
        }
        shopCommentDTOs.addAll(list);
        posterAdapter.notifyData(shopCommentDTOs);
    }

}
