package com.poso2o.lechuan.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.poso2o.lechuan.tool.print.Print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 文件相关工具类
 */
public class FileUtils {
    private static Context mContext;
    private static String TAG = "FileUtils";

    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;

    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/AndroidImage";

    /**
     * 图文详情图片缓存
     */
    private final static String IMAGE_RICHTEXT = "/RichTextImage";

    private FileUtils() {
    }

    public static void init(Context context) {
        mContext = context;
        mDataRootPath = context.getCacheDir().getPath();
    }

    /**
     * 获取手机根目录，若有SD卡就是SD卡根目录，否则为手机缓存目录
     *
     * @return
     */
    public String getRootDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath : mDataRootPath;
    }

    /**
     * 获取储存Image的目录
     *
     * @return
     */
    public static String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME
                : mDataRootPath + FOLDER_NAME;
    }

    public static String getRichTextImageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + IMAGE_RICHTEXT
                : mDataRootPath + IMAGE_RICHTEXT;
    }

    /**
     * @param fileName
     * @param bitmap
     * @return 保存的图片的路径
     */
    public static String savaRichTextImage(String fileName, Bitmap bitmap) {
        fileName = fileName.replaceAll("[^\\w]", "");
        if (bitmap == null) {
            return null;
        }
        String result = null;
        String path = getRichTextImageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(path + File.separator + fileName + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            result = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "savaImage() has FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "savaImage() has IOException");
        }

        return result;
    }

    public static void deleteRichTextImage() {
        String path = getRichTextImageDirectory();
        File f = new File(path);
        deleteFile(f);
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public static String getFilePathFromUri(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = mContext.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getNewImagePath() {
        return getNewImagePath(UUIDUtils.generate());
    }

    public static String getNewImagePath(String name) {
        File PHOTO_DIR = new File(getStorageDirectory());
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
        }
        // 截取文件名，去掉后缀
        if (name.contains("http://")) {
            name = name.substring(name.lastIndexOf('/') + 1).replace(".jpg", "").replace(".jpeg", "").replace(".png", "");
        }
        File file = new File(PHOTO_DIR, name);// 给新照的照片文件命名
        return file.getAbsolutePath();
    }

    public static String getNewImagePath2(String path) {
        File f = new File(path);
        String fileName = f.getName();
        String n = fileName.substring(fileName.lastIndexOf("."));
        File PHOTO_DIR = new File(getStorageDirectory());
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
        }
        String name = UUIDUtils.generate() + n;
        // 截取文件名，去掉后缀
        if (name.contains("http://")) {
            name = name.substring(name.lastIndexOf('/') + 1).replace(".jpg", "").replace(".jpeg", "").replace(".png", "");
        }
        File file = new File(PHOTO_DIR, name);// 给新照的照片文件命名
        return file.getAbsolutePath();
    }

    /**
     * 获取拍照相片存储文件
     *
     * @param context
     * @return
     */
    public static File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp + ".jpg");
        }
        return file;
    }


    public static String getLogoSavePath(Context context) {
        String dir = context.getCacheDir().getAbsolutePath() + File.separator + "lechuan";
        File file = new File(dir, "logo.png");
        Print.println("getLogoSavePath:" + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath(Context context) {
        if (FileUtils.sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }
}
