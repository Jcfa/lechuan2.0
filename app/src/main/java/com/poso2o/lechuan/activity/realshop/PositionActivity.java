package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.wshop.PositionPermissionEditActivity;
import com.poso2o.lechuan.adapter.PositionListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.powerdata.PowerAllData;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.dialog.DelTipsDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/8/8.
 * 权限管理页
 */

public class PositionActivity extends BaseActivity implements View.OnClickListener,PositionListAdapter.OnPositionClickListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    //类型为0时，该页面可以添加编辑职位；类型为1时，该页面只可以添加和选择职位
    public static final String VIEW_POSITION_TYPE = "position_type";
    //添加职位请求码
    public static final int ADD_POSITION_PERMISSION_CODE = 10001;
    //编辑职位权限请求码
    public static final int EDIT_POSITION_PERMISSION_CODE = 10010;
    private int viewType;

    //返回
    private ImageView activity_position_back;
    //下拉刷新
    private SwipeRefreshLayout position_swipe_refresh;
    //权限列表
    private RecyclerView position_recycler;
    //添加职位
    private ImageView position_add;

    //列表适配器
    private PositionListAdapter listAdapter ;

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_position;
    }

    @Override
    public void initView() {

        context = this;

        activity_position_back = (ImageView) findViewById(R.id.activity_position_back);

        position_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.position_swipe_refresh);

        position_recycler = (RecyclerView) findViewById(R.id.position_recycler);

        position_add = (ImageView) findViewById(R.id.position_add);
    }

    @Override
    public void initData() {

        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        switchView();
        initAdapter();
        requestPowerData();
    }

    @Override
    public void initListener() {
        position_swipe_refresh.setOnRefreshListener(this);
        activity_position_back.setOnClickListener(this);
        position_add.setOnClickListener(this);
        listAdapter.setOnPositionClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_position_back:
                finish();
                break;
            case R.id.position_add:
                toAddPosition();
                break;
        }
    }
    
    @Override
    public void onRefresh() {
        requestPowerData();
    }

    @Override
    public void onPositionListener(PowerData powerData) {
        if (viewType == 1){
            Intent intent = new Intent();
            intent.putExtra(EditStaffActivity.POSITION_DATA,powerData);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            if (is_online){
                Intent intent = new Intent();
                intent.setClass(context,PositionPermissionEditActivity.class);
                intent.putExtra(PositionPermissionEditActivity.PERMISSION_VIEW_TYPE,1);
                intent.putExtra(PositionPermissionEditActivity.PERMISSION_VIEW_DATA,powerData);
                startActivityForResult(intent,EDIT_POSITION_PERMISSION_CODE);
            }else {
//                Intent intent = new Intent();
//                intent.setClass(context,RPermissionActivity.class);
//                intent.putExtra(RPermissionActivity.R_PERMISSION_VIEW_TYPE,1);
//                intent.putExtra(RPermissionActivity.R_PERMISSION_VIEW_DATA,powerData);
//                startActivityForResult(intent,EDIT_POSITION_PERMISSION_CODE);
                Intent intent = new Intent();
                intent.setClass(context,EditPositionActivity.class);
                intent.putExtra(EditPositionActivity.E_PERMISSION_VIEW_TYPE,1);
                intent.putExtra(EditPositionActivity.E_PERMISSION_VIEW_DATA,powerData);
                startActivityForResult(intent,EDIT_POSITION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onPositionLongClick(PowerData powerData) {
        //删除弹窗
        delPosition(powerData);
    }

    private void switchView(){
        //bundle为空，则默认为类型0，可以添加编辑职位
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        viewType = (int) bundle.get(VIEW_POSITION_TYPE);
    }

    private void initAdapter(){
        listAdapter = new PositionListAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        position_recycler.setLayoutManager(linearLayoutManager);
        position_recycler.setAdapter(listAdapter);
    }

    private void toAddPosition(){
        if (is_online){
            Intent intent = new Intent();
            intent.setClass(context,PositionPermissionEditActivity.class);
            intent.putExtra(PositionPermissionEditActivity.PERMISSION_VIEW_TYPE,0);
            startActivityForResult(intent,ADD_POSITION_PERMISSION_CODE);
        }else {
            Intent intent = new Intent();
            intent.setClass(context,EditPositionActivity.class);
            intent.putExtra(EditPositionActivity.E_PERMISSION_VIEW_TYPE,0);
            startActivityForResult(intent,ADD_POSITION_PERMISSION_CODE);
        }
    }

    private void requestPowerData(){
        if (!position_swipe_refresh.isRefreshing())position_swipe_refresh.setRefreshing(true);
        RStaffManager.getRStaffManager().rPositionList(this,new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                position_swipe_refresh.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                position_swipe_refresh.setRefreshing(false);
                PowerAllData powerAllData = (PowerAllData) object;
                if (listAdapter != null && powerAllData != null)
                    listAdapter.notifyAdapter(powerAllData.list);
            }
        });
    }

    //删除职务
    private void delPosition(final PowerData powerData){
        final DelTipsDialog delTipsDialog = new DelTipsDialog(this);
        delTipsDialog.show();
        delTipsDialog.setTips("确定删除职位 " + powerData.positionname + " 吗？");
        delTipsDialog.setOnDelListener(new DelTipsDialog.OnDelListener() {
            @Override
            public void onDelClick() {
                showLoading("正在删除职位...");
                RStaffManager.getRStaffManager().rPositionDel(PositionActivity.this, powerData.positionid, new IRequestCallBack() {
                    @Override
                    public void onFailed(int tag, String msg) {
                        dismissLoading();
                        Toast.show(context,msg);
                    }

                    @Override
                    public void onResult(int tag, Object object) {
                        dismissLoading();
                        delTipsDialog.dismiss();
                        Toast.show(context,"删除成功");
                        requestPowerData();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_POSITION_PERMISSION_CODE && resultCode == RESULT_OK){
            requestPowerData();
        }else if (requestCode == EDIT_POSITION_PERMISSION_CODE && resultCode == RESULT_OK){
            requestPowerData();
        }
    }
}
