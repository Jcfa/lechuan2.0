package com.poso2o.lechuan.activity.topup;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.mine.RedPacketRecordData;
import com.poso2o.lechuan.bean.mine.RedPacketRecordItemBean;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.manager.trading.TradingManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 提现页面
 * Created by Administrator on 2017-12-08.
 */

public class WithdrawalActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private static final int WITHDRAWAL_ID = 4;
    private ArrayList<RedPacketRecordItemBean> mList = new ArrayList<>();
    private BaseAdapter mRecordAdapter;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    protected void initView() {
        setTitle("提现");
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
                ivLogo.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                tvAmount.setText("" + bean.getAmount());
                tvAmount.setGravity(Gravity.CENTER);
                tvTime.setGravity(Gravity.CENTER);
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
        TextView top_up_money = findView(R.id.top_up_money);
        top_up_money.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
        loadData(false);
    }

    @Override
    protected void initListener() {
        // 支付宝
        findView(R.id.top_up_alipay).setOnClickListener(this);
        // 微信
        findView(R.id.top_up_wx).setOnClickListener(this);
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
        MineDataManager.getMineDataManager().getRedPacketRecordList(activity, WITHDRAWAL_ID, CURRENT_PAGE, new IRequestCallBack<RedPacketRecordData>() {
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

    @Override
    public void onClick(View view) {
        if (SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) < 1) {
            Toast.show(activity, "红包余额不足1元！");
            return;
        }
        switch (view.getId()) {
            case R.id.top_up_alipay:// 支付宝

                break;
            case R.id.top_up_wx:// 微信
                if (TextUtil.isEmpty(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_OPEN_ID))) {//未绑定微信，提示绑定
                    showBindWeixinDialog();
                    return;
                }
                WaitDialog.showLoaddingDialog(activity, "正在提现...");
                TradingManager.getInstance().weixinWithdrawal(activity, "1.0", new IRequestCallBack() {
                    @Override
                    public void onResult(int tag, Object result) {
                        EventBus.getDefault().post(new PayEvent(1,PayEvent.WEIXIN_TYPE));
                        finish();
                    }

                    @Override
                    public void onFailed(int tag, String msg) {

                    }
                });
                break;
        }
    }

    /**
     * 绑定微信提醒
     */
    private void showBindWeixinDialog() {
        CommonHintDialog dialog = new CommonHintDialog(activity, getString(R.string.dialog_weixin_bind_text), new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                UserDataManager.getUserDataManager().weixinBindAuth(activity);
            }
        });
        dialog.show();
    }

}
