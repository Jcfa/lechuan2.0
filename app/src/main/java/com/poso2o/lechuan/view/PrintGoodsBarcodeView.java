package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.printer.GoodsBarcodePrintData;
import com.poso2o.lechuan.bean.printer.GoodsDetailsNumsData;
import com.poso2o.lechuan.bean.printer.PrintNumsItemData;
import com.poso2o.lechuan.util.PrintGoodsBarcodeViewP;

import java.util.ArrayList;

/**
 *
 * Created by lenovo on 2016/12/24.
 */
public class PrintGoodsBarcodeView extends BaseView {
    private View view;
    private PrintGoodsBarcodeViewP printGoodsBarcodeViewP;
    private GoodsBarcodePrintData goodsBarcodePrintData;

    //数据展示 商品基本数据
    private ImageView print_barcode_goods_img;
    private TextView print_barcode_goods_name;
    private TextView print_barcode_goods_bh;
    private TextView print_barcode_goods_price;

    private LinearLayout print_barcode_nums_group;
    private ArrayList<PrintNumsItemData> printNumsItemDatas;
    private ArrayList<PrintBarcodeNumItemView> printBarcodeNumItemViews;

    public PrintGoodsBarcodeView(Context context, GoodsBarcodePrintData goodsBarcodePrintData) {
        super(context);
        this.goodsBarcodePrintData = goodsBarcodePrintData;
    }

    @Override
    public View initGroupView() {
        return view = View.inflate(context, R.layout.view_print_goods_barcode, null);
    }

    @Override
    public void initView() {
        print_barcode_nums_group = (LinearLayout) view.findViewById(R.id.print_barcode_nums_group);

        // 数据展示 商品基本数据
        print_barcode_goods_img = (ImageView) view.findViewById(R.id.print_barcode_goods_img);
        print_barcode_goods_name = (TextView) view.findViewById(R.id.print_barcode_goods_name);
        print_barcode_goods_bh = (TextView) view.findViewById(R.id.print_barcode_goods_bh);
        print_barcode_goods_price = (TextView) view.findViewById(R.id.print_barcode_goods_price);
    }

    @Override
    public void initData() {
        printGoodsBarcodeViewP = new PrintGoodsBarcodeViewP(context);
//        loaderImageNet = new LoaderImageNet(R.color.load_default_color, R.color.load_default_color, true, false, new FadeInBitmapDisplayer(300));
        printNumsItemDatas = new ArrayList<>();

        initPrintData();
    }

    /**
     * 初始化数据
     */
    private void initPrintData() {
        // 商品基本数据
        if (goodsBarcodePrintData.imgs != null && goodsBarcodePrintData.imgs.size() > 0) {
            Glide.with(context).load(goodsBarcodePrintData.imgs.get(0).pic).into(print_barcode_goods_img);
        }

        print_barcode_goods_name.setText(goodsBarcodePrintData.name);
        print_barcode_goods_bh.setText(goodsBarcodePrintData.bh);
        print_barcode_goods_price.setText("￥" + goodsBarcodePrintData.price);

        //商品条码数据
        if (printNumsItemDatas == null) {
            printNumsItemDatas = new ArrayList<>();
        }
        if (goodsBarcodePrintData.nums != null && goodsBarcodePrintData.nums.size() > 0) {
            for (GoodsDetailsNumsData g : goodsBarcodePrintData.nums) {
                PrintNumsItemData p = new PrintNumsItemData();
                p.barcode = g.barcode;
                p.spec_name = g.spec_name;
                p.price = g.price;
                p.num = g.num;
                printNumsItemDatas.add(p);
            }
        }

        if (printBarcodeNumItemViews == null) {
            printBarcodeNumItemViews = new ArrayList<>();
        } else {
            printBarcodeNumItemViews.clear();
        }

        print_barcode_nums_group.removeAllViews();
        for (int i = 0; i < printNumsItemDatas.size(); i++) {
            PrintBarcodeNumItemView p = new PrintBarcodeNumItemView(context, printNumsItemDatas.get(i));
            printBarcodeNumItemViews.add(p);
            print_barcode_nums_group.addView(p.getRootView());
        }
    }

    /**
     * 获取选中商品的数量
     *
     * @return
     */
    public int getNumber() {
        return printGoodsBarcodeViewP.getNumber(printBarcodeNumItemViews);
    }

    /**
     * 所有item项全选
     */
    public void allItemSelect(boolean b) {
        printGoodsBarcodeViewP.allItemSelect(printBarcodeNumItemViews, b);
    }

    /**
     * 改变所有item项的数量
     *
     * @param i
     */
    public void notifyItemNum(int i) {
        printGoodsBarcodeViewP.notifyItemNum(printBarcodeNumItemViews, i);
    }

    /**
     * 获取商品名
     */
    public String getGoodsName() {
        return goodsBarcodePrintData.name;
    }

    /**
     * 获取商品货号
     */
    public String getGoodsBh() {
        return goodsBarcodePrintData.bh;
    }

    public ArrayList<PrintBarcodeNumItemView> getPrintBarcodeNumItemViews() {
        return printBarcodeNumItemViews;
    }

}