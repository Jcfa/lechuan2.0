package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.size.GoodsSize;
import com.poso2o.lechuan.bean.size.SizeBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RealSizeAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealSizeManager extends BaseManager {

    public static final int LIST = 2005;
    public static final int ADD = 2006;
    public static final int DEL = 2007;

    private static volatile RealSizeManager realSizeManager;

    public static RealSizeManager getInstance() {
        if (realSizeManager == null) {
            synchronized (RealSizeManager.class) {
                if (realSizeManager == null) realSizeManager = new RealSizeManager();
            }
        }
        return realSizeManager;
    }

    /**
     * 实体店商品尺码列表
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<SizeBean> iRequestCallBack) {
        Request<String> request = getStringRequest(RealSizeAPI.LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                SizeBean sizeBean = new Gson().fromJson(response, SizeBean.class);
                iRequestCallBack.onResult(LIST, sizeBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(LIST, response);
            }
        }, true, false);
    }

    /**
     * 添加
     *
     * @param baseActivity
     * @param goodsSize
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, final GoodsSize goodsSize, final IRequestCallBack<GoodsSize> iRequestCallBack) {
        Request<String> request = getStringRequest(RealSizeAPI.ADD);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sizeid", goodsSize.sizeid);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(ADD, goodsSize);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(ADD, response);
            }
        }, true, false);
    }

    /**
     * 删除
     *
     * @param baseActivity
     * @param sizeid
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String sizeid, final IRequestCallBack iRequestCallBack) {

        Request<String> request = getStringRequest(RealSizeAPI.DEL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sizeid", sizeid);

        baseActivity.request(DEL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(DEL, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onResult(DEL, response);
            }
        }, true, false);
    }
}
