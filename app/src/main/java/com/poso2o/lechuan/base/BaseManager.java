package com.poso2o.lechuan.base;

import android.content.Context;
import android.util.Log;

import com.poso2o.lechuan.bean.BaseBean;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017-11-24.
 */

public class BaseManager<T> {

    /**
     * 页码类型
     */
    public static final int FIRST = 1;
    public static final int NEXT = 2;

    /**
     * 排序方向
     */
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    /**
     * 页码
     */
    protected int currPage = 0;

    /**
     * 创建一个String数据请求
     *
     * @return
     */
    public Request<String> getStringRequest(String url) {
        return getStringRequest(url, RequestMethod.POST);
    }

    public Request<String> getStringRequest(String url, RequestMethod method) {
        Request<String> request = NoHttp.createStringRequest(url, method);
        return request;
    }

    /**
     * 创建一个JSONObject数据请求
     *
     * @return
     */
    public Request<JSONObject> getJSONObjectRequest(String url) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        return request;
    }

    /**
     * 创建一个JSONArray数据请求
     *
     * @return
     */
    public Request<JSONArray> getJSONArrayRequest(String url) {
        Request<JSONArray> request = NoHttp.createJsonArrayRequest(url, RequestMethod.POST);
        return request;
    }

    public Request<T> defaultParam(Request<T> request) {
        Log.i("defaultParam", "defaultParam_uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + ",token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("shop_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        return request;
    }

    /**
     * 不要店铺ID，分销商的shop_id参数并不是自己的uid
     *
     * @param request
     * @return
     */
    public Request<T> defaultParamNoShop(Request<T> request) {
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        return request;
    }

    public boolean checkResult(Context context, BaseBean bean) {
        if (context == null || bean == null) {
//            Toast.show(context, "数据异常");
            return false;
        }
        if (!"success".equals(bean.code)) {
//            Toast.show(context, "请求失败");
            return false;
        }
        return true;
    }

}
