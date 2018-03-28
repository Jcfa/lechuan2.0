package com.poso2o.lechuan.activity.orderinfo;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPaperBean;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPaperManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 库存管理
 */

public class OrderInfoPaperActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private RecyclerView rlvPaper;
    private OrderInfoPaperAdapter adapter;
    private int type = 1;
    private TextView tv_order_sell_many_total, tv_order_zm_total;
    private TextView tv_default_null;
    private RelativeLayout rl_default_null;
    private ImageView ivSearch;
    private FrameLayout fl_search;
    private EditText etKcSearch;
    private List<OrderInfoPaperBean.DataBean> data;
    private ImageButton clear_input;
    private InputMethodManager imm;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_paper;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlvPaper = (RecyclerView) findViewById(R.id.rlv_order_sell_list);
        tv_order_sell_many_total = (TextView) findViewById(R.id.tv_order_sell_many_total);
        tv_order_zm_total = (TextView) findViewById(R.id.tv_order_zm_total);
        tv_default_null = (TextView) findViewById(R.id.tv_default_null);
        rl_default_null = (RelativeLayout) findViewById(R.id.rl2);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        fl_search = (FrameLayout) findViewById(R.id.fl_search);
        etKcSearch = (EditText) findViewById(R.id.et_kcsearch_input);
        clear_input = (ImageButton) findViewById(R.id.clear_input);
    }

    @Override
    protected void initData() {
        tvTitle.setText("库存管理");
        rlvPaper.setLayoutManager(new LinearLayoutManager(activity));
        ivSearch.setVisibility(View.VISIBLE);
        initNetApi();

    }

    private void initNetApi() {
        OrderInfoPaperManager.getsInsatcne().orderInfoPaperApi(activity, new IRequestCallBack<OrderInfoPaperBean>() {
            @Override
            public void onResult(int tag, OrderInfoPaperBean paperBean) {
                dismissLoading();
                data = paperBean.getData();
                if (data == null || data.size() < 0) {
                    Toast.show(activity, "数据为空");
                } else {
                    adapter = new OrderInfoPaperAdapter(activity, data);
                    rlvPaper.setAdapter(adapter);
                    tv_order_sell_many_total.setText(paperBean.getTotal().getTotalnums());
                    tv_order_zm_total.setText(paperBean.getTotal().getTotalamounts());
                    adapter.setOnItemClickListener(new OrderInfoPaperAdapter.RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            OrderInfoPaperBean.DataBean dataBean = data.get(position);
                            OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                            dialog.show();
                            dialog.setData(dataBean.getGuid(), "", type);
                        }
                    });
                }

            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                tv_default_null.setVisibility(View.VISIBLE);
                rl_default_null.setVisibility(View.GONE);
                rlvPaper.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initListener() {
        ivSearch.setOnClickListener(this);
        etKcSearch.setOnClickListener(this);
        clear_input.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                search();
                break;
            case R.id.et_kcsearch_input:
                kuSearch();
                break;
            case R.id.clear_input:
                etKcSearch.setText("");
                rl_default_null.setVisibility(View.VISIBLE);
//                initNetApi();
                adapter.setData(data);
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    /**
     * 因为是一次性加载所有数据  所以可以读取里面所有内容
     * 也可查询到所有数据
     */
    private void kuSearch() {
        //进行实时的监听搜索字段
        etKcSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchKeyword(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 按昵称进行搜索
     */
    private void searchKeyword(String msg) {
        //创建新的集合
        List<OrderInfoPaperBean.DataBean> searchlists = new ArrayList<>();
        if (TextUtils.isEmpty(msg)) {
            searchlists = data;
        } else {
            searchlists.clear();
            //根据lists集合中的对象字段名过滤
            for (OrderInfoPaperBean.DataBean list : data) {
                String num = list.getName();
                if (num.indexOf(msg.toString()) != -1) {
                    searchlists.add(list);
                }
            }
        }
        // 不管怎么样都要刷新
        adapter.updateSearchListView(searchlists);
    }

    /**
     * 点击按钮显示搜索框  进行搜索  不搜索时进行展示界面
     */
    private void search() {
        fl_search.setVisibility(View.VISIBLE);
        rl_default_null.setVisibility(View.GONE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 获取软键盘的显示状态
        boolean isOpen = imm.isActive();
        // 如果软键盘已经显示，则隐藏，反之则显示
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            rlvPaper.setVisibility(View.VISIBLE);
            rl_default_null.setVisibility(View.VISIBLE);
            tv_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rlvPaper.setVisibility(View.GONE);
            rl_default_null.setVisibility(View.GONE);
            tv_default_null.setVisibility(View.VISIBLE);
        } else if (action.equals("网络已连接")) {
            rlvPaper.setVisibility(View.VISIBLE);
            rl_default_null.setVisibility(View.VISIBLE);
            tv_default_null.setVisibility(View.GONE);
            initNetApi();
        } else if (action.equals("网络请求失败")) {
            rlvPaper.setVisibility(View.GONE);
            rl_default_null.setVisibility(View.GONE);
            tv_default_null.setVisibility(View.VISIBLE);
        }
    }
}
