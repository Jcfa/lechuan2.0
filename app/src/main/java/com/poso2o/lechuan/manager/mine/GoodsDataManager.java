package com.poso2o.lechuan.manager.mine;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.mine.GoodsData;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianCatalogAPI;
import com.poso2o.lechuan.http.vdian.VdianGoodsAPI;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by Administrator on 2017-12-06.
 */

public class GoodsDataManager extends BaseManager {
    /**
     * 排序类型
     */
    public static final String SORT_TYPE_SALE_NUMBER = "goods_sale_number";// 销量
    public static final String SORT_TYPE_COMMISSION = "commission_amount";// 佣金
    public static GoodsDataManager mManager;
    /**
     * 商品
     */
    public static final int TAG_GOODS = 130;
    /**
     * 商品列表
     */
    public static final int TAG_CATALOG = 131;
    /**
     * 单品佣金
     */
    public static final int TAG_GOODS_COMMISSION = 132;
    /**
     * 通用佣金
     */
    public static final int TAG_COMMISSION = 133;

    public static GoodsDataManager getGoodsDataManager() {
        if (mManager == null) {
            mManager = new GoodsDataManager();
        }
        return mManager;
    }

    /**
     * 获取商品列表
     *
     * @param baseActivity
     * @param catalog_id
     * @param orderByName
     * @param sort
     * @param keywords
     * @param page
     * @param callBack
     */
    public void getGoodsList(BaseActivity baseActivity, String catalog_id, String orderByName, String sort, String keywords, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(VdianGoodsAPI.QUERY);
        request = defaultParam(request);
        request.add("catalog_id", catalog_id);
        request.add("orderByName", orderByName);
        request.add("sort", sort);
        request.add("keywords", keywords);
        request.add("currPage", page);
        baseActivity.request(TAG_GOODS, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                GoodsData goodsData = gson.fromJson(response, GoodsData.class);
                callBack.onResult(TAG_GOODS, goodsData);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 商品目录
     *
     * @param baseActivity
     * @param callBack
     */
    public void getCatalogList(BaseActivity baseActivity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(VdianCatalogAPI.LIST);
        request = defaultParam(request);
        baseActivity.request(TAG_CATALOG, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                CatalogBean catalogBean = gson.fromJson(response, CatalogBean.class);
                callBack.onResult(TAG_CATALOG, catalogBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 设置单品佣金比例
     */
    public void setGoodsCommissionRate(BaseActivity baseActivity, String rate, String discount, String goodsId, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_GOODS_COMMISSION_SET_API);
        request = defaultParam(request);
        request.add("commission_rate", rate);
        request.add("commission_discount", discount);
        request.add("goods_id", goodsId);
        baseActivity.request(TAG_GOODS_COMMISSION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Gson gson = new Gson();
//                CatalogBean catalogBean = gson.fromJson(response, CatalogBean.class);
//                callBack.onResult(TAG_GOODS_COMMISSION, catalogBean);
                callBack.onResult(TAG_GOODS_COMMISSION, response);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 设置通用佣金比例
     */
    public void setCommissionRate(BaseActivity baseActivity, String rate, String discount, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_COMMISSION_SET_API);
        request = defaultParam(request);
        request.add("commission_rate", rate);
        request.add("commission_discount", discount);
        baseActivity.request(TAG_COMMISSION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Gson gson = new Gson();
//                CatalogBean catalogBean = gson.fromJson(response, CatalogBean.class);
                callBack.onResult(TAG_COMMISSION, response);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

}
