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

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MyFollowAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.MyFlowsDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 广告我的关注页面
 */
public class MyFollowActivity extends BaseActivity {
    private Context context;
    //网络管理
    private MyFlowsDataManager mMyFlowsDataManager;
    //页码数据
    private MyFlowsTotalBean mMyFlowsTotalBean = null;
    //返回
    private ImageView my_follow_back;
    /**
     * 列表
     */
    private RecyclerView my_follow_recycler;
    private RecyclerView.LayoutManager my_follow_manager;
    private MyFollowAdapter myFollowAdapter;
    private SwipeRefreshLayout my_follow_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_follow;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        my_follow_back = (ImageView) findViewById(R.id.my_follow_back);
        //列表
        my_follow_recycler = (RecyclerView) findViewById(R.id.my_follow_recycler);
        my_follow_manager = new LinearLayoutManager(context);
        my_follow_recycler.setLayoutManager(my_follow_manager);
        my_follow_refresh = (SwipeRefreshLayout) findViewById(R.id.my_follow_refresh);
        my_follow_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        this.context = getApplicationContext();
        mMyFlowsDataManager = MyFlowsDataManager.getMyFlowsDataManager();

        //列表
        myFollowAdapter = new MyFollowAdapter(context, "MyFollowActivity", new MyFollowAdapter.OnItemListener() {
            @Override
            public void onClickItem(MyFlowsDTO posterData) {
                itemOnClickListenner(posterData);
            }

            @Override
            public void onClickReward(MyFlowsDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_follow_recycler.setLayoutManager(linearLayoutManager);
        my_follow_recycler.setAdapter(myFollowAdapter);

        //获取我的关注数据
        loadPosterData(1);

    }

    /**
     * 进入粉丝个人公开主页
     *
     * @param posterData
     */
    private void itemOnClickListenner(MyFlowsDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", posterData.flow_uid.toString());
        bundle.putInt("user_type", posterData.flow_user_type);
        bundle.putString("nick", posterData.flow_nick);
        bundle.putString("logo", posterData.flow_logo);
        bundle.putInt("has_flow", 1);//我的关注都是已关注的
        startActivity(new Intent(context, MyScrollViewActivity.class).putExtras(bundle));
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        my_follow_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        // 刷新
        my_follow_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        my_follow_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (my_follow_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && myFollowAdapter.getItemCount() >= 20 * mMyFlowsTotalBean.currPage) {
                    Print.println("XXX:" + mMyFlowsTotalBean.currPage);
                    Print.println("XXX:" + mMyFlowsTotalBean.pages);
                    if (mMyFlowsTotalBean.pages >= mMyFlowsTotalBean.currPage + 1) {
                        loadPosterData(mMyFlowsTotalBean.currPage + 1);
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
     * 获取我的关注数据
     */
    public void loadPosterData(int currPage) {
        my_follow_refresh.setRefreshing(true);
        mMyFlowsDataManager.loadFlowsData(activity, "" + currPage, new IRequestCallBack<MyFlowsQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                my_follow_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, MyFlowsQueryDTO myFlowsQueryDTO) {
                my_follow_refresh.setRefreshing(false);
                mMyFlowsTotalBean = myFlowsQueryDTO.total;
                if (myFlowsQueryDTO.list != null) {
                    refreshItem(myFlowsQueryDTO.list);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<MyFlowsDTO> posterData) {
        if (mMyFlowsTotalBean.currPage == 1) {
            myFollowAdapter.notifyData(posterData);
        } else {
            myFollowAdapter.addItems(posterData);
        }
    }

}
