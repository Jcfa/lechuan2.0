package com.poso2o.lechuan.activity.orderinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoBean;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoEntityAdapter;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DeviceNetUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.tools.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店列表数据
 */

public class OrderInfoEntityFragment extends BaseFragment {

    private View view;
    private SmartRefreshLayout swipeRefreshLayout;
    private RecyclerView rlv;
    private OrderInfoEntityAdapter adapter;
    private OrderInfoBean infoBean;
    private int currPage = 1;
    private List<DataBean> data;
    private TextView tvNum, tvMoeny;
    private boolean aute = false;
    private FrameLayout flSearch;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private ImageView ivVisibility;
    private boolean isBeginTime;
    private String beginTime, endTime;
    private OrderInfoEntityFragment infoEntityFragment;
    private ImageView iv_search;
    private TextView tv_order_info_bgin_time, tv_order_end_time;
    //当点击搜索时进行隐藏
    private LinearLayout ll_vi;
    //进行显示搜索
    private FrameLayout fl_search;
    //输入搜索
    private EditText searchInput;
    //清楚搜索内容
    private ImageButton clearInput;
    //点击搜索
    private TextView tvSearch;
    //返回当前界面 搜索结束后
    private ImageView iv_back_search;
    private List<DataBean> list;
    //点击进去搜索的时候隐藏  退出时显示
    private RelativeLayout ll;
    //显示有数据界面
    private RelativeLayout rl_defult_null;
    //没有网络时 展示的界面
    private TextView iv_default_null;
    //设置用户名
    private TextView tvNick;
    private RelativeLayout rlSearchTitle;
    private ImageView ivBack;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderinfo_rlist, null);
        return view;
    }

    @Override
    public void initView() {
        //设置用户名
        tvNick = (TextView) view.findViewById(R.id.tv_title);
        swipeRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srl_refreshlayout);
        rlv = (RecyclerView) view.findViewById(R.id.rlv_order_entity);
        tvNum = (TextView) view.findViewById(R.id.tv_order_info_num);
        tvMoeny = (TextView) view.findViewById(R.id.tv_order_info_moeny);
        flSearch = (FrameLayout) view.findViewById(R.id.fl_search);
        tvBeginTime = (TextView) view.findViewById(R.id.tv_order_info_bgin_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_order_end_time);
        ivVisibility = (ImageView) view.findViewById(R.id.iv_order_time_visibility);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        tv_order_info_bgin_time = (TextView) view.findViewById(R.id.tv_order_info_bgin_time);
        tv_order_end_time = (TextView) view.findViewById(R.id.tv_order_end_time);
        ll_vi = (LinearLayout) view.findViewById(R.id.ll_vi);
        fl_search = (FrameLayout) view.findViewById(R.id.fl_search);
        searchInput = (EditText) view.findViewById(R.id.article_search_input);
        clearInput = (ImageButton) view.findViewById(R.id.clear_input);
        tvSearch = (TextView) view.findViewById(R.id.article_search);
        iv_back_search = (ImageView) view.findViewById(R.id.iv_back_search);
        ll = (RelativeLayout) view.findViewById(R.id.ll);
        rl_defult_null = (RelativeLayout) view.findViewById(R.id.rl_defult_null);
        iv_default_null = (TextView) view.findViewById(R.id.iv_default_null);
        rlSearchTitle = (RelativeLayout) view.findViewById(R.id.rl_search_title);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
    }

    @Override
    public void initData() {
        //默认为当天时间
        final String nowDay = CalendarUtil.getTodayDate();
        //本月第一天
        final String begin = CalendarUtil.getFirstDay();
        tvNick.setText("我的订单");
        iv_search.setVisibility(View.VISIBLE);
        ivVisibility.setVisibility(View.GONE);
        tvBeginTime.setText(begin);
        tvEndTime.setText(nowDay);
        //进行断线重连机制  心跳机制原理
        initNet(begin, nowDay, currPage);
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderInfoEntityAdapter(getContext());
        rlv.setAdapter(adapter);
    }

    private String beginTs, endTS;

    private void initNet(String beginTime, String endTime, final int currPage) {
        swipeRefreshLayout.autoRefresh(200);
        this.beginTs = beginTime;
        this.endTS = endTime;
        OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTime, endTime, currPage + "",
                new IRequestCallBack<OrderInfoBean>() {
                    @Override
                    public void onResult(int tag, OrderInfoBean infoBeans) {
                        dismissLoading();
                        infoBean = infoBeans;
                        List<DataBean> data = infoBeans.getData();
                        if (currPage == 1) {
                            adapter.setData(data);
                            tvNum.setText(": " + infoBean.getTotal().getTotal_num());
                            tvMoeny.setText(": " + infoBean.getTotal().getTotal_amount());
                        } else {
                            adapter.AddData(data);
                        }

                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        dismissLoading();
                        rl_defult_null.setVisibility(View.GONE);
                        iv_default_null.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currPage = 1;
//                swipeRefreshLayout.autoRefresh(200);
                initNet(beginTs, endTS, currPage);
             /*   OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTs, endTS, currPage + "",
                        new IRequestCallBack<OrderInfoBean>() {
                            @Override
                            public void onResult(int tag, OrderInfoBean infoBeans) {
                                dismissLoading();
                                infoBean = infoBeans;
                                List<DataBean> data = infoBeans.getData();
                                if (data == null && data.size() < 0) {
                                    Toast.show(context, "数据为空");
                                } else {
                                    adapter.setData(data);
                                    tvNum.setText(": " + infoBean.getTotal().getTotal_num());
                                    tvMoeny.setText(": " + infoBean.getTotal().getTotal_amount());
                                }

                            }

                            @Override
                            public void onFailed(int tag, String msg) {
                                dismissLoading();
                                rl_defult_null.setVisibility(View.GONE);
                                iv_default_null.setVisibility(View.VISIBLE);
                            }
                        });*/
                refreshlayout.finishRefresh(200);
            }
        });
        swipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currPage++;
                initNet(beginTs, endTS, currPage);
             /*   OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTs, endTS, currPage + "",
                        new IRequestCallBack<OrderInfoBean>() {
                            @Override
                            public void onResult(int tag, OrderInfoBean infoBeans) {
                                dismissLoading();
                                data = infoBeans.getData();
                                if (data == null || data.size() < 0) {
                                    Toast.show(context, "数据为空");
                                } else
                                    adapter.AddData(data);
                            }

                            @Override
                            public void onFailed(int tag, String msg) {
                                dismissLoading();
                                rl_defult_null.setVisibility(View.GONE);
                                iv_default_null.setVisibility(View.VISIBLE);
                            }
                        });*/
                refreshlayout.finishLoadmore(200);
            }
        });
        tv_order_info_bgin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBeginTime = true;
                showCalender();
            }
        });
        tv_order_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBeginTime = false;
                showCalender();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_vi.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                rlSearchTitle.setVisibility(View.VISIBLE);

            }
        });
        iv_back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_vi.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
                rlSearchTitle.setVisibility(View.GONE);
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                swipeRefreshLayout.autoRefresh();
            }
        });
        //输入搜索内
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ll.setVisibility(View.GONE);
                filterData(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
            }
        });
        ivBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                context.startActivity(new Intent(context, OrderInfoMainActivity.class));
                getActivity().finish();
                return false;
            }
        });
    }

    //搜索字段
    private void filterData(final String mesg) {
        OrderInfoManager.getInfoManager().searchOrderInfo((BaseActivity) context, beginTs,
                endTS, mesg.toString(), new IRequestCallBack<OrderInfoBean>() {
                    @Override
                    public void onResult(int tag, OrderInfoBean infoBean) {
                        list = infoBean.getData();
                        //创建新的集合
                        List<DataBean> newlists = new ArrayList<DataBean>();
                        if (TextUtils.isEmpty(mesg)) {
                            newlists = list;
                        } else {
                            newlists.clear();
                            //根据lists集合中的对象字段名过滤
                            for (DataBean sortModel : list) {
                                String num = sortModel.getOrder_id();
                                if (num.indexOf(mesg.toString()) != -1) {
                                    newlists.add(sortModel);
                                } else {
                                    Toast.show(context, "没有此订单号!");
                                }
                            }
                        }
                        // 不管怎么样都要刷新
                        adapter.updateListView(newlists);
                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        ((BaseActivity) context).dismissLoading();
                        rl_defult_null.setVisibility(View.GONE);
                        iv_default_null.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void onInstance(String str, String end) {
        boolean isdd = CalendarUtil.TimeCompare(str, end);
        if (isdd == false) {
            initNet(end, str, currPage);
        } else {
            initNet(str, end, currPage);
        }
    }

    //显示日期
    private void showCalender() {
        final CalendarDialog calendarDialog = new CalendarDialog(context);
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
                        calendarDialog.dismiss();
                    } else if (CalendarUtil.TimeCompare(dateT, str)) {
                        tvBeginTime.setText(dateT);
                        beginTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
                        calendarDialog.dismiss();
                        String end = CalendarUtil.stampToDate(beginTime);
                        onInstance(str, end);
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
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
                        onInstance(str, end);
                    } else {
                        Toast.show(context, "选择的时间范围不正确");
                    }
                }
            }
        });

    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            if (currPage == 1) {
                swipeRefreshLayout.finishRefresh(true);
            } else {
                swipeRefreshLayout.finishLoadmore(true);
            }
            rl_defult_null.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rl_defult_null.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        } else if (action.equals("网络已连接")) {
            rl_defult_null.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
            initNet(beginTs, endTS, currPage);
        } else if (action.equals("网络请求失败")) {
            rl_defult_null.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        }
    }
}
