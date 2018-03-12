package com.poso2o.lechuan.tool.print;

import android.util.Log;

import com.poso2o.lechuan.configs.AppConfig;

/**
 * Created by Luo on 2017/11/29.
 * 打印共有类
 * 通过Configuration配置是否进行打印
 * */

public class Print {
    public static  void println(String printString){
        if (AppConfig.IS_PRINT){
            System.out.println(printString);
            //Log.i("lechuan", printString);
        }
    }
    public static  void println(String type, String vale){
        if (AppConfig.IS_PRINT){
            Log.i(type, vale);
        }
    }
}
