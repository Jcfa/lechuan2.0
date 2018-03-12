package com.poso2o.lechuan.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.topup.CommissionSettleActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.mine.BrokerageDetailData;
import com.poso2o.lechuan.bean.mine.BrokerageDetailItemBean;
import com.poso2o.lechuan.bean.mine.MerchantItemBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * 佣金详情
 * Created by Administrator on 2017-11-30.
 */

public class CommissionDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<BrokerageDetailItemBean> mList = new ArrayList<>();
    private MineDataManager mMineDataManager;
    private int CURRENT_PAGE = 1;//当前页
    private int TOTAL_PAGE = 1;
    public static String KEY_ID = "id";
    public static String KEY_BEAN_INFO = "bean_info";
    public static int mType = 0;
    public static String KEY_TYPE = "type";
    public static int VALUE_COMMISSION = 1;//来自未结佣金列表
    public static int VALUE_DISTRIBUTION = 2;//来自分销商列表
    private ImageView ivLogo;
    private TextView tvNick, tvAmount, tvYjAmount, tvWjAmount;
    private TextView tvSettle;//解除或结算
    private BaseAdapter mAdapter;
    private String mId = "";//如果商家是分销商ID，如果是分销商就是商家ID
    private MerchantItemBean mMerchantInfo;//商家信息

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
            mType = bundle.getInt(KEY_TYPE);
            mMerchantInfo = (MerchantItemBean) bundle.getSerializable(KEY_BEAN_INFO);
        }
        if (mMerchantInfo != null) {
            if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_TYPE) == Constant.MERCHANT_TYPE) {
                tvNick.setText(mMerchantInfo.getDistributor_name());
                Glide.with(activity).load(mMerchantInfo.getDistributor_logo()).thumbnail(0.1f).error(R.mipmap.ic_launcher).into(ivLogo);
            } else {
                tvNick.setText(mMerchantInfo.getShop_name());
                Glide.with(activity).load(mMerchantInfo.getShop_logo()).thumbnail(0.1f).error(R.mipmap.ic_launcher).into(ivLogo);
            }
            tvAmount.setText("总佣 " + NumberUtils.format2(mMerchantInfo.getGeneral_commission()));
            tvYjAmount.setText("已结 " + NumberUtils.format2(mMerchantInfo.getSettle_amount()));
            tvWjAmount.setText("未结 " + NumberUtils.format2(mMerchantInfo.getNot_settle_amount()));
            if (mMerchantInfo.getNot_settle_amount() > 0) {
                tvWjAmount.setSelected(true);
            }
        }
        TextView tv1 = findView(R.id.tv_commission_amount);
        TextView tv2 = findView(R.id.tv_commission_amount2);

        TextView tv = findView(R.id.tv_settle);
        tv.setVisibility(View.VISIBLE);
//        if (mType == VALUE_COMMISSION) {
        if (mMerchantInfo.getNot_settle_amount() > 0) {
            tv.setText("结算");
            tv.setTextColor(getResources().getColor(R.color.colorWhite));
            tv.setBackgroundResource(R.drawable.selector_green_radius5_bg);
//        } else if (mType == VALUE_DISTRIBUTION) {
        } else {
            tv.setText("解除");
            tv.setTextColor(getResources().getColor(R.color.colorOrange));
            tv.setBackgroundResource(R.drawable.selector_gray_radius5_bg);
        }
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            //是商家
            tv1.setText("佣金支出");
            tv2.setText("佣金支出");
            getBrokerageListForDistribution();
        } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
            //分销商
            tv1.setText("佣金收入");
            tv2.setText("佣金收入");
            getBrokerageListForMerchant();
        }
    }

    @Override
    protected void initListener() {
        /**
         * 结算
         */
        tvSettle = findView(R.id.tv_settle);
        tvSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mType == VALUE_COMMISSION) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CommissionSettleActivity.KEY_BEAN_INFO, mMerchantInfo);
                    startActivity(new Intent(activity, CommissionSettleActivity.class).putExtras(bundle));
                } else if (mType == VALUE_DISTRIBUTION) {
                    if (mMerchantInfo == null) {
                        Toast.show(activity, "数据丢失！");
                        return;
                    }
                    showRelieveDialog(mMerchantInfo);
                }
            }
        });
    }

    //
//    /**
//     * 解除确认提示
//     */
//    private void showRelieveDialog(final String id) {
//        CommonDialog dialog = new CommonDialog(activity, R.string.dialog_relieve_distribution);
//        dialog.show(new CommonDialog.OnConfirmListener() {
//            @Override
//            public void onConfirm() {
//                doRelieveDistribution(id);
//            }
//        });
//    }
    private void showRelieveDialog(final MerchantItemBean bean) {
        CommonHintDialog dialog = new CommonHintDialog(activity, "是否确认解除与" + bean.getDistributor_name() + "的分销关系？", new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                doRelieveDistribution(bean.getDistributor_id());
            }
        });
        dialog.show();
    }

    /**
     * 解除分销商
     *
     * @param id
     */
    private void doRelieveDistribution(String id) {
        String shopId = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        MineDataManager.getMineDataManager().doRelieveDistribution(activity, shopId, id, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, "解除失败：" + msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                Toast.show(activity, "解除成功！");
                tvSettle.setText("已解除");
                tvSettle.setEnabled(false);
            }
        });
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
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
                    getBrokerageListForMerchant();
                    return;
                } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
                    getBrokerageListForDistribution();
                    return;
                }
            }
        } else {
            CURRENT_PAGE = 1;
            if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
                getBrokerageListForMerchant();
                return;
            } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
                getBrokerageListForDistribution();
                return;
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getBrokerageListForDistribution() {
        mMineDataManager.getBrokerageListForDistribution(activity, mId, CURRENT_PAGE, new IRequestCallBack<BrokerageDetailData>() {
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
                    setTotalView(object.total);
                }
                mList.addAll(object.getList());
                mAdapter.notifyDataSetChanged(mList);
            }
        });
    }

    private void getBrokerageListForMerchant() {
        mMineDataManager.getBrokerageListForMerchant(activity, mId, CURRENT_PAGE, new IRequestCallBack<BrokerageDetailData>() {
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
                    setTotalView(object.total);
                }
                mList.addAll(object.getList());
                mAdapter.notifyDataSetChanged(mList);
            }
        });
    }

    /**
     * 底部合计
     */
    private void setTotalView(TotalBean totalBean) {
        if (totalBean == null) {
            return;
        }
        TextView tvAmount = findView(R.id.tv_total_amount);
        tvAmount.setText(NumberUtils.format2(totalBean.getTotal_order_amount()));//成交额
        TextView tvExpend = findView(R.id.tv_total_expend);
        tvExpend.setText(NumberUtils.format2(totalBean.getTotal_commission_amount()));//总佣金
    }
}
