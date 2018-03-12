package com.poso2o.lechuan.activity.order;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderDeliveryDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 修改订单备注页面
 */
public class OrderRemarkActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;
    //订单数据
    private OrderDTO mOrderDTO;
    //返回
    private ImageView order_remark_back;
    private TextView order_remark_confirm_btn;
    private EditText order_remark_et;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_remark;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_remark_back = (ImageView) findViewById(R.id.order_remark_back);
        order_remark_confirm_btn = (TextView) findViewById(R.id.order_remark_confirm_btn);
        order_remark_et = (EditText) findViewById(R.id.order_remark_et);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        glideRequest = Glide.with(activity);

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO)getIntent().getSerializableExtra("orderDTOs");
        order_remark_et.setText("" + mOrderDTO.order_remark);
        order_remark_et.setSelection(order_remark_et.getText().toString().length());//将光标移至文字末尾

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_remark_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //确定
        order_remark_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                editOrderRemark();
            }
        });
    }

    /**
     * 修改订单备注信息
     */
    public void editOrderRemark(){
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        final String order_remark = order_remark_et.getText().toString();
        if (order_remark.isEmpty()){
            Toast.show(activity,"请输入备注");
            order_remark_et.requestFocus();//获取焦点 光标出现
            KeyboardUtils.showSoftInput(order_remark_et);
            return;
        }
        showLoading();
        mOrderDataManager.editOrderRemark(activity, shop_id, "" + mOrderDTO.order_id, order_remark, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "设置备注成功！");
                dismissLoading();
                //添加给第一个Activity的返回值，并设置resultCode
                Intent intent = new Intent();
                intent.putExtra("order_id", "" + mOrderDTO.order_id);
                intent.putExtra("order_remark", "" + order_remark);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
