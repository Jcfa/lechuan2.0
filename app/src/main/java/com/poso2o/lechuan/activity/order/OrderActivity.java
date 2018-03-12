package com.poso2o.lechuan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.poster.PosterDetailsActivity;
import com.poso2o.lechuan.adapter.MyFansAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.order.TodayOrderSaleReportDTO;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFansQueryDTO;
import com.poso2o.lechuan.bean.poster.MyFlowsTotalBean;
import com.poso2o.lechuan.dialog.InvitingDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.MyFansDataManager;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 订单页面
 */
public class OrderActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    //加载图片
    private RequestManager glideRequest;
    //返回
    private ImageView order_back,order_logo;
    private TextView order_title;
    //订单数量
    private TextView order_sales,order_sales_num,order_date_num,order_pending_payment_num,
            order_pending_delivery_num,order_already_shipped_num,order_refunds_num,
            order_successful_num,order_closed_num;
    private LinearLayout order_date,order_pending_payment,order_pending_delivery,
            order_already_shipped,order_refunds,order_successful,oorder_closed,order_evaluate;

    private SwipeRefreshLayout order_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_back = (ImageView) findViewById(R.id.order_back);
        order_title = (TextView) findViewById(R.id.order_title);
        order_logo = (ImageView) findViewById(R.id.order_logo);

        //订单数量
        order_sales = (TextView) findViewById(R.id.order_sales);
        order_sales_num = (TextView) findViewById(R.id.order_sales_num);
        order_date_num = (TextView) findViewById(R.id.order_date_num);
        order_pending_payment_num = (TextView) findViewById(R.id.order_pending_payment_num);
        order_pending_delivery_num = (TextView) findViewById(R.id.order_pending_delivery_num);
        order_already_shipped_num = (TextView) findViewById(R.id.order_already_shipped_num);
        order_refunds_num = (TextView) findViewById(R.id.order_refunds_num);
        order_successful_num = (TextView) findViewById(R.id.order_successful_num);
        order_closed_num = (TextView) findViewById(R.id.order_closed_num);

        order_date = (LinearLayout) findViewById(R.id.order_date);
        order_pending_payment = (LinearLayout) findViewById(R.id.order_pending_payment);
        order_pending_delivery = (LinearLayout) findViewById(R.id.order_pending_delivery);
        order_already_shipped = (LinearLayout) findViewById(R.id.order_already_shipped);
        order_refunds = (LinearLayout) findViewById(R.id.order_refunds);
        order_successful = (LinearLayout) findViewById(R.id.order_successful);
        oorder_closed = (LinearLayout) findViewById(R.id.oorder_closed);
        order_evaluate = (LinearLayout) findViewById(R.id.order_evaluate);

        //刷新
        order_refresh = (SwipeRefreshLayout) findViewById(R.id.order_refresh);
        order_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        //加载图片
        glideRequest = Glide.with(activity);
        glideRequest.load("" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO)).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e)
                .transform(new GlideCircleTransform(activity)).into(order_logo);
        order_title.setText("" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
        loadTodayOrderSaleReport();
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        // 刷新
        order_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTodayOrderSaleReport();
            }
        });
        order_date.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","0");//订单状态 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_pending_payment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","1");//订单状态 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_pending_delivery.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","2");
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_already_shipped.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","3");
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_refunds.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","4");
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_successful.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","8");
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        oorder_closed.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","9");
                startActivity(new Intent(activity, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_evaluate.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("order_state","0");
                startActivity(new Intent(activity, OrderEvaluateActivity.class).putExtras(bundle));
            }
        });
    }

    /**
     * 当天订单销售统计
     */
    private void loadTodayOrderSaleReport(){
        order_refresh.setRefreshing(true);
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.loadTodayOrderSaleReport(activity, shop_id, new IRequestCallBack<TodayOrderSaleReportDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                order_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, TodayOrderSaleReportDTO todayOrderSaleReportDTO) {
                order_refresh.setRefreshing(false);
                //订单数量
                order_sales.setText("" + NumberFormatUtils.format(todayOrderSaleReportDTO.total_today_order_amount));
                order_sales_num.setText("" + todayOrderSaleReportDTO.total_today_order_count);
                order_pending_payment_num.setText("" + todayOrderSaleReportDTO.total_order_pending_payment);
                order_pending_delivery_num.setText("" + todayOrderSaleReportDTO.total_order_paid);
                order_already_shipped_num.setText("" + todayOrderSaleReportDTO.total_order_shipped);
                order_refunds_num.setText("" + todayOrderSaleReportDTO.total_order_cancel);
                order_successful_num.setText("" + todayOrderSaleReportDTO.total_order_finish);
                order_closed_num.setText("" + todayOrderSaleReportDTO.total_order_close);
            }
        });
    }

    @Override
    public void onEvent(EventBean event) {
        super.onEvent(event);
        if (event.code == EventBean.CODE_V_ORDER_UPDATE)loadTodayOrderSaleReport();
    }
}
