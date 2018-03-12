package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.ExpressLogisticsDTO;
import com.poso2o.lechuan.bean.order.ExpressLogisticsQueryDTO;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.system.ExpressDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

/**
 * 物流信息页面
 */
public class LogisticsInfoActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private static final int MODIFY_REQUEST_CODE = 11;
    //返回
    private ImageView logistics_info_back;
    private TextView logistics_info_id,logistics_info_company,logistics_info_company_no;
    private LinearLayout logistics_info_item_groups,logistics_info_company_no_layout,logistics_info_btn;
    //订单数据
    private OrderDTO mOrderDTO;
    private ExpressDTO choiceExpressDTO = new ExpressDTO();
    //
    private TextView logistics_info_tips;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_logistics_info;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        logistics_info_back = (ImageView) findViewById(R.id.logistics_info_back);
        logistics_info_id = (TextView) findViewById(R.id.logistics_info_id);
        logistics_info_company = (TextView) findViewById(R.id.logistics_info_company);
        logistics_info_company_no = (TextView) findViewById(R.id.logistics_info_company_no);
        logistics_info_btn = (LinearLayout) findViewById(R.id.logistics_info_btn);
        logistics_info_company_no_layout = (LinearLayout) findViewById(R.id.logistics_info_company_no_layout);

        logistics_info_item_groups = (LinearLayout) findViewById(R.id.logistics_info_item_groups);

        logistics_info_tips = (TextView) findViewById(R.id.logistics_info_tips);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mOrderDataManager = OrderDataManager.getOrderDataManager();

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("orderDTOs");

        logistics_info_id.setText("" + mOrderDTO.order_id);
        if (!mOrderDTO.express_company.isEmpty()){
            logistics_info_company.setText("" + mOrderDTO.express_company);
            OrderInfo();
        }else{
            logistics_info_company.setText("无需物流");
            logistics_info_company_no_layout.setVisibility(View.GONE);
        }
        logistics_info_company_no.setText("" + mOrderDTO.express_order_id);
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        logistics_info_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //修改物流
        logistics_info_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("orderDTOs",mOrderDTO);
                startActivityForResult(new Intent(activity, LogisticsModifyActivity.class).putExtras(bundle),MODIFY_REQUEST_CODE);
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<ExpressLogisticsDTO> express_logistics) {
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(express_logistics)) {

            // 清空列表布局
            logistics_info_item_groups.removeAllViews();

            // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局
            for (int i = 0; i < express_logistics.size(); i++) {
                OrderDeliveryItemView itemView = new OrderDeliveryItemView(activity);
                // 将View添加到列表布局里面
                logistics_info_item_groups.addView(itemView.getRootView());
                itemView.logistics_info_item_name.setText("" + express_logistics.get(i).time + "  " + express_logistics.get(i).status);
                if (i==0){
                    itemView.logistics_info_item_name.setTextColor(Color.parseColor("#00BCB4"));
                    itemView.logistics_info_item_iv.setImageResource(R.mipmap.round_dot_a);
                }else{
                    itemView.logistics_info_item_name.setTextColor(Color.parseColor("#5E5E5E"));
                    itemView.logistics_info_item_iv.setImageResource(R.mipmap.round_dot_b);
                }
            }
        }else{
            logistics_info_tips.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 查看订单详情
     */
    public void OrderInfo(){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.expressLogistics(activity, shop_id, "" + mOrderDTO.express_company_id, "" + mOrderDTO.express_order_id, new IRequestCallBack<ExpressLogisticsQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, ExpressLogisticsQueryDTO orderDTO) {
                dismissLoading();
                refreshItem(orderDTO.list);
            }
        });
    }


    public class OrderDeliveryItemView extends BaseView {
        View itemView;
        View logistics_info_item_group;
        ImageView logistics_info_item_iv;
        TextView logistics_info_item_name;

        public OrderDeliveryItemView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_logistics_info_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            logistics_info_item_group = itemView.findViewById(R.id.logistics_info_item_group);
            logistics_info_item_iv = (ImageView) itemView.findViewById(R.id.logistics_info_item_iv);
            logistics_info_item_name = (TextView) itemView.findViewById(R.id.logistics_info_item_name);
        }

        @Override
        public void initData() {

        }

        @Override
        public void initListener() {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MODIFY_REQUEST_CODE:
                    // 修改物流返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        choiceExpressDTO.express_company_id = data.getStringExtra("express_company_id");
                        choiceExpressDTO.express_company = data.getStringExtra("express_company");
                        choiceExpressDTO.express_order_id = data.getStringExtra("express_order_id");
                        logistics_info_company.setText(choiceExpressDTO.express_company);
                        logistics_info_company_no.setText(choiceExpressDTO.express_order_id);

                        //添加给第一个Activity的返回值，并设置resultCode
                        Intent intent = new Intent();
                        intent.putExtra("express_company_id", "" + choiceExpressDTO.express_company_id);
                        intent.putExtra("express_company", "" + choiceExpressDTO.express_company);
                        intent.putExtra("express_order_id", "" + choiceExpressDTO.express_order_id);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                    break;
            }
        }
    }

}
