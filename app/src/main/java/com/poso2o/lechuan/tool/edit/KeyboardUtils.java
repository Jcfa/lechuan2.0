package com.poso2o.lechuan.tool.edit;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.poso2o.lechuan.base.BaseActivity;

/**
 * 软键盘工具类
 * Created by Jaydon on 2017/2/9.
 */
public class KeyboardUtils {

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(BaseActivity context) {
        View v = context.getWindow().getDecorView().findFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 弹出键盘
     */
    public static void showSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, 0);
        }
    }
}
