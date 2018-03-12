package com.poso2o.lechuan.activity.topup;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.BrokerageDetailData;
import com.poso2o.lechuan.bean.mine.BrokerageDetailItemBean;
import com.poso2o.lechuan.bean.mine.MerchantItemBean;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * 佣金结算 支付页面
 * Created by Administrator on 2017-12-08.
 */

public class CommissionSettleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<BrokerageDetailItemBean> mList = new ArrayList<>();
    private BaseAdapter mAdapter;
    private int CURRENT_PAGE = 1;//当前页
    private int TOTAL_PAGE = 1;
    public static String KEY_BEAN_INFO = "bean_info";
    private MerchantItemBean mMerchantInfo;//商家信息
    private ImageView ivLogo;
    private TextView tvNick;
    private EditText etAmount;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_commission_settle;
    }

    @Override
    protected void initView() {
        setTitle("佣金结算");
        mSwipeRefreshLayout = findView(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        ivLogo = findView(R.id.iv_logo);
        tvNick = findView(R.id.tv_nick);
        etAmount = findView(R.id.et_amount);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMerchantInfo = (MerchantItemBean) bundle.getSerializable(KEY_BEAN_INFO);
        }
        if (mMerchantInfo != null) {
            Glide.with(activity).load(mMerchantInfo.getDistributor_logo()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
            tvNick.setText(mMerchantInfo.getDistributor_name());
            if (mMerchantInfo.getGeneral_commission() > 0) {
                etAmount.setText(mMerchantInfo.getGeneral_commission() + "");
            }
        }
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
        loadData(false);
    }

    @Override
    protected void initListener() {
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
//        //微信
//        findView(R.id.top_up_wx).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                doCommissionPayment(mMerchantInfo.getDistributor_id());
//            }
//        });
//        //支付宝
//        findView(R.id.top_up_alipay).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                doCommissionPayment(mMerchantInfo.getDistributor_id());
//            }
//        });
        //确定结算
        findView(R.id.btn_settle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCommissionPayment(mMerchantInfo.getDistributor_id());
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    /**
     * 佣金结算
     *
     * @param id 分销商ID
     */
    private void doCommissionPayment(String id) {
        WaitDialog.showLoaddingDialog(activity, "正在结算中...");
        MineDataManager.getMineDataManager().doCommissionPayment(activity, id, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
//                Toast.show(activity, "结算失败：" + msg);
                float amount = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT);
                CommonHintDialog dialog = new CommonHintDialog(activity, "结算失败：" + msg + "\n当前余额：" + amount, new InvitationDistributionDialog.DialogClickCallBack() {
                    @Override
                    public void setAffirm() {

                    }
                });
                dialog.show();
            }

            @Override
            public void onResult(int tag, Object object) {
//                Toast.show(activity, "结算成功！");
                CommonHintDialog dialog = new CommonHintDialog(activity, "结算成功！", new InvitationDistributionDialog.DialogClickCallBack() {
                    @Override
                    public void setAffirm() {

                    }
                });
            }
        });
    }

    private void loadData(boolean next) {
        if (next) {
            if (CURRENT_PAGE < TOTAL_PAGE) {
                ++CURRENT_PAGE;
                getBrokerageListForDistribution();
            }
        } else {
            CURRENT_PAGE = 1;
            mSwipeRefreshLayout.setRefreshing(true);
            getBrokerageListForDistribution();
        }
    }

    private void getBrokerageListForDistribution() {
        if (mMerchantInfo == null) {
            return;
        }
        MineDataManager.getMineDataManager().getBrokerageListForDistribution(activity, mMerchantInfo.getDistributor_id(), CURRENT_PAGE, new IRequestCallBack<BrokerageDetailData>() {
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
