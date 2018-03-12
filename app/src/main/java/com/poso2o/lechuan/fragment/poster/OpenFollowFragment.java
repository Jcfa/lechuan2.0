package com.poso2o.lechuan.fragment.poster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.poster.MyScrollViewActivity;
import com.poso2o.lechuan.adapter.MyFollowAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OpenFlowsDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PullZoomScrollView;
import com.poso2o.lechuan.views.MyViewPager;

import java.util.ArrayList;

/**
 * Created by Luo on 2017-11-25.
 */

public class OpenFollowFragment extends BaseFragment {
    //网络管理
    private OpenFlowsDataManager mOpenFlowsDataManager;
    //页码数据
//    private MyFlowsTotalBean mMyFlowsTotalBean = null;
    private int currPage = 1;
    private int totalPage = 1;
    private String uid = "";
    /**
     * 列表
     */
    private RecyclerView user_home_follow_recycler;
    private RecyclerView.LayoutManager user_home_follow_manager;
    private MyFollowAdapter posterAdapter;
    //    private SwipeRefreshLayout user_home_follow_refresh;
    private MyViewPager myViewPager;

    public void setViewPager(MyViewPager myViewPager) {
        this.myViewPager = myViewPager;
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_follow, container, false);
    }

    /**
     * 初始化视图
     */
    public void initView() {
        mOpenFlowsDataManager = OpenFlowsDataManager.getOpenFlowsDataManager();
        //列表
        user_home_follow_recycler = (RecyclerView) view.findViewById(R.id.user_home_follow_recycler);
        user_home_follow_manager = new LinearLayoutManager(context);
        user_home_follow_recycler.setLayoutManager(user_home_follow_manager);
//        user_home_follow_refresh = (SwipeRefreshLayout) view.findViewById(R.id.user_home_follow_refresh);
//        user_home_follow_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        uid = getArguments().getString("uid");
        int userType = getArguments().getInt("user_type");
        myViewPager.setObjectForPosition(view, userType == Constant.MERCHANT_TYPE ? 3 : 2);
        //广告列表
        posterAdapter = new MyFollowAdapter(context, "", new MyFollowAdapter.OnItemListener() {
            @Override
            public void onClickItem(MyFlowsDTO posterData) {
                //itemOnClickListenner(posterData);
                Bundle bundle = new Bundle();
                bundle.putString("uid", posterData.flow_uid.toString());
                bundle.putInt("user_type", posterData.flow_user_type);
                bundle.putString("nick", posterData.flow_nick);
                bundle.putString("logo", posterData.flow_logo);
                bundle.putInt("has_flow", posterData.has_flow);
                startActivity(new Intent(context, MyScrollViewActivity.class).putExtras(bundle));
            }

            @Override
            public void onClickReward(MyFlowsDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        user_home_follow_recycler.setLayoutManager(linearLayoutManager);
        user_home_follow_recycler.setAdapter(posterAdapter);

        //获取广告数据
        //loadPosterData(1);

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
//        // 刷新
//        user_home_follow_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPosterData(1);
//            }
//        });
        // 滚动监听
        user_home_follow_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                // 滚动到底部加载更多
//                if (user_home_follow_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
//                        && posterAdapter.getItemCount() >= 20 * mMyFlowsTotalBean.currPage) {
//                    Print.println("XXX:" + mMyFlowsTotalBean.currPage);
//                    Print.println("XXX:" + mMyFlowsTotalBean.pages);
//                    if (mMyFlowsTotalBean.pages >= mMyFlowsTotalBean.currPage + 1) {
//                        loadPosterData(mMyFlowsTotalBean.currPage + 1);
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
        if (currPage < totalPage) {
            ++currPage;
            loadPosterData(currPage);
        }
    }

    public void loadPosterData(int currPage) {
        loadPosterData(currPage, null);
    }

    /**
     * 获取我的关注数据
     */
    public void loadPosterData(int currPage, final PullZoomScrollView scrollView) {
        this.uid = uid;

        WaitDialog.showLoaddingDialog(context);
        OpenFlowsDataManager.getOpenFlowsDataManager().loadFlowsData((BaseActivity) context, "" + currPage, uid, new IRequestCallBack<MyFlowsQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
//                user_home_follow_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, MyFlowsQueryDTO myFlowsQueryDTO) {
//                user_home_follow_refresh.setRefreshing(false);
                if (myFlowsQueryDTO != null && myFlowsQueryDTO.total != null) {
                    totalPage = myFlowsQueryDTO.total.pages;
                }
                if (myFlowsQueryDTO.list != null) {
                    refreshItem(myFlowsQueryDTO.list);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
                if (scrollView != null) {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<MyFlowsDTO> posterData) {
        if (currPage == 1) {
            posterAdapter.notifyData(posterData);
        } else {
            posterAdapter.addItems(posterData);
        }
    }

}
