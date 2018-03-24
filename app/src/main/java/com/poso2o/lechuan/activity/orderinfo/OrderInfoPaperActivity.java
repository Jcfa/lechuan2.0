package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPaperBean;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPaperManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 库存管理
 */

public class OrderInfoPaperActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private RecyclerView rlvPaper;
    private OrderInfoPaperAdapter adapter;
    private int type = 1;
    private TextView tv_order_sell_many_total, tv_order_zm_total;
    private TextView tv_default_null;
    private RelativeLayout rl_default_null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_paper;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlvPaper = (RecyclerView) findViewById(R.id.rlv_order_sell_list);
        tv_order_sell_many_total = (TextView) findViewById(R.id.tv_order_sell_many_total);
        tv_order_zm_total = (TextView) findViewById(R.id.tv_order_zm_total);
        tv_default_null = (TextView) findViewById(R.id.tv_default_null);
        rl_default_null = (RelativeLayout) findViewById(R.id.rl2);

    }

    @Override
    protected void initData() {
        tvTitle.setText("库存管理");
        rlvPaper.setLayoutManager(new LinearLayoutManager(activity));
        initNetApi();

    }

    private void initNetApi() {
        OrderInfoPaperManager.getsInsatcne().orderInfoPaperApi(activity, new IRequestCallBack<OrderInfoPaperBean>() {
            @Override
            public void onResult(int tag, OrderInfoPaperBean paperBean) {
                dismissLoading();
                final List<OrderInfoPaperBean.DataBean> data = paperBean.getData();
                if (data == null || data.size() < 0) {
                    Toast.show(activity, "数据为空");
                } else {
                    adapter = new OrderInfoPaperAdapter(activity, data);
                    rlvPaper.setAdapter(adapter);
                    tv_order_sell_many_total.setText(paperBean.getTotal().getTotalnums());
                    tv_order_zm_total.setText(paperBean.getTotal().getTotalamounts());
                    adapter.setOnItemClickListener(new OrderInfoPaperAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            OrderInfoPaperBean.DataBean dataBean = data.get(position);
                            OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                            dialog.show();
                            dialog.setData(dataBean.getGuid(), "", type);
                        }
                    });
                }

            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                tv_default_null.setVisibility(View.VISIBLE);
                rl_default_null.setVisibility(View.GONE);
                rlvPaper.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initListener() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                break;
        }
    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            rlvPaper.setVisibility(View.VISIBLE);
            rl_default_null.setVisibility(View.VISIBLE);
            tv_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rlvPaper.setVisibility(View.GONE);
            rl_default_null.setVisibility(View.GONE);
            tv_default_null.setVisibility(View.VISIBLE);
        } else if (action.equals("网络已连接")) {
            rlvPaper.setVisibility(View.VISIBLE);
            rl_default_null.setVisibility(View.VISIBLE);
            tv_default_null.setVisibility(View.GONE);
            initNetApi();
        } else if (action.equals("网络请求失败")) {
            rlvPaper.setVisibility(View.GONE);
            rl_default_null.setVisibility(View.GONE);
            tv_default_null.setVisibility(View.VISIBLE);
        }
    }
}
