/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.tool.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入监听类，防止死循环
 * @author Zheng Jie Dong
 * @date 2015-4-4
 */
public class PointControlTextWatcher implements TextWatcher {

    /**
     * 是否正在输入
     * @author Zheng Jie Dong
     * @date 2015-4-4
     */
    private static boolean isInput;

    private EditText et;

    public PointControlTextWatcher(EditText et) {
        this.et = et;
    }

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

    public void input(String s, int start, int before, int count) {
        if (s.length() == 0) {
            return;
        }
        // 输入两个零
        if ("00".equals(s)) {
            et.setText("0");
            et.setSelection(et.length());
            return;
        }
        // 判断是否是零开头并且不包含小数点的数字
        if (isStartZero(s)) {
            et.setText(s.replace("0", ""));
            et.setSelection(et.length());
            return;
        }
        // 只输入小数点
        if (s.equals(".")) {
            et.setText("0.");
            et.setSelection(et.length());
            return;
        }
        // 格式化小数点后位数
        if (s.contains(".")) {
            String end = s.substring(s.indexOf(".") + 1);
            if (end.length() > 2) {
                et.setText(s.substring(0, s.length() - (end.length() - 2)));
                et.setSelection(et.length());
//                return;
            }
        }
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
}
