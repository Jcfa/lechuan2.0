package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.StaffListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.staff.AllStaffData;
import com.poso2o.lechuan.bean.staff.StaffData;
import com.poso2o.lechuan.dialog.DelTipsDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/8/7.
 * 员工页
 */
public class MenuStaffActivity extends BaseActivity implements View.OnClickListener,StaffListAdapter.OnStaffClickListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    //返回
    private ImageView r_staff_back;
    //下拉刷新
    private SwipeRefreshLayout menu_staff_swipe;
    //员工列表
    private RecyclerView menu_staff_recycler;
    //编辑员工
    private RelativeLayout menu_staff_edit_staff;
    //添加员工
    private RelativeLayout menu_staff_add_staff;

    //员工列表适配器
    private StaffListAdapter listAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_menu_staff;
    }

    @Override
    public void initView() {

        context = this;

        r_staff_back = (ImageView) findViewById(R.id.r_staff_back);

        menu_staff_swipe = (SwipeRefreshLayout) findViewById(R.id.menu_staff_swipe);

        menu_staff_recycler = (RecyclerView) findViewById(R.id.menu_staff_recycler);

        menu_staff_edit_staff = (RelativeLayout) findViewById(R.id.menu_staff_edit_staff);

        menu_staff_add_staff = (RelativeLayout) findViewById(R.id.menu_staff_add_staff);

        menu_staff_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        initAdapter();
        requestStaffList();
    }

    @Override
    public void initListener() {
        r_staff_back.setOnClickListener(this);
        menu_staff_edit_staff.setOnClickListener(this);
        menu_staff_add_staff.setOnClickListener(this);
        menu_staff_add_staff.setOnClickListener(this);
        menu_staff_swipe.setOnRefreshListener(this);
        listAdapter.setOnStaffClickListener(this);
    }

    private void initAdapter(){
        listAdapter = new StaffListAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        menu_staff_recycler.setLayoutManager(linearLayoutManager);
        menu_staff_recycler.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_staff_back:
                finish();
                break;
            case R.id.menu_staff_edit_staff:
                gotoPositionList();
                break;
            case R.id.menu_staff_add_staff:
                toAddStaff();
                break;
        }
    }

    @Override
    public void onStaffClick(StaffData staffData) {
        if (staffData.positionid.equals("")){
            Toast.show(context,"抱歉，主账号不可编辑");
            return;
        }
        toEditStaff(staffData);
    }

    @Override
    public void onStaffLongClick(StaffData staffData) {
        //删除员工
        toDelStaff(staffData);
    }

    @Override
    public void onRefresh() {
        requestStaffList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditStaffActivity.ADD_STAFF_CODE && resultCode == RESULT_OK){
            requestStaffList();
        }else if (requestCode == EditStaffActivity.EDIT_STAFF_CODE && resultCode == RESULT_OK){
            requestStaffList();
        }
    }

    private void gotoPositionList(){
        Intent intent = new Intent();
        intent.setClass(context,PositionActivity.class);
        startActivity(intent);
    }

    private void toAddStaff(){
        Intent intent = new Intent();
        intent.setClass(context,EditStaffActivity.class);
        intent.putExtra(EditStaffActivity.VIEW_STAFF_TYPE,0);
        startActivityForResult(intent,EditStaffActivity.ADD_STAFF_CODE);
    }

    private void toEditStaff(StaffData staffData){
        Intent intent = new Intent();
        intent.setClass(context,EditStaffActivity.class);
        intent.putExtra(EditStaffActivity.VIEW_STAFF_TYPE,1);
        intent.putExtra(EditStaffActivity.STAFF_DATA,staffData);
        startActivityForResult(intent,EditStaffActivity.EDIT_STAFF_CODE);
    }

    private void toDelStaff(final StaffData staffData){
        if (staffData.positionid.equals("")){
            Toast.show(context,"抱歉，主账号不可删除");
            return;
        }
        final DelTipsDialog delTipsDialog = new DelTipsDialog(context);
        delTipsDialog.show();
        delTipsDialog.setTips("确定要删除员工 " + staffData.realname + " 吗");
        delTipsDialog.setOnDelListener(new DelTipsDialog.OnDelListener() {
            @Override
            public void onDelClick() {
                RStaffManager.getRStaffManager().rStaffDel(MenuStaffActivity.this,staffData.czy, new IRequestCallBack() {
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
                        requestStaffList();
                    }
                });
            }
        });
    }

    private void requestStaffList(){
        if (!menu_staff_swipe.isRefreshing())menu_staff_swipe.setRefreshing(true);
        RStaffManager.getRStaffManager().rStaffList(this, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context,msg);
                menu_staff_swipe.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, Object object) {
                AllStaffData allStaffData = (AllStaffData) object;
                if (listAdapter != null && allStaffData != null){
                    listAdapter.notifyAdapter(allStaffData.list);
                }
                menu_staff_swipe.setRefreshing(false);
            }
        });
    }
}
