package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.color.ColorBean;
import com.poso2o.lechuan.bean.color.GoodsColor;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianColorAPI;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 微店单位管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianColorManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;

    private static VdianColorManager vdianColorManager;

    public static VdianColorManager getInstance() {
        if (vdianColorManager == null) {
            synchronized (VdianColorManager.class) {
                if (vdianColorManager == null) {
                    vdianColorManager = new VdianColorManager();
                }
            }
        }
        return vdianColorManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<ColorBean> callback) {
        Request<String> request = getStringRequest(VdianColorAPI.LIST);
        defaultParam(request);
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                ColorBean colorBean = gson.fromJson(response, ColorBean.class);
                if (colorBean != null) {
                    callback.onResult(what, colorBean);
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
     * 添加实体店颜色
     *
     * @param baseActivity
     * @param color
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, GoodsColor color, final IRequestCallBack<GoodsColor> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianColorAPI.ADD);
        request.add("goods_colour_id", RandomStringUtil.getOrderId());
        request.add("goods_colour_name", color.goods_colour_name);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsColor color = new Gson().fromJson(response, GoodsColor.class);
                iRequestCallBack.onResult(what, color);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品颜色
     *
     * @param baseActivity
     * @param color_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String color_id, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianColorAPI.DEL);
        request.add("goods_colour_id", color_id);
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
     * 编辑实体店商品颜色
     *
     * @param baseActivity
     * @param color
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, GoodsColor color, final IRequestCallBack<GoodsColor> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianColorAPI.EDIT);
        request.add("goods_colour_id", color.goods_colour_id);
        request.add("goods_colour_name", color.goods_colour_name);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsColor color = new Gson().fromJson(response, GoodsColor.class);
                iRequestCallBack.onResult(EDIT, color);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(EDIT, response);
            }
        }, true, false);
    }
}
