package com.poso2o.lechuan.tool.edit;

/**
 * 字符串处理
 * Created by Jaydon on 2017/1/10.
 */
public class TextUtils {

    public static final String EMPTY = "";

    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String text) {
        return android.text.TextUtils.isEmpty(text);
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
        return !android.text.TextUtils.isEmpty(text);
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
}
