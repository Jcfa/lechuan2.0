package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.order.GoodsAppraiseQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.bean.poster.ShopDetailsCommentsQueryDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 店铺评论
 * Created by Luo on 2017-12-01.
 */

public class ShopCommentDataManager<T> extends BaseManager {
    public static ShopCommentDataManager mShopCommentDataManager;
    /**
     * 获取店铺评论列表
     */
    private final int TAG_POSTER_ID = 1;

    public static ShopCommentDataManager getShopCommentDataManager() {
        if (mShopCommentDataManager == null) {
            mShopCommentDataManager = new ShopCommentDataManager();
        }
        return mShopCommentDataManager;
    }

    /**
     * 获取店铺评论列表
     *
     * @param baseActivity
     * @param open_uid       查个公开账号ID
     * @param currPage       广告ID（必填）
     * @param callBack       回调
     *                       uid=13423678930&shop_id=13423678930&token=e41a8183db9a8b3b650a11846e7b5fc6&currPage=1&open_uid=13423678930
     */
    public void loadShopCommentData(final BaseActivity baseActivity, final String open_uid, final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_SHOP_COMMENT_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("currPage", currPage);
        request.add("open_uid", open_uid);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                GoodsAppraiseQueryDTO posterQueryDTO = gson.fromJson(response, GoodsAppraiseQueryDTO.class);
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
     * 获取店铺详情评论列表
     *
     * @param baseActivity
     * @param appraise_id       购买评论ID
     * @param callBack          回调
     */
    public void loadShopDetailsCommentData(final BaseActivity baseActivity, final String appraise_id, final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_SHOP_DETAILS_COMMENT_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("currPage", currPage);
        request.add("appraise_id", appraise_id);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                ShopDetailsCommentsQueryDTO posterQueryDTO = gson.fromJson(response, ShopDetailsCommentsQueryDTO.class);
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
     * 添加店铺评论
     *
     * @param baseActivity
     * @param appraise_id    购买评论ID（必填）
     * @param comments       评论
     * @param callBack       回调
     */
    public void addShopDetailsComment(final BaseActivity baseActivity, final String appraise_id, final String comments, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.POSTER_ADD_SHOP_DETAILS_COMMENTS_API);
        request = defaultParam(request);
        //request.add("uid", "13423678930");
        //request.add("token", "e41a8183db9a8b3b650a11846e7b5fc6");
        request.add("appraise_id", appraise_id);
        request.add("comments", comments);
        Print.println("URL:" + request.getCacheKey().toString());
        baseActivity.request(TAG_POSTER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Print.println("response:" + response.get());
                Gson gson = new Gson();
                PosterCommentsDTO posterQueryDTO = gson.fromJson(response, PosterCommentsDTO.class);
                callBack.onResult(TAG_POSTER_ID, posterQueryDTO);
            }
            @Override
            public void onFailed(int what, String response) {
                //Toast.show(baseActivity, response);
                callBack.onFailed(TAG_POSTER_ID, response);
            }
        }, true, true);
    }



}
