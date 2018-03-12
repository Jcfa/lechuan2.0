package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MSelectDGAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.view.View.VISIBLE;


/**
 * Created by mr zhang on 2017/11/6.
 *选择折扣商品界面
 */

public class MSelectDGActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private Context context;

    //已选择的商品
    public static final String DG_SELECT_DATA = "dg_select_data";

    //返回
    private ImageView m_select_dg_back;
    //搜索输入框
    private EditText m_select_dg_search;
    //清楚搜索输入框
    private ImageView m_select_dg_search_delete;

    //全选
    private ImageView m_select_dg_select;
    //销量
    private TextView m_select_dg_sort_sale;
    //库存
    private TextView m_select_dg_sort_stock;
    //全部
    private TextView m_select_dg_catalog;

    private SwipeRefreshLayout m_dg_select_swipe;
    //列表
    private RecyclerView m_dg_select_recycler;
    //列表适配器
    private MSelectDGAdapter dgAdapter;

    //选择商品数量
    private TextView m_select_dg_num;
    //确定
    private Button m_select_dg_ok;

//    private SharedPreferencesUtil sharedPreferencesUtil ;

    //全部商品
    private ArrayList<Goods> mGoods;
    //当前展示的商品
    private ArrayList<Goods> currGoods;
    //目录
    private ArrayList<Catalog> catalogs;
    //已选中的商品
    private ArrayList<Goods> selectGoods = new ArrayList<>();
    //已参加活动的商品数量
    private int mHasNum;

    //排序
    private boolean sale_sort = true;
    private boolean stock_sort = true;

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    //当前目录
    private Catalog mCatalog;
    //目录弹窗
    private CatalogPopupWindow catalogPopupWindow;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_dg;
    }

    @Override
    public void initView() {
        context = this;

        m_select_dg_back = (ImageView) findViewById(R.id.m_select_dg_back);
        m_select_dg_search = (EditText) findViewById(R.id.m_select_dg_search);
        m_select_dg_search_delete = (ImageView) findViewById(R.id.m_select_dg_search_delete);

        m_select_dg_select = (ImageView) findViewById(R.id.m_select_dg_select);
        m_select_dg_sort_sale = (TextView) findViewById(R.id.m_select_dg_sort_sale);
        m_select_dg_sort_stock = (TextView) findViewById(R.id.m_select_dg_sort_stock);
        m_select_dg_catalog = (TextView) findViewById(R.id.m_select_dg_catalog);

        m_dg_select_swipe = (SwipeRefreshLayout) findViewById(R.id.m_dg_select_swipe);
        m_dg_select_recycler = (RecyclerView) findViewById(R.id.m_dg_select_recycler);

        m_select_dg_num = (TextView) findViewById(R.id.m_select_dg_num);
        m_select_dg_ok = (Button) findViewById(R.id.m_select_dg_ok); 
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);
        initGoodsAdapter();
        initGoodsData();
    }

    @Override
    public void initListener() {
        m_select_dg_back.setOnClickListener(this);
        m_select_dg_search_delete.setOnClickListener(this);

        m_select_dg_select.setOnClickListener(this);

        m_select_dg_sort_sale.setOnClickListener(this);
        m_select_dg_sort_stock.setOnClickListener(this);

        m_select_dg_catalog.setOnClickListener(this);
        m_select_dg_ok.setOnClickListener(this);

        m_dg_select_swipe.setOnRefreshListener(this);

        m_select_dg_search.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                search(m_select_dg_search.getText().toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_select_dg_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.m_select_dg_search_delete:
                m_select_dg_search.setText(null);
                break;
            case R.id.m_select_dg_select:
                selectAll();
                break;
            case R.id.m_select_dg_sort_sale:
                setSortTextAndIcon(m_select_dg_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
                isAscStock = false;
                if (isAscSale) {// 升序
                    setSortTextAndIcon(m_select_dg_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(m_select_dg_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscSale = !isAscSale;
                saleSort();
                break;
            case R.id.m_select_dg_sort_stock:
                setSortTextAndIcon(m_select_dg_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
                isAscSale = false;
                if (isAscStock) {// 升序
                    setSortTextAndIcon(m_select_dg_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(m_select_dg_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscStock = !isAscStock;
                stockSort();
                break;
            case R.id.m_select_dg_catalog:
                showCatalog();
                break;
            case R.id.m_select_dg_ok:
                clickOK();
                break;
        }
    }

    @Override
    public void onRefresh() {
        initGoodsData();
    }

    private void initGoodsAdapter(){
        dgAdapter = new MSelectDGAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        m_dg_select_recycler.setLayoutManager(linearLayoutManager);
        m_dg_select_recycler.setAdapter(dgAdapter);

        dgAdapter.setOnDGListener(new MSelectDGAdapter.OnDGListener() {
            @Override
            public void onDGClick(Goods goods) {
                if (selectGoods.contains(goods)){
                    selectGoods.remove(goods);
                }else {
                    selectGoods.add(goods);
                }
                m_select_dg_num.setText(selectGoods.size() + "");
                if (selectGoods.size() == mGoods.size()-mHasNum){
                    m_select_dg_select.setSelected(true);
                }else {
                    m_select_dg_select.setSelected(false);
                }
                if (selectGoods.size() == 0){
                    m_select_dg_ok.setSelected(false);
                }else {
                    m_select_dg_ok.setSelected(true);
                }
            }
        });
    }

    private void initGoodsData(){
        /*m_dg_select_swipe.setRefreshing(true);
        ArticleManager.getArticleManager().adGoodsList(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new ArticleManager.OnAdGoodsListener() {
            @Override
            public void onAdGoodsSuccess(BaseActivity baseActivity, AllAdGoodsData allAdGoodsData) {
                m_dg_select_swipe.setRefreshing(false);
                if (dgAdapter != null && allAdGoodsData != null){
                    dgAdapter.notifyAdapterData(allAdGoodsData.goods,selectGoods);
                    m_select_dg_catalog.setText("全部（" + allAdGoodsData.goods.size() + "）");
                    mGoods = allAdGoodsData.goods;
                    currGoods = mGoods;
                    catalogs = allAdGoodsData.catalogs;
                    Catalog catalog = new Catalog();
                    catalog.catalog_id = "-1";
                    catalog.catalog_name = "全部";
                    catalog.catalog_goods_number = mGoods.size();
                    catalogs.add(0,catalog);
                    mCatalog = catalog;
                    catalogPopupWindow = new CatalogPopupWindow(context,catalogs,mCatalog);
                    caculateNum();
                    catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {
                        @Override
                        public void onItemClick(Catalog catalog) {
                            catalogGoods(catalog);
                        }
                    });
                }
            }

            @Override
            public void onAdGoodsFail(BaseActivity baseActivity, String failStr) {
                m_dg_select_swipe.setRefreshing(false);
                new ToastView(context,failStr).show();
            }
        });*/
    }

    private void caculateNum(){
        for (Goods goods : currGoods){
            if (goods.goodsPromotionInfo != null && !goods.goodsPromotionInfo.goods_name.equals("")){
                mHasNum++;
            }
        }
    }

    private void clickOK(){
        if (selectGoods.size() == 0){
            Toast.show(context,"请先选择商品");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DG_SELECT_DATA,selectGoods);
        setResult(RESULT_OK,intent);
        finish();
    }

    //全选
    private void selectAll(){
        if (m_select_dg_select.isSelected()){
            selectGoods.clear();
            m_select_dg_select.setSelected(false);
            m_select_dg_ok.setSelected(false);
            m_select_dg_num.setText(selectGoods.size() + "");
        }else {
            selectGoods.clear();
            if (currGoods == null)return;
            for (Goods goods : currGoods){
                if (!(goods.goodsPromotionInfo != null && !goods.goodsPromotionInfo.goods_name.equals("")))selectGoods.add(goods);
            }
            m_select_dg_select.setSelected(true);
            m_select_dg_ok.setSelected(true);
            m_select_dg_num.setText(selectGoods.size() + "");
        }
        dgAdapter.notifyAdapterData(currGoods,selectGoods);
    }

    /**
     * 设置排序按钮颜色和图标
     * @param textView
     * @param colorResId
     * @param iconResId
     */
    private void setSortTextAndIcon(TextView textView, int colorResId, int iconResId) {
        textView.setTextColor(context.getResources().getColor(iconResId));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
    }

    //销量筛选
    private void saleSort(){
        if (sale_sort){
            sale_sort = false;
        }else {
            sale_sort = true;
        }

        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(mGoods);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = o1.goods_sale_number;
                Integer num2 = o2.goods_sale_number;
                if (!sale_sort) {// 升序
                    return num1.compareTo(num2);
                } else {// 降序
                    return num2.compareTo(num1);
                }
            }
        });

        if (dgAdapter != null){
            dgAdapter.notifyAdapterData(sortGoodsDatas,selectGoods);
        }
    }

    //库存筛选
    private void stockSort(){
        if (stock_sort){
            stock_sort = false;
        }else {
            stock_sort = true;
        }

        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(currGoods);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = o1.goods_number;
                Integer num2 = o2.goods_number;
                if (!stock_sort) {// 升序
                    return num1.compareTo(num2);
                } else {// 降序
                    return num2.compareTo(num1);
                }
            }
        });

        if (dgAdapter != null){
            dgAdapter.notifyAdapterData(sortGoodsDatas,selectGoods);
        }
    }

    //关键词搜索
    private void search(String keywords){
        ArrayList<Goods> search_goods = new ArrayList<>();
        search_goods:
        for (Goods good : mGoods){
            if (keywords.contains(good.goods_name) || good.goods_name.contains(keywords)){
                search_goods.add(good);
                continue search_goods;
            }
            if (keywords.contains(good.goods_no) || good.goods_no.contains(keywords)){
                search_goods.add(good);
                continue search_goods;
            }
            for (GoodsSpec spec : good.goods_spec){
                if (keywords.contains(spec.spec_bar_code) || spec.spec_bar_code.contains(keywords)){
                    search_goods.add(good);
                    continue search_goods;
                }
            }
        }
        currGoods = search_goods;
        if (dgAdapter != null){
            dgAdapter.notifyAdapterData(search_goods,selectGoods);
        }
    }

    //展示目录
    private void showCatalog(){
        if (catalogPopupWindow != null) {
            catalogPopupWindow.showAsDropDown(m_select_dg_catalog);
        }
    }

    //目录分类
    private void catalogGoods(Catalog catalog){
        mCatalog = catalog;
        m_select_dg_catalog.setText(catalog.catalog_name + "（" + catalog.catalog_goods_number + "）");
        if (catalog.catalog_id.equals("-1")){
            currGoods = mGoods;
            if (dgAdapter != null)dgAdapter.notifyAdapterData(currGoods,selectGoods);
            return;
        }
        ArrayList<Goods> catalog_goods = new ArrayList<>();
        for (Goods good : mGoods){
            if (good.catalog_id.equals(catalog.catalog_id)){
                catalog_goods.add(good);
            }
        }
        currGoods = catalog_goods;
        if (isAscSale){
            saleSort();
        }else {
            stockSort();
        }
    }

}
