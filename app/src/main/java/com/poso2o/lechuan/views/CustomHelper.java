package com.poso2o.lechuan.views;

import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.poso2o.lechuan.R;

import java.io.File;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me5
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class CustomHelper {
    private int mMaxSize = 300000;//图片最大不超过300kb
    private boolean mShowProgressBar = true;//是否显示压缩进度条
    private boolean mRawFile = false;//压缩后是否保存原图
    private boolean mCompressTool = true;//是否使用自带压缩，否则Luban压缩
    private boolean mCropTool = true;//是否使用系统自带剪切工具
    private boolean mCrop = true;//是否要剪切
    private int mCropWidth = 100, mCropHeight = 100;
    private int mCompressWidth = 800, mCompressHeight = 800;//px
    private boolean mAspect = true;//宽高等比剪切，正方形
    private boolean mCorrectTool = true;//是否纠正照片的旋转角度
    private boolean mPickTool = false;//是否使用takephoto自带相册

    public static CustomHelper of() {
        return new CustomHelper();
    }

    private CustomHelper() {
    }

    /**
     * 设置压缩大小
     *
     * @param maxKb
     * @return
     */
    public CustomHelper setmMaxSize(int maxKb) {
        mMaxSize = maxKb * 1000;
        return this;
    }

    /**
     * 设置剪切大小
     *
     * @param
     * @return
     */
    public CustomHelper setmCropSize(int width, int height) {
        mCropWidth = width;
        mCropHeight = height;
        return this;
    }

    /**
     * 设置压缩的分辨率大小
     *
     * @param width
     * @param height
     * @return
     */
    public CustomHelper setmCompressSize(int width, int height) {
        mCompressWidth = width;
        mCompressHeight = height;
        return this;
    }

    /**
     * 相册选择
     *
     * @param takePhoto
     * @param limit     最大选择数量
     */
    public void pickBySelect(TakePhoto takePhoto, int limit) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        if (limit > 1) {
            if (mCrop) {
                takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
            } else {
                takePhoto.onPickMultiple(limit);
            }
        } else {
            if (true) {//是否从文件选择
                if (mCrop) {
                    takePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromDocuments();
                }
            } else {
                if (mCrop) {
                    takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromGallery();
                }
            }
        }
    }

    /**
     * 拍照
     */
    public void pickByTake(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
//        takePhoto.onPickFromCapture(imageUri);
    }

    /**
     * 使用相册设置
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        if (mPickTool) {//是否使用系统自带相册
            builder.setWithOwnGallery(true);
        }
        if (mCorrectTool) {
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 压缩
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config;
        if (mCompressTool) {//自带压缩
            config = new CompressConfig.Builder()
                    .setMaxSize(mMaxSize)
                    .setMaxPixel(mCompressWidth >= mCompressHeight ? mCompressWidth : mCompressHeight)
                    .enableReserveRaw(mRawFile)
                    .create();
        } else {//Luban压缩
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxHeight(mCompressHeight)
                    .setMaxWidth(mCompressWidth)
                    .setMaxSize(mMaxSize)
                    .create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(mRawFile);
        }
        takePhoto.onEnableCompress(config, mShowProgressBar);


    }

    //剪切设置
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        if (mAspect) {//剪切尺寸或比例，rbAspect宽/高
            builder.setAspectX(mCropWidth).setAspectY(mCropHeight);
        } else {
            builder.setOutputX(mCropWidth).setOutputY(mCropHeight);
        }
        builder.setWithOwnCrop(mCropTool);
        return builder.create();
    }

}
