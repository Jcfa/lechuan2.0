package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.supplier.Supplier;
import com.poso2o.lechuan.bean.supplier.SupplierBankBean;
import com.poso2o.lechuan.bean.supplier.SupplierBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianSupplierAPI;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 微店供应商管理
 * Created by Jaydon on 2017/12/4.
 */
public class VdianSupplierManager extends BaseManager {

    private static final int LIST = 2001;
    private static final int ADD = 2002;
    private static final int DEL = 2003;
    private static final int EDIT = 2004;

    private static VdianSupplierManager vdianSupplierManager;

    public static VdianSupplierManager getInstance() {
        if (vdianSupplierManager == null) {
            synchronized (VdianSupplierManager.class) {
                if (vdianSupplierManager == null) {
                    vdianSupplierManager = new VdianSupplierManager();
                }
            }
        }
        return vdianSupplierManager;
    }

    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<SupplierBean> callback) {
        Request<String> request = getStringRequest(VdianSupplierAPI.LIST);
        defaultParam(request);
        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                SupplierBean supplierBean = gson.fromJson(response, SupplierBean.class);
                if (supplierBean != null) {
                    callback.onResult(what, supplierBean);
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
     * 添加实体店供应商
     *
     * @param baseActivity
     * @param supplier
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, Supplier supplier, final IRequestCallBack<Supplier> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSupplierAPI.ADD);
        request.add("supplier_id", RandomStringUtil.getOrderId());
        request.add("supplier_name", supplier.supplier_name);
        request.add("supplier_number", supplier.supplier_number);
        request.add("supplier_shortname", supplier.supplier_shortname);
        request.add("supplier_logo", supplier.supplier_logo);
        request.add("supplier_telephone", supplier.supplier_telephone);
        request.add("supplier_contacts", supplier.supplier_contacts);
        request.add("supplier_contacts_mobile", supplier.supplier_contacts_mobile);
        request.add("supplier_mail", supplier.supplier_mail);
        request.add("supplier_bank_name", supplier.supplier_bank_name);
        request.add("supplier_bank_account_id", supplier.supplier_bank_account_id);
        request.add("supplier_bank_account_name", supplier.supplier_bank_account_name);
        request.add("has_supplier_contract", Integer.toString(supplier.has_supplier_contract));
        request.add("total_goods_number", Integer.toString(supplier.total_goods_number));
        request.add("total_order_number", Integer.toString(supplier.total_order_number));
        request.add("total_order_amount", Double.toString(supplier.total_order_amount));
        request.add("province_id", supplier.province_id);
        request.add("province_name", supplier.province_name);
        request.add("city_id", supplier.city_id);
        request.add("city_name", supplier.city_name);
        request.add("area_id", supplier.area_id);
        request.add("area_name", supplier.area_name);
        request.add("supplier_address", supplier.supplier_address);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Supplier supplier = new Gson().fromJson(response, Supplier.class);
                iRequestCallBack.onResult(what, supplier);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 删除实体店商品供应商
     *
     * @param baseActivity
     * @param supplier_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String supplier_id, final IRequestCallBack iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSupplierAPI.DEL);
        request.add("supplier_id", supplier_id);
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
     * 编辑实体店商品供应商
     *
     * @param baseActivity
     * @param supplier
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, Supplier supplier, final IRequestCallBack<Supplier> iRequestCallBack) {
        Request<String> request = getStringRequest(VdianSupplierAPI.EDIT);
        request.add("supplier_id",supplier.supplier_id);
        request.add("supplier_name", supplier.supplier_name);
        request.add("supplier_number", supplier.supplier_number);
        request.add("supplier_shortname", supplier.supplier_shortname);
        request.add("supplier_logo", supplier.supplier_logo);
        request.add("supplier_telephone", supplier.supplier_telephone);
        request.add("supplier_contacts", supplier.supplier_contacts);
        request.add("supplier_contacts_mobile", supplier.supplier_contacts_mobile);
        request.add("supplier_mail", supplier.supplier_mail);
        request.add("supplier_bank_name", supplier.supplier_bank_name);
        request.add("supplier_bank_account_id", supplier.supplier_bank_account_id);
        request.add("supplier_bank_account_name", supplier.supplier_bank_account_name);
        request.add("has_supplier_contract", Integer.toString(supplier.has_supplier_contract));
        request.add("total_goods_number", Integer.toString(supplier.total_goods_number));
        request.add("total_order_number", Integer.toString(supplier.total_order_number));
        request.add("total_order_amount", Double.toString(supplier.total_order_amount));
        request.add("province_id", supplier.province_id);
        request.add("province_name", supplier.province_name);
        request.add("city_id", supplier.city_id);
        request.add("city_name", supplier.city_name);
        request.add("area_id", supplier.area_id);
        request.add("area_name", supplier.area_name);
        request.add("supplier_address", supplier.supplier_address);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Supplier supplier = new Gson().fromJson(response, Supplier.class);
                iRequestCallBack.onResult(what, supplier);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 加载银行列表
     */
    public void loadBanks(final BaseActivity baseActivity, final IRequestCallBack<SupplierBankBean> callback) {
        Request<String> request = getStringRequest(VdianSupplierAPI.BANK);
        defaultParam(request);

        baseActivity.request(0, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                SupplierBankBean supplierBankBean = new Gson().fromJson(response, SupplierBankBean.class);
                callback.onResult(what, supplierBankBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFailed(what, response);
            }
        }, true, false);
    }

}
