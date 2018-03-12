package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.HotSaleGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.AllHotSaleProduct;
import com.poso2o.lechuan.bean.goodsdata.HotSaleProduct;
import com.poso2o.lechuan.dialog.CalendarDialog;
import com.poso2o.lechuan.dialog.HotSaleDetailDialog;
import com.poso2o.lechuan.dialog.MainDateDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/2.
 * 热销页
 */

public class RHotSaleActivity extends BaseActivity implements HotSaleGoodsAdapter.OnHotSaleItemListener,View.OnClickListener,CalendarDialog.OnDateSelectListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    //返回
    private ImageView r_hot_sale_back;

    //下拉刷新
    private SwipeRefreshLayout hot_product_swipe;
    //开始时间
    private TextView hot_product_calender_start;
    //结束时间
    private TextView hot_product_calender_stop;
    //销量排序
    private RelativeLayout layout_sale_volume;
    //销量排序图标
    private ImageView sale_volume;
    //商品列表
    private RecyclerView hot_product_recycler;

    //商品列表适配器
    private HotSaleGoodsAdapter goodsAdapter ;

    //是否为开始时间
    private boolean isStartTime;
    //日历弹窗
//    private CalendarDialog calendarDialog;
    //时间类型,1为日，2为周，3为月，4为自定义
    private int mDateType ;
    //开始时间
    private String mStartTime ;
    //结束时间
    private String mEndTime;
    //排序
    private String sort = "DESC";
    //当前页数
    private int curPage = 1;
    //总页数
    private int totalPage;
    //显示数据
    private ArrayList<HotSaleProduct> hotSaleProducts = new ArrayList<>();

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hot_product;
    }

    @Override
    public void initView() {

        context = this;

        r_hot_sale_back = (ImageView) findViewById(R.id.r_hot_sale_back);

        hot_product_swipe = (SwipeRefreshLayout) findViewById(R.id.hot_product_swipe);

        hot_product_calender_start = (TextView) findViewById(R.id.hot_product_calender_start);

        hot_product_calender_stop = (TextView) findViewById(R.id.hot_product_calender_stop);

        layout_sale_volume = (RelativeLayout) findViewById(R.id.layout_sale_volume);

        sale_volume = (ImageView) findViewById(R.id.sale_volume);

        hot_product_recycler = (RecyclerView) findViewById(R.id.hot_product_recycler);

        hot_product_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);
        initTime();
        initAdapter();
        requestHotSale();
    }

    @Override
    public void initListener() {
        r_hot_sale_back.setOnClickListener(this);
        goodsAdapter.setOnHotSaleItemListener(this);
        hot_product_calender_start.setOnClickListener(this);
        hot_product_calender_stop.setOnClickListener(this);
        layout_sale_volume.setOnClickListener(this);
        hot_product_swipe.setOnRefreshListener(this);
    }

    @Override
    public void onHotSaleClick(HotSaleProduct hotSaleProduct) {
        if (is_online){
            HotSaleDetailDialog detailDialog = new HotSaleDetailDialog(context);
            detailDialog.show();
            detailDialog.setData(hotSaleProduct,true);
        }else {
            forHotInfo(hotSaleProduct);
        }
    }

    @Override
    public void onLoadMore(TextView textView, ProgressBar progressBar) {
        if (curPage >= totalPage){
            textView.setText("商品已加载完毕");
            progressBar.setVisibility(View.GONE);
        }else {
            loadMore();
            textView.setText("正在加载更多。。。");
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_hot_sale_back:
                finish();
                break;
            case R.id.hot_product_calender_start:
//                onDateClick(true);
                showDateDialog();
                break;
            case R.id.hot_product_calender_stop:
//                onDateClick(false);
                break;
            case R.id.layout_sale_volume:
                sortSale();
                break;
        }
    }

    @Override
    public void onRefresh() {
        requestHotSale();
    }

    private void initTime(){
//        hot_product_calender_start.setText(CalendarUtil.getFirstDay());
//        hot_product_calender_stop.setText(CalendarUtil.getTodayDate());
//        mStartTime = CalendarUtil.timeStamp(CalendarUtil.getFirstDay() + " 00:00:00");
//        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
        mDateType = 2;
        mEndTime = CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 23:59:59");
        mStartTime = (Long.parseLong(CalendarUtil.timeStamp(CalendarUtil.getTodayDate() + " 00:00:00")) - 86400000*6) + "";
    }

    private void showDateDialog() {
        //时间弹窗
        MainDateDialog dateDialog = new MainDateDialog(context, mDateType, mStartTime, mEndTime);
        dateDialog.show();
        dateDialog.setOnDateListener(new MainDateDialog.OnDateListener() {
            @Override
            public void onDateClick(int type, String begin, String end) {
                mDateType = type;
                mStartTime = begin;
                mEndTime = end;
                if (type == 1) {
                    hot_product_calender_start.setText("今天");
                } else if (type == 2) {
                    hot_product_calender_start.setText("最近七天");
                } else if (type == 3) {
                    hot_product_calender_start.setText("本月");
                } else if (type == 4) {
                    mStartTime = DateTimeUtil.dataOne(begin + "-00-00-00");
                    mEndTime = DateTimeUtil.dataOne(end + "-59-59-59");
                    hot_product_calender_start.setText(begin + "至" + end);
                }
                requestHotSale();
            }
        });
    }

    private void initAdapter(){
        goodsAdapter = new HotSaleGoodsAdapter(context,is_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hot_product_recycler.setLayoutManager(linearLayoutManager);
        hot_product_recycler.setAdapter(goodsAdapter);
    }

    private void requestHotSale(){
        if (! hot_product_swipe.isRefreshing())hot_product_swipe.setRefreshing(true);
        curPage = 1;
        if (is_online){
            WReportManager.getwReportManager().wHotSale(this, curPage + "", mStartTime, mEndTime, sort, "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    hot_product_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    hot_product_swipe.setRefreshing(false);
                    AllHotSaleProduct allHotSaleProduct = (AllHotSaleProduct) object;
                    if (allHotSaleProduct != null){
                        totalPage = allHotSaleProduct.total.pages;
                        hotSaleProducts.clear();
                        hotSaleProducts.addAll(allHotSaleProduct.list);
                    }
                    if (goodsAdapter != null){
                        goodsAdapter.notifyAdapter(hotSaleProducts);
                    }
                }
            });
        }else {
            RReportManager.getRReportManger().hotSale(this, curPage + "",DateTimeUtil.StringToData(mStartTime,"yyyy-MM-dd"), DateTimeUtil.StringToData(mEndTime,"yyyy-MM-dd"),sort, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    hot_product_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    hot_product_swipe.setRefreshing(false);
                    AllHotSaleProduct allHotSaleProduct = (AllHotSaleProduct) object;
                    if (allHotSaleProduct != null){
                        totalPage = allHotSaleProduct.total.pages;
                        hotSaleProducts.clear();
                        hotSaleProducts.addAll(allHotSaleProduct.list);
                    }
                    if (goodsAdapter != null){
                        goodsAdapter.notifyAdapter(hotSaleProducts);
                    }
                }
            });
        }
    }

    public void loadMore(){
//        showLoading("正在加载数据");
        curPage++;
        if (is_online){
            WReportManager.getwReportManager().wHotSale(this, curPage + "", mStartTime, mEndTime, sort, "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    curPage--;
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    AllHotSaleProduct allHotSaleProduct = (AllHotSaleProduct) object;
                    if (allHotSaleProduct != null){
                        hotSaleProducts.addAll(allHotSaleProduct.list);
                    }
                    if (goodsAdapter != null)
                        goodsAdapter.notifyAdapter(hotSaleProducts);
                }
            });
        }else {
            RReportManager.getRReportManger().hotSale(this, curPage + "", hot_product_calender_start.getText().toString(),hot_product_calender_stop.getText().toString(),sort, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    curPage--;
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    AllHotSaleProduct allHotSaleProduct = (AllHotSaleProduct) object;
                    if (allHotSaleProduct != null){
                        hotSaleProducts.addAll(allHotSaleProduct.list);
                    }
                    if (goodsAdapter != null)
                        goodsAdapter.notifyAdapter(hotSaleProducts);
                }
            });
        }
    }

    //选择日历
    /*private void onDateClick(boolean b) {
        isStartTime = b;
        if (b){
            calendarDialog = new CalendarDialog(context,hot_product_calender_start.getText().toString());
        }else {
            calendarDialog = new CalendarDialog(context,hot_product_calender_stop.getText().toString());
        }
        calendarDialog.show();
        calendarDialog.setOnDateSelectListener(this);
    }*/

    @Override
    public void onDateSelect(CustomDate date) {
        if (date == null) return;
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isStartTime) {
            if (CalendarUtil.TimeCompare(dateT, hot_product_calender_stop.getText().toString())) {
                hot_product_calender_start.setText(dateT);
                mStartTime = CalendarUtil.timeStamp(dateT + " 00:00:00");
//                calendarDialog.dismiss();
                requestHotSale();
            } else {
                Toast.show(context, "选择的时间范围不正确");
                return;
            }
        } else {
            if (CalendarUtil.TimeCompare(hot_product_calender_start.getText().toString(), dateT)) {
                hot_product_calender_stop.setText(dateT);
                mEndTime = CalendarUtil.timeStamp(dateT + " 23:59:59");
//                calendarDialog.dismiss();
            } else {
                Toast.show(context, "选择的时间范围不正确");
                return;
            }
        }
        requestHotSale();
    }

    //排序
    private void sortSale(){
        hot_product_swipe.setRefreshing(true);
        if (sort.equals("DESC")){
            sort = "ASC";
            sale_volume.setImageResource(R.mipmap.icon_up_sort);
            requestHotSale();
        }else {
            sort = "DESC";
            sale_volume.setImageResource(R.mipmap.down_sort);
            requestHotSale();
        }
    }

    //获取详情信息
    private void forHotInfo(HotSaleProduct hotSaleProduct){
        showLoading();
        RReportManager.getRReportManger().hotSalesInfo(this, hot_product_calender_start.getText().toString(), hot_product_calender_stop.getText().toString(), hotSaleProduct.guid, hotSaleProduct.colorid, hotSaleProduct.sizeid, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                HotSaleProduct product = (HotSaleProduct) result;
                if (product == null){
                    Toast.show(context,"该商品已删除，无法显示");
                    return;
                }
                HotSaleDetailDialog detailDialog = new HotSaleDetailDialog(context);
                detailDialog.show();
                detailDialog.setData(product,false);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }
        });
    }

}
