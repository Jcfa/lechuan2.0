package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.wopenaccountadapter.ServiceOrderingTrialAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrialBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.ServiceOrderinTrialManager;

/**
 * 服务订购
 *
 * Created by Administrator on 2018/3/14 0014.
 */
public class ServiceOrderingTrialActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_TRY = "is_try";

    private TextView tv_title;
    private Button bt_wopen_try;
    private Button bt_wopen_trial_order;
    private ListView lsv_wopen_trim;
    private String service_name, amount, service_id, service_type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_service_ordering_trial;
    }

    @Override
    protected void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);

        bt_wopen_try = (Button) findViewById(R.id.bt_wopen_try);
        bt_wopen_trial_order = (Button) findViewById(R.id.bt_wopen_trial_order);
        lsv_wopen_trim = (ListView) findViewById(R.id.lsv_wopen_trim);

    }

    @Override
    protected void initData() {
        boolean isTry = getIntent().getBooleanExtra(IS_TRY, false);
        if (isTry) {
            setTitle("服务订购/试用");
        } else {
            setTitle("服务订购");
            bt_wopen_try.setVisibility(View.GONE);
        }

//        tv_title.setText(getResources().getString(R.string.service_orderint_trial));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));

        //获取服务的信息
        ServiceOrderinTrialManager manager = new ServiceOrderinTrialManager();
        manager.TrialListDate(this, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                Gson gson = new Gson();
                final ServiceOrderingTrialBean trial = gson.fromJson(result.toString(), ServiceOrderingTrialBean.class);
                final ServiceOrderingTrialAdapter adapter = new ServiceOrderingTrialAdapter(ServiceOrderingTrialActivity.this, trial.list);
                lsv_wopen_trim.setAdapter(adapter);
                setListViewHeightOnChildren(lsv_wopen_trim);
                service_id = trial.list.get(0).getService_id();
                amount = trial.list.get(0).getAmount();
                service_name = trial.list.get(0).getService_name();
                service_type = trial.list.get(0).getService_type();
                //单选
                adapter.setOnAddClickListener(new ServiceOrderingTrialAdapter.OnAddClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        adapter.setSelected(position);
                        adapter.notifyDataSetChanged();
                        service_id = trial.list.get(position).getService_id();
                        amount = trial.list.get(position).getAmount();
                        service_name = trial.list.get(position).getService_name();
                        service_type = trial.list.get(position).getService_type();
                    }
                });
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    @Override
    protected void initListener() {
        bt_wopen_trial_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_wopen_trial_order:

                //点击立即订购
                if (service_id != null) {
                    Intent i = new Intent();
                    i.putExtra("service_id", service_id);
                    i.putExtra("amount", amount);
                    i.putExtra("service_name", service_name);
                    i.putExtra("service_type", service_type);
                    i.setClass(ServiceOrderingTrialActivity.this, ServiceOrderActivity.class);
                    startActivity(i);
                }
                break;
        }
    }


    //计算listview的高度
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
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }
}
