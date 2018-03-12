package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.color.ColorBean;
import com.poso2o.lechuan.bean.color.GoodsColor;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RealColorAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/11.
 */
public class RealColorManager extends BaseManager {

    // 颜色列表
    public static final int LIST = 2008;
    // 添加颜色
    public static final int ADD = 2009;
    // 删除颜色
    public static final int DEL = 2010;

    private static volatile RealColorManager realColorManager;

    public static RealColorManager getInstance() {
        if (realColorManager == null) {
            synchronized (RealColorManager.class) {
                if (realColorManager == null) realColorManager = new RealColorManager();
            }
        }
        return realColorManager;
    }

    /**
     * 获取颜色列表
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<ColorBean> iRequestCallBack) {
        Request<String> request = getStringRequest(RealColorAPI.LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                ColorBean colorBean = new Gson().fromJson(response, ColorBean.class);
                iRequestCallBack.onResult(LIST, colorBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(LIST, response);
            }
        }, true, false);
    }

    /**
     * 添加颜色
     *
     * @param baseActivity
     * @param color
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, GoodsColor color, final IRequestCallBack iRequestCallBack) {
        Request<String> request = getStringRequest(RealColorAPI.ADD);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("colorid", color.colorid);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(ADD, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(ADD, response);
            }
        }, true, false);
    }

    /**
     * 删除颜色
     *
     * @param baseActivity
     * @param colorid
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String colorid, final IRequestCallBack iRequestCallBack) {
        Request<String> request = getStringRequest(RealColorAPI.DEL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("colorid", colorid);

        baseActivity.request(DEL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                ColorBean colorBean = new Gson().fromJson(response, ColorBean.class);
                iRequestCallBack.onResult(DEL, colorBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(DEL, response);
            }
        }, true, false);
    }
}
