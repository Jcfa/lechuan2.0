package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.activity.oa.ArticleInfoActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.RenewalsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.oa.RenewalsBean;
import com.poso2o.lechuan.bean.oa.RenewalsList;
import com.poso2o.lechuan.dialog.DeleteRenewalsDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.RenewalsManager;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * 公众号助手-发布-稿件箱
 * Created by Administrator on 2018-02-03.
 */

public class PublishDraftFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    //无稿件提示
    private TextView tips_no_renewals;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private RenewalsAdapter renewalsAdapter;
    private ArrayList<RenewalsBean> renewals = new ArrayList<>();
    private DeleteRenewalsDialog dialog;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish_draft, container, false);
    }

    @Override
    public void initView() {

        tips_no_renewals = findView(R.id.tips_no_renewals);

        swipeRefreshLayout = findView(R.id.swipeRefreshLayout);

        recyclerView = findView(R.id.recyclerView);

    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerViewDivider(
                context, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        renewalsAdapter = new RenewalsAdapter(getContext(),renewals);
        recyclerView.setAdapter(renewalsAdapter);

        initRenewalsData();
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        renewalsAdapter.setOnRenewalDelListener(new RenewalsAdapter.OnRenewalDelListener() {
            @Override
            public void onRenewalDel(RenewalsBean renewalsBean) {
                showDelDialog(renewalsBean);
            }
        });

        renewalsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<RenewalsBean>() {
            @Override
            public void onItemClick(RenewalsBean item) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ArticleInfoActivity.class);
                intent.putExtra(ArticleAdActivity.ART_DATA,item.articles);
                intent.putExtra(ArticleAdActivity.RENEWALS_ID,item.news_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        initRenewalsData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取稿件箱
     */
    private void initRenewalsData(){
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsList((BaseActivity) getActivity(), 1, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                swipeRefreshLayout.setRefreshing(false);
                RenewalsList renewalsList = (RenewalsList) result;
                renewals.clear();
                renewals.addAll(renewalsList.list);
                renewalsAdapter.notifyDataSetChanged(renewals);
                if (renewals.size() == 0){
                    tips_no_renewals.setVisibility(View.VISIBLE);
                }else {
                    tips_no_renewals.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                swipeRefreshLayout.setRefreshing(false);
                Toast.show(getContext(),msg);
            }
        });
    }

    /**
     * 显示删除稿件提示框
     * @param renewals
     */
    private void showDelDialog(RenewalsBean renewals){
        if (dialog == null)dialog = new DeleteRenewalsDialog(getContext(),onDelRenewalsListener);
        dialog.show();
        dialog.setRenewals(renewals);
    }

    private DeleteRenewalsDialog.OnDelRenewalsListener onDelRenewalsListener = new DeleteRenewalsDialog.OnDelRenewalsListener() {
        @Override
        public void onDelRenewals(RenewalsBean renewalsBean) {
            showLoading();
            RenewalsManager.getRenewalsManager().renewalsDel((BaseActivity) getActivity(), renewalsBean.news_id, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    dialog.dismiss();
                    //删除成功，重新啊加载稿件列表数据
                    initRenewalsData();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(getContext(),msg);
                }
            });
        }
    };

    public void refreshDraft(){
        if (RenewalsManager.getRenewalsManager().getOperateState()){
            initRenewalsData();
        }
    }
}
