package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.poso2o.lechuan.activity.poster.ShopCommentActivity;
import com.poso2o.lechuan.adapter.OrderEvaluateAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.GoodsAppraiseDTO;
import com.poso2o.lechuan.bean.order.GoodsAppraiseQueryDTO;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderQueryDTO;
import com.poso2o.lechuan.bean.order.GoodsAppraiseTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

/**
 * 订单评论页面
 */
public class OrderEvaluateActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    //页码数据
    private GoodsAppraiseTotalBean mGoodsAppraiseTotalBean = null;
    //返回
    private ImageView order_evaluate_back, order_evaluate_scan, order_evaluate_search_del;
    private EditText order_evaluate_search;
    private static final int REQUEST_CODE = 11;
    private final int CAMERA = 1003;
    /**
     * 列表
     */
    private RecyclerView order_evaluate_recycler;
    private RecyclerView.LayoutManager order_evaluate_manager;
    private OrderEvaluateAdapter mOrderEvaluateAdapter;
    private SwipeRefreshLayout order_evaluate_refresh;

    //选择状态
    private Spinner order_evaluate_refund_state_spinner;
    private static final String[] name={"全部(0)","差评(0)","中评(0)","好评(0)"};
    private ArrayAdapter<String> adapter;

    private TextView no_evaluate_tips;

    //分类
    private int appraise_grade = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_evaluate;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_evaluate_back = (ImageView) findViewById(R.id.order_evaluate_back);
        order_evaluate_scan = (ImageView) findViewById(R.id.order_evaluate_scan);
        order_evaluate_search = (EditText) findViewById(R.id.order_evaluate_search);
        order_evaluate_search_del = (ImageView) findViewById(R.id.order_evaluate_search_del);
        //列表
        order_evaluate_recycler = (RecyclerView) findViewById(R.id.order_evaluate_recycler);
        order_evaluate_manager = new LinearLayoutManager(activity);
        order_evaluate_recycler.setLayoutManager(order_evaluate_manager);
        order_evaluate_refresh = (SwipeRefreshLayout) findViewById(R.id.order_evaluate_refresh);
        order_evaluate_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        order_evaluate_refund_state_spinner = (Spinner) findViewById(R.id.order_evaluate_refund_state_spinner);

        no_evaluate_tips = (TextView)  findViewById(R.id.no_evaluate_tips);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mOrderDataManager = OrderDataManager.getOrderDataManager();

        //将可选内容与ArrayAdapter连接起来，simple_spinner_item是android系统自带样式
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_order_state,name);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
        //将adapter 添加到spinner中
        order_evaluate_refund_state_spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        order_evaluate_refund_state_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                order_evaluate_refund_state_spinner.setTag((String) order_evaluate_refund_state_spinner.getSelectedItem());
                arg0.setVisibility(View.VISIBLE);
                appraise_grade = arg2;
                loadPosterData(1);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        //列表
        mOrderEvaluateAdapter = new OrderEvaluateAdapter(activity, new OrderEvaluateAdapter.OnItemListener() {

            @Override
            public void onClickItem(GoodsAppraiseDTO posterData) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("ShopComment", posterData);
                //bundle.putString("appraise_id","" + posterData.appraise_id);
                startActivity(new Intent(activity, ShopCommentActivity.class).putExtras(bundle));
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        order_evaluate_recycler.setLayoutManager(linearLayoutManager);
        order_evaluate_recycler.setAdapter(mOrderEvaluateAdapter);

        //订单查找
        loadPosterData(1);
        /*ArrayList<OrderDTO> orderData = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.order_id = 1;
        orderDTO.receipt_mobile = "13423678930";
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        orderData.add(orderDTO);
        mOrderEvaluateAdapter.notifyData(orderData);*/

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_evaluate_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //扫码
        order_evaluate_scan.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                doCamera();
            }
        });
        //关键字搜索
        order_evaluate_search.addTextChangedListener(new TextWatcher() {
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
        order_evaluate_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                    order_evaluate_search.requestFocus();
                    order_evaluate_search.selectAll();

                    //处理事件
                    loadPosterData(1);

                    final Handler handlerXXXX = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            order_evaluate_search.requestFocus();
                            order_evaluate_search.selectAll();
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
        order_evaluate_search_del.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                order_evaluate_search_del.setVisibility(View.GONE);
                order_evaluate_search.setText("");
                order_evaluate_search.requestFocus();
                order_evaluate_search.selectAll();
                // 强制隐藏键盘
                KeyboardUtils.hideSoftInput(activity);
            }
        });
        // 刷新
        order_evaluate_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        order_evaluate_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (order_evaluate_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && mOrderEvaluateAdapter.getItemCount() >= 20 * mGoodsAppraiseTotalBean.currPage) {
                    Print.println("XXX:" + mGoodsAppraiseTotalBean.currPage);
                    Print.println("XXX:" + mGoodsAppraiseTotalBean.pages);
                    if (mGoodsAppraiseTotalBean.pages >= mGoodsAppraiseTotalBean.currPage + 1) {
                        loadPosterData(mGoodsAppraiseTotalBean.currPage + 1);
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
    }

    /**
     * 订单查找+分页
     */
    public void loadPosterData(int currPage) {
        order_evaluate_refresh.setRefreshing(true);
        //order_state     1 待付款   2.已付款   3.已发货   4.已取消（申请退款  / 拒绝退款  / 已退款) 7.投诉中   8.已完成
        //keywords        关键字
        //begin_time      开始时间（必填）
        //end_time        结束时间（必填）
        //currPage        查看页码（必填）
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        String keywords = order_evaluate_search.getText().toString();
        mOrderDataManager.GoodsAppraiseQuery(activity, shop_id, keywords, "" + currPage,appraise_grade + "", new IRequestCallBack<GoodsAppraiseQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                order_evaluate_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, GoodsAppraiseQueryDTO orderQueryDTO) {
                order_evaluate_refresh.setRefreshing(false);
                mGoodsAppraiseTotalBean = orderQueryDTO.total;
                initSpinner();
                if (orderQueryDTO.list != null && orderQueryDTO.list.size() > 0) {
                    refreshItem(orderQueryDTO.list);
                    no_evaluate_tips.setVisibility(View.GONE);
                } else {
                    //Toast.show(activity, "没有任何信息！");
                    mOrderEvaluateAdapter.notifyData(null);
                    no_evaluate_tips.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //设置分类数据
    private void initSpinner(){
        if (mGoodsAppraiseTotalBean == null)return;
        name[0] = "全部（" + mGoodsAppraiseTotalBean.total_appraise_grade_count + "）";
        name[1] = "差评（" + mGoodsAppraiseTotalBean.total_appraise_grade_bad_count + "）";
        name[2] = "中评（" + mGoodsAppraiseTotalBean.total_appraise_grade_middle_count + "）";
        name[3] = "好评（" + mGoodsAppraiseTotalBean.total_appraise_grade_good_count + "）";
        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<GoodsAppraiseDTO> orderData) {
        if (mGoodsAppraiseTotalBean.currPage == 1) {
            mOrderEvaluateAdapter.notifyData(orderData);
        } else {
            mOrderEvaluateAdapter.addItems(orderData);
        }
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
            if (order_evaluate_search.getText().toString().isEmpty()){
                order_evaluate_search_del.setVisibility(View.GONE);
            }else{
                order_evaluate_search_del.setVisibility(View.VISIBLE);
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
                            order_evaluate_search.setText(TextUtil.trimToEmpty(result));
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(activity, "扫描二维码失败");
                        }
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
