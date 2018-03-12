package com.poso2o.lechuan.activity.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.AcceptDetailData;
import com.poso2o.lechuan.bean.mine.AcceptDetailItemBean;
import com.poso2o.lechuan.bean.mine.BrokerageDetailData;
import com.poso2o.lechuan.bean.mine.BrokerageDetailItemBean;
import com.poso2o.lechuan.bean.mine.PosterItemBean;
import com.poso2o.lechuan.bean.mine.RedPacketItemBean;
import com.poso2o.lechuan.bean.mine.RedpacketTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * 领取详情
 * Created by Administrator on 2017-11-30.
 */

public class AcceptDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<AcceptDetailItemBean> mList = new ArrayList<>();
    private MineDataManager mMineDataManager;
    private int CURRENT_PAGE = 1;//当前页
    private int TOTAL_PAGE = 1;
    public static String KEY_ID = "id";
    public static String KEY_BEAN_INFO = "bean_info";
    private ImageView ivLogo;
    private TextView tvNick, tvTime, tvAmount, tvSurplus, tvTake;
    private BaseAdapter mAdapter;
    private String mId = "";//如果商家是分销商ID，如果是分销商就是商家ID
    private RedPacketItemBean mRedPacketItemBean;//

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_accept_detail;
    }

    @Override
    protected void initView() {
        setTitle("领取详情");
        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
        ivLogo = findView(R.id.iv_logo);
        tvNick = findView(R.id.tv_nick);
        tvTime = findView(R.id.tv_time);
        tvAmount = findView(R.id.tv_amount);
        tvSurplus = findView(R.id.tv_surplus);
        tvTake = findView(R.id.tv_take);
        mAdapter = new BaseAdapter(activity, mList) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_accept_detail_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                AcceptDetailItemBean bean = (AcceptDetailItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                Glide.with(activity).load(bean.getLogo()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                TextView tvNick = holder.getView(R.id.tv_nick);
                tvNick.setText(bean.getNick());
                TextView tvTime = holder.getView(R.id.tv_time);
                tvTime.setText(TimeUtil.longToDateString(bean.getBuild_time(), "yyy-MM-dd"));
                TextView tvAmount = holder.getView(R.id.tv_amount);
                tvAmount.setText(bean.getAmount() + "");

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mMineDataManager = MineDataManager.getMineDataManager();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mId = bundle.getString(KEY_ID);
            mRedPacketItemBean = (RedPacketItemBean) bundle.getSerializable(KEY_BEAN_INFO);
        }
        if (mRedPacketItemBean != null) {
            Glide.with(activity).load(mRedPacketItemBean.getNews_img()).error(R.mipmap.ic_launcher).into(ivLogo);
            tvNick.setText(mRedPacketItemBean.getNews_title());
            tvTime.setText(TimeUtil.longToDateString(mRedPacketItemBean.getBuild_time(), "yyyy-MM-dd HH:mm"));
//            tvAmount.setText("总佣 " + mPosterItemBean.getCommission_amount());
//            tvYjAmount.setText("已结 " + mPosterItemBean.getCommission_amount_paid());
//            tvWjAmount.setText("未结 " + mPosterItemBean.getCommission_amount_unpaid());
//            if (mPosterItemBean.getCommission_amount_unpaid() > 0) {
//                tvWjAmount.setSelected(true);
//            }
        }
        getAcceptListForEssay();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onRefresh() {
        CURRENT_PAGE = 1;
        loadData(false);
    }

    private void loadData(boolean next) {
        if (next) {
            if (CURRENT_PAGE < TOTAL_PAGE) {
                ++CURRENT_PAGE;
                getAcceptListForEssay();
            }
        } else {
            CURRENT_PAGE = 1;
            getAcceptListForEssay();
        }
    }


    private void getAcceptListForEssay() {
        mMineDataManager.getRedPacketListForEssay(activity, mId, CURRENT_PAGE, new IRequestCallBack<AcceptDetailData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, AcceptDetailData object) {
                mSwipeRefreshLayout.setRefreshing(false);
//                TOTAL_PAGE = object.total.getPages();
                if (CURRENT_PAGE == 1) {
                    mList.clear();
                }
                mList.addAll(object.getList());
                mAdapter.notifyDataSetChanged(mList);
                setHeadInfo(object.getTotal());
            }
        });
    }

    /**
     * 设置头部信息
     */
    private void setHeadInfo(RedpacketTotalBean bean) {
        if (bean == null) {
            return;
        }
        tvAmount.setText(bean.getRed_envelopes_amount() + "");
        tvSurplus.setText(bean.getRed_envelopes_surplus_number() + "");
        tvTake.setText((bean.getRed_envelopes_number() - bean.getRed_envelopes_surplus_number()) + "");

    }
}
