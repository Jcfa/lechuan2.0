package com.poso2o.lechuan.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.print.PrintBarcodeActivity;
import com.poso2o.lechuan.activity.realshop.RCatalogActivity;
import com.poso2o.lechuan.activity.wshop.WEditGoodsActivity;
import com.poso2o.lechuan.activity.wshop.WGoodsDetailActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.GoodsListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.OldSpec;
import com.poso2o.lechuan.bean.printer.GoodsBarcodePrintData;
import com.poso2o.lechuan.bean.printer.GoodsDetailsImgsData;
import com.poso2o.lechuan.bean.printer.GoodsDetailsNumsData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.RGoodsDeleteDialog;
import com.poso2o.lechuan.dialog.TransferCatalogDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealCatalogManager;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by mr zhang on 2017/11/30.
 */
public class RGoodsActivity extends BaseActivity {

    private static final int REQUEST_CODE = 11;

    private static final int REQUEST_PRINT = 12;

    private static final int REQUEST_ADD_GOODS = 13;

    private static final int REQUEST_GOODS_DETAILS = 14;

    private static final int REQUEST_CATALOG = 15;

    private Context context;

    /**
     * 返回
     */
    private ImageView home_back;

    /**
     * 全选
     */
    private ImageView home_all_select;

    /**
     * 排序选项卡
     */
    private TextView home_sort_sale;// 销量
    private TextView home_sort_stock;// 库存
    private TextView home_goods_catalog;// 商品分类
    private View home_shade;// 弹出下拉列表时的阴影

    /**
     * 检索
     */
    private EditText home_goods_search;
    private ImageView home_search_text_delete;
    private TextView home_search_no_goods;
    private TextView home_group_no_goods;

    /**
     * 列表数据
     */
    private RecyclerView goodsRecyclerView;
    private GoodsListAdapter goodsListAdapter;

    /**
     * 列表刷新数据
     */
    private SwipeRefreshLayout goodsDataRefresh;

    /**
     * 批量编辑
     */
    private TextView home_manage_goods;
    private LinearLayout home_foot_second;
    private LinearLayout home_foot_main;

    /**
     * 批量删除
     */
    private View home_covering;// 弹出dialog覆盖物
    private RGoodsDeleteDialog goodsDeleteDialog;
    private LinearLayout home_foot_second_delete;

    /**
     * 商品数量为0显示界面
     */
    private TextView home_hint_no_goods;

    /**
     * 打印
     */
    private LinearLayout home_goods_print;

    /**
     * 转移目录
     */
    private TextView home_transfer_catalog;

    /**
     * 添加商品
     */
    private TextView home_add_goods;

    /**
     * 扫码
     */
    private ImageView home_scan_code;

    /**
     * 下拉菜单
     */
    private CatalogPopupWindow catalogPopupWindow;

    /**
     * 选中的目录
     */
    private Catalog selectCatalog;

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    /**
     * 店铺类型：实体店，微店
     */
    private boolean isVdian = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods;
    }

    @Override
    protected void initView() {

        context = this;

        // 筛选选项卡
        home_sort_sale = (TextView) findViewById(R.id.home_sort_sale);
        home_sort_stock = (TextView) findViewById(R.id.home_sort_stock);
        home_goods_catalog = (TextView) findViewById(R.id.home_goods_catalog);
        home_shade = findViewById(R.id.home_shade);

        // 商品列表
        goodsRecyclerView = (RecyclerView) findViewById(R.id.home_goods_recycle);
        goodsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // 刷新列表商品
        goodsDataRefresh = (SwipeRefreshLayout) findViewById(R.id.home_swipe);
        goodsDataRefresh.setColorSchemeColors(Color.RED);

        // 退出编辑
        home_back = (ImageView) findViewById(R.id.home_back);

        // 全选
        home_all_select = (ImageView) findViewById(R.id.home_all_select);

        // 检索
        home_goods_search = (EditText) findViewById(R.id.home_goods_search);
        home_search_text_delete = (ImageView) findViewById(R.id.home_search_text_delete);
        home_search_text_delete.setVisibility(View.INVISIBLE);

        home_search_no_goods = (TextView) findViewById(R.id.home_search_no_goods);
        home_group_no_goods = (TextView) findViewById(R.id.home_group_no_goods);

        // 批量编辑
        home_manage_goods = (TextView) findViewById(R.id.home_manage_goods);
        home_foot_second = (LinearLayout) findViewById(R.id.home_foot_second);
        home_foot_main = (LinearLayout) findViewById(R.id.home_foot_main);

        // 批量删除
        home_covering = findViewById(R.id.home_covering); // 覆盖物
        home_foot_second_delete = (LinearLayout) findViewById(R.id.home_foot_second_delete);

        // 页面商品数量为0
        home_hint_no_goods = (TextView) findViewById(R.id.home_hint_no_goods);

        // 打印
        home_goods_print = (LinearLayout) findViewById(R.id.home_goods_print);

        // 添加商品
        home_add_goods = (TextView) findViewById(R.id.home_add_goods);

        // 转移目录
        home_transfer_catalog = (TextView) findViewById(R.id.home_transfer_catalog);

        // 扫码
        home_scan_code = (ImageView) findViewById(R.id.home_scan_code);

    }

    @Override
    protected void initData() {
        isVdian = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);

        goodsDeleteDialog = new RGoodsDeleteDialog(context);

        goodsListAdapter = new GoodsListAdapter(context, isVdian);
        goodsRecyclerView.setAdapter(goodsListAdapter);

        goodsDataRefresh.setRefreshing(true);
        loadData();
    }

    @Override
    protected void initListener() {
        home_goods_search.setOnKeyListener(onKeyListener);

        // 扫码
        home_scan_code.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 刷新
        goodsDataRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                home_goods_search.setText("");
                restorationSort();
                loadData();
            }
        });

        // 列表项点击
        goodsListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Goods>() {

            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, item);
                if (isVdian) {
                    intent.putExtra(Constant.TYPE, 2);
                    intent.setClass(context, WGoodsDetailActivity.class);
                } else {
                    intent.putExtra(Constant.TYPE, 1);
                    intent.setClass(context, GoodsDetailsActivity.class);
                }
                startActivityForResult(intent, REQUEST_GOODS_DETAILS);
            }
        });

        goodsListAdapter.setOnItemSelectListener(new GoodsListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(boolean is_all_select) {
                home_all_select.setSelected(is_all_select);
            }
        });

        home_hint_no_goods.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                home_hint_no_goods.setVisibility(GONE);
                showLoading("正在加载商品...");
                loadData();
            }
        });

        // 目录
        home_goods_catalog.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (catalogPopupWindow != null) {
                    // 显示阴影
                    home_shade.setVisibility(VISIBLE);
                    catalogPopupWindow.showAsDropDown(v);
                }
            }
        });

        // 打印
        home_goods_print.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                toPrintBarcodeActivity();
            }
        });

        // 退出編輯
        home_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goodsListAdapter.isMainEdit()) {
                    setEditMode(false);
                    return;
                }
                finish();
            }
        });

        // 全选
        home_all_select.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                home_all_select.setSelected(!home_all_select.isSelected());
                goodsListAdapter.allSelect(home_all_select.isSelected());
            }
        });

        // 销量排序
        home_sort_sale.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                setSortTextAndIcon(home_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
                isAscStock = false;
                if (isAscSale) {// 升序
                    setSortTextAndIcon(home_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(home_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscSale = !isAscSale;
                saleSort();
            }
        });

        // 库存排序
        home_sort_stock.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                setSortTextAndIcon(home_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
                isAscSale = false;

                if (isAscStock) {// 升序
                    setSortTextAndIcon(home_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(home_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscStock = !isAscStock;
                stockSort();
            }
        });

        // 批量删除
        home_foot_second_delete.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goodsListAdapter.getSelects().size() > 0) {
                    home_covering.setVisibility(VISIBLE);
                    goodsDeleteDialog.show(new RGoodsDeleteDialog.OnDeleteGoodsListener() {

                        @Override
                        public void onDelete() {
                            removeGoods();
                        }
                    }, goodsListAdapter.getSelects().size());
                } else {
                    Toast.show(context, R.string.toast_select_goods);
                }
            }
        });

        // 批量编辑
        home_manage_goods.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goodsListAdapter.getItemCount() > 0) {
                    setEditMode(true);
                } else {
                    Toast.show(context, R.string.toast_not_can_edit_goods);
                }
            }
        });

        // 批量转移目录
        home_transfer_catalog.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goodsListAdapter.getSelects().size() > 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, RCatalogActivity.class);
                    startActivityForResult(intent, REQUEST_CATALOG);
                } else {
                    Toast.show(context, R.string.toast_select_goods);
                }
            }
        });

        // 添加商品
        home_add_goods.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                if (isVdian){
                    intent.setClass(context, WEditGoodsActivity.class);
                }else {
                    intent.setClass(context, EditGoodsActivity.class);
                }
                intent.putExtra(Constant.TYPE, Constant.ADD);
                intent.putExtra(Constant.CATALOG, selectCatalog);
                startActivityForResult(intent, REQUEST_ADD_GOODS);
            }
        });

        // 搜索
        home_goods_search.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    home_search_text_delete.setVisibility(GONE);
                } else {
                    home_search_text_delete.setVisibility(VISIBLE);
                }
                search(s);
            }
        });

        // 清空搜索框
        home_search_text_delete.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                home_goods_search.setText("");
            }
        });
    }

    /**
     * 加载商品数据
     */
    private void loadData() {
        if (isVdian) {
            VdianGoodsManager.getInstance().loadList(this, new IRequestCallBack<AllGoodsAndCatalog>() {

                @Override
                public void onResult(int tag, AllGoodsAndCatalog allGoodsAndCatalog) {
                    refreshCatalogData(allGoodsAndCatalog.catalogs, allGoodsAndCatalog.list.size());
                    if (TextUtil.equals(selectCatalog.catalog_id, "-1")) {
                        refreshListData(allGoodsAndCatalog.list);
                    } else {
                        loadCatalogGoods();
                    }
                    goodsDataRefresh.setRefreshing(false);
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    goodsDataRefresh.setRefreshing(false);
                    Toast.show(context, msg);
                }
            });
        } else {
            RealGoodsManager.getInstance().loadGoodsAndCatalog(this, "dat", BaseManager.DESC, "", "", new IRequestCallBack<AllGoodsAndCatalog>() {

                @Override
                public void onResult(int tag, AllGoodsAndCatalog allGoodsAndCatalog) {
                    refreshCatalogData(allGoodsAndCatalog.directory, allGoodsAndCatalog.list.size());
                    if (TextUtil.equals(selectCatalog.fid, "-1")) {
                        refreshListData(allGoodsAndCatalog.list);
                    } else {
                        loadCatalogGoods();
                    }
                    goodsDataRefresh.setRefreshing(false);
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    goodsDataRefresh.setRefreshing(false);
                    Toast.show(context, msg);
                }
            });
        }
    }

    /**
     * 刷新目录数据
     * @param catalogs
     */
    private void refreshCatalogData(ArrayList<Catalog> catalogs, int goodsCount) {
        if (isVdian) {
            Catalog allCatalog = new Catalog();
            allCatalog.catalog_id = "-1";
            allCatalog.catalog_name = getString(R.string.all);
            allCatalog.catalog_goods_number = goodsCount;
            catalogs.add(0, allCatalog);
            if (selectCatalog == null) {
                selectCatalog = allCatalog;
            } else {
                for (Catalog catalog : catalogs) {
                    if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
                        selectCatalog = catalog;
                    }
                }
            }
            home_goods_catalog.setText(selectCatalog.getNameAndNum());
        } else {
            Catalog allCatalog = new Catalog();
            allCatalog.fid = "-1";
            allCatalog.directory = getString(R.string.all);
            allCatalog.productNum = goodsCount + "";
            catalogs.add(0, allCatalog);
            if (selectCatalog == null) {
                selectCatalog = allCatalog;
            } else {
                for (Catalog catalog : catalogs) {
                    if (TextUtils.equals(selectCatalog.fid, catalog.fid)) {
                        selectCatalog = catalog;
                    }
                }
            }
            home_goods_catalog.setText(selectCatalog.getDirNameAndNum());
        }
        initCatalogPopupWindow(catalogs);
    }

    @Override
    public void onPause() {
        super.onPause();
        goodsDataRefresh.setRefreshing(false);
    }

    private void refreshGoodsListData(ArrayList<Goods> goodsDatas) {
        home_search_no_goods.setVisibility(GONE);
        home_hint_no_goods.setVisibility(GONE);
        if (ListUtils.isNotEmpty(goodsDatas)) {
            home_group_no_goods.setVisibility(GONE);
        } else {
            home_group_no_goods.setVisibility(VISIBLE);
        }
        goodsListAdapter.notifyDataSetChanged(goodsDatas);
        goodsRecyclerView.scrollToPosition(0);
    }

    private void refreshListData(ArrayList<Goods> goodsDatas) {
        home_search_no_goods.setVisibility(GONE);
        home_group_no_goods.setVisibility(GONE);
        if (ListUtils.isNotEmpty(goodsDatas)) {
            home_hint_no_goods.setVisibility(GONE);
        } else {
            home_hint_no_goods.setVisibility(VISIBLE);
        }
        goodsListAdapter.notifyDataSetChanged(goodsDatas);
        goodsRecyclerView.scrollToPosition(0);
    }

    private void loadCatalog() {
        if (isVdian) {
            VdianCatalogManager.getInstance().loadList(this, new IRequestCallBack<CatalogBean>() {

                @Override
                public void onResult(int tag, CatalogBean result) {
                    int num = 0;
                    for (Catalog catalog : result.list) {
                        num += catalog.catalog_goods_number;
                    }
                    refreshCatalogData(result.list, num);
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg);
                    dismissLoading();
                }
            });
        } else {
            RealCatalogManager.getInstance().loadList(this, new IRequestCallBack<CatalogBean>() {

                @Override
                public void onResult(int tag, CatalogBean object) {
                    int num = 0;
                    for (Catalog catalog : object.list) {
                        num += NumberUtils.toInt(catalog.productNum);
                    }
                    refreshCatalogData(object.list, num);
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg);
                    dismissLoading();
                }
            });
        }
    }

    /**
     * 加载指定目录商品
     */
    private void loadCatalogGoods() {
        if (isVdian) {
            VdianGoodsManager.getInstance().getPacketGoods(selectCatalog.catalog_id, new VdianGoodsManager.OnPacketGoods() {

                @Override
                public void packetGoods(ArrayList<Goods> packetGoodsDatas) {
                    // 复原排序按钮状态
                    restorationSort();
                    refreshGoodsListData(packetGoodsDatas);
                }

                @Override
                public void packetGoodsFail() {
                    refreshGoodsListData(null);
                    Toast.show(context, R.string.toast_load_catalog_data_fail);
                }
            });
        } else {
            RealGoodsManager.getInstance().getPacketGoods(selectCatalog.fid, new RealGoodsManager.OnPacketGoods() {

                @Override
                public void packetGoods(ArrayList<Goods> packetGoodsDatas) {
                    // 复原排序按钮状态
                    restorationSort();
                    refreshGoodsListData(packetGoodsDatas);
                }

                @Override
                public void packetGoodsFail() {
                    refreshGoodsListData(null);
                    Toast.show(context, R.string.toast_load_catalog_data_fail);
                }
            });
        }
    }

    /**
     * 初始化目录列表
     *
     * @param catalogs
     */
    private void initCatalogPopupWindow(ArrayList<Catalog> catalogs) {
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog, isVdian);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                if (isVdian) {
                    home_goods_catalog.setText(catalog.getNameAndNum());
                } else {
                    home_goods_catalog.setText(catalog.getDirNameAndNum());
                }
                selectCatalog = catalog;
                loadCatalogGoods();
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                home_shade.setVisibility(GONE);
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
     * 进入打印页面
     */
    private void toPrintBarcodeActivity() {
        if (goodsListAdapter.getSelects().size() == 0) {
            Toast.show(context, R.string.toast_select_goods);
            return;
        }

        // 封装数据
        ArrayList<GoodsBarcodePrintData> datas = makePrintBarcodeInfo(goodsListAdapter.getSelects());

        Intent intent = new Intent();
        intent.setClass(context, PrintBarcodeActivity.class);
        intent.putExtra(Constant.DATA, datas);
        startActivityForResult(intent, REQUEST_PRINT);
    }

    /**
     * 封装打印数据
     */
    public ArrayList<GoodsBarcodePrintData> makePrintBarcodeInfo(ArrayList<Goods> goodsDatas) {
        ArrayList<GoodsBarcodePrintData> datas = new ArrayList<GoodsBarcodePrintData>();
        if (isVdian) {
            for (Goods goods : goodsDatas) {
                GoodsBarcodePrintData g = new GoodsBarcodePrintData();
                g.bh = goods.goods_no;
                g.guid = goods.goods_id;
                g.name = goods.goods_name;
                g.price = goods.goods_price_section;
                g.imgs = new ArrayList<GoodsDetailsImgsData>();

                // 图片
                GoodsDetailsImgsData img = new GoodsDetailsImgsData();
                img.pic = goods.main_picture;
                g.imgs.add(img);

                // 颜色 尺寸 条码
                g.nums = new ArrayList<GoodsDetailsNumsData>();
                for (GoodsSpec num : goods.goods_spec) {
                    GoodsDetailsNumsData detailsNum = new GoodsDetailsNumsData();
                    detailsNum.barcode = num.spec_bar_code;
                    detailsNum.spec_name = num.goods_spec_name;
                    detailsNum.num = Integer.toString(num.spec_number);
                    detailsNum.price = Double.toString(num.spec_price);
                    g.nums.add(detailsNum);
                }
                datas.add(g);
            }
        } else {
            for (Goods goods : goodsDatas) {
                GoodsBarcodePrintData g = new GoodsBarcodePrintData();
                g.bh = goods.bh;
                g.guid = goods.guid;
                g.name = goods.name;
                g.price = goods.price;
                g.imgs = new ArrayList<GoodsDetailsImgsData>();

                // 图片
                GoodsDetailsImgsData img = new GoodsDetailsImgsData();
                img.pic = goods.image222;
                g.imgs.add(img);

                // 颜色 尺寸 条码
                g.nums = new ArrayList<GoodsDetailsNumsData>();
                for (OldSpec num : goods.nums) {
                    GoodsDetailsNumsData detailsNum = new GoodsDetailsNumsData();
                    detailsNum.barcode = num.barcode;
                    detailsNum.spec_name = num.getSpecName();
                    detailsNum.num = num.num;
                    detailsNum.price = goods.price;
                    g.nums.add(detailsNum);
                }
                datas.add(g);
            }
        }
        return datas;
    }

    /**
     * 搜索
     */
    private void search(String s) {
        if (isVdian) {
            ArrayList<Goods> goodses = VdianGoodsManager.getInstance().search(this, s, selectCatalog);
            restorationSort();
            goodsListAdapter.notifyDataSetChanged(goodses);
            goodsRecyclerView.scrollToPosition(0);
            if (ListUtils.isEmpty(goodses)) {
                home_search_no_goods.setVisibility(VISIBLE);
            } else {
                home_search_no_goods.setVisibility(GONE);
            }
        } else {
            ArrayList<Goods> goodses = RealGoodsManager.getInstance().search(this, s, selectCatalog);
            restorationSort();
            goodsListAdapter.notifyDataSetChanged(goodses);
            goodsRecyclerView.scrollToPosition(0);
            if (ListUtils.isEmpty(goodses)) {
                home_search_no_goods.setVisibility(VISIBLE);
            } else {
                home_search_no_goods.setVisibility(GONE);
            }
        }
    }

    /**
     * 复位排序视图
     */
    private void restorationSort() {
        setSortTextAndIcon(home_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
        setSortTextAndIcon(home_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
        isAscStock = false;
        isAscSale = false;
    }

    /**
     * 按销量排序
     */
    private void saleSort() {
        if (isVdian) {
            VdianGoodsManager.getInstance().saleSort(isAscSale, goodsListAdapter.getItems(), new VdianGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    goodsRecyclerView.scrollToPosition(0);
                }
            });
        } else {
            RealGoodsManager.getInstance().saleSort(isAscSale, goodsListAdapter.getItems(), new RealGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    goodsRecyclerView.scrollToPosition(0);
                }
            });
        }
    }

    /**
     * 按库存排序
     */
    private void stockSort() {
        if (isVdian) {
            VdianGoodsManager.getInstance().stockSort(isAscStock, goodsListAdapter.getItems(), new VdianGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    goodsRecyclerView.scrollToPosition(0);
                }
            });
        } else {
            RealGoodsManager.getInstance().stockSort(isAscStock, goodsListAdapter.getItems(), new RealGoodsManager.OnSortListener() {
                @Override
                public void onSort(ArrayList<Goods> goodsDatas) {
                    goodsListAdapter.notifyDataSetChanged(goodsDatas);
                    goodsRecyclerView.scrollToPosition(0);
                }
            });
        }
    }

    /**
     * 删除选中商品
     */
    private void removeGoods() {
        showLoading("正在删除商品...");
        if (isVdian) {
            VdianGoodsManager.getInstance().batchDel(this, goodsListAdapter.getSelects(), new IRequestCallBack() {

                @Override
                public void onResult(int tag, Object object) {
                    goodsListAdapter.removeGoods();
                    // 当删掉当前列表所有数据之后，显示无数据视图
                    if (goodsListAdapter.getItemCount() == 0) {
                        if (TextUtil.isNotEmpty(home_goods_search.getText().toString())) {
                            home_search_no_goods.setVisibility(VISIBLE);
                        } else if (!TextUtils.equals(selectCatalog.fid, "-1")) {
                            home_group_no_goods.setVisibility(VISIBLE);
                        }
                    }
                    loadCatalog();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg);
                    dismissLoading();
                }
            });
        } else {
            RealGoodsManager.getInstance().batchDelGoods(this, goodsListAdapter.getSelects(), new IRequestCallBack() {

                @Override
                public void onResult(int tag, Object object) {
                    goodsListAdapter.removeGoods();
                    // 当删掉当前列表所有数据之后，显示无数据视图
                    if (goodsListAdapter.getItemCount() == 0) {
                        if (TextUtil.isNotEmpty(home_goods_search.getText().toString())) {
                            home_search_no_goods.setVisibility(VISIBLE);
                        } else if (!TextUtils.equals(selectCatalog.fid, "-1")) {
                            home_group_no_goods.setVisibility(VISIBLE);
                        }
                    }
                    loadCatalog();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg);
                    dismissLoading();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (goodsListAdapter.isMainEdit()) {
            setEditMode(false);
            return;
        }
        finish();
    }

    /**
     * 设置编辑模式
     *
     * @param b
     */
    private void setEditMode(boolean b) {
        if (b) {
            home_all_select.setVisibility(VISIBLE);
            home_foot_second.setVisibility(VISIBLE);

            home_foot_main.setVisibility(GONE);
        } else {
            home_all_select.setSelected(false);
            home_all_select.setVisibility(GONE);
            home_foot_second.setVisibility(GONE);

            home_foot_main.setVisibility(VISIBLE);
        }
        goodsListAdapter.setMainEdit(b);
        goodsListAdapter.allSelect(false);
    }

    /**
     * 显示转移目录对话框
     *
     * @param catalog
     */
    private void showTransferCatalogDialog(final Catalog catalog) {
        TransferCatalogDialog dialog = new TransferCatalogDialog(context, goodsListAdapter.getSelects().size(), catalog, isVdian);
        dialog.show(new TransferCatalogDialog.OnTransferListener() {

            @Override
            public void onConfirm() {
                transferCatalog(catalog);
            }
        });
    }

    /**
     * 转移目录
     *
     * @param catalog
     */
    private void transferCatalog(Catalog catalog) {
        showLoading("正在转移商品");
        if (isVdian) {
            VdianCatalogManager.getInstance().move(this, catalog, goodsListAdapter.getSelects(), new IRequestCallBack<String>() {

                @Override
                public void onResult(int tag, String object) {
                    setEditMode(false);
                    goodsDataRefresh.setRefreshing(true);
                    loadData();
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg);
                    dismissLoading();
                }
            });
        } else {
            RealCatalogManager.getInstance().move(this, catalog.fid, goodsListAdapter.getSelects(), new IRequestCallBack<String>() {

                @Override
                public void onResult(int tag, String object) {
                    setEditMode(false);
                    goodsDataRefresh.setRefreshing(true);
                    loadData();
                    dismissLoading();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg);
                    dismissLoading();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 扫码
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            home_goods_search.setText(TextUtil.trimToEmpty(result));
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(context, "扫描二维码失败");
                        }
                    }
                    break;

                case REQUEST_ADD_GOODS:// 新增商品
                case REQUEST_GOODS_DETAILS:// 商品详情
                    loadData();
                    break;

                case REQUEST_CATALOG:// 转移目录
                    if (data != null) {
                        Catalog catalog = (Catalog) data.getSerializableExtra(Constant.CATALOG);
                        if (catalog != null) {
                            showTransferCatalogDialog(catalog);
                        }
                    }
                    loadCatalog();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CATALOG:// 转移目录
                    loadCatalog();
                    break;
            }
        }
    }

    @Override
    public void onEvent(EventBean event) {
        switch (event.code){
            case EventBean.CODE_CATALOG_ADD:
                showLoading();
                loadData();
                break;
            case EventBean.CODE_CATALOG_DEL:
                showLoading();
                loadData();
                break;
            case EventBean.CODE_CATALOG_UPDATE:
                showLoading();
                loadData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果退出去后切换了店铺类型，会导致数据异常
        SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, false);
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isActive()){
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
    };
}
