package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 订单发货页面
 */
public class OrderDeliveryActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;
    private static final int REQUEST_CODE = 11;
    private static final int LOGISTICS_REQUEST_CODE = 22;
    private final int CAMERA = 1003;
    //订单数据
    private OrderDTO mOrderDTO;
    //选中的物流公司
    private ExpressDTO choiceExpressDTO = new ExpressDTO();
    //返回
    private ImageView order_delivery_back,order_delivery_number_iv;
    private TextView order_delivery_confirm_btn,order_delivery_name,order_delivery_address,order_delivery_order_id,order_delivery_mode,order_delivery_company;
    private EditText order_delivery_number_et;
    private LinearLayout order_delivery_mode_layout,order_delivery_company_layout,order_delivery_number_layout;
    private TextView order_delivery_set_company_layout;
    //发货方式窗口
    private OrderDeliveryModeDialog mOrderDeliveryModeDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_delivery;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_delivery_back = (ImageView) findViewById(R.id.order_delivery_back);
        order_delivery_number_iv = (ImageView) findViewById(R.id.order_delivery_number_iv);

        order_delivery_confirm_btn = (TextView) findViewById(R.id.order_delivery_confirm_btn);
        order_delivery_order_id = (TextView) findViewById(R.id.order_delivery_order_id);
        order_delivery_name = (TextView) findViewById(R.id.order_delivery_name);
        order_delivery_address = (TextView) findViewById(R.id.order_delivery_address);
        order_delivery_mode = (TextView) findViewById(R.id.order_delivery_mode);
        order_delivery_company = (TextView) findViewById(R.id.order_delivery_company);
        order_delivery_number_et = (EditText) findViewById(R.id.order_delivery_number_et);
        order_delivery_mode_layout = (LinearLayout) findViewById(R.id.order_delivery_mode_layout);
        order_delivery_company_layout = (LinearLayout) findViewById(R.id.order_delivery_company_layout);
        order_delivery_set_company_layout = (TextView) findViewById(R.id.order_delivery_set_company_layout);
        order_delivery_number_layout = (LinearLayout) findViewById(R.id.order_delivery_number_layout);
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

        //默认发货方式
        String default_delivery_mode  = SharedPreferencesUtils.getString("default_delivery_mode");
        if (default_delivery_mode.isEmpty()){
            order_delivery_mode.setText("物流");
        }else{
            order_delivery_mode.setText("" + default_delivery_mode);
        }
        if (default_delivery_mode.equals("无需物流")){
            order_delivery_company_layout.setVisibility(View.GONE);
            order_delivery_number_layout.setVisibility(View.GONE);
        }

        //默认物流公司
        String default_express_id = SharedPreferencesUtils.getString("default_express_id");
        String default_express_name  = SharedPreferencesUtils.getString("default_express_name");
        if (default_express_id.isEmpty()){
            default_express_id = "SFEXPRESS";
            default_express_name = "顺丰";
        }
        choiceExpressDTO.express_company_id = default_express_id;
        choiceExpressDTO.express_company = default_express_name;
        order_delivery_company.setText("" + default_express_name);

        order_delivery_order_id.setText("" + mOrderDTO.order_id);
        order_delivery_name.setText("" + mOrderDTO.receipt_name + " " + mOrderDTO.receipt_mobile);
        order_delivery_address.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name + mOrderDTO.receipt_address);

        order_delivery_number_et.setText("" + mOrderDTO.express_order_id);
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_delivery_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //发货
        order_delivery_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                OrderShippingConfrim();
            }
        });
        //发货方式
        order_delivery_mode_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderDeliveryModeDialog != null){
                    mOrderDeliveryModeDialog.show(new OrderDeliveryModeDialog.OnOrderDeliveryModeListener() {
                        @Override
                        public void onConfirm(int type) {
                            if (type == 0){
                                order_delivery_company_layout.setVisibility(View.VISIBLE);
                                order_delivery_number_layout.setVisibility(View.VISIBLE);
                                order_delivery_mode.setText("物流");
                                SharedPreferencesUtils.put("default_delivery_mode", "物流");
                            }else {
                                order_delivery_company_layout.setVisibility(View.GONE);
                                order_delivery_number_layout.setVisibility(View.GONE);
                                order_delivery_mode.setText("无需物流");
                                SharedPreferencesUtils.put("default_delivery_mode", "无需物流");
                            }
                        }
                    });
                }
            }
        });
        //物流公司
        order_delivery_company_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("default","0");
                startActivityForResult(new Intent(activity, LogisticsCompanyActivity.class).putExtras(bundle),LOGISTICS_REQUEST_CODE);

            }
        });
        //扫码
        order_delivery_number_iv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                doCamera();
            }
        });
        //设置默认物流公司
        order_delivery_set_company_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("default","1");
                startActivity(new Intent(activity, LogisticsCompanyActivity.class).putExtras(bundle));
            }
        });
    }

    /**
     * 卖家【发货确认】
     */
    public void OrderShippingConfrim(){

        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        String express_order_id = order_delivery_number_et.getText().toString();
        String has_express = order_delivery_mode.getText().toString();
        if (has_express.equals("物流")){//是否需要发物流  1=需要,0=不需要
            has_express ="1";
            if (express_order_id.isEmpty()){
                Toast.show(activity,"请输入运单号");
                order_delivery_number_et.requestFocus();//获取焦点 光标出现
                KeyboardUtils.showSoftInput(order_delivery_number_et);
                return;
            }
        }else{
            has_express ="0";
        }

        ArrayList<OrderDeliveryDTO> list = new ArrayList<>();
        OrderDeliveryDTO orderDeliveryDTO = new OrderDeliveryDTO();
        orderDeliveryDTO.order_id = mOrderDTO.order_id;
        orderDeliveryDTO.express_order_id = order_delivery_number_et.getText().toString();
        list.add(orderDeliveryDTO);

        Print.println("list:" + new Gson().toJson(list));

        showLoading();
        mOrderDataManager.OrderShippingConfrim(activity, shop_id, has_express, choiceExpressDTO.express_company_id, list, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "发货成功！");
                dismissLoading();
                //发通知让订单页刷新数据
                EventBean bean = new EventBean(EventBean.CODE_V_ORDER_UPDATE);
                EventBus.getDefault().post(bean);
                //添加给第一个Activity的返回值，并设置resultCode
                Intent intent = new Intent();
                intent.putExtra("order_id", "" + orderDTO.order_id);
                intent.putExtra("express_company_id", "" + choiceExpressDTO.express_company_id);
                intent.putExtra("express_company", "" + choiceExpressDTO.express_company);
                intent.putExtra("express_order_id", "" + order_delivery_number_et.getText().toString());
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
                            order_delivery_number_et.setText(TextUtil.trimToEmpty(result));
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(activity, "扫描二维码失败");
                        }
                    }
                    break;
                case LOGISTICS_REQUEST_CODE:
                    // 物流公司
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        choiceExpressDTO.express_company_id = data.getStringExtra("express_company_id");
                        choiceExpressDTO.express_company = data.getStringExtra("express_company");
                        order_delivery_company.setText(choiceExpressDTO.express_company);
                        Print.println("express_company_id:" + choiceExpressDTO.express_company_id);
                        Print.println("express_company:" + choiceExpressDTO.express_company);
                    }
                    break;
            }
        }
    }
    /**
     * 调起系统摄像头功能
     */
    public void doCamera(){
        //系统会弹出需要请求权限的对话框
        if (android.os.Build.VERSION.SDK_INT > 22 && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions(new String[]{Manifest.permission.CAMERA }, CAMERA);
        }else{
            Intent intent = new Intent();
            intent.setClass(activity, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    //接收权限是否请求的请求状态
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Print.println("requestCode:=========================================:"+requestCode);
        switch (requestCode) {
            case CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCamera();
                } else {
                    Toast.show(activity,"没有权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
