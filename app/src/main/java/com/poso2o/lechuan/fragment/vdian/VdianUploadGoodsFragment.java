package com.poso2o.lechuan.fragment.vdian;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.dialog.DownloadProgressDialog;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

/**
 * 微店商品上传界面
 * <p>
 * Created by Jaydon on 2018/3/16.
 */
public class VdianUploadGoodsFragment extends BaseFragment {

    /**
     * 下载请求.
     */
    private DownloadRequest mDownloadRequest;
    private DownloadProgressDialog downloadProgressDialog;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vdian_upload_goods, container, false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        findView(R.id.upload_goods_download).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 弹出一个选择浏览器的框，选择浏览器再进入
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse("http://sj.qq.com/myapp/detail.htm?apkName=com.android.helper");
                intent.setData(content_url);
                startActivity(Intent.createChooser(intent, "随便写点什么"));
//                String downloadUrl = "http://imtt.dd.qq.com/16891/8680135F6B7C0516E8E31B0877F0C2B5.apk?fsname=com.android.helper_1.0.2.2_1022.apk&csr=1bbd";
//                mDownloadRequest = new DownloadRequest(downloadUrl, RequestMethod.GET,
//                        AppConfig.getInstance().APP_PATH_ROOT,
//                        true, true);
//                CallServer.getInstance().download(0, mDownloadRequest, downloadListener);
//
//                showDownloadProgress();
            }
        });
    }

    /**
     * 显示下载进度dialog
     */
    public void showDownloadProgress() {
        downloadProgressDialog = new DownloadProgressDialog(context);
        downloadProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        downloadProgressDialog.show();
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
            Toast.show(context, "下载完成");
            downloadProgressDialog.dismiss();
            Print.println("APK_FILE_NAME=" + AppConfig.getInstance().APK_FILE_PATH);
            AppUtil.install(context, AppConfig.getInstance().APK_FILE_PATH);
        }

        @Override
        public void onCancel(int what) {
            Toast.show(context, "取消下载");
        }
    };
}
