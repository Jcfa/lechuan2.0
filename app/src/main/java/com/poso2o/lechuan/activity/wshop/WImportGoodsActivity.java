package com.poso2o.lechuan.activity.wshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.GoodsDetailsActivity;
import com.poso2o.lechuan.adapter.ShopGoodsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealGoodsManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by mr zhang on 2017/12/6.
 */

public class WImportGoodsActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private final static int REQUEST_SHOP_INFO = 1;

    private final static int REQUEST_GOODS_DETAILS = 2;

    private final static int REQUEST_SCAN_CODE = 111;

    private Context context;

    //返回
    private View import_goods_back;

    //搜索输入框
    private EditText import_goods_goods_search;
    //清除搜索输入框
    private ImageView import_goods_search_text_delete;
    //扫描二维码
    private ImageView import_goods_scan_code;

    //销量
    private TextView import_sort_sale;
    //库存
    private TextView import_sort_stock;
    //目录
    private TextView import_goods_catalog;

    //全选
    private TextView import_goods_select;
    //导入
    private TextView import_goods_import;

    //列表
    private SwipeRefreshLayout import_goods_swipe;
    private RecyclerView import_goods_recycle;
    private ShopGoodsAdapter shopGoodsAdapter;
    private View import_goods_shade;

    //下拉菜单
    private CatalogPopupWindow catalogPopupWindow;

   //选中的目录
    private Catalog selectCatalog;

    private boolean isAscSale = false;
    private boolean isAscStock = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_w_import_goods;
    }

    @Override
    protected void initView() {

        import_goods_goods_search = (EditText) findViewById(R.id.import_goods_goods_search);
        import_goods_search_text_delete = (ImageView) findViewById(R.id.import_goods_search_text_delete);
        import_goods_scan_code = (ImageView) findViewById(R.id.import_goods_scan_code);

        import_goods_back = findViewById(R.id.import_goods_back);

        import_sort_sale = (TextView) findViewById(R.id.import_sort_sale);
        import_sort_stock = (TextView) findViewById(R.id.import_sort_stock);
        import_goods_catalog = (TextView) findViewById(R.id.import_goods_catalog);

        import_goods_swipe = (SwipeRefreshLayout) findViewById(R.id.import_goods_swipe);
        import_goods_recycle = (RecyclerView) findViewById(R.id.import_goods_recycle);
        import_goods_shade = findViewById(R.id.import_goods_shade);
        import_goods_select = (TextView) findViewById(R.id.import_goods_all_select);
        import_goods_import = (TextView) findViewById(R.id.import_goods_import);

    }

    @Override
    protected void initData() {
        context = this;

        shopGoodsAdapter = new ShopGoodsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        import_goods_recycle.setLayoutManager(linearLayoutManager);
        import_goods_recycle.setAdapter(shopGoodsAdapter);

        loadGoods();
    }

    @Override
    protected void initListener() {
        shopGoodsAdapter.setOnItemClickListener(new ShopGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Goods item) {
                Intent intent = new Intent();
                intent.setClass(context,GoodsDetailsActivity.class);
                intent.putExtra(Constant.DATA,item);
                startActivityForResult(intent,REQUEST_GOODS_DETAILS);
            }
        });

        shopGoodsAdapter.setOnItemSelectListener(new ShopGoodsAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(boolean is_all_select) {
                import_goods_select.setSelected(is_all_select);
            }
        });

        import_goods_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        import_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //实体店
                if (selectCatalog != null && !TextUtils.equals(selectCatalog.fid, "-1")) {
                    search("");
                } else {
                    loadGoods();
                }
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

        import_goods_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                import_goods_select.setSelected(!import_goods_select.isSelected());
                shopGoodsAdapter.allSelect(import_goods_select.isSelected());
            }
        });

        import_goods_goods_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(import_goods_goods_search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        import_goods_search_text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                import_goods_goods_search.setText(null);
            }
        });

        import_goods_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTask();
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
        catalog.productNum = shopGoodsAdapter.getItemCount() + "";
        selectCatalog = catalog;
        catalogs.add(0,catalog);
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {

                import_goods_catalog.setText(catalog.directory + "（" + catalog.productNum + "）");
                selectCatalog = catalog;
                search("");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SCAN_CODE:
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            import_goods_goods_search.setText(result);
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.show(context,"解析二维码失败");
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;
    /**
     * EsayPermissions接管权限处理逻辑
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
        Toast.show(context,"执行onPermissionsGranted()...");
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

    private void loadGoods(){
        showLoading("正在加载商品...");
        RealGoodsManager.getInstance().loadGoodsAndCatalog(this, "dat", "DESC", "", "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                import_goods_swipe.setRefreshing(false);
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                import_goods_swipe.setRefreshing(false);
                AllGoodsAndCatalog allGoodsAndCatalog = (AllGoodsAndCatalog) object;
                if (allGoodsAndCatalog != null){
                    shopGoodsAdapter.notifyDatas(allGoodsAndCatalog.list,false);
                    initCatalogPopupWindow(allGoodsAndCatalog.directory);
                }
                restorationSort();
            }
        });
    }

    //搜索商品
    private void search(String keywords){
        ArrayList<Goods> goodsDatas = RealGoodsManager.getInstance().search(this,keywords,selectCatalog);
        shopGoodsAdapter.notifyDatas(goodsDatas,false);
        import_goods_recycle.scrollToPosition(0);
    }

    //导入商品
    private void importGoods(){
        ArrayList<Goods> selects = shopGoodsAdapter.getSelects();
        if (selects == null || selects.size() == 0){
            Toast.show(context,"请选择商品");
            return;
        }
        String goods = "[";
        for (int i=0;i<selects.size();i++){
            if (i==selects.size()-1){
                goods = goods + "\"" + selects.get(i).guid + "\"]";
            }else {
                goods = goods + "\""  + selects.get(i).guid + "\",";
            }
        }
        showLoading("正在导入商品...");
        WShopManager.getrShopManager().importGoods(this, goods, false,new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                setResult(RESULT_OK);
                finish();
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
        RealGoodsManager.getInstance().saleSort(isAscSale, shopGoodsAdapter.getItems(), new RealGoodsManager.OnSortListener() {
            @Override
            public void onSort(ArrayList<Goods> goodsDatas) {
                shopGoodsAdapter.notifyDatas(goodsDatas, false);
                import_goods_recycle.scrollToPosition(0);
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
                import_goods_recycle.scrollToPosition(0);
            }
        });
    }
}
