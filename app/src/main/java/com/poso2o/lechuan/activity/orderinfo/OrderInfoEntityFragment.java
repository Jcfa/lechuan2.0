package com.poso2o.lechuan.activity.orderinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.poso2o.lechuan.tool.recycler.OnSlideToBottomListener;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/12 0012.
 * 实体店列表数据
 */

public class OrderInfoEntityFragment extends BaseFragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rlv;
    private OrderInfoEntityAdapter adapter;
    private OrderInfoBean infoBean;
    private int currPage = 1;
    private List<DataBean> data;
    private TextView tvNum, tvMoeny;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderinfo_rlist, null);
        return view;
    }

    @Override
    public void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refreshlayout);
        rlv = (RecyclerView) view.findViewById(R.id.rlv_order_entity);
        tvNum = (TextView) view.findViewById(R.id.tv_order_info_num);
        tvMoeny = (TextView) view.findViewById(R.id.tv_order_info_moeny);
    }

    @Override
    public void initData() {
        String beginTime = getArguments().getString("beginTime");
        String endTime = getArguments().getString("endTime");
        // 刷新颜色
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        initNet(beginTime, endTime);
    }

    private void initNet(String beginTime, String endTime) {
        swipeRefreshLayout.setRefreshing(true);
        OrderInfoManager.getInfoManager().myOrderInfo((BaseActivity) getActivity(), beginTime, endTime, new IRequestCallBack<OrderInfoBean>() {
            @Override
            public void onResult(int tag, OrderInfoBean infoBeans) {
                dismissLoading();
                infoBean = infoBeans;
                data = infoBeans.getData();
                adapter = new OrderInfoEntityAdapter(getContext(), data);
                rlv.setAdapter(adapter);
                tvNum.setText("合计数量:" + infoBean.getTotal().getTotal_num());
                tvMoeny.setText("合计金额:" + infoBean.getTotal().getTotal_amount());
                swipeRefreshLayout.setRefreshing(false);

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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Integer.parseInt(infoBean.getTotal().getCurrPage()) == currPage) {
                    adapter.setData(data);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        rlv.addOnScrollListener(new OnSlideToBottomListener() {

            @Override
            protected void slideToBottom() {
                if (currPage > Integer.parseInt(infoBean.getTotal().getCurrPage())) {
                    currPage++;
                    adapter.AddData(data);
                }
            }
        });
    }

    public void onInstance(String str, String end) {
        boolean isdd = CalendarUtil.TimeCompare(str, end);
        if (isdd == false) {
            initNet(end, str);
        } else {
            initNet(str, end);
        }
    }
}
