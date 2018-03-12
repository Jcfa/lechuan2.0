package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MyFansAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFansQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommissionSettingDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.InvitingDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.GoodsDataManager;
import com.poso2o.lechuan.manager.poster.MyFansDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 广告我的关注页面
 */
public class MyFansActivity extends BaseActivity {
    private Context context;
    //网络管理
    private MyFansDataManager mMyFansDataManager;
    //页码数据
    private MyFlowsTotalBean mMyFlowsTotalBean = null;
    //返回
    private ImageView my_fans_back;
    //邀请确认窗口
//    private InvitingDialog mInvitingDialog;
    /**
     * 列表
     */
    private RecyclerView my_fans_recycler;
    private RecyclerView.LayoutManager my_fans_manager;
    private MyFansAdapter myFollowAdapter;
    private SwipeRefreshLayout my_fans_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_fans;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            setTitle("我的会员");
        } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
            setTitle("我的粉丝");
        }
//        my_fans_back = (ImageView) findViewById(R.id.my_fans_back);
        //列表
        my_fans_recycler = (RecyclerView) findViewById(R.id.my_fans_recycler);
        my_fans_manager = new LinearLayoutManager(context);
        my_fans_recycler.setLayoutManager(my_fans_manager);
        my_fans_refresh = (SwipeRefreshLayout) findViewById(R.id.my_fans_refresh);
        my_fans_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        this.context = getApplicationContext();
        mMyFansDataManager = MyFansDataManager.getMyFansDataManager();
//        mInvitingDialog = new InvitingDialog(activity);

        //列表
        myFollowAdapter = new MyFansAdapter(activity, "MyFansActivity", new MyFansAdapter.OnItemListener() {
            @Override
            public void onClickItem(MyFansDTO posterData) {
                itemOnClickListenner(posterData);
            }

            @Override
            public void onClickReward(MyFansDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
                showInvitingDialog(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_fans_recycler.setLayoutManager(linearLayoutManager);
        my_fans_recycler.setAdapter(myFollowAdapter);

        //获取我的关注数据
        loadPosterData(1);

    }

    /**
     * 进入粉丝个人公开主页
     *
     * @param posterData
     */
    private void itemOnClickListenner(MyFansDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", posterData.uid.toString());
        bundle.putInt("user_type", posterData.user_type);
        bundle.putString("nick", posterData.nick.toString());
        bundle.putString("logo", posterData.logo.toString());
//        bundle.putInt("has_flow", posterData.has_flow);
        startActivity(new Intent(context, MyScrollViewActivity.class).putExtras(bundle));
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
//        //返回
//        my_fans_back.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                finish();
//            }
//        });
        // 刷新
        my_fans_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        my_fans_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (my_fans_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
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
     * 我的全部粉丝,0全部，1分销商，2不是分销商
     */
    public void loadPosterData(int currPage) {
        my_fans_refresh.setRefreshing(true);
        mMyFansDataManager.loadFansData(activity, "" + currPage, 0, new IRequestCallBack<MyFansQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                my_fans_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, MyFansQueryDTO myFansQueryDTO) {
                my_fans_refresh.setRefreshing(false);
                mMyFlowsTotalBean = myFansQueryDTO.total;
                if (myFansQueryDTO.list != null) {
                    refreshItem(myFansQueryDTO.list);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<MyFansDTO> posterData) {
        if (mMyFlowsTotalBean.currPage == 1) {
            myFollowAdapter.notifyData(posterData);
        } else {
            myFollowAdapter.addItems(posterData);
        }
    }

    /**
     * 邀请确认窗口
     */
    private void showInvitingDialog(final MyFansDTO posterData) {
        InvitationDistributionDialog mInvitingDialog = new InvitationDistributionDialog(this, posterData.nick, new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                toInviting(posterData);
            }
        });
        mInvitingDialog.show();
    }

    /**
     * 邀请分销
     */
    private void toInviting(MyFansDTO posterData) {
        //是否设置佣金判断
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION) != 1) {
            showCommissionSettingDialog();
            return;
        }
        showLoading();
        float rate = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE);
//        if (rate == 0) {
//            rate = 5;
//        }
        float discount = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT);
//        if (discount == 0) {
//            discount = 95;
//        }
        mMyFansDataManager.loadFansInvitingData(activity, posterData.uid + "", rate + "", discount + "", new IRequestCallBack<InvitationBean>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, InvitationBean invitationBean) {
                Toast.show(context, "发送邀请成功！");
                dismissLoading();
            }
        });
    }

    /**
     * 显示佣金设置对话框
     */
    private void showCommissionSettingDialog() {
        CommissionSettingDialog dialog = new CommissionSettingDialog(activity, CommissionSettingDialog.COMMON_TYPE, new CommissionSettingDialog.SettingCallBack() {
            @Override
            public void setFinish(final float rate, final float discount) {
                GoodsDataManager manager = GoodsDataManager.getGoodsDataManager();
                manager.setCommissionRate(activity, rate + "", discount + "", new IRequestCallBack() {
                    @Override
                    public void onFailed(int tag, String msg) {

                    }

                    @Override
                    public void onResult(int tag, Object object) {
                        Toast.show(activity, "设置成功！");
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE, rate);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT, discount);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION, 1);
                    }
                });
            }
        });
        dialog.show();
    }

}
