package com.poso2o.lechuan.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BatchDeliveryAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderQueryDTO;
import com.poso2o.lechuan.bean.order.OrderTotalBean;
import com.poso2o.lechuan.bean.printer.PrinterBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.printer.PrinterBluetoothActivity;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

/**
 * 批量发货页面
 */
public class BatchDeliveryActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    //页码数据
    private OrderTotalBean mOrderTotalBean = null;
    private static final int BATCH_DELIVERY_NULL_REQUEST_CODE = 55;
    private static final int BATCH_PRINT_REQUEST_CODE = 66;
    private String type = "delivery";
    private String keywords = "";
    //返回
    private ImageView batch_delivery_back;
    private TextView batch_delivery_title;
    //全选、下一步
    private LinearLayout order_list_batch_check_all_layout,order_list_batch_next_layout,order_list_batch_print_layout;
    private ImageView order_list_batch_check_all;
    private TextView batch_delivery_num,batch_delivery_print_num;
    private boolean isChecked = false;
    /**
     * 列表
     */
    private RecyclerView batch_delivery_recycler;
    private RecyclerView.LayoutManager batch_delivery_manager;
    private BatchDeliveryAdapter mBatchDeliveryAdapter;
    private SwipeRefreshLayout batch_delivery_refresh;

    private ArrayList<OrderDTO> orderDTOs;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_batch_delivery;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        batch_delivery_back = (ImageView) findViewById(R.id.batch_delivery_back);
        batch_delivery_title = (TextView)  findViewById(R.id.batch_delivery_title);

        //列表
        batch_delivery_recycler = (RecyclerView) findViewById(R.id.batch_delivery_recycler);
        batch_delivery_manager = new LinearLayoutManager(activity);
        batch_delivery_recycler.setLayoutManager(batch_delivery_manager);
        batch_delivery_refresh = (SwipeRefreshLayout) findViewById(R.id.batch_delivery_refresh);
        batch_delivery_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);


        //全选、下一步
        order_list_batch_check_all_layout = (LinearLayout) findViewById(R.id.order_list_batch_check_all_layout);
        order_list_batch_next_layout = (LinearLayout) findViewById(R.id.order_list_batch_next_layout);
        order_list_batch_print_layout = (LinearLayout) findViewById(R.id.order_list_batch_print_layout);
        order_list_batch_check_all = (ImageView) findViewById(R.id.order_list_batch_check_all);
        batch_delivery_num = (TextView) findViewById(R.id.batch_delivery_num);
        batch_delivery_print_num = (TextView) findViewById(R.id.batch_delivery_print_num);
         ;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mOrderDataManager = OrderDataManager.getOrderDataManager();

        // 上一级页面传来的数据
        Intent intent = getIntent();
        keywords = intent.getStringExtra("keywords");
        type = intent.getStringExtra("type");
        if (type.equals("print")){
            batch_delivery_title.setText("批量打印");
            order_list_batch_next_layout.setVisibility(View.GONE);
            order_list_batch_print_layout.setVisibility(View.VISIBLE);
        }else{
            batch_delivery_title.setText("批量发货");
            order_list_batch_next_layout.setVisibility(View.VISIBLE);
            order_list_batch_print_layout.setVisibility(View.GONE);
        }

        //列表
        mBatchDeliveryAdapter = new BatchDeliveryAdapter(activity, new BatchDeliveryAdapter.OnItemListener() {
            @Override
            public void onClickItem() {
                totalAllCheckedNum();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        batch_delivery_recycler.setLayoutManager(linearLayoutManager);
        batch_delivery_recycler.setAdapter(mBatchDeliveryAdapter);

        this.orderDTOs = (ArrayList<OrderDTO>) intent.getSerializableExtra("orderDTOs");
        mBatchDeliveryAdapter.notifyData(orderDTOs);
        totalAllCheckedNum();

        //订单查找
        //loadPosterData(1);
        /*ArrayList<OrderDTO> orderData = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.order_id = 1;
        orderDTO.receipt_mobile = "13423678930";
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        mOrderListAdapter.notifyData(orderData);*/

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        batch_delivery_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        // 刷新
        batch_delivery_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                batch_delivery_refresh.setRefreshing(false);
                //loadPosterData(1);
            }
        });
        // 滚动监听
        /*batch_delivery_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (batch_delivery_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && mBatchDeliveryAdapter.getItemCount() >= 20 * mOrderTotalBean.currPage) {
                    Print.println("XXX:" + mOrderTotalBean.currPage);
                    Print.println("XXX:" + mOrderTotalBean.pages);
                    if (mOrderTotalBean.pages >= mOrderTotalBean.currPage + 1) {
                        loadPosterData(mOrderTotalBean.currPage + 1);
                    } else {
                        Toast.show(activity, "没有更多数据了");
                    }
                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });*/
        //全选
        order_list_batch_check_all_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (isChecked){
                    isChecked = false;
                    order_list_batch_check_all.setImageResource(R.mipmap.icon_ad_select_48);
                    mBatchDeliveryAdapter.setAllCheckedTrue(false);
                }else{
                    isChecked = true;
                    order_list_batch_check_all.setImageResource(R.mipmap.icon_ad_selected_blue_48);
                    mBatchDeliveryAdapter.setAllCheckedTrue(true);
                }
                totalAllCheckedNum();
            }
        });
        //下一步
        order_list_batch_next_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mBatchDeliveryAdapter.getCheckedOrderDTO().size() == 0){
                    Toast.show(activity, "没有选择的订单");
                    return;
                }
                Bundle bundle=new Bundle();
                //bundle.putString("keywords",order_list_search.getText().toString());
                bundle.putSerializable("orderDTOs",mBatchDeliveryAdapter.getCheckedOrderDTO());
                startActivityForResult(new Intent(activity, BatchDeliveryNextActivity.class).putExtras(bundle),BATCH_DELIVERY_NULL_REQUEST_CODE);
            }
        });
        //打印
        order_list_batch_print_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mBatchDeliveryAdapter.getCheckedOrderDTO().size() == 0){
                    Toast.show(activity, "没有选择的订单");
                    return;
                }
                PrinterBean printerBean = new PrinterBean();
                printerBean.printer_type = 0;// 打印机类型，0：票据，1：标签
                printerBean.ticket_type = 1;// 小票类型：0:打印测试，1：销售单，2：退货单，3：交接班，4:会员充值，5:标签
                printerBean.printer_num = 1;// 打印张数
                printerBean.print_message = "欢迎使用日进斗金店铺管理系统！";// 打印内容
                printerBean.open_casher = 1;// 1/2:开钱箱,3:检测打印机状态
                printerBean.lablePrinterBeans = null;
                printerBean.orderDTOs = mBatchDeliveryAdapter.getCheckedOrderDTO();

                Intent intent = new Intent();
                intent.putExtra("printerData",printerBean);//打印数据
                intent.setClass(activity, PrinterBluetoothActivity.class);
                ((BaseActivity) activity).startActivityForResult(intent, BATCH_PRINT_REQUEST_CODE);

            }
        });
    }

    /**
     * 订单查找+分页
     */
    public void loadPosterData(int currPage) {
        batch_delivery_refresh.setRefreshing(true);
        //order_state     1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成
        //keywords        关键字
        //begin_time      开始时间（必填）
        //end_time        结束时间（必填）
        //currPage        查看页码（必填）
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        Print.println("getFirstDay:" + DateTimeUtil.getFirstDay());
        Print.println("getTodayDate:" + DateTimeUtil.getTodayDate());
        String begin_time = DateTimeUtil.stringToDate(DateTimeUtil.getFirstDay());
        String end_time = DateTimeUtil.stringToDate(DateTimeUtil.getTodayDate());
        mOrderDataManager.loadOrderQuery(activity, shop_id, "2", "0", keywords, begin_time, end_time, "" + currPage, new IRequestCallBack<OrderQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                batch_delivery_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderQueryDTO orderQueryDTO) {
                batch_delivery_refresh.setRefreshing(false);
                mOrderTotalBean = orderQueryDTO.total;
                if (orderQueryDTO.list != null && orderQueryDTO.list.size() > 0) {
                    refreshItem(orderQueryDTO.list);
                } else {
                    Toast.show(activity, "没有任何信息！");
                    mBatchDeliveryAdapter.notifyData(null);
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<OrderDTO> orderData) {
        if (mOrderTotalBean.currPage == 1) {
            mBatchDeliveryAdapter.notifyData(orderData);
        } else {
            mBatchDeliveryAdapter.addItems(orderData);
        }
        totalAllCheckedNum();
    }

    /**
     * 统计选中数量
     * @return
     */
    public void totalAllCheckedNum(){
        batch_delivery_num.setText("(" + mBatchDeliveryAdapter.totalAllCheckedNum() + ")");
        batch_delivery_print_num.setText("(" + mBatchDeliveryAdapter.totalAllCheckedNum() + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BATCH_DELIVERY_NULL_REQUEST_CODE:
                    // 物流公司
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String express_order_id = data.getStringExtra("express_order_id");
                        //添加给第一个Activity的返回值，并设置resultCode
                        Intent intent = new Intent();
                        intent.putExtra("express_order_id", express_order_id);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }
    }
}
