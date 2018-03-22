package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.vdian.ServiceOrderingActivity;
import com.poso2o.lechuan.adapter.OaServiceAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrial;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrialBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.EmpowermentManager;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.FullyLinearLayoutManager;

/**
 * 公众号设置界面
 * <p>
 * Edit by Zhang on 2018/02/02
 */
public class OASetupFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private RecyclerView oa_set_wopen_trim;

    /**
     * 服务套餐适配器
     */
    private OaServiceAdapter serviceAdapter;

    /**
     * 服务名称，价格，id，类型
     */
    private String service_name, amount, service_id, service_type;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.fragment_oa_packages, null);
        return view;
    }

    @Override
    public void initView() {
        oa_set_wopen_trim = findView(R.id.oa_set_wopen_trim);
    }

    @Override
    public void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        serviceAdapter = new OaServiceAdapter(getContext(), null);
        oa_set_wopen_trim.setLayoutManager(linearLayoutManager);
        oa_set_wopen_trim.setAdapter(serviceAdapter);

        initService();
    }

    @Override
    public void initListener() {
        findView(R.id.oa_set_buy_service).setOnClickListener(this);
        serviceAdapter.setOnAddClickListener(new OaServiceAdapter.OnAddClickListener() {
            @Override
            public void onItemClick(int position, ServiceOrderingTrial item) {
                serviceAdapter.setSelected(position);
                serviceAdapter.notifyDataSetChanged();
                service_id = item.getService_id();
                amount = item.getAmount();
                service_name = item.getService_name();
                service_type = item.getService_type();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oa_set_buy_service:
                if (service_id!=null) {
                    Intent i = new Intent();
                    i.putExtra("service_id", service_id);
                    i.putExtra("amount", amount);
                    i.putExtra("service_name", service_name);
                    i.putExtra("service_type",service_type);
                    i.setClass(getContext(), ServiceOrderingActivity.class);
                    startActivity(i);
                }
                break;
        }
    }

    private void initService() {
        //获取服务的信息
        showLoading();
        EmpowermentManager.getInstance().trialListDate((BaseActivity) getActivity(), new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Gson gson = new Gson();
                final ServiceOrderingTrialBean trial = gson.fromJson(result.toString(), ServiceOrderingTrialBean.class);
                serviceAdapter.notifyDataSetChanged(trial.list);
                //默认第一项
                service_id = trial.list.get(0).getService_id();
                amount = trial.list.get(0).getAmount();
                service_name = trial.list.get(0).getService_name();
                service_type = trial.list.get(0).getService_type();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getContext(),msg);
            }
        });
    }
}
