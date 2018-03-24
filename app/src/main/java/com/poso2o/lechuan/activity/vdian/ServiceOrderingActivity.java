package com.poso2o.lechuan.activity.vdian;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.wopenaccountadapter.ServiceOrderingAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.vdian.ServiceOrderingTrialBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.EmpowermentManager;
import com.poso2o.lechuan.util.Toast;

import java.util.Collections;

/**
 * 服务订购
 *
 * Created by Administrator on 2018/3/14 0014.
 */
public class ServiceOrderingActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_TRY = "is_try";
    /**
     * 试用
     */
    private Button service_ordering_try;
    /**
     * 购买
     */
    private Button service_ordering_purchase;
    /**
     * 数据列表
     */
    private ListView service_ordering_list;

    private String service_name;
    private  double amount;
    private int service_id, service_type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_ordering;
    }

    @Override
    protected void initView() {
        service_ordering_try = (Button) findViewById(R.id.service_ordering_try);
        service_ordering_purchase = (Button) findViewById(R.id.service_ordering_purchase);
        service_ordering_list = (ListView) findViewById(R.id.service_ordering_list);
    }

    @Override
    protected void initData() {
        boolean isTry = getIntent().getBooleanExtra(IS_TRY, false);
        if (isTry) {
            setTitle("服务订购/试用");
        } else {
            setTitle("服务订购");
            service_ordering_try.setVisibility(View.GONE);
        }

        // 获取服务的信息
        EmpowermentManager.getInstance().trialListDate(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                Gson gson = new Gson();
                final ServiceOrderingTrialBean trial = gson.fromJson(result.toString(), ServiceOrderingTrialBean.class);
                Collections.reverse(trial.list);// 倒序
                final ServiceOrderingAdapter adapter = new ServiceOrderingAdapter(ServiceOrderingActivity.this, trial.list);
                service_ordering_list.setAdapter(adapter);
                setListViewHeightOnChildren(service_ordering_list);
                service_id = trial.list.get(0).service_id;
                amount = trial.list.get(0).amount;
                service_name = trial.list.get(0).service_name;
                service_type = trial.list.get(0).service_type;
                // 单选
                adapter.setOnAddClickListener(new ServiceOrderingAdapter.OnAddClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        adapter.setSelected(position);
                        adapter.notifyDataSetChanged();
                        service_id = trial.list.get(position).service_id;
                        amount = trial.list.get(position).amount;
                        service_name = trial.list.get(position).service_name;
                        service_type = trial.list.get(position).service_type;
                    }
                });
            }
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
            }
        });
    }

    @Override
    protected void initListener() {
        service_ordering_purchase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_ordering_purchase:
                // 点击立即订购
                if (service_id >0) {
                    Intent i = new Intent();
                    i.putExtra("service_id", service_id);
                    i.putExtra("amount", amount);
                    i.putExtra("service_name", service_name);
                    i.putExtra("service_type", service_type);
                    i.setClass(ServiceOrderingActivity.this, VdianPaymentActivity.class);
                    startActivity(i);
                }
                break;
        }
    }

    // 计算listView的高度
    public void setListViewHeightOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }
}
