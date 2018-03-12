package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MemberListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.member.AllMemberOrderData;
import com.poso2o.lechuan.bean.member.Member;
import com.poso2o.lechuan.bean.member.MemberAllData;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.MemberDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RMemberManager;
import com.poso2o.lechuan.manager.wshopmanager.WMemberManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class MemberActivity extends BaseActivity implements View.OnClickListener, CalendarDialog.OnDateSelectListener, MemberListAdapter.OnMemberClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context context;

    //返回
    private ImageView r_member_back;
    //开始时间
    private TextView member_calender_start;
    //结束时间
    private TextView member_calender_stop;
    //成交数
    private RelativeLayout buy_account;
    private ImageView buy_account_picture;
    //成交额
    private RelativeLayout buy_amount;
    private ImageView buy_amount_picture;
    //余额
    private RelativeLayout member_balance;
    private ImageView member_balance_picture;
    //积分
    private RelativeLayout member_integral;
    private ImageView member_integral_picture;

    //下拉刷新
    private SwipeRefreshLayout member_swipe_refresh;
    //列表
    private RecyclerView member_recycler;

    //合计
    private LinearLayout member_total_layout;
    //合计成交数
    private TextView total_member_buy_count;
    //合计成交额
    private TextView total_member_buy_amount;
    //合计余额
    private TextView total_member_balance;
    //合计积分
    private TextView total_member_integral;

    //日历选择弹窗
    private CalendarDialog calendarDialog;

    //当前开始时间
    private String mStartTime;
    //当前结束时间
    private String mEndTime;
    //是否为开始时间
    private boolean isStartTime;

    //列表适配器
    private MemberListAdapter listAdapter;

    //数据集合
    private ArrayList<Member> members = new ArrayList<>();
    //总页数
    private int mTotalPage;
    //当前页数
    private int mCurrPage = 1;
    //关键词
    private String keyword = "";

    //排序名称
    private String sortName = "member_purchase_number";
    //排序分类
    private String sortType = "DESC";
    //上一个选择的分类
    private ImageView preSort;
    //    private SharedPreferencesUtil sharedPreferencesUtil;
    private String shop_branch_id;

    //是否微店
    private boolean is_online = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_member;
    }

    @Override
    public void initView() {
        context = this;

        r_member_back = (ImageView) findViewById(R.id.r_member_back);

        member_calender_start = (TextView) findViewById(R.id.member_calender_start);

        member_calender_stop = (TextView) findViewById(R.id.member_calender_stop);

        buy_account = (RelativeLayout) findViewById(R.id.buy_account);

        buy_account_picture = (ImageView) findViewById(R.id.buy_account_picture);

        buy_amount = (RelativeLayout) findViewById(R.id.buy_amount);

        buy_amount_picture = (ImageView) findViewById(R.id.buy_amount_picture);

        member_balance = (RelativeLayout) findViewById(R.id.member_balance);

        member_balance_picture = (ImageView) findViewById(R.id.member_balance_picture);

        member_integral = (RelativeLayout) findViewById(R.id.member_integral);

        member_integral_picture = (ImageView) findViewById(R.id.member_integral_picture);

        member_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.member_swipe_refresh);

        member_recycler = (RecyclerView) findViewById(R.id.member_recycler);

        member_total_layout = (LinearLayout) findViewById(R.id.member_total_layout);

        total_member_buy_count = (TextView) findViewById(R.id.total_member_buy_count);

        total_member_buy_amount = (TextView) findViewById(R.id.total_member_buy_amount);

        total_member_balance = (TextView) findViewById(R.id.total_member_balance);

        total_member_integral = (TextView) findViewById(R.id.total_member_integral);

        member_swipe_refresh.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        initDate();
        initAdapter();
        preSort = buy_account_picture;
        requestMemberList();
    }

    @Override
    public void initListener() {
        r_member_back.setOnClickListener(this);
        member_calender_start.setOnClickListener(this);
        member_calender_stop.setOnClickListener(this);
        listAdapter.setOnMemberClickListener(this);
        buy_account.setOnClickListener(this);
        buy_amount.setOnClickListener(this);
        member_balance.setOnClickListener(this);
        member_integral.setOnClickListener(this);
        member_swipe_refresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_member_back:
                finish();
                break;
            case R.id.member_calender_start:
                onDateClick(true);
                break;
            case R.id.member_calender_stop:
                onDateClick(false);
                break;
            case R.id.buy_account:
                clickBuyCount();
                break;
            case R.id.buy_amount:
                clickBuyAmount();
                break;
            case R.id.member_balance:
                clickBalance();
                break;
            case R.id.member_integral:
                clickIntegral();
                break;
        }
    }

    @Override
    public void onDateSelect(CustomDate date) {
        dateSelect(date);
    }

    @Override
    public void onMemberClick(final Member member) {
        showLoading("正在加载会员详情...");
        if (is_online) {
            WMemberManager.getwMemberManager().wMemberInfo(this, member.member_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    AllMemberOrderData allMemberOrderData = (AllMemberOrderData) object;
                    toMemberDetail(member, allMemberOrderData);
                }
            });
        }else {
            RMemberManager.getRMemberManager().rMemberInfo(this, member.uid,"2012-01-01",CalendarUtil.getTodayDate(), new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    AllMemberOrderData allMemberOrderData = (AllMemberOrderData) result;
                    toMemberDetail(member, allMemberOrderData);
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }
            });
        }
    }

    @Override
    public void onLoadMore(TextView textView, ProgressBar progressBar) {
        //加载更多
        if (mCurrPage < mTotalPage) {
            textView.setText("正在加载更多。。。");
            progressBar.setVisibility(View.VISIBLE);
            loadMore();
        } else {
            textView.setText("     数据加载完毕");
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initDate() {
        member_calender_start.setText(CalendarUtil.getFirstDay());
        member_calender_stop.setText(CalendarUtil.getTodayDate());
        mStartTime = CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00");
        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
    }

    private void onDateClick(boolean b) {
        isStartTime = b;
        calendarDialog = new CalendarDialog(context);
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(this);
    }

    private void dateSelect(CustomDate date) {
        if (date == null) return;
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isStartTime) {
            if (CalendarUtil.TimeCompare(dateT, member_calender_stop.getText().toString())) {
                member_calender_start.setText(dateT);
                mStartTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                calendarDialog.dismiss();
                requestMemberList();
            } else {
                Toast.show(context, "选择的时间范围不正确");
            }
        } else {
            if (CalendarUtil.TimeCompare(member_calender_start.getText().toString(), dateT)) {
                member_calender_stop.setText(dateT);
                mEndTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
                calendarDialog.dismiss();
                requestMemberList();
            } else {
                Toast.show(context, "选择的时间范围不正确");
            }
        }
    }

    private void initAdapter() {
        listAdapter = new MemberListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        member_recycler.setLayoutManager(linearLayoutManager);
        member_recycler.setAdapter(listAdapter);
    }

    private void requestMemberList() {
        if (!member_swipe_refresh.isRefreshing()) member_swipe_refresh.setRefreshing(true);
        mCurrPage = 1;
        if (is_online) {
            //微店
            WMemberManager.getwMemberManager().wMemberList(this, keyword, mCurrPage + "", sortName, sortType, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    member_swipe_refresh.setRefreshing(false);
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    member_swipe_refresh.setRefreshing(false);
                    if (listAdapter == null) return;
                    MemberAllData memberAllData = (MemberAllData) object;
                    if (memberAllData == null) {
                        mTotalPage = 0;
                        total_member_buy_count.setText("0");
                        total_member_buy_amount.setText("0.00");
                        total_member_balance.setText("0.00");
                        total_member_integral.setText("0");
                        members.clear();
                        listAdapter.notifyAdapter(members);
                        return;
                    }
                    mTotalPage = memberAllData.total.pages;
                    total_member_buy_count.setText(memberAllData.total.total_member_purchase_number + "");
                    total_member_buy_amount.setText(memberAllData.total.total_member_consume);
                    total_member_balance.setText(memberAllData.total.total_member_balance);
                    total_member_integral.setText(memberAllData.total.total_member_integral);
                    members.clear();
                    members.addAll(memberAllData.list);
                    listAdapter.notifyAdapter(members);
                }
            });
        } else {
            //实体店
            RMemberManager.getRMemberManager().rMemberList(this, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    member_swipe_refresh.setRefreshing(false);
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    member_swipe_refresh.setRefreshing(false);
                    if (listAdapter == null) return;
                    MemberAllData memberAllData = (MemberAllData) object;
                    if (memberAllData == null) {
                        mTotalPage = 0;
                        total_member_buy_count.setText("0");
                        total_member_buy_amount.setText("0.00");
                        total_member_balance.setText("0.00");
                        total_member_integral.setText("0");
                        members.clear();
                        listAdapter.notifyAdapter(members);
                        return;
                    }
                    if (memberAllData.total != null && memberAllData.total.pages != 0) {
                        member_total_layout.setVisibility(View.VISIBLE);
                        mTotalPage = memberAllData.total.pages;
                        total_member_buy_count.setText(memberAllData.total.total_member_purchase_number + "");
                        total_member_buy_amount.setText(memberAllData.total.total_member_consume);
                        total_member_balance.setText(memberAllData.total.total_member_balance);
                        total_member_integral.setText(memberAllData.total.total_member_integral);
                    }else {
                        member_total_layout.setVisibility(View.GONE);
                    }
                    members.clear();
                    members.addAll(memberAllData.list);
                    listAdapter.notifyAdapter(members);
                }
            });
        }
    }

    private void loadMore() {
        mCurrPage++;
        if (is_online) {
            WMemberManager.getwMemberManager().wMemberList(this, keyword, mCurrPage + "", sortName, sortType, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    mCurrPage--;
                    member_swipe_refresh.setRefreshing(false);
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    MemberAllData memberAllData = (MemberAllData) object;
                    if (listAdapter != null) return;
                    if (memberAllData == null) return;
                    members.addAll(memberAllData.list);
                    listAdapter.notifyAdapter(members);
                }
            });
        } else {
            RMemberManager.getRMemberManager().rMemberList(this, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    member_swipe_refresh.setRefreshing(false);
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    MemberAllData memberAllData = (MemberAllData) object;
                    if (listAdapter != null) return;
                    if (memberAllData == null) return;
                    members.addAll(memberAllData.list);
                    listAdapter.notifyAdapter(members);
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        requestMemberList();
    }

    //点击成交数
    private void clickBuyCount() {
        if (preSort != buy_account_picture) {
            preSort.setImageResource(R.mipmap.icon_default_sort);
            preSort = buy_account_picture;
            sortName = "member_purchase_number";
            sortType = "DESC";
            buy_account_picture.setImageResource(R.mipmap.down_sort);
        } else if (sortType.equals("DESC")) {
            sortType = "ASC";
            buy_account_picture.setImageResource(R.mipmap.icon_up_sort);
        } else {
            sortType = "DESC";
            buy_account_picture.setImageResource(R.mipmap.down_sort);
        }
        if (is_online){
            requestMemberList();
        }else {
            listAdapter.notifyAdapter(RMemberManager.getRMemberManager().queryMember(1,sortType));
        }
    }

    //点击成交额
    private void clickBuyAmount() {
        if (preSort != buy_amount_picture) {
            preSort.setImageResource(R.mipmap.icon_default_sort);
            preSort = buy_amount_picture;
            sortName = "member_consume";
            sortType = "DESC";
            buy_amount_picture.setImageResource(R.mipmap.down_sort);
        } else if (sortType.equals("DESC")) {
            sortType = "ASC";
            buy_amount_picture.setImageResource(R.mipmap.icon_up_sort);
        } else {
            sortType = "DESC";
            buy_amount_picture.setImageResource(R.mipmap.down_sort);
        }
        if (is_online){
            requestMemberList();
        }else {
            listAdapter.notifyAdapter(RMemberManager.getRMemberManager().queryMember(2,sortType));
        }
    }

    //点击余额
    private void clickBalance() {
        if (preSort != member_balance_picture) {
            preSort.setImageResource(R.mipmap.icon_default_sort);
            preSort = member_balance_picture;
            sortName = "member_balance";
            sortType = "DESC";
            member_balance_picture.setImageResource(R.mipmap.down_sort);
        } else if (sortType.equals("DESC")) {
            sortType = "ASC";
            member_balance_picture.setImageResource(R.mipmap.icon_up_sort);
        } else {
            sortType = "DESC";
            member_balance_picture.setImageResource(R.mipmap.down_sort);
        }
        if (is_online){
            requestMemberList();
        }else {
            listAdapter.notifyAdapter(RMemberManager.getRMemberManager().queryMember(3,sortType));
        }
    }

    //点击积分
    private void clickIntegral() {
        if (preSort != member_integral_picture) {
            preSort.setImageResource(R.mipmap.icon_default_sort);
            preSort = member_integral_picture;
            sortName = "member_integral";
            sortType = "DESC";
            member_integral_picture.setImageResource(R.mipmap.down_sort);
        } else if (sortType.equals("DESC")) {
            sortType = "ASC";
            member_integral_picture.setImageResource(R.mipmap.icon_up_sort);
        } else {
            sortType = "DESC";
            member_integral_picture.setImageResource(R.mipmap.down_sort);
        }
        if (is_online){
            requestMemberList();
        }else {
            listAdapter.notifyAdapter(RMemberManager.getRMemberManager().queryMember(4,sortType));
        }
    }

    private void toMemberDetail(Member member, AllMemberOrderData allMemberOrderData) {
        MemberDetailDialog detailDialog = new MemberDetailDialog(context);
        detailDialog.show();
        if (is_online){
            detailDialog.initDatas(true,member, allMemberOrderData.list);
        }else {
            detailDialog.initDatas(false,member, allMemberOrderData.lists);
        }
    }

}
