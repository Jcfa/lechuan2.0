package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;

import java.io.File;

/**
 * Created by mr zhang on 2017/12/7.
 */

public class RealShopManager extends BaseManager {

    //店铺信息地址
    public static final String R_SHOP_INFO_URL = "http://fuzhuang.poso2o.com/user.htm?Act=shopinfo";
    public static final int R_SHOP_INFO_ID = 10024;

    //编辑店铺信息
    public static final String R_SHOP_INFO_EDIT_URL = "http://fuzhuang.poso2o.com/user.htm?Act=edit";
    public static final int R_SHOP_INFO_EDIT_ID = 10025;

    //上传logo
    public static final String R_SHOP_LOGO_URL = "http://fuzhuang.poso2o.com/user.htm?Act=uploadLogo";
//    public static final String R_SHOP_LOGO_URL = "http://192.168.10.153:8080/user.htm?Act=uploadLogo";
    public static final int R_SHOP_LOGO_ID = 10026;

    private static volatile RealShopManager realShopManager;
    public static RealShopManager getRealShopManager(){
        if (realShopManager == null){
            synchronized (RealGoodsManager.class){
                if (realShopManager == null)realShopManager = new RealShopManager();
            }
        }
        return realShopManager;
    }

    /**
     * 店铺信息
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void rShopInfo(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(R_SHOP_INFO_URL);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(R_SHOP_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                ShopData shopData = new Gson().fromJson(response,ShopData.class);
                iRequestCallBack.onResult(R_SHOP_INFO_ID,shopData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_SHOP_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 编辑店铺信息
     * @param baseActivity
     * @param shopData
     * @param iRequestCallBack
     */
    public void rEditShopInfo(final BaseActivity baseActivity,ShopData shopData,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(R_SHOP_INFO_EDIT_URL);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy",SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("mobile",shopData.mobile);
        request.add("shopname",shopData.shopname);
        request.add("attn",shopData.attn);
        request.add("description",shopData.description);
        request.add("province",shopData.province);
        request.add("city",shopData.city);
        request.add("area",shopData.area);
        request.add("address",shopData.address);
        request.add("tel",shopData.tel);
        request.add("shoptype",shopData.shoptype);
        request.add("remark",shopData.remark);

        baseActivity.request(R_SHOP_INFO_EDIT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_SHOP_INFO_EDIT_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_SHOP_INFO_EDIT_ID,response);
            }
        },true,true);
    }

    /**
     * 修改店铺头像接口
     * @param baseActivity
     * @param path
     * @param iRequestCallBack
     */
    public void upRShoLogo(final BaseActivity baseActivity,String path,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(R_SHOP_LOGO_URL + "&sessionUid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&sessionKey=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN) + "&shopid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&czy=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        File file = new File(path);
        FileBinary binary = new FileBinary(file);
        request.add("file",binary);
        Print.println("上传头像地址：" + request.url());
        Print.println("文件地址：" + path + "  " + binary);

        baseActivity.request(R_SHOP_LOGO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_SHOP_LOGO_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_SHOP_LOGO_ID,response);
            }
        },true,true);
    }
}
