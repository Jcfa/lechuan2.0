package com.poso2o.lechuan.activity.wshop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.CatalogActivity;
import com.poso2o.lechuan.activity.goods.ColorActivity;
import com.poso2o.lechuan.activity.goods.GoodsNameActivity;
import com.poso2o.lechuan.activity.goods.SizeActivity;
import com.poso2o.lechuan.activity.goods.SpecActivity;
import com.poso2o.lechuan.activity.goods.SupplierActivity;
import com.poso2o.lechuan.activity.goods.UnitActivity;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.realshop.GoodsTypeActivity;
import com.poso2o.lechuan.adapter.EditGoodsImageAdapter;
import com.poso2o.lechuan.adapter.WEditGoodsImageAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.color.GoodsColor;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsMovePicture;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.bean.size.GoodsSize;
import com.poso2o.lechuan.bean.spec.Spec;
import com.poso2o.lechuan.bean.unit.GoodsUnit;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.configs.MenuConstant;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.dialog.KeepAddGoodsBackDialog;
import com.poso2o.lechuan.dialog.UnitSetupDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.listener.PointControlTextWatcher;
import com.poso2o.lechuan.util.FileManage;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ImageLoaderUtils;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.LogUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.SpecUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.view.GoodsSupplierItemView;
import com.poso2o.lechuan.view.WSpecViewHolder;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.configs.Constant.GOODS;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class WEditGoodsActivity extends BaseActivity {

    /**
     * 活动请求码
     */
    private static final int REQUEST_PHOTO = 9;// 相册
    private static final int REQUEST_TYPE = 10;// 类别
    private static final int REQUEST_NAME = 11;// 品名
    private static final int REQUEST_COLOUR = 12;// 颜色
    private static final int REQUEST_SIZE = 13;// 尺寸
    private static final int REQUEST_SPEC = 14;// 规格
    private static final int REQUEST_UNIT = 15;// 单位
    private static final int REQUEST_CODE = 16;// 扫码
    private static final int REQUEST_CATALOG = 17;// 目录
    private static final int REQUEST_SUPPLIER = 18;// 目录
    private static final int REQUEST_SHEAR = 19;// 剪切

    /**
     * 选中的目录
     */
    private Catalog selectCatalog;

    private WEditGoodsActivity context;

    /**
     * 返回
     */
    private ImageView edit_goods_back;

    /**
     * 图片列表
     */
    private RecyclerView edit_goods_image_recycle;
    private WEditGoodsImageAdapter editGoodsImageAdapter;

    /**
     * 类别
     */
    private LinearLayout edit_goods_type_group;
    private TextView edit_goods_type;

    /**
     * 添加品名
     */
    private LinearLayout edit_goods_name_group;
    private TextView edit_goods_name;

    /**
     * 价格
     */
    private EditText edit_goods_price;
    private LinearLayout edit_goods_price_layout;

    /**
     * 成本
     */
    private EditText edit_goods_cost;
    private LinearLayout edit_goods_cost_layout;
    private boolean isShowCost = true;

    /**
     * 目录
     */
    private LinearLayout edit_goods_catalog_group;
    private TextView edit_goods_catalog;

    /**
     * 供应商
     */
    private LinearLayout edit_goods_supplier_group;
    private TextView edit_goods_supplier;
    private LinearLayout edit_goods_supplier_list;

    /**
     * 规格
     */
    private LinearLayout edit_goods_spec_list;

    /**
     * 单位
     */
    private LinearLayout edit_goods_unit_group;
    private TextView edit_goods_unit;
    private TextView edit_goods_auxiliary_unit;

    /**
     * 颜色
     */
    private LinearLayout edit_goods_color_group;
    private TextView edit_goods_color;

    /**
     * 尺码
     */
    private LinearLayout edit_goods_size_group;
    private TextView edit_goods_size;

    /**
     * 规格
     */
    private LinearLayout edit_goods_spec_group;
    private TextView edit_goods_spec;

    /**
     * 货号
     */
    private EditText edit_goods_code;

    /**
     * 库存
     */
    private EditText edit_goods_stock;
    private TextView edit_goods_stock_hint;

    /**
     * 上架时间
     */
    private LinearLayout on_sale_group;
    private TextView edit_goods_now_putaway;
    private TextView edit_goods_in_warehouse;

    /**
     * 上传商品
     */
    private View edit_goods_upload;

    /**
     * 刷新
     */
    private SwipeRefreshLayout edit_goods_swipe;

    /**
     * 遮罩
     */
    private View edit_goods_shield;

    /**
     * 滚动布局
     */
    private ScrollView edit_goods_scroll_view;

    /**
     * 退出保存
     */
    private KeepAddGoodsBackDialog keepAddGoodsBackDialog;

    /**
     * 编辑商品(从商品详情页进入)
     */
    private Goods goods;

    /**
     * 已选择的数据集合
     */
    private ArrayList<Spec> specs;
    private ArrayList<GoodsColor> colours;
    private ArrayList<GoodsSize> sizes;

    /**
     * 图片数据
     */
    private ArrayList<Bitmap> images;

    /**
     * 加载过程中显示的图片数据
     */
    private ArrayList<Bitmap> backImages;

    /**
     * 未加载完之前，记录被删除的图片下标
     */
    private ArrayList<Integer> deleteImageIndexs = new ArrayList<>();

    /**
     * 业务类型
     */
    private int type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_goods;
    }

    @Override
    public void initView() {
        context = this;

        // 返回
        edit_goods_back = (ImageView) findViewById(R.id.edit_goods_back);

        // 调用相册
        edit_goods_image_recycle = (RecyclerView) findViewById(R.id.edit_goods_photo_recycle);
        edit_goods_image_recycle.setLayoutManager(new GridLayoutManager(context, 4));
        itemTouchHelper.attachToRecyclerView(edit_goods_image_recycle);

        // 类别
        edit_goods_type_group = (LinearLayout) findViewById(R.id.edit_goods_type_group);
        edit_goods_type = (TextView) findViewById(R.id.edit_goods_type);

        // 添加品名
        edit_goods_name_group = (LinearLayout) findViewById(R.id.edit_goods_name_group);
        edit_goods_name = (TextView) findViewById(R.id.edit_goods_name);

        // 价格
        edit_goods_price = (EditText) findViewById(R.id.edit_goods_price);
        edit_goods_price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edit_goods_price_layout = (LinearLayout) findViewById(R.id.edit_goods_price_layout);

        // 成本
        edit_goods_cost_layout = (LinearLayout) findViewById(R.id.edit_goods_cost_layout);
        edit_goods_cost = (EditText) findViewById(R.id.edit_goods_cost);
        edit_goods_cost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // 目录
        edit_goods_catalog_group = (LinearLayout) findViewById(R.id.edit_goods_catalog_group);
        edit_goods_catalog = (TextView) findViewById(R.id.edit_goods_catalog);

        // 供应商
        edit_goods_supplier_group = (LinearLayout) findViewById(R.id.edit_goods_supplier_group);
        edit_goods_supplier = (TextView) findViewById(R.id.edit_goods_supplier);
        edit_goods_supplier_list = (LinearLayout) findViewById(R.id.edit_goods_supplier_list);

        // 规格列表
        edit_goods_spec_list = (LinearLayout) findViewById(R.id.edit_goods_spec_list);

        // 单位
        edit_goods_unit_group = (LinearLayout) findViewById(R.id.edit_goods_unit_group);
        edit_goods_unit = (TextView) findViewById(R.id.edit_goods_unit);
        edit_goods_auxiliary_unit = (TextView) findViewById(R.id.edit_goods_auxiliary_unit);

        // 颜色
        edit_goods_color_group = (LinearLayout) findViewById(R.id.edit_goods_color_group);
        edit_goods_color = (TextView) findViewById(R.id.edit_goods_color);

        // 尺码
        edit_goods_size_group = (LinearLayout) findViewById(R.id.edit_goods_size_group);
        edit_goods_size = (TextView) findViewById(R.id.edit_goods_size);

        // 规格
        edit_goods_spec_group = (LinearLayout) findViewById(R.id.edit_goods_spec_group);
        edit_goods_spec = (TextView) findViewById(R.id.edit_goods_spec);

        // 上传商品
        edit_goods_upload = findViewById(R.id.edit_goods_upload);
        edit_goods_upload.setSelected(false);

        // 货号
        edit_goods_code = (EditText) findViewById(R.id.edit_goods_code);

        // 库存
        edit_goods_stock = (EditText) findViewById(R.id.edit_goods_stock);
        edit_goods_stock_hint = (TextView) findViewById(R.id.edit_goods_stock_hint);

        // 上传时间
        on_sale_group = (LinearLayout) findViewById(R.id.on_sale_group);
        edit_goods_now_putaway = (TextView) findViewById(R.id.edit_goods_now_putaway);
        edit_goods_in_warehouse = (TextView) findViewById(R.id.edit_goods_in_warehouse);
        edit_goods_now_putaway.setSelected(true);

        // 遮罩
        edit_goods_shield = findViewById(R.id.edit_goods_shield);
        edit_goods_scroll_view = (ScrollView) findViewById(R.id.edit_goods_scroll_view);

        // 刷新
        edit_goods_swipe = (SwipeRefreshLayout) findViewById(R.id.edit_goods_swipe);
        edit_goods_swipe.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
        setRefreshing(false);

        // 退出保存
        keepAddGoodsBackDialog = new KeepAddGoodsBackDialog(context, new KeepAddGoodsBackDialog.OnKeepAddGoodsBack() {

            @Override
            public void onCancel() {
                SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, false);
                keepAddGoodsBackDialog.dismiss();
                finish();
            }

            @Override
            public void onConfirm() {
                keepAddGoods();
                keepAddGoodsBackDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void initData() {
        // 显示相册
        images = new ArrayList<>();
        editGoodsImageAdapter = new WEditGoodsImageAdapter(context, images);
        edit_goods_image_recycle.setAdapter(editGoodsImageAdapter);

        // 更多
        images = new ArrayList<>();
        backImages = new ArrayList<>();
        specs = new ArrayList<>();
        sizes = new ArrayList<>();
        colours = new ArrayList<>();

        Intent intent = getIntent();

        type = intent.getIntExtra(Constant.TYPE, -1);
        // 判断来源
        switch (type) {
            case Constant.ADD:
                selectCatalog = (Catalog) intent.getSerializableExtra(Constant.CATALOG);
                // 展示保存的数据
                if (SharedPreferencesUtils.getBoolean(Constant.ADD_GOODS_SAVE)) {
                    visibleSaveData();
                }
                if (goods == null) {
                    goods = new Goods();
                    goods.goods_type = SharedPreferencesUtils.getInt(Constant.GOODS_TYPE, 1);
                    if (goods.goods_type == 2) {
                        edit_goods_type.setText(R.string.shop_type);
                        edit_goods_color_group.setVisibility(GONE);
                        edit_goods_size_group.setVisibility(GONE);
                        edit_goods_spec_group.setVisibility(VISIBLE);
                        edit_goods_stock_hint.setVisibility(VISIBLE);
                    }
                    if (selectCatalog != null) {
                        if (!TextUtil.equals(selectCatalog.catalog_id, "-1")) {
                            goods.catalog_id = selectCatalog.catalog_id;
                            goods.catalog_name = selectCatalog.catalog_name;
                            edit_goods_catalog.setText(selectCatalog.catalog_name);
                        }
                    }
                    goods.goods_move_picture = new ArrayList<>();
                    goods.goods_spec = new ArrayList<>();
                }
                edit_goods_swipe.setEnabled(false);
                break;

            case Constant.UPDATE:
                goods = (Goods) intent.getExtras().get(Constant.GOODS);

                edit_goods_type_group.setEnabled(false);
                // 设置图片数据
                loadImages(true);
                // 展示商品详情信息
                refreshView();
                break;
        }
    }

    @Override
    public void initListener() {
        // 上传商品
        edit_goods_upload.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                saleGoods();
            }
        });

        edit_goods_price.addTextChangedListener(new PointControlTextWatcher(edit_goods_price));
        edit_goods_cost.addTextChangedListener(new PointControlTextWatcher(edit_goods_cost));

        // 返回
        edit_goods_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                finish();
                if (type == Constant.ADD) {
                    if (hasEdit()) {
                        keepAddGoodsBackDialog.show();
                    } else {
                        SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, false);
                        finish();
                    }
                } else if (type == Constant.UPDATE) {
                    Intent intent = new Intent();
                    intent.putExtra(GOODS, goods);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        });

        // 刷新
        edit_goods_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 重新加载图片
                loadImages(true);
            }
        });

        // 类别
        edit_goods_type_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goods.goods_spec.size() > 0 || TextUtil.isNotEmpty(goods.goods_unit)) {
                    CommonDialog dialog = new CommonDialog(context, R.string.dialog_switch_goods_type);
                    dialog.show(new CommonDialog.OnConfirmListener() {

                        @Override
                        public void onConfirm() {
                            toGoodsTypeActivity();
                        }
                    });
                } else {
                    toGoodsTypeActivity();
                }
            }
        });

        // 货号
        edit_goods_code.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                goods.goods_no = s;
                goods.bh = s;
            }
        });

        // 添加品名
        edit_goods_name_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(context, GoodsNameActivity.class);
                intent.putExtra(Constant.NAME, goods.goods_name);
                startActivityForResult(intent, REQUEST_NAME);
            }
        });

        // 单位
        edit_goods_unit_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (goods.goods_type == 1) {
                    Intent intent = new Intent(context, UnitActivity.class);
                    intent.putExtra(Constant.DATA, goods.goods_unit);
                    startActivityForResult(intent, REQUEST_UNIT);
                } else {
                    showUnitSetupDialog();
                }
            }
        });

        // 颜色
        edit_goods_color_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(context, ColorActivity.class);
                intent.putExtra(Constant.DATA, colours);
                startActivityForResult(intent, REQUEST_COLOUR);
            }
        });

        // 尺码
        edit_goods_size_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(context, SizeActivity.class);
                intent.putExtra(Constant.DATA, sizes);
                startActivityForResult(intent, REQUEST_SIZE);
            }
        });

        // 规格
        edit_goods_spec_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (TextUtil.isEmpty(goods.goods_unit)) {
                    Toast.show(context, "请先设置单位");
                } else {
                    Intent intent = new Intent(context, SpecActivity.class);
                    intent.putExtra(Constant.DATA, specs);
                    startActivityForResult(intent, REQUEST_SPEC);
                }
            }
        });

        // 目录
        edit_goods_catalog_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CatalogActivity.class);
                intent.putExtra(Constant.CATALOG, goods.catalog_id);
                startActivityForResult(intent, REQUEST_CATALOG);
            }
        });

        // 供应商
        edit_goods_supplier_group.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, SupplierActivity.class);
                intent.putExtra(Constant.DATA, goods.goods_supplier);
                startActivityForResult(intent, REQUEST_SUPPLIER);
            }
        });

        // 立即上架
        edit_goods_now_putaway.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                goods.sale_type = 1;
                edit_goods_now_putaway.setSelected(true);
                edit_goods_in_warehouse.setSelected(false);
            }
        });

        // 放入仓库
        edit_goods_in_warehouse.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                goods.sale_type = 0;
                edit_goods_in_warehouse.setSelected(true);
                edit_goods_now_putaway.setSelected(false);
            }
        });
    }

    /**
     * 保存商品
     */
    private void saleGoods() {
        if (selectCatalog == null || selectCatalog.catalog_id.equals("-1")) {
            Toast.show(activity, "请选择目录");
            return;
        }
        /*if (TextUtil.isEmpty(goods.goods_no)) {
            Toast.show(activity, "货号不能为空");
            return;
        }*/
        if (TextUtil.isEmpty(goods.goods_name)) {
            Toast.show(activity, "品名不能为空");
            return;
        }
        if (TextUtil.isEmpty(goods.goods_unit)) {
            Toast.show(activity, "请选择单位");
            return;
        }
        if (ListUtils.isEmpty(goods.goods_spec) || goods.goods_spec.size() == 0) {// 没有规格的时候默认为无规格
            GoodsSpec spec = new GoodsSpec();
            spec.goods_spec_name = GoodsSpec.NOT_SPEC;
            spec.goods_spec_id = RandomStringUtil.getOrderId();
            spec.spec_price = NumberUtils.toDouble(edit_goods_price.getText().toString());
            spec.spec_cost = NumberUtils.toDouble(edit_goods_cost.getText().toString());
            spec.spec_number = NumberUtils.toInt(edit_goods_stock.getText().toString());
            spec.spec_bar_code = RandomStringUtil.randomGUID(13,"0123456789");
            goods.goods_spec.add(spec);
            goods.goods_number = spec.spec_number;
        } else if (goods.goods_spec.size() == 1 && TextUtil.equals(GoodsSpec.NOT_SPEC, goods.goods_spec.get(0).goods_spec_name)) {
            GoodsSpec spec = goods.goods_spec.get(0);
//            spec.goods_spec_id = RandomStringUtil.getOrderId();
//            spec.spec_price = NumberUtils.toDouble(edit_goods_price.getText().toString());
//            spec.spec_cost = NumberUtils.toDouble(edit_goods_cost.getText().toString());
//            spec.spec_number = NumberUtils.toInt(edit_goods_stock.getText().toString());
            if (TextUtil.isEmpty(spec.spec_bar_code))spec.spec_bar_code = RandomStringUtil.randomGUID(13,"0123456789");
            goods.goods_number = spec.spec_number;
        } else {
            goods.goods_number = SpecUtils.getNum(goods.goods_spec);
        }
        goods.goods_price_section = SpecUtils.getPriceText(goods.goods_spec);
        goods.goods_cost_section = SpecUtils.getCostText(goods.goods_spec);
        switch (type) {
            case Constant.ADD:
                addGoods();
                break;

            case Constant.UPDATE:
                updateGoods();
                break;
        }
    }

    /**
     * 显示单位设置对话框
     */
    private void showUnitSetupDialog() {
        UnitSetupDialog dialog = new UnitSetupDialog(context, goods.goods_unit, goods.goods_auxiliary_unit, goods.goods_auxiliary_unit_packingrate);
        dialog.show(new UnitSetupDialog.OnUnitSetupListener() {

            @Override
            public void onConfirm(String unit, String auxiliary_unit, int auxiliary_unit_number) {
                goods.goods_unit = unit;
                goods.goods_auxiliary_unit = auxiliary_unit;
                goods.goods_auxiliary_unit_packingrate = auxiliary_unit_number;
                edit_goods_unit.setText(unit);
                if (TextUtil.isNotEmpty(auxiliary_unit)) {
                    edit_goods_auxiliary_unit.setText("(" + auxiliary_unit_number + unit + "/" + auxiliary_unit + ")");
                }
                initSpecView();
            }
        });
    }

    private void toGoodsTypeActivity() {
        Intent intent = new Intent();
        intent.setClass(context, GoodsTypeActivity.class);
        intent.putExtra(Constant.TYPE, goods.goods_type);
        startActivityForResult(intent, REQUEST_TYPE);
    }

    /**
     * 新增
     */
    private void addGoods() {
        showLoading("正在上传商品...");
        VdianGoodsManager.getInstance().add(this, goods, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, false);
                SharedPreferencesUtils.put(Constant.GOODS_TYPE, goods.goods_type);
                Intent intent = new Intent();
                intent.putExtra(Constant.GOODS, goods);
                setResult(RESULT_OK, intent);
                setRefreshing(false);
                dismissLoading();
                finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
            }
        });
    }

    /**
     * 修改
     */
    private void updateGoods() {
        showLoading("正在修改商品...");
        VdianGoodsManager.getInstance().edit(this, goods, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                Intent intent = new Intent();
                intent.putExtra(Constant.GOODS, goods);
                setResult(RESULT_OK, intent);
                setRefreshing(false);
                dismissLoading();
                finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                setRefreshing(false);
                dismissLoading();
            }
        });
    }

    /**
     * 释放图片
     */
    private void recycleImages() {
        for (Bitmap bm : images) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * 点击扫码的视图
     */
    private WSpecViewHolder scanCodeSpecView;

    /**
     * 进入条码扫描页面
     */
    public void toScanCodeActivity(WSpecViewHolder scanCodeSpecView) {
        this.scanCodeSpecView = scanCodeSpecView;
        Intent intent = new Intent();
        intent.setClass(context, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        if (type == Constant.ADD) {
            if (hasEdit()) {
                keepAddGoodsBackDialog.show();
                return;
            } else {
                SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, false);
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra(GOODS, goods);
            setResult(RESULT_CANCELED, intent);
        }
        super.onBackPressed();
    }

    /**
     * 删除图片
     */
    public void deleteImage(Bitmap bitmap, int position) {
        if (bitmap != null) {
            if (loadImageNum < images.size()) {// 还有图片没加载完
                int index = images.indexOf(bitmap);
                deleteImageIndexs.add(index);
                showBackImage();
            } else {// 所有图片加载完删除图片
                images.remove(position);
                goods.goods_move_picture.remove(position);
                bitmap.recycle();
                editGoodsImageAdapter.notifyDataSetChanged(images);
            }
        } else {
            // 图片未加载出来时删除
            if (loadImageNum < images.size()) {
                int index = position;
                for (int i = deleteImageIndexs.size() - 1; i >= 0; i--) {
                    Integer imageIndex = deleteImageIndexs.get(i);
                    if (index >= imageIndex) {
                        index++;
                    }
                }
                deleteImageIndexs.add(index);
                showBackImage();
            }
        }
    }

    /**
     * 进入相册
     */
    public void toPhoto() {
        if (applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            toSelectImagesActivity();
        }
    }

    @Override
    protected void onRequestPermissionsResult(boolean isSucceed) {
        if (isSucceed) {
            toSelectImagesActivity();
        }
    }

    private void toSelectImagesActivity() {
        Intent intent = new Intent(context, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, 8 - images.size());
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_PHOTO:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        compressImages(selectedImages);
                    }
                    break;

                case REQUEST_SHEAR:// 裁剪图片
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        String path = ImageManage.saveBitmap(context, photo);
                        images.set(shearPosition, ImageManage.lessenUriImage(path, 1));
                        showLoading("正在上传图片");
                        goods.goods_move_picture.get(shearPosition).path = path;
                        uploadImage(goods.goods_move_picture.get(shearPosition));
                        editGoodsImageAdapter.notifyDataSetChanged(images);
                        shearPosition = -1;
                    }
                    break;

                case REQUEST_TYPE:// 商品类别
                    int type = data.getIntExtra(Constant.TYPE, 1);
                    if (type != goods.goods_type) {
                        goods.goods_type = type;
                        switchGoodsType();
                    }
                    break;

                case REQUEST_NAME:// 品名
                    String name = data.getStringExtra(Constant.NAME);
                    goods.goods_name = name;
                    goods.name = name;
                    edit_goods_name.setText(name);
                    break;

                case REQUEST_UNIT:// 单位
                    GoodsUnit unit = (GoodsUnit) data.getSerializableExtra(Constant.DATA);
                    goods.goods_unit = unit.unit_name;
                    edit_goods_unit.setText(unit.unit_name);
                    break;

                case REQUEST_CATALOG:// 目录
                    selectCatalog = (Catalog) data.getSerializableExtra(Constant.CATALOG);
                    if (selectCatalog != null) {
                        goods.catalog_id = selectCatalog.catalog_id;
                        goods.catalog_name = selectCatalog.catalog_name;
                        goods.goods_discount = selectCatalog.catalog_discount;
                        edit_goods_catalog.setText(selectCatalog.catalog_name);
                    }
                    break;

                case REQUEST_SUPPLIER:// 供应商
                    goods.goods_supplier.clear();
                    ArrayList<GoodsSupplier> suppliers = (ArrayList<GoodsSupplier>) data.getSerializableExtra(Constant.DATA);
                    if (ListUtils.isNotEmpty(suppliers)) {
                        suppliers.get(0).has_default = 1;
                        goods.goods_supplier.addAll(suppliers);
                    }
                    initSupplierView();
                    break;

                case REQUEST_COLOUR:// 选择颜色
                    ArrayList<GoodsColor> colours = (ArrayList<GoodsColor>) data.getSerializableExtra(Constant.DATA);
                    this.colours.clear();
                    if (colours != null) {
                        this.colours.addAll(colours);
                    }
                    setColorText();
                    initSpecView();
                    break;

                case REQUEST_SIZE:// 选择尺码
                    ArrayList<GoodsSize> sizes = (ArrayList<GoodsSize>) data.getSerializableExtra(Constant.DATA);
                    this.sizes.clear();
                    if (sizes != null) {
                        this.sizes.addAll(sizes);
                    }
                    setSizeText();
                    initSpecView();
                    break;

                case REQUEST_SPEC:// 选择规格
                    ArrayList<Spec> specs = (ArrayList<Spec>) data.getSerializableExtra(Constant.DATA);
                    this.specs.clear();
                    if (specs != null) {
                        this.specs.addAll(specs);
                    }
                    setSpecText();
                    initSpecView();
                    break;

                case REQUEST_CODE:
                    // 扫码
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        scanCodeSpecView.setCode(result);
                        scanCodeSpecView = null;
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.show(context, "扫描二维码失败");
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {// 取消
            switch (requestCode) {
                case REQUEST_UNIT:// 单位
                    GoodsUnit unit = (GoodsUnit) data.getSerializableExtra(Constant.DATA);
                    goods.goods_unit = unit.unit_name;
                    edit_goods_unit.setText(unit.unit_name);
                    break;

                case REQUEST_COLOUR:// 选择颜色
                    ArrayList<GoodsColor> colours = (ArrayList<GoodsColor>) data.getSerializableExtra(Constant.DATA);
                    if (colours != null) {
                        for (GoodsColor colour : colours) {
                            for (GoodsColor c : this.colours) {
                                if (TextUtil.equals(colour.goods_colour_id, c.goods_colour_id)) {
                                    c.goods_colour_name = colour.goods_colour_name;
                                }
                            }
                        }
                    }
                    setColorText();
                    initSpecView();
                    break;

                case REQUEST_SIZE:// 选择尺码
                    ArrayList<GoodsSize> sizes = (ArrayList<GoodsSize>) data.getSerializableExtra(Constant.DATA);
                    if (sizes != null) {
                        for (GoodsSize size : sizes) {
                            for (GoodsSize s : this.sizes) {
                                if (TextUtil.equals(size.goods_size_id, s.goods_size_id)) {
                                    s.goods_size_name = size.goods_size_name;
                                }
                            }
                        }
                    }
                    setSizeText();
                    initSpecView();
                    break;

                case REQUEST_SPEC:// 选择规格
                    ArrayList<Spec> specs = (ArrayList<Spec>) data.getSerializableExtra(Constant.DATA);
                    if (specs != null) {
                        for (Spec spec : specs) {
                            for (Spec s : this.specs) {
                                if (TextUtil.equals(spec.spec_id, s.spec_id)) {
                                    s.spec_name = spec.spec_name;
                                }
                            }
                        }
                    }
                    setSpecText();
                    initSpecView();
                    break;
            }
        }
    }

    /**
     * 上传单张图片
     */
    private void uploadImage(final GoodsMovePicture img) {
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(context, img.path, new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                img.picture_url = url;
                img.picture_type = type;
                editGoodsImageAdapter.notifyDataSetChanged(images);
                Toast.show(context, "上传图片成功");
                dismissLoading();
            }

            @Override
            public void fail() {
                Toast.show(context, "上传图片失败");
                dismissLoading();
            }
        });
        asyncTask.execute();
    }

    /**
     * 加载并压缩图片
     */
    private void compressImages(final ArrayList<String> selectedImages) {
        final ArrayList<GoodsMovePicture> gmps = new ArrayList<>();
        final ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            GoodsMovePicture gmp = new GoodsMovePicture();
            gmp.path = FileUtils.getNewImagePath();
            gmps.add(gmp);
            urls.add(gmp.path);
            goods.goods_move_picture.add(gmp);
        }
        showLoading("正在压缩图片(" + "0/" + gmps.size() + ")");
        CompressImageAsyncTask task = new CompressImageAsyncTask(selectedImages, urls, new CompressImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(Integer result) {
                if (result == null) {
                    Toast.show(context, "压缩图片失败");
                    dismissLoading();
                } else if (result == -1) {
                    index = 0;
                    uploadImages(gmps);
                    loadLocalImages(gmps);
                } else if (result > 0) {
                    showLoading("正在压缩图片(" + result.toString() + "/" + gmps.size() + ")");
                }
            }
        });
        task.execute();
    }

    /**
     * 记录上传图片下标
     */
    private int index;

    /**
     * 多张图片上传
     *
     * @param paths
     */
    private void uploadImages(final ArrayList<GoodsMovePicture> paths) {
        showLoading("正在上传图片(" + (index + 1) + "/" + paths.size() + ")");
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(context, paths.get(index).path, new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                GoodsMovePicture img = paths.get(index);
                img.picture_url = url;
                img.picture_type = type;
                index++;
                if (index < paths.size()) {
                    uploadImages(paths);
                } else {
                    dismissLoading();
                    Toast.show(context, "上传图片成功");
                }
            }

            @Override
            public void fail() {
                dismissLoading();
                Toast.show(context, "上传图片失败");
            }
        });
        asyncTask.execute();
    }

    /**
     * 记录加载图片数量
     */
    private int loadImageNum;

    private void loadLocalImage(final int index, final GoodsMovePicture gmp) {
        new Thread() {

            @Override
            public void run() {
                Bitmap bm = lessenUriImage(gmp.path);
                images.set(index, bm);
                loadImageNum++;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (deleteImageIndexs.size() > 0) {
                            showBackImage();
                        } else {
                            editGoodsImageAdapter.notifyDataSetChanged(images);
                        }
                        if (loadImageNum == images.size()) {
                            dismissLoading();
                            edit_goods_swipe.setRefreshing(false);
                            if (deleteImageIndexs.size() > 0) {
                                restoreImages();
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private void loadLocalImages(final ArrayList<GoodsMovePicture> paths) {
        new Thread() {

            @Override
            public void run() {
                Bitmap bm;
                for (int i = 0; i < paths.size(); i++) {
                    String path = paths.get(i).path;
                    bm = lessenUriImage(path);
                    images.add(bm);
                }
                loadImageNum = images.size();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        editGoodsImageAdapter.notifyDataSetChanged(images);
                        if (index >= paths.size()) {
                            dismissLoading();
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 通过路径将图片加载到内存中
     */
    public Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
    }

    /**
     * 其切换商品类型
     */
    private void switchGoodsType() {
        // 清空规格数据
        edit_goods_unit.setText("");
        goods.goods_unit = "";
        goods.goods_spec.clear();
        edit_goods_spec_list.removeAllViews();
        if (goods.goods_type == 1) {
            edit_goods_type.setText(R.string.clothing_type);
            edit_goods_spec_group.setVisibility(GONE);
            edit_goods_stock_hint.setVisibility(GONE);
            edit_goods_spec.setText("");
            specs.clear();

            edit_goods_color_group.setVisibility(VISIBLE);
            edit_goods_size_group.setVisibility(VISIBLE);
        } else {
            edit_goods_type.setText(R.string.shop_type);
            // 隐藏并清空颜色尺码数据
            edit_goods_color_group.setVisibility(GONE);
            edit_goods_size_group.setVisibility(GONE);
            edit_goods_color.setText("");
            edit_goods_size.setText("");
            colours.clear();
            sizes.clear();

            goods.goods_auxiliary_unit = "";
            goods.goods_auxiliary_unit_packingrate = 1;
            edit_goods_auxiliary_unit.setText("");

            edit_goods_spec_group.setVisibility(VISIBLE);
            edit_goods_stock_hint.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置颜色文本
     */
    private void setColorText() {
        if (colours != null && colours.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (GoodsColor colour : colours) {
                sb.append(colour.goods_colour_name).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            edit_goods_color.setText(sb.toString());
        } else {
            edit_goods_color.setText("");
        }
    }

    /**
     * 设置尺码文本
     */
    private void setSizeText() {
        // 微店模商品
        if (sizes != null && sizes.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (GoodsSize size : sizes) {
                sb.append(size.goods_size_name).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            edit_goods_size.setText(sb.toString());
        } else {
            edit_goods_size.setText("");
        }
    }

    /**
     * 设置规格文本
     */
    private void setSpecText() {
        if (specs != null && specs.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Spec spec : specs) {
                sb.append(spec.spec_name).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            edit_goods_spec.setText(sb.toString());
        } else {
            edit_goods_spec.setText("");
        }
    }

    /**
     * 初始化规格数据
     */
    private void initSpecData() {
        ArrayList<GoodsSpec> goods_specs = new ArrayList<>();
        if (goods.goods_type == 1) {
            // 新服装版商品
            if (ListUtils.isNotEmpty(colours) && ListUtils.isNotEmpty(sizes)) {
                for (GoodsColor colour : colours) {
                    for (GoodsSize size : sizes) {
                        addSpecData(goods_specs, colour.goods_colour_name, size.goods_size_name);
                    }
                }
            } else if (ListUtils.isNotEmpty(colours)) {
                for (GoodsColor colour : colours) {
                    addSpecData(goods_specs, colour.goods_colour_name, "");
                }
            } else if (ListUtils.isNotEmpty(sizes)) {
                for (GoodsSize size : sizes) {
                    addSpecData(goods_specs, "", size.goods_size_name);
                }
            }
        } else {
            if (ListUtils.isNotEmpty(specs)) {
                for (Spec spec : specs) {
                    addSpecData(goods_specs, spec.spec_name, goods.goods_unit, "");
                    if (TextUtil.isNotEmpty(goods.goods_auxiliary_unit)) {
                        addSpecData(goods_specs, spec.spec_name, goods.goods_unit, goods.goods_auxiliary_unit);
                    }
                }
            } else if (TextUtil.isNotEmpty(goods.goods_auxiliary_unit)) {
                addSpecData(goods_specs, "", goods.goods_unit, "");
                addSpecData(goods_specs, "", goods.goods_unit, goods.goods_auxiliary_unit);
            }
        }
        goods.goods_spec.clear();
        goods.goods_spec.addAll(goods_specs);
        goods.goods_price_section = SpecUtils.getPriceText(goods.goods_spec);
        goods.goods_cost_section = SpecUtils.getCostText(goods.goods_spec);
    }

    /**
     * 添加规格数据
     */
    private void addSpecData(ArrayList<GoodsSpec> goods_specs, String colour, String size) {
        GoodsSpec spec = null;
        for (GoodsSpec s : goods.goods_spec) {
            if (!TextUtil.equals(s.spec_colour, colour)) {
                continue;
            }
            if (!TextUtil.equals(s.spec_size, size)) {
                continue;
            }
            spec = s;
        }
        if (spec == null) {
            spec = new GoodsSpec();
            spec.goods_id = goods.goods_id;
            spec.goods_spec_id = RandomStringUtil.getOrderId();
            spec.spec_colour = colour;
            spec.spec_size = size;
            spec.goods_spec_name = spec.getSpecName();
        }
        spec.serial = goods_specs.size() + 1;
        goods_specs.add(spec);
    }

    /**
     * 添加规格数据
     */
    private void addSpecData(ArrayList<GoodsSpec> goods_specs, String spec_name, String unit, String auxiliary_unit) {
        GoodsSpec spec = null;
        for (GoodsSpec s : goods.goods_spec) {
            if (!TextUtil.equals(s.spec_name, spec_name)) {
                continue;
            }
            if (!TextUtil.equals(s.goods_unit, unit)) {
                continue;
            }
            if (!TextUtil.equals(s.goods_auxiliary_unit, auxiliary_unit)) {
                continue;
            }
            spec = s;
        }
        if (spec == null) {
            spec = new GoodsSpec();
            spec.goods_id = goods.goods_id;
            spec.goods_spec_id = RandomStringUtil.getOrderId();
            spec.spec_name = spec_name;
            spec.goods_unit = unit;
            spec.goods_auxiliary_unit = auxiliary_unit;
            spec.goods_auxiliary_unit_packingrate = goods.goods_auxiliary_unit_packingrate;
            spec.goods_spec_name = spec.getSpecName();
        }
        spec.serial = goods_specs.size() + 1;
        goods_specs.add(spec);
    }

    /**
     * 初始化规格视图列表
     */
    private void initSpecView() {
        setRefreshing(true);
        initSpecData();
        edit_goods_spec_list.removeAllViews();
        BigDecimal stock = BigDecimal.ZERO;
        for (GoodsSpec spec : goods.goods_spec) {
            WSpecViewHolder specViewHolder = new WSpecViewHolder(context, spec, true);
            edit_goods_spec_list.addView(specViewHolder.view);
            stock = NumberUtils.add(stock, new BigDecimal(spec.spec_number));
        }
        if (goods.goods_spec.size() > 0) {
            edit_goods_price_layout.setVisibility(GONE);
            edit_goods_cost_layout.setVisibility(GONE);
        } else {
            edit_goods_price_layout.setVisibility(VISIBLE);
            edit_goods_cost_layout.setVisibility(isShowCost ? VISIBLE : GONE);
        }
        edit_goods_stock.setEnabled(goods.goods_spec.size() <= 0);
        edit_goods_stock.setText(NumberFormatUtils.formatToInteger(stock));
        setRefreshing(false);
    }

    /**
     * 计算库存
     */
    public void calculateStock() {
        BigDecimal stock = BigDecimal.ZERO;
        for (GoodsSpec spec : goods.goods_spec) {
            stock = NumberUtils.add(stock, new BigDecimal(spec.spec_number));
        }
        if (goods.goods_spec.size() > 0) {
            edit_goods_stock.setEnabled(false);
        } else {
            edit_goods_stock.setEnabled(true);
        }
        edit_goods_stock.setText(NumberFormatUtils.formatToInteger(stock));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        images.clear();
        recycleImages();
    }

    /**
     * load显示状态设置
     */
    public void setRefreshing(boolean b) {
        edit_goods_swipe.setRefreshing(b);
        edit_goods_shield.setVisibility(b ? VISIBLE : GONE);
        if (b) {
            // 禁止滑动事件
            edit_goods_scroll_view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } else {
            edit_goods_scroll_view.setOnTouchListener(null);
        }
    }

    // 设置商品详情数据
    private void refreshView() {
        if (selectCatalog == null) {
            selectCatalog = new Catalog();
        }
        // 填充数据
        edit_goods_code.setText(goods.goods_no);
        edit_goods_name.setText(goods.goods_name);
        edit_goods_unit.setText(goods.goods_unit);
        if (goods.goods_type == 2) {// 切换到百货模式
            edit_goods_type.setText(R.string.shop_type);
            edit_goods_color_group.setVisibility(GONE);
            edit_goods_size_group.setVisibility(GONE);
            edit_goods_spec_group.setVisibility(VISIBLE);
            edit_goods_stock_hint.setVisibility(VISIBLE);
            edit_goods_auxiliary_unit.setText(goods.getAuxiliaryUnitText());
        }
        // 目录
        edit_goods_catalog.setText(goods.catalog_name);

        initSupplierView();

        if (TextUtil.isEmpty(selectCatalog.catalog_id)) {
            selectCatalog.catalog_id = goods.catalog_id;
            selectCatalog.catalog_name = goods.catalog_name;
        }
        edit_goods_now_putaway.setSelected(goods.sale_type == 1);
        edit_goods_in_warehouse.setSelected(goods.sale_type == 0);

        // 设置颜色 尺寸 库存 条码
        specs.clear();
        sizes.clear();
        colours.clear();
        edit_goods_spec_list.removeAllViews();
        if (ListUtils.isNotEmpty(goods.goods_spec)) {
            edit_goods_price_layout.setVisibility(GONE);
            edit_goods_cost_layout.setVisibility(GONE);
            for (int i = 0; i < goods.goods_spec.size(); i++) {
                GoodsSpec spec = goods.goods_spec.get(i);
                if (goods.goods_type == 1) {
                    if (isNotContainSize(spec.spec_size)) {
                        GoodsSize size = new GoodsSize();
                        size.goods_size_id = spec.spec_size;
                        size.goods_size_name = spec.spec_size;
                        sizes.add(size);
                    }
                    if (isNotContainColor(spec.spec_colour)) {
                        GoodsColor colour = new GoodsColor();
                        colour.goods_colour_id = spec.spec_colour;
                        colour.goods_colour_name = spec.spec_colour;
                        this.colours.add(colour);
                    }
                } else {
                    if (isNotContainSpec(spec.spec_name)) {
                        Spec s = new Spec();
                        s.spec_id = spec.spec_name;
                        s.spec_name = spec.spec_name;
                        this.specs.add(s);
                    }
                }
                // 添加视图
                WSpecViewHolder specViewHolder = new WSpecViewHolder(context, spec, true);
                edit_goods_spec_list.addView(specViewHolder.view);
            }
            setSizeText();
            setColorText();
            setSpecText();
            calculateStock();
        } else {
            edit_goods_price.setText(goods.goods_price_section);
            edit_goods_cost.setText(goods.goods_cost_section);
        }
    }

    private ArrayList<GoodsSupplierItemView> goodsSupplierItemViews = new ArrayList<>();

    /**
     * 初始化供应商视图
     */
    private void initSupplierView() {
        if (ListUtils.isNotEmpty(goods.goods_supplier)) {
            edit_goods_supplier.setText("选择供应商" + goods.goods_supplier.size());

            goodsSupplierItemViews.clear();
            edit_goods_supplier_list.removeAllViews();

            for (int i = 0; i < goods.goods_supplier.size(); i++) {
                GoodsSupplier supplier = goods.goods_supplier.get(i);

                final GoodsSupplierItemView matchView = new GoodsSupplierItemView(context, supplier);
                edit_goods_supplier_list.addView(matchView.getRootView());
                goodsSupplierItemViews.add(matchView);
                if (supplier.has_default == 1) {
                    goods.main_supplier_id = supplier.supplier_id;
                    goods.main_supplier_name = supplier.supplier_shortname;
                    matchView.setTagSelect(true);
                }

                matchView.setOnDataUpdateListener(new GoodsSupplierItemView.OnDataUpdateListener() {

                    @Override
                    public void onTagClick(GoodsSupplier supplier, View view) {
                        if (supplier != null) {
                            goods.main_supplier_id = supplier.supplier_id;
                            goods.main_supplier_name = supplier.supplier_shortname;
                        }
                        if (view != null) {
                            for (GoodsSupplierItemView goodsSupplierItemView : goodsSupplierItemViews) {
                                if (view.equals(goodsSupplierItemView.getRootView())) {
                                    goodsSupplierItemView.setTagSelect(true);
                                } else {
                                    goodsSupplierItemView.setTagSelect(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onDeleteItem(GoodsSupplier supplier, View view) {
                        goods.goods_supplier.remove(supplier);
                        edit_goods_supplier_list.removeView(view);
                        goodsSupplierItemViews.remove(matchView);
                        if (goods.goods_supplier.size() == 0) {
                            edit_goods_supplier_list.removeAllViews();
                            edit_goods_supplier.setText("选择供应商");
                        } else {
                            edit_goods_supplier.setText("供应商" + goods.goods_supplier.size());
                            // 如果被删除的是默认供应商，则重新指定一个默认供应商
                            if (supplier.has_default == 1) {
                                goodsSupplierItemViews.get(0).setTagSelect(true);
                                GoodsSupplier gs = goods.goods_supplier.get(0);
                                goods.main_supplier_id = gs.supplier_id;
                                goods.main_supplier_name = gs.supplier_shortname;
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 是否不包含这个尺码
     */
    private boolean isNotContainSize(String size) {
        if (TextUtil.isEmpty(size)) {
            return false;
        }
        for (GoodsSize s : sizes) {
            if (TextUtil.equals(s.goods_size_name, size)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否不包含这个颜色
     */
    private boolean isNotContainColor(String color) {
        if (TextUtil.isEmpty(color)) {
            return false;
        }
        for (GoodsColor colour : colours) {
            if (TextUtil.equals(colour.goods_colour_name, color)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否不包含这个规格
     */
    private boolean isNotContainSpec(String specName) {
        if (TextUtil.isEmpty(specName)) {
            return false;
        }
        for (Spec spec : specs) {
            if (TextUtil.equals(spec.spec_name, specName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量加载图片
     */
    private void loadImages(boolean isRefresh) {
        if (ListUtils.isEmpty(goods.goods_move_picture)) {
            edit_goods_swipe.setRefreshing(false);
            return;
        }
        // 为图片添加空位
        images.clear();
        for (int i = 0; i < goods.goods_move_picture.size(); i++) {
            GoodsMovePicture gmp = goods.goods_move_picture.get(i);
            if (gmp != null) {
                images.add(null);
                backImages.add(null);
                if (TextUtil.isEmpty(gmp.path)) {
                    gmp.path = FileUtils.getNewImagePath(gmp.picture_url);
                }
            } else {
                goods.goods_move_picture.remove(i);
            }
        }
        editGoodsImageAdapter.notifyDataSetChanged(images);

        showLoading();
        // 异步加载图片
        loadImageNum = 0;
        for (int i = 0; i < goods.goods_move_picture.size(); i++) {
            GoodsMovePicture imageData = goods.goods_move_picture.get(i);
            if (isRefresh && !imageFileExists(imageData.path)) {
                loadImage(i, imageData.picture_url);
            } else {
                loadLocalImage(i, imageData);
            }
        }
    }

    /**
     * 加载图片
     */
    private void loadImage(final int index, String pic) {
        Glide.with(this).load(pic).asBitmap().into(new SimpleTarget<Bitmap>(250, 250) {

            @Override
            public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
                LogUtils.i("加载图片成功");
                loadImageNum++;
                images.set(index, loadedImage);
                ImageLoaderUtils.saveImageNotRecycle(loadedImage, goods.goods_move_picture.get(index).path);
                if (deleteImageIndexs.size() > 0) {
                    showBackImage();
                } else {
                    editGoodsImageAdapter.notifyDataSetChanged(images);
                }
                // 如果在加载的过程有删除过图片，则需要还原数据集合
                if (loadImageNum == images.size()) {
                    edit_goods_swipe.setRefreshing(false);
                    dismissLoading();
                    if (deleteImageIndexs.size() > 0) {
                        restoreImages();
                    }
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                LogUtils.i("加载图片取消");
                loadImageNum++;
                if (loadImageNum == images.size()) {
                    edit_goods_swipe.setRefreshing(false);
                    dismissLoading();
                    if (deleteImageIndexs.size() > 0) {
                        restoreImages();
                    }
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                LogUtils.i("加载图片失败");
                loadImageNum++;
                if (loadImageNum == images.size()) {
                    edit_goods_swipe.setRefreshing(false);
                    dismissLoading();
                    if (deleteImageIndexs.size() > 0) {
                        restoreImages();
                    }
                }
            }
        });
    }

    /**
     * 恢复图片正常显示
     */
    private void restoreImages() {
        Collections.sort(deleteImageIndexs, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (Integer i : deleteImageIndexs) {
            if (i != null) {
                images.remove(i.intValue());
                goods.goods_move_picture.remove(i.intValue());
            }
        }
        deleteImageIndexs.clear();
        editGoodsImageAdapter.notifyDataSetChanged(images);
    }

    /**
     * 图片还没加载完，显示备份图片
     */
    private void showBackImage() {
        backImages.clear();
        backImages.addAll(images);
        Collections.sort(deleteImageIndexs, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (Integer i : deleteImageIndexs) {
            if (i != null) {
                backImages.remove(i.intValue());
            }
        }
        editGoodsImageAdapter.notifyDataSetChanged(backImages);
    }

    /**
     * 剪切图片
     */
    private int shearPosition = -1;

    public void shearPhoto(int position) {
        shearPosition = position;
        File tempFile = new File(goods.goods_move_picture.get(position).path);
        startPhotoZoom(Uri.fromFile(tempFile));
    }

    /**
     * 剪切图片功能
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = ImageManage.getPath(this.getApplication(), uri);
            if (url != null) {
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 设置裁剪  
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, REQUEST_SHEAR);
    }

    /**
     * 判断是否填写了数据
     */
    private boolean hasEdit() {
        boolean b = false;
        // 图片
        if (ListUtils.isNotEmpty(goods.goods_move_picture)) {
            b = true;
        }
        // 货号
        if (TextUtil.isNotEmpty(edit_goods_code.getText())) {
            b = true;
        }
        // 名称
        if (TextUtil.isNotEmpty(edit_goods_name.getText())) {
            b = true;
        }
        // 名称
        if (TextUtil.isNotEmpty(edit_goods_unit.getText())) {
            b = true;
        }
        // 单价
        if (TextUtil.isNotEmpty(edit_goods_price.getText())) {
            b = true;
        }
        // 成本
        if (TextUtil.isNotEmpty(edit_goods_cost.getText())) {
            b = true;
        }
        // 目录
        if (TextUtil.isNotEmpty(edit_goods_catalog.getText())) {
            b = true;
        }
        // 规格
        if (ListUtils.isNotEmpty(goods.goods_spec)) {
            b = true;
        }
        // 总库存
        if (NumberUtils.isNotZero(edit_goods_stock.getText().toString())) {
            b = true;
        }
        // 供应商
        if (ListUtils.isNotEmpty(goods.goods_supplier)) {
            b = true;
        }

        return b;
    }

    /**
     * 保存数据
     */
    private void keepAddGoods() {
        SharedPreferencesUtils.put(Constant.ADD_GOODS_SAVE, true);
        FileManage.saveObject(MenuConstant.ADD_GOODS_INFO_PATH, goods);
    }

    private void visibleSaveData() {
        Object obj = FileManage.restoreObject(MenuConstant.ADD_GOODS_INFO_PATH);

        if (obj == null) {
            setRefreshing(false);
            return;
        }

        if (obj instanceof Goods) {
            goods = (Goods) obj;
            // 设置图片数据
            images.clear();
            if (ListUtils.isNotEmpty(goods.goods_move_picture)) {
                showLoading("正在加载图片。。。");
                loadImages(true);
            }

            refreshView();
        }
    }

    /**
     * 判断图片文件是否存在
     */
    private boolean imageFileExists(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void movePhotoPosition(int fromPosition, int toPosition) {
        ListUtils.move(goods.goods_move_picture, fromPosition, toPosition);
        ListUtils.move(images, fromPosition, toPosition);
        // 改变显示排序
        editGoodsImageAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 拖拽
     */
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        /**
         * 指定可以支持的拖放和滑动的方向
         * List类型的RecyclerView，拖拽只有UP、DOWN
         * Grid类型的则有UP、DOWN、LEFT、RIGHT四个方向
         * <p/>
         * dragFlags 是拖拽标志，swipeFlags是滑动标志，在Grid中把swipeFlags设置为0，表示不处理滑动操作。
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 设置不支持长按拖拽
            if (viewHolder instanceof EditGoodsImageAdapter.ImageHolder) {
                if (!((EditGoodsImageAdapter.ImageHolder) viewHolder).touchFlg) {
                    return makeMovementFlags(0, 0);
                }
            }
            // 判断recyclerView传入的LayoutManager是不是GridLayoutManager
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                // Grid的拖拽方向，上、下、左、右
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // 设置为0，表示不支持滑动
                final int swipeFlags = 0;
                // 创建拖拽或者滑动标志的快速方式
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                // List类型的拖拽方向，上或者下
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                // List类型支持滑动，左或者右
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                // 创建拖拽或者滑动标志的快速方式
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        /**
         * 告诉ItemTouchHelper是否需要RecyclerView支持长按拖拽,返回true是支持，false是不支持
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        /**
         * 当拖拽开始的时候调用
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            // 拖拽的时候改变一下选中Item的颜色
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof EditGoodsImageAdapter.ImageHolder) {
                    // 让ViewHolder知道Item开始选中
                    EditGoodsImageAdapter.ImageHolder itemViewHolder = (EditGoodsImageAdapter.ImageHolder) viewHolder;
                    // 回调ItemTouchHelperVIewHolder的方法
                    itemViewHolder.onItemSelected();
                    // 禁止下拉刷新事件
                    edit_goods_swipe.setEnabled(false);
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 当拖拽结束的时候调用
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1);
            if (viewHolder instanceof EditGoodsImageAdapter.ImageHolder) {
                EditGoodsImageAdapter.ImageHolder itemViewHolder = (EditGoodsImageAdapter.ImageHolder) viewHolder;
                // Item移动完成之后的回调
                itemViewHolder.onItemClear();
                // 恢复下拉刷新事件
                edit_goods_swipe.setEnabled(true);

                editGoodsImageAdapter.notifyDataSetChanged(images);
            }
        }

        /**
         * 当ItemTouchHelper要拖动的Item从原来位置拖动到新的位置的时候调用
         * 当我们拖拽的时候调用这个方法
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (target instanceof EditGoodsImageAdapter.ImageHolder) {
                if (!((EditGoodsImageAdapter.ImageHolder) target).touchFlg) {
                    return false;
                }
            }

            // 如果两个item不是一个类型的，我们让他不可以拖拽
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }

            // 得到拖动ViewHolder的position
            int fromPosition = viewHolder.getAdapterPosition();
            // 得到目标ViewHolder的position
            int toPosition = target.getAdapterPosition();

            movePhotoPosition(fromPosition, toPosition);

            return true;
        }

        /**
         * 当ViewHolder滑动的时候调用
         * 当滑动Item的时候调用此方法
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        /**
         * 由 ItemTouchHelper 在 RecyclerView 的 onDraw方法中回调调用
         */
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    });

}
