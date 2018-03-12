package com.poso2o.lechuan.util;

import android.content.Context;

import com.poso2o.lechuan.bean.printer.PrintNumsItemData;
import com.poso2o.lechuan.view.PrintBarcodeNumItemView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/24.
 */

public class PrintGoodsBarcodeViewP {
    private Context context;

    public PrintGoodsBarcodeViewP(Context context) {
        this.context = context;

    }

    /**
     * 获得所有item 选中打印的数量
     * @param printBarcodeNumsItemViews
     * @return
     */
    public int getNumber(ArrayList<PrintBarcodeNumItemView> printBarcodeNumsItemViews) {
        int i = 0;
        for (PrintBarcodeNumItemView p : printBarcodeNumsItemViews) {
            PrintNumsItemData printInfo = p.getPrintNumsItemData();
            if (printInfo.isSelect) {
                i += p.getNumber();
            }

        }
        return i;
    }

    /**
     * item项目所有打印项选中状态
      * @param printBarcodeNumsItemViews
     * @param b
     */
    public void allItemSelect(ArrayList<PrintBarcodeNumItemView> printBarcodeNumsItemViews, boolean b) {
        for (PrintBarcodeNumItemView p : printBarcodeNumsItemViews) {
            p.setSelect(b);
        }
    }

    /**
     * item项所有商品数量改变
     * @param printBarcodeNumsItemViews
     * @param i
     */
    public void notifyItemNum(ArrayList<PrintBarcodeNumItemView> printBarcodeNumsItemViews, int i) {
        for (PrintBarcodeNumItemView p : printBarcodeNumsItemViews) {
            p.setNumber(i);
        }
    }
}
