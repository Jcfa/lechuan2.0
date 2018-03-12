package com.poso2o.lechuan.util;

import android.content.Context;

import com.poso2o.lechuan.bean.printer.PrintData;
import com.poso2o.lechuan.bean.printer.PrintNumsItemData;
import com.poso2o.lechuan.view.PrintBarcodeNumItemView;
import com.poso2o.lechuan.view.PrintGoodsBarcodeView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/24.
 */

public class PrintBarcodeActivityP {

    private Context context;

    public PrintBarcodeActivityP(Context context) {
        this.context = context;
    }

    /**
     * 获取总选择打印的数量
     *
     * @param printGoodsBarcodeViews
     * @return
     */
    public int notifyAllPrintNum(ArrayList<PrintGoodsBarcodeView> printGoodsBarcodeViews) {
        int i = 0;
        for (PrintGoodsBarcodeView p : printGoodsBarcodeViews) {
            i += p.getNumber();
        }
        return i;
    }

    /**
     * 全选
     *
     * @param printGoodsBarcodeViews
     */
    public void allSelect(ArrayList<PrintGoodsBarcodeView> printGoodsBarcodeViews, boolean b) {
        for (PrintGoodsBarcodeView p : printGoodsBarcodeViews) {
            p.allItemSelect(b);
        }
    }

    /**
     * 修改所有item项目的数量
     *
     * @param i
     */
    public void notifyNum(ArrayList<PrintGoodsBarcodeView> printGoodsBarcodeViews, int i) {
        for (PrintGoodsBarcodeView p : printGoodsBarcodeViews) {
            p.notifyItemNum(i);

        }
    }


    /**
     * 商品打印信息封装
     *
     * @param printGoodsBarcodeViews 商品视图
     * @param printDatas
     * @return
     */
    public ArrayList<PrintData> initPrintData(ArrayList<PrintGoodsBarcodeView> printGoodsBarcodeViews, ArrayList<PrintData> printDatas) {
        printDatas.clear();
        if (printGoodsBarcodeViews == null || printGoodsBarcodeViews.size() < 1) {
            Toast.show(context,"没有可以打印的商品");
            return null;
        }
        for (PrintGoodsBarcodeView p : printGoodsBarcodeViews) {
            ArrayList<PrintBarcodeNumItemView> printBarcodeItemViews = p.getPrintBarcodeNumItemViews();
            for (PrintBarcodeNumItemView p1 : printBarcodeItemViews) {
                if (p1.getPrintNumsItemData().isSelect) {
                    PrintNumsItemData printNumsItemData = p1.getPrintNumsItemData();
                    if (printNumsItemData != null) {
                        PrintData printdata = new PrintData();
                        printdata.bh = p.getGoodsBh();
                        printdata.name = p.getGoodsName();
                        printdata.print_num = p1.getNumber();

                        printdata.spec_name = printNumsItemData.spec_name;
                        printdata.barcode = printNumsItemData.barcode;
                        printdata.price = printNumsItemData.price;

                        printDatas.add(printdata);
                    }
                }
            }
        }

        return printDatas;
    }
}
