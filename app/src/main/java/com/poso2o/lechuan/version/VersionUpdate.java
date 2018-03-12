package com.poso2o.lechuan.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.version.VersionBean;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.DownloadProgressDialog;
import com.poso2o.lechuan.dialog.VersionUpdateDialog;
import com.poso2o.lechuan.http.CallServer;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.Request;

import java.io.File;

/**
 * 版本更新
 * Created by Administrator on 2017-12-21.
 */

public class VersionUpdate extends BaseManager {
    private BaseActivity baseActivity;
    /**
     * 下载请求.
     */
    private DownloadRequest mDownloadRequest;
    private DownloadProgressDialog downloadProgressDialog;

    public VersionUpdate(BaseActivity context) {
        baseActivity = context;
    }

    /**
     * 检测新版本
     */
    public void checkNewVersion() {
        Request<String> request = getStringRequest(HttpAPI.VERSION_API, RequestMethod.GET);
        baseActivity.request(1, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                VersionBean versionBean = gson.fromJson(response, VersionBean.class);
                if (versionBean == null) {
                    return;
                }
                int currentVersion = getCurrentVersion();
                if (versionBean.getNumber() > currentVersion) {//高于当前版本
                    showVersionUpdateDialog(versionBean, currentVersion);
                }
            }

            @Override
            public void onFailed(int what, String response) {

            }
        }, true, true);
    }

    private int getCurrentVersion() {
        PackageManager packageManager = baseActivity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(baseActivity.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return -1;
    }

    /**
     * 判断本地下载的是否是最新版本
     */
    private boolean isExistNewestLocaFile(File file, int netVersion, int currentVersion) {
        if (file == null || !file.exists()) {
            return false;
        }
        PackageManager packageManager = baseActivity.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(),
                PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            return false;
        }
        if (packageInfo.versionCode >= netVersion && packageInfo.versionCode > currentVersion) {
            return true;
        }
        return false;
    }

    /**
     * 显示版本更新弹窗
     */
    public void showVersionUpdateDialog(final VersionBean versionBean, int currentVersion) {
        boolean localFile = false;
        final File apkFile = new File(AppConfig.getInstance().APK_FILE_PATH);
        if (apkFile.exists()) {
            localFile = isExistNewestLocaFile(apkFile, versionBean.getNumber(), currentVersion);
        }
        final boolean local = localFile;
        VersionUpdateDialog updateDialog = new VersionUpdateDialog(baseActivity, versionBean, localFile, new VersionUpdateDialog.SettingCallBack() {
            @Override
            public void confirm() {
                if (local) {
                    AppUtil.install(baseActivity, AppConfig.getInstance().APK_FILE_PATH);
                } else {
                    download(versionBean.downloadurl);
                    showDownloadProgress();
                }
            }
        });
        updateDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        updateDialog.show();
    }

    /**
     * 显示下载进度dialog
     */
    public void showDownloadProgress() {
        downloadProgressDialog = new DownloadProgressDialog(baseActivity);
        downloadProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        downloadProgressDialog.show();
    }

    /**
     * 开始下载。
     */
    private void download(String downloadUrl) {
        // 开始下载了，但是任务没有完成，代表正在下载，那么暂停下载。
        if (mDownloadRequest != null && mDownloadRequest.isStarted() && !mDownloadRequest.isFinished()) {
            // 暂停下载。
            mDownloadRequest.cancel();
        } else if (mDownloadRequest == null || mDownloadRequest.isFinished()) {// 没有开始或者下载完成了，就重新下载。

            mDownloadRequest = new DownloadRequest(downloadUrl, RequestMethod.GET,
                    AppConfig.getInstance().APP_PATH_ROOT,
                    true, true);
            // what 区分下载。
            // downloadRequest 下载请求对象。
            // downloadListener 下载监听。
            CallServer.getInstance().download(0, mDownloadRequest, downloadListener);

            // 添加到队列，在没响应的时候让按钮不可用。
//            mBtnStart.setEnabled(false);
        }
    }

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadError(int what, Exception exception) {

        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
            if (downloadProgressDialog != null) {
                downloadProgressDialog.updateTotal(allCount);
            }
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            Print.println("onProgress_progress:" + progress + ",fileCount:" + fileCount);
            if (downloadProgressDialog != null) {
                downloadProgressDialog.updateProgress(progress);
            }
        }

        @Override
        public void onFinish(int what, String filePath) {
            Toast.show(baseActivity, "下载完成");
            downloadProgressDialog.dismiss();
            Print.println("APK_FILE_NAME=" + AppConfig.getInstance().APK_FILE_PATH);
            AppUtil.install(baseActivity, AppConfig.getInstance().APK_FILE_PATH);
        }

        @Override
        public void onCancel(int what) {
            Toast.show(baseActivity, "取消下载");
        }
    };

}
