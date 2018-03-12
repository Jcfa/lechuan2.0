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
import com.poso2o.lechuan.bean.mine.BrokerageDetailData;
import com.poso2o.lechuan.bean.mine.BrokerageDetailItemBean;
import com.poso2o.lechuan.bean.mine.PosterItemBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.TimeUtil;

import java.util.ArrayList;

/**
 * 佣金详情
 * Created by Administrator on 2017-11-30.
 */

public class PosterDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<BrokerageDetailItemBean> mList = new ArrayList<>();
    private MineDataManager mMineDataManager;
    private int CURRENT_PAGE = 1;//当前页
    private int TOTAL_PAGE = 1;
    public static String KEY_ID = "id";
    public static String KEY_BEAN_INFO = "bean_info";
    private ImageView ivLogo;
    private TextView tvNick, tvAmount, tvYjAmount, tvWjAmount;
    private BaseAdapter mAdapter;
    private String mId = "";//如果商家是分销商ID，如果是分销商就是商家ID
    private PosterItemBean mPosterItemBean;//商家信息

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_commission_detail;
    }

    @Override
    protected void initView() {
        setTitle("佣金详情");
        ivLogo = findView(R.id.iv_logo);
        tvNick = findView(R.id.tv_usernick);
        tvAmount = findView(R.id.tv_amount);
        tvYjAmount = findView(R.id.tv_yj_amount);
        tvWjAmount = findView(R.id.tv_wj_amount);
        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
        mAdapter = new BaseAdapter(activity, mList) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_commission_detail_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                BrokerageDetailItemBean bean = (BrokerageDetailItemBean) item;
                TextView tvDate = holder.getView(R.id.tv_date);
                tvDate.setText(TimeUtil.longToDateString(bean.getPayment_time(), "yyyy-MM-dd"));
                TextView tvMember = holder.getView(R.id.tv_member);
                tvMember.setText(bean.getMember_name());
                TextView tvAmount = holder.getView(R.id.tv_amount);
                tvAmount.setText(bean.getOrder_amount() + "");
                TextView tvCommissionAmount = holder.getView(R.id.tv_commission_amount);
                tvCommissionAmount.setText(bean.getCommission_amount() + "");
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mMineDataManager = MineDataManager.getMineDataManager();
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mId = bundle.getString(KEY_ID);
            mPosterItemBean = (PosterItemBean) bundle.getSerializable(KEY_BEAN_INFO);
        }
        if (mPosterItemBean != null) {
            Glide.with(activity).load(mPosterItemBean.getNews_img()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
            tvNick.setText(mPosterItemBean.getNews_title());
//            tvAmount.setText("总佣 " + mPosterItemBean.getCommission_amount());
//            tvYjAmount.setText("已结 " + mPosterItemBean.getCommission_amount_paid());
//            tvWjAmount.setText("未结 " + mPosterItemBean.getCommission_amount_unpaid());
//            if (mPosterItemBean.getCommission_amount_unpaid() > 0) {
//                tvWjAmount.setSelected(true);
//            }
        }
//        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
//            //是商家
//        } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
//            //分销商
//        }
        getBrokerageListForEssay();
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
                getBrokerageListForEssay();
            }
        } else {
            CURRENT_PAGE = 1;
            getBrokerageListForEssay();
        }
    }

//    private void getBrokerageListForDistribution() {
//        mMineDataManager.getBrokerageListForDistribution(activity, mId, CURRENT_PAGE, new IRequestCallBack<BrokerageDetailData>() {
//            @Override
//            public void onFailed(int tag, String msg) {
//                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onResult(int tag, BrokerageDetailData object) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                TOTAL_PAGE = object.total.getPages();
//                if (CURRENT_PAGE == 1) {
//                    mList.clear();
//                }
//                mList.addAll(object.getList()));
//                mAdapter.notifyDataSetChanged(mList);
//            }
//        });
//    }

    private void getBrokerageListForEssay() {
        mMineDataManager.getBrokerageListForEssay(activity, mId, CURRENT_PAGE, new IRequestCallBack<BrokerageDetailData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, BrokerageDetailData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                TOTAL_PAGE = object.total.getPages();
                if (CURRENT_PAGE == 1) {
                    mList.clear();
                }
                mList.addAll(object.getList());
                mAdapter.notifyDataSetChanged(mList);
            }
        });
    }
}
