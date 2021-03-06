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
import com.poso2o.lechuan.activity.vdian.VdianActivity;
import com.poso2o.lechuan.activity.vdian.VdianImportGoodsActivity;
import com.poso2o.lechuan.activity.vdian.VdianShopInfoActivity;
import com.poso2o.lechuan.activity.wshop.WGoodsDetailActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.GoodsListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsBean;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.TipsNoAuthorDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager2;
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
 * <p>
 * Created by Jaydon on 2018/3/14.
 */
public class VdianGoodsFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_SHOP_INFO = 11;

    private static final int REQUEST_GOODS_DETAILS = 12;

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
        vdian_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!vdian_recycle.canScrollVertically(1)) {//滚动到底部
                    if (goodsTotal.pages > currentPage) {
                        ++currentPage;
                        loadGoodsList(currentPage);
                    }
                }
            }
        });
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
//        showLoading();
        loadShopInfo();
        loadGoodsList(FIRST);
    }

//    private void loadCatalogData() {
//        VdianCatalogManager.getInstance().loadList((BaseActivity) context, shop_id, new IRequestCallBack<CatalogBean>() {
//            @Override
//            public void onResult(int tag, CatalogBean catalogBean) {
//                Catalog allCatalog = new Catalog();
//                allCatalog.catalog_id = "-1";
//                allCatalog.catalog_name = getString(R.string.all);
//                for (Catalog catalog : catalogBean.list) {
//                    allCatalog.catalog_goods_number += catalog.catalog_goods_number;
//                }
//                catalogBean.list.add(0, allCatalog);
//                if (selectCatalog == null) {
//                    selectCatalog = allCatalog;
//                } else {
//                    for (Catalog catalog : catalogBean.list) {
//                        if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
//                            selectCatalog = catalog;
//                        }
//                    }
//                }
//                vdian_catalog.setText(selectCatalog.getNameAndNum());
//                initCatalogPopupWindow(catalogBean.list);
//                dismissLoading();
//            }
//
//            @Override
//            public void onFailed(int tag, String msg) {
//                Toast.show(context, msg);
//                dismissLoading();
//            }
//        });
//    }

    /**
     * 加载商品目录
     */
    public void loadCatalogList() {
        VdianGoodsManager2.getInstance().loadCatalogList((BaseActivity) context, "1", new IRequestCallBack<CatalogBean>() {
            @Override
            public void onResult(int tag, CatalogBean result) {
                dismissLoading();
//                Catalog allCatalog = new Catalog();
//                allCatalog.catalog_id = "-1";
//                allCatalog.catalog_name = getString(R.string.all);
//                for (Catalog catalog : result.list) {
//                    allCatalog.catalog_goods_number += catalog.catalog_goods_number;
//                }
//                result.list.add(0, allCatalog);
//                if (selectCatalog == null) {
//                    selectCatalog = allCatalog;
//                } else {
//                    for (Catalog catalog : result.list) {
//                        if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
//                            selectCatalog = catalog;
//                        }
//                    }
//                }
//                vdian_catalog.setText(selectCatalog.getNameAndNum());
                if (result != null && result.list != null) {
                    initCatalogPopupWindow(result.list);
                } else {
                    Toast.show(context, "加载目录失败");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
            }
        });
//        VdianCatalogManager.getInstance().loadList((BaseActivity) context, shop_id, new IRequestCallBack<CatalogBean>() {
//            @Override
//            public void onResult(int tag, CatalogBean catalogBean) {
//                Catalog allCatalog = new Catalog();
//                allCatalog.catalog_id = "-1";
//                allCatalog.catalog_name = getString(R.string.all);
//                for (Catalog catalog : catalogBean.list) {
//                    allCatalog.catalog_goods_number += catalog.catalog_goods_number;
//                }
//                catalogBean.list.add(0, allCatalog);
//                if (selectCatalog == null) {
//                    selectCatalog = allCatalog;
//                } else {
//                    for (Catalog catalog : catalogBean.list) {
//                        if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
//                            selectCatalog = catalog;
//                        }
//                    }
//                }
//                vdian_catalog.setText(selectCatalog.getNameAndNum());
//                initCatalogPopupWindow(catalogBean.list);
//                dismissLoading();
//            }
//
//            @Override
//            public void onFailed(int tag, String msg) {
//                Toast.show(context, msg);
//                dismissLoading();
//            }
//        });
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
        vdian_catalog.setText(catalog.catalog_name + "（" + catalog.catalog_goods_number + "）");
        selectCatalog = catalog;
        catalogs.add(0, catalog);
        catalogPopupWindow = new CatalogPopupWindow(context, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                vdian_catalog.setText(catalog.getNameAndNum());
                selectCatalog = catalog;
                catalog_id = catalog.catalog_id;
                vdian_swipe.setRefreshing(true);
                loadGoodsList(FIRST);
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
     * 全部目录总商品数
     *
     * @param catalogs
     * @return
     */
    private int getGoodsCount(ArrayList<Catalog> catalogs) {
        int count = 0;
        for (Catalog catalog : catalogs) {
            count += catalog.catalog_goods_number;
        }
        return count;
    }

    /**
     * 加载商品列表
     */
    public void loadGoodsList(int page) {
        currentPage = page;
        vdian_swipe.setRefreshing(true);
        VdianGoodsManager2.getInstance().loadGoodsList((BaseActivity) context, orderByName, sort, selectCatalog.catalog_id, keywords, currentPage + "", "1", new IRequestCallBack<GoodsBean>() {
            @Override
            public void onResult(int tag, GoodsBean result) {
                vdian_swipe.setRefreshing(false);
                if (currentPage == FIRST) {
                    goodsList.clear();
                }
                if (result != null && result.list != null) {
                    if (currentPage == FIRST) {
                        goodsTotal = result.total;
                    }
                    goodsList.addAll(result.list);
                }
                if(goodsList.size()>0){
                    vdian_goods_add.setVisibility(GONE);
                    findView(R.id.vdian_addgoods_hint).setVisibility(GONE);
                }else{
                    vdian_goods_add.setVisibility(VISIBLE);
                    findView(R.id.vdian_addgoods_hint).setVisibility(VISIBLE);
                }
                goodsListAdapter.notifyDataSetChanged(goodsList);
            }

            @Override
            public void onFailed(int tag, String msg) {
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
                    loadCatalogList();
                    vdian_swipe.setRefreshing(true);
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
                loadGoodsList(FIRST);
            }
        });

        goodsListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Goods>() {

            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.setClass(context, WGoodsDetailActivity.class);
                intent.putExtra(Constant.DATA, item);
                intent.putExtra(Constant.TYPE, 2);
                startActivityForResult(intent, REQUEST_GOODS_DETAILS);
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
//                loadGoodsData(FIRST);
                loadGoodsList(FIRST);
                break;

            case R.id.vdian_goods_add:// 导入商品
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
        loadGoodsList(FIRST);
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
        loadGoodsList(FIRST);
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

                case REQUEST_GOODS_DETAILS:
                    vdian_swipe.setRefreshing(true);
                    loadCatalogList();
                    loadGoodsList(FIRST);
                    break;
            }
        }
    }
}
