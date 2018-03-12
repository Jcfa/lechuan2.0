package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.view.rich.RichTextEditor;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018-02-05.
 */

public class TemplateFragment extends BaseFragment {
    private WebView mWebView;
    private static final int REQUEST_CAMERA = 8;// 拍照
    private static final int REQUEST_PHOTO = 9;// 相册
    public static final int INSERT_PIC = 1;//图入图片
    public static final int REPLACE_PIC = 2;//替换图片

    /**
     * 事件处理
     */
    private Handler mHandler = new Handler();
    /**
     * 是否上传图片结束
     */
    private boolean isUploadImageFinish = true;
    private int mClickPosition = 0;//1=插入图片，2=替换图片

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template_edit, container, false);
    }

    @Override
    public void initView() {
        mWebView = findView(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        //JS交互权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.loadUrl("http://wechat.poso2o.com/editor/");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.show(context, R.string.toast_sd_error);
                        return;
                    }
                    if (mCameraImageFile != null) {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(mCameraImageFile.getAbsolutePath());
                        compressImages(paths);
                        mCameraImageFile = null;
                    }
                    break;

                case REQUEST_PHOTO:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        compressImages(selectedImages);
                    }
                    break;
            }
        }
    }


    /**
     * 加载并压缩图片
     */
    private void compressImages(final ArrayList<String> selectedImages) {
        showLoading(R.string.loading_images);
        final ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            if (selectedImages.get(i).toLowerCase().contains(".gif")) {
                paths.add(selectedImages.get(i)); //如果是gif图就直接用原图
            } else {
                paths.add(FileUtils.getNewImagePath2(selectedImages.get(i)));
            }
        }
        showLoading("正在压缩图片(" + "0/" + paths.size() + ")");
        CompressImageAsyncTask task = new CompressImageAsyncTask(selectedImages, paths, new CompressImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(Integer result) {
                if (result == null) {
                    Toast.show(context, "压缩图片失败");
                    dismissLoading();
                } else if (result == -1) {
                    index = 0;
                    isUploadImageFinish = false;
                    uploadImages(paths);
                } else if (result > 0) {
                    setLoadingMessage("正在压缩图片(" + result.toString() + "/" + paths.size() + ")");
                }
            }
        });
        task.execute();
    }

//    /**
//     * 批量添加图片的回调函数
//     */
//    private RichTextEditor.Callback mCallback = new RichTextEditor.Callback() {
//
//        @Override
//        public void complete() {
//            // 500毫秒后滚动到底部
//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    int height = image_text_rich_text.getHeight();
//                    if (height < 0) {
//                        height = 0;
//                    }
//                    image_text_content.scrollTo(0, height);
//                }
//            }, 500);
//        }
//    };

    /**
     * 记录上传图片下标
     */
    private int index;

    /**
     * 多张图片上传
     *
     * @param paths
     */
    private void uploadImages(final ArrayList<String> paths) {
        setLoadingMessage("正在上传图片(" + (index + 1) + "/" + paths.size() + ")");
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(context, paths.get(index), new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                if (mClickPosition == INSERT_PIC) {
                    // 添加图片
//                    image_text_rich_text.insertImage(paths.get(index), url, mCallback);
                    insertPic(url);
                } else if (mClickPosition == REPLACE_PIC) {
                    // 修改图片
                    replacePic(url);
                    dismissLoading();
                }
                index++;
                if (index < paths.size()) {
                    uploadImages(paths);
                } else {
                    isUploadImageFinish = true;
                    Toast.show(context, "上传图片成功");
                    dismissLoading();
                }
            }

            @Override
            public void fail() {
                isUploadImageFinish = true;
                Toast.show(context, "上传图片失败");
                dismissLoading();
            }
        });
        asyncTask.execute();
    }

    /**
     * 切换模版
     *
     * @param templateBean 模版
     */
    public void changeTemplate(TemplateBean templateBean) {
        clearHtml();//清空模版
        insertHtml(templateBean.content);//插入模版内容
    }

    /**
     * 调用JS显示h5的模版dialog
     */
    public void showTemplateDialog() {
        mWebView.loadUrl("javascript:showAdTemplateDialog()");
    }

    /**
     * 调用JS清空模版内容
     */
    private void clearHtml() {
        mWebView.loadUrl("javascript:emptyHtml()");
    }

    /**
     * 调用JS插入模版html代码
     */
    private void insertHtml(String html) {
        mWebView.loadUrl("javascript:appendHTML('" + html + "')");
    }

    /**
     * 调用JS插入图片
     */
    private void insertPic(String pic) {
        mWebView.loadUrl("javascript:appendImgUrl('" + pic + "')");
    }

    /**
     * 调用JS替换图片
     */
    private void replacePic(String pic) {
        mWebView.loadUrl("javascript:replaceImgUrl('" + pic + "')");
    }

    /**
     * 照相机拍照得到的图片
     */
    private static File mCameraImageFile;

    /**
     * 给JS调用，打开系统相机
     *
     * @param type
     */
    public void openCamera(int type) {
        mClickPosition=type;
        mCameraImageFile = AppUtil.openCamera(this, REQUEST_CAMERA);
    }

    /**
     * 给JS调用，打开系统相册
     */
    public void openPhoto(int type) {
        mClickPosition=type;
        AppUtil.openPhoto(context, this, REQUEST_PHOTO);
    }
}
