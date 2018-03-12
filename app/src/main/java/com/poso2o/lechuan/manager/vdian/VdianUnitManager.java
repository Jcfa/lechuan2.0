package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.unit.GoodsUnit;
import com.poso2o.lechuan.bean.unit.UnitBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianUnitAPI;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 微店单位管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianUnitManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;

    private static VdianUnitManager vdianUnitManager;

    public static VdianUnitManager getInstance() {
        if (vdianUnitManager == null) {
            synchronized (VdianUnitManager.class) {
                if (vdianUnitManager == null) {
                    vdianUnitManager = new VdianUnitManager();
                }
            }
        }
        return vdianUnitManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<UnitBean> callback) {
        Request<String> request = getStringRequest(VdianUnitAPI.LIST);
        defaultParam(request);
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                UnitBean unitBean = gson.fromJson(response, UnitBean.class);
                if (unitBean != null) {
                    callback.onResult(what, unitBean);
                } else {
                    callback.onFailed(what, baseActivity.getString(R.string.toast_load_unit_fail));
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
     * @param unit
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, GoodsUnit unit, final IRequestCallBack<GoodsUnit> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianUnitAPI.ADD);
        request.add("unit_id", RandomStringUtil.getOrderId());
        request.add("unit_name", unit.unit_name);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsUnit unit = new Gson().fromJson(response, GoodsUnit.class);
                iRequestCallBack.onResult(ADD, unit);
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
     * @param unit_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String unit_id, final IRequestCallBack<String> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianUnitAPI.DEL);
        request.add("unit_id", unit_id);
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
     * 编辑实体店商品目录
     *
     * @param baseActivity
     * @param unit
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, GoodsUnit unit, final IRequestCallBack<GoodsUnit> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianUnitAPI.EDIT);
        request.add("unit_id",unit.unit_id);
        request.add("unit_name",unit.unit_name);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                GoodsUnit unit = new Gson().fromJson(response, GoodsUnit.class);
                iRequestCallBack.onResult(EDIT, unit);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(EDIT, response);
            }
        }, true, false);
    }
}
