package com.poso2o.lechuan.manager.wshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.member.AllMemberOrderData;
import com.poso2o.lechuan.bean.member.Member;
import com.poso2o.lechuan.bean.member.MemberAllData;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * Created by mr zhang on 2017/12/11.
 */

public class WMemberManager extends BaseManager {

    //微店会员信息
    public static final String W_MEMBER_LIST = HttpAPI.SERVER_MAIN_API + "MemberManage.htm?Act=query";
    public static final int W_MEMBER_LIST_ID = 20012;

    //微店会员详情
    public static final String W_MEMBER_INFO = HttpAPI.SERVER_MAIN_API + "MemberManage.htm?Act=info";
    public static final int W_MEMBER_INFO_ID = 20013;

    private static volatile WMemberManager wMemberManager;
    public static WMemberManager getwMemberManager(){
        if (wMemberManager == null){
            synchronized (WMemberManager.class){
                if (wMemberManager == null)wMemberManager = new WMemberManager();
            }
        }
        return wMemberManager;
    }

    /**
     * 微店会员列表
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void wMemberList(final BaseActivity baseActivity,String keywords,String currPage,String sort_name,String sort_type, final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(W_MEMBER_LIST);
        defaultParam(request);

        request.add("keywords",keywords);
        request.add("currPage",currPage);
        request.add("sort_name",sort_name);
        request.add("sort_type",sort_type);

        baseActivity.request(W_MEMBER_LIST_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MemberAllData memberAllData = new Gson().fromJson(response, MemberAllData.class);
                iRequestCallBack.onResult(W_MEMBER_LIST_ID,memberAllData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_MEMBER_LIST_ID,response);
            }
        },true,true);
    }

    /**
     * 会员详情
     * @param baseActivity
     * @param member_id
     * @param iRequestCallBack
     */
    public void wMemberInfo(final BaseActivity baseActivity,String member_id,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(W_MEMBER_INFO);
        defaultParam(request);

        request.add("member_id",member_id);

        baseActivity.request(W_MEMBER_INFO_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AllMemberOrderData allMemberOrderData = new Gson().fromJson(response,AllMemberOrderData.class);
                iRequestCallBack.onResult(W_MEMBER_INFO_ID,allMemberOrderData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(W_MEMBER_INFO_ID,response);
            }
        },true,true);
    }
}
