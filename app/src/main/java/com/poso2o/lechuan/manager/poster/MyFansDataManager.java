package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.event.InviteEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.poster.MyFansQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

import org.greenrobot.eventbus.EventBus;

/**
 * 广告我的粉丝
 * Created by Luo on 2017-12-01.
 */

public class MyFansDataManager<T> extends BaseManager {
    public static MyFansDataManager mMyFansDataManager;
    private static final String SORT_NAME_PUBLISH = "";//文章数排序
    private static final String SORT_NAME_FANS = "";//粉丝数排序
    private static final String DESC = "desc";
    private static final String ASC = "asc";
    /**
     * 获取广告 - 我的粉丝
     */
    private final int TAG_POSTER_ID = 1;

    public static MyFansDataManager getMyFansDataManager() {
        if (mMyFansDataManager == null) {
            mMyFansDataManager = new MyFansDataManager();
        }
        return mMyFansDataManager;
    }

    /**
     * 获取广告 - 我的粉丝
     *
     * @param baseActivity
     * @param currPage     查看页码 （必填）
     * @param callBack     回调
     */
    public void loadFansData(final BaseActivity baseActivity, final String currPage, int type, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_MY_FANS_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("currPage", currPage);
        request.add("has_distributor", type + "");
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                MyFansQueryDTO posterQueryDTO = gson.fromJson(response, MyFansQueryDTO.class);
                callBack.onResult(TAG_POSTER_ID, posterQueryDTO);
            }

            @Override
            public void onFailed(int what, String response) {
                //Toast.show(baseActivity, response);
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 获取广告 - 分销邀请
     *
     * @param baseActivity
     * @param id                           = 分销商ID
     * @param commission_rate              =佣金比例（必填）
     * @param commission_discount=专享折扣（必填）
     * @param callBack                     回调
     */
    public void loadFansInvitingData(final BaseActivity baseActivity, String id, final String commission_rate, final String commission_discount, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_SEND_INVITED_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("distributor_id", id);
        request.add("commission_rate", commission_rate);
        request.add("commission_discount", commission_discount);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                InvitationBean invitationBean = gson.fromJson(response, InvitationBean.class);
                callBack.onResult(TAG_POSTER_ID, invitationBean);
            }

            @Override
            public void onFailed(int what, String response) {
                //Toast.show(baseActivity, response);
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }

    /**
     * 同意邀请成为分销商
     */
    public void acceptInvitation(final BaseActivity baseActivity, String shopId, String shopNick) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_ACCEPT_INVITED_API);
        request.add("shop_id", shopId);
        request.add("token", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                EventBus.getDefault().post(new InviteEvent());
                Toast.show(baseActivity, "接受邀请成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                Toast.show(baseActivity, "接受邀请失败：" + response);
            }
        }, true, true);
    }
}
