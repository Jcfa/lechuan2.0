/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poso2o.lechuan.http;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.login.LoginActivity;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created in Nov 4, 2015 12:02:55 PM.
 *
 * @author Yan Zhenjie.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Activity mActivity;
    /**
     * Dialog.
     */
//    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<String> callback;

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Activity activity, Request<?> request, HttpListener<String> httpCallback, boolean
            canCancel, boolean isLoading) {
        this.mActivity = activity;
        this.mRequest = request;
//        if (activity != null && isLoading) {
//            mWaitDialog = new WaitDialog(activity);
//            mWaitDialog.setCancelable(canCancel);
//            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    mRequest.cancel();
//                }
//            });
//        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
//        if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing())
//            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
//        if (mWaitDialog != null && mWaitDialog.isShowing())
//            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        WaitDialog.dismissLoaddingDialog();
        if (callback != null) {
            System.out.println("服务器数据：" + response.get());
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
//            if (response.get() instanceof String) {
            // 解析json数据
            try {
                JSONTokener jsonTokener = new JSONTokener((String) response.get());
                JSONObject json = (JSONObject) jsonTokener.nextValue();
                if (json.has("unionid") && json.has("openid")) {//微信接口,不截取data原数据返回
                    callback.onSucceed(what, json.toString());
                    return;
                }
                if (!json.has("code")) {//版本更新数据没有返回code
                    callback.onSucceed(what, response.get().toString());
                    return;
                }
                /**
                 * 兼容旧老板管理数据结构
                 */
                String code = json.getString("code");
                if (code.equals("success")) {
                    Object jsonObject = new JSONTokener(json.getString("data")).nextValue();
                    if (json.has("total")) {
//                        JSONObject data = json.optJSONObject("data");
                        JSONObject total = json.optJSONObject("total");
                        if (jsonObject instanceof JSONObject) {
                            JSONObject jsonObject1 = (JSONObject) jsonObject;
                            if (jsonObject1.has("list")) {
                                jsonObject1.put("total", total);
                                callback.onSucceed(what, jsonObject1.toString());
                                return;
                            } else {
//                                JSONObject jsonObject2 = new JSONObject();
//                                jsonObject2.put("total", total);
//                                jsonObject2.put("list", jsonObject);
//                                callback.onSucceed(what, jsonObject2.toString());
                                callback.onSucceed(what, jsonObject.toString());
                                return;
                            }
                        } else if (jsonObject instanceof JSONArray) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("total", total);
                            jsonObject1.put("list", jsonObject);
                            callback.onSucceed(what, jsonObject1.toString());
                            return;
                        }
                        callback.onSucceed(what, jsonObject.toString());
                        return;
                    }
                    callback.onSucceed(what, jsonObject.toString());
                    return;
                } else if (code.equals("codeError")) {
                    Toast.show(mActivity, "密码错误");
                    String msg = json.getString("msg");
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
                } else if (code.equals("mobileCodeError")) {
                    Toast.show(mActivity, "认证码错误");
                    String msg = json.getString("msg");
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
                } else if (code.equals("stopservice")) {
                    Toast.show(mActivity, "帐号已被冻结");
                    String msg = json.getString("msg");
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
                } else if (code.equals("enabled")) {
//                    Toast.show(mActivity, "帐号不存在");
                    String msg = json.getString("msg");
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
                } else if (code.equals("update_version")) {
//                    toastAndDismissLoading(null);
                    JSONObject data = json.getJSONObject("data");
                    Gson gson = new Gson();
//                    UpdateVersionData updateVersionData = gson.fromJson(data.toString(), UpdateVersionData.class);
//                    if (requestContext.getContextValue() instanceof BaseActivity) {
//                        ((BaseActivity) requestContext.getContextValue()).update_app_version(updateVersionData);
//                    }
                } else if (code.equals("error")) {
                    String msg = json.getString("msg");
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
                } else if (code.equals("loginout")) {
                    String msg = json.has("msg") ? json.getString("msg") : "";
                    if (msg.equals("")) {
                        msg = json.has("data") ? json.getString("data") : "";
                    }
                    //预处理后失败返回 目前也给返回数据结果
                    callback.onFailed(what, msg);
//                    Toast.show(mActivity, msg);
//                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));//微信登录未绑定loginout
                } else {
                    callback.onFailed(what, "错误：" + code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onFailed(what, "数据异常");
            } catch (ClassCastException e) {
                e.printStackTrace();
                callback.onFailed(what, "请求错误");
            }
//            }
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        WaitDialog.dismissLoaddingDialog();
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            Toast.show(mActivity, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            Toast.show(mActivity, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            Toast.show(mActivity, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            Toast.show(mActivity, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            Toast.show(mActivity, R.string.error_unknow);
        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, (String) response.get());
    }

}
