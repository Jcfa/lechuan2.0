package com.poso2o.lechuan.fragment.vdian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.wshop.VdianActivity;
import com.poso2o.lechuan.activity.wshop.VdianImportGoodsActivity;
import com.poso2o.lechuan.activity.wshop.VdianShopInfoActivity;
import com.poso2o.lechuan.adapter.GoodsListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.TipsNoAuthorDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;

/**
 * 微店商品列表
 *
 * Created by Jaydon on 2018/3/14.
 */
public class VdianGoodsFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_SHOP_INFO = 11;

    /**
     * 商户头像
     */
    private ImageView vdian_info_icon;

    /**
     * 商户信息布局
     */
    private View vdian_info_group;

    /**
     * 商户信息提示语
     */
    private View vdian_info_hint;

    /**
     * 商户信息主视图
     */
    private View vdian_info_main;

    /**
     * 商户名称
     */
    private TextView vdian_info_name;

    /**
     * 商户描述
     */
    private TextView vdian_info_describe;

    /**
     * 销量
     */
    private TextView vdian_sort_sale;

    /**
     * 库存
     */
    private TextView vdian_sort_stock;

    /**
     * 选中的排序按钮
     */
    private TextView selectSort;

    /**
     * 目录
     */
    private TextView vdian_catalog;

    /**
     * 无商品提示
     */
    private TextView vdian_goods_hint;

    /**
     * 添加商品
     */
    private TextView vdian_goods_add;

    /**
     * 列表
     */
    private SwipeRefreshLayout vdian_swipe;
    private RecyclerView vdian_recycle;
    private GoodsListAdapter goodsListAdapter;
    private View vdian_shade;

    /**
     * 店铺数据
     */
    private ShopData shopData;

    /**
     * 下拉菜单
     */
    private CatalogPopupWindow catalogPopupWindow;

    /**
     * 选中的目录
     */
    private Catalog selectCatalog;

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
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vdian_goods, container, false);
    }

    @Override
    public void initView() {
        vdian_info_icon = findView(R.id.vdian_info_icon);
        vdian_info_group = findView(R.id.vdian_info_group);
        vdian_info_hint = findView(R.id.vdian_info_hint);
        vdian_info_main = findView(R.id.vdian_info_main);
        vdian_info_name = findView(R.id.vdian_info_name);
        vdian_info_describe = findView(R.id.vdian_info_describe);

        vdian_goods_hint = findView(R.id.vdian_goods_hint);
        vdian_goods_add = findView(R.id.vdian_goods_add);

        vdian_sort_sale = findView(R.id.vdian_sort_sale);
        vdian_sort_stock = findView(R.id.vdian_sort_stock);
        vdian_catalog = findView(R.id.vdian_catalog);
        vdian_swipe = findView(R.id.vdian_swipe);
        vdian_recycle = findView(R.id.vdian_recycle);
        vdian_shade = findView(R.id.vdian_shade);

        vdian_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        goodsListAdapter = new GoodsListAdapter(context, true);
        goodsListAdapter.setHaveBottom(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vdian_recycle.setLayoutManager(linearLayoutManager);
        vdian_recycle.setAdapter(goodsListAdapter);

//        shopData = (ShopData) getIntent().getExtras().get(RShopMainActivity.DATA_SHOP);
//        refreshShopData();
        showLoading();
        loadShopInfo();
    }

    private void loadCatalogData() {
        VdianCatalogManager.getInstance().loadList((BaseActivity) context, shop_id, new IRequestCallBack<CatalogBean>() {

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
                vdian_catalog.setText(selectCatalog.getNameAndNum());
                initCatalogPopupWindow(catalogBean.list);
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
        catalogPopupWindow = new CatalogPopupWindow(context, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                vdian_catalog.setText(catalog.getNameAndNum());
                selectCatalog = catalog;
                catalog_id = catalog.catalog_id;
                vdian_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                vdian_shade.setVisibility(GONE);
            }
        });
    }

    /**
     * 加载商品数据
     */
    public void loadGoodsData(final int pageType) {
        vdian_swipe.setRefreshing(true);
        vdian_goods_hint.setVisibility(GONE);
        vdian_goods_add.setVisibility(GONE);
        VdianGoodsManager.getInstance().query((BaseActivity) context, shop_id, catalog_id, orderByName, sort, keywords, pageType, new IRequestCallBack<ArrayList<Goods>>() {

            @Override
            public void onResult(int tag, ArrayList<Goods> goodsDatas) {
                if (pageType == FIRST) {
                    if (ListUtils.isEmpty(goodsDatas)) {
                        vdian_goods_hint.setVisibility(VISIBLE);
                        vdian_goods_add.setVisibility(VISIBLE);
                        vdian_goods_hint.setText("请点击下方或右上角的按钮，添加商品至微店.");
                    } else {
                        vdian_goods_hint.setVisibility(GONE);
                    }
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                } else {
                    goodsListAdapter.addItems(goodsDatas);
                }
                vdian_swipe.setRefreshing(false);
            }

            @Override
            public void onFailed(int tag, String msg) {
                if (pageType == FIRST) {
                    vdian_goods_hint.setVisibility(VISIBLE);
                    vdian_goods_hint.setText(R.string.hint_load_goods_fail);
                }
                Toast.show(context, msg);
                vdian_swipe.setRefreshing(false);
            }
        });
    }

    /**
     * 加载店铺数据
     */
    private void loadShopInfo() {
        WShopManager.getrShopManager().wShopInfo((BaseActivity) context, new IRequestCallBack<ShopData>() {

            @Override
            public void onResult(int tag, ShopData result) {
                if (result == null) {
                    Toast.show(context, "加载店铺数据出错");
                } else {
                    shopData = result;
                    shop_id = NumberUtils.toLong(shopData.shop_id);
                    refreshShopData();
//                    if (shopData.has_bank_binding == 1) {
                        // 加载目录和商品数据
                        loadCatalogData();
                        vdian_swipe.setRefreshing(true);
                        loadGoodsData(FIRST);
//                    } else {
//                        showSetupBindAccountsDialog();
//                    }
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
     * 显示绑定收款帐号
     */
    private void showSetupBindAccountsDialog() {

    }

    /**
     * 刷新店铺数据
     */
    private void refreshShopData() {
        Glide.with(this).load(shopData.shop_logo).placeholder(R.mipmap.background_image).into(vdian_info_icon);
        vdian_info_name.setText(shopData.shop_name);
        vdian_info_describe.setText(shopData.shop_introduction);
    }

    @Override
    public void initListener() {
        vdian_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadShopInfo();
//                loadGoodsData(FIRST);
            }
        });
        vdian_info_group.setOnClickListener(this);
        vdian_goods_hint.setOnClickListener(this);
        vdian_goods_add.setOnClickListener(this);
        vdian_catalog.setOnClickListener(this);
        vdian_sort_sale.setOnClickListener(this);
        vdian_sort_stock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vdian_info_group:// 店铺信息
                Bundle data = new Bundle();
                data.putSerializable(Constant.SHOP, shopData);
                startActivityForResult(VdianShopInfoActivity.class, data, REQUEST_SHOP_INFO);
                break;

            case R.id.vdian_goods_hint:// 无数据提示
                vdian_goods_hint.setVisibility(GONE);
                vdian_swipe.setRefreshing(true);
                loadGoodsData(FIRST);
                break;

            case R.id.vdian_goods_add:// 导入商品
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY) == 0){
                    TipsNoAuthorDialog noAuthorDialog = new TipsNoAuthorDialog(context);
                    noAuthorDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(context, VdianImportGoodsActivity.class);
                ((BaseActivity) context).startActivityForResult(intent, VdianActivity.REQUEST_IMPORT_CODE);
                break;

            case R.id.vdian_catalog:// 目录
                if (catalogPopupWindow != null) {
                    // 显示阴影
                    vdian_shade.setVisibility(VISIBLE);
                    catalogPopupWindow.showAsDropDown(v);
                }
                break;

            case R.id.vdian_sort_sale:// 销量
                orderByName = VdianGoodsManager.SORT_TYPE_SALE_NUMBER;
                sort(vdian_sort_sale);
                break;

            case R.id.vdian_sort_stock:// 库存
                orderByName = VdianGoodsManager.SORT_TYPE_STOCK;
                sort(vdian_sort_stock);
                break;
        }
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
        vdian_swipe.setRefreshing(true);
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
        textView.setTextColor(ContextCompat.getColor(context, colorResId));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
    }

    /**
     * 搜索商品
     */
    public void search(String keywords) {
        this.keywords = keywords;
        loadGoodsData(FIRST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SHOP_INFO:
                    shopData = (ShopData) data.getSerializableExtra(Constant.SHOP);
                    refreshShopData();
                    break;
            }
        }
    }
}
