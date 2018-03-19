package com.poso2o.lechuan.fragment.vdian;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.order.OrderEvaluateActivity;
import com.poso2o.lechuan.activity.order.OrderListActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.order.TodayOrderSaleReportDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * 订单页面
 */
public class VdianOrderFragment extends BaseFragment {

    /**
     * 网络管理
     */
    private OrderDataManager mOrderDataManager;

    /**
     * 订单数量
     */
    private TextView order_sales, order_sales_num, order_date_num, order_pending_payment_num,
            order_pending_delivery_num, order_already_shipped_num, order_refunds_num,
            order_successful_num, order_closed_num;

    private LinearLayout order_date, order_pending_payment, order_pending_delivery,
            order_already_shipped, order_refunds, order_successful, order_closed, order_evaluate;

    private SwipeRefreshLayout order_refresh;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vdian_order, container, false);
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        // 订单数量
        order_sales = findView(R.id.order_sales);
        order_sales_num = findView(R.id.order_sales_num);
        order_date_num = findView(R.id.order_date_num);
        order_pending_payment_num = findView(R.id.order_pending_payment_num);
        order_pending_delivery_num = findView(R.id.order_pending_delivery_num);
        order_already_shipped_num = findView(R.id.order_already_shipped_num);
        order_refunds_num = findView(R.id.order_refunds_num);
        order_successful_num = findView(R.id.order_successful_num);
        order_closed_num = findView(R.id.order_closed_num);

        order_date = findView(R.id.order_date);
        order_pending_payment = findView(R.id.order_pending_payment);
        order_pending_delivery = findView(R.id.order_pending_delivery);
        order_already_shipped = findView(R.id.order_already_shipped);
        order_refunds = findView(R.id.order_refunds);
        order_successful = findView(R.id.order_successful);
        order_closed = findView(R.id.oorder_closed);
        order_evaluate = findView(R.id.order_evaluate);

        // 刷新
        order_refresh = findView(R.id.order_refresh);
        order_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        // 加载图片
        loadTodayOrderSaleReport();
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
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
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "0");//订单状态 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_pending_payment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "1");//订单状态 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_pending_delivery.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "2");
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_already_shipped.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "3");
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_refunds.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "4");
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_successful.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "8");
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_closed.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "9");
                startActivity(new Intent(context, OrderListActivity.class).putExtras(bundle));
            }
        });
        order_evaluate.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("order_state", "0");
                startActivity(new Intent(context, OrderEvaluateActivity.class).putExtras(bundle));
            }
        });
    }

    /**
     * 当天订单销售统计
     */
    private void loadTodayOrderSaleReport() {
        order_refresh.setRefreshing(true);
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.loadTodayOrderSaleReport((BaseActivity) context, shop_id, new IRequestCallBack<TodayOrderSaleReportDTO>() {

            @Override
            public void onFailed(int tag, String msg) {
                order_refresh.setRefreshing(false);
                Toast.show(context, msg);
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

    public void onEvent(EventBean event) {
        if (event.code == EventBean.CODE_V_ORDER_UPDATE && view != null) {
            loadTodayOrderSaleReport();
        }
    }
}
