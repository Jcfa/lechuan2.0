package com.poso2o.lechuan.fragment.poster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MyFansAdapter;
import com.poso2o.lechuan.adapter.MyFollowAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFansQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OpenFansDataManager;
import com.poso2o.lechuan.manager.poster.OpenFansDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PullZoomScrollView;
import com.poso2o.lechuan.views.MyViewPager;

import java.util.ArrayList;

/**
 * 公开粉丝
 * Created by Luo on 2017-11-25.
 */

public class OpenFansFragment extends BaseFragment {
    //网络管理
    private OpenFansDataManager mOpenFansDataManager;
    //页码数据
//    private MyFlowsTotalBean mMyFlowsTotalBean = null;
    private String uid = "";
    private int currentPage = 1;
    private int totalPage = 1;
    /**
     * 列表
     */
    private RecyclerView user_home_fans_recycler;
    private RecyclerView.LayoutManager user_home_fans_manager;
    private MyFansAdapter posterAdapter;
    //    private SwipeRefreshLayout user_home_fans_refresh;
    private MyViewPager myViewPager;

    public void setViewPager(MyViewPager myViewPager) {
        this.myViewPager = myViewPager;
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_fans, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OpenFansFragment", "OpenFansFragment=uid:oncreate");
    }


    /**
     * 初始化视图
     */
    public void initView() {
        //列表
        user_home_fans_recycler = (RecyclerView) view.findViewById(R.id.user_home_fans_recycler);
        user_home_fans_manager = new LinearLayoutManager(context);
        user_home_fans_recycler.setLayoutManager(user_home_fans_manager);
//        user_home_fans_refresh = (SwipeRefreshLayout) view.findViewById(R.id.user_home_fans_refresh);
//        user_home_fans_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mOpenFansDataManager = OpenFansDataManager.getOpenFansDataManager();
        uid = getArguments().getString("uid");
        int userType = getArguments().getInt("user_type");
        myViewPager.setObjectForPosition(view, userType == Constant.MERCHANT_TYPE ? 2 : 1);
        Log.i("OpenFansFragment", "OpenFansFragment=uid:" + uid);
        //广告列表
        posterAdapter = new MyFansAdapter((BaseActivity) context, "", new MyFansAdapter.OnItemListener() {
            @Override
            public void onClickItem(MyFansDTO posterData) {
                //itemOnClickListenner(posterData);
            }

            @Override
            public void onClickReward(MyFansDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        user_home_fans_recycler.setLayoutManager(linearLayoutManager);
        user_home_fans_recycler.setAdapter(posterAdapter);

        //获取广告数据
        //loadPosterData(1);

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
//        // 刷新
//        user_home_fans_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPosterData(1);
//            }
//        });
        // 滚动监听
        user_home_fans_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 滚动到底部加载更多
//                if (user_home_fans_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
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
        if (currentPage < totalPage) {
            ++currentPage;
            loadPosterData(currentPage);
        }
    }

    public void loadPosterData(int currPage) {
        loadPosterData(currPage, null);
    }

    /**
     * 获取公开粉丝数据
     */
    public void loadPosterData(int currPage, final PullZoomScrollView scrollView) {
        WaitDialog.showLoaddingDialog(context);
        currentPage = currPage;
        OpenFansDataManager.getOpenFansDataManager().loadOpenFansData((BaseActivity) context, "" + currPage, uid, new IRequestCallBack<MyFansQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
//                user_home_fans_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, MyFansQueryDTO myFansQueryDTO) {
//                user_home_fans_refresh.setRefreshing(false);
                if (myFansQueryDTO != null && myFansQueryDTO.total != null) {
                    totalPage = myFansQueryDTO.total.pages;
                }
                if (myFansQueryDTO.list != null) {
                    refreshItem(myFansQueryDTO.list);
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
    private void refreshItem(ArrayList<MyFansDTO> posterData) {
//        if (mMyFlowsTotalBean.currPage == 1) {
        posterAdapter.notifyData(posterData);
//        } else {
//            posterAdapter.addItems(posterData);
//        }
    }

}
