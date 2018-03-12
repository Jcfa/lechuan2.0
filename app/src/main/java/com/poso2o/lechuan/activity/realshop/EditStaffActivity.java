package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.bean.staff.StaffData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RStaffManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/8/8.
 * 编辑员工页(添加员工页)
 */

public class EditStaffActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //页面类型 ,0表示添加员工，1表示编辑员工
    public static final String VIEW_STAFF_TYPE = "staff_type";
    //员工数据
    public static final String STAFF_DATA = "staff_data";
    //添加跳转请求码
    public static final int ADD_STAFF_CODE = 10100;
    //编辑跳转请求码
    public static final int EDIT_STAFF_CODE = 10011;
    //职位选择数据
    public static final String POSITION_DATA = "position_data";
    //职位选择请求码
    public static final int POSITION_SELECT_CODE = 10101;

    //返回
    private ImageView edit_staff_back;
    //确定
    private Button edit_staff_confirm;
    //标题
    private TextView edit_staff_title;
    //工号
    private EditText edit_staff_no;
    //编辑员工情况下显示工号，不可编辑
    private TextView text_staff_no;
    //姓名
    private EditText edit_staff_name;
    //职位选择
    private LinearLayout edit_staff_select_position;
    //职位名称
    private TextView edit_staff_position;
    //开启停用
    private LinearLayout edit_staff_state;
    //开启停用开关
    private CheckBox edit_staff_off_on;
    //手机
    private EditText edit_staff_phone;
    //密码
    private EditText edit_staff_password;

    //界面类型，0表示该界面为添加员工界面，1表示该界面为编辑员工页面
    private int viewType = 0;
    private StaffData staffData;
    //职务,不能为空
    private PowerData powerData;

    //记录要编辑的员工号
    private String mStaffNo = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_staff;
    }

    @Override
    public void initView() {

        context = this;

        edit_staff_back = (ImageView) findViewById(R.id.edit_staff_back);

        edit_staff_confirm = (Button) findViewById(R.id.edit_staff_confirm);

        edit_staff_title = (TextView) findViewById(R.id.edit_staff_title);

        edit_staff_no = (EditText) findViewById(R.id.edit_staff_no);

        text_staff_no = (TextView) findViewById(R.id.text_staff_no);

        edit_staff_name = (EditText) findViewById(R.id.edit_staff_name);

        edit_staff_select_position = (LinearLayout) findViewById(R.id.edit_staff_select_position);

        edit_staff_position = (TextView) findViewById(R.id.edit_staff_position);

        edit_staff_state = (LinearLayout) findViewById(R.id.edit_staff_state);

        edit_staff_off_on = (CheckBox) findViewById(R.id.edit_staff_off_on);

        edit_staff_phone = (EditText) findViewById(R.id.edit_staff_phone);

        edit_staff_password = (EditText) findViewById(R.id.edit_staff_password);
 
    }

    @Override
    public void initData() { 
        initType();
    }

    @Override
    public void initListener() {
        edit_staff_back.setOnClickListener(this);
        edit_staff_confirm.setOnClickListener(this);
        edit_staff_select_position.setOnClickListener(this);
        edit_staff_state.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_staff_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.edit_staff_confirm:
                commitStaff();
                break;
            case R.id.edit_staff_select_position:
                toPositionList();
                break;
            case R.id.edit_staff_state:
                toggleState();
                break;
        }
    }

    private void toPositionList(){
        Intent intent = new Intent();
        intent.setClass(context,PositionActivity.class);
        intent.putExtra(PositionActivity.VIEW_POSITION_TYPE,1);
        startActivityForResult(intent,POSITION_SELECT_CODE);
    }

    private void initType(){
        Bundle bundle = getIntent().getExtras();
        viewType = (int) bundle.get(VIEW_STAFF_TYPE);
        if (viewType == 0){
            edit_staff_title.setText("添加员工");
            edit_staff_off_on.setChecked(true);
            edit_staff_no.setVisibility(View.VISIBLE);
            text_staff_no.setVisibility(View.GONE);
        }else if (viewType == 1){
            edit_staff_title.setText("编辑员工");
            edit_staff_no.setVisibility(View.GONE);
            text_staff_no.setVisibility(View.VISIBLE);
            staffData = (StaffData) bundle.get(STAFF_DATA);
            if (staffData == null)return;
            mStaffNo = staffData.czy;
            //旧版数据
            text_staff_no.setText(staffData.czy);
            edit_staff_name.setText(staffData.realname);
            if (staffData.realname.length() > 0)edit_staff_name.setSelection(staffData.realname.length());
            edit_staff_position.setText(staffData.positionname);
            edit_staff_off_on.setChecked(true);
            edit_staff_phone.setText(staffData.mobile);

            edit_staff_password.setHint("不输入则默认使用原来密码");
            powerData = new PowerData();
            powerData.positionid = staffData.positionid;
            powerData.positionname = staffData.positionname;
        }
    }

    private void commitStaff(){

        StaffData staff = new StaffData();
        //处理旧版数据
        staff.realname = edit_staff_name.getText().toString();
        staff.password = edit_staff_password.getText().toString();
        staff.mobile = edit_staff_phone.getText().toString();

        /*员工编号已改为不可改，不需要这一步判断
        if (TextUtil.isEmpty(staff.czy)){
            Toast.show(context,"请输入员工工号");
            edit_staff_no.requestFocus();
            return;
        }*/
        if (TextUtil.isEmpty(staff.realname)){
            Toast.show(context,"请输入员工姓名");
            edit_staff_name.requestFocus();
            return;
        }
        if (powerData == null){
            Toast.show(context,"请选择职务");
            return;
        }
        staff.positionid = powerData.positionid;
        staff.positionname = powerData.positionname;
        if (viewType == 0){
            //添加员工
            staff.czy = edit_staff_no.getText().toString();
            if (TextUtil.isEmpty(staff.czy)){
                Toast.show(context,"请输入员工工号");
                edit_staff_no.requestFocus();
                return;
            }
            if (TextUtil.isEmpty(staff.password)){
                Toast.show(context,"请输入密码");
                edit_staff_password.requestFocus();
                return;
            }
            addStaff(staff);
        }else if (viewType == 1){
            //编辑员工
            staff.czy = staffData.czy;
            if (!staff.czy.equals(mStaffNo)){
                //员工号改变了
                staff.edit_no = true;
            }
            if (TextUtil.isEmpty(staff.password)){
                staff.password = staffData.password;
            }
            editStaff(staff);
        }
    }

    private void addStaff(StaffData staffData){
        showLoading("正在提交数据...");
        RStaffManager.getRStaffManager().rStaffAdd(this, staffData, new IRequestCallBack() {
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

    private void editStaff(StaffData staffData){
        showLoading("正在提交数据...");
        RStaffManager.getRStaffManager().rStaffEdit(this, staffData, new IRequestCallBack() {
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

    private void toggleState(){
        if (edit_staff_off_on.isChecked()){
            edit_staff_off_on.setChecked(false);
        }else {
            edit_staff_off_on.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POSITION_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getExtras() != null){
            powerData = (PowerData) data.getExtras().get(POSITION_DATA);
            if (powerData != null) edit_staff_position.setText(powerData.positionname);
        }
    }
}
