package com.poso2o.lechuan.printer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Luo on 2017/09/09 0009.
 */

public class PrinterSPU {

    public PrinterSPU() {
    }

    public static String getPrinterAddress(Context context, String key) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("SmartPrinter", 0);
            String value = preferences.getString(key, "");
            return value;
        }
        return null;
    }

    public static void setPrinterAddress(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("SmartPrinter", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

}