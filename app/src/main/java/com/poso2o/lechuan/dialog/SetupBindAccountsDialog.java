package com.poso2o.lechuan.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.image.SaveImageThread;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ImageLoaderUtils;
import com.poso2o.lechuan.util.ImageUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * 设置绑定收款银行帐号
 * <p>
 * Created by Jaydon on 2018/3/15.
 */
public class SetupBindAccountsDialog extends BaseDialog {
    //    private Context mContext;
//    private ShopData shopData;
    private Activity activity;
    private Bitmap mBitmap;
    private Callback callback;

    public SetupBindAccountsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
//        this.shopData = shopData;
    }

    public void show(Callback callback) {
        super.show();
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        return View.inflate(activity, R.layout.dialog_setup_bind_accounts, null);
    }

    @Override
    public void initView() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ImageView iv_qrcode = findView(R.id.iv_qrcode);
        mBitmap = getQRCode();
        if (mBitmap != null) {
            iv_qrcode.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        findView(R.id.bind_account_open_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查系统是否开启了地理位置权限;
                //注意：此时的Manifest的导入包路径import android.Manifest;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                if (mBitmap != null) {
                    saveQRCode(mBitmap);
                }
            }
        });
        findView(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.onCancel();
            }
        });
    }

    public interface Callback {
        void onResult();

        void onCancel();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            WaitDialog.dismissLoaddingDialog();
            if (msg.what == SaveImageThread.SUCCESSFULY) {
                Toast.show(context, "保存二维码成功！");
                openWeixinScanning();
            } else {
                Toast.show(context, "保存二维码失败！");
            }
        }
    };


    /**
     * 打开微信扫一扫
     */
    private void openWeixinScanning() {
//        try {
//            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
//            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
//            startActivityForResult(intent, 1);
//        } catch (Exception e) {
//            Toast.show(activity, "打开微信失败！");
//        }
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    private Bitmap getQRCode() {
        String url = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_BIND_ACCOUNT_QRCODE);
        Print.println("getQRCode()_url:" + url);
        if (!url.equals("")) {
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.company_logo);
            try {
                return ImageUtils.createQrCode(url, logo, BarcodeFormat.QR_CODE, 35);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.show(context, "生成二维码异常");
            }
        }
        return null;
    }

    private void saveQRCode(Bitmap bitmap) {
        WaitDialog.showLoaddingDialog(context);
        new SaveImageThread(context, bitmap, Constant.LECHUAN_ROOT_DIR, handler).start();
    }
}
