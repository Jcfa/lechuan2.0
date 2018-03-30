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
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager2;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.tool.recycler.OnSlideToBottomListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;

/**
 * Created by mr zhang on 2017/10/31.
 */

public class AdGoodsActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //返回类型 ：0是店铺，1是商品
    public static final String AD_TYPE = "ad_type";
    //返回商品
    public static final String AD_ARTICLE_GOODS = "ad_article_goods";

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
    
    //列表数据适配器
    private GoodsListAdapter goodsListAdapter;

    /**
     * 选中的排序按钮
     */
    private TextView selectSort;

    /**
     * 选中的目录
     */
    private Catalog selectCatalog = new Catalog();
    private int currentPage = 1;//当前商品页
    private ArrayList<Goods> goodsList = new ArrayList<>();
    private TotalBean goodsTotal = new TotalBean();

    /**
     * 目录ID
     */
    private String catalog_id = "-1";

    /**
     * 排序类型
     */
    private String orderByName = "";

    /**
     * 排序方向
     */
    private String sort = BaseManager.ASC;

    /**
     * 搜索关键词
     */
    private String keywords = "";

    /**
     * 店铺ID
     */
    private long shop_id = 0;

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

        goodsListAdapter = new GoodsListAdapter(context,true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ad_goods_recycle.setLayoutManager(linearLayoutManager);
        ad_goods_recycle.setAdapter(goodsListAdapter);

        showLoading();
        loadCatalogData();
        loadGoodsData(FIRST);
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
                loadGoodsData(FIRST);
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
                keywords = ad_goods_search.getText().toString();
                loadGoodsData(FIRST);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ad_goods_recycle.addOnScrollListener(new OnSlideToBottomListener() {
            @Override
            protected void slideToBottom() {
                if (goodsListAdapter.getItemCount() >= 20 && !ad_goods_swipe.isRefreshing()){
                    currentPage ++;
                    loadGoodsData(currentPage);
                }
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
                orderByName = VdianGoodsManager.SORT_TYPE_SALE_NUMBER;
                sort(ad_goods_sort_sale);
                break;
            case R.id.ad_goods_sort_stock:
                orderByName = VdianGoodsManager.SORT_TYPE_STOCK;
                sort(ad_goods_sort_stock);
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

    private void loadCatalogData() {
        VdianGoodsManager2.getInstance().loadCatalogList(this, "1", new IRequestCallBack<CatalogBean>() {
            @Override
            public void onResult(int tag, CatalogBean catalogBean) {
                dismissLoading();
                if (catalogBean != null && catalogBean.list != null) {
                    initCatalogPopupWindow(catalogBean.list);
                } else {
                    Toast.show(context, "加载目录失败");
                }
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                dismissLoading();
            }
        });
    }

    /**
     * 初始化目录列表
     *
     * @param catalogs
     */
    private void initCatalogPopupWindow(ArrayList<Catalog> catalogs) {
        Catalog catalog = new Catalog();
        catalog.fid = "-1";
        catalog.directory = "全部";
        catalog.catalog_name = "全部";
        catalog.catalog_goods_number = getGoodsCount(catalogs);
        ad_goods_catalog.setText(catalog.catalog_name + "（" + catalog.catalog_goods_number + "）");
        selectCatalog = catalog;
        catalogs.add(0, catalog);
        catalogPopupWindow = new CatalogPopupWindow(context, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                ad_goods_catalog.setText(catalog.getNameAndNum());
                selectCatalog = catalog;
                catalog_id = catalog.catalog_id;
                ad_goods_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                catalog_shade.setVisibility(GONE);
            }
        });
    }

    /**
     * 全部目录总商品数
     * @param catalogs
     * @return
     */
    private int getGoodsCount(ArrayList<Catalog> catalogs){
        int count=0;
        for(Catalog catalog:catalogs){
            count+=catalog.catalog_goods_number;
        }
        return count;
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
     * 排序
     */
    private void sort(TextView tv) {
        if (selectSort != tv) {
            sort = BaseManager.ASC;
            if (selectSort != null) {
                setSortTextAndIcon(selectSort, R.color.textBlack, R.mipmap.home_hand_default);
            }
            selectSort = tv;
        } else {
            sort = sort.equals(BaseManager.ASC) ? BaseManager.DESC : BaseManager.ASC;
        }
        if (sort.equals(BaseManager.ASC)) {
            setSortTextAndIcon(tv, R.color.textGreen, R.mipmap.home_hand_down);
        } else {
            setSortTextAndIcon(tv, R.color.textGreen, R.mipmap.home_hand_up);
        }
        ad_goods_swipe.setRefreshing(true);
        loadGoodsData(FIRST);
    }

    /**
     * 加载商品数据
     */
    public void loadGoodsData(final int pageType) {
        currentPage = pageType;
        ad_goods_swipe.setRefreshing(true);
        VdianGoodsManager2.getInstance().loadGoodsList((BaseActivity) context, orderByName,sort,selectCatalog.catalog_id, keywords, currentPage + "", "1", new IRequestCallBack<GoodsBean>() {
            @Override
            public void onResult(int tag, GoodsBean result) {
                ad_goods_swipe.setRefreshing(false);
                if (currentPage == FIRST) {
                    goodsList.clear();
                }
                if (result != null && result.list != null) {
                    goodsList.addAll(result.list);
                }
                goodsListAdapter.notifyDataSetChanged(goodsList);
            }

            @Override
            public void onFailed(int tag, String msg) {
                ad_goods_swipe.setRefreshing(false);
            }
        });
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
