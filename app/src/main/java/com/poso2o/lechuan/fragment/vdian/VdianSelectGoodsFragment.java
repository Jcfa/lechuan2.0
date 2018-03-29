package com.poso2o.lechuan.fragment.vdian;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.VdianSelectGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager2;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 导入商品选商品界面
 * <p>
 * Created by Jaydon on 2018/3/16.
 */
public class VdianSelectGoodsFragment extends BaseFragment {

    /**
     * 销量
     */
    private TextView import_sort_sale;

    /**
     * 库存
     */
    private TextView import_sort_stock;

    /**
     * 目录
     */
    private TextView import_goods_catalog;

    /**
     * 全选
     */
    private TextView import_goods_all_select;

    /**
     * 数量
     */
    private TextView import_goods_select_num;

    /**
     * 导入
     */
    private TextView import_goods_import;

    /**
     * 列表
     */
    private SwipeRefreshLayout import_goods_swipe;
    private RecyclerView import_goods_recycle;
    private VdianSelectGoodsAdapter vdianSelectGoodsAdapter;
    private View import_goods_shade;

    /**
     * 下拉菜单
     */
    private CatalogPopupWindow catalogPopupWindow;

    /**
     * 选中的目录
     */
    private Catalog selectCatalog = new Catalog();
    private TotalBean goodsTotal = new TotalBean();//商品列表的total
    private int currentPage = 1;//当前第几页商品

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vdian_select_goods, container, false);
    }

    @Override
    public void initView() {
        import_sort_sale = findView(R.id.import_sort_sale);
        import_sort_stock = findView(R.id.import_sort_stock);
        import_goods_catalog = findView(R.id.import_goods_catalog);

        import_goods_swipe = findView(R.id.import_goods_swipe);
        import_goods_recycle = findView(R.id.import_goods_recycle);
        import_goods_shade = findView(R.id.import_goods_shade);
        import_goods_all_select = findView(R.id.import_goods_all_select);
        import_goods_select_num = findView(R.id.import_goods_select_num);
        import_goods_import = findView(R.id.import_goods_import);
        import_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                loadCatalogs();
                loadGoods();
            }
        });
    }

    @Override
    public void initData() {
        vdianSelectGoodsAdapter = new VdianSelectGoodsAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        import_goods_recycle.setLayoutManager(linearLayoutManager);
        import_goods_recycle.setAdapter(vdianSelectGoodsAdapter);
        import_goods_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!import_goods_recycle.canScrollVertically(1)) {//滚动到底部
                    if (goodsTotal.pages > currentPage) {
                        ++currentPage;
                        loadGoods();
                    }
                }
            }
        });
        loadGoods();
        loadCatalogs();
    }

    /**
     * 加载商品列表
     */
    public void loadGoods() {
        import_goods_swipe.setRefreshing(true);
        VdianGoodsManager2.getInstance().loadGoodsList((BaseActivity) context, selectCatalog.catalog_id, "", currentPage + "", "", new IRequestCallBack<GoodsBean>() {
            @Override
            public void onResult(int tag, GoodsBean result) {
                import_goods_swipe.setRefreshing(false);
                if (result != null && result.list != null) {
                    goodsTotal = result.total;
                    vdianSelectGoodsAdapter.notifyDatas(result.list);
                } else {
                    Toast.show(context, "加载商品失败");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                import_goods_swipe.setRefreshing(false);
                Toast.show(context, "加载商品失败");
            }
        });
    }

    /**
     * 加载商品目录列表
     */
    public void loadCatalogs() {
        VdianGoodsManager2.getInstance().loadCatalogList((BaseActivity) context, new IRequestCallBack<CatalogBean>() {
            @Override
            public void onResult(int tag, CatalogBean result) {
                if (result != null && result.list != null) {
                    initCatalogPopupWindow(result.list);
                } else {
                    Toast.show(context, "加载目录失败");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, "加载目录失败");
            }
        });
    }

//    private void loadGoods() {
//        showLoading("正在加载商品...");
//        RealGoodsManager.getInstance().loadGoodsAndCatalog((BaseActivity) context, "dat", "DESC", "", "", new IRequestCallBack() {
//
//            @Override
//            public void onResult(int tag, Object object) {
//                dismissLoading();
//                import_goods_swipe.setRefreshing(false);
//                AllGoodsAndCatalog allGoodsAndCatalog = (AllGoodsAndCatalog) object;
//                if (allGoodsAndCatalog != null) {
//                    vdianSelectGoodsAdapter.notifyDatas(allGoodsAndCatalog.list);
//                    initCatalogPopupWindow(allGoodsAndCatalog.directory);
//                }
//                restorationSort();
//            }
//
//            @Override
//            public void onFailed(int tag, String msg) {
//                dismissLoading();
//                import_goods_swipe.setRefreshing(false);
//                Toast.show(context, msg);
//            }
//        });
//    }

    @Override
    public void initListener() {
        vdianSelectGoodsAdapter.setOnItemClickListener(new VdianSelectGoodsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Goods item) {
                import_goods_select_num.setText(Integer.toString(vdianSelectGoodsAdapter.getSelects().size()));
            }
        });
        vdianSelectGoodsAdapter.setOnItemSelectListener(new VdianSelectGoodsAdapter.OnItemSelectListener() {

            @Override
            public void onItemSelect(boolean is_all_select) {
                import_goods_all_select.setSelected(is_all_select);
            }
        });

        import_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadGoods();
            }
        });

        import_sort_sale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setSortTextAndIcon(import_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
                isAscStock = false;
                if (isAscSale) {// 升序
                    setSortTextAndIcon(import_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(import_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscSale = !isAscSale;
                saleSort();
            }
        });

        import_sort_stock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setSortTextAndIcon(import_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
                isAscSale = false;

                if (isAscStock) {// 升序
                    setSortTextAndIcon(import_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(import_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscStock = !isAscStock;
                stockSort();
            }
        });

        import_goods_catalog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (catalogPopupWindow != null) {
                    //显示阴影
                    import_goods_shade.setVisibility(View.VISIBLE);
                    catalogPopupWindow.showAsDropDown(v);
                }
            }
        });

        import_goods_all_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                import_goods_all_select.setSelected(!import_goods_all_select.isSelected());
                vdianSelectGoodsAdapter.allSelect(import_goods_all_select.isSelected());
                import_goods_select_num.setText(Integer.toString(vdianSelectGoodsAdapter.getSelects().size()));
            }
        });

        import_goods_import.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                importGoods();
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
        catalog.productNum = vdianSelectGoodsAdapter.getItemCount() + "";
        import_goods_catalog.setText(catalog.catalog_name + "（" + catalog.productNum + "）");
        selectCatalog = catalog;
        catalogs.add(0, catalog);
        catalogPopupWindow = new CatalogPopupWindow(context, catalogs, selectCatalog, true);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                import_goods_catalog.setText(catalog.catalog_name + "（" + catalog.catalog_goods_number + "）");
                selectCatalog = catalog;
                loadGoods();
//                search("");
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                import_goods_shade.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 导入商品
     */
    private void importGoods() {
        ArrayList<Goods> selects = vdianSelectGoodsAdapter.getSelects();
        if (selects == null || selects.size() == 0) {
            Toast.show(context, "请选择商品");
            return;
        }
        JSONArray array = new JSONArray();
        for (int i = 0; i < selects.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("guid", selects.get(i).guid);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showLoading("正在导入商品...");
        WShopManager.getrShopManager().importGoods((BaseActivity) context, array.toString(), new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                ((BaseActivity) context).setResult(Activity.RESULT_OK);
                ((BaseActivity) context).finish();
                Toast.show(context, "成功导入商品");
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
     * 复位排序视图
     */
    private void restorationSort() {
        setSortTextAndIcon(import_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
        setSortTextAndIcon(import_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
        isAscStock = false;
        isAscSale = false;
    }

    /**
     * 按销量排序
     */
    private void saleSort() {
        RealGoodsManager.getInstance().saleSort(isAscSale, vdianSelectGoodsAdapter.getItems(), new RealGoodsManager.OnSortListener() {
            @Override
            public void onSort(ArrayList<Goods> goodsDatas) {
                vdianSelectGoodsAdapter.notifyDatas(goodsDatas);
                import_goods_recycle.scrollToPosition(0);
            }
        });
    }

    /**
     * 按库存排序
     */
    private void stockSort() {
        RealGoodsManager.getInstance().stockSort(isAscStock, vdianSelectGoodsAdapter.getItems(), new RealGoodsManager.OnSortListener() {
            @Override
            public void onSort(ArrayList<Goods> goodsDatas) {
                vdianSelectGoodsAdapter.notifyDatas(goodsDatas);
                import_goods_recycle.scrollToPosition(0);
            }
        });
    }
}
