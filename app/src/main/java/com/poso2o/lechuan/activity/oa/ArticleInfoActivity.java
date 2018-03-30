package com.poso2o.lechuan.activity.oa;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.realshop.AdGoodsActivity;
import com.poso2o.lechuan.activity.realshop.ArtPreviewActivity;
import com.poso2o.lechuan.adapter.ADTemplateAdapter;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.manager.oa.ModelGroupManager;
import com.poso2o.lechuan.manager.oa.RenewalsManager;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ScrollWebView;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/27.
 */

public class ArticleInfoActivity extends BaseActivity implements View.OnClickListener{

    private  LinearLayout article_info;

    private ImageView art_info_back;
    //预览
    private TextView article_info_preview;
    //网页
    private ScrollWebView art_info_web;
    //菜单栏
    private LinearLayout menu_layout;
    //隐藏、展示模板组
    private LinearLayout add_model_layout;
    //添加广告
    private ImageView hide_ad_models;
    //模板名称
    private TextView ad_model_name;
    //模板列表
    private RecyclerView info_model_list;
    //加入草稿箱
    private TextView add_renewals;
    //添加发布
    private TextView add_publish;
    //悬浮按钮
    private ImageView to_bottom;


    private ADTemplateAdapter mTemplateAdapter;

    //图片的本地路径
    private String data;
    private Uri uri;

    //选中的广告模板ID
    private String select_id = "";
    private String select_name = "";

    /**
     * 记录上传图片下标
     */
    private int index;
    /**
     * 是否上传图片结束
     */
    private boolean isUploadImageFinish = true;

    private Article article;
    private String renewals_id;//稿件id，若为空，则该页展示的是文章详情，反之当前页是稿件详情页
    private boolean from_publish = false; //当前展示的是否为发布列表里面的文章详情
    private ArrayList<TemplateGroup> templatesGroup;
    private ArrayList<TemplateBean> templates = new ArrayList<>();

    //键盘打开状态
    private boolean keyBoardShown;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_info;
    }

    @Override
    protected void initView() {
        article_info = findView(R.id.article_info);

        art_info_back = findView(R.id.art_info_back);

        article_info_preview = findView(R.id.article_info_preview);

        art_info_web = findView(R.id.art_info_web);

        menu_layout = findView(R.id.menu_layout);

        add_model_layout = findView(R.id.add_model_layout);

        hide_ad_models = findView(R.id.hide_ad_models);

        ad_model_name = findView(R.id.ad_model_name);

        info_model_list = findView(R.id.info_model_list);

        add_renewals = findView(R.id.add_renewals);

        add_publish = findView(R.id.add_publish);

        to_bottom = findView(R.id.to_bottom);
    }

    @Override
    protected void initData() {
        initArtDetail();
        getMyTemplateGroups();
    }

    @Override
    protected void initListener() {
        art_info_back.setOnClickListener(this);
        article_info_preview.setOnClickListener(this);

        add_model_layout.setOnClickListener(this);

        add_renewals.setOnClickListener(this);
        add_publish.setOnClickListener(this);

        to_bottom.setOnClickListener(this);

        mTemplateAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<TemplateBean>() {
            @Override
            public void onItemClick(TemplateBean item) {
                if (select_id.equals(item.template_id))return;
                ad_model_name.setText(item.template_name);
                String str = item.content;
                select_id = item.template_id;
                select_name = item.template_name;
                mTemplateAdapter.notifyDataSetChanged(templates,select_id);
                art_info_web.loadUrl("javascript:emptyAdTemplate()");
                art_info_web.loadUrl("javascript:appendBase64HTML('" + str + "')");
                to_bottom.setVisibility(View.VISIBLE);
                menu_layout.setVisibility(View.GONE);
            }
        });

        article_info.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = article_info.getRootView().getHeight() - article_info.getHeight();
                // 大于100像素，是打开的情况
                if (heightDiff > 100) {
                    // 如果已经打开软键盘，就不理会
                    if (keyBoardShown) { return; }
                    keyBoardShown = true;

                    to_bottom.setVisibility(View.GONE);
                    menu_layout.setVisibility(View.GONE);
                    return;
                }

                // 软键盘收起的情况
                keyBoardShown = false;
            }
        });

        art_info_web.setOnScrollChangeListener(new ScrollWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                if (menu_layout.getVisibility() == View.GONE && !keyBoardShown){
                    menu_layout.setVisibility(View.VISIBLE);
                    to_bottom.setVisibility(View.GONE);
                    article_info.invalidate();
                }
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                to_bottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (menu_layout.getVisibility() == View.VISIBLE && oldt > t){
                    menu_layout.setVisibility(View.GONE);
                    to_bottom.setVisibility(View.VISIBLE);
                    article_info.invalidate();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.art_info_back:
                finish();
                break;
            case R.id.article_info_preview:
                getHtml(0);
                break;
            case R.id.add_model_layout:
                showOrNot();
                break;
            case R.id.add_renewals:
                getHtml(1);
                break;
            case R.id.add_publish:
                getHtml(2);
                break;
            case R.id.to_bottom:
                art_info_web.requestFocus();
                art_info_web.loadUrl("javascript:scrollToEnd()");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(art_info_web.getWindowToken(), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 001 && resultCode == RESULT_OK) {
            cameraResult();
        } else if (requestCode == 002 && resultCode == RESULT_OK) {
            photoResult(data);
        } else if (requestCode == ArticleAdActivity.AD_GOODS_CODE && resultCode == RESULT_OK) {
            Goods goods = (Goods) data.getExtras().get(AdGoodsActivity.AD_ARTICLE_GOODS);
            //序列化对象
            try {
                String str = new Gson().toJson(goods);
                str = str.replaceAll("\n", "");
                String xmlStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
                xmlStr = xmlStr.replaceAll("\n", "");
                art_info_web.loadUrl("javascript:replaceGoodsItemInfo('" + xmlStr + "')");
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        if (art_info_web != null){
            art_info_web.loadDataWithBaseURL(null,"","text/html","uft-8",null);
            art_info_web.clearHistory();
            ((ViewGroup)art_info_web.getParent()).removeView(art_info_web);
            art_info_web.destroy();
            art_info_web = null;
        }
        super.onDestroy();
    }

    //初始化文章详情
    private void initArtDetail() {
        WebSettings settings = art_info_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        art_info_web.loadUrl("http://wechat.poso2o.com/editor/?v=2.0");
        art_info_web.addJavascriptInterface(ArticleInfoActivity.this, "android");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        article = (Article) bundle.get(ArticleAdActivity.ART_DATA);
        renewals_id = (String) bundle.get(ArticleAdActivity.RENEWALS_ID);
        from_publish = !(ArticleDataManager.getInstance().findSelectData(article) == null);
        if (from_publish) add_publish.setText("保存");
        if (TextUtil.isNotEmpty(renewals_id)) add_renewals.setText("保存草稿");
        if (TextUtil.isNotEmpty(article.ad_name))ad_model_name.setText(article.ad_name);
        select_id = article.ad_id;
        select_name = article.ad_name;
        final String str = article.content;
        art_info_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    art_info_web.loadUrl("javascript:setTitleHTML('" + article.title + "')");
                art_info_web.loadUrl("javascript:setHTML('" + str + "')");
            }
        });
    }

    //模板列表展开收起
    private void showOrNot() {
        if (info_model_list.getVisibility() == View.VISIBLE) {
            info_model_list.setVisibility(View.GONE);
            hide_ad_models.setImageResource(R.mipmap.arrow_more);
        } else {
            info_model_list.setVisibility(View.VISIBLE);
            hide_ad_models.setImageResource(R.mipmap.arrow_less);
        }
    }

    //删除广告模板回调
    @JavascriptInterface
    public void callDelClick(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                select_id = "";
                select_name = "";
                ad_model_name.setText("请选择模板");
                art_info_web.clearFocus();
                mTemplateAdapter.notifyDataSetChanged(templates,select_id);
            }
        });
    }

    //跳转商品选择页面
    @JavascriptInterface
    public void toSelectGoods() {
        Intent intent = new Intent();
        intent.setClass(getApplication(), AdGoodsActivity.class);
        startActivityForResult(intent, ArticleAdActivity.AD_GOODS_CODE);
    }

    //打开相机
    @JavascriptInterface
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uri = Uri.fromFile(new File("/mnt/sdcard/DCIM/Camera" + System.currentTimeMillis() + ".jpg"));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(cameraIntent, 001);
    }

    //打开相册
    @JavascriptInterface
    private void openPhoto() {
        AppUtil.openPhoto(this, this, 002);
    }

    //拍照返回
    private void cameraResult() {
        final String scheme = uri.getScheme();
        data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = this.getContentResolver().query(uri,
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
    }

    //相册返回
    private void photoResult(Intent intent) {
        ArrayList<String> selectedImages = intent.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
        if (selectedImages != null && selectedImages.size() > 0) {
            compressImages(selectedImages);
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
                    Toast.show(getApplication(), "压缩图片失败");
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

    /**
     * 多张图片上传
     *
     * @param paths
     */
    private void uploadImages(final ArrayList<String> paths) {
        setLoadingMessage("正在上传图片(" + (index + 1) + "/" + paths.size() + ")");
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(getApplication(), paths.get(index), new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                insertPic(url);
                index++;
                if (index < paths.size()) {
                    uploadImages(paths);
                } else {
                    isUploadImageFinish = true;
                    Toast.show(getApplication(), "上传图片成功");
                    dismissLoading();
                }
            }

            @Override
            public void fail() {
                isUploadImageFinish = true;
                Toast.show(getApplication(), "上传图片失败");
                dismissLoading();
            }
        });
        asyncTask.execute();
    }

    /**
     * 调用JS插入图片
     */
    private void insertPic(String pic) {
        art_info_web.loadUrl("javascript:appendImgUrl('" + pic + "')");
    }

    /**
     * 加载模版
     */
    private void getMyTemplateGroups() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        info_model_list.setLayoutManager(linearLayoutManager);
        mTemplateAdapter = new ADTemplateAdapter(this, templates);
        info_model_list.setAdapter(mTemplateAdapter);

        showLoading();
        ModelGroupManager.getModelGroupManager().modelGroups(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                TemplateGroups templateGroups = (TemplateGroups) result;
                if (templateGroups != null && templateGroups.list != null) {
                    templatesGroup = templateGroups.list;
                    if (templatesGroup == null) return;
                    for (TemplateGroup templateGroup : templatesGroup) {
                        templates.addAll(templateGroup.templates);
                    }
                    mTemplateAdapter.notifyDataSetChanged(templates,article.ad_id);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
            }
        });
    }

    //添加到稿件箱
    private void addToRenewals() {
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsAdd(this, article, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(getApplication(), "添加成功");
                if (from_publish) ArticleDataManager.getInstance().removeSelectData(article);
                ArticleInfoActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(), msg);
            }
        });
    }

    //删除稿件
    private void delRenewals() {
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsDel(this, renewals_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                if (ArticleDataManager.getInstance().addSelectData(getApplication(), article))
                    ArticleInfoActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(), msg);
            }
        });
    }

    //修改稿件
    private void updateRenewals() {
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsEdit(this, renewals_id, article, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(getApplication(), "保存成功");
                ArticleInfoActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(), msg);
            }
        });
    }

    /**
     * 点击预览，获取HTML代码，然后传递跳转
     *
     * @param type 0：预览；1：加入草稿箱；2：添加到发布
     */
    public void getHtml(final int type) {
        art_info_web.evaluateJavascript("getAllHtml()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String html) {
                if (type == 0) {
                    final String h = html;
                    art_info_web.evaluateJavascript("getTitleHTML()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Intent intent = new Intent();
                            intent.setClass(getApplication(), ArtPreviewActivity.class);
                            intent.putExtra(ArticleAdActivity.ART_DATA, value + h);
                            startActivity(intent);
                        }
                    });
                } else if (type == 1) {
                    article.content = html;
                    article.ad_id = select_id;
                    article.ad_name = select_name;
                    if (TextUtil.isEmpty(renewals_id)) {
                        addToRenewals();
                    } else {
                        updateRenewals();
                    }
                } else if (type == 2) {
                    article.content = html;
                    article.ad_id = select_id;
                    article.ad_name = select_name;
                    if (TextUtil.isNotEmpty(renewals_id)) {
                        //稿件详情，先删除稿件，然后添加到发布列表
                        if (ArticleDataManager.getInstance().addAble(getApplication(), article))
                            delRenewals();
                    } else if (from_publish) {
                        //发布列表文章详情，保存新的文章内容
                        ArticleDataManager.getInstance().updateSelectData(article);
                        ArticleInfoActivity.this.finish();
                    } else {
                        //资讯列表的文章详情，添加到发布列表
                        if (ArticleDataManager.getInstance().addSelectData(getApplication(), article))
                            ArticleInfoActivity.this.finish();
                    }
                }
            }
        });
    }
}
