package com.poso2o.lechuan.manager.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseApplication;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.BitmapUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * 分享管理类
 * Created by Administrator on 2017-11-28.
 */

public class ShareManager {
    public static ShareManager shareManager;

    private ShareManager() {
    }

    public static ShareManager getShareManager() {
        if (shareManager == null) {
            shareManager = new ShareManager();
        }
        return shareManager;
    }

    /**
     * 微信分享文字
     *
     * @param content
     * @param shareTimeline 是否分享到朋友圈
     */
    public void weixinShareText(String content, boolean shareTimeline) {
        WXTextObject wxTextObject = new WXTextObject();
        wxTextObject.text = content;
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        wxMediaMessage.mediaObject = wxTextObject;
        wxMediaMessage.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = wxMediaMessage;
        req.scene = shareTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        BaseApplication.getIWXAPI().sendReq(req);
    }

    /**
     * 微信分享图片
     *
     * @param
     * @param imgId         图片ID
     * @param shareTimeline
     */
    public void weixinShareImage(Activity activity, int imgId, boolean shareTimeline) {
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), imgId);
        weixinShareImage(bitmap, shareTimeline);
    }

    /**
     * 微信分享图片
     *
     * @param path          图片路径
     * @param shareTimeline
     */
    public void weixinShareImage(String path, boolean shareTimeline) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        weixinShareImage(bitmap, shareTimeline);
    }

    private void weixinShareImage(Bitmap bitmap, boolean shareTimeline) {
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        wxMediaMessage.mediaObject = wxImageObject;
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        wxMediaMessage.thumbData = BitmapUtil.bitmapToByteArray(thumbBitmap, 100);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = wxMediaMessage;
        req.scene = shareTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        BaseApplication.getIWXAPI().sendReq(req);
    }

    /**
     * 微信分享网页
     *
     * @param url         网页URL
     * @param title       标题
     * @param description 描述
     * @param imgId       icon
     */
    public void weixinShareWebImage(Activity activity, String url, String title, String description, int imgId, boolean shareTimeline) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), imgId);
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        wxMediaMessage.thumbData = BitmapUtil.bitmapToByteArray(thumbBitmap, 100);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = wxMediaMessage;
        req.scene = shareTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        BaseApplication.getIWXAPI().sendReq(req);
    }

    /**
     * 微信网页分享+本地图片
     *
     * @param activity
     * @param url           分享网页地址
     * @param title
     * @param description
     * @param imgPath       本地图片地址
     * @param shareTimeline 是否分享到朋友圈
     */
    public void weixinShareWebImage(Activity activity, String url, String title, String description, String imgPath, boolean shareTimeline) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        byte[] thumbData = BitmapUtil.bitmapToByteArray(thumbBitmap, 100);
        weixinShareWebImageForByte(activity, url, title, description, thumbData, shareTimeline);
    }

    public void weixinShareWebImage(Activity activity, String url, String title, String description, Bitmap bitmap, boolean shareTimeline) {
        byte[] thumbData = null;
        if (bitmap != null) {
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            bitmap.recycle();
            thumbData = BitmapUtil.bitmapToByteArray(thumbBitmap, 100);
        }
        weixinShareWebImageForByte(activity, url, title, description, thumbData, shareTimeline);
    }

    /**
     * 微信网页分享+本地图片
     *
     * @param activity
     * @param url           分享网页地址
     * @param title
     * @param description
     * @param bitmap        本地图片
     * @param shareTimeline 是否分享到朋友圈
     */
    public void weixinShareWebImageForByte(Activity activity, String url, String title, String description, byte[] bitmap, boolean shareTimeline) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;
        wxMediaMessage.thumbData = bitmap;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = wxMediaMessage;
        req.scene = shareTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        BaseApplication.getIWXAPI().sendReq(req);
    }

    /**
     * QQ分享图片
     */
    public void qqShareImage(Activity context, String path) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getResources().getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        BaseApplication.getmTencent().shareToQQ(context, params, new BaseUiListener());
    }

    /**
     * QQ分享图文
     *
     * @param context
     * @param title   标题
     * @param summary 摘要
     * @param webUrl  网页URL
     * @param imgUrl  图片URL
     */
    public void qqShareImageAndText(Activity context, String title, String summary, String webUrl, String imgUrl) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getResources().getString(R.string.app_name));
        BaseApplication.getmTencent().shareToQQ(context, params, new BaseUiListener());
    }

    /**
     * 分享到QQ空间
     *
     * @param context
     * @param title
     * @param summary
     * @param webUrl
     * @param imgUrl
     */
    public void qzoneShareImageAndText(Activity context, String title, String summary, String webUrl, String imgUrl) {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add(imgUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgList);
//        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, context.getResources().getString(R.string.app_name));
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "图片链接ArrayList");
        BaseApplication.getmTencent().shareToQzone(context, params, new BaseUiListener());
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
