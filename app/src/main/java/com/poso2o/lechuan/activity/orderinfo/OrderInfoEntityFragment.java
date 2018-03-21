package com.poso2o.lechuan.activity.orderinfo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoEntityAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderinfo_rlist, null);
        return view;
    }

    @Override
    public void initView() {
        swipeRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srl_refreshlayout);
        rlv = (RecyclerView) view.findViewById(R.id.rlv_order_entity);
        tvNum = (TextView) view.findViewById(R.id.tv_order_info_num);
        tvMoeny = (TextView) view.findViewById(R.id.tv_order_info_moeny);
    }

    @Override
    public void initData() {
        String beginTime = getArguments().getString("beginTime");
        String endTime = getArguments().getString("endTime");
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        if (aute == false) {
            swipeRefreshLayout.autoRefresh(100);
            aute = true;
        }
        initNet(beginTime, endTime, currPage);

    }

    private String beginTs, endTS;

    private void initNet(String beginTime, String endTime, final int currPage) {
        swipeRefreshLayout.autoRefresh(100);
        this.beginTs = beginTime;
        this.endTS = endTime;
        OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTime, endTime, currPage + "",
                new IRequestCallBack<OrderInfoBean>() {
                    @Override
                    public void onResult(int tag, OrderInfoBean infoBeans) {
                        dismissLoading();
                        infoBean = infoBeans;
                        data = infoBeans.getData();
                        adapter = new OrderInfoEntityAdapter(getContext());
                        rlv.setAdapter(adapter);
                      /*  if (data == null || data.size() < 0) {
                            Toast.show(context, "数据为空");
                        } else {
                            adapter.setData(data);
                            tvNum.setText("合计数量:" + infoBean.getTotal().getTotal_num());
                            tvMoeny.setText("合计金额:" + infoBean.getTotal().getTotal_amount());
                        }*/

                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        dismissLoading();
                        Toast.show(getContext(), msg);
                    }
                });
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currPage = 1;
                OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTs, endTS, currPage + "",
                        new IRequestCallBack<OrderInfoBean>() {
                            @Override
                            public void onResult(int tag, OrderInfoBean infoBeans) {
                                dismissLoading();
                                infoBean = infoBeans;
                                data = infoBeans.getData();
                                if (data == null || data.size() < 0) {
                                    Toast.show(context, "数据为空");
                                } else {
                                    adapter.setData(data);
                                    tvNum.setText("合计数量:" + infoBean.getTotal().getTotal_num());
                                    tvMoeny.setText("合计金额:" + infoBean.getTotal().getTotal_amount());
                                }

                            }

                            @Override
                            public void onFailed(int tag, String msg) {
                                dismissLoading();
                                Toast.show(getContext(), msg);
                            }
                        });
                refreshlayout.finishRefresh(2000);
            }
        });
        swipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currPage++;
                OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTs, endTS, currPage + "",
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
                                Toast.show(getContext(), msg);
                            }
                        });
                refreshlayout.finishLoadmore(2000);
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
}
