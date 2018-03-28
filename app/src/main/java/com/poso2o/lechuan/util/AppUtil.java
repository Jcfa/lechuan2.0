package com.poso2o.lechuan.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.login.LoginActivity;
import com.poso2o.lechuan.activity.login.StartActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.manager.main.ActivityManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;

/**
 * Created by Administrator on 2017-12-18.
 */

public class AppUtil {
    /**
     * 退出APP
     *
     * @param activity
     * @param login    是否退出到登录页面
     */
    public static void exitApp(Context activity, boolean login) {
        ActivityManager.getActivityManager().finishAll();
        if (login) {
            MiPushClient.unsetAlias(activity, SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID), null);
//            MiPushClient.pausePush(activity,SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
            SharedPreferencesUtils.logout();//清除数据
//            Intent intent = new Intent(activity, StartActivity.class);
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra(SharedPreferencesUtils.TAG_EXIT, true);
            activity.startActivity(intent);
        }
    }

    public static void install(Context context, String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(
//                    context
//                    , "com.poso2o.lechuan.fileprovider"
//                    , apkFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            Log.w(TAG, "正常进行安装");
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//        }
        context.startActivity(intent);
        //关闭旧版本的应用程序的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 拍照
     */
    public static File openCamera(BaseFragment fragment, int code) {
        File cameraImageFile = null;
        try {
            File PHOTO_DIR = new File(FileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            cameraImageFile = new File(PHOTO_DIR, UUIDUtils.generate() + ".jpg");// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(cameraImageFile);
            fragment.startActivityForResult(intent, code);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
        return cameraImageFile;
    }

    private static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 进入相册
     */
    public static void openPhoto(Context context,BaseFragment fragment,int code) {
        Intent intent = new Intent(context, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, 9);
        fragment.startActivityForResult(intent, code);
    }

    public static void openPhoto(Context context,Activity activity,int code) {
        Intent intent = new Intent(context, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, 9);
        activity.startActivityForResult(intent, code);
    }
}
