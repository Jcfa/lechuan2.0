/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.tool.listener;

import android.widget.EditText;


/**
 * 输入监听类，防止死循环
 * @author Zheng Jie Dong
 * @date 2015-4-4
 */
public class MoneyTextWatcher extends CustomTextWatcher {
    
    private EditText mEt;
    
    public MoneyTextWatcher(EditText et) {
        mEt = et;
    }
    
    public void input(String s, int start, int before, int count) {
        if (s.length() > 0) {
            s = formatEditTextMoney(s);
            mEt.setText(s);
            mEt.setSelection(s.length());
        } else {
            s = "0";
        }
        input(s);
    }
    
    public void input(String s) {
        // 如果还有后续操作，可重写此方法
    }
}
