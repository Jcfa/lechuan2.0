package com.poso2o.lechuan.manager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.UploadImageBean;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;

import java.io.File;

/**
 * Created by mr zhang on 2017/12/6.
 */

public class UpLoadImageManager extends BaseManager {

    //上传图片通用路径
    public static final String COMMEN_IMAGE_PATH = HttpAPI.SERVER_MAIN_API + "ImgManage.htm?Act=uploadShopLogo";
    public static final int COMMEN_IMAGE = 0000;

    private static volatile UpLoadImageManager upLoadImageManager;
    public static UpLoadImageManager getUpLoadImageManager(){
        if (upLoadImageManager == null){
            synchronized (UpLoadImageManager.class){
                if (upLoadImageManager == null){
                    upLoadImageManager = new UpLoadImageManager();
                }
            }
        }
        return upLoadImageManager;
    }

    /**
     * 上传图片
     * @param baseActivity
     * @param image       图片路径
     * @param iRequestCallBack
     */
    public void uploadImage(final BaseActivity baseActivity, String image, final IRequestCallBack iRequestCallBack){

        Request request = getStringRequest(COMMEN_IMAGE_PATH + "&token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN) + "&uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        File file = new File(image);
        FileBinary binary = new FileBinary(file);
        request.add("logo",binary);

        baseActivity.request(COMMEN_IMAGE, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Print.println("图片路径：" + response);
                UploadImageBean bean = new Gson().fromJson(response,UploadImageBean.class);
                iRequestCallBack.onResult(COMMEN_IMAGE,bean);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(COMMEN_IMAGE,response);
            }
        },true,true);
    }
}
