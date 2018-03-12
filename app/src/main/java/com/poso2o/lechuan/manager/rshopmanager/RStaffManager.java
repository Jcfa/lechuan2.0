package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.powerdata.PowerAllData;
import com.poso2o.lechuan.bean.powerdata.PowerData;
import com.poso2o.lechuan.bean.staff.AllStaffData;
import com.poso2o.lechuan.bean.staff.StaffData;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RStaffHttpAPI;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/2.
 */

public class RStaffManager extends BaseManager {

    //员工列表
    public static final int R_STAFF_LIST = 4001;
    //添加员工
    public static final int R_STAFF_ADD = 4002;
    //编辑员工
    public static final int R_STAFF_EDIT = 4003;
    //删除员工
    public static final int R_STAFF_DEL = 4004;

    //权限（职位）列表
    public static final int R_POSITION_LIST = 4005;
    //增加权限
    public static final int R_POSITION_ADD = 4006;
    //编辑权限
    public static final int R_POSITION_EDIT = 4007;
    //删除权限
    public static final int R_POSITION_DEL = 4008;

    //员工列表
    private ArrayList<StaffData> staffDatas = new ArrayList<>();

    private static volatile RStaffManager rStaffManager;
    public static RStaffManager getRStaffManager(){
        if (rStaffManager == null){
            synchronized (RStaffManager.class){
                if (rStaffManager == null)rStaffManager = new RStaffManager();
            }
        }
        return rStaffManager;
    }

    /**
     * 请求员工列表
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void rStaffList(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_STAFF_LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(R_STAFF_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试员工：" + response);
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                AllStaffData allStaffData = new Gson().fromJson(response,AllStaffData.class);
                iRequestCallBack.onResult(R_STAFF_LIST,allStaffData);
                if (allStaffData != null)staffDatas.addAll(allStaffData.list);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_STAFF_LIST,response);
            }
        },true,true);
    }

    /**
     * 增加员工
     * @param baseActivity
     * @param staffData
     * @param iRequestCallBack
     */
    public void rStaffAdd(final BaseActivity baseActivity, StaffData staffData, final IRequestCallBack iRequestCallBack){

        if (isAlreadyHave(staffData.czy)){
            iRequestCallBack.onFailed(R_STAFF_ADD,"该工号已被使用,请重新输入");
            return;
        }

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_STAFF_ADD);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("czyid", staffData.czy);
        request.add("realname", staffData.realname);
        request.add("mobile", staffData.mobile);
        request.add("password", staffData.password);
        request.add("positionid", staffData.positionid);

        baseActivity.request(R_STAFF_ADD, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试员工：" + response);
                iRequestCallBack.onResult(R_STAFF_ADD,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_STAFF_ADD,response);
            }
        },true,true);
    }

    /**
     * 编辑员工
     * @param baseActivity
     * @param staffData
     * @param iRequestCallBack
     */
    public void rStaffEdit(final BaseActivity baseActivity, StaffData staffData, final IRequestCallBack iRequestCallBack){

        /*不可修改工号，所以不用这一步判断
        if (staffData.edit_no && isAlreadyHave(staffData.czy)){
            iRequestCallBack.onFailed(R_STAFF_EDIT,"该员工号已被使用,请重新输入");
            return;
        }*/

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_STAFF_EDIT);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("czyid", staffData.czy);
        request.add("realname", staffData.realname);
        request.add("mobile", staffData.mobile);
        request.add("password", staffData.password);
        request.add("positionid", staffData.positionid);

        baseActivity.request(R_STAFF_EDIT, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试员工：" + response);
                iRequestCallBack.onResult(R_STAFF_EDIT,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_STAFF_EDIT,response);
            }
        },true,true);
    }

    /**
     * 删除员工
     * @param baseActivity
     * @param czy_id
     * @param iRequestCallBack
     */
    public void rStaffDel(final BaseActivity baseActivity, final String czy_id, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_STAFF_DEL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czyid", czy_id);

        baseActivity.request(R_STAFF_DEL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //解决旧版接口数据结构不统一问题
//                if (response.startsWith("[") && response.endsWith("]")){
//                    response = "{\nlist\n:" + response + "}";
//                }
//                AllStaffData allStaffData = new Gson().fromJson(response,AllStaffData.class);
                removeLocalStaff(czy_id);
                iRequestCallBack.onResult(R_STAFF_DEL,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_STAFF_DEL,response);
            }
        },true,true);
    }


    /**
     * 权限列表
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void rPositionList(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_POSITION_LIST);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        baseActivity.request(R_POSITION_LIST, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试权限：" + response);
                //解决旧版接口数据结构不统一问题
                if (response.startsWith("[") && response.endsWith("]")){
                    response = "{\nlist\n:" + response + "}";
                }
                PowerAllData powerAllData = new Gson().fromJson(response,PowerAllData.class);
                iRequestCallBack.onResult(R_POSITION_LIST,powerAllData);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_POSITION_LIST,response);
            }
        },true,true);
    }

    /**
     * 添加职位
     * @param baseActivity
     * @param powerData
     * @param iRequestCallBack
     */
    public void rPositionAdd(final BaseActivity baseActivity, PowerData powerData, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_POSITION_ADD);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("positionname", powerData.positionname);
        request.add("product_add", powerData.product_add);
        request.add("product_edit", powerData.product_edit);
        request.add("product_del", powerData.product_del);
        request.add("client_add", powerData.client_add);
        request.add("client_edit", powerData.client_edit);
        request.add("client_del", powerData.client_del);
        request.add("order_add", powerData.order_add);
        request.add("order_edit", powerData.order_edit);
        request.add("order_del", powerData.order_del);
        request.add("client_payment", powerData.client_payment);
        request.add("order_fh", powerData.order_fh);
        request.add("order_query", powerData.order_query);
        request.add("order_discount", powerData.order_discount);
        request.add("indextotal", powerData.indextotal);
        request.add("kc_add", powerData.kc_add);
        request.add("kc_del", powerData.kc_del);
        request.add("kc_back", powerData.kc_back);
        request.add("kc_booking", powerData.kc_booking);
        request.add("kc_bug", powerData.kc_bug);
        request.add("kc_update", powerData.kc_update);
        request.add("factory", powerData.factory);
        request.add("factory_contract", powerData.factory_contract);
        request.add("order_quote", powerData.order_quote);
        request.add("total_sales", powerData.total_sales);
        request.add("total_product", powerData.total_product);
        request.add("total_czy", powerData.total_czy);
        request.add("shop_seting", powerData.shop_seting);
        request.add("user_seting", powerData.user_seting);
        request.add("user_position", powerData.user_position);
        request.add("total_shop", powerData.total_shop);

        baseActivity.request(R_POSITION_ADD, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_POSITION_ADD,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_POSITION_ADD,response);
            }
        },true,true);
    }

    /**
     * 编辑权限
     * @param baseActivity
     * @param powerData
     * @param iRequestCallBack
     */
    public void rPositionEdit(final BaseActivity baseActivity,PowerData powerData, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_POSITION_EDIT);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));

        request.add("positionname", powerData.positionname);
        request.add("positionid", powerData.positionid);
        request.add("product_add", powerData.product_add);
        request.add("product_edit", powerData.product_edit);
        request.add("product_del", powerData.product_del);
        request.add("client_add", powerData.client_add);
        request.add("client_edit", powerData.client_edit);
        request.add("client_del", powerData.client_del);
        request.add("order_add", powerData.order_add);
        request.add("order_edit", powerData.order_edit);
        request.add("order_del", powerData.order_del);
        request.add("client_payment", powerData.client_payment);
        request.add("order_fh", powerData.order_fh);
        request.add("order_query", powerData.order_query);
        request.add("order_discount", powerData.order_discount);
        request.add("indextotal", powerData.indextotal);
        request.add("kc_add", powerData.kc_add);
        request.add("kc_del", powerData.kc_del);
        request.add("kc_back", powerData.kc_back);
        request.add("kc_booking", powerData.kc_booking);
        request.add("kc_bug", powerData.kc_bug);
        request.add("kc_update", powerData.kc_update);
        request.add("factory", powerData.factory);
        request.add("factory_contract", powerData.factory_contract);
        request.add("order_quote", powerData.order_quote);
        request.add("total_sales", powerData.total_sales);
        request.add("total_product", powerData.total_product);
        request.add("total_czy", powerData.total_czy);
        request.add("shop_seting", powerData.shop_seting);
        request.add("user_seting", powerData.user_seting);
        request.add("user_position", powerData.user_position);
        request.add("total_shop", powerData.total_shop);

        baseActivity.request(R_POSITION_EDIT, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(R_POSITION_EDIT,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_POSITION_EDIT,response);
            }
        },true,true);
    }

    public void rPositionDel(final BaseActivity baseActivity, String positionid, final IRequestCallBack iRequestCallBack){

        final Request<String> request = getStringRequest(RStaffHttpAPI.R_POSITION_DEL);
        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("positionid", positionid);

        baseActivity.request(R_POSITION_DEL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                System.out.println("测试权限：" + response);
                iRequestCallBack.onResult(R_POSITION_DEL,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(R_POSITION_DEL,response);
            }
        },true,true);
    }

    //添加员工时判断工号是否重复
    private boolean isAlreadyHave(String staff_no){
        for (StaffData staff : staffDatas){
            if (staff.czy.equals(staff_no))return true;
        }
        return false;
    }

    //服务端删除员工成功后，删除本地员工数据，同步服务端
    private void removeLocalStaff(String staff_czy){
        StaffData temp = null;
        for (StaffData staff : staffDatas){
            if (staff.czy.equals(staff_czy)){
                temp = staff;
                break;
            }
        }
        if (temp != null)staffDatas.remove(temp);
    }
}
