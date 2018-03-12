package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.member.AllMemberOrderData;
import com.poso2o.lechuan.bean.member.Member;
import com.poso2o.lechuan.bean.member.MemberAllData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RMemberHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RMemberManager extends BaseManager{

    //会员列表请求
    public static final int R_MEMBER_LIST = 3001;
    //会员详情请求
    public static final int R_MEMBER_INFO = 3002;

    //所有会员数据
    private ArrayList<Member> members = new ArrayList<>();

    private static volatile RMemberManager rMemberManager;
    public static RMemberManager getRMemberManager(){
        if (rMemberManager == null){
            synchronized (RMemberManager.class){
                if (rMemberManager == null)rMemberManager = new RMemberManager();
            }
        }
        return rMemberManager;
    }

    public void rMemberList(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RMemberHttpAPI.R_MEMBER_LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(R_MEMBER_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                MemberAllData memberAllData = new Gson().fromJson(response,MemberAllData.class);
                iRequestCallBack.onResult(R_MEMBER_LIST,memberAllData);
                if (memberAllData == null){
                    members = null;
                    return;
                }
                if (members == null)members = new ArrayList<Member>();
                members.clear();
                members.addAll(memberAllData.list);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_MEMBER_LIST,response);
            }
        },true,true);
    }

    public void rMemberInfo(final BaseActivity baseActivity,String uid,String begin_date,String close_date, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RMemberHttpAPI.R_MEMBER_INFO);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("begin_date", begin_date);
        request.add("close_date", close_date);
        request.add("uid", uid);

        baseActivity.request(R_MEMBER_INFO, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AllMemberOrderData allMemberOrderData = new Gson().fromJson(response,AllMemberOrderData.class);
                iRequestCallBack.onResult(R_MEMBER_INFO,allMemberOrderData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_MEMBER_INFO,response);
            }
        },true,true);
    }

    /**
     * 数据本地排序
     * @param type    排序类型，1为成交数排序，2为成交额排序，3为余额排序，4为积分排序
     * @param sort    DESC降序，ASC升序
     * @return
     */
    public ArrayList<Member> queryMember(int type,final String sort){
        if (members == null || members.size() < 2)return members;

        ArrayList<Member> sortMembers = new ArrayList<>();
        sortMembers.addAll(members);

        switch (type){
            case 1:
                Collections.sort(sortMembers, new Comparator<Member>() {

                    @Override
                    public int compare(Member o1, Member o2) {
                        Integer num1 = Integer.parseInt(o1.ordernum);
                        Integer num2 = Integer.parseInt(o2.ordernum);
                        if ("ASC".equals(sort)) {// 升序
                            return num1.compareTo(num2);
                        } else {// 降序
                            return num2.compareTo(num1);
                        }
                    }
                });
                break;
            case 2:
                Collections.sort(sortMembers, new Comparator<Member>() {

                    @Override
                    public int compare(Member o1, Member o2) {
                        Double num1 = Double.parseDouble(o1.orderamount);
                        Double num2 = Double.parseDouble(o2.orderamount);
                        if ("ASC".equals(sort)) {// 升序
                            return num1.compareTo(num2);
                        } else {// 降序
                            return num2.compareTo(num1);
                        }
                    }
                });
                break;
            case 3:
                Collections.sort(sortMembers, new Comparator<Member>() {

                    @Override
                    public int compare(Member o1, Member o2) {
                        Double num1 = Double.parseDouble(o1.amounts);
                        Double num2 = Double.parseDouble(o2.amounts);
                        if ("ASC".equals(sort)) {// 升序
                            return num1.compareTo(num2);
                        } else {// 降序
                            return num2.compareTo(num1);
                        }
                    }
                });
                break;
            case 4:
                Collections.sort(sortMembers, new Comparator<Member>() {

                    @Override
                    public int compare(Member o1, Member o2) {
                        Integer num1 = Integer.parseInt(o1.points);
                        Integer num2 = Integer.parseInt(o2.points);
                        if ("ASC".equals(sort)) {// 升序
                            return num1.compareTo(num2);
                        } else {// 降序
                            return num2.compareTo(num1);
                        }
                    }
                });
                break;
        }
        return sortMembers;
    }
}
