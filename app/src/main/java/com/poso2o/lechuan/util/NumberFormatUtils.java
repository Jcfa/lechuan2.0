/*
 * Copyright 漏 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utils - 数字格式化
 *
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
public class NumberFormatUtils {

    /** 默认进位：不进位 */
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.DOWN;

    /** 整数格式 */
    public static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#0");

    /** 整数格式默认值 */
    public static final String DEFAULT_INTEGER = INTEGER_FORMAT.format(0);

    /** 小数格式 */
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    /** 默认格式 */
    public static final DecimalFormat DEFAULT_FORMAT = DECIMAL_FORMAT;

    /** 默认值 */
    private static final String DEFAULT_STRING_VALUE = DEFAULT_FORMAT.format(0);

    static {
        INTEGER_FORMAT.setRoundingMode(DEFAULT_ROUNDING_MODE);
        DECIMAL_FORMAT.setRoundingMode(DEFAULT_ROUNDING_MODE);
    }

    /**
     * 不允许实例化
     */
    private NumberFormatUtils() {
    }

    /**
     * 将BigDecimal转为String
     *
     * @param value 值
     */
    public static String format(final Object value) {
        return format(value, DEFAULT_STRING_VALUE, DEFAULT_FORMAT);
    }

    /**
     * 将String格式数字转为正确的.00格式
     */
    public static String format(final String value) {
        double num = 0;
        try {
            if (TextUtil.isNotEmpty(value)) {
                num = Double.parseDouble(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format(num);
    }

    /**
     * 将BigDecimal转为String整数
     *
     * @param value 值
     */
    public static String formatToInteger(final Object value) {
        return format(value, DEFAULT_INTEGER, INTEGER_FORMAT);
    }

    /**
     * 将String格式数字转为整形格式
     */
    public static String formatToInteger(final String value) {
        double num= 0;
        try {
            if (TextUtil.isNotEmpty(value)) {
                num = Double.parseDouble(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatToInteger(num);
    }

    /**
     * 将BigDecimal转为String
     *
     * @param value 值
     * @param defaultValue 默认值，值为null时用该值代替
     * @param format 格式
     */
    public static String format(final Object value, final String defaultValue, final DecimalFormat format) {
        Object formatValue = value;
        if (formatValue == null) {
            return defaultValue;
        }
        return format.format(formatValue);
    }


}
