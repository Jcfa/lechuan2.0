package com.poso2o.lechuan.activity.realshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.GoodsDetailsActivity;
import com.poso2o.lechuan.activity.wshop.WImportGoodsActivity;
import com.poso2o.lechuan.adapter.ShopGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.BatchUpGoodsBean;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.TipsNoAuthorDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.rshopmanager.RealShopManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by mr zhang on 2017/11/13.
 * <p>
 * 实体店
 */

public class RShopActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private final static int REQUEST_SHOP_INFO = 1;

    private final static int REQUEST_GOODS_DETAILS = 2;

    private final static int REQUEST_SCAN_CODE = 111;

    //微店导入商品
    private final static int REQUEST_IMPORT_CODE = 112;

    private Context context;

    //搜索输入框
    private EditText shop_goods_search;
    //清除搜索输入框
    private ImageView shop_search_text_delete;
    //扫描二维码
    private ImageView shop_scan_code;

    /**
     * 商户头像
     */
    private ImageView shop_info_icon;

    /**
     * 返回
     */
    private View shop_back;

    /**
     * 商户信息布局
     */
    private View shop_info_group;

    /**
     * 商户信息提示语
     */
    private View shop_info_hint;

    /**
     * 商户信息主视图
     */
    private View shop_info_main;

    /**
     * 商户名称
     */
    private TextView shop_info_name;

    /**
     * 商户描述
     */
    private TextView shop_info_describe;

    /**
     * 销量
     */
    private TextView shop_sort_sale;

    /**
     * 库存
     */
    private TextView shop_sort_stock;

    /**
     * 目录
     */
    private TextView shop_catalog;

    /**
     * 底部操作栏
     */
    private LinearLayout layout_bottom;

    /**
     * 全选
     */
    private TextView shop_all_select;

    /**
     * 下架
     */
    private TextView shop_sold_out;

    /**
     * 上架
     */
    private TextView shop_putaway;

    /**
     * 导入商品
     */
    private TextView to_import;

    /**
     * 无商品提示
     **/
    private TextView no_goods_tips;

    /**
     * 列表
     */
    private SwipeRefreshLayout shop_swipe;
    private RecyclerView shop_recycle;
    private ShopGoodsAdapter shopGoodsAdapter;
    private View shop_shade;

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
     * 是否修改过店铺信息
     */
    private boolean change_shop_info;

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_r_shop;
    }

    @Override
    protected void initView() {

        shop_goods_search = (EditText) findViewById(R.id.shop_goods_search);
        shop_search_text_delete = (ImageView) findViewById(R.id.shop_search_text_delete);
        shop_scan_code = (ImageView) findViewById(R.id.shop_scan_code);

        shop_info_icon = (ImageView) findViewById(R.id.shop_info_icon);
        shop_info_group = findViewById(R.id.shop_info_group);
        shop_back = findViewById(R.id.shop_back);
        shop_info_hint = findViewById(R.id.shop_info_hint);
        shop_info_main = findViewById(R.id.shop_info_main);
        shop_info_name = (TextView) findViewById(R.id.shop_info_name);
        shop_info_describe = (TextView) findViewById(R.id.shop_info_describe);

        no_goods_tips = (TextView) findViewById(R.id.no_goods_tips);

        shop_sort_sale = (TextView) findViewById(R.id.shop_sort_sale);
        shop_sort_stock = (TextView) findViewById(R.id.shop_sort_stock);
        shop_catalog = (TextView) findViewById(R.id.shop_catalog);
        shop_swipe = (SwipeRefreshLayout) findViewById(R.id.shop_swipe);
        shop_recycle = (RecyclerView) findViewById(R.id.shop_recycle);
        shop_shade = findViewById(R.id.shop_shade);

        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);

        shop_all_select = (TextView) findViewById(R.id.shop_all_select);
        shop_sold_out = (TextView) findViewById(R.id.shop_sold_out);
        shop_putaway = (TextView) findViewById(R.id.shop_putaway);
        to_import = (TextView) findViewById(R.id.to_import);

        shop_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    protected void initData() {
        context = this;

        shopGoodsAdapter = new ShopGoodsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shop_recycle.setLayoutManager(linearLayoutManager);
        shop_recycle.setAdapter(shopGoodsAdapter);

        shopData = (ShopData) getIntent().getExtras().get(RShopMainActivity.DATA_SHOP);
        refreshShopData();
        if (shopData == null) requestShopInfo();

        to_import.setVisibility(View.GONE);
        showLoading("正在加载商品数据...");
        loadRealGoods();

    }

    @Override
    protected void initListener() {
        shop_goods_search.setOnKeyListener(onKeyListener);

        shopGoodsAdapter.setOnItemClickListener(new ShopGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.setClass(context, GoodsDetailsActivity.class);
                intent.putExtra(Constant.DATA, item);
                intent.putExtra(Constant.TYPE, 1);
                startActivityForResult(intent, REQUEST_GOODS_DETAILS);
            }
        });

        shopGoodsAdapter.setOnItemSelectListener(new ShopGoodsAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(boolean is_all_select) {
                shop_all_select.setSelected(is_all_select);
            }
        });

        shop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change_shop_info) {
                    Intent intent = new Intent();
                    intent.putExtra(RShopMainActivity.DATA_SHOP, shopData);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        shop_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //实体店
                if (selectCatalog != null && !TextUtils.equals(selectCatalog.fid, "-1")) {
                    shop_goods_search.setText("");
                } else {
                    shop_goods_search.setText("");
                    loadRealGoods();
                }
            }
        });

        shop_info_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, RShopInfoActivity.class);
                intent.putExtra(RShopInfoActivity.SHOP_TYPE, false);
                startActivityForResult(intent, REQUEST_SHOP_INFO);
            }
        });

        shop_catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catalogPopupWindow != null) {
                    //显示阴影
                    shop_shade.setVisibility(View.VISIBLE);
                    catalogPopupWindow.showAsDropDown(v);
                }
            }
        });

        shop_all_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shop_all_select.setSelected(!shop_all_select.isSelected());
                shopGoodsAdapter.allSelect(shop_all_select.isSelected());
            }
        });

        shop_sold_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.show(context, "开发中。。。");
//                batchUpGoods("0");
            }
        });

        shop_putaway.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.show(context, "开发中。。。");
//                batchUpGoods("1");
            }
        });

        shop_goods_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(shop_goods_search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        shop_search_text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_goods_search.setText(null);
            }
        });

        shop_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTask();
            }
        });

        to_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY) == 0) {
                    TipsNoAuthorDialog noAuthorDialog = new TipsNoAuthorDialog(context);
                    noAuthorDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(context, WImportGoodsActivity.class);
                startActivityForResult(intent, REQUEST_IMPORT_CODE);
            }
        });

        shop_sort_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSortTextAndIcon(shop_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
                isAscStock = false;
                if (isAscSale) {// 升序
                    setSortTextAndIcon(shop_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(shop_sort_sale, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscSale = !isAscSale;
                saleSort();
            }
        });

        shop_sort_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSortTextAndIcon(shop_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
                isAscSale = false;

                if (isAscStock) {// 升序
                    setSortTextAndIcon(shop_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_down);
                } else {// 降序
                    setSortTextAndIcon(shop_sort_stock, R.color.color_00BCB4, R.mipmap.home_hand_up);
                }
                isAscStock = !isAscStock;
                stockSort();
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
        catalog.catalog_goods_number = shopGoodsAdapter.getItemCount();
        catalog.catalog_name = "全部";
        catalog.catalog_id = "-1";
        catalog.fid = "-1";
        catalog.directory = "全部";
        catalog.productNum = shopGoodsAdapter.getItemCount() + "";
        selectCatalog = catalog;
        catalogs.add(0, catalog);
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog, false);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                shop_catalog.setText(catalog.directory + "（" + catalog.productNum + "）");
                selectCatalog = catalog;
                search("");
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                shop_shade.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SHOP_INFO:
//                    shopData = (ShopData) data.getSerializableExtra("shop_data");
                    //刷新数据
//                    refreshShopData();
                    showLoading("正在加载店铺信息...");
                    change_shop_info = true;
                    requestShopInfo();
                    break;
                case REQUEST_SCAN_CODE:
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            shop_goods_search.setText(result);
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(context, "解析二维码失败");
                        }
                    }
                    break;
                case REQUEST_IMPORT_CODE:
//                    loadWGoods();
                    break;
                case REQUEST_GOODS_DETAILS:
                    shop_swipe.setRefreshing(true);
                    loadRealGoods();
            }
        }
    }

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;

    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            Intent intent = new Intent();
            intent.setClass(context, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.show(context, "执行onPermissionsGranted()...");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    //请求实体店商品数据
    private void loadRealGoods() {
        RealGoodsManager.getInstance().loadGoodsAndCatalog(this, "dat", "DESC", "", "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                shop_swipe.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                shop_swipe.setRefreshing(false);
                AllGoodsAndCatalog allGoodsAndCatalog = (AllGoodsAndCatalog) object;
                if (allGoodsAndCatalog != null) {
                    shopGoodsAdapter.notifyDatas(allGoodsAndCatalog.list, false);
                    initCatalogPopupWindow(allGoodsAndCatalog.directory);
                    shop_catalog.setText("全部（" + allGoodsAndCatalog.list.size() + "）");
                }
                if (allGoodsAndCatalog == null && allGoodsAndCatalog.list.size() == 0) {
                    no_goods_tips.setVisibility(View.VISIBLE);
                    no_goods_tips.setText("您的店铺还没有上传商品~");
                } else {
                    no_goods_tips.setVisibility(View.GONE);
                    restorationSort();
                }
            }
        });
    }

    //请求微店商品数据
    /*private void loadWGoods(){
        showLoading("正在加载商品数据...");
        VdianGoodsManager.getInstance().loadList(this, new IRequestCallBack<AllGoodsAndCatalog>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                shop_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag,AllGoodsAndCatalog allGoodsAndCatalog) {
                dismissLoading();
                shop_swipe.setRefreshing(false);
                if (allGoodsAndCatalog != null){
                    shopGoodsAdapter.notifyDatas(allGoodsAndCatalog.list,false);
                    initCatalogPopupWindow(allGoodsAndCatalog.catalogs);
                }
                if (allGoodsAndCatalog == null && allGoodsAndCatalog.list.size() == 0){
                    no_goods_tips.setVisibility(View.VISIBLE);
                    no_goods_tips.setText("您的店铺还没有上传商品~");
                }else {
                    no_goods_tips.setVisibility(View.GONE);
                }
            }
        });
    }*/

    //刷新店铺数据
    private void refreshShopData() {
        if (shopData == null) return;
        Glide.with(this).load(shopData.pic222_222).placeholder(R.mipmap.background_image).into(shop_info_icon);
        shop_info_name.setText(shopData.shopname);
        shop_info_describe.setText(shopData.remark);
    }

    //搜索商品
    private void search(String keywords) {
        ArrayList<Goods> goodsDatas = RealGoodsManager.getInstance().search(this, keywords, selectCatalog);
        shopGoodsAdapter.notifyDatas(goodsDatas, false);
        shop_recycle.scrollToPosition(0);
        if (goodsDatas == null || goodsDatas.size() == 0) {
            no_goods_tips.setVisibility(View.VISIBLE);
            no_goods_tips.setText("抱歉，找不到相关商品");
        } else {
            no_goods_tips.setVisibility(View.GONE);
        }
    }

    //批量上下架商品
    private void batchUpGoods(String sale_type) {
        ArrayList<Goods> goodses = shopGoodsAdapter.getSelects();
        if (goodses == null || goodses.size() == 0) {
            Toast.show(context, "请选择商品");
            return;
        }
        ArrayList<BatchUpGoodsBean> goodsBeen = new ArrayList<>();
        for (Goods good : goodses) {
            BatchUpGoodsBean bean = new BatchUpGoodsBean();
            bean.goods_id = good.goods_id;

            goodsBeen.add(bean);
        }
        /*showLoading("正在提交商品...");
        VdianGoodsManager.getInstance().putaway(this, goodsBeen,sale_type, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                shopGoodsAdapter.allSelect(false);
                showLoading("商品设置成功，正在重新加载商品...");
                loadWGoods();
            }
        });*/
    }

    //请求店铺信息
    private void requestShopInfo() {
        RealShopManager.getRealShopManager().rShopInfo(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                shopData = (ShopData) result;
                refreshShopData();
                if (change_shop_info) {
                    Intent intent = new Intent();
                    intent.putExtra(RShopMainActivity.DATA_SHOP, shopData);
                    setResult(RESULT_OK, intent);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
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
        setSortTextAndIcon(shop_sort_stock, R.color.textBlack, R.mipmap.home_hand_default);
        setSortTextAndIcon(shop_sort_sale, R.color.textBlack, R.mipmap.home_hand_default);
        isAscStock = false;
        isAscSale = false;
    }

    /**
     * 按销量排序
     */
    private void saleSort() {
        RealGoodsManager.getInstance().saleSort(isAscSale, shopGoodsAdapter.getItems(), new RealGoodsManager.OnSortListener() {
            @Override
            public void onSort(ArrayList<Goods> goodsDatas) {
                shopGoodsAdapter.notifyDatas(goodsDatas, false);
                shop_recycle.scrollToPosition(0);
            }
        });
    }

    /**
     * 按库存排序
     */
    private void stockSort() {
        RealGoodsManager.getInstance().stockSort(isAscStock, shopGoodsAdapter.getItems(), new RealGoodsManager.OnSortListener() {
            @Override
            public void onSort(ArrayList<Goods> goodsDatas) {
                shopGoodsAdapter.notifyDatas(goodsDatas,false);
                shop_recycle.scrollToPosition(0);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && change_shop_info) {
            Intent intent = new Intent();
            intent.putExtra(RShopMainActivity.DATA_SHOP, shopData);
            setResult(RESULT_OK, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
    };
}
