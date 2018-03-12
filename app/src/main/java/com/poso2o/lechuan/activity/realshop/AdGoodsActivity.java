package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.GoodsListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by mr zhang on 2017/10/31.
 */

public class AdGoodsActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回类型 ：0是店铺，1是商品
    public static final String AD_TYPE = "ad_type";
    //返回商品
    public static final String AD_ARTICLE_GOODS = "ad_article_goods";
    //店铺类型：true为微店，false为实体店
    public static final String SHOP_TYPE = "shop_type";

    //返回
    private ImageView ad_goods_back;
    //搜索框
    private EditText ad_goods_search;
    //删除
    private ImageView ad_goods_search_delete;
    //扫码
    private ImageView ad_goods_scan_code;
    //店铺头像
    private ImageView ad_shop_pic;
    //店铺名称
    private TextView ad_shop_name;
    //店铺描述
    private TextView ad_shop_des;
    //销量筛选
    private TextView ad_goods_sort_sale;
    //库存筛选
    private TextView ad_goods_sort_stock;
    //类目筛选
    private TextView ad_goods_catalog;
    //阴影
    private View catalog_shade;

    private SwipeRefreshLayout ad_goods_swipe;
    private RecyclerView ad_goods_recycle;

    private TextView ad_goods_no_goods;
    private TextView ad_goods_search_no_goods;
    private TextView ad_goods_hint_no_goods;

    //下拉菜单
    private CatalogPopupWindow catalogPopupWindow;
    //选中的目录
    private Catalog selectCatalog;
    
    //列表数据适配器
    private GoodsListAdapter goodsListAdapter;

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    //是否微店
    private boolean is_vshop = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ad_goods;
    }

    @Override
    public void initView() {
        context = this; 

        ad_goods_back = (ImageView) findViewById(R.id.ad_goods_back);

        ad_goods_search = (EditText) findViewById(R.id.ad_goods_search);

        ad_goods_search_delete = (ImageView) findViewById(R.id.ad_goods_search_delete);

        ad_goods_scan_code = (ImageView) findViewById(R.id.ad_goods_scan_code);

        ad_shop_pic = (ImageView) findViewById(R.id.ad_shop_pic);

        ad_shop_name = (TextView) findViewById(R.id.ad_shop_name);

        ad_shop_des = (TextView) findViewById(R.id.ad_shop_des);

        ad_goods_sort_sale = (TextView) findViewById(R.id.ad_goods_sort_sale);

        ad_goods_sort_stock = (TextView) findViewById(R.id.ad_goods_sort_stock);

        ad_goods_catalog = (TextView) findViewById(R.id.ad_goods_catalog);

        catalog_shade = findViewById(R.id.catalog_shade);

        ad_goods_swipe = (SwipeRefreshLayout) findViewById(R.id.ad_goods_swipe);

        ad_goods_recycle = (RecyclerView) findViewById(R.id.ad_goods_recycle);

        ad_goods_no_goods = (TextView) findViewById(R.id.ad_goods_no_goods);

        ad_goods_search_no_goods = (TextView) findViewById(R.id.ad_goods_search_no_goods);

        ad_goods_hint_no_goods = (TextView) findViewById(R.id.ad_goods_hint_no_goods);
    }

    @Override
    public void initData() {
//        is_vshop = !SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        goodsListAdapter = new GoodsListAdapter(context,is_vshop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ad_goods_recycle.setLayoutManager(linearLayoutManager);
        ad_goods_recycle.setAdapter(goodsListAdapter);
 
        loadGoodsData();
    }

    @Override
    public void initListener() {
        ad_goods_back.setOnClickListener(this);
        ad_goods_search_delete.setOnClickListener(this);
        ad_goods_scan_code.setOnClickListener(this);
        ad_goods_sort_sale.setOnClickListener(this);
        ad_goods_sort_stock.setOnClickListener(this);
        ad_goods_catalog.setOnClickListener(this);

        ad_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGoodsData();
            }
        });

        goodsListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.putExtra(AD_TYPE,1);
                intent.putExtra(AD_ARTICLE_GOODS,item);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        ad_goods_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(ad_goods_search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View  view){
        switch (view.getId()){
            case R.id.ad_goods_back:
                finish();
                break;
            case R.id.ad_goods_search_delete:
                ad_goods_search.setText("");
                break;
            case R.id.ad_goods_scan_code:
                Intent intent = new Intent();
                intent.setClass(context, CaptureActivity.class);
                startActivityForResult(intent,111);
                break;
            case R.id.ad_goods_sort_sale:
                setSortTextAndIcon(ad_goods_sort_stock, R.color.color_262626, R.mipmap.home_hand_default);
                isAscStock = false;
                if (isAscSale) {// 升序
                    setSortTextAndIcon(ad_goods_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(ad_goods_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscSale = !isAscSale;
                saleSort();
                break;
            case R.id.ad_goods_sort_stock:
                setSortTextAndIcon(ad_goods_sort_sale, R.color.color_262626, R.mipmap.home_hand_default);
                isAscSale = false;

                if (isAscStock) {// 升序
                    setSortTextAndIcon(ad_goods_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(ad_goods_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscStock = !isAscStock;
                stockSort();
                break;
            case R.id.ad_goods_catalog:
                if (catalogPopupWindow != null) {
                    //显示阴影
                    catalog_shade.setVisibility(View.VISIBLE);
                    catalogPopupWindow.showAsDropDown(ad_goods_catalog);
                }
                break;
        }
    }

    /**
     * 初始化目录列表
     * @param catalogs
     */
    private void initCatalogPopupWindow(ArrayList<Catalog> catalogs) {
        Catalog catalog = new Catalog();
        catalog.catalog_goods_number = goodsListAdapter.getItemCount();
        catalog.catalog_name = "全部";
        catalog.catalog_id = "-1";
        catalog.fid = "-1";
        catalog.directory = "全部";
        catalog.productNum = goodsListAdapter.getItemCount() + "";
        selectCatalog = catalog;
        catalogs.add(0, catalog);
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog, is_vshop);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                ad_goods_catalog.setText(catalog.directory + "（" + catalog.productNum + "）");
                selectCatalog = catalog;
                search("");
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                catalog_shade.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 加载商品数据
     */
    private void loadGoodsData() {
        ad_goods_swipe.setRefreshing(true);
        if (is_vshop){
            VdianGoodsManager.getInstance().loadList(this, new IRequestCallBack<AllGoodsAndCatalog>() {
                @Override
                public void onResult(int tag, AllGoodsAndCatalog result) {
                    dismissLoading();
                    ad_goods_swipe.setRefreshing(false);
                    restorationSort();
                    refreshGoodsListData(result.list);
                    initCatalogPopupWindow(result.catalogs);
                    ad_goods_catalog.setText("全部（" + result.list.size() + "）");
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    ad_goods_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }
            });
        }else {
            RealGoodsManager.getInstance().loadGoodsAndCatalog(this, "dat", BaseManager.DESC, "", "", new IRequestCallBack<AllGoodsAndCatalog>() {
                @Override
                public void onResult(int tag, AllGoodsAndCatalog result) {
                    dismissLoading();
                    ad_goods_swipe.setRefreshing(false);
                    restorationSort();
                    refreshGoodsListData(result.list);
                    initCatalogPopupWindow(result.directory);
                    ad_goods_catalog.setText("全部（" + result.list.size() + "）");
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    ad_goods_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }
            });
        }
    }

    /**
     * 搜索
     */
    private void search(String s) {
        ArrayList<Goods> goodsDatas = null;
        if (is_vshop){
            goodsDatas = VdianGoodsManager.getInstance().search(this,s,selectCatalog);
        }else {
            goodsDatas = RealGoodsManager.getInstance().search(this, s, selectCatalog);
        }
        refreshGoodsListData(goodsDatas);
        ad_goods_recycle.scrollToPosition(0);
        if (goodsDatas == null || goodsDatas.size() == 0) {
            ad_goods_search_no_goods.setVisibility(View.VISIBLE);
            ad_goods_search_no_goods.setText("抱歉，找不到相关商品");
        } else {
            ad_goods_search_no_goods.setVisibility(View.GONE);
        }
    }


    /**
     * 复位排序视图
     */
    private void restorationSort() {
        setSortTextAndIcon(ad_goods_sort_stock, R.color.color_262626, R.mipmap.home_hand_default);
        setSortTextAndIcon(ad_goods_sort_sale, R.color.color_262626, R.mipmap.home_hand_default);
        isAscStock = false;
        isAscSale = false;
    }
    
    private void refreshGoodsListData(ArrayList<Goods> goodsDatas) {
        ad_goods_catalog.setText("全部（" + goodsDatas.size() + "）");
        ad_goods_search_no_goods.setVisibility(GONE);
        ad_goods_hint_no_goods.setVisibility(GONE);
        if (ListUtils.isNotEmpty(goodsDatas)) {
            ad_goods_no_goods.setVisibility(GONE);
        } else {
            ad_goods_no_goods.setVisibility(VISIBLE);
        }
        goodsListAdapter.notifyDataSetChanged(goodsDatas);
        ad_goods_recycle.scrollToPosition(0);
    }

    /**
     * 设置排序按钮颜色和图标
     * @param textView
     * @param colorResId
     * @param iconResId
     */
    private void setSortTextAndIcon(TextView textView, int colorResId, int iconResId) {
        textView.setTextColor(getColorValue(colorResId));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
    }

    /**
     * 按销量排序
     */
    private void saleSort() {
        if (is_vshop){
            VdianGoodsManager.getInstance().saleSort(isAscSale, goodsListAdapter.getItems(), new VdianGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    ad_goods_recycle.scrollToPosition(0);
                }
            });
        }else {
            RealGoodsManager.getInstance().saleSort(isAscSale, goodsListAdapter.getItems(), new RealGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    ad_goods_recycle.scrollToPosition(0);
                }
            });
        }
    }

    /**
     * 按库存排序
     */
    private void stockSort() {
        if (is_vshop){
            VdianGoodsManager.getInstance().stockSort(isAscStock, goodsListAdapter.getItems(), new VdianGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    ad_goods_recycle.scrollToPosition(0);
                }
            });
        }else {
            RealGoodsManager.getInstance().stockSort(isAscStock, goodsListAdapter.getItems(), new RealGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    ad_goods_recycle.scrollToPosition(0);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 111){
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        ad_goods_search.setText(TextUtils.trimToEmpty(result));
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.show(context,"扫描二维码失败");
                    }
                }
            }
        }
    }
}
