package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RealCatalogAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/9.
 */

public class RealCatalogManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;
    private static final int SORT = 2005;

    private static volatile RealCatalogManager realCatalogManager;

    public static RealCatalogManager getInstance() {
        if (realCatalogManager == null) {
            synchronized (RealCatalogManager.class) {
                if (realCatalogManager == null) realCatalogManager = new RealCatalogManager();
            }
        }
        return realCatalogManager;
    }

    /**
     * 目录列表
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<CatalogBean> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                // 解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")) {
                    response = "{\nlist\n:" + response + "}";
                }
                CatalogBean catalogBean = new Gson().fromJson(response, CatalogBean.class);
                iRequestCallBack.onResult(what, catalogBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 添加实体店目录
     *
     * @param baseActivity
     * @param catalog
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, Catalog catalog, final IRequestCallBack<Catalog> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.ADD);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("directory", catalog.directory);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(what, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品目录
     *
     * @param baseActivity
     * @param fid
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String fid, final IRequestCallBack<Catalog> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.DEL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("fid", fid);

        baseActivity.request(DEL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(what, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 编辑实体店商品目录
     *
     * @param baseActivity
     * @param catalog
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, Catalog catalog, final IRequestCallBack<Catalog> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.EDIT);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("fid", catalog.fid);
        request.add("directory", catalog.directory);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(what, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 排序
     *
     * @param baseActivity
     * @param items
     * @param iRequestCallBack
     */
    public void sort(BaseActivity baseActivity, ArrayList<Catalog> items, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.SORT);

        StringBuilder fids = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (i != 0) {
                fids.append(",");
            }
            fids.append(items.get(i).fid);
        }

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("fids", fids.toString());

        baseActivity.request(SORT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 移动商品
     */
    public void move(BaseActivity baseActivity, String fid, ArrayList<Goods> goodsDatas, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(RealCatalogAPI.MOVE);

        StringBuilder guids = new StringBuilder();

        for (int i = 0; i < goodsDatas.size(); i++) {
            if (i != 0) {
                guids.append(",");
            }
            guids.append(goodsDatas.get(i).guid);
        }

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("fid", fid);
        request.add("guids", guids.toString());

        baseActivity.request(0, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }
}