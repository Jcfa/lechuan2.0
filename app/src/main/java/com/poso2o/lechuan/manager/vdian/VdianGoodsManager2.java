package com.poso2o.lechuan.manager.vdian;

import android.content.Context;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.GoodsBean;
import com.poso2o.lechuan.bean.mine.GoodsData;
import com.poso2o.lechuan.http.CallServer;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * 微店商品改界面后新的接口
 * Created by Administrator on 2018-03-28.
 */

public class VdianGoodsManager2 extends BaseManager {
    private static final int VDIAN_ID = 1;
    //目录
    private String CATALOG_LIST_API = HttpAPI.SERVER_MAIN_API + "GoodsCatalogManage.htm?Act=list";
    //商品
    private String GOODS_LIST_API = HttpAPI.SERVER_MAIN_API + "GoodsManage.htm?Act=query";

    private static volatile VdianGoodsManager2 vdianGoodsManager;

    public static VdianGoodsManager2 getInstance() {
        if (vdianGoodsManager == null) {
            synchronized (VdianGoodsManager.class) {
                if (vdianGoodsManager == null) vdianGoodsManager = new VdianGoodsManager2();
            }
        }
        return vdianGoodsManager;
    }

    /**
     * 加载商品目录列表
     */
    public void loadCatalogList(BaseActivity baseActivity, final IRequestCallBack<CatalogBean> callBack) {
        Request<String> request = new StringRequest(CATALOG_LIST_API, RequestMethod.POST);
        defaultParam(request);
        CallServer.getInstance().request(baseActivity, VDIAN_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                CatalogBean catalogBean = new Gson().fromJson(response, CatalogBean.class);
                callBack.onResult(what, catalogBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, false, false);
    }

    /**
     * 加载商品列表
     */
    public void loadGoodsList(BaseActivity baseActivity, String catalogId, String keywords, String currPage, String online, final IRequestCallBack<GoodsBean> callBack) {
        Request<String> request = new StringRequest(GOODS_LIST_API, RequestMethod.POST);
        defaultParam(request);
        if (!catalogId.equals("")) {
            request.add("catalog_id", catalogId);
        }
        if (!keywords.equals("")) {
            request.add("keywords", keywords);
        }
        request.add("currPage", currPage);
        if (!online.equals("")) {
            request.add("online", online);
        }
        CallServer.getInstance().request(baseActivity, VDIAN_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                GoodsBean goodsBean = new Gson().fromJson(response, GoodsBean.class);
                callBack.onResult(what, goodsBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, false, false);
    }
}
