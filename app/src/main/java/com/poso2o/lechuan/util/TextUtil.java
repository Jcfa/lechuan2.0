package com.poso2o.lechuan.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-11-28.
 */

public class TextUtil {

    public static final String REGEX_MOBILE = "^1[3|4|5|7|8|9][0-9]{9}$";

    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * 字符串是否为不为空
     */
    public static boolean isNotEmpty(CharSequence text) {
        return text != null && isNotEmpty(text.toString());
    }

    /**
     * 字符串是否为不为空
     */
    public static boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

    /**
     * 对比字符串
     */
    public static boolean equals(String text1, String text2) {
        return android.text.TextUtils.equals(text1, text2);
    }

    /**
     * 进行非null处理
     */
    public static String trimToEmpty(String text) {
        return trimToEmpty(text, "");
    }

    /**
     * 进行非null处理
     *
     * @param defaultValue 默认值
     */
    public static String trimToEmpty(String text, String defaultValue) {
        return isEmpty(text) ? defaultValue : text.trim();
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
}
