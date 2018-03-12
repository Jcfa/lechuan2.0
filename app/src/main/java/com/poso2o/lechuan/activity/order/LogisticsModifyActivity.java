package com.poso2o.lechuan.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderDeliveryDTO;
import com.poso2o.lechuan.bean.system.ExpressDTO;
import com.poso2o.lechuan.dialog.OrderDeliveryModeDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

/**
 * 修改物流页面
 */
public class LogisticsModifyActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;
    private static final int REQUEST_CODE = 11;
    private static final int LOGISTICS_REQUEST_CODE = 22;
    //订单数据
    private OrderDTO mOrderDTO;
    //选中的物流公司
    private ExpressDTO choiceExpressDTO = new ExpressDTO();
    //返回
    private ImageView logistics_modify_back,logistics_modify_number_iv;
    private TextView logistics_modify_confirm_btn,logistics_modify_name,logistics_modify_address,logistics_modify_order_id,logistics_modify_company;
    private EditText logistics_modify_number_et;
    private LinearLayout order_modify_mode_layout,logistics_modify_company_layout,logistics_modify_number_layout;
    private TextView logistics_modify_mode;
    //发货方式窗口
    private OrderDeliveryModeDialog mOrderDeliveryModeDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_logistics_modify;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        logistics_modify_back = (ImageView) findViewById(R.id.logistics_modify_back);
        logistics_modify_number_iv = (ImageView) findViewById(R.id.logistics_modify_number_iv);

        logistics_modify_confirm_btn = (TextView) findViewById(R.id.logistics_modify_confirm_btn);
        logistics_modify_order_id = (TextView) findViewById(R.id.logistics_modify_order_id);
        logistics_modify_name = (TextView) findViewById(R.id.logistics_modify_name);
        logistics_modify_address = (TextView) findViewById(R.id.logistics_modify_address);
        logistics_modify_company = (TextView) findViewById(R.id.logistics_modify_company);
        logistics_modify_number_et = (EditText) findViewById(R.id.logistics_modify_number_et);

        order_modify_mode_layout = (LinearLayout) findViewById(R.id.order_modify_mode_layout);
        logistics_modify_company_layout = (LinearLayout) findViewById(R.id.logistics_modify_company_layout);
        logistics_modify_mode = (TextView) findViewById(R.id.logistics_modify_mode);
        logistics_modify_number_layout = (LinearLayout) findViewById(R.id.logistics_modify_number_layout);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        glideRequest = Glide.with(activity);
        //发货方式窗口
        mOrderDeliveryModeDialog = new OrderDeliveryModeDialog(activity);

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO)getIntent().getSerializableExtra("orderDTOs");

        choiceExpressDTO.express_company_id = mOrderDTO.express_company_id;
        choiceExpressDTO.express_company = mOrderDTO.express_company;
        choiceExpressDTO.express_order_id = mOrderDTO.express_order_id;

        logistics_modify_order_id.setText("" + mOrderDTO.order_id);
        logistics_modify_name.setText("" + mOrderDTO.receipt_name + " " + mOrderDTO.receipt_mobile);
        logistics_modify_address.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name);

        logistics_modify_company.setText("" +mOrderDTO.express_company);
        if (mOrderDTO.express_company.isEmpty()){
            logistics_modify_mode.setText("无需物流");
            logistics_modify_company_layout.setVisibility(View.GONE);
            logistics_modify_number_layout.setVisibility(View.GONE);
        }else{
            logistics_modify_mode.setText("物流");
        }
        logistics_modify_number_et.setText("" +mOrderDTO.express_order_id);
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        logistics_modify_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //发货方式
        order_modify_mode_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderDeliveryModeDialog != null){
                    mOrderDeliveryModeDialog.show(new OrderDeliveryModeDialog.OnOrderDeliveryModeListener() {
                        @Override
                        public void onConfirm(int type) {
                            if (type == 0){
                                logistics_modify_company_layout.setVisibility(View.VISIBLE);
                                logistics_modify_number_layout.setVisibility(View.VISIBLE);
                                logistics_modify_mode.setText("物流");
                            }else {
                                logistics_modify_company_layout.setVisibility(View.GONE);
                                logistics_modify_number_layout.setVisibility(View.GONE);
                                logistics_modify_mode.setText("无需物流");
                            }
                        }
                    });
                }
            }
        });
        //物流公司
        logistics_modify_company_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("default","0");
                startActivityForResult(new Intent(activity, LogisticsCompanyActivity.class).putExtras(bundle),LOGISTICS_REQUEST_CODE);
            }
        });
        //扫码
        logistics_modify_number_iv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //确定
        logistics_modify_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                editExpress();
            }
        });
    }

    /**
     * 修改物流信息
     */
    public void editExpress(){

        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        final String express_order_id = logistics_modify_number_et.getText().toString();
        String has_express = logistics_modify_mode.getText().toString();
        if (has_express.equals("物流")){//是否需要发物流  1=需要,0=不需要
            has_express ="1";
            if (express_order_id.isEmpty()){
                Toast.show(activity,"请输入运单号");
                logistics_modify_number_et.requestFocus();//获取焦点 光标出现
                KeyboardUtils.showSoftInput(logistics_modify_number_et);
                return;
            }
        }else{
            has_express ="0";
        }

        if (choiceExpressDTO.express_company_id.isEmpty()){
            Toast.show(activity,"请选择物流公司");
            return;
        }

        showLoading();
        mOrderDataManager.editExpress(activity, shop_id, "" + mOrderDTO.order_id, has_express, choiceExpressDTO.express_company_id, express_order_id, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "修改物流成功！");
                dismissLoading();
                //添加给第一个Activity的返回值，并设置resultCode
                Intent intent = new Intent();
                intent.putExtra("express_company_id", "" + choiceExpressDTO.express_company_id);
                intent.putExtra("express_company", "" + choiceExpressDTO.express_company);
                intent.putExtra("express_order_id", "" + express_order_id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 扫码
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            logistics_modify_number_et.setText(TextUtil.trimToEmpty(result));
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(activity, "扫描二维码失败");
                        }
                    }
                    break;
                case LOGISTICS_REQUEST_CODE:
                    // 选择物流公司
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        choiceExpressDTO.express_company_id = data.getStringExtra("express_company_id");
                        choiceExpressDTO.express_company = data.getStringExtra("express_company");
                        logistics_modify_company.setText(choiceExpressDTO.express_company);
                    }
                    break;
            }
        }
    }

}
