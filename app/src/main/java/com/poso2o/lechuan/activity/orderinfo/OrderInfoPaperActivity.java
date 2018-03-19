package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
    private int type=1;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_paper;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlvPaper = (RecyclerView) findViewById(R.id.rlv_order_sell_list);

    }

    @Override
    protected void initData() {
        tvTitle.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK) + ">" + "库存管理");
        rlvPaper.setLayoutManager(new LinearLayoutManager(activity));
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
                    adapter.setOnItemClickListener(new OrderInfoPaperAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            OrderInfoPaperBean.DataBean dataBean = data.get(position);
                            OrderPaperDetailDialog dialog=new OrderPaperDetailDialog(activity);
                            dialog.show();
                            dialog.setData(dataBean.getGuid(),"",type);
                        }
                    });
                }

            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
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
}
