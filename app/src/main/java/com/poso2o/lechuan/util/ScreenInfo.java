/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 屏幕信息类
 *
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
public class ScreenInfo {

    /** 显示测量对象 */
    public static DisplayMetrics DM;

    /** 屏幕宽度 */
    public static int WIDTH;

    /** 屏幕高度 */
    public static int HEIGHT;

    /** 屏幕密度 */
    public static int DPI;
    
    /** 屏幕比例 */
    public static float DENSITY;

    /** 状栏栏高度 */
    public static int STATUS_BAR_HEIGHT;

    public static void init(Context context) {
        DM = context.getApplicationContext().getResources().getDisplayMetrics();
        WIDTH = DM.widthPixels;
        HEIGHT = DM.heightPixels;
        DPI = DM.densityDpi;
        DENSITY = DM.scaledDensity;
        STATUS_BAR_HEIGHT = getStatusBarHeight(context);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 将dp转换成像素值
     */
    public static int dpCpx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, DM);
    }

    /**
     * 将像素转换成dp值
     */
    public static int pxCdp(int px) {
        return (int) (px / DM.density);
    }
}
