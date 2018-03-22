package com.poso2o.lechuan.manager.oa;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.oa.DefaultTemplates;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.wshop.OaAPI;
import com.poso2o.lechuan.util.TextUtil;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2018/2/5.
 */

public class ModelGroupManager extends BaseManager {
    //模板组列表
    public static final int MODEL_GROUPs_ID = 2101;
    //模板组内的模板列表
    public static final int MODEL_GROUP_LIST_ID = 2102;
    //单个模板详情
    public static final int MODEL_GROUP_INFO_ID = 2013;
    //默认的模板列表
    public static final int MODEL_DEFAULT_ID = 2014;

    private static ModelGroupManager modelGroupManager;
    private TemplateGroups groups;

    private ModelGroupManager(){

    }
    public static ModelGroupManager getModelGroupManager(){
        if (modelGroupManager == null){
            synchronized (ModelGroupManager.class){
                if (modelGroupManager == null)modelGroupManager = new ModelGroupManager();
            }
        }
        return modelGroupManager;
    }

    /**
     * 模板组列表
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void modelGroups(BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(OaAPI.MODEL_GROUPS_URL);
        defaultParam(request);

        baseActivity.request(MODEL_GROUPs_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                groups = new Gson().fromJson(response,TemplateGroups.class);
                iRequestCallBack.onResult(MODEL_GROUPs_ID, groups);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(MODEL_GROUPs_ID,response);
            }
        },true,true);
    }

    /**
     * 模板组内的模板列表
     * @param baseActivity
     * @param service_id
     * @param iRequestCallBack
     */
    public void modelGroupList(BaseActivity baseActivity,String service_id,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.MODEL_GROUP_LIST_URL);
        defaultParam(request);
        request.add("service_id",service_id);

        baseActivity.request(MODEL_GROUP_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(MODEL_GROUP_LIST_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(MODEL_GROUP_LIST_ID,response);
            }
        },true,true);
    }

    /**
     * 单个模板详情
     * @param baseActivity
     * @param template_id
     * @param iRequestCallBack
     */
    public void modelGroupInfo(BaseActivity baseActivity,String template_id,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(OaAPI.MODEL_GROUP_INFO_URL);
        defaultParam(request);
        request.add("template_id",template_id);

        baseActivity.request(MODEL_GROUP_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(MODEL_GROUP_INFO_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onResult(MODEL_GROUP_INFO_ID,response);
            }
        },true,true);
    }

    /**
     * 获取设置的默认模板
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void defaultTemplates(BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(OaAPI.OA_DEFAULT_TEMPLATE);
        defaultParamNoShop(request);

        baseActivity.request(MODEL_DEFAULT_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                DefaultTemplates defaultTemplates = new Gson().fromJson(response,DefaultTemplates.class);
                iRequestCallBack.onResult(MODEL_DEFAULT_ID,defaultTemplates);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(MODEL_DEFAULT_ID,response);
            }
        },true,true);
    }

    //根据模板组id，查找模板组
    public TemplateGroup findTemplateGroup(String group_id){
        if(TextUtil.isEmpty(group_id))return null;
        if (groups == null)return null;
        for (TemplateGroup group : groups.list){
            if (group.group_id.equals(group_id))return group;
        }
        return null;
    }
}
