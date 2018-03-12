/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poso2o.lechuan.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * 加载图片工具类
 *
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
public class ImageLoaderUtils {


    private static DisplayImageOptions mCommonOptions;

    private static DisplayImageOptions mIconOptions;

    public static void init(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .denyCacheImageMultipleSizesInMemory()// 拒绝缓存多种尺寸的图片在存储器
                .diskCacheFileCount(1000)// 设置缓存到sd卡的文件最大数量
                .memoryCacheSize(1024 * 1024 * 8).tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
                .diskCache(new UnlimitedDiskCache(new File(getImgDir()), null, new FileNameGenerator() {
                    @Override
                    public String generate(String url) {
                        return url.substring(url.lastIndexOf("/") + 1); // 生成图片的名称
                    }
                })) // 自定义缓存路径,图片缓存到sd卡
                .build();

        ImageLoader.getInstance().init(config);

        mCommonOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(0))// 设置为圆角
                .showImageOnLoading(R.mipmap.ic_launcher) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)// 设置当图片URI为空或空字符串时显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)// 设置当有些错误发生在图片解析或者下载时显示的图像
//                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY) // 图像缩小尺寸刚好到目标大小
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build(); // 开始构建

        mIconOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(0))// 设置为圆角
                .showImageOnLoading(R.mipmap.ic_launcher) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)// 设置当图片URI为空或空字符串时显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)// 设置当有些错误发生在图片解析或者下载时显示的图像
//                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.NONE) // 图像缩小尺寸刚好到目标大小
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build(); // 开始构建
    }

    /**
     * 获取图片目录路径
     *
     * @author Zheng Jie Dong
     * @date 2015-4-27
     */
    public static String getImgDir() {
        return getImageDir();
    }

    /**
     * 图片Dir
     */
    public static String getImageDir() {
        String imageDir = FileUtils.getStorageDirectory();
        mkdirs(imageDir);
        return imageDir;
    }

    /**
     * 创建目录
     */
    public static void mkdirs(String path) {
        File fileDir = new File(path);
        // 目录不存在,创建目录
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    /**
     * 压缩图片
     */
    public static Bitmap compressBySize(Bitmap image, int targetWidth, int targetHeight) {
        long start = System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int intoptions = 100;
//        while (baos.toByteArray().length / 1024 > 1024) {// 循环判断如果压缩后图片是否大于1M,大于继续压缩
//            baos.reset();// 重置baos即清空baos
//            intoptions -= 20;// 每次都减少20
//            image.compress(Bitmap.CompressFormat.JPEG, intoptions, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm, null, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 设置缩放比例,1表示不缩放
        opts.inSampleSize = 1;
        if (imgHeight > targetHeight || imgWidth > targetWidth) {
            // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
            final int heightRatio = Math.round(imgHeight / (float) targetHeight);
            final int widthRatio = Math.round(imgWidth / (float) targetWidth);
            opts.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());

        Bitmap bm = BitmapFactory.decodeStream(isBm, null, opts);
        // 压缩好比例大小后再进行质量压缩
        return bm;
    }

    /**
     * 压缩图片
     */
    public static Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 设置缩放比例,1表示不缩放
        opts.inSampleSize = 1;
        if (imgHeight > targetHeight || imgWidth > targetWidth) {
            // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
            final int heightRatio = Math.round(imgHeight / (float) targetHeight);
            final int widthRatio = Math.round(imgWidth / (float) targetWidth);
            opts.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(pathName, opts);
        return bm;
    }

    /**
     * 存储进SD卡
     */
    public static String saveImageNoRecycle(Bitmap bm, String path) {
        try {
            File myCaptureFile = new File(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            // 100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            // 回收
            bos.flush();
            bos.close();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储进SD卡
     */
    public static String saveImage(Bitmap bm, String path) {
        try {
            return saveFile(bm, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    /**
//     * 存储进SD卡
//     */
//    public static String saveFile(Bitmap bm, String path) throws Exception {
//        int quality = 100;
//        LogUtils.i("图片大小 = " + (bm.getByteCount() / 1024));
//        if (bm.getByteCount() / 1024 > 8500) {
//            quality = 70;
//        }
//        File myCaptureFile = new File(path);
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//        // 100表示不进行压缩，70表示压缩率为30%
//        bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
//        // 回收
//        bm.recycle();
//        bos.flush();
//        bos.close();
//        return path;
//    }

    /**
     * 图片压缩至100k以内
     * @param bm
     * @param path
     * @return
     * @throws Exception
     */
    public static String saveFile(Bitmap bm, String path) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        File myCaptureFile = new File(path);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bos.write(baos.toByteArray());
        bm.recycle();
        bos.flush();
        bos.close();
        return path;
    }

    /**
     * 保存图片并返回图片地址
     */
    public static String saveImageBackUrl(Bitmap bm, String path) {
        Bitmap cBm = compressBySize(bm, 600, 720);
        return saveImage(cBm, path);
    }

    /**
     * 保存图片并返回图片地址
     */
    public static String saveImageBackUrl(String sourcePath, String targetPath) {
        Bitmap cBm = compressBySize(sourcePath, 600, 720);
        return saveImage(cBm, targetPath);
    }

    /**
     * 清楚磁盘缓存
     */
    public static void clearDiskCache() {
//        ImageLoader.getInstance().clearDiskCache();
    }


    /**
     * 存储进SD卡
     */
    public static String saveImageNotRecycle(Bitmap bm, String path) {
        try {
            File myCaptureFile = new File(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            // 100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            // 回收
//        bm.recycle();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}