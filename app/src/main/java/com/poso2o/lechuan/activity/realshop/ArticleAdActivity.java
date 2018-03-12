package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.RenewalsManager;
import com.poso2o.lechuan.manager.oa.TemplateDataManager;
import com.poso2o.lechuan.util.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/5.
 */

public class ArticleAdActivity extends BaseActivity implements View.OnClickListener {

    //文章详情
    public static final String ART_DATA = "article_data";
    //添加广告的文章
    public static String AD_ARTICLE = "ad_article";

    //返回
    private ImageView art_ad_back;
    //预览
    private TextView article_detail_preview;

    //文章详情
    private WebView art_detail_web;

    //广告模板名称
    private TextView ad_model_name;
    //展开模板
    private ImageView show_ad_models;
    //模板列表
    private RecyclerView recyclerView_ad_model;
    //模板web
    private WebView web_ad;

    //加入稿件箱
    private TextView add_to_renewals;
    //去发布
    private TextView add_to_publish;

    private ArrayList<TemplateGroup> templatesGroup;
    private ArrayList<TemplateBean> templates = new ArrayList<>();
    private BaseAdapter mTemplateAdapter;
    private Article article;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_ad;
    }

    @Override
    protected void initView() {
        art_ad_back = findView(R.id.art_ad_back);

        article_detail_preview = findView(R.id.article_detail_preview);

        art_detail_web = findView(R.id.art_detail_web);

        ad_model_name = findView(R.id.ad_model_name);

        show_ad_models = findView(R.id.show_ad_models);

        recyclerView_ad_model = findView(R.id.recyclerView_ad_model);

        web_ad = findView(R.id.web_ad);

        add_to_renewals = findView(R.id.add_to_renewals);

        add_to_publish = findView(R.id.add_to_publish);
    }

    @Override
    protected void initData() {
        initArtDetail();
        initModelList();
    }

    @Override
    protected void initListener() {
        art_ad_back.setOnClickListener(this);
        article_detail_preview.setOnClickListener(this);
        show_ad_models.setOnClickListener(this);
        add_to_renewals.setOnClickListener(this);
        add_to_publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.art_ad_back:
                finish();
                break;
            case R.id.article_detail_preview:
                break;
            case R.id.show_ad_models:
                break;
            case R.id.add_to_renewals:
                addToRenewals();
                break;
            case R.id.add_to_publish:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //初始化文章详情
    private void initArtDetail() {
        WebSettings settings = art_detail_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        art_detail_web.loadUrl("file:///android_asset/android_web_view.html");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        article = (Article) bundle.get(ART_DATA);
        final String str = article.content;
        art_detail_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    art_detail_web.loadUrl("javascript:setBODYHTML('" + str + "')");
            }
        });
    }

    //初始化广告模板
    private void initModelList() {
        WebSettings settings = web_ad.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        web_ad.loadUrl("file:///android_asset/android_web_ad.html");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mTemplateAdapter = new BaseAdapter(activity, templates) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, final int position) {
                final TemplateBean templateBean = (TemplateBean) item;
                ImageView templatePic = holder.getView(R.id.iv_template_pic);
                Glide.with(activity).load(templateBean.pic).placeholder(R.mipmap.ic_launcher).into(templatePic);
                TextView templateName = holder.getView(R.id.tv_template_name);
                templateName.setText(templateBean.template_name);
                holder.getView(R.id.item_main).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad_model_name.setText(templateBean.template_name);
                        web_ad.loadUrl("javascript:setBODYHTML('" + templateBean.content + "')");
                    }
                });
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_oa_template_common, parent));
            }
        };

        recyclerView_ad_model.setAdapter(mTemplateAdapter);
        recyclerView_ad_model.setLayoutManager(linearLayoutManager);

        getMyTemplateGroups();
    }

    //打开相机
    private void openCamera() {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        uri = Uri.fromFile(new File("/mnt/sdcard/DCIM/Camera" + System.currentTimeMillis() + ".jpg"));
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(cameraIntent, 001);
    }

    //打开相册
    private void openPhoto() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 002);
    }

    //拍照返回
    private void cameraResult() {
        /*final String scheme = uri.getScheme();
        data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
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
        } */
    }

    //相册返回
    private void photoResult(Intent intent) {
        /*Uri uri = intent.getData();
        if (null == uri) return;
        final String scheme = uri.getScheme();
        data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
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
        }*/
    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File("/mnt/sdcard/DCIM/Camera/screenshot" + System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 加载购买的模版
     */
    private void getMyTemplateGroups() {
        showLoading();
        TemplateDataManager.getInstance().getMyTemplateGroups(activity, new IRequestCallBack<TemplateGroups>() {
            @Override
            public void onResult(int tag, TemplateGroups result) {
                dismissLoading();
                if (result != null && result.list != null) {
                    templatesGroup = result.list;
                    if (templatesGroup == null) return;
                    for (TemplateGroup templateGroup : templatesGroup) {
                        templates.addAll(templateGroup.templates);
                    }
                    mTemplateAdapter.notifyDataSetChanged(templates);
                }

                if (templates.size() == 0) return;
                web_ad.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        if (newProgress == 100)
                            web_ad.loadUrl("javascript:setBODYHTML('" + templates.get(0).content + "')");
                    }
                });
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
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }
}
