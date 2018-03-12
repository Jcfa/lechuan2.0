package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.spec.Spec;
import com.poso2o.lechuan.bean.spec.SpecBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianSpecAPI;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 微店规格管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianSpecManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;

    private static VdianSpecManager vdianSpecManager;

    public static VdianSpecManager getInstance() {
        if (vdianSpecManager == null) {
            synchronized (VdianSpecManager.class) {
                if (vdianSpecManager == null) {
                    vdianSpecManager = new VdianSpecManager();
                }
            }
        }
        return vdianSpecManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<SpecBean> callback) {
        Request<String> request = getStringRequest(VdianSpecAPI.LIST);
        defaultParam(request);
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                SpecBean specBean = gson.fromJson(response, SpecBean.class);
                if (specBean != null) {
                    callback.onResult(what, specBean);
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
     * 添加实体店规格
     *
     * @param baseActivity
     * @param spec
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, Spec spec, final IRequestCallBack<Spec> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSpecAPI.ADD);
        request.add("spec_id", RandomStringUtil.getOrderId());
        request.add("spec_name", spec.spec_name);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Spec spec = new Gson().fromJson(response, Spec.class);
                iRequestCallBack.onResult(what, spec);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品规格
     *
     * @param baseActivity
     * @param spec_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String spec_id, final IRequestCallBack iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSpecAPI.DEL);
        request.add("spec_id", spec_id);
        defaultParam(request);

        baseActivity.request(DEL, request, new HttpListener<String>() {

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
     * 编辑实体店商品规格
     *
     * @param baseActivity
     * @param spec
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, Spec spec, final IRequestCallBack<Spec> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSpecAPI.EDIT);
        request.add("spec_id",spec.spec_id);
        request.add("spec_name",spec.spec_name);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Spec spec = new Gson().fromJson(response, Spec.class);
                iRequestCallBack.onResult(what, spec);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

}
