/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.tool.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 输入监听类，防止死循环
 * @author Zheng Jie Dong
 * @date 2015-4-4
 */
public abstract class CustomTextWatcher implements TextWatcher {

    /**
     * 是否正在输入
     * @author Zheng Jie Dong
     * @date 2015-4-4
     */
    private static boolean isInput;

    @Override
    public void afterTextChanged(Editable s) {
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isInput) {
            isInput = true;
            if (s == null) {
            	s = "";
            }
            input(s.toString(), start, before, count);
            isInput = false;
        }
    }

    /**
     * 格式化编辑框输入的数据
     */
    public String formatEditTextMoney(String s) {
        // 输入两个零
        if ("00".equals(s)) {
            return "0";
        }
        // 判断是否是零开头并且不包含小数点的数字
        if (isStartZero(s)) {
            return s.replace("0", "");
        }
        // 只输入小数点
        if (".".equals(s)) {
            return "0.";
        }
        // 格式化小数点后位数
        if (s.contains(".")) {
            String end = s.substring(s.indexOf(".") + 1);
            if (end.length() > 2) {
                return s.substring(0, s.length() - (end.length() - 2));
            }
        }
        return s;
    }

    /**
     * 开头数字是不是0
     */
    private boolean isStartZero(String s) {
        if (s.length() == 2 && !s.contains(".") && s.charAt(0) == '0') {
            return true;
        } else if (s.length() == 3 && !s.contains(".") && s.contains("-0")) {
            return true;
        }
        return false;
    }

    /**
     * 格式化小数点后位数
     */
    public String fomatPrint(String s) {
        // 格式化小数点后位数
        if (s.contains(".")) {
            String end = s.substring(s.indexOf(".") + 1);
            if (end.length() > 2) {
                return s.substring(0, s.length() - (end.length() - 2));
            }
        }
        return s;
    }

    public abstract void input(String s, int start, int before, int count);
}
