package com.poso2o.lechuan.activity.wshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.print.PrintBarcodeActivity;
import com.poso2o.lechuan.adapter.GoodsDetailsImgsAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.BatchUpGoodsBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsMovePicture;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.bean.goodsdata.OldSpec;
import com.poso2o.lechuan.bean.printer.GoodsBarcodePrintData;
import com.poso2o.lechuan.bean.printer.GoodsDetailsImgsData;
import com.poso2o.lechuan.bean.printer.GoodsDetailsNumsData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.GoodsDetailsImgsItemView;
import com.poso2o.lechuan.view.GoodsSupplierItemView;
import com.poso2o.lechuan.view.NumsDataView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class WGoodsDetailActivity extends BaseActivity {

    public static final int REQUEST_EDIT_GOODS = 12;

    private Context context;

    // 返回
    private ImageView goods_details_back;

    // 商品图片
    private ViewPager goods_details_imgs;
    private GoodsDetailsImgsAdapter goodsDetailsImgsAdapter;
    private ArrayList<GoodsDetailsImgsItemView> imgsData;
    private LinearLayout goods_details_spots;
    private ArrayList<ImageView> spotViews;

    // 展示数据
    private TextView goods_details_name;
    private TextView goods_details_nums;
    private TextView goods_details_price;
    private TextView goods_details_bh;
    private TextView goods_details_rename;
    private TextView goods_details_unit;
    private TextView goods_details_auxiliary_unit;
    private TextView goods_details_num_info;
    private TextView goods_details_reprice;
    private TextView goods_details_cost;
    private TextView goods_details_catalog;
    private TextView goods_details_salesnum;
    private TextView goods_details_stock;

    // 单位布局
    private View goods_details_unit_group;

    // 价格
    private LinearLayout goods_details_price_group;
    private LinearLayout goods_details_cost_group;

    // 规格标签
    private TextView goods_details_spec_tag;
    private View goods_details_barcode_tag;
    private View goods_details_price_tag;
    private View goods_details_cost_tag;

    // 供应商
    private LinearLayout goods_details_supplier_group;
    private LinearLayout goods_details_supplier_list;

    // 规格信息
    private LinearLayout goods_details_spec_group;
    private LinearLayout goods_details_spec_list;

    // 重新编辑
//    private TextView goods_details_edit;
    private TextView goods_details_offline;

    // 上下架
    private TextView goods_details_sale_type;

    // 分享
    private TextView goods_details_share;

    // 商品详情数据
    private Goods goods;

    // 打印
    private TextView goods_details_print;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void initView() {
        context = this;

        // 返回
        goods_details_back = findView(R.id.goods_details_back);
        // 商品图片
        goods_details_imgs = findView(R.id.goods_details_imgs);
        goods_details_spots = findView(R.id.goods_details_spots);

        // 展示数据
        goods_details_name = findView(R.id.goods_details_name);
        goods_details_nums = findView(R.id.goods_details_nums);
        goods_details_price = findView(R.id.goods_details_price);
        goods_details_bh = findView(R.id.goods_details_bh);
        goods_details_rename = findView(R.id.goods_details_rename);
        goods_details_unit = findView(R.id.goods_details_unit);
        goods_details_auxiliary_unit = findView(R.id.goods_details_auxiliary_unit);
        goods_details_num_info = findView(R.id.goods_details_num_info);
        goods_details_reprice = findView(R.id.goods_details_reprice);
        goods_details_cost = findView(R.id.goods_details_cost);
        goods_details_catalog = findView(R.id.goods_details_catalog);
        goods_details_salesnum = findView(R.id.goods_details_salesnum);
        goods_details_stock = findView(R.id.goods_details_stock);

        goods_details_unit_group = findView(R.id.goods_details_unit_group);

        goods_details_price_group = findView(R.id.goods_details_price_group);
        goods_details_cost_group = findView(R.id.goods_details_cost_group);

        goods_details_spec_tag = findView(R.id.goods_details_spec_tag);
        goods_details_barcode_tag = findView(R.id.goods_details_barcode_tag);
        goods_details_price_tag = findView(R.id.goods_details_price_tag);
        goods_details_cost_tag = findView(R.id.goods_details_cost_tag);

        goods_details_supplier_group = findView(R.id.goods_details_supplier_group);
        goods_details_supplier_list = findView(R.id.goods_details_supplier_list);

        // 规格信息
        goods_details_spec_group = findView(R.id.goods_details_spec_group);
        goods_details_spec_list = findView(R.id.goods_details_spec_list);

        // 编辑商品
        goods_details_offline = findView(R.id.goods_details_offline);

        // 打印
        goods_details_print = findView(R.id.goods_details_print);

        // 上下架
        goods_details_sale_type = findView(R.id.goods_details_sale_type);

        // 分享
        goods_details_share = findView(R.id.goods_details_share);
    }

    @Override
    public void initData() {
        // 商品图片
        imgsData = new ArrayList<>();
        goodsDetailsImgsAdapter = new GoodsDetailsImgsAdapter(context, imgsData);
        goods_details_imgs.setAdapter(goodsDetailsImgsAdapter);
        spotViews = new ArrayList<>();

        // 获取数据
        goods = (Goods) getIntent().getSerializableExtra(Constant.DATA);

        refreshVdianView();
    }

    /**
     * 获取商品详情数据成功，初始化微店商品数据
     */
    public void refreshVdianView() {
        if (goods == null) {
            Toast.show(context, R.string.toast_goods_data_lose);
            return;
        }

        imgsData.retainAll(imgsData);
        goods_details_spots.removeAllViews();
        spotViews.removeAll(spotViews);
        if (goods.goods_move_picture != null && goods.goods_move_picture.size() > 0) {
            for (GoodsMovePicture img : goods.goods_move_picture) {
                GoodsDetailsImgsItemView imgView = new GoodsDetailsImgsItemView(context, img.picture_url);
                imgsData.add(imgView);
                Log.i("img_url", "图片链接" + img.picture_url);

                // 设置点
                View spotView = View.inflate(context, R.layout.item_goods_details_spot, null);
                ImageView iv = (ImageView) spotView.findViewById(R.id.item_goods_details_spot);
                spotViews.add(iv);
                goods_details_spots.addView(spotView);
            }
            // 只有一张图片的时候隐藏显示点
            if (goods.goods_move_picture.size() == 1) {
                goods_details_spots.setVisibility(GONE);
            } else {
                goods_details_spots.setVisibility(VISIBLE);
            }
        }

        // 初始化点状态
        for (int i = 0; i < spotViews.size(); i++) {
            if (i == 0) {
                spotViews.get(i).setSelected(true);
            } else {
                spotViews.get(i).setSelected(false);
            }
        }
        goodsDetailsImgsAdapter.notify(imgsData);

        // 设置当前显示图片
        goods_details_imgs.setCurrentItem(0);

        if (goods.goods_spec == null) {
            goods.goods_spec = new ArrayList<>();
        }

        goods_details_name.setText(goods.goods_name);
        goods_details_nums.setText(goods.goods_spec.size() + "款可选");
//        goods_details_price.setText("￥" + goods.goods_price_section);
        goods_details_price.setText("￥" + goods.price);
        goods_details_bh.setText(goods.goods_no);
        goods_details_rename.setText(goods.goods_name);
        goods_details_unit.setText(goods.goods_unit);
        goods_details_num_info.setText(goods.goods_spec.size() + "款可选");
        goods_details_catalog.setText(goods.catalog_name);
        goods_details_salesnum.setText(Integer.toString(goods.goods_sale_number));
        goods_details_stock.setText(Integer.toString(goods.goods_number));

//        if (goods.goods_type == 2) {
//            goods_details_auxiliary_unit.setText(goods.getAuxiliaryUnitText());
//        }

        /*if (goods.goods_spec.size() == 1 && TextUtils.equals(GoodsSpec.NOT_SPEC, goods.goods_spec.get(0).goods_spec_name)) {
            goods_details_spec_group.setVisibility(GONE);
            goods_details_price_group.setVisibility(VISIBLE);
            goods_details_cost_group.setVisibility(VISIBLE);
            goods_details_cost.setText(goods.goods_cost_section);

            goods_details_reprice.setText(goods.goods_price_section);
        } else {
            goods_details_spec_group.setVisibility(VISIBLE);
            goods_details_price_group.setVisibility(GONE);
            goods_details_cost_group.setVisibility(GONE);

            goods_details_spec_list.removeAllViews();
            for (int i = 0; i < goods.goods_spec.size(); i++) {
                GoodsSpec spec = goods.goods_spec.get(i);
                NumsDataView numsDataView = new NumsDataView(context, spec, i + 1);
                goods_details_spec_list.addView(numsDataView.getRootView());
            }
        }*/

        goods_details_spec_group.setVisibility(VISIBLE);
        goods_details_price_group.setVisibility(GONE);
        goods_details_cost_group.setVisibility(GONE);

        goods_details_spec_list.removeAllViews();
        for (int i = 0; i < goods.goods_spec.size(); i++) {
            GoodsSpec spec = goods.goods_spec.get(i);
            NumsDataView numsDataView = new NumsDataView(context, spec, i + 1);
            goods_details_spec_list.addView(numsDataView.getRootView());
        }

        initSupplierView();
    }

    /**
     * 初始化实体店商品数据
     */
    private void refreshRealView() {
        if (goods == null) {
            Toast.show(context, R.string.toast_goods_data_lose);
            return;
        }

        imgsData.clear();
        goods_details_spots.removeAllViews();
        spotViews.clear();
        if (goods.imgs != null && goods.imgs.size() > 0) {
            for (GoodsDetailsImgsData img : goods.imgs) {
                GoodsDetailsImgsItemView imgView = new GoodsDetailsImgsItemView(context, img.pic);
                imgsData.add(imgView);

                // 设置点
                View spotView = View.inflate(context, R.layout.item_goods_details_spot, null);
                ImageView iv = (ImageView) spotView.findViewById(R.id.item_goods_details_spot);
                spotViews.add(iv);
                goods_details_spots.addView(spotView);
            }
            // 只有一张图片的时候隐藏显示点
            if (goods.goods_move_picture.size() == 1) {
                goods_details_spots.setVisibility(GONE);
            } else {
                goods_details_spots.setVisibility(VISIBLE);
            }
        }

        // 初始化点状态
        for (int i = 0; i < spotViews.size(); i++) {
            if (i == 0) {
                spotViews.get(i).setSelected(true);
            } else {
                spotViews.get(i).setSelected(false);
            }
        }
        goodsDetailsImgsAdapter.notify(imgsData);

        // 设置当前的goods_details_imgs_viewpager显示图片
        goods_details_imgs.setCurrentItem(0);

        if (goods.nums == null) {
            goods.nums = new ArrayList<>();
        }

        goods_details_name.setText(goods.name);
        goods_details_nums.setText(goods.nums.size() + "款可选");
        goods_details_price.setText("￥" + NumberFormatUtils.format(goods.price));
        goods_details_reprice.setText(NumberFormatUtils.format(goods.price));
        goods_details_cost.setText(NumberFormatUtils.format(goods.fprice));
        goods_details_bh.setText(goods.bh);
        goods_details_rename.setText(goods.name);
        goods_details_unit.setText(goods.unit);
        goods_details_num_info.setText(goods.nums.size() + "款可选");
        goods_details_catalog.setText(goods.directory);
        goods_details_salesnum.setText(goods.salesnum);
        goods_details_stock.setText(goods.kcnum);

        goods_details_spec_tag.setText(R.string.spec);
        goods_details_barcode_tag.setVisibility(View.VISIBLE);
        goods_details_price_tag.setVisibility(View.GONE);
        goods_details_cost_tag.setVisibility(View.GONE);
        goods_details_spec_list.removeAllViews();
        for (int i = 0; i < goods.nums.size(); i++) {
            OldSpec spec = goods.nums.get(i);
            NumsDataView numsDataView = new NumsDataView(context, spec, i + 1);
            goods_details_spec_list.addView(numsDataView.getRootView());
        }
    }

    private void initSupplierView() {
        if (goods.goods_supplier == null || goods.goods_supplier.size() == 0) {
            goods_details_supplier_group.setVisibility(GONE);
        } else {
            // 修改商品时显示删除按钮
            goods_details_supplier_list.removeAllViews();
            // 统计数据
            for (int i = 0; i < goods.goods_supplier.size(); i++) {
                GoodsSupplier supplier = goods.goods_supplier.get(i);

                GoodsSupplierItemView matchView = new GoodsSupplierItemView(context, supplier);
                goods_details_supplier_list.addView(matchView.getRootView());
            }
        }
    }

    @Override
    public void initListener() {
        // 打印
        goods_details_print.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                toPrintBarcodeActivity();
            }
        });

        // 编辑商品
        goods_details_offline.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
//                Bundle data = new Bundle();
//                data.putSerializable(Constant.GOODS, goods);
//                data.putInt(Constant.TYPE, Constant.UPDATE);
//                startActivityForResult(WEditGoodsActivity.class, data, REQUEST_EDIT_GOODS);
                importGoods();
            }
        });

        // 返回
        goods_details_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });

        // 滑动改变点的状态
        goods_details_imgs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSpotTypeChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    // 正在滑动 pager处于正在拖拽中
//                    goods_details_swipe.setEnabled(false);
//                } else {
//                    goods_details_swipe.setEnabled(true);
//                }
            }
        });

    }

    private void switchSaleType() {
        showLoading("正在提交商品信息...");
        goods.sale_type = goods.sale_type == 1 ? 0 : 1;

        ArrayList<BatchUpGoodsBean> goodsBeen = new ArrayList<>();
        BatchUpGoodsBean bean = new BatchUpGoodsBean();
        bean.goods_id = goods.goods_id;
        goodsBeen.add(bean);

        VdianGoodsManager.getInstance().putaway(this, goodsBeen, goods.sale_type + "", new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
                goods.sale_type = goods.sale_type == 1 ? 0 : 1;
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                if (goods.sale_type == 1) {
                    goods_details_sale_type.setText("下架");
                } else {
                    goods_details_sale_type.setText("上架");
                }
            }
        });
    }

    /**
     * 改变点的状态
     */
    private void setSpotTypeChange(int position) {
        if (spotViews.size() < position || spotViews.size() == position) {
            return;
        }

        for (int i = 0; i < spotViews.size(); i++) {
            if (i == position) {
                spotViews.get(i).setSelected(true);
            } else {
                spotViews.get(i).setSelected(false);
            }
        }
    }

    /**
     * 跳转打印界面
     */
    private void toPrintBarcodeActivity() {
        // 封装数据
        ArrayList<GoodsBarcodePrintData> datas = new ArrayList<>();
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

        startActivity(PrintBarcodeActivity.class, datas);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_EDIT_GOODS:
                        goods = (Goods) data.getSerializableExtra(Constant.GOODS);
                        refreshVdianView();
                        setResult(RESULT_OK);
                        break;
                }
            } else if (resultCode == RESULT_CANCELED) {
//                Goods goods = (Goods) data.getSerializableExtra(Constant.GOODS);
//                Goods goodsData = getGoodsDataManage().findGoods(this.goods.goods_id);
//                if (goods != null && goods.goods_move_picture != null) {
//                    if (goods.goods_move_picture.size() != goodsData.goods_move_picture.size()) {
//                        return;
//                    }
//                    for (GoodsMovePicture gmp : goods.goods_move_picture) {
//                        for (GoodsMovePicture gmp2 : goodsData.goods_move_picture) {
//                            if (TextUtils.equals(gmp.picture_url, gmp2.picture_url)) {
//                                gmp2.path = gmp.path;
//                            }
//                        }
//                    }
//                    this.goods = goodsData;
//                    setResult(RESULT_OK);
//                }
            }
        }
    }

    // 分享商品
    private void shareWeb() {
        UMImage thumb = new UMImage(this, goods.main_picture);
        UMWeb web = new UMWeb(goods.share_url);
        web.setThumb(thumb);
        web.setTitle(goods.goods_name);
        new ShareAction(this).withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).share();
    }

    /**
     * 设置商品下架，去掉微信商品标识
     */
    private void importGoods() {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("goods_id", goods.goods_id);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showLoading("正在下架商品...");
        WShopManager.getrShopManager().importGoods((BaseActivity) context, array.toString(), true, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                ((BaseActivity) context).setResult(Activity.RESULT_OK);
                ((BaseActivity) context).finish();
                Toast.show(context, "下架成功！");
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
