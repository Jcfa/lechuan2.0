package com.poso2o.lechuan.activity.topup;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.alipay.AlipayManager;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.topup.TopUpAmountBean;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonEditNumberDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.manager.trading.TradingManager;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

/**
 * 余额充值
 *
 * @author Zheng Jie Dong
 * @date 2016-11-16
 */
public class TopUpActivity extends BaseActivity implements OnClickListener {

    /**
     * 金额
     */
    private RecyclerView mRecyclerView;
    private ArrayList<TopUpAmountBean> mList = new ArrayList<>();
    private int[] dataArray = {50, 100, 200, 500, 1000, 0};
    private BaseAdapter mAdapter;
    private String mSelectedAmount = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_top_up;
    }

    @Override
    protected void initView() {
        setTitle(R.string.top_up);
        registerBroadcast(Constant.BROADCAST_WEIXIN_TOP_UP);
        mRecyclerView = findView(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
    }

    @Override
    protected void onBroadcastReceive(Intent intent) {
        // 微信支付成功后回退到上一级界面
        if (intent != null && intent.getAction().equals(Constant.BROADCAST_WEIXIN_TOP_UP)) {
            Toast.show(activity, R.string.toast_top_up_succeed);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void initData() {
        TextView top_up_money = findView(R.id.top_up_money);
        top_up_money.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
        mList.clear();
        for (int i = 0; i < dataArray.length; i++) {
            TopUpAmountBean bean = new TopUpAmountBean(i, dataArray[i], false);
            mList.add(bean);
        }
        mAdapter = new BaseAdapter(activity, mList) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                TopUpAmountBean bean = (TopUpAmountBean) item;
                TextView textView = holder.getView(R.id.textView);
                textView.setText(bean.getAmountText());
                textView.setSelected(bean.isChecked());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.layout_topup_item, parent));
            }

        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                TopUpAmountBean bean = (TopUpAmountBean) item;
                mSelectedAmount = bean.getAmount() + "";
                setAllNotSelected();
                mList.get(bean.getId()).setChecked(true);
                mAdapter.notifyDataSetChanged(mList);
                if (bean.getId() == 5) {
                    showEditDialog(bean.getId());
                }
            }
        });
    }

    @Override
    protected void initListener() {
        // 支付宝
        findView(R.id.top_up_alipay).setOnClickListener(this);
        // 微信
        findView(R.id.top_up_wx).setOnClickListener(this);
    }

    private void showEditDialog(final int position) {
        CommonEditNumberDialog dialog = new CommonEditNumberDialog(activity, "输入金额", "请输入金额", mList.get(position).getAmount() + "");
        dialog.show(new CommonEditNumberDialog.OnEditListener() {
            @Override
            public void onConfirm(String name) {
                mSelectedAmount = name;
                mList.get(position).setAmount(NumberUtils.toDouble(name));
                mAdapter.notifyDataSetChanged(mList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_up_alipay:// 支付宝
                if (TextUtil.isEmpty(mSelectedAmount)) {
                    Toast.show(activity, R.string.toast_money_not_null);
                    return;
                }
                TradingManager.getInstance().aliPay(this, mSelectedAmount, new TradingManager.OnAliPayCallback() {

                    @Override
                    public void onSucceed(String response) {
                        // TODO 测试代码，需要换成R.string形式
//                        Toast.show(activity, "支付宝支付成功");
                        AlipayManager alipay = AlipayManager.getAlipayManager();
                        alipay.pay(activity, response);
                    }


                    @Override
                    public void onFail(String failMsg) {
                        Toast.show(activity, failMsg);
                    }
                });

                break;

            case R.id.top_up_wx:// 微信
                if (TextUtil.isEmpty(mSelectedAmount)) {
                    Toast.show(this, R.string.toast_money_not_null);
                    return;
                }
                WaitDialog.showLoaddingDialog(activity);
                TradingManager.getInstance().wxPay(this, mSelectedAmount, new TradingManager.OnWXPayCallback() {

                    @Override
                    public void onSucceed(PayReq req) {
                        Toast.show(activity, R.string.toast_open_wx_pay);
                        IWXAPI api = WXAPIFactory.createWXAPI(activity, AppConfig.WEIXIN_APPID);
                        // 微信支付只能通过广播回调
                        api.sendReq(req);
                    }

                    @Override
                    public void onFail(String failMsg) {
                        Toast.show(activity, failMsg);
                    }
                });
                break;
        }
    }

    /**
     * 设置全部不选中
     */
    private void setAllNotSelected() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setChecked(false);
        }
    }

    /**
     * 微信和支付宝支付成功的回调通知
     *
     * @param event
     */
    @Override
    public void onPayResult(PayEvent event) {
        if (event.code == PayEvent.SUCCESS) {//支付成功
            if (event.payType == PayEvent.WEIXIN_TYPE) {//微信

            } else if (event.payType == PayEvent.ALIPAY_TYPE) {//支付宝

            }
//            getUserInfo();
            finish();
        }
    }

//    /**
//     * 充值成功重新获取信息，更新余额等数据
//     */
//    private void getUserInfo() {
//        WaitDialog.showLoaddingDialog(activity, "更新红包余额...");
//        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
//        MineDataManager.getMineDataManager().getUserInfo(activity, uid, new IRequestCallBack<UserInfoBean>() {
//            @Override
//            public void onResult(int tag, UserInfoBean object) {
//                finish();
//            }
//
//            @Override
//            public void onFailed(int tag, String msg) {
//                finish();
//            }
//        });
//    }
}