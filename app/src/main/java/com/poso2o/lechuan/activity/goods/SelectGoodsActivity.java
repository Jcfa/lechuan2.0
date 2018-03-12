package com.poso2o.lechuan.activity.goods;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.SelectGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.distributor.DistShop;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.distributor.DistributorManager;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.popubwindow.ShopPopupWindow;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.recycler.OnSlideToBottomListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.RoundAngleImage;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;
import static com.poso2o.lechuan.base.BaseManager.NEXT;

/**
 * 选择商品
 * Created by Jaydon on 2017/12/2.
 */
public class SelectGoodsActivity extends BaseActivity implements OnClickListener {

    /**
     * 标题上的店铺视图
     */
    private View select_goods_shop;

    /**
     * 店铺
     */
    private ShopPopupWindow shopPopupWindow;
    private DistShop selectDistShop;// 选中的店铺
    private RoundAngleImage select_goods_shop_icon;// 图标
    private TextView select_goods_shop_name;// 名称

    /**
     * 搜索框
     */
    private EditText select_goods_search_edit;
    private View select_goods_search_delete;

    /**
     * 销量排序
     */
    private TextView select_goods_sort_sale;

    /**
     * 佣金排序
     */
    private TextView select_goods_sort_brokerage;

    /**
     * 选中的排序按钮
     */
    private TextView selectSort;

    /**
     * 目录
     */
    private TextView select_goods_catalog;
    private CatalogPopupWindow catalogPopupWindow;
    private Catalog selectCatalog;
    private View select_goods_shade;// 弹出下拉列表时的阴影

    /**
     * 无数据时的提示文本
     */
    private TextView select_goods_hint;

    /**
     * 列表
     */
    private RecyclerView select_goods_list;
    private SwipeRefreshLayout select_goods_swipe;
    private SelectGoodsAdapter selectGoodsAdapter;

    /**
     * 目录ID
     */
    private String catalogId = "-1";

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
        return R.layout.activity_select_goods;
    }

    @Override
    protected void initView() {
        select_goods_shop = findView(R.id.select_goods_shop);
        select_goods_shop_icon = findView(R.id.select_goods_shop_icon);
        select_goods_shop_name = findView(R.id.select_goods_shop_name);
        select_goods_search_edit = findView(R.id.select_goods_search_edit);
        select_goods_search_delete = findView(R.id.select_goods_search_delete);
        select_goods_sort_sale = findView(R.id.select_goods_sort_sale);
        select_goods_sort_brokerage = findView(R.id.select_goods_sort_brokerage);
        select_goods_catalog = findView(R.id.select_goods_catalog);
        select_goods_shade = findView(R.id.select_goods_shade);
        select_goods_hint = findView(R.id.select_goods_hint);
        select_goods_list = findView(R.id.select_goods_list);
        select_goods_list.setLayoutManager(new LinearLayoutManager(this));
        select_goods_swipe = findView(R.id.select_goods_swipe);
        select_goods_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    protected void initData() {
        selectGoodsAdapter = new SelectGoodsAdapter(this);
        select_goods_list.setAdapter(selectGoodsAdapter);
        showLoading();
        select_goods_swipe.setRefreshing(true);
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
            loadDistShop();
        } else {
            select_goods_shop.setVisibility(GONE);
            setTitle(R.string.title_select_goods);
            loadCatalogData();
            loadGoodsData(FIRST);
        }
    }

    private void loadDistShop() {
        DistributorManager.getInstance().loadShopList(this, new DistributorManager.OnLoadShopListCallback() {

            @Override
            public void onSucceed(ArrayList<DistShop> list) {
                initShopPopupWindow(list);
                loadCatalogData();
                loadGoodsData(FIRST);
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(activity, failMsg);
                select_goods_swipe.setRefreshing(false);
                dismissLoading();
            }
        });
    }

    private void loadCatalogData() {
        VdianCatalogManager.getInstance().loadList(this, shop_id, new IRequestCallBack<CatalogBean>() {

            @Override
            public void onResult(int tag, CatalogBean catalogBean) {
                Catalog allCatalog = new Catalog();
                allCatalog.catalog_id = "-1";
                allCatalog.catalog_name = getString(R.string.all);
                for (Catalog catalog : catalogBean.list) {
                    allCatalog.catalog_goods_number += catalog.catalog_goods_number;
                }
                catalogBean.list.add(0, allCatalog);
                if (selectCatalog == null) {
                    selectCatalog = allCatalog;
                } else {
                    for (Catalog catalog : catalogBean.list) {
                        if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
                            selectCatalog = catalog;
                        }
                    }
                }
                select_goods_catalog.setText(selectCatalog.getNameAndNum());
                initCatalogPopupWindow(catalogBean.list);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
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
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                select_goods_catalog.setText(catalog.getNameAndNum());
                selectCatalog = catalog;
                catalogId = catalog.catalog_id;
                select_goods_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                select_goods_shade.setVisibility(GONE);
            }
        });
    }

    /**
     * 初始化店铺列表
     */
    private void initShopPopupWindow(ArrayList<DistShop> shopDatas) {
        if (selectDistShop == null) {
            refreshShopView(shopDatas.get(0));
        }
        shopPopupWindow = new ShopPopupWindow(this, shopDatas, selectDistShop);
        shopPopupWindow.setOnItemClickListener(new ShopPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(DistShop shop) {
                refreshShopView(shop);
                showLoading();
                select_goods_swipe.setRefreshing(true);
                loadCatalogData();
                loadGoodsData(FIRST);
            }
        });

        // 窗口撤销监听
        shopPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                select_goods_shade.setVisibility(GONE);
            }
        });
    }

    private void refreshShopView(DistShop shop) {
        shop_id = shop.shop_id;
        selectDistShop = shop;
        Glide.with(activity).load(shop.shop_logo).into(select_goods_shop_icon);
        select_goods_shop_name.setText(shop.shop_name);
    }

    @Override
    protected void initListener() {
        selectGoodsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Goods>() {

            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        select_goods_hint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                select_goods_hint.setVisibility(GONE);
                select_goods_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
            }
        });

        findView(R.id.iv_back).setOnClickListener(this);
        findView(R.id.select_goods_search).setOnClickListener(this);
        select_goods_search_delete.setOnClickListener(this);
        select_goods_shop.setOnClickListener(this);
        select_goods_sort_sale.setOnClickListener(this);
        select_goods_sort_brokerage.setOnClickListener(this);
        select_goods_catalog.setOnClickListener(this);

        select_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadGoodsData(FIRST);
            }
        });

        select_goods_list.addOnScrollListener(new OnSlideToBottomListener() {

            @Override
            protected void slideToBottom() {
                if (selectGoodsAdapter.getItemCount() >= 20 && !select_goods_swipe.isRefreshing()) {
                    loadGoodsData(NEXT);
                }
            }
        });

        select_goods_search_edit.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                select_goods_search_delete.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }
        });

        select_goods_search_edit.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    search(select_goods_search_edit.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (select_goods_search_edit.getVisibility() == VISIBLE) {
            hideSearch();
            select_goods_search_edit.setText("");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                if (select_goods_search_edit.getVisibility() == VISIBLE) {
                    hideSearch();
                    select_goods_search_edit.setText("");
                } else {
                    finish();
                }
                break;

            case R.id.select_goods_shop:// 店铺
                if (shopPopupWindow != null) {
                    // 显示阴影
                    select_goods_shade.setVisibility(VISIBLE);
                    shopPopupWindow.showAsDropDown(v);
                }
                break;

            case R.id.select_goods_search:// 搜索
                if (select_goods_search_edit.getVisibility() == VISIBLE) {
                    search(select_goods_search_edit.getText().toString());
                } else {
                    showSearch();
                }
                break;

            case R.id.select_goods_search_delete:// 清空搜索
                select_goods_search_edit.setText("");
                keywords = "";
                select_goods_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
                break;

            case R.id.select_goods_sort_sale:// 销量
                orderByName = VdianGoodsManager.SORT_TYPE_SALE_NUMBER;
                sort(select_goods_sort_sale);
                break;

            case R.id.select_goods_sort_brokerage:// 佣金
                orderByName = VdianGoodsManager.SORT_TYPE_COMMISSION;
                sort(select_goods_sort_brokerage);
                break;

            case R.id.select_goods_catalog:// 目录
                if (catalogPopupWindow != null) {
                    // 显示阴影
                    select_goods_shade.setVisibility(VISIBLE);
                    catalogPopupWindow.showAsDropDown(v);
                }
                break;
        }
    }

    private void showSearch() {
        select_goods_search_edit.setVisibility(VISIBLE);
        select_goods_search_edit.requestFocus();
        if (select_goods_search_edit.getText().length() > 0) {
            select_goods_search_delete.setVisibility(VISIBLE);
        }
        KeyboardUtils.showSoftInput(select_goods_search_edit);
    }

    private void hideSearch() {
        search("");
        select_goods_search_edit.setVisibility(GONE);
        select_goods_search_delete.setVisibility(GONE);
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
        select_goods_swipe.setRefreshing(true);
        loadGoodsData(FIRST);
    }

    /**
     * 设置排序按钮颜色和图标
     *
     * @param textView
     * @param colorResId
     * @param iconResId
     */
    private void setSortTextAndIcon(TextView textView, int colorResId, int iconResId) {
        textView.setTextColor(getColorValue(colorResId));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
    }

    /**
     * 搜索商品
     */
    private void search(String s) {
        select_goods_search_edit.clearFocus();
        KeyboardUtils.hideSoftInput(activity);
        keywords = s;
        select_goods_swipe.setRefreshing(true);
        loadGoodsData(FIRST);
    }

    private void loadGoodsData(final int pageType) {
        select_goods_hint.setVisibility(GONE);
        VdianGoodsManager.getInstance().query(this, shop_id, catalogId, orderByName, sort, keywords, pageType, new IRequestCallBack<ArrayList<Goods>>() {

            @Override
            public void onResult(int tag, ArrayList<Goods> goodsDatas) {
                if (pageType == FIRST) {
                    if (ListUtils.isEmpty(goodsDatas)) {
                        select_goods_hint.setVisibility(VISIBLE);
                        select_goods_hint.setText(R.string.hint_load_goods_null);
                    } else {
                        select_goods_hint.setVisibility(GONE);
                    }
                    selectGoodsAdapter.notifyDataSetChanged(goodsDatas);
                } else {
                    selectGoodsAdapter.addItems(goodsDatas);
                }
                select_goods_swipe.setRefreshing(false);
            }

            @Override
            public void onFailed(int tag, String msg) {
                if (pageType == FIRST) {
                    select_goods_hint.setVisibility(VISIBLE);
                    select_goods_hint.setText(R.string.hint_load_goods_fail);
                }
                Toast.show(activity, msg);
                select_goods_swipe.setRefreshing(false);
            }
        });
    }
}