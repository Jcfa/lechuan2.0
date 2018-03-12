package com.poso2o.lechuan.manager.oa;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.article.ArticleBean;
import com.poso2o.lechuan.bean.oa.RenewalsList;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.wshop.OaRenewalsAPI;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2018/3/10.
 *
 * 稿件箱数据管理类
 */

public class RenewalsManager extends BaseManager {

    //稿件箱列表
    private static final int RENEWALS_LIST_ID = 2301;
    //添加稿件
    private static final int RENEWALS_ADD_ID = 2302;
    //修改稿件
    private static final int RENEWALS_EDIT_ID = 2303;
    //删除稿件
    private static final int RENEWALS_DEL_ID = 2304;
    //稿件详情
    private static final int RENEWALS_INFO_ID = 2305;

    private static RenewalsManager renewalsManager;
    private RenewalsManager(){
    }
    public static RenewalsManager getRenewalsManager(){
        if (renewalsManager == null){
            synchronized (RenewalsManager.class){
                if (renewalsManager == null)renewalsManager = new RenewalsManager();
            }
        }
        return renewalsManager;
    }

    /**
     * 稿件箱列表
     * @param baseActivity
     * @param currPage
     * @param iRequestCallBack
     */
    public void renewalsList(BaseActivity baseActivity, int currPage, final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaRenewalsAPI.RENEWALS_LIST_URL);
        defaultParamNoShop(request);
        request.add("currPage",currPage + "");

        baseActivity.request(RENEWALS_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                RenewalsList renewalsList = new Gson().fromJson(response,RenewalsList.class);
                iRequestCallBack.onResult(RENEWALS_LIST_ID,renewalsList);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(RENEWALS_LIST_ID,response);
            }
        },true,true);
    }

    /**
     * 添加到稿件箱
     * @param baseActivity
     * @param article
     * @param iRequestCallBack
     */
    public void renewalsAdd(BaseActivity baseActivity, Article article,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaRenewalsAPI.RENEWALS_ADD_URL);
        defaultParamNoShop(request);
        request.add("articles",new Gson().toJson(article));

        baseActivity.request(RENEWALS_ADD_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(RENEWALS_ADD_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(RENEWALS_ADD_ID,response);
            }
        },true,true);
    }

    /**
     * 修改稿件
     * @param baseActivity
     * @param news_id
     * @param article
     * @param iRequestCallBack
     */
    public void renewalsEdit(BaseActivity baseActivity,String news_id,Article article,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaRenewalsAPI.RENEWALS_EDIT_URL);
        defaultParamNoShop(request);
        request.add("news_id",news_id);
        request.add("articles",new Gson().toJson(article));

        baseActivity.request(RENEWALS_EDIT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(RENEWALS_EDIT_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(RENEWALS_EDIT_ID,response);
            }
        },true,true);
    }

    /**
     * 删除稿件
     * @param baseActivity
     * @param news_id
     * @param iRequestCallBack
     */
    public void renewalsDel(BaseActivity baseActivity,String news_id,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaRenewalsAPI.RENEWALS_DEL_URL);
        defaultParamNoShop(request);
        request.add("news_id",news_id);

        baseActivity.request(RENEWALS_DEL_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(RENEWALS_DEL_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(RENEWALS_DEL_ID,response);
            }
        },true,true);
    }

    /**
     * 稿件详情
     * @param baseActivity
     * @param news_id
     * @param iRequestCallBack
     */
    public void renewalsInfo(BaseActivity baseActivity,String news_id,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaRenewalsAPI.RENEWALS_INFO_URL);
        defaultParamNoShop(request);
        request.add("news_id",news_id);

        baseActivity.request(RENEWALS_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(RENEWALS_INFO_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(RENEWALS_INFO_ID,response);
            }
        },true,true);
    }
}
