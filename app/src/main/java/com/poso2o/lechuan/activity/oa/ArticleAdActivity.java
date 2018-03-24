package com.poso2o.lechuan.activity.oa;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
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
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.view.LazyScrollView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/5.
 *
 * 文章详情，或者稿件详情页
 */

public class ArticleAdActivity extends BaseActivity implements View.OnClickListener {

    //文章详情
    public static final String ART_DATA = "article_data";
    //添加广告的文章
    public static String AD_ARTICLE = "ad_article";
    //稿件ID
    public static String RENEWALS_ID = "renewals_id";
    //发布列表跳转过来的标识字段
    public static String FROM_PUBLISH_LIST = "from_publish";
    //添加广告选择商品页面跳转请求码
    public static final int AD_GOODS_CODE = 1310;

    //滚动布局
    private LazyScrollView scroll_layout;
    //返回
    private ImageView art_ad_back;
    //预览
    private TextView article_detail_preview;

    //滑动到底部悬浮键
    private ImageView art_to_bottom;

    //文章详情
    private WebView art_detail_web;

    //广告布局
    private LinearLayout add_ad_layout;
    //模板名称
    private TextView ad_model_name;
    //展开收起模板
    private ImageView show_ad_models;
    //模板列表
    private RecyclerView recyclerView_ad_model;

    //加入稿件箱
    private TextView add_to_renewals;
    //去发布
    private TextView add_to_publish;

    private ADTemplateAdapter mTemplateAdapter;

    //图片的本地路径
    private String data;
    private Uri uri;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_ad;
    }

    @Override
    protected void initView() {
        scroll_layout = findView(R.id.scroll_layout);

        art_ad_back = findView(R.id.art_ad_back);

        article_detail_preview = findView(R.id.article_detail_preview);

        art_to_bottom = findView(R.id.art_to_bottom);

        art_detail_web = findView(R.id.art_detail_web);

        add_ad_layout = findView(R.id.add_ad_layout);

        ad_model_name = findView(R.id.ad_model_name);

        show_ad_models = findView(R.id.show_ad_models);

        recyclerView_ad_model = findView(R.id.recyclerView_ad_model);

        add_to_renewals = findView(R.id.add_to_renewals);

        add_to_publish = findView(R.id.add_to_publish);
    }

    @Override
    protected void initData() {
        initArtDetail();
        getMyTemplateGroups();
    }

    @Override
    protected void initListener() {
        art_ad_back.setOnClickListener(this);
        article_detail_preview.setOnClickListener(this);
        add_ad_layout.setOnClickListener(this);
        add_to_renewals.setOnClickListener(this);
        add_to_publish.setOnClickListener(this);
        art_to_bottom.setOnClickListener(this);

        mTemplateAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<TemplateBean>(){
            @Override
            public void onItemClick(TemplateBean item) {
                ad_model_name.setText(item.template_name);
                String str = item.content;
                art_detail_web.loadUrl("javascript:emptyAdTemplate()");
                art_detail_web.loadUrl("javascript:appendBase64HTML('" + str + "')");
            }
        });

        scroll_layout.setOnScrollListener(new LazyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                if (scroll_layout.getScrollY() + scroll_layout.getHeight() - scroll_layout.getPaddingTop() - scroll_layout.getPaddingBottom() > scroll_layout.getChildAt(0).getHeight() - 200){
                    art_to_bottom.setVisibility(View.GONE);
                }else {
                    art_to_bottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.art_ad_back:
                finish();
                break;
            case R.id.add_ad_layout:
                showOrNot();
                break;
            case R.id.article_detail_preview:
                getHtml(0);
                break;
            case R.id.add_to_renewals:
                getHtml(1);
                break;
            case R.id.add_to_publish:
                getHtml(2);
                break;
            case R.id.art_to_bottom:
                scroll_layout.smoothScrollTo(0,Integer.MAX_VALUE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 001 && resultCode == RESULT_OK) {
            cameraResult();
        } else if (requestCode == 002 && resultCode == RESULT_OK) {
            photoResult(data);
        } else if (requestCode == AD_GOODS_CODE && resultCode == RESULT_OK){
            Goods goods = (Goods) data.getExtras().get(AdGoodsActivity.AD_ARTICLE_GOODS);
            //序列化对象
            try{
                String str = new Gson().toJson(goods);
                str = str.replaceAll("\n","");
                String xmlStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
                xmlStr = xmlStr.replaceAll("\n","");
                art_detail_web.loadUrl("javascript:replaceGoodsItemInfo('" + xmlStr + "')");
            }catch (Exception e){

            }
        }
    }

    //初始化文章详情
    private void initArtDetail() {
        WebSettings settings = art_detail_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        art_detail_web.loadUrl("http://wechat.poso2o.com/editor/?v=2.0");
        art_detail_web.addJavascriptInterface(ArticleAdActivity.this,"android");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        article = (Article) bundle.get(ART_DATA);
        renewals_id = (String) bundle.get(RENEWALS_ID);
        from_publish = !(ArticleDataManager.getInstance().findSelectData(article) == null);
        if (from_publish)add_to_publish.setText("保存");
        if (TextUtil.isNotEmpty(renewals_id))add_to_renewals.setText("保存草稿");
        final String str = article.content;
        art_detail_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    art_detail_web.loadUrl("javascript:setTitleHTML('" + article.title + "')");
                    art_detail_web.loadUrl("javascript:setHTML('" + str + "')");
            }
        });
    }

    //模板列表展开收起
    private void showOrNot(){
        if (recyclerView_ad_model.getVisibility() == View.VISIBLE){
            recyclerView_ad_model.setVisibility(View.GONE);
            show_ad_models.setImageResource(R.mipmap.arrow_more);
        }else {
            recyclerView_ad_model.setVisibility(View.VISIBLE);
            show_ad_models.setImageResource(R.mipmap.arrow_less);
            scroll_layout.post(new Runnable() {
                @Override
                public void run() {
                    scroll_layout.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    //跳转商品选择页面
    @JavascriptInterface
    public void toSelectGoods(){
        Intent intent = new Intent();
        intent.setClass(getApplication(),AdGoodsActivity.class);
        startActivityForResult(intent,AD_GOODS_CODE);
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
        AppUtil.openPhoto(this,this,002);
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
        art_detail_web.loadUrl("javascript:appendImgUrl('" + pic + "')");
    }

    /**
     * 加载模版
     */
    private void getMyTemplateGroups() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView_ad_model.setLayoutManager(linearLayoutManager);
        mTemplateAdapter = new ADTemplateAdapter(this,templates);
        recyclerView_ad_model.setAdapter(mTemplateAdapter);

        showLoading();
        ModelGroupManager.getModelGroupManager().modelGroups(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                TemplateGroups templateGroups = (TemplateGroups) result;
                if (templateGroups != null && templateGroups.list != null) {
                    templatesGroup = templateGroups.list;
                    if (templatesGroup == null)return;
                    for (TemplateGroup templateGroup : templatesGroup){
                        templates.addAll(templateGroup.templates);
                    }
                    mTemplateAdapter.notifyDataSetChanged(templates);
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
                Toast.show(getApplication(),"添加成功");
                if (from_publish)ArticleDataManager.getInstance().removeSelectData(article);
                ArticleAdActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }

    //删除稿件
    private void delRenewals(){
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsDel(this, renewals_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                if (ArticleDataManager.getInstance().addSelectData(getApplication(),article))
                    ArticleAdActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }

    //修改稿件
    private void updateRenewals(){
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsEdit(this, renewals_id, article, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(getApplication(),"保存成功");
                ArticleAdActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }

    /**
     * 点击预览，获取HTML代码，然后传递跳转
     * @param type  0：预览；1：加入草稿箱；2：添加到发布
     */
    public void getHtml(final int type){
        art_detail_web.evaluateJavascript("getAllHtml()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String html) {
                if (type == 0){
                    final String h = html;
                    art_detail_web.evaluateJavascript("getTitleHTML()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Intent intent = new Intent();
                            intent.setClass(getApplication(),ArtPreviewActivity.class);
                            intent.putExtra(ArticleAdActivity.ART_DATA,value + h);
                            startActivity(intent);
                        }
                    });
                }else if (type == 1){
                    article.content = html;
                    if (TextUtil.isEmpty(renewals_id)){
                        addToRenewals();
                    }else {
                        updateRenewals();
                    }
                }else if (type == 2){
                    article.content = html;
                    if (TextUtil.isNotEmpty(renewals_id)){
                        //稿件详情，先删除稿件，然后添加到发布列表
                        if (ArticleDataManager.getInstance().addAble(getApplication(),article))
                            delRenewals();
                    }else if (from_publish){
                        //发布列表文章详情，保存新的文章内容
                        ArticleDataManager.getInstance().updateSelectData(article);
                        ArticleAdActivity.this.finish();
                    }else {
                        //资讯列表的文章详情，添加到发布列表
                        if (ArticleDataManager.getInstance().addSelectData(getApplication(),article))
                            ArticleAdActivity.this.finish();
                    }
                }
            }
        });
    }

}
