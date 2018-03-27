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
import com.poso2o.lechuan.bean.vdian.ServiceOrderingTrial;
import com.poso2o.lechuan.bean.vdian.ServiceOrderingTrialBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.EmpowermentManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 服务订购
 * <p>
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
    private double amount;
    private int service_id, service_type;
    private int mServiceId;//已购买的服务ID
    private int mModuleId;//微店或公众号助手
    private ServiceOrderingAdapter mAdapter;
    private ArrayList<ServiceOrderingTrial> serviceList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_ordering;
    }

    @Override
    protected void initView() {
//        mServiceId = getIntent().getIntExtra(AuthorizationActivity.KEY_SERVICE_ID, 0);
        mServiceId = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_ID_OA, 0);
        mModuleId = getIntent().getIntExtra(AuthorizationActivity.KEY_MODULE_ID, 0);
        service_ordering_try = (Button) findViewById(R.id.service_ordering_try);
        service_ordering_purchase = (Button) findViewById(R.id.service_ordering_purchase);
        service_ordering_list = (ListView) findViewById(R.id.service_ordering_list);
        mAdapter = new ServiceOrderingAdapter(ServiceOrderingActivity.this, serviceList);
        service_ordering_list.setAdapter(mAdapter);
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
        showLoading();
        // 获取服务的信息
        EmpowermentManager.getInstance().trialListDate(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Gson gson = new Gson();
                final ServiceOrderingTrialBean trial = gson.fromJson(result.toString(), ServiceOrderingTrialBean.class);
                Collections.reverse(trial.list);// 倒序
                serviceList = trial.list;
                mAdapter.notifyDataSetChanged(serviceList);
                setListViewHeightOnChildren(service_ordering_list);
                service_id = trial.list.get(0).service_id;
                amount = trial.list.get(0).amount;
                service_name = trial.list.get(0).service_name;
                service_type = trial.list.get(0).service_type;
                // 单选
                mAdapter.setOnAddClickListener(new ServiceOrderingAdapter.OnAddClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ServiceOrderingTrial orderingTrial = serviceList.get(position);
                        if ((mServiceId > 0 && mServiceId > orderingTrial.service_id) || !orderingTrial.enable) {//只能选择不低于当前的服务
                            return;
                        }
                        setSelectedService(position);
                    }
                });
                setDefaultService();
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
        service_ordering_purchase.setOnClickListener(this);
    }

    /**
     * 设置选中默认的服务，比当前高一级的服务
     */
    private void setDefaultService() {
        ServiceOrderingTrial defaultService = null;
        int position = 0;
        for (int i = 0; i < serviceList.size(); i++) {
            ServiceOrderingTrial orderingTrial = serviceList.get(i);
            if (orderingTrial.service_id < mServiceId) {
                orderingTrial.enable = false;
            } else {
                orderingTrial.enable = true;
            }
            if (defaultService == null) {
                defaultService = orderingTrial;
                continue;
            }
            if (orderingTrial.service_id > mServiceId && orderingTrial.service_id < defaultService.service_id) {
                defaultService = orderingTrial;
                position = i;
            }
        }
        setSelectedService(position);
    }

    /**
     * 选中第几个服务
     *
     * @param position
     */
    private void setSelectedService(int position) {
        mAdapter.setSelected(position);
        mAdapter.notifyDataSetChanged();
        service_id = serviceList.get(position).service_id;
        amount = serviceList.get(position).amount;
        service_name = serviceList.get(position).service_name;
        service_type = serviceList.get(position).service_type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_ordering_purchase:
                // 点击立即订购
                if (service_id > 0) {
                    Intent i = new Intent();
                    i.putExtra("service_id", service_id);
                    i.putExtra("amount", amount);
                    i.putExtra("service_name", service_name);
                    i.putExtra("service_type", service_type);
                    i.putExtra(AuthorizationActivity.KEY_MODULE_ID, mModuleId);
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
