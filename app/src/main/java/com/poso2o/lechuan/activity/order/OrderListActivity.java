package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.OrderListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderQueryDTO;
import com.poso2o.lechuan.bean.order.OrderTotalBean;
import com.poso2o.lechuan.bean.printer.PrinterBean;
import com.poso2o.lechuan.dialog.AgreeRefundDialog;
import com.poso2o.lechuan.dialog.CloseOrderDialog;
import com.poso2o.lechuan.dialog.ContactBuyerDialog;
import com.poso2o.lechuan.dialog.RefuseRefundDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.printer.PrinterBluetoothActivity;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 订单列表页面
 */
public class OrderListActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    //页码数据
    private OrderTotalBean mOrderTotalBean = null;
    private String order_state = "0";
    private String order_refund_state = "0";
    //返回
    private ImageView order_list_back, order_list_scan, order_list_search_del;
    private EditText order_list_search;
    private static final int REQUEST_CODE = 11;
    private static final int DELIVERY_REQUEST_CODE = 22;
    private static final int REMARK_REQUEST_CODE = 33;
    private static final int LOGISTICS_REQUEST_CODE = 44;
    private static final int BATCH_DELIVERY_REQUEST_CODE = 55;
    private static final int ADDRESS_REQUEST_CODE = 66;
    private static final int ORDER_INFO_REQUEST_CODE = 77;
    private static final int ORDER_PRICE_REQUEST_CODE = 88;
    private static final int PRINT_REQUEST_CODE = 99;

    private final int CALL_PHONE = 1001;
    private final int SEND_SMS = 1002;
    private final int CAMERA = 1003;
    private String contact_buyer_phone;
    //当前编辑订单序号
    private int current_order_no = 0;
    /**
     * 列表
     */
    private RecyclerView order_list_recycler;
    private RecyclerView.LayoutManager order_list_manager;
    private OrderListAdapter mOrderListAdapter;
    private SwipeRefreshLayout order_list_refresh;

    private TextView no_orders_tips;

    //批量
    private LinearLayout order_list_batch_btn_layout;
    private LinearLayout order_list_batch_print,order_list_batch_delivery;

    //选择退款状态
    private LinearLayout order_list_refund_state_layout;
    private Spinner order_list_refund_state_spinner;
    private static final String[] name={"全部(0)","待处理(0)","已拒绝(0)","退款完成(0)"};
    private ArrayAdapter<String> adapter;

    //联系买家窗口
    private ContactBuyerDialog mContactBuyerDialog;
    //关闭订单窗口
    private CloseOrderDialog mCloseOrderDialog;
    //同意退款窗口
    private AgreeRefundDialog mAgreeRefundDialog;
    //拒绝退款窗口
    private RefuseRefundDialog mRefuseRefundDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_list;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_list_back = (ImageView) findViewById(R.id.order_list_back);
        order_list_scan = (ImageView) findViewById(R.id.order_list_scan);
        order_list_search = (EditText) findViewById(R.id.order_list_search);
        order_list_search_del = (ImageView) findViewById(R.id.order_list_search_del);

        //列表
        order_list_recycler = (RecyclerView) findViewById(R.id.order_list_recycler);
        order_list_manager = new LinearLayoutManager(activity);
        order_list_recycler.setLayoutManager(order_list_manager);
        order_list_refresh = (SwipeRefreshLayout) findViewById(R.id.order_list_refresh);
        order_list_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        no_orders_tips = (TextView) findViewById(R.id.no_orders_tips);

        order_list_batch_btn_layout = (LinearLayout) findViewById(R.id.order_list_batch_btn_layout);
        order_list_batch_print = (LinearLayout) findViewById(R.id.order_list_batch_print);
        order_list_batch_delivery = (LinearLayout) findViewById(R.id.order_list_batch_delivery);

        order_list_refund_state_layout = (LinearLayout) findViewById(R.id.order_list_refund_state_layout);
        order_list_refund_state_spinner = (Spinner) findViewById(R.id.order_list_refund_state_spinner);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mOrderDataManager = OrderDataManager.getOrderDataManager();
        //联系买家窗口
        mContactBuyerDialog = new ContactBuyerDialog(activity);
        //关闭订单窗口
        mCloseOrderDialog = new CloseOrderDialog(activity);
        //同意退款窗口
        mAgreeRefundDialog = new AgreeRefundDialog(activity);
        //拒绝退款窗口
        mRefuseRefundDialog = new RefuseRefundDialog(activity);

        // 上一级页面传来的数据
        Intent intent = getIntent();
        order_state = intent.getStringExtra("order_state");
        //order_state=订单状态 0=全部, 1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成  9.已关闭
        if (order_state.equals("2")) {
            order_list_batch_btn_layout.setVisibility(View.VISIBLE);
        } if (order_state.equals("4")){
            order_list_refund_state_layout.setVisibility(View.VISIBLE);
        }

        //将可选内容与ArrayAdapter连接起来，simple_spinner_item是android系统自带样式
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_order_state,name);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
        //将adapter 添加到spinner中
        order_list_refund_state_spinner.setAdapter(adapter);

        //列表
        mOrderListAdapter = new OrderListAdapter(activity, new OrderListAdapter.OnItemListener() {
            @Override
            public void onClickItem(OrderDTO orderDTOs, int position) {
                //itemOnClickListenner(posterData);

                current_order_no = position;
                Bundle bundle=new Bundle();
                //bundle.putString("news_id",posterData.news_id.toString());
                bundle.putSerializable("orderDTOs",orderDTOs);
                startActivityForResult(new Intent(activity, OrderInfoActivity.class).putExtras(bundle),ORDER_INFO_REQUEST_CODE);

            }
            @Override
            public void onClickBtn(int type, final OrderDTO orderDTOs, int position) {
                current_order_no = position;
                if (type == 1){//联系买家
                    if (mContactBuyerDialog != null) {
                        mContactBuyerDialog.show(new ContactBuyerDialog.OnContactBuyerListener() {
                            @Override
                            public void onConfirm(int type) {
                                if (type == 0){
                                    //call(posterData.receipt_mobile);
                                    contact_buyer_phone = orderDTOs.receipt_mobile;
                                    call();
                                }else if (type == 1){
                                    contact_buyer_phone = orderDTOs.receipt_mobile;
                                    doSendSMSTo();
                                }else {
                                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    //创建ClipData对象
                                    ClipData clipData = ClipData.newPlainText("copy",orderDTOs.receipt_name + " " + orderDTOs.receipt_mobile + " " + orderDTOs.receipt_province_name + orderDTOs.receipt_city_name +
                                            orderDTOs.receipt_area_name + orderDTOs.receipt_address);
                                    //添加ClipData对象到剪切板中
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.show(activity, "复制成功，可以发给买家了。");
                                }
                            }
                        });
                    }
                }else if (type == 2){//关闭
                    //关闭订单窗口
                    if (mCloseOrderDialog != null){
                        mCloseOrderDialog.show(orderDTOs, new CloseOrderDialog.OnContactBuyerListener() {
                            @Override
                            public void onConfirm(OrderDTO orderDTOs, String remark) {
                                delOrder(orderDTOs, remark);
                            }
                        });
                    }
                }else if (type == 3){//改价
                    Bundle bundle=new Bundle();
                    //bundle.putString("news_id",posterData.news_id.toString());
                    bundle.putSerializable("orderDTOs",orderDTOs);
                    startActivityForResult(new Intent(activity, OrderPriceActivity.class).putExtras(bundle),ORDER_PRICE_REQUEST_CODE);
                }else if (type == 4){//打印

                    ArrayList<OrderDTO> orderDTOsX = new ArrayList<OrderDTO>();//订单数据
                    orderDTOsX.add(orderDTOs);

                    PrinterBean printerBean = new PrinterBean();
                    printerBean.printer_type = 0;// 打印机类型，0：票据，1：标签
                    printerBean.ticket_type = 1;// 小票类型：0:打印测试，1：销售单，2：退货单，3：交接班，4:会员充值，5:标签
                    printerBean.printer_num = 1;// 打印张数
                    printerBean.print_message = "";// 打印内容
                    printerBean.open_casher = 1;// 1/2:开钱箱,3:检测打印机状态
                    printerBean.lablePrinterBeans = null;
                    printerBean.orderDTOs = orderDTOsX;

                    Intent intent = new Intent();
                    intent.putExtra("printerData",printerBean);//打印数据
                    intent.setClass(activity, PrinterBluetoothActivity.class);
                    ((BaseActivity) activity).startActivityForResult(intent, PRINT_REQUEST_CODE);

                }else if (type == 5){//发货
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderDTOs",orderDTOs);
                    startActivityForResult(new Intent(activity, OrderDeliveryActivity.class).putExtras(bundle),DELIVERY_REQUEST_CODE);
                }else if (type == 6){//拒绝退款
                    if (mRefuseRefundDialog != null){
                        mRefuseRefundDialog.show(orderDTOs, new RefuseRefundDialog.OnRefuseRefundListener() {
                            @Override
                            public void onConfirm(String remark) {
                                OrderRefundFail("" + orderDTOs.order_id, remark);
                            }
                        });
                    }
                }else if (type == 7){//同意退款
                    if (mAgreeRefundDialog != null){
                        mAgreeRefundDialog.show(orderDTOs, new AgreeRefundDialog.OnAgreeRefundListener() {
                            @Override
                            public void onConfirm() {
                                OrderRefundSuccess("" + orderDTOs.order_id);
                            }
                        });
                    }
                }else if (type == 8){//收货地址
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderDTOs",orderDTOs);
                    startActivityForResult(new Intent(activity, OrderAddressActivity.class).putExtras(bundle),ADDRESS_REQUEST_CODE);
                }else if (type == 9){//修改订单备注页面
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderDTOs",orderDTOs);
                    startActivityForResult(new Intent(activity, OrderRemarkActivity.class).putExtras(bundle),REMARK_REQUEST_CODE);
                }else if (type == 10){//物流信息页面
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderDTOs",orderDTOs);
                    startActivityForResult(new Intent(activity, LogisticsInfoActivity.class).putExtras(bundle),LOGISTICS_REQUEST_CODE);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        order_list_recycler.setLayoutManager(linearLayoutManager);
        order_list_recycler.setAdapter(mOrderListAdapter);

        //订单查找
        loadPosterData(1);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_list_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //扫码
        order_list_scan.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                doCamera();
            }
        });
        //关键字搜索
        order_list_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    // 每次editText有变化的时候，则移除上次发出的延迟线程
                    search_handler.removeCallbacks(delayRun);
                }
                // 延迟800ms，如果不再输入字符，则执行该线程的run方法
                search_handler.postDelayed(delayRun, 500);
            }
        });
        //搜索框
        order_list_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    // 强制隐藏键盘
                    KeyboardUtils.hideSoftInput(activity);

                    order_list_search.requestFocus();
                    order_list_search.selectAll();

                    //处理事件
                    loadPosterData(1);

                    final Handler handlerXXXX = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            order_list_search.requestFocus();
                            order_list_search.selectAll();
                            // 强制隐藏键盘
                            KeyboardUtils.hideSoftInput(activity);
                        }
                    };
                    handlerXXXX.postDelayed(runnable, 100);// 打开定时器，执行操作
                }
                return false;
            }
        });
        //清空输入框内容
        order_list_search_del.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                order_list_search_del.setVisibility(View.GONE);
                order_list_search.setText("");
                order_list_search.requestFocus();
                order_list_search.selectAll();
                // 强制隐藏键盘
                KeyboardUtils.hideSoftInput(activity);
            }
        });
        // 刷新
        order_list_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        order_list_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (order_list_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && mOrderListAdapter.getItemCount() >= 20 * mOrderTotalBean.currPage) {
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
        });
        //批量打印
        order_list_batch_print.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderListAdapter.getAllOrderDatas().size() == 0){
                    Toast.show(activity, "没有任何订单");
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("type","print");
                bundle.putString("keywords",order_list_search.getText().toString());
                bundle.putSerializable("orderDTOs",mOrderListAdapter.getAllOrderDatas());
                startActivity(new Intent(activity, BatchDeliveryActivity.class).putExtras(bundle));
            }
        });
        //批量发货
        order_list_batch_delivery.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderListAdapter.getAllOrderDatas().size() == 0){
                    Toast.show(activity, "没有任何订单");
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("type","delivery");
                bundle.putString("keywords",order_list_search.getText().toString());
                bundle.putSerializable("orderDTOs",mOrderListAdapter.getAllOrderDatas());
                startActivityForResult(new Intent(activity, BatchDeliveryActivity.class).putExtras(bundle),BATCH_DELIVERY_REQUEST_CODE);
            }
        });
    }

    /**
     * 订单查找+分页
     */
    public void loadPosterData(int currPage) {
        order_list_refresh.setRefreshing(true);
        //order_state     1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成
        //keywords        关键字
        //begin_time      开始时间（必填）
        //end_time        结束时间（必填）
        //currPage        查看页码（必填）
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        String keywords = order_list_search.getText().toString();

        //Print.println("getFirstDay:" + DateTimeUtil.getFirstDay());
        //Print.println("getTodayDate:" + DateTimeUtil.getTodayDate());
        //String begin_time = DateTimeUtil.stringToDate(DateTimeUtil.getFirstDay());
        //String end_time = DateTimeUtil.stringToDate(DateTimeUtil.getTodayDate());

        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -3);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        //System.out.println("前3个月的时间是：" + defaultStartDate);
        //System.out.println("生成的时间是：" + defaultEndDate);

        String begin_time = DateTimeUtil.stringToDate(defaultStartDate);
        String end_time = DateTimeUtil.stringToDate(defaultEndDate);

        mOrderDataManager.loadOrderQuery(activity, shop_id, order_state, order_refund_state, keywords, begin_time, end_time, "" + currPage, new IRequestCallBack<OrderQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                order_list_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderQueryDTO orderQueryDTO) {
                order_list_refresh.setRefreshing(false);
                mOrderTotalBean = orderQueryDTO.total;
                initSpinner(mOrderTotalBean);
                if (orderQueryDTO.list != null && orderQueryDTO.list.size() > 0) {
                    refreshItem(orderQueryDTO.list);
                    no_orders_tips.setVisibility(View.GONE);
                } else {
                    //Toast.show(activity, "没有任何信息！");
                    mOrderListAdapter.notifyData(null);
                    no_orders_tips.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //设置分类数据
    private void initSpinner(OrderTotalBean orderTotalBean){
        name[0] = "全部（" + orderTotalBean.total_order_cancel + "）";
        name[1] = "待处理（" + orderTotalBean.order_refund_state_flow + "）";
        name[2] = "已拒绝（" + orderTotalBean.order_refund_state_fail + "）";
        name[3] = "已完成（" + orderTotalBean.order_refund_state_success + "）";
        adapter.notifyDataSetChanged();
        //添加事件Spinner事件监听
        order_list_refund_state_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                order_list_refund_state_spinner.setTag((String) order_list_refund_state_spinner.getSelectedItem());
                arg0.setVisibility(View.VISIBLE);

                order_state = "4";
                if (arg2 == 0){
                    /**退款代码   :  1.申请退款   2.拒绝退款  3.已退款*/
                    order_refund_state = "0";
                }else if(arg2 == 1){
                    order_refund_state = "1";
                }else if(arg2 == 2){
                    order_refund_state = "2";
                }else if(arg2 == 3){
                    order_refund_state = "3";
                }
                loadPosterData(1);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<OrderDTO> orderData) {
        if (mOrderTotalBean.currPage == 1) {
            mOrderListAdapter.notifyData(orderData);
        } else {
            mOrderListAdapter.addItems(orderData);
        }
    }

    /**
     * 卖家【作废订单】
     */
    public void delOrder(final OrderDTO orderDTOs, String order_remark){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.delOrder(activity, shop_id, "" + orderDTOs.order_id, order_remark, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
                if (mCloseOrderDialog != null){
                    mCloseOrderDialog.dismiss();
                }
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                dismissLoading();
                Toast.show(activity, "关闭订单成功！");
                //发通知让订单页刷新数据
                EventBean bean = new EventBean(EventBean.CODE_V_ORDER_UPDATE);
                EventBus.getDefault().post(bean);
                if (mCloseOrderDialog != null){
                    mCloseOrderDialog.dismiss();
                }
                orderDTOs.order_state = 9;
                mOrderListAdapter.notifyItemChanged(current_order_no, "order");
            }
        });
    }

    /**
     * 卖家【拒绝退款】
     */
    public void OrderRefundFail(String order_id,String remark){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.OrderRefundFail(activity, shop_id, order_id, remark, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "拒绝退款成功！");
                dismissLoading();
                //发通知让订单页刷新数据
                EventBean bean = new EventBean(EventBean.CODE_V_ORDER_UPDATE);
                EventBus.getDefault().post(bean);
                if (mRefuseRefundDialog != null){
                    mRefuseRefundDialog.dismiss();
                }
                if (order_refund_state.equals("0")){//所有订单状态
                    /**退款代码   :  1.申请退款   2.拒绝退款  3.已退款*/
                    mOrderListAdapter.setRefundOrderFlag(2, current_order_no);
                    mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                }else{
                    mOrderListAdapter.delOrderItem(current_order_no);
                }
            }
        });
    }

    /**
     * 卖家【同意退款】
     */
    public void OrderRefundSuccess(String order_id){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.OrderRefundSuccess(activity, shop_id, order_id, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "同意退款成功！");
                dismissLoading();
                //发通知让订单页刷新数据
                EventBean bean = new EventBean(EventBean.CODE_V_ORDER_UPDATE);
                EventBus.getDefault().post(bean);
                if (mAgreeRefundDialog != null){
                    mAgreeRefundDialog.dismiss();
                }
                if (order_refund_state.equals("0")){//所有订单状态
                    /**退款代码   :  1.申请退款   2.拒绝退款  3.已退款*/
                    mOrderListAdapter.setRefundOrderFlag(3, current_order_no);
                    mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                }else{
                    mOrderListAdapter.delOrderItem(current_order_no);
                }
            }
        });
    }


    private Handler search_handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            // 在这里调用服务器的接口，获取数据
            //搜索
            loadPosterData(1);
            if (order_list_search.getText().toString().isEmpty()){
                order_list_search_del.setVisibility(View.GONE);
            }else{
                order_list_search_del.setVisibility(View.VISIBLE);
            }
        }
    };

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
                            order_list_search.setText(TextUtil.trimToEmpty(result));
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(activity, "扫描二维码失败");
                        }
                    }
                    break;
                case DELIVERY_REQUEST_CODE:
                    // 发货返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (order_state.equals("0")){//所有订单状态
                            String order_id = data.getStringExtra("order_id");
                            mOrderListAdapter.setOrderFlag(3, current_order_no);

                            String express_company_id = data.getStringExtra("express_company_id");
                            String express_company = data.getStringExtra("express_company");
                            String express_order_id = data.getStringExtra("express_order_id");
                            mOrderListAdapter.editOrderLogisticsItem(express_company_id, express_company, express_order_id, current_order_no);
                            mOrderListAdapter.notifyItemChanged(current_order_no, "order");

                        }else{
                            mOrderListAdapter.delOrderItem(current_order_no);
                        }
                    }
                    break;
                case REMARK_REQUEST_CODE:// 修改订单备注返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String order_remark = data.getStringExtra("order_remark");
                        mOrderListAdapter.editOrderRemarkItem(order_remark, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                    }
                    break;
                case LOGISTICS_REQUEST_CODE:// 物流信息页面返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String express_company_id = data.getStringExtra("express_company_id");
                        String express_company = data.getStringExtra("express_company");
                        String express_order_id = data.getStringExtra("express_order_id");
                        mOrderListAdapter.editOrderLogisticsItem(express_company_id, express_company, express_order_id, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                    }
                    break;
                case BATCH_DELIVERY_REQUEST_CODE:// 批量发货页面
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        loadPosterData(1);
                    }
                    break;
                case ADDRESS_REQUEST_CODE:// 修改订单地址页面
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String provinceName = data.getStringExtra("provinceName");
                        String cityName = data.getStringExtra("cityName");
                        String areaName = data.getStringExtra("areaName");
                        String address = data.getStringExtra("address");
                        mOrderListAdapter.editOrderAddressItem(provinceName, cityName, areaName, address, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                    }
                    break;
                case ORDER_INFO_REQUEST_CODE:// 订单详情页面返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        OrderDTO orderDTO = (OrderDTO) data.getSerializableExtra("orderDTOs");
                        mOrderListAdapter.editOrderItem(orderDTO, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");
                    }
                    break;
                case ORDER_PRICE_REQUEST_CODE:// 修改订单价格页面返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        OrderDTO orderDTO = (OrderDTO) data.getSerializableExtra("orderDTOs");
                        mOrderListAdapter.editOrderItem(orderDTO, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");
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

    /**
     * 调用拨号功能
     */
    private void call() {
        if(PhoneNumberUtils.isGlobalPhoneNumber(contact_buyer_phone)) {
            //系统会弹出需要请求权限的对话框
            if (android.os.Build.VERSION.SDK_INT > 22 && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact_buyer_phone));
                startActivity(intent);
            }
        }else {
            Toast.show(activity, "买家填写电话有误");
        }
    }

    /**
     * 调起系统发短信功能
     */
    public void doSendSMSTo(){
        if(PhoneNumberUtils.isGlobalPhoneNumber(contact_buyer_phone)){
            //系统会弹出需要请求权限的对话框
            if (android.os.Build.VERSION.SDK_INT > 22 && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.SEND_SMS }, SEND_SMS);
            }else{
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+contact_buyer_phone));
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        }else {
            Toast.show(activity, "买家填写电话有误");
        }
    }

    //接收权限是否请求的请求状态
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Print.println("requestCode:=========================================:"+requestCode);
        switch (requestCode) {
            case CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.show(activity,"没有权限");
                    mContactBuyerDialog.dismiss();
                }
            }
            case SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSendSMSTo();
                } else {
                    Toast.show(activity,"没有权限");
                    mContactBuyerDialog.dismiss();
                }
            }
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
