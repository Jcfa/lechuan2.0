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
import com.poso2o.lechuan.adapter.ItemOaGroupModelAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * Created by mr zhang on 2018/2/5.
 *
 * 模板组详情（新品推荐）
 */

public class ModelGroupInfoActivity extends BaseActivity {

    public static final String TEMPLATE_GROUP_DATA = "template_group";

    //返回
    private ImageView model_groups_back;
    //模板组名称
    private TextView set_model_group_detail_name;
    //模板组模板数量
    private TextView set_model_group_detail_num;
    //模板组价格
    private TextView set_model_detail_price;
    //购买
    private TextView set_model_detail_buy;
    //续订
    private TextView set_model_detail_continue;
    //列表
    private RecyclerView set_model_group_detail_list;

    private ItemOaGroupModelAdapter modelAdapter;

    private TemplateGroup templateGroup;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_group_detail;
    }

    @Override
    protected void initView() {
        model_groups_back = (ImageView) findViewById(R.id.model_groups_back);
        set_model_group_detail_name = (TextView) findViewById(R.id.set_model_group_detail_name);
        set_model_group_detail_num = (TextView) findViewById(R.id.set_model_group_detail_num);
        set_model_detail_price = (TextView) findViewById(R.id.set_model_detail_price);
        set_model_detail_buy = (TextView) findViewById(R.id.set_model_detail_buy);
        set_model_detail_continue = (TextView) findViewById(R.id.set_model_detail_continue);
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
                finish();
            }
        });
        //购买
        set_model_detail_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ModelGroupInfoActivity.this, ModelServiceActivity.class);
                startActivity(intent);
            }
        });
        //续订
        set_model_detail_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ModelGroupInfoActivity.this, ModelServiceActivity.class);
                intent.putExtra(Constant.DATA,templateGroup);
                startActivity(intent);
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

    private void setGroupData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        templateGroup = (TemplateGroup) bundle.get(TEMPLATE_GROUP_DATA);
        if (templateGroup == null)return;

        set_model_group_detail_name.setText(templateGroup.group_name);
        set_model_group_detail_num.setText(templateGroup.templates.size() + "");
        set_model_detail_price.setText("¥" + NumberFormatUtils.format(templateGroup.amount));
        if (templateGroup.has_buy.equals("1")){
            //已购买
            set_model_detail_buy.setVisibility(View.GONE);
            set_model_detail_continue.setVisibility(View.VISIBLE);
        }else {
            //未购买
            set_model_detail_buy.setVisibility(View.VISIBLE);
            set_model_detail_continue.setVisibility(View.GONE);
        }
        modelAdapter.notifyData(templateGroup.templates);
    }

    //模板详情跳转
    private void goToDetail(TemplateBean templateBean){
        if (templateGroup.has_buy.equals("0")){
            //没购买
            Intent intent = new Intent();
            intent.setClass(this,ModelInfoActivity.class);
            startActivity(intent);
        }else {
            //已购买
            Intent intent = new Intent();
            intent.setClass(this,ModelEditActivity.class);
            startActivity(intent);
        }
    }
}
