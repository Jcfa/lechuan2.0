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
import com.poso2o.lechuan.activity.official.ModelGroupInfoActivity;
import com.poso2o.lechuan.activity.official.ModelServiceActivity;
import com.poso2o.lechuan.adapter.OaSetModelAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.ModelGroupManager;

/**
 * Created by mr zhang on 2018/2/5.
 *
 * 设置模板页
 */

public class OaSetModelFragment extends BaseFragment {

    private View view;

    //全部模板
    private TextView oa_set_all_model;
    //已购买
    private TextView oa_set_model_mine;

    //下拉刷新
    private SwipeRefreshLayout oa_set_model_swipe;
    //模板列表
    private RecyclerView oa_set_model;
    //没有模板提示
    private TextView no_model_tips;

    //全部列表
    private OaSetModelAdapter allAdapter;
    private TemplateGroups allTemplateGroup;

    //已购买列表
    private OaSetModelAdapter boughtAdapter;
    private TemplateGroups boughtTemplateGroup;

    //当前页面类型
    private int viewType = 1;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.fragment_oa_set_model,null);
        return view;
    }

    @Override
    public void initView() {
        oa_set_all_model = (TextView) view.findViewById(R.id.oa_set_all_model);
        oa_set_model_mine = (TextView) view.findViewById(R.id.oa_set_model_mine);

        oa_set_model_swipe = (SwipeRefreshLayout) view.findViewById(R.id.oa_set_model_swipe);
        oa_set_model = (RecyclerView) view.findViewById(R.id.oa_set_model);
        no_model_tips = (TextView) view.findViewById(R.id.no_model_tips);

        oa_set_model_swipe.setColorSchemeColors(0xFFFF0000);

        if (viewType == 1){
            oa_set_model_swipe.setRefreshing(false);
            oa_set_all_model.setSelected(true);
            oa_set_model_mine.setSelected(false);
        }else {
            oa_set_model_swipe.setRefreshing(false);
            oa_set_all_model.setSelected(false);
            oa_set_model_mine.setSelected(true);
        }
    }

    @Override
    public void initData() {
        allAdapter = new OaSetModelAdapter(context,1);
        boughtAdapter = new OaSetModelAdapter(context,2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        oa_set_model.setLayoutManager(layoutManager);

        oa_set_model.setAdapter(allAdapter);

        showLoading();
        requestData();
    }

    @Override
    public void initListener() {
        oa_set_all_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewType == 2){
                    viewType = 1;
                    oa_set_model_swipe.setRefreshing(false);
                    oa_set_all_model.setSelected(true);
                    oa_set_model_mine.setSelected(false);
                    oa_set_model.setAdapter(allAdapter);
                    if (allTemplateGroup == null || allTemplateGroup.list.size() == 0){
                        oa_set_model_swipe.setRefreshing(true);
                        modelGroups();
                    }else {
                        setAdapter(1,allTemplateGroup);
                    }
                }
            }
        });

        oa_set_model_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewType == 1){
                    viewType = 2;
                    oa_set_model_swipe.setRefreshing(false);
                    oa_set_all_model.setSelected(false);
                    oa_set_model_mine.setSelected(true);
                    oa_set_model.setAdapter(boughtAdapter);
                    if (boughtTemplateGroup == null || boughtTemplateGroup.list.size() == 0){
                        oa_set_model_swipe.setRefreshing(true);
                        modelGroupBought();
                    }else {
                        setAdapter(2,boughtTemplateGroup);
                    }
                }
            }
        });

        oa_set_model_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (viewType == 1){
                    modelGroups();
                }else if (viewType == 2){
                    modelGroupBought();
                }
            }
        });

        allAdapter.setOnModelGroupListener(new OaSetModelAdapter.OnModelGroupListener() {
            @Override
            public void onModelGroupClick(TemplateGroup templateGroup) {
                Intent intent = new Intent();
                intent.setClass(context, ModelGroupInfoActivity.class);
                intent.putExtra(ModelGroupInfoActivity.TEMPLATE_GROUP_DATA,templateGroup);
                startActivity(intent);
            }

            @Override
            public void onModelGroupBuy(TemplateGroup templateGroup) {
                Intent intent = new Intent();
                intent.setClass(context, ModelServiceActivity.class);
                intent.putExtra(Constant.DATA,templateGroup);
                getActivity().startActivityForResult(intent,8002);
            }

            @Override
            public void onModelGroupContinue(TemplateGroup templateGroup) {
            }
        });

        boughtAdapter.setOnModelGroupListener(new OaSetModelAdapter.OnModelGroupListener() {
            @Override
            public void onModelGroupClick(TemplateGroup templateGroup) {
                Intent intent = new Intent();
                intent.setClass(context, ModelGroupInfoActivity.class);
                intent.putExtra(ModelGroupInfoActivity.TEMPLATE_GROUP_DATA,templateGroup);
                startActivity(intent);
            }

            @Override
            public void onModelGroupBuy(TemplateGroup templateGroup) {
            }

            @Override
            public void onModelGroupContinue(TemplateGroup templateGroup) {
                Intent intent = new Intent();
                intent.setClass(context, ModelServiceActivity.class);
                intent.putExtra(Constant.DATA,templateGroup);
                getActivity().startActivityForResult(intent,8002);
            }
        });
    }

    //请求模板组列表数据
    private void modelGroups(){
        ModelGroupManager.getModelGroupManager().modelGroups((BaseActivity) context, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                allTemplateGroup = (TemplateGroups) result;
                oa_set_all_model.setText("全部（" + allTemplateGroup.list.size() + "）");
                setAdapter(1,allTemplateGroup);
            }

            @Override
            public void onFailed(int tag, String msg) {
                if (viewType != 1)return;
                dismissLoading();
                oa_set_model_swipe.setRefreshing(false);
            }
        });
    }

    //请求已购买模板组数据
    private void modelGroupBought(){
        ModelGroupManager.getModelGroupManager().modelGroupBought((BaseActivity) context, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                boughtTemplateGroup = (TemplateGroups) result;
                oa_set_model_mine.setText("已购买（" + boughtTemplateGroup.list.size() + "）");
                setAdapter(2,boughtTemplateGroup);
            }

            @Override
            public void onFailed(int tag, String msg) {
                if (viewType != 2)return;
                dismissLoading();
                oa_set_model_swipe.setRefreshing(false);
            }
        });
    }

    //设置数据
    private void setAdapter(int type,TemplateGroups groups){
        if (viewType != type)return;
        dismissLoading();
        oa_set_model_swipe.setRefreshing(false);
        if (viewType == 1){
            oa_set_model.setAdapter(allAdapter);
            allAdapter.notifyData(groups.list);
            if (groups.list.size() == 0){
                no_model_tips.setVisibility(View.VISIBLE);
                no_model_tips.setText("没有任何模板");
            }else {
                no_model_tips.setVisibility(View.GONE);
            }
        }else if (viewType == 2){
            oa_set_model.setAdapter(boughtAdapter);
            boughtAdapter.notifyData(groups.list);
            if (groups.list.size() == 0){
                no_model_tips.setVisibility(View.VISIBLE);
                no_model_tips.setText("您暂时没购买过任何模板\n请前往购买");
            }else {
                no_model_tips.setVisibility(View.GONE);
            }
        }
    }

    public void requestData(){
        modelGroups();
        modelGroupBought();
    }
}
