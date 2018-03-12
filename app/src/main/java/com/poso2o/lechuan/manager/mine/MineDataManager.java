package com.poso2o.lechuan.manager.mine;

import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.mine.AcceptDetailData;
import com.poso2o.lechuan.bean.mine.AmountTotalBean;
import com.poso2o.lechuan.bean.mine.BrokerageDetailData;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.MerchantData;
import com.poso2o.lechuan.bean.mine.PosterData;
import com.poso2o.lechuan.bean.mine.RedPacketData;
import com.poso2o.lechuan.bean.mine.RedPacketRecordData;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Administrator on 2017-12-01.
 */

public class MineDataManager extends BaseManager {
    //广告转发列表
    public static int TAG_MY_FORWARD = 111;
    //分销商列表
    public static int TAG_DISTRIBUTION = 112;
    //商家列表
    public static int TAG_MERCHANT = 113;
    //红包余额收支记录
    public static int TAG_REDPACKET = 114;

    //修改手机
    public static int TAG_EDIT_PHONE = 120;
    //修改昵称
    public static int TAG_EDIT_NICK = 121;
    //修改logo
    public static int TAG_EDIT_LOGO = 122;
    //修改简介
    public static int TAG_EDIT_DESCRIPTION = 123;
    //充值，收入，支出明细
    public static int TAG_RED_PACKET_RECORD = 124;
    //用户信息
    public static int TAG_GET_USER_INFO = 125;
    //佣金结算
    public static int TAG_COMMISSION_PAYMENT = 126;
    //解除分销
    public static int TAG_RELIEVE_DISTRIBUTION = 127;
    //邀请分销
    public static int TAG_INVITATION_DISTRIBUTION = 128;
    //收入，支出，充值金额统计
    public static int TAG_AMOUNT_TOTAL = 129;
    //背景封面图
    public static int TAG_EDIT_BACKGROUND_LOGO = 130;

    private static MineDataManager mMineDataManager;
    private Gson mGson = new Gson();

    private MineDataManager() {
    }

    public static MineDataManager getMineDataManager() {
        synchronized (MineDataManager.class) {
            if (mMineDataManager == null) {
                mMineDataManager = new MineDataManager();
            }
            return mMineDataManager;
        }
    }

    /**
     * 我的-广告转发-文章列表
     */
    public void getMyForwardEssayList(BaseActivity baseActivity, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_FORWARD_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        baseActivity.request(TAG_MY_FORWARD, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                PosterData data = mGson.fromJson(response, PosterData.class);
//                Log.i("getMyForwardEssayList", "getMyForwardEssayList_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 我的-红包余额-红包收支
     *
     * @param baseActivity
     * @param page
     * @param callBack
     */
    public void getMyRedPacketList(BaseActivity baseActivity, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_REDPACKET_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        baseActivity.request(TAG_REDPACKET, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                RedPacketData data = mGson.fromJson(response, RedPacketData.class);
//                Log.i("getMyRedPacketList", "getMyRedPacketList_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 我的-未结佣金-商家列表
     */
    public void getMyMerchantList(BaseActivity baseActivity, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_MERCHANT_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        baseActivity.request(TAG_MERCHANT, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MerchantData data = mGson.fromJson(response, MerchantData.class);
//                Log.i("getMyMerchantList", "getMyMerchantList_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 我的-未结佣金-分销商列表
     */
    public void getMyDistributionList(BaseActivity baseActivity, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_DISTRIBUTION_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        baseActivity.request(TAG_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                MerchantData data = mGson.fromJson(response, MerchantData.class);
//                Log.i("getMyDistributionList", "getMyDistributionList_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 按商家查佣金详情列表
     *
     * @param baseActivity
     * @param id           分销商ID
     * @param page
     * @param callBack
     */
    public void getBrokerageListForMerchant(BaseActivity baseActivity, String id, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_MERCHANT_BROKERAGE_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        request.add("shop_id", id);
        baseActivity.request(TAG_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BrokerageDetailData data = mGson.fromJson(response, BrokerageDetailData.class);
//                Log.i("按商家查佣金详情", "getBrokerageListForMerchant_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 按分销商查佣金详情列表
     *
     * @param baseActivity
     * @param page
     * @param callBack
     */
    public void getBrokerageListForDistribution(BaseActivity baseActivity, String id, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_DISTRIBUTION_BROKERAGE_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        request.add("distributor_id", id);
        baseActivity.request(TAG_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BrokerageDetailData data = mGson.fromJson(response, BrokerageDetailData.class);
//                Log.i("按分销商查佣金详情", "getBrokerageListForDistribution_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 按文章查佣金列表
     *
     * @param baseActivity
     * @param id
     * @param page
     * @param callBack
     */
    public void getBrokerageListForEssay(BaseActivity baseActivity, String id, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_ESSAY_BROKERAGE_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        request.add("news_id", id);
        baseActivity.request(TAG_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                BrokerageDetailData data = mGson.fromJson(response, BrokerageDetailData.class);
//                Log.i("按文章查佣金列表", "getBrokerageListForDistribution_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 按文章查红包列表
     *
     * @param baseActivity
     * @param id
     * @param page
     * @param callBack
     */
    public void getRedPacketListForEssay(BaseActivity baseActivity, String id, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_ESSAY_RED_PACKET_LIST_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        request.add("news_id", id);
        baseActivity.request(TAG_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AcceptDetailData data = mGson.fromJson(response, AcceptDetailData.class);
//                Log.i("按文章查佣金列表", "getBrokerageListForDistribution_response=" + response);
//                Log.i("getMyForwardEssayList", "data=" + data.data.list.size());
                callBack.onResult(what, data);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 修改手机号
     *
     * @param baseActivity
     * @param phone
     * @param callBack
     */
    public void editPhone(final BaseActivity baseActivity, String phone, String verifyCode, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_PHONE_API);
        request = defaultParamNoShop(request);
        request.add("mobile", phone);
        request.add("verifyCode", verifyCode);
        baseActivity.request(TAG_EDIT_PHONE, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(what, "修改手机号成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }


    /**
     * 修改昵称
     *
     * @param baseActivity
     * @param nick
     * @param callBack
     */
    public void editNick(final BaseActivity baseActivity, String nick, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_NICK_API);
        request = defaultParamNoShop(request);
        request.add("nick", nick);
        baseActivity.request(TAG_EDIT_NICK, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(what, "修改昵称成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onResult(what, response);
            }
        }, true, true);
    }


    /**
     * 修改logo
     *
     * @param baseActivity
     * @param logo
     * @param callBack
     */
    public void editLogo(final BaseActivity baseActivity, String logo, final IRequestCallBack callBack) {
        Log.i("editLogo", "logo=" + logo);
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_LOGO_API + "&uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
//        request = defaultParam(request);
        if (!TextUtil.isEmpty(logo)) {
            File file = new File(logo);
            FileBinary binary = new FileBinary(file);
            request.add("logo", binary);
        } else {
            Toast.show(baseActivity, "没有选择图片");
            return;
        }
        baseActivity.request(TAG_EDIT_LOGO, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(what, "修改头像成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 编辑相册封面图
     *
     * @param baseActivity
     * @param logo
     * @param callBack
     */
    public void editBackgroundLogo(final BaseActivity baseActivity, String logo, final IRequestCallBack callBack) {
        Log.i("editBackgroundLogo", "logo=" + logo);
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_BACKGROUND_LOGO_API + "&uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID) + "&token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        if (!TextUtil.isEmpty(logo)) {
            File file = new File(logo);
            FileBinary binary = new FileBinary(file);
            request.add("logo", binary);
        } else {
            Toast.show(baseActivity, "没有选择图片");
            return;
        }
        baseActivity.request(TAG_EDIT_BACKGROUND_LOGO, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(what, "设置相册封面成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 修改简介
     *
     * @param baseActivity
     * @param remark
     * @param callBack
     */
    public void editDescription(final BaseActivity baseActivity, String remark, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_DESCRIPTION_API);
        request = defaultParamNoShop(request);
        request.add("remark", remark);
        baseActivity.request(TAG_EDIT_DESCRIPTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                callBack.onResult(what, "修改个人简介成功！");
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 修改头像
     */
    public void editLogo(final BaseActivity baseActivity, File file, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_EDIT_LOGO_API);
        request = defaultParam(request);
        FileBinary binary = new FileBinary(file);
        request.add("logo", binary);
        baseActivity.request(TAG_EDIT_DESCRIPTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Toast.show(baseActivity, response);
//                callBack
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 1充值，2收入，3支出明细
     *
     * @param baseActivity
     * @param type
     * @param callBack
     */
    public void getRedPacketRecordList(final BaseActivity baseActivity, int type, int page, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_RED_PACKET_RECORD_API);
        request = defaultParamNoShop(request);
        request.add("currPage", page + "");
        request.add("amount_type", type + "");
        baseActivity.request(TAG_RED_PACKET_RECORD, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                RedPacketRecordData redPacketData = mGson.fromJson(response, RedPacketRecordData.class);
                callBack.onResult(what, redPacketData);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 获取用户信息
     *
     * @param baseActivity
     * @param callBack
     */
    public void getUserInfo(final BaseActivity baseActivity, final String id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_GET_USER_INFO_API);
        request = defaultParamNoShop(request);
        request.add("open_uid", id);
        baseActivity.request(TAG_GET_USER_INFO, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                UserInfoBean userInfoData = mGson.fromJson(response, UserInfoBean.class);
                if (id.equals(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID))) {//这个接口并不是仅请求自己的帐号详情，根据ID查询只是等于自己时才保存
                    SharedPreferencesUtils.saveUserInfo(userInfoData);
                }
                EventBus.getDefault().post(userInfoData);
                if (callBack != null) {
                    callBack.onResult(what, userInfoData);
                }
            }

            @Override
            public void onFailed(int what, String response) {
                if (callBack != null) {
                    callBack.onFailed(what, response);
                }
            }
        }, true, true);
    }


    /**
     * 佣金结算
     *
     * @param baseActivity
     * @param callBack
     */
    public void doCommissionPayment(final BaseActivity baseActivity, String distributorId, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_COMMISSION_PAYMENT_API);
        request = defaultParam(request);
        request.add("distributor_id", distributorId);
        baseActivity.request(TAG_COMMISSION_PAYMENT, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                Toast.show(baseActivity, response);
//                UserInfoBean userInfoData = mGson.fromJson(response, UserInfoBean.class);
                callBack.onResult(TAG_COMMISSION_PAYMENT, response);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 解除分销
     *
     * @param baseActivity
     * @param distributorId
     * @param callBack
     */
    public void doRelieveDistribution(final BaseActivity baseActivity, String shopId, String distributorId, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_RELIEVE_DISTRIBUTION_API);
        request = defaultParamNoShop(request);
        request.add("shop_id", shopId);
        request.add("distributor_id", distributorId);
        baseActivity.request(TAG_RELIEVE_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
//                UserInfoBean userInfoData = mGson.fromJson(response, UserInfoBean.class);
                callBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 邀请分销
     *
     * @param baseActivity
     * @param rate         佣金比例
     * @param discount     专享折扣
     * @param callBack
     */
    public void doInvitationDistribution(final BaseActivity baseActivity, float rate, float discount, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_INVITATION_DISTRIBUTION_API);
        request = defaultParam(request);
        request.add("commission_rate", rate + "");
        request.add("commission_discount", discount + "");
        baseActivity.request(TAG_INVITATION_DISTRIBUTION, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                InvitationBean invitationBean = mGson.fromJson(response, InvitationBean.class);

//                Toast.show(baseActivity, response);
//                UserInfoBean userInfoData = mGson.fromJson(response, UserInfoBean.class);
                callBack.onResult(what, invitationBean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }

    /**
     * 收入，支出，充值金额统计
     *
     * @param baseActivity
     * @param callBack
     */
    public void getAmountTotal(final BaseActivity baseActivity, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.MY_AMOUNT_TOTAL_API);
        request = defaultParamNoShop(request);
        baseActivity.request(TAG_AMOUNT_TOTAL, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                AmountTotalBean bean = mGson.fromJson(response, AmountTotalBean.class);
                callBack.onResult(what, bean);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(what, response);
            }
        }, true, true);
    }
}
