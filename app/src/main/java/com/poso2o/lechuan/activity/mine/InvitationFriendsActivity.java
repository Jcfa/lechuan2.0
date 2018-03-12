package com.poso2o.lechuan.activity.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.manager.main.ShareManager;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ImageLoaderUtils;
import com.poso2o.lechuan.util.ImageUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 邀请
 * Created by Administrator on 2017-12-04.
 */

public class InvitationFriendsActivity extends BaseActivity implements View.OnClickListener {
    private ShareManager mShareManager;
    public static final String KEY_TYPE = "type";//是邀请好友，还是邀请分销
    public static final String KEY_URL = "url";//分享链接
    public static final String KEY_TITLE = "title";//分享标题
    public static final String KEY_DESCRIPTION = "description";//分享文字
    private int mType = 0;
    private String mUrl = "";
    private String mTitle = "";
    private String mDescription = "";
    public static final int VALUE_FRIENDS = 1;//好友邀请
    public static final int VALUE_DISTRIBUTION = 2;//分销邀请
    private ImageView ivQrcode;
    private Bitmap mLogo, mQrcode;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invitation_friends;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getInt(KEY_TYPE);
            mUrl = bundle.getString(KEY_URL);
            mTitle = bundle.getString(KEY_TITLE);
            mDescription = bundle.getString(KEY_DESCRIPTION);
        }
        LinearLayout layoutMember = findView(R.id.layout_member);
        if (mType == VALUE_DISTRIBUTION) {
            setTitle("分销邀请");
            layoutMember.setVisibility(View.VISIBLE);
            findView(R.id.layout_member).setOnClickListener(this);
        } else {
            setTitle("好友邀请");
            layoutMember.setVisibility(View.GONE);
        }
        ivQrcode = findView(R.id.iv_qrcode);
        findView(R.id.layout_wechat).setOnClickListener(this);
        findView(R.id.layout_wxcircle).setOnClickListener(this);
        findView(R.id.layout_qq).setOnClickListener(this);
        findView(R.id.layout_qzone).setOnClickListener(this);
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.class);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(MainActivity.class);
//            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initData() {
        mShareManager = ShareManager.getShareManager();
        TextView tvMessage = findView(R.id.tv_message);
        tvMessage.setText(mType == VALUE_DISTRIBUTION ? "邀请好友做分销" : "邀请好友抢红包");

    }

    @Override
    public void onResume() {
        super.onResume();
        String logo = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO);
        if (!TextUtil.isEmpty(logo)) {
            WaitDialog.showLoaddingDialog(activity, "正在生成二维码...");
            new Thread(new LoadImageRunable(logo)).start();
        } else {
            try {
                mLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                mQrcode = ImageUtils.createQrCode(mUrl, mLogo, BarcodeFormat.QR_CODE, 35);
                ivQrcode.setImageBitmap(mQrcode);
                mLogo.recycle();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            WaitDialog.dismissLoaddingDialog();
            if (mQrcode == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                try {
                    mQrcode = ImageUtils.createQrCode(mUrl, bitmap, BarcodeFormat.QR_CODE, 35);
                    bitmap.recycle();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            ivQrcode.setImageBitmap(mQrcode);
        }
    };

    private class LoadImageRunable implements Runnable {
        private String logo;

        public LoadImageRunable(String logo) {
            this.logo = logo;
        }

        @Override
        public void run() {
            try {
                mLogo = BitmapFactory.decodeStream(getInputStream(logo));
//                ImageLoaderUtils.saveImage(mLogo, FileUtils.getLogoSavePath(activity));
                mQrcode = ImageUtils.createQrCode(mUrl, mLogo, BarcodeFormat.QR_CODE, 35);
                mHandler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onStop() {
        super.onStop();
        ivQrcode.setImageBitmap(null);
        if (mQrcode != null && !mQrcode.isRecycled())
            mQrcode.recycle();
    }

    private InputStream getInputStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn.getInputStream();
    }

    @Override
    public void onClick(View view) {
//        String url = "http://sj.qq.com/myapp/detail.htm?apkName=com.android.helper";
        if (TextUtil.isEmpty(mUrl)) {
            Toast.show(activity, "分享链接错误!");
            return;
        }
        switch (view.getId()) {
            case R.id.layout_wechat://微信好友
                toShare(false);
                break;
            case R.id.layout_wxcircle://微信朋友圈
                toShare(true);
                break;
            case R.id.layout_qq://QQ好以
                mShareManager.qqShareImageAndText(activity, mTitle, mDescription, mUrl, SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO));
                break;
            case R.id.layout_qzone://QQ空间
                mShareManager.qzoneShareImageAndText(activity, mTitle, mDescription, mUrl, SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO));
                break;
            case R.id.layout_member:
                startActivity(InvitationFansActivity.class);
                break;
        }
    }

//    private class DownloadLogo extends Thread {
//        private boolean shareTimeline = false;
//
//        public DownloadLogo(boolean shareTimeline) {
//            this.shareTimeline = shareTimeline;
//            WaitDialog.showLoaddingDialog(activity, "下载图片...");
//        }
//
//        @Override
//        public void run() {
//            String logo = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO);
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeStream(getInputStream(logo));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            final Bitmap logoBitmap = bitmap;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    WaitDialog.dismissLoaddingDialog();
//                    toShare(logoBitmap, shareTimeline);
//                }
//            });
//        }
//    }

    private void toShare(boolean shareTimeline) {
        if (mLogo != null && !mLogo.isRecycled()) {
            mShareManager.weixinShareWebImage(activity, mUrl, mTitle, mDescription, mLogo, shareTimeline);
        } else {
            mShareManager.weixinShareWebImage(activity, mUrl, mTitle, mDescription, R.mipmap.ic_launcher, shareTimeline);
        }
    }
}
