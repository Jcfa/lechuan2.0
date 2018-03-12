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
import com.poso2o.lechuan.adapter.StockGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsStockData;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.dialog.StockGoodsDetailDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/3.
 * 库存页
 */

public class RStockActivity extends BaseActivity implements StockGoodsAdapter.OnStockSpecListener,SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private Context context;

    private ImageView r_stock_back;

    //下拉刷新
    private SwipeRefreshLayout stock_swipe_refresh;
    //库存排序
    private RelativeLayout stock_sort;
    //库存排序图标
    private ImageView tag_stock_picture;
    //商品列表
    private RecyclerView stock_goods_recycler;
    //库存数量合计
    private TextView total_stock_number;
    //总成本
    private TextView total_cost_amount;

    //商品列表适配器
    private StockGoodsAdapter goodsAdapter;

    //当前页数
    private int mPage = 1;
    //排序
    private String mSort = "DESC";
    //总页数
    private int mPages;
    //展示商品数据
    private ArrayList<Goods> goodses = new ArrayList<>();

    //是否微店
    private boolean is_online ;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stock;
    }

    @Override
    public void initView() {

        context = this;

        r_stock_back = (ImageView) findViewById(R.id.r_stock_back);

        stock_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.stock_swipe_refresh);

        stock_sort = (RelativeLayout) findViewById(R.id.stock_sort);

        tag_stock_picture = (ImageView) findViewById(R.id.tag_stock_picture);

        stock_goods_recycler = (RecyclerView) findViewById(R.id.stock_goods_recycler);

        total_stock_number = (TextView) findViewById(R.id.total_stock_number);

        total_cost_amount = (TextView) findViewById(R.id.total_cost_amount);

        stock_swipe_refresh.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        is_online = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);
        initGoodsAdapter();
        requestGoodsStock();
    }

    @Override
    public void initListener() {
        r_stock_back.setOnClickListener(this);
        stock_swipe_refresh.setOnRefreshListener(this);
        goodsAdapter.setOnStockSpecListener(this);
        stock_sort.setOnClickListener(this);
    }

    @Override
    public void onStockSpecClick(Goods goods) {
        if (is_online){
            StockGoodsDetailDialog detailDialog = new StockGoodsDetailDialog(context,goods,is_online);
            detailDialog.show();
        }else {
            showLoading();
            RReportManager.getRReportManger().stockDetail(this, goods.guid, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    Goods g = (Goods) result;
                    StockGoodsDetailDialog detailDialog = new StockGoodsDetailDialog(context,g,is_online);
                    detailDialog.show();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context,msg);
                }
            });
        }
    }

    @Override
    public void onLoadMore(TextView textView, ProgressBar progressBar) {
        if (mPage >= mPages){
            textView.setText("商品已加载完毕");
            progressBar.setVisibility(View.GONE);
        }else {
            textView.setText("正在加载数据。。。");
            progressBar.setVisibility(View.VISIBLE);
            loadMore();
        }
    }

    @Override
    public void onRefresh() {
        requestGoodsStock();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.r_stock_back:
                finish();
                break;
            case R.id.stock_sort:
                sortGoodsStock();
                break;
        }
    }

    private void initGoodsAdapter(){
        goodsAdapter = new StockGoodsAdapter(context,is_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stock_goods_recycler.setLayoutManager(linearLayoutManager);
        stock_goods_recycler.setAdapter(goodsAdapter);
    }

    private void requestGoodsStock(){
        if (!stock_swipe_refresh.isRefreshing())stock_swipe_refresh.setRefreshing(true);
        mPage = 1;
        if (is_online){
            WReportManager.getwReportManager().wStockList(this, mPage + "", mSort, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    stock_swipe_refresh.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    stock_swipe_refresh.setRefreshing(false);
                    AllGoodsStockData allGoodsStockData = (AllGoodsStockData) object;
                    if (allGoodsStockData == null)return;
                    setRefreshData(allGoodsStockData);
                    mSort = "ASC";
                    tag_stock_picture.setImageResource(R.mipmap.icon_default_sort);
                    if (allGoodsStockData.total != null && allGoodsStockData.total.pages != null){
                        mPages = Integer.parseInt(allGoodsStockData.total.pages);
                        total_stock_number.setText(allGoodsStockData.total.total_goods_number);
                        total_cost_amount.setText(allGoodsStockData.total.total_goods_cost_amount);
                    }else {
                        computerTotal(allGoodsStockData.list);
                    }
                }
            });
        }else {
            RReportManager.getRReportManger().goodsStock(this, mPage + "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    stock_swipe_refresh.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    stock_swipe_refresh.setRefreshing(false);
                    AllGoodsStockData allGoodsStockData = (AllGoodsStockData) object;
                    if (allGoodsStockData == null)return;
                        setRefreshData(allGoodsStockData);
                    mSort = "ASC";
                    tag_stock_picture.setImageResource(R.mipmap.icon_default_sort);
                    computerTotal(allGoodsStockData.list);
                    if (allGoodsStockData.total != null && allGoodsStockData.total.pages != null && !allGoodsStockData.total.pages.equals("")){
                        mPages = Integer.parseInt(allGoodsStockData.total.pages);
                        total_stock_number.setText(allGoodsStockData.total.totalnums);
                        total_cost_amount.setText(allGoodsStockData.total.totalamounts);
                    }
                }
            });
        }
    }

    private void loadMore(){
        mPage++;
        if (is_online){
            WReportManager.getwReportManager().wStockList(this,mPage + "",mSort, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    mPage--;
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    AllGoodsStockData allGoodsStockData = (AllGoodsStockData) object;
                    if (allGoodsStockData == null || goodsAdapter == null)return;
                    mPages = Integer.parseInt(allGoodsStockData.total.pages);
                    goodses.addAll(allGoodsStockData.list);
                    goodsAdapter.notifyAdapter(goodses);
                }
            });
        }else {
            RReportManager.getRReportManger().goodsStock(this, mPage + "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    mPage--;
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    AllGoodsStockData allGoodsStockData = (AllGoodsStockData) object;
                    if (allGoodsStockData == null || goodsAdapter == null)return;
                    mPages = Integer.parseInt(allGoodsStockData.total.pages);
                    goodses.addAll(allGoodsStockData.list);
                    goodsAdapter.notifyAdapter(goodses);
                }
            });
        }
    }

    private void sortGoodsStock(){
        if (mSort.equals("DESC")){
            mSort = "ASC";
            tag_stock_picture.setImageResource(R.mipmap.icon_up_sort);
        }else {
            mSort = "DESC";
            tag_stock_picture.setImageResource(R.mipmap.down_sort);
        }
        if (is_online){
            goodses.clear();
            goodses.addAll(WReportManager.getwReportManager().sortStockGoods(mSort));
            goodsAdapter.notifyAdapter(goodses);
            stock_goods_recycler.scrollToPosition(0);
        }else {
            goodses.clear();
            goodses.addAll(RReportManager.getRReportManger().sortStockGoods(mSort));
            goodsAdapter.notifyAdapter(goodses);
            stock_goods_recycler.scrollToPosition(0);
        }
    }

    private void setRefreshData(AllGoodsStockData allGoodsStockData){
        if ( goodsAdapter == null)return;
        if (allGoodsStockData == null){
            total_stock_number.setText("0");
            total_cost_amount.setText("0.00");
            mPages = 0;
            goodses.clear();
            goodsAdapter.notifyAdapter(goodses);
            return;
        }
        if (is_online){
//            mPages = Integer.parseInt(allGoodsStockData.total.pages);
//            total_stock_number.setText(allGoodsStockData.total.totalnums);
//            total_cost_amount.setText(allGoodsStockData.total.totalamounts);
            goodses.clear();
            goodses.addAll(allGoodsStockData.list);
            goodsAdapter.notifyAdapter(goodses);
        }else {
            goodses.clear();
            goodses.addAll(allGoodsStockData.list);
            goodsAdapter.notifyAdapter(goodses);
        }
    }

    //计算合计
    private void computerTotal(ArrayList<Goods> goodses){
        if (goodses == null || goodses.size() == 0)return;
        int totalNum = 0;
        double totalCost = 0;
        if (is_online){
            for (Goods goods : goodses){
                totalNum = totalNum + goods.goods_number;
                totalCost = ArithmeticUtils.add(totalCost,Double.parseDouble(goods.goods_cost_amount));
            }
        }else {
            for (Goods goods : goodses){
                totalNum = totalNum + Integer.parseInt(goods.totalnum);
                totalCost = ArithmeticUtils.add(totalCost,ArithmeticUtils.mul(Double.parseDouble(goods.fprice),Double.parseDouble(goods.totalnum)));
            }
        }
        total_cost_amount.setText(NumberFormatUtils.format(totalCost));
        total_stock_number.setText(totalNum + "");
    }

}
