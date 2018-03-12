package com.poso2o.lechuan.util;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;

import java.util.ArrayList;

/**
 * Created by Jaydon on 2017/4/8.
 */
public class SpecUtils {

    /**
     * 获取价格文本
     */
    public static String getPriceText(ArrayList<GoodsSpec> specs) {
        if (ListUtils.isNotEmpty(specs)) {
            if (specs.size() == 1) {
                return NumberFormatUtils.format(specs.get(0).spec_price);
            } else {
                double min, max;
                min = max = specs.get(0).spec_price;
                for (int i = 1; i < specs.size(); i++) {
                    GoodsSpec spec = specs.get(i);
                    if (min > spec.spec_price) {
                        min = spec.spec_price;
                    }
                    if (spec.spec_price > max) {
                        max = spec.spec_price;
                    }
                }
                if (min == max) {
                    return NumberFormatUtils.format(min);
                }
                return NumberFormatUtils.format(min) + " ~ " + NumberFormatUtils.format(max);
            }
        }
        return "0.00";
    }

    /**
     * 获取成本文本
     */
    public static String getCostText(ArrayList<GoodsSpec> specs) {
        if (ListUtils.isNotEmpty(specs)) {
            if (specs.size() == 1) {
                return NumberFormatUtils.format(specs.get(0).spec_cost);
            } else {
                double min, max;
                min = max = specs.get(0).spec_cost;
                for (int i = 1; i < specs.size(); i++) {
                    GoodsSpec spec = specs.get(i);
                    if (min > spec.spec_cost) {
                        min = spec.spec_cost;
                    }
                    if (spec.spec_cost > max) {
                        max = spec.spec_cost;
                    }
                }
                if (min == max) {
                    return NumberFormatUtils.format(min);
                }
                return NumberFormatUtils.format(min) + " ~ " + NumberFormatUtils.format(max);
            }
        }
        return "0.00";
    }

    /**
     * 获取数量文本
     */
    public static String getNumText(ArrayList<GoodsSpec> specs) {
        if (ListUtils.isNotEmpty(specs)) {
            if (specs.size() == 1) {
                return NumberFormatUtils.formatToInteger(specs.get(0).spec_number);
            } else {
                int num = 0;
                for (int i = 0; i < specs.size(); i++) {
                    GoodsSpec spec = specs.get(i);
                    num += spec.spec_number;
                }
                return Integer.toString(num);
            }
        }
        return "0";
    }

    /**
     * 获取数量文本
     */
    public static int getNum(ArrayList<GoodsSpec> specs) {
        if (ListUtils.isNotEmpty(specs)) {
            if (specs.size() == 1) {
                return specs.get(0).spec_number;
            } else {
                int num = 0;
                for (int i = 0; i < specs.size(); i++) {
                    GoodsSpec spec = specs.get(i);
                    num += spec.spec_number;
                }
                return num;
            }
        }
        return 0;
    }
}
