package com.poso2o.lechuan.fragment.oa;

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
import com.poso2o.lechuan.adapter.OAPublishedAdapter;
import com.poso2o.lechuan.adapter.OAPublishedAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 公众号已发布文章列表
 *
 * Created by Luo on 2018/2/5.
 */
public class OAPublishedFragment extends BaseFragment {

    //网络管理
    private PosterDataManager mPosterDataManager;
    //页码数据
    private PosterTotalBean mPosterTotalBean = null;

    /**
     * 列表
     */
    private RecyclerView published_recycler;
    private RecyclerView.LayoutManager published_manager;
    private OAPublishedAdapter publishedAdapter;
    private SwipeRefreshLayout published_refresh;


    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oa_published, container, false);
    }

    @Override
    public void initView() {
        //列表
        published_recycler = (RecyclerView) view.findViewById(R.id.oa_published_recycler);
        published_manager = new LinearLayoutManager(context);
        published_recycler.setLayoutManager(published_manager);
        published_refresh = (SwipeRefreshLayout) view.findViewById(R.id.oa_published_refresh);
        published_refresh.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        mPosterDataManager = PosterDataManager.getPosterDataManager();
        //广告列表
        publishedAdapter = new OAPublishedAdapter(context, new OAPublishedAdapter.OnItemListener() {
            @Override
            public void onClickItem(int position, PosterDTO posterData) {
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        published_recycler.setLayoutManager(linearLayoutManager);
        published_recycler.setAdapter(publishedAdapter);

        //获取广告数据
        loadPosterData(1);
    }

    @Override
    public void initListener() {
        // 刷新
        published_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        published_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (published_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && publishedAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
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
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData(int currPage) {
        Print.println("loadPosterData_" + currPage);
        published_refresh.setRefreshing(true);
        mPosterDataManager.loadPosterData((BaseActivity) context, currPage + "", "", new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                published_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                published_refresh.setRefreshing(false);
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
            publishedAdapter.notifyData(posterData);
        } else {
            publishedAdapter.addItems(posterData);
        }
    }

}
