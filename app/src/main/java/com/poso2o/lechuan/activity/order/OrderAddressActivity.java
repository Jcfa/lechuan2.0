package com.poso2o.lechuan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * 修改订单地址页面
 */
public class OrderAddressActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;
    //订单数据
    private OrderDTO mOrderDTO;
    //返回
    private ImageView order_address_back;
    private TextView order_address_confirm_btn,order_address_address, order_address_province_et;
    private EditText order_address_name_et, order_address_mobile_et, order_address_tel_et, order_address_other_et, order_address_code_et;
    private LinearLayout order_address_layout;
    private static final int RESULT_SELECT_ID = 1;//选择省市区requestCode

    private String provinceId = "";
    private String provinceName = "";
    private String cityId = "";
    private String cityName = "";
    private String areaId = "";
    private String areaName = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_address;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_address_back = (ImageView) findViewById(R.id.order_address_back);

        order_address_confirm_btn = (TextView) findViewById(R.id.order_address_confirm_btn);
        order_address_address = (TextView) findViewById(R.id.order_address_address);
        order_address_province_et = (TextView) findViewById(R.id.order_address_province_et);
        order_address_name_et = (EditText) findViewById(R.id.order_address_name_et);
        order_address_mobile_et = (EditText) findViewById(R.id.order_address_mobile_et);
        order_address_tel_et = (EditText) findViewById(R.id.order_address_tel_et);
        order_address_other_et = (EditText) findViewById(R.id.order_address_other_et);
        order_address_code_et = (EditText) findViewById(R.id.order_address_code_et);
        order_address_layout = (LinearLayout) findViewById(R.id.order_address_layout);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        glideRequest = Glide.with(activity);

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("orderDTOs");

        order_address_address.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name + mOrderDTO.receipt_address);
        order_address_province_et.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name);
        order_address_name_et.setText("" + mOrderDTO.receipt_name);
        order_address_mobile_et.setText("" + mOrderDTO.receipt_mobile);
        order_address_tel_et.setText("" + mOrderDTO.receipt_tel);
        order_address_other_et.setText("" + mOrderDTO.receipt_address);
        order_address_code_et.setText("" + mOrderDTO.receipt_zipcode);

        provinceName = mOrderDTO.receipt_province_name;
        cityName = mOrderDTO.receipt_city_name;
        areaName = mOrderDTO.receipt_area_name;

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_address_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //选择省市区
        order_address_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivityForResult(AddressSelectActivity.class, RESULT_SELECT_ID);
            }
        });
        //确定
        order_address_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                editReceiptAddress();
            }
        });
    }

    /**
     * 修改收货信息
     */
    private void editReceiptAddress(){

        /** @param baseActivity
                * @param shop_id                     商家店铺ID
                * @param order_id                    订单号（必填）
        * @param receipt_name			    收货姓名
        * @param receipt_province_name		收货地址-省份名称
                * @param receipt_city_name			    收货地址-市名称
                * @param receipt_area_name			收货地址-区名称
                * @param receipt_address			    收货地址-地址
                * @param receipt_mobile			    联系手机*/

        String receipt_name = order_address_name_et.getText().toString();
        String receipt_mobile = order_address_mobile_et.getText().toString();
        String receipt_tel = order_address_tel_et.getText().toString();
        final String receipt_address = order_address_other_et.getText().toString();
        String receipt_zipcode = order_address_code_et.getText().toString();

        if (receipt_name.isEmpty()){
            Toast.show(activity,"请输入收货人。");
            order_address_name_et.requestFocus();//获取焦点 光标出现
            KeyboardUtils.showSoftInput(order_address_name_et);
            return;
        }
        if (receipt_mobile.isEmpty() && receipt_tel.isEmpty()){
            Toast.show(activity,"请输入手机。");
            order_address_mobile_et.requestFocus();//获取焦点 光标出现
            KeyboardUtils.showSoftInput(order_address_mobile_et);
            return;
        }
        if (areaName.isEmpty()){
            Toast.show(activity,"请选择省市区。");
            return;
        }
        if (receipt_address.isEmpty()){
            Toast.show(activity,"请输入街道地址。");
            order_address_other_et.requestFocus();//获取焦点 光标出现
            KeyboardUtils.showSoftInput(order_address_other_et);
            return;
        }

        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        showLoading();
        mOrderDataManager.editReceiptAddress(activity, shop_id, "" + mOrderDTO.order_id, receipt_name, provinceName, cityName, areaName,
                receipt_mobile, receipt_tel, receipt_zipcode, receipt_address, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "修改地址成功");
                dismissLoading();
                //添加给第一个Activity的返回值，并设置resultCode
                Intent intent = new Intent();
                intent.putExtra("provinceName", "" + provinceName);
                intent.putExtra("cityName", "" + cityName);
                intent.putExtra("areaName", "" + areaName);
                intent.putExtra("address", "" + receipt_address);
                setResult(RESULT_OK, intent);
                finish();
            }
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SELECT_ID && resultCode == RESULT_OK) {//选择省市区返回结果
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                provinceId = bundle.getString(AddressSelectActivity.KEY_PROVINCE_ID);
                provinceName = bundle.getString(AddressSelectActivity.KEY_PROVINCE_NAME);
                cityId = bundle.getString(AddressSelectActivity.KEY_CITY_ID);
                cityName = bundle.getString(AddressSelectActivity.KEY_CITY_NAME);
                areaId = bundle.getString(AddressSelectActivity.KEY_AREA_ID);
                areaName = bundle.getString(AddressSelectActivity.KEY_AREA_NAME);
                Print.println("onActivityResult=" + provinceId + "," + cityId + "," + areaId + ";地址：" + provinceName + cityName + areaName);
                order_address_province_et.setText(provinceName + cityName + areaName);
            }
        }
    }
}
