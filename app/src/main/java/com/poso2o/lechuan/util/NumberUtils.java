/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils - 数字
 *
 * @author 郑洁东
 * @version 1.0
 */
public class NumberUtils {

    /**
     * 默认值
     */
    private static final BigDecimal defaultBigDecimalValue = BigDecimal.ZERO;

    /**
     * 默认值
     */
    private static final Long DEFAULT_LONG_VALUE = 0L;

    /**
     * 舍入模式：四舍五入
     */
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 舍入精度
     */
    private static final int ROUNDING_PRECISION = 6;

    /**
     * 不允许实例化
     */
    private NumberUtils() {
    }

    // 运算 begin
    // --------------------------------------------------

    // Integer

    /**
     * number0 + number1
     */
    public static Integer add(Integer number0, Integer number1) {
        return number0 == null || number1 == null ? 0 : number0 + number1;
    }

    /**
     * number0 - number1
     */
    public static Integer subtract(Integer number0, Integer number1) {
        return number0 == null || number1 == null ? 0 : number0 - number1;
    }

    /**
     * number0 * number1
     */
    public static Integer multiply(Integer number0, Integer number1) {
        return number0 == null || number1 == null ? 0 : number0 * number1;
    }

    /**
     * number0 * number1
     */
    public static BigDecimal multiply(String number0, String number1) {
        try {
            return number0 == null || number1 == null ? BigDecimal.ZERO : multiply(new BigDecimal(number0), new BigDecimal(number1));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * number0 / number1
     */
    public static Integer divide(Integer number0, Integer number1) {
        return number0 == null || number1 == null || number1 == 0 ? 0 : number0 / number1;
    }

    // Long

    /**
     * number0 + number1
     */
    public static Long add(Long number0, Long number1) {
        return number0 == null || number1 == null ? 0L : number0 + number1;
    }

    /**
     * number0 - number1
     */
    public static Long subtract(Long number0, Long number1) {
        return number0 == null || number1 == null ? 0L : number0 - number1;
    }

    /**
     * number0 * number1
     */
    public static Long multiply(Long number0, Long number1) {
        return number0 == null || number1 == null ? 0L : number0 * number1;
    }

    /**
     * number0 / number1
     */
    public static Long divide(Long number0, Long number1) {
        return number0 == null || number1 == null || number1 == 0L ? 0L : number0 / number1;
    }

    // Float

    /**
     * number0 + number1
     */
    public static Float add(Float number0, Float number1) {
        return number0 == null || number1 == null ? 0F : number0 + number1;
    }

    /**
     * number0 - number1
     */
    public static Float subtract(Float number0, Float number1) {
        return number0 == null || number1 == null ? 0F : number0 - number1;
    }

    /**
     * number0 * number1
     */
    public static Float multiply(Float number0, Float number1) {
        return number0 == null || number1 == null ? 0F : number0 * number1;
    }

    /**
     * number0 / number1
     */
    public static Float divide(Float number0, Float number1) {
        return number0 == null || number1 == null || number1 == 0F ? 0F : number0 / number1;
    }

    // Double

    /**
     * number0 + number1
     */
    public static Double add(Double number0, Double number1) {
        return number0 == null || number1 == null ? 0D : number0 + number1;
    }

    /**
     * number0 - number1
     */
    public static Double subtract(Double number0, Double number1) {
        return number0 == null || number1 == null ? 0D : number0 - number1;
    }

    /**
     * number0 * number1
     */
    public static Double multiply(Double number0, Double number1) {
        return number0 == null || number1 == null ? 0D : number0 * number1;
    }

    /**
     * number0 / number1
     */
    public static BigDecimal divide(String number0, String number1) {
        return divide(toBigDecimal(number0), toBigDecimal(number1));
    }

    /**
     * number0 / number1
     */
    public static Double divide(Double number0, Double number1) {
        return number0 == null || number1 == null || number1 == 0D ? 0D : number0 / number1;
    }

    // BigDecimal

    /**
     * number0 + number1
     */
    public static BigDecimal add(BigDecimal number0, BigDecimal number1) {
        return number0 == null || number1 == null ? BigDecimal.ZERO : number0.add(number1);
    }

    /**
     * number0 - number1
     */
    public static BigDecimal subtract(BigDecimal number0, BigDecimal number1) {
        return number0 == null || number1 == null ? BigDecimal.ZERO : number0.subtract(number1);
    }

    /**
     * number0 * number1
     */
    public static BigDecimal multiply(BigDecimal number0, BigDecimal number1) {
        return number0 == null || number1 == null ? BigDecimal.ZERO : number0.multiply(number1);
    }

    /**
     * number0 / number1
     */
    public static BigDecimal divide(BigDecimal number0, BigDecimal number1) {
        try {
            return number0 == null || number1 == null || BigDecimal.ZERO.equals(number1) ? BigDecimal.ZERO : number0.divide(number1, ROUNDING_PRECISION, ROUNDING_MODE);
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 判断number0是否等于number1
     */
    public static boolean equals(String number0, String number1) {
        return equals(toBigDecimal(number0), toBigDecimal(number1));
    }

    /**
     * 判断number0是否等于number1
     */
    public static boolean equals(BigDecimal number0, BigDecimal number1) {
        return number0 != null && number1 != null && number0.doubleValue() == number1.doubleValue();
    }

    /**
     * 判断number0是否大于number1
     */
    public static boolean greaterThan(BigDecimal number0, BigDecimal number1) {
        return number0 != null && number1 != null && number0.doubleValue() > number1.doubleValue();
    }

    /**
     * 判断number0是否大于number1
     */
    public static boolean greaterThan(String number0, String number1) {
        try {
            double num0 = Double.parseDouble(number0);
            double num1 = Double.parseDouble(number1);
            return num0 > num1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是零
     */
    public static boolean isZero(BigDecimal num) {
        return num == null || num.doubleValue() == 0;
    }

    /**
     * 是零
     */
    public static boolean isZero(String num) {
        return TextUtils.isEmpty(num) || Double.parseDouble(num) == 0;
    }

    /**
     * 不是零
     */
    public static boolean isNotZero(BigDecimal num) {
        return num != null && num.doubleValue() != 0;
    }

    /**
     * 不是零
     */
    public static boolean isNotZero(String num) {
        return !TextUtils.isEmpty(num) && Double.parseDouble(num) != 0;
    }

    /**
     * 返回double数值
     */
    public static double toDouble(String num) {
        return TextUtils.isEmpty(num) ? 0 : Double.parseDouble(num);
    }

    /**
     * 返回float数值
     */
    public static float toFloat(String num) {
        return TextUtils.isEmpty(num) ? 0 : Float.parseFloat(num);
    }

    /**
     * 返回int数值
     */
    public static int toInt(String num) {
        try {
            if (!TextUtils.isEmpty(num)) {
                return Integer.parseInt(num);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    /**
     * 返回long数值
     */
    public static long toLong(String num) {
        return TextUtils.isEmpty(num) ? 0 : Long.parseLong(num);
    }

    /**
     * 返回BigDecimal数据
     */
    public static BigDecimal toBigDecimal(String num) {
        return TextUtils.isEmpty(num) ? BigDecimal.ZERO : new BigDecimal(num);
    }

    /**
     * 是不是数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 保留2位小数
     *
     * @param value
     * @return
     */
    public static String format2(Object value) {
        if (value instanceof Double || value instanceof Float || value instanceof String) {
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(value);
        }
        return value.toString();
    }

//    /**
//     * double类型保留2位小数
//     *
//     * @param d
//     * @param len
//     * @return
//     */
//    public static double bigDecimal(double d, int len) {
//        BigDecimal b = new BigDecimal(d);
//        //ROUND_HALF_UP=入，ROUND_HALF_DOWN=舍
//        double d1 = b.setScale(len, BigDecimal.ROUND_UP).doubleValue();
//        return d1;
//    }
//
//    /**
//     * float类型保留2位小数
//     *
//     * @param d
//     * @param len
//     * @return
//     */
//    public static double bigDecimal(float d, int len) {
//        BigDecimal b = new BigDecimal(d);
//        //ROUND_HALF_UP=入，ROUND_HALF_DOWN=舍
//        float d1 = b.setScale(len, BigDecimal.ROUND_UP).floatValue();
//        return d1;
//    }
}