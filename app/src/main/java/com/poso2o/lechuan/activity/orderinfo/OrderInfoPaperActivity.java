package com.poso2o.lechuan.activity.orderinfo;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darsh.multipleimageselect.models.Image;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.DataBean;
import com.poso2o.lechuan.bean.orderInfo.FidEventBus;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoPaperBean;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellBean;
import com.poso2o.lechuan.dialog.DialogQuerySellDir;
import com.poso2o.lechuan.dialog.OrderPaperDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPaperManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoPaperAdapter;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/14 0014.
 * 库存管理
 * 注意这里搜索为什么不OnClickListene的  因为第一次输入时 它
 * 还没获取到焦点  无法进行搜索相关内容 需要再次点击时才能进行
 * 搜索  而用OnTouchListener则第一次就可以获取到焦点  注意后面OnTouchListener
 * 返回false 不能为true  否则事件被拦截了
 */

public class OrderInfoPaperActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private TextView tvTitle;
    private RecyclerView rlvPaper;
    private OrderInfoPaperAdapter adapter;
    private int type = 1;
    private TextView tv_order_sell_many_total, tv_order_zm_total;
    private TextView tv_default_null;
    private RelativeLayout rl_default_null, rl_search_title;
    private ImageView ivSearch;
    private FrameLayout fl_search;
    private EditText etKcSearch;
    private List<OrderInfoPaperBean.DataBean> data;
    private ImageButton clear_input;
    private InputMethodManager imm;
    /**
     * 查询按钮
     */
    private TextView tvQuery;
    /**
     * 目录列表弹框
     */
    private DialogQuerySellDir dir;
    /**
     * 这里因为不需要展示开始时间和结束时间  隐藏掉
     */
    private TextView tvEndTime, tvEndTimeVisibibity, tvBeginTime;
    /**
     * 图标
     */
    private ImageView ivTuP, iv_back_search;
    /**
     * 格式化.0格式的
     */
    private String format;
    /**
     * 库存排序  总成本排序
     */
    private LinearLayout ll_kc_sort, ll_zcb_sort;
    /**
     * 库存排序  总成本排序
     */
    private ImageView iv_kc_sort, iv_zcb_sort;
    /**
     * 总成本   库存名称
     */
    private TextView tv_zcb_meny, tv_kc_count;
    /**
     * 标记按钮是否点击
     */
    public boolean IV_PAPER = false;

    /**
     * 标记是否点击回退
     */
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
        tvQuery = (TextView) findViewById(R.id.tv_query);
        tvEndTime = (TextView) findViewById(R.id.tv_order_end_time);
        tvEndTimeVisibibity = (TextView) findViewById(R.id.tv_visibility);
        tvBeginTime = (TextView) findViewById(R.id.tv_order_info_bgin_time);
        ivTuP = (ImageView) findViewById(R.id.iv_tu_biao);
        iv_back_search = (ImageView) findViewById(R.id.iv_back_search);

        ll_kc_sort = (LinearLayout) findViewById(R.id.ll_kc_sort);
        ll_zcb_sort = (LinearLayout) findViewById(R.id.ll_zcb_sort);
        iv_kc_sort = (ImageView) findViewById(R.id.iv_kc_sort);
        iv_zcb_sort = (ImageView) findViewById(R.id.iv_zcb_sort);
        tv_kc_count = (TextView) findViewById(R.id.tv_kc_count);
        tv_zcb_meny = (TextView) findViewById(R.id.tv_zcb_meny);
        rl_search_title = (RelativeLayout) findViewById(R.id.rl_search_title);
    }

    @Override
    protected void initData() {
        tvTitle.setText("库存管理");
        rlvPaper.setLayoutManager(new LinearLayoutManager(activity));
        ivSearch.setVisibility(View.VISIBLE);
        tvQuery.setVisibility(View.VISIBLE);
        tvEndTime.setVisibility(View.GONE);
        ivTuP.setImageResource(R.mipmap.kuquer);
        tvEndTimeVisibibity.setVisibility(View.GONE);
        tvBeginTime.setVisibility(View.GONE);
        initNetApi();
    }

    private void initNetApi() {
        showLoading();
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
                    tvQuery.setText("全部(" + data.size() + ")");
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
        etKcSearch.setOnTouchListener(this);
        iv_back_search.setOnTouchListener(this);
        clear_input.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        ll_kc_sort.setOnClickListener(this);
        ll_zcb_sort.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                search();
                break;
            case R.id.clear_input:
                etKcSearch.setText("");
                rl_default_null.setVisibility(View.GONE);
//                initNetApi();
                adapter.setData(data);
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.tv_query:
                queryDir();
                break;
            case R.id.ll_kc_sort:
                paperSort(iv_kc_sort);
                break;
            case R.id.ll_zcb_sort:
                paperSort(iv_zcb_sort);
                break;
        }
    }

    /**
     * 点击按钮进行排序和更改图标
     * * 进行比较大小
     * //当返回0的时候排序方式是 t1,t2
     * //当返回1的时候排序方式是 t2,t1
     */
    private void paperSort(ImageView ivsort) {
        if (!IV_PAPER) {
            if (ivsort.getId() == R.id.iv_kc_sort) {
                iv_kc_sort.setImageResource(R.mipmap.home_hand_up);
                tv_kc_count.setTextColor(getResources().getColor(R.color.color_00BCB4));
                tv_zcb_meny.setTextColor(getResources().getColor(R.color.textBlack));
                Collections.sort(data, new Comparator<OrderInfoPaperBean.DataBean>() {
                    @Override
                    public int compare(OrderInfoPaperBean.DataBean o1, OrderInfoPaperBean.DataBean o2) {
                        int sort1 = Integer.parseInt(o1.getTotalnum());
                        int sort2 = Integer.parseInt(o2.getTotalnum());
                        return sort1 - sort2;
                    }
                });
            } else if (ivsort.getId() == R.id.iv_zcb_sort) {
                iv_zcb_sort.setImageResource(R.mipmap.home_hand_up);
                tv_zcb_meny.setTextColor(getResources().getColor(R.color.color_00BCB4));
                tv_kc_count.setTextColor(getResources().getColor(R.color.textBlack));
                Collections.sort(data, new Comparator<OrderInfoPaperBean.DataBean>() {
                    @Override
                    public int compare(OrderInfoPaperBean.DataBean o1, OrderInfoPaperBean.DataBean o2) {
                        double sort1 = Double.parseDouble(o1.getTotalamount());
                        double sort2 = Double.parseDouble(o2.getTotalamount());
                        return (int) (sort1 - sort2);
                    }
                });
            }
            adapter.setData(data);
            IV_PAPER = true;
        } else if (IV_PAPER) {
            if (ivsort.getId() == R.id.iv_kc_sort) {
                iv_kc_sort.setImageResource(R.mipmap.down_sort);
                tv_kc_count.setTextColor(getResources().getColor(R.color.color_00BCB4));
                tv_zcb_meny.setTextColor(getResources().getColor(R.color.textBlack));
                Collections.reverse(data);
                adapter.updateSorttView(data);
            } else if (ivsort.getId() == R.id.iv_zcb_sort) {
                iv_zcb_sort.setImageResource(R.mipmap.down_sort);
                tv_zcb_meny.setTextColor(getResources().getColor(R.color.color_00BCB4));
                tv_kc_count.setTextColor(getResources().getColor(R.color.textBlack));
                Collections.sort(data, new Comparator<OrderInfoPaperBean.DataBean>() {
                    @Override
                    public int compare(OrderInfoPaperBean.DataBean o1, OrderInfoPaperBean.DataBean o2) {
                        double sort1 = Double.parseDouble(o1.getTotalamount());
                        double sort2 = Double.parseDouble(o2.getTotalamount());
                        return (int) (sort2 - sort1);
                    }
                });
                adapter.updateSorttView(data);
            }

            IV_PAPER = false;
        }

    }

    /**
     * 查询所有目录列表以及多少条目
     */
    private void queryDir() {
        dir = new DialogQuerySellDir(activity);
        dir.setData();
        dir.show();
    }

    /**
     * 根据事件总线进行参数设置参
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FidEventBus fidEventBus) {
        rl_default_null.setVisibility(View.GONE);
        String fid = fidEventBus.getFid();
        //创建新的集合

        List<OrderInfoPaperBean.DataBean> newlists = new ArrayList<>();
        if (TextUtils.isEmpty(fid)) {
            newlists = data;
        } else {
            newlists.clear();
            //根据lists集合中的对象字段名过滤
            int kucount = 0;
            double totalMeny = 0.00;
            for (OrderInfoPaperBean.DataBean sortModel : data) {
                String num = sortModel.getFid();
                if (num.equals(fid)) {
                    newlists.add(sortModel);
                    kucount += Integer.parseInt(sortModel.getTotalnum());
                    BigDecimal bg = new BigDecimal(Double.parseDouble(sortModel.getTotalamount()));
                    totalMeny += bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                    format = df.format(totalMeny);
                }
            }
//            tv_order_sell_many_total.setText(kucount + "");
//            tv_order_zm_total.setText(format);
        }
        // 不管怎么样都要刷新
        adapter.updateSearchListView(newlists);
        dir.dismiss();
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
                rl_default_null.setVisibility(View.GONE);
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
        rl_search_title.setVisibility(View.VISIBLE);
        fl_search.setVisibility(View.VISIBLE);
        rl_default_null.setVisibility(View.GONE);
      /*  imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 获取软键盘的显示状态
        boolean isOpen = imm.isActive();
        // 如果软键盘已经显示，则隐藏，反之则显示
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
    }

    /**
     * 进行网络状态的判断
     */
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.et_kcsearch_input:
                kuSearch();
                break;
            case R.id.iv_back_search:
                adapter.setData(data);
                rl_search_title.setVisibility(View.GONE);
                rl_default_null.setVisibility(View.VISIBLE);
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
        return false;
    }
}
