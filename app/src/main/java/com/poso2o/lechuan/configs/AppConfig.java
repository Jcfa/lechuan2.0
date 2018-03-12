package com.poso2o.lechuan.configs;

import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.util.FileUtils;

import java.io.File;

/**
 * Created by Administrator on 2017-11-28.
 */

public class AppConfig {
    /* 配置是否打印 true 打印  false 不许可打印 对应的工具类 tool.print.Print **/
    public final static boolean IS_PRINT = true;
    /**
     * 乐传微信
     */
//    public static final String WEIXIN_APPID = "wx730df332c5011c13";
//    public static final String WEIXIN_APPSECRET = "8fce2a6466bafb0d8c5a8d96f24393fe";
    /**
     * 爱乐传微信
     */
    public static final String WEIXIN_APPID = "wx5e1d8102a98c8d2f";
    public static final String WEIXIN_APPSECRET = "ee27e8be86d372d62f177b569c2753fd";
//    public static final String WEIXIN_MCHID = "";// 微信商户号
    public static final String WEIXIN_APPKEY = "";// 微信api商户key
//    public static final String WEIXIN_NOTIFY_URL = HttpAPI.SERVER_MAIN_API + "wxnotify.htm";// 微信支付回调链接
    /**
     * QQ
     */
    public static final String QQ_APPID = "1106571585";
    /**
     * 小米
     */
    public static final String XM_APPID = "2882303761517676667";
    public static final String XM_APPKEY = "5381767646667";

    /**
     * apk下载保存目录
     */
    public String APP_PATH_ROOT = "";
    public String APK_FILE_PATH = "";
    public static final String APK_NAME = "buyinchina_helper.apk";

    private static AppConfig appConfig;


    private AppConfig() {
        APP_PATH_ROOT = FileUtils.getRootPath(BaseApplication.getContext()).getAbsolutePath() + File.separator +
                "LeChuan";
        APK_FILE_PATH = APP_PATH_ROOT + File.separator + APK_NAME;
    }

    /**
     * apk下载保存目录
     */
    public static AppConfig getInstance() {
        if (appConfig == null) {
            synchronized (AppConfig.class) {
                if (appConfig == null) {
                    appConfig = new AppConfig();
                }
            }
        }
        return appConfig;
    }

}
