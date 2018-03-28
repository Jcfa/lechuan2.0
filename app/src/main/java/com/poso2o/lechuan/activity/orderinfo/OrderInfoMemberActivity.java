package com.poso2o.lechuan.activity.orderinfo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoMemberBean;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoMemberManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoMemberAdapter;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/15 0015.
 * 会员管理
 */

public class OrderInfoMemberActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private RecyclerView rlv;
    private int type = 3;
    //显示网络异常或者为空清空
    private TextView iv_default_null;
    private ImageView ivSearch;
    private FrameLayout fl_search;
    private EditText etMemberSearch;
    private ImageButton clear_input;
    private List<OrderInfoMemberBean.DataBean> data;
    private OrderInfoMemberAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orderinfo_member;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rlv = (RecyclerView) findViewById(R.id.lrv_member);
        iv_default_null = (TextView) findViewById(R.id.iv_default_null);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        fl_search = (FrameLayout) findViewById(R.id.fl_search);
        etMemberSearch = (EditText) findViewById(R.id.et_kcsearch_input);
        clear_input = (ImageButton) findViewById(R.id.clear_input);

    }

    @Override
    protected void initData() {
        tvTitle.setText("会员管理");
        rlv.setLayoutManager(new LinearLayoutManager(activity));
        ivSearch.setVisibility(View.VISIBLE);
        initNetApi();
    }

    private void initNetApi() {
        OrderInfoMemberManager.getsInstance().oInfoMember(activity, new IRequestCallBack<OrderInfoMemberBean>() {
            @Override
            public void onResult(int tag, OrderInfoMemberBean infoMemberBean) {
                dismissLoading();
                data = infoMemberBean.getData();
                adapter = new OrderInfoMemberAdapter(data);
                rlv.setAdapter(adapter);
                adapter.setOnItemClickListener(new OrderInfoMemberAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        OrderPaperDetailDialog dialog = new OrderPaperDetailDialog(activity);
                        dialog.show();
                        dialog.setData(data.get(position).getUid(), "", type);
                    }
                });
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                iv_default_null.setVisibility(View.VISIBLE);
                rlv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initListener() {
        ivSearch.setOnClickListener(this);
        clear_input.setOnClickListener(this);
        etMemberSearch.setOnClickListener(this);
    }

    @Override
    public void onEvent(String action) {
        super.onEvent(action);
        if (action.equals("网络请求成功")) {
            rlv.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
        } else if (action.equals("网络未连接")) {
            rlv.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        } else if (action.equals("网络已连接")) {
            rlv.setVisibility(View.VISIBLE);
            iv_default_null.setVisibility(View.GONE);
            initNetApi();
        } else if (action.equals("网络请求失败")) {
            rlv.setVisibility(View.GONE);
            iv_default_null.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                fl_search.setVisibility(View.VISIBLE);
                break;
            case R.id.et_kcsearch_input:
                memberSearch();
                break;
            case R.id.clear_input:
                etMemberSearch.setText("");
                //清除数据刷新列表
                adapter.setData(data);
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    /**
     * 会员搜索 按会员号进行搜索
     */
    private void memberSearch() {
        etMemberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchMember(s.toString());
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
     * 开始搜索会员号
     */
    private void searchMember(String msg) {
        List<OrderInfoMemberBean.DataBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(msg)) {
            list = data;
        } else {
            list.clear();
            for (OrderInfoMemberBean.DataBean search : data) {
                String nick = search.getNick();
                if (nick.indexOf(msg.toString()) != -1) {
                    list.add(search);
                }
            }
        }
        adapter.updateMemberView(list);

    }
}
