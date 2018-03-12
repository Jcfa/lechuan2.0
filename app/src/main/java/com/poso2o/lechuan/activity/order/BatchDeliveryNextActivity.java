package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BatchDeliveryAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderDeliveryDTO;
import com.poso2o.lechuan.bean.order.OrderGoodsDTO;
import com.poso2o.lechuan.bean.order.OrderQueryDTO;
import com.poso2o.lechuan.bean.order.OrderTotalBean;
import com.poso2o.lechuan.bean.system.ExpressDTO;
import com.poso2o.lechuan.dialog.OrderDeliveryModeDialog;
import com.poso2o.lechuan.dialog.OrderExpressIdDialog;
import com.poso2o.lechuan.dialog.OrderModifyPriceDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 批量发货下一步页面
 */
public class BatchDeliveryNextActivity extends BaseActivity {
    private OrderDataManager mOrderDataManager;
    //返回
    private ImageView batch_delivery_next_next_back;
    private TextView batch_delivery_next_confirm_btn,batch_delivery_next_mode,batch_delivery_next_company;
    private LinearLayout batch_delivery_next_item_groups,batch_delivery_next_mode_layout,batch_delivery_next_company_layout;
    //订单数据
    private ArrayList<OrderDTO> mOrderDTO;
    private static final int REQUEST_CODE = 11;
    private static final int LOGISTICS_REQUEST_CODE = 22;
    //选中的物流公司
    private ExpressDTO choiceExpressDTO = new ExpressDTO();
    private OrderDTO currentOrderDTO;
    private TextView barcode_et;
    private final int CAMERA = 1003;
    private boolean isLogistics = true;
    //发货方式窗口
    private OrderDeliveryModeDialog mOrderDeliveryModeDialog;
    //输入快递单号窗口
    private OrderExpressIdDialog mOrderExpressIdDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_batch_delivery_next;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        batch_delivery_next_next_back = (ImageView) findViewById(R.id.batch_delivery_next_back);
        batch_delivery_next_confirm_btn = (TextView) findViewById(R.id.batch_delivery_next_confirm_btn);
        batch_delivery_next_company = (TextView) findViewById(R.id.batch_delivery_next_company);
        batch_delivery_next_mode = (TextView) findViewById(R.id.batch_delivery_next_mode);
        batch_delivery_next_item_groups = (LinearLayout) findViewById(R.id.batch_delivery_next_item_groups);
        batch_delivery_next_mode_layout = (LinearLayout) findViewById(R.id.batch_delivery_next_mode_layout);
        batch_delivery_next_company_layout = (LinearLayout) findViewById(R.id.batch_delivery_next_company_layout);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        //发货方式窗口
        mOrderDeliveryModeDialog = new OrderDeliveryModeDialog(activity);
        mOrderExpressIdDialog = new OrderExpressIdDialog(activity);

        // 上一级页面传来的数据
        mOrderDTO = (ArrayList<OrderDTO>) getIntent().getSerializableExtra("orderDTOs");

        //默认发货方式
        String default_delivery_mode  = SharedPreferencesUtils.getString("default_delivery_mode");
        if (default_delivery_mode.isEmpty()){
            batch_delivery_next_mode.setText("物流");
        }else{
            batch_delivery_next_mode.setText("" + default_delivery_mode);
        }
        if (default_delivery_mode.equals("无需物流")){
            batch_delivery_next_company_layout.setVisibility(View.GONE);
        }

        //默认物流公司
        String default_express_id = SharedPreferencesUtils.getString("default_express_id");
        String default_express_name  = SharedPreferencesUtils.getString("default_express_name");
        if (default_express_id.isEmpty()){
            default_express_id = "shunfeng";
            default_express_name = "顺丰速递";
        }
        choiceExpressDTO.express_company_id = default_express_id;
        choiceExpressDTO.express_company = default_express_name;
        batch_delivery_next_company.setText("" + default_express_name);

        refreshItem(mOrderDTO);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        batch_delivery_next_next_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //发货方式
        batch_delivery_next_mode_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderDeliveryModeDialog != null){
                    mOrderDeliveryModeDialog.show(new OrderDeliveryModeDialog.OnOrderDeliveryModeListener() {
                        @Override
                        public void onConfirm(int type) {
                            if (type == 0){
                                batch_delivery_next_company_layout.setVisibility(View.VISIBLE);
                                batch_delivery_next_mode.setText("物流");
                                SharedPreferencesUtils.put("default_delivery_mode", "物流");
                                isLogistics = true;
                            }else {
                                batch_delivery_next_company_layout.setVisibility(View.GONE);
                                batch_delivery_next_mode.setText("无需物流");
                                SharedPreferencesUtils.put("default_delivery_mode", "无需物流");
                                isLogistics = false;
                            }
                            refreshItem(mOrderDTO);
                        }
                    });
                }
            }
        });

        //物流公司
        batch_delivery_next_company_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("default","0");
                startActivityForResult(new Intent(activity, LogisticsCompanyActivity.class).putExtras(bundle),LOGISTICS_REQUEST_CODE);

            }
        });
        //发货
        batch_delivery_next_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                OrderShippingConfrim();
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<OrderDTO> orderData) {
        //mBatchDeliveryAdapter.addItems(orderData);
        // 清空列表布局
        batch_delivery_next_item_groups.removeAllViews();
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(orderData)) {
            // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局
            for (int i = 0; i < orderData.size(); i++) {
                OrderDeliveryItemView itemView = new OrderDeliveryItemView(activity);
                // 将View添加到列表布局里面
                batch_delivery_next_item_groups.addView(itemView.getRootView());
                itemView.batch_delivery_next_item_no.setText("" + orderData.get(i).order_id);
                itemView.batch_delivery_next_item_name.setText("" + orderData.get(i).receipt_name + "," +orderData.get(i).receipt_mobile + ",");//收货人,1340000000,
                itemView.batch_delivery_next_item_address.setText("" + orderData.get(i).receipt_province_name + orderData.get(i).receipt_city_name +
                        orderData.get(i).receipt_area_name + orderData.get(i).receipt_address);
                itemView.batch_delivery_next_item_number_et.setTag(orderData.get(i));
                itemView.batch_delivery_next_item_number_iv.setTag(itemView.batch_delivery_next_item_number_et);
                itemView.batch_delivery_next_item_group.setTag(itemView.batch_delivery_next_item_number_et);
                if (isLogistics){
                    itemView.order_delivery_number_layout.setVisibility(View.VISIBLE);
                }else{
                    itemView.order_delivery_number_layout.setVisibility(View.GONE);
                }

            }
        }
    }

    public class OrderDeliveryItemView extends BaseView {
        View itemView;
        View batch_delivery_next_item_group;
        TextView batch_delivery_next_item_no;
        TextView batch_delivery_next_item_name;
        TextView batch_delivery_next_item_address;
        TextView batch_delivery_next_item_number_et;
        ImageView batch_delivery_next_item_number_iv;
        LinearLayout order_delivery_number_layout;

        public OrderDeliveryItemView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_batch_delivery_next_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            batch_delivery_next_item_group = itemView.findViewById(R.id.batch_delivery_next_item_group);
            batch_delivery_next_item_no = (TextView) itemView.findViewById(R.id.batch_delivery_next_item_no);
            batch_delivery_next_item_name = (TextView) itemView.findViewById(R.id.batch_delivery_next_item_name);
            batch_delivery_next_item_address = (TextView) itemView.findViewById(R.id.batch_delivery_next_item_address);
            batch_delivery_next_item_number_et = (TextView) itemView.findViewById(R.id.batch_delivery_next_item_number_et);
            batch_delivery_next_item_number_iv = (ImageView) itemView.findViewById(R.id.batch_delivery_next_item_number_iv);
            order_delivery_number_layout = (LinearLayout) itemView.findViewById(R.id.order_delivery_number_layout);
        }

        @Override
        public void initData() {

        }

        @Override
        public void initListener() {
            //扫码
            batch_delivery_next_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    barcode_et = (TextView) v.getTag();
                    currentOrderDTO = (OrderDTO) barcode_et.getTag();
                    doCamera();
                }
            });
            //扫码
            batch_delivery_next_item_number_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    barcode_et = (TextView) v.getTag();
                    currentOrderDTO = (OrderDTO) barcode_et.getTag();
                    doCamera();
                }
            });
            //运单号
            batch_delivery_next_item_number_et.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    barcode_et = (TextView) v;
                    currentOrderDTO = (OrderDTO) barcode_et.getTag();
                    if (mOrderExpressIdDialog != null){
                        mOrderExpressIdDialog.show(barcode_et.getText().toString(), new OrderExpressIdDialog.OrderExpressIdListener() {
                            @Override
                            public void onConfirm(String freight) {
                                barcode_et.setText(freight);
                                currentOrderDTO.express_order_id = freight;
                            }
                        });
                    }
                }
            });
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

    /**
     * 接收权限是否请求的请求状态
     */
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
                            barcode_et.setText(TextUtil.trimToEmpty(result));
                            currentOrderDTO.express_order_id = TextUtil.trimToEmpty(result);
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
                        batch_delivery_next_company.setText(choiceExpressDTO.express_company);
                        Print.println("express_company_id:" + choiceExpressDTO.express_company_id);
                        Print.println("express_company:" + choiceExpressDTO.express_company);
                    }
                    break;
            }
        }
    }

    /**
     * 卖家【发货确认】
     */
    public void OrderShippingConfrim(){

        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);

        ArrayList<OrderDeliveryDTO> list = new ArrayList<>();
        boolean express_id_null = false;
        for (int i = 0; i < mOrderDTO.size(); i++) {
            if (mOrderDTO.get(i).express_order_id.isEmpty()){
                express_id_null = true;
                break;
            }
            OrderDeliveryDTO orderDeliveryDTO = new OrderDeliveryDTO();
            orderDeliveryDTO.order_id = mOrderDTO.get(i).order_id;
            orderDeliveryDTO.express_order_id = mOrderDTO.get(i).express_order_id;
            list.add(orderDeliveryDTO);
        }
        String has_express = batch_delivery_next_mode.getText().toString();
        if (has_express.equals("物流")){//是否需要发物流  1=需要,0=不需要
            has_express ="1";
            if (express_id_null){
                Toast.show(activity,"请输入运单号");
                return;
            }
        }else{
            has_express ="0";
        }
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
                Toast.show(activity, "批量发货成功！");
                dismissLoading();
                //发通知让订单页刷新数据
                EventBean bean = new EventBean(EventBean.CODE_V_ORDER_UPDATE);
                EventBus.getDefault().post(bean);
                //添加给第一个Activity的返回值，并设置resultCode
                Intent intent = new Intent();
                intent.putExtra("express_order_id", "123456789");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
