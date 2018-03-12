package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.shopdata.CompanyAuthorityBean;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.CompanyAuthorityAPI;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/6.
 */

public class CompanyAuthorityManager extends BaseManager {

    //提交企业认证信息
    public static final int COMPANY_AUTHORITY_COMMIT = 60001;
    //获取企业资质认证信息
    public static final int COMPANY_AUTHORITY_INFO = 60002;

    private static volatile CompanyAuthorityManager authorityManager;
    public static CompanyAuthorityManager getAuthorityManager(){
        if (authorityManager == null){
            synchronized (CompanyAuthorityManager.class){
                if (authorityManager == null){
                    authorityManager = new CompanyAuthorityManager();
                }
            }
        }
        return authorityManager;
    }

    /**
     * 提交企业认证信息
     * @param baseActivity
     * @param corporate_name
     * @param legal_person
     * @param business_license
     * @param business_license_img
     * @param shop_business_license_img
     * @param shop_scene_img
     * @param iRequestCallBack
     */
    public void authorityCommit(final BaseActivity baseActivity, String corporate_name,String legal_person, String mobile,  String business_license,String corporate_address, String business_license_img, String shop_business_license_img, String shop_scene_img,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(CompanyAuthorityAPI.COMPANY_AUTHORITY_COMMIT);
        defaultParam(request);
        request.add("corporate_name",corporate_name);
        request.add("mobile",mobile);
        request.add("legal_person",legal_person);
        request.add("business_license",business_license);
        request.add("business_license_img",business_license_img);
        request.add("shop_business_license_img",shop_business_license_img);
        request.add("shop_scene_img",shop_scene_img);
        request.add("corporate_address",corporate_address);

        baseActivity.request(COMPANY_AUTHORITY_COMMIT, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试提交：" + response);
                iRequestCallBack.onResult(COMPANY_AUTHORITY_COMMIT,response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity,response);
            }
        },true,true);
    }

    public void authorityInfo(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){
        final Request<String> request = getStringRequest(CompanyAuthorityAPI.COMPANY_AUTHORITY_INFO);
        defaultParam(request);

        baseActivity.request(COMPANY_AUTHORITY_INFO, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试资质：" + response);
                CompanyAuthorityBean bean = new Gson().fromJson(response,CompanyAuthorityBean.class);
                iRequestCallBack.onResult(COMPANY_AUTHORITY_INFO,bean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(COMPANY_AUTHORITY_INFO,response);
            }
        },true,true);
    }
}
