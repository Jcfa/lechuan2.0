package com.poso2o.lechuan.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by J on 2017/5/12.
 */

public class RandomStringUtil {

    /**
     * 按日期生成订单号
     *
     * @return String ID
     */
    public static String getOrderId() {
//        return "";
        return getOrderId(8, "0123456789");
    }

    /**
     * 按日期生成订单号
     *
     * @param length 字符串额外长度(如果传入0则未八位,年月日时)
     * @param str    随机数的来源字符串
     * @return String ID
     */
    public static String getOrderId(int length, String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
        String nowDate = sdf.format(new Date(System.currentTimeMillis()));
        return nowDate + randomGUID(length, str);
    }

    /**
     * 生产一个指定长度的随机字符串
     *
     * @param length 字符串长度
     * @param str    随机数的来源字符串
     * @return
     */
    public static String randomGUID(int length, String str) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }
}
