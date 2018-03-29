package com.poso2o.lechuan.manager.wshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.shopdata.BangDingData;
import com.poso2o.lechuan.bean.shopdata.BindPayData;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.WShopHttpAPI;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/1.
 */

public class WShopManager<T> extends BaseManager {

    private static volatile WShopManager wShopManager;

    //微店详情标识
    public static final int W_SHOP_INFO_ID = 0001;

    //微店头像修改标识
    public static final int W_SHOP_LOGO_ID = 0002;

    //微店导入商品
    public static final int W_SHOP_IMPORT_GOODS = 0003;

    //微店信息修改
    public static final int W_SHOP_EDIT_ID = 0004;

    //微店公众号认证信息
    public static final int W_BINGING_STATE_ID = 0005;

    //提交或修改绑定微信支付
    public static final int W_COMMIT_BIND_ID = 0006;

    //绑定微信支付详情
    public static final int W_BIND_PAY_ID = 0007;

    public static WShopManager getrShopManager() {
        if (wShopManager == null) {
            synchronized (WShopManager.class) {
                if (wShopManager == null)
                    wShopManager = new WShopManager();
            }
        }
        return wShopManager;
    }

    /**
     * 微店详情
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void wShopInfo(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(WShopHttpAPI.W_SHOP_INFO);
        defaultParam(request);
        request.add("shop_branch_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(W_SHOP_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                ShopData shopData = new Gson().fromJson(response, ShopData.class);
                iRequestCallBack.onResult(W_SHOP_INFO_ID, shopData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 获取乐传帐号详情
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void getlcAccountDetailInfo(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(WShopHttpAPI.LC_ACCOUNT_DETAIL);
        defaultParam(request);
        request.add("open_uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        baseActivity.request(W_SHOP_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                UserInfoBean userInfoBean = new Gson().fromJson(response, UserInfoBean.class);
                SharedPreferencesUtils.saveUserInfo(userInfoBean);
                iRequestCallBack.onResult(W_SHOP_INFO_ID, userInfoBean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 微店头像上传
     *
     * @param baseActivity
     * @param base64pic
     * @param iRequestCallBack
     */
    public void updateWShopLogo(final BaseActivity baseActivity, final String base64pic, final IRequestCallBack iRequestCallBack) {

        Request request = getStringRequest(WShopHttpAPI.W_SHOP_UPDATE_LOGO + "&token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN) + "&uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&key=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN) + "&shop_id=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("base64pic", base64pic);

        baseActivity.request(W_SHOP_LOGO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(W_SHOP_LOGO_ID, "Success");
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, true);
    }

    /**
     * 导入商品
     *
     * @param baseActivity
     * @param goods_id
     * @param offline          true下架，false上架
     * @param iRequestCallBack
     */
    public void importGoods(final BaseActivity baseActivity, String goods_id, boolean offline, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(offline ? WShopHttpAPI.W_SHOP_IMPORT_GOODS_OFFLINE : WShopHttpAPI.W_SHOP_IMPORT_GOODS_ONLINE);
        defaultParam(request);
        request.add("datas", goods_id);
        Print.println("参数是：" + goods_id);

        baseActivity.request(W_SHOP_IMPORT_GOODS, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试数据：" + response);
                iRequestCallBack.onResult(W_SHOP_IMPORT_GOODS, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_SHOP_IMPORT_GOODS, response);
            }
        }, true, true);
    }

    /**
     * 修改店铺信息
     *
     * @param baseActivity
     * @param shopData
     * @param iRequestCallBack
     */
    public void editWShop(final BaseActivity baseActivity, ShopData shopData, final IRequestCallBack iRequestCallBack) {

        Request<String> request = getStringRequest(WShopHttpAPI.W_SHOP_EDIT_URL);
        defaultParam(request);

        request.add("shop_opentime", shopData.shop_opentime);
        request.add("shop_closetime", shopData.shop_closetime);
        request.add("shop_name", shopData.shop_name);
        request.add("shop_logo", shopData.shop_logo);
        request.add("shop_introduction", shopData.shop_introduction);
        request.add("shop_mobile", shopData.shop_mobile);
        request.add("shop_tel", shopData.shop_tel);
        request.add("shop_contacts", shopData.shop_contacts);
        request.add("shop_real_name", shopData.shop_real_name);
        request.add("province_id", shopData.province_id);
        request.add("province_name", shopData.province_name);
        request.add("city_id", shopData.city_id);
        request.add("city_name", shopData.city_name);
        request.add("area_id", shopData.area_id);
        request.add("area_name", shopData.area_name);
        request.add("address", shopData.address);
        request.add("freight_num", shopData.freight_num);
        request.add("freight", shopData.freight);
        request.add("freight_addnum", shopData.freight_addnum);
        request.add("freight_add", shopData.freight_add);

        baseActivity.request(W_SHOP_EDIT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(W_SHOP_EDIT_ID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_SHOP_EDIT_ID, response);
            }
        }, true, true);
    }

    /**
     * 公众号认证状态
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void authorizeState(BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(WShopHttpAPI.W_BINGING_STATE);
        defaultParam(request);

        baseActivity.request(W_BINGING_STATE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BangDingData bangDingData = new Gson().fromJson(response, BangDingData.class);
                iRequestCallBack.onResult(W_BINGING_STATE_ID, bangDingData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_BINGING_STATE_ID, response);
            }
        }, true, true);
    }

    /**
     * 提交或修改绑定的微信appkey和商户号
     *
     * @param baseActivity
     * @param appkey
     * @param mch_id
     * @param iRequestCallBack
     */
    public void bindPay(BaseActivity baseActivity, String appkey, String mch_id, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(WShopHttpAPI.W_COMMIT_BIND_PAY);
        defaultParamNoShop(request);

        request.add("appkey", appkey);
        request.add("mch_id", mch_id);

        baseActivity.request(W_COMMIT_BIND_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(W_COMMIT_BIND_ID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_COMMIT_BIND_ID, response);
            }
        }, true, true);
    }

    /**
     * 获取绑定微信支付详情
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void bindPayInfo(BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(WShopHttpAPI.W_BIND_PAY_INFO);
        defaultParamNoShop(request);

        baseActivity.request(W_BIND_PAY_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BindPayData bindPayData = new Gson().fromJson(response, BindPayData.class);
                iRequestCallBack.onResult(W_BIND_PAY_ID, bindPayData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_BIND_PAY_ID, response);
            }
        }, true, true);
    }

    /**
     * 绑定收款帐号
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void setBankAccount(BaseActivity baseActivity, String shop_bank_code, String shop_bank_name, String shop_bank_account_name,
                               String shop_bank_account_no, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(WShopHttpAPI.SET_BANK_ACCOUNT);
        defaultParam(request);
        request.add("shop_bank_code", shop_bank_code);
        request.add("shop_bank_name", shop_bank_name);
        request.add("shop_bank_account_name", shop_bank_account_name);
        request.add("shop_bank_account_no", shop_bank_account_no);

        baseActivity.request(W_BIND_PAY_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                BindPayData bindPayData = new Gson().fromJson(response, BindPayData.class);
                iRequestCallBack.onResult(W_BIND_PAY_ID, bindPayData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_BIND_PAY_ID, response);
            }
        }, true, true);
    }
}
