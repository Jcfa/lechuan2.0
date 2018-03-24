package com.poso2o.lechuan.activity.official;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.FreeEditActivity;
import com.poso2o.lechuan.adapter.ItemOaGroupModelAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.ModelGroupManager;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2018/2/5.
 *
 * 模板组详情（新品推荐）
 */

public class ModelGroupInfoActivity extends BaseActivity {

    //模板详情跳转
    private static final int CODE_TEMPLATE_DETAIL = 1510;
    public static final String TEMPLATE_GROUP_DATA = "template_group";
    //是否为选择模板页面
    public static final String TAG_CHANGE_TEMPLATE = "change_template";

    //返回
    private ImageView model_groups_back;
    //模板组名称
    private TextView template_group_name;
    //列表
    private RecyclerView set_model_group_detail_list;

    private ItemOaGroupModelAdapter modelAdapter;

    private TemplateGroup templateGroup;
    //是否为选择模板页面
    private boolean select_template = false;

    //默认模板变更过
    private boolean is_change = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_group_detail;
    }

    @Override
    protected void initView() {
        model_groups_back = (ImageView) findViewById(R.id.model_groups_back);
        template_group_name = (TextView) findViewById(R.id.template_group_name);
        set_model_group_detail_list = (RecyclerView) findViewById(R.id.set_model_group_detail_list);
    }

    @Override
    protected void initData() {
        modelAdapter = new ItemOaGroupModelAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        set_model_group_detail_list.setLayoutManager(gridLayoutManager);
        set_model_group_detail_list.setAdapter(modelAdapter);
        setGroupData();
    }

    @Override
    protected void initListener() {
        //返回
        model_groups_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_change)setResult(RESULT_OK);
                finish();
            }
        });
        //模板点击
        modelAdapter.setOnModelListener(new ItemOaGroupModelAdapter.OnModelListener() {
            @Override
            public void onModelClick(TemplateBean templateBean) {
                goToDetail(templateBean);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CODE_TEMPLATE_DETAIL){
                is_change = true;
//                refreshGroup();
            }
        }
    }

    private void setGroupData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        templateGroup = (TemplateGroup) bundle.get(TEMPLATE_GROUP_DATA);
        select_template = bundle.getBoolean(TAG_CHANGE_TEMPLATE,false);
        if (templateGroup == null)return;
        template_group_name.setText(templateGroup.group_name);
        modelAdapter.notifyData(templateGroup.templates);
    }

    //模板详情跳转
    private void goToDetail(TemplateBean templateBean){
        if (select_template){
            Intent intent = new Intent();
            intent.putExtra(FreeEditActivity.DATA_TEMPLATE,templateBean);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            Intent intent = new Intent();
            intent.setClass(this,ModelEditActivity.class);
            intent.putExtra(ModelEditActivity.TEMPLATE_INFO,templateBean);
            intent.putExtra(ModelEditActivity.TEMPLATE_GROUP_ID,templateGroup.group_id);
            startActivityForResult(intent,CODE_TEMPLATE_DETAIL);
        }
    }

    private void refreshGroup(){
        showLoading();
        ModelGroupManager.getModelGroupManager().modelGroupInfo(this, templateGroup.group_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                templateGroup = (TemplateGroup) result;
                if (templateGroup != null){
                    modelAdapter.notifyData(templateGroup.templates);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }
}
