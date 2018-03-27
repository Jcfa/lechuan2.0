package com.poso2o.lechuan.activity.orderinfo;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.OAHelperActivity;
import com.poso2o.lechuan.activity.vdian.AuthorizationActivity;
import com.poso2o.lechuan.activity.vdian.EmpowermentActivity;
import com.poso2o.lechuan.activity.vdian.ServiceOrderingActivity;
import com.poso2o.lechuan.activity.vdian.VdianActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellCountBean;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.OrderInfoExitApp;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoSellManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;
import com.yanzhenjie.nohttp.tools.NetUtils;

import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_AUTHORIZATION_OA;
import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP;
import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_SERVICE_ID_OA;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 订单信息主界面
 */

public class OrderInfoMainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llOrderEntityShop;  // 实体店
    private LinearLayout llOrderPaper;  // 畅销管理
    private LinearLayout llOrderSell;  // 库存管理
    private LinearLayout llOrderWeid;  // 微店
    private LinearLayout ll_order_poplstaff;  // 人员业绩
    private LinearLayout ll_order_primecost;
    private LinearLayout ll_order_exit;//退出登录

    //公众号助手
    private LinearLayout ll_order_public_hao;

    private LinearLayout ll_order_members;//会员管理
    private TextView tvBeginTime;//开始时间
    private TextView tvEndTime;//结束时间
    private boolean isBeginTime;
    private String beginTime;
    private String endTime;
    //销售额 目标额 完成率 毛利润
    private TextView tvSellPrice, tvAimPrice, tvComPletePrice, tvGpmPrice;
    private TextView tvNick;
    private OrderInfoExitApp detailDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_main;
    }

    @Override
    protected void initView() {

        llOrderEntityShop = (LinearLayout) findViewById(R.id.ll_order_entity_shop);

        llOrderSell = (LinearLayout) findViewById(R.id.ll_order_sell);

        llOrderPaper = (LinearLayout) findViewById(R.id.ll_order_paper);

        llOrderWeid = (LinearLayout) findViewById(R.id.ll_order_weid);

        ll_order_poplstaff = (LinearLayout) findViewById(R.id.ll_order_poplstaff);

        ll_order_primecost = (LinearLayout) findViewById(R.id.ll_order_primecost);
        ll_order_members = (LinearLayout) findViewById(R.id.ll_order_members);
        ll_order_exit = (LinearLayout) findViewById(R.id.ll_order_exit);

        ll_order_public_hao = findView(R.id.ll_order_public_hao);

        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);

        //销售统计
        tvSellPrice = (TextView) findViewById(R.id.tv_order_sell_price);
        tvAimPrice = (TextView) findViewById(R.id.tv_order_aim_price);
        tvComPletePrice = (TextView) findViewById(R.id.tv_order_complete_price);
        tvGpmPrice = (TextView) findViewById(R.id.tv_order_gpm_price);
        //设置用户名
        tvNick = (TextView) findViewById(R.id.tv_title);
        //第一个界面隐藏返回图标
        findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);

    }

    @Override
    protected void initData() {
        //默认为当天时间
        String nowDay = CalendarUtil.getTodayDate();
        String begin = CalendarUtil.getFirstDay();
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        beginTime = tvBeginTime.getText().toString();
        endTime = tvEndTime.getText().toString();
        String nick = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK);
        tvNick.setText(nick);
        //网络请求
        initNetRequest(beginTime, endTime);

    }

    private void initNetRequest(String beginTime, String endTime) {
        OrderInfoSellManager.getOrderInfo().orderInfoSell(activity, beginTime, endTime, new IRequestCallBack<OrderInfoSellCountBean>() {
            @Override
            public void onResult(int tag, OrderInfoSellCountBean result) {
                OrderInfoSellCountBean sellCountBean = result;
                initNetData(sellCountBean);
                loadAccountDetailState();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    //初始化网络数据
    private void initNetData(OrderInfoSellCountBean data) {
        tvSellPrice.setText(data.getOrder_amounts());
        tvAimPrice.setText(data.getAssignment());
        tvComPletePrice.setText(data.getCompletion_rate() + "%");
        tvGpmPrice.setText(data.getGross_profit());
    }

    private void setCalender() {
        final CalendarDialog calendarDialog = new CalendarDialog(this);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(new CalendarDialog.OnDateSelectListener() {
            @Override
            public void onDateSelect(CustomDate date) {
                if (date == null) return;
                String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (isBeginTime) {
                    String str = tvEndTime.getText().toString();
                    if (str == null || str.equals("")) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(beginTime);
                        initTimes(str, end);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                } else {
                    String str = tvBeginTime.getText().toString();
                    if (str == null || str.equals("")) {
                        tvEndTime.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(str, dateT)) {
                        tvEndTime.setText(dateT);
                        endTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(endTime);
                        initTimes(str, end);
                    } else {
                        Toast.show(activity, "选择的时间范围不正确");
                    }
                }
            }
        });

    }

    /**
     * 进行时间的比较  再进行交换 开始时间必须小于结束时间
     */
    private void initTimes(String str, String end) {
        boolean isdd = CalendarUtil.TimeCompare(str, end);
        if (isdd == false) {
            initNetRequest(end, str);
        } else {
            initNetRequest(str, end);
        }
    }

    @Override
    protected void initListener() {
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        llOrderPaper.setOnClickListener(this);
        ll_order_poplstaff.setOnClickListener(this);
        ll_order_primecost.setOnClickListener(this);
        ll_order_members.setOnClickListener(this);
        ll_order_exit.setOnClickListener(this);
        // 实体店
        llOrderEntityShop.setOnClickListener(this);
        //   微店  1
        llOrderSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Print.println("KEY_USER_HAS_WEBSHOP=" + SharedPreferencesUtils.getInt(KEY_USER_HAS_WEBSHOP, 0));
                Print.println("AUTHORIZATION_OA_FALSE=" + SharedPreferencesUtils.getInt(KEY_USER_AUTHORIZATION_OA, Constant.AUTHORIZATION_OA_FALSE));
                if (SharedPreferencesUtils.getInt(KEY_USER_HAS_WEBSHOP, 0) == 1 && SharedPreferencesUtils.getInt(KEY_USER_AUTHORIZATION_OA, Constant.AUTHORIZATION_OA_FALSE) == Constant.AUTHORIZATION_OA_TRUE) {//有微店并授权了公众号
                    if (SharedPreferencesUtils.getInt(KEY_USER_SERVICE_ID_OA, 0) > 0) {//已购买服务了
                        startActivity(VdianActivity.class);
                    } else {//未购买服务
                        Intent intent = new Intent(activity, ServiceOrderingActivity.class);
                        intent.putExtra(AuthorizationActivity.KEY_MODULE_ID, Constant.BOSS_MODULE_WX);
                        startActivity(intent);
                    }
                } else {//没微店或没授权
                    startActivity(new Intent(activity, EmpowermentActivity.class).putExtra(AuthorizationActivity.KEY_MODULE_ID, Constant.BOSS_MODULE_WX));
                }
            }
        });
        // 微店  //库存 1
        llOrderWeid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoPaperActivity.class));

            }
        });
        //公众号助手
        ll_order_public_hao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_info_bgin_time:
                isBeginTime = true;
                setCalender();
                break;
            case R.id.tv_order_end_time:
                isBeginTime = false;
                setCalender();
                break;
            case R.id.ll_order_entity_shop://实体店
                startActivity(new Intent(OrderInfoMainActivity.this, OrderEntityShopActivity.class));
                break;
            //库存管理   //公众号助手 1
            case R.id.ll_order_paper:
                if (SharedPreferencesUtils.getInt(KEY_USER_AUTHORIZATION_OA, Constant.AUTHORIZATION_OA_FALSE) == Constant.AUTHORIZATION_OA_TRUE) {//公众号已授权成功过了
                    if (SharedPreferencesUtils.getInt(KEY_USER_SERVICE_ID_OA, 0) == Constant.OA_SERVICE_ID) {//已购买有公众号助手的服务
                        startActivity(new Intent(activity, OAHelperActivity.class).putExtra(AuthorizationActivity.KEY_MODULE_ID, Constant.BOSS_MODULE_OA));
                    } else {//未购买服务、或购买的服务没有公众号助手的权限
                        Intent intent = new Intent(activity, ServiceOrderingActivity.class);
                        intent.putExtra(AuthorizationActivity.KEY_MODULE_ID, Constant.BOSS_MODULE_OA);
                        startActivity(intent);
                    }
                } else {//未授权
                    startActivity(new Intent(activity, EmpowermentActivity.class).putExtra(AuthorizationActivity.KEY_MODULE_ID, Constant.BOSS_MODULE_OA));
                }

                break;
            //人员业绩   //会员1
            case R.id.ll_order_poplstaff:
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoMemberActivity.class));

                break;
            //月损益表  //业绩
            case R.id.ll_order_primecost:
                startActivity(new Intent(OrderInfoMainActivity.this, OrderPoplstaffActivity.class));
                break;
            //会员管理  //畅销 1
            case R.id.ll_order_members:
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoSellActivity.class));
                break;
            //改  损益 1
            case R.id.ll_order_public_hao:
                startActivity(new Intent(OrderInfoMainActivity.this, OrderInfoPrimecostActivity.class));
                break;
            //退出登录
            case R.id.ll_order_exit:
                detailDialog = new OrderInfoExitApp(this);
                detailDialog.show();
                break;
        }
    }

    /**
     * 帐号的详情
     */
    private void loadAccountDetailState() {
        WShopManager.getrShopManager().getlcAccountDetailInfo(this, new IRequestCallBack<UserInfoBean>() {
            @Override
            public void onResult(int tag, UserInfoBean result) {
                dismissLoading();
                if (result == null) {
                    Toast.show(activity, "帐号信息失败");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络已连接")) {
            initNetRequest(beginTime, endTime);
        }
    }
}
