package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.size.GoodsSize;
import com.poso2o.lechuan.bean.size.SizeBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianSizeAPI;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 微店单位管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianSizeManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;

    private static VdianSizeManager vdianSizeManager;

    public static VdianSizeManager getInstance() {
        if (vdianSizeManager == null) {
            synchronized (VdianSizeManager.class) {
                if (vdianSizeManager == null) {
                    vdianSizeManager = new VdianSizeManager();
                }
            }
        }
        return vdianSizeManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<SizeBean> callback) {
        Request<String> request = getStringRequest(VdianSizeAPI.LIST);
        defaultParam(request);
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                SizeBean sizeBean = gson.fromJson(response, SizeBean.class);
                if (sizeBean != null) {
                    callback.onResult(what, sizeBean);
                } else {
                    callback.onFailed(what, baseActivity.getString(R.string.toast_load_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 添加实体店尺码
     *
     * @param baseActivity
     * @param size
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, GoodsSize size, final IRequestCallBack<GoodsSize> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSizeAPI.ADD);
        request.add("goods_size_id", RandomStringUtil.getOrderId());
        request.add("goods_size_name", size.goods_size_name);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsSize size = new Gson().fromJson(response, GoodsSize.class);
                iRequestCallBack.onResult(what, size);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品尺码
     *
     * @param baseActivity
     * @param size_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String size_id, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSizeAPI.DEL);
        request.add("goods_size_id", size_id);
        defaultParam(request);

        baseActivity.request(DEL, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(DEL, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(DEL, response);
            }
        }, true, false);
    }

    /**
     * 编辑实体店商品尺码
     *
     * @param baseActivity
     * @param size
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, GoodsSize size, final IRequestCallBack<GoodsSize> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSizeAPI.EDIT);
        request.add("goods_size_id", size.goods_size_id);
        request.add("goods_size_name", size.goods_size_name);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsSize size = new Gson().fromJson(response, GoodsSize.class);
                iRequestCallBack.onResult(EDIT, size);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(EDIT, response);
            }
        }, true, false);
    }
}
