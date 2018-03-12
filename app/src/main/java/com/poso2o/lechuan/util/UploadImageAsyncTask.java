package com.poso2o.lechuan.util;

import android.content.Context;
import android.os.AsyncTask;

import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.tool.print.Print;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

@SuppressWarnings("deprecation")
public class UploadImageAsyncTask extends AsyncTask<String, Void, String> {

    /**
     * 发送请求用的URL
     */
    private static final String GOODS_URL = HttpAPI.SERVER_MAIN_API + "ImgManage.htm?Act=uploadGoodsImage";// 商品
    private static final String ARTICLE_URL = HttpAPI.SERVER_MAIN_API + "ImgManage.htm?Act=uploadAriclesImage";// 资讯

    /**
     * 环境变量
     */
    private Context context;

    /**
     * 图片路径
     */
    private String mPath;

    /**
     * 回调函数
     */
    private AsyncTaskCallback mAsyncTaskCallback;

    public UploadImageAsyncTask(Context context, String path, AsyncTaskCallback callback) {
        this.context = context;
        mPath = path;
        mAsyncTaskCallback = callback;
    }

    @Override
    @SuppressWarnings("resource")
    protected String doInBackground(String... strs) {
        if (TextUtil.isEmpty(mPath)) {
            return null;
        }
        String url;
        if (context.getClass().getSimpleName().contains("Goods")) {
            url = GOODS_URL;
        } else {
            url = ARTICLE_URL;
        }
        url += "&uid=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        url += "&token=" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN);
        Print.println("本地path=" + mPath);
        Print.println("url=" + url);
        try {
            // 创建链接对象
            HttpClient client = new DefaultHttpClient();
            // 设置链接属性
            client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            // 创建请求
            HttpPost post = new HttpPost(url);
            // 创建文件对象
            File file = new File(mPath);
            // 创建文件流
            MultipartEntity entity = new MultipartEntity();
            FileBody fileBody = new FileBody(file);
            entity.addPart("file", fileBody);
            String fileName = file.getName();
            if (fileName.indexOf(".") != -1) {
                String name = fileName.substring(fileName.lastIndexOf("."));
                entity.addPart("name", new StringBody(name, Charset.forName("UTF-8")));
            }
            // 将流对象添加到请求中
            post.setEntity(entity);
            // 发送请求并获取返回值
            HttpResponse response = client.execute(post);
            // 获取返回实体
            HttpEntity resEntity = response.getEntity();
            // 获取返回结果
            String result = null;
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
                // 注销实体
                resEntity.consumeContent();
            }
            // 结束链接
            client.getConnectionManager().shutdown();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Print.println("UploadImageAsyncTask_result:" + result);
        if (TextUtil.isNotEmpty(result)) {
            try {
                JSONObject object = new JSONObject(result);
                String code = object.getString("code");
                JSONObject data = object.getJSONObject("data");
                if (TextUtil.equals("success", code)) {
                    String url = data.getString("picture_url");
                    String type = data.getString("picture_type");
                    mAsyncTaskCallback.action(url, type);
                } else {
                    mAsyncTaskCallback.fail();
                    Toast.show(context, "上传图片失败");
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mAsyncTaskCallback.fail();
    }

    public interface AsyncTaskCallback {

        /**
         * 回调方法
         */
        void action(String url, String type);

        void fail();
    }
}
