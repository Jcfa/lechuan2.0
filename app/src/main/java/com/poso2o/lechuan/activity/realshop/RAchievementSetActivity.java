package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.AchieveSetListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.staff.AllStaffData;
import com.poso2o.lechuan.bean.staffreport.AllStaffReportData;
import com.poso2o.lechuan.bean.staffreport.StaffReportData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/4.
 * 业绩设置页
 */

public class RAchievementSetActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回键
    private ImageView achievement_set_back;
    //确认键
    private Button achievement_set_confirm;
    //业绩设置列表
    private RecyclerView achievement_list_recycler;

    //列表适配器
    private AchieveSetListAdapter listAdapter ;

    private AllStaffReportData allStaffReport;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_achievement_setting;
    }

    @Override
    public void initView() {

        context = this;

        achievement_set_back = (ImageView) findViewById(R.id.achievement_set_back);

        achievement_set_confirm = (Button) findViewById(R.id.achievement_set_confirm);

        achievement_list_recycler = (RecyclerView) findViewById(R.id.achievement_list_recycler); 
    }

    @Override
    public void initData() {
        initAdapter();
        requestStaffList();
    }

    @Override
    public void initListener() {
        achievement_set_back.setOnClickListener(this);
        achievement_set_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.achievement_set_back:
                RAchievementSetActivity.this.setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.achievement_set_confirm:
                toBatchSetAssig();
                break;
        }
    }

    private void initAdapter(){
        listAdapter = new AchieveSetListAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        achievement_list_recycler.setLayoutManager(linearLayoutManager);
        achievement_list_recycler.setAdapter(listAdapter);
    }

    private void requestStaffList(){
        showLoading("正在加载数据...");
        RReportManager.getRReportManger().achievemence(this,"-1","-1", new IRequestCallBack() {
            
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                allStaffReport = (AllStaffReportData) object;
                if (listAdapter != null && allStaffReport != null){
                    listAdapter.notifyAdapter(allStaffReport.list);
                }
            }
        });
    }

    private void toBatchSetAssig(){
        showLoading("正在提交数据...");
        RReportManager.getRReportManger().batchSetAchieve(this, new Gson().toJson(listAdapter.getSetDatas()), new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
