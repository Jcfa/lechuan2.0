package com.poso2o.lechuan.activity.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.RedPacketData;
import com.poso2o.lechuan.bean.mine.RedPacketRecordData;
import com.poso2o.lechuan.bean.mine.RedPacketRecordItemBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入明细，支出明细，充值明细 ，共用
 * Created by Administrator on 2017-12-04.
 */

public class RedPacketRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    public static final int RECHARGE_ID = 1, INCOME_ID = 2, EXPEND_ID = 3, WITHDRAWAL_ID = 4;//1充值，2收入，3支出，4提现
    public static final String KEY_TYPE = "type";
    private int mType = 0;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;
    private ArrayList<RedPacketRecordItemBean> mList = new ArrayList<>();
    private BaseAdapter mRecordAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_red_packet_record;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1)) {
                    loadData(true);
                }
            }
        });
        mRecordAdapter = new BaseAdapter(activity, mList) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                RedPacketRecordItemBean bean = (RedPacketRecordItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvTime = holder.getView(R.id.tv_time);
                tvTime.setText(TimeUtil.longToDateString(bean.getBuild_time(), "yyy-MM-dd HH:mm"));
                TextView tvAmount = holder.getView(R.id.tv_amount);
                if (mType == EXPEND_ID) {
                    tvAmount.setText("-" + bean.getAmount());
                    tvTitle.setText(bean.getNews_title());
                    tvTime.setTextSize(12);
                    Glide.with(activity).load(bean.getNews_img()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                } else if (mType == INCOME_ID) {
                    tvAmount.setText("+" + bean.getAmount());
                    tvTitle.setText(bean.getNews_title());
                    tvTime.setTextSize(12);
                    Glide.with(activity).load(bean.getNews_img()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                } else {
                    ivLogo.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                    tvTime.setGravity(Gravity.CENTER);
                    tvAmount.setGravity(Gravity.CENTER);
                    tvAmount.setText("" + bean.getAmount());
                }
                tvAmount.setSelected(true);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_redpactket_record_item, viewGroup));
            }
        };
        mRecyclerView.setAdapter(mRecordAdapter);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getInt(KEY_TYPE);
        }
        if (mType == RECHARGE_ID) {
            setTitle("充值明细");
        } else if (mType == INCOME_ID) {
            setTitle("收入明细");
            findView(R.id.layout_head).setVisibility(View.GONE);
        } else if (mType == EXPEND_ID) {
            setTitle("支出明细");
            findView(R.id.layout_head).setVisibility(View.GONE);
        }
        getRedPacketRecordList();
    }

    @Override
    protected void initListener() {

    }

    private void loadData(boolean next) {
        if (next) {
            if (CURRENT_PAGE < TOTAL_PAGE) {
                ++CURRENT_PAGE;
                getRedPacketRecordList();
            }
        } else {
            CURRENT_PAGE = 1;
            getRedPacketRecordList();
        }
    }

    private void getRedPacketRecordList() {
        MineDataManager.getMineDataManager().getRedPacketRecordList(activity, mType, CURRENT_PAGE, new IRequestCallBack<RedPacketRecordData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, RedPacketRecordData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (CURRENT_PAGE == 1) {
                    mList.clear();
                }
                if (object != null && object.list != null) {
                    mList.addAll(object.list);
                }
                TOTAL_PAGE = object.total.getPages();
                mRecordAdapter.notifyDataSetChanged(mList);
            }
        });
    }

    @Override
    public void onRefresh() {
        CURRENT_PAGE = 1;
        getRedPacketRecordList();
    }


}
