package com.poso2o.lechuan.activity.orderinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoMemberBean;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoMemberManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoMemberAdapter;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 * 会员管理
 */

public class OrderInfoMemberActivity extends BaseActivity {

    private TextView tvTitle;
    private RecyclerView rlv;
    private int type = 3;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_member;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlv = (RecyclerView) findViewById(R.id.lrv_member);

    }

    @Override
    protected void initData() {
        tvTitle.setText("会员管理");
        rlv.setLayoutManager(new LinearLayoutManager(activity));
        OrderInfoMemberManager.getsInstance().oInfoMember(activity, new IRequestCallBack<OrderInfoMemberBean>() {
            @Override
            public void onResult(int tag, OrderInfoMemberBean infoMemberBean) {
                dismissLoading();
                final List<OrderInfoMemberBean.DataBean> data = infoMemberBean.getData();
                final OrderInfoMemberAdapter adapter = new OrderInfoMemberAdapter(data);
                rlv.setAdapter(adapter);
                adapter.setOnItemClickListener(new OrderInfoMemberAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                        dialog.show();
                        dialog.setData(data.get(position).getUid(), "", type);
                    }
                });
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
}
