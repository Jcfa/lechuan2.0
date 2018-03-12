package com.poso2o.lechuan.manager.distributor;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.distributor.DistShop;
import com.poso2o.lechuan.bean.distributor.DistShopBean;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.util.ListUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * Created by Jaydon on 2017/12/8.
 */
public class DistributorManager extends BaseManager {

    private final int TAG_DIST_LIST_ID = 141;

    private static DistributorManager distributorManager;

    public static DistributorManager getInstance() {
        if (distributorManager == null) {
            synchronized (DistributorManager.class) {
                if (distributorManager == null) {
                    distributorManager = new DistributorManager();
                }
            }
        }
        return distributorManager;
    }

    public void loadShopList(final BaseActivity baseActivity, final OnLoadShopListCallback callback) {
        Request<String> request = getStringRequest(HttpAPI.DIST_SHOP_LIST_API);
        defaultParam(request);
        baseActivity.request(TAG_DIST_LIST_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                DistShopBean distShopBean = gson.fromJson(response, DistShopBean.class);
                if (distShopBean != null && ListUtils.isNotEmpty(distShopBean.list)) {
                    callback.onSucceed(distShopBean.list);
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_load_catalog_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    public interface OnLoadShopListCallback {

        void onSucceed(ArrayList<DistShop> list);

        void onFail(String failMsg);
    }
}
