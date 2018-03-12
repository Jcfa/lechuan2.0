package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianCatalogAPI;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * 微店目录管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianCatalogManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;
    private static final int SORT = 2005;
    private static final int MOVE = 2006;

    private static VdianCatalogManager vdianCatalogManager;

    public static VdianCatalogManager getInstance() {
        if (vdianCatalogManager == null) {
            synchronized (VdianCatalogManager.class) {
                if (vdianCatalogManager == null) {
                    vdianCatalogManager = new VdianCatalogManager();
                }
            }
        }
        return vdianCatalogManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<CatalogBean> callback) {
        loadList(baseActivity, 0, callback);
    }

    public void loadList(final BaseActivity baseActivity, long shop_id, final IRequestCallBack<CatalogBean> callback) {
        Request<String> request = getStringRequest(VdianCatalogAPI.LIST);
        defaultParam(request);
        if (shop_id != 0) {
            request.add("shop_id", shop_id);
        }
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                CatalogBean catalogBean = gson.fromJson(response, CatalogBean.class);
                if (catalogBean != null) {
                    callback.onResult(what, catalogBean);
                } else {
                    callback.onFailed(what, baseActivity.getString(R.string.toast_load_catalog_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 添加实体店目录
     *
     * @param baseActivity
     * @param catalog
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, Catalog catalog, final IRequestCallBack<Catalog> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianCatalogAPI.ADD);
        request.add("catalog_id", catalog.catalog_id);
        request.add("catalog_name", catalog.catalog_name);
        request.add("catalog_discount", catalog.catalog_discount);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(ADD, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(ADD, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品目录
     *
     * @param baseActivity
     * @param catalog_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String catalog_id, final IRequestCallBack<Catalog> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianCatalogAPI.DEL);
        request.add("catalog_id", catalog_id);
        defaultParam(request);

        baseActivity.request(DEL, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(DEL, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(DEL, response);
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
        Request<String> request = getStringRequest(VdianCatalogAPI.EDIT);
        request.add("catalog_id",catalog.catalog_id);
        request.add("catalog_name",catalog.catalog_name);
        request.add("catalog_discount",catalog.catalog_discount);
        request.add("catalog_goods_number",catalog.catalog_goods_number);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Catalog catalog = new Gson().fromJson(response, Catalog.class);
                iRequestCallBack.onResult(EDIT, catalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(EDIT, response);
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
        Request<String> request = getStringRequest(VdianCatalogAPI.SORT);
        request.add("datas", new Gson().toJson(items));
        defaultParam(request);

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
    public void move(BaseActivity baseActivity, Catalog catalog, ArrayList<Goods> goodsDatas, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianCatalogAPI.MOVE);

        StringBuilder datas = new StringBuilder("[");
        for (Goods goods : goodsDatas) {
            datas.append("{\"goods_id\":\"").append(goods.goods_id).append("\"},");
        }
        datas.deleteCharAt(datas.length() - 1);
        datas.append("]");

        request.add("catalog_id", catalog.catalog_id);
        request.add("catalog_name", catalog.catalog_name);
        request.add("datas", datas.toString());
        defaultParam(request);

        baseActivity.request(MOVE, request, new HttpListener<String>() {

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
