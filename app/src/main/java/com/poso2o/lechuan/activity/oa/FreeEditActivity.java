package com.poso2o.lechuan.activity.oa;
import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.official.ModelGroupInfoActivity;
import com.poso2o.lechuan.activity.realshop.AdGoodsActivity;
import com.poso2o.lechuan.activity.realshop.ArtPreviewActivity;
import com.poso2o.lechuan.adapter.ADTemplateAdapter;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsMovePicture;
import com.poso2o.lechuan.bean.oa.DefaultTemplates;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.manager.oa.ModelGroupManager;
import com.poso2o.lechuan.manager.oa.RenewalsManager;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.poso2o.lechuan.util.ScreenUtil;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;

import java.security.Permission;
import java.util.ArrayList;

/**
 * 公众号助手-自由编辑，模版部分由h5实现
 * Created by Administrator on 2018-02-05.
 */

public class FreeEditActivity extends BaseActivity implements View.OnClickListener {

    //打开相册
    private static final int CODE_OPEN_PIC = 1301;
    //JS调用Android选择商品
    private static final int CODE_SELECT_GOODS = 1302;
    //Android主动选择商品添加到文章中
    private static final int CODE_INPUT_GOODS = 1304;
    //切换模板
    private static final int CODE_CHANGE_MODEL = 1303;

    //选择模板数据
    public static final String DATA_TEMPLATE = "select_tempate";

    private View free_edit_view;

    //选择模板的名称
    private TextView select_template_name;
    //展开收起模板
    private ImageView free_edit_hide_template;
    //模板列表
    private RecyclerView recyclerView_template;
    //模板列表和网页之间的分割线
    private View recycler_line;

    //编辑网页
    private WebView webview_edit;

    //添加商品、图片布局
    private LinearLayout add_layout;

    //操作布局
    private LinearLayout free_edit_menu;

    //键盘打开状态
    private boolean keyBoardShown;

    /**
     * 记录上传图片下标
     */
    private int index;
    /**
     * 是否上传图片结束
     */
    private boolean isUploadImageFinish = true;

    private ADTemplateAdapter mTemplateAdapter;
    private ArrayList<TemplateGroup> templatesGroup;
    private ArrayList<TemplateBean> templates = new ArrayList<>();
    //当前选择的模板
    private TemplateBean templateBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_oa_free_edit;
    }

    @Override
    protected void initView() {
        free_edit_view = findView(R.id.free_edit_view);

        select_template_name = findView(R.id.select_template_name);

        free_edit_hide_template = findView(R.id.free_edit_hide_template);

        recyclerView_template = findView(R.id.recyclerView_template);

        recycler_line = findView(R.id.recycler_line);

        webview_edit = findView(R.id.webview_edit);

        add_layout = findView(R.id.add_layout);

        free_edit_menu = findView(R.id.free_edit_menu);
    }

    @Override
    protected void initData() {
        initWebView();
        getTemplateGroups();
    }

    @Override
    protected void initListener() {
        findView(R.id.iv_back).setOnClickListener(this);
        findView(R.id.free_edit_preview).setOnClickListener(this);
        free_edit_hide_template.setOnClickListener(this);

        findView(R.id.oa_template_change).setOnClickListener(this);
        findView(R.id.oa_template_goods).setOnClickListener(this);
        findView(R.id.oa_template_image).setOnClickListener(this);

        findView(R.id.free_edit_draft).setOnClickListener(this);
        findView(R.id.free_edit_publish).setOnClickListener(this);

        mTemplateAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<TemplateBean>() {
            @Override
            public void onItemClick(TemplateBean item) {
                templateBean = item;
                select_template_name.setText(item.template_name);
                webview_edit.loadUrl("javascript:emptyAdTemplate()");
                webview_edit.loadUrl("javascript:appendBase64HTML('" + item.content + "')");
                mTemplateAdapter.notifyDataSetChanged(templates,templateBean == null ? "" : templateBean.template_id);
                showOrNot();
            }
        });

        new ScreenUtil(this).observeInputlayout(free_edit_view, new ScreenUtil.OnInputActionListener() {
            @Override
            public void onOpen() {
                //键盘弹出
                add_layout.setVisibility(View.VISIBLE);
                free_edit_menu.setVisibility(View.GONE);
            }

            @Override
            public void onClose() {
                //键盘收起
                add_layout.setVisibility(View.GONE);
                free_edit_menu.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.free_edit_preview:
                //预览
                getHtml(0,null);
                break;
            case R.id.free_edit_hide_template:
                //模板展开、收起
                showOrNot();
                break;
            case R.id.oa_template_change:
                //切换模板
                toChangeTemplate();
                break;
            case R.id.oa_template_goods:
                //选择插入商品
                selectGoodsInput();
                break;
            case R.id.oa_template_image:
                //选择插入图片
                openPicture();
                break;
            case R.id.free_edit_draft:
                //添加到草稿箱
                changeToArticle(1);
                break;
            case R.id.free_edit_publish:
                //添加到发布
                changeToArticle(2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_OPEN_PIC:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        compressImages(selectedImages);
                    }
                    break;
                case CODE_SELECT_GOODS:
                    Goods goods = (Goods) data.getExtras().get(AdGoodsActivity.AD_ARTICLE_GOODS);
                    //序列化对象
                    try{
                        String str = new Gson().toJson(goods);
                        str = str.replaceAll("\n","");
                        String xmlStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
                        xmlStr = xmlStr.replaceAll("\n","");
                        webview_edit.loadUrl("javascript:replaceGoodsItemInfo('" + xmlStr + "')");
                    }catch (Exception e){
                        Toast.show(getApplication(),"数据读写出错");
                    }
                    break;
                case CODE_CHANGE_MODEL:
                    templateBean = (TemplateBean) data.getExtras().get(DATA_TEMPLATE);
                    if (templateBean != null){
                        select_template_name.setText(templateBean.template_name);
                        webview_edit.loadUrl("javascript:emptyAdTemplate()");
                        webview_edit.loadUrl("javascript:appendBase64HTML('" + templateBean.content + "')");
                    }
                    break;
                case CODE_INPUT_GOODS:
                    Goods g = (Goods) data.getExtras().get(AdGoodsActivity.AD_ARTICLE_GOODS);
                    inputGoods(g);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (webview_edit != null){
            webview_edit.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            webview_edit.clearHistory();
            ((ViewGroup)webview_edit.getParent()).removeView(webview_edit);
            webview_edit.destroy();
            webview_edit = null;
        }
        super.onDestroy();
    }

    private void initWebView(){
        WebSettings settings = webview_edit.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview_edit.loadUrl("http://wechat.poso2o.com/editor/?v=3.0");
        webview_edit.addJavascriptInterface(FreeEditActivity.this,"android");
        webview_edit.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    webview_edit.loadUrl("javascript:emptyHtml()");
            }
        });
    }

    /**
     * 点击预览，获取HTML代码，然后传递跳转
     * @param type  0：预览；1：加入草稿箱；2：添加到发布
     */
    public void getHtml(final int type,final Article article){
        webview_edit.evaluateJavascript("getAllHtml()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String html) {
                final String str = html;
                if (type == 0){
                    webview_edit.evaluateJavascript("getTitleHTML()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Intent intent = new Intent();
                            intent.setClass(getApplication(),ArtPreviewActivity.class);
                            intent.putExtra(ArticleAdActivity.ART_DATA,value + str);
                            startActivity(intent);
                        }
                    });
                }else if (type == 1){
                    article.content = html;
                    addToRenewals(article);
                }else if (type == 2){
                    article.content = html;
                    if (ArticleDataManager.getInstance().addSelectData(getApplication(),article))
                        FreeEditActivity.this.finish();
                }
            }
        });
    }

    //模板列表展开收起
    private void showOrNot(){
        if (recyclerView_template.getVisibility() == View.VISIBLE){
            recyclerView_template.setVisibility(View.GONE);
            free_edit_hide_template.setImageResource(R.mipmap.arrow_more);
            recycler_line.setVisibility(View.GONE);
        }else {
            recyclerView_template.setVisibility(View.VISIBLE);
            free_edit_hide_template.setImageResource(R.mipmap.arrow_less);
            recycler_line.setVisibility(View.VISIBLE);
        }
    }

    //打开相册
    private void openPicture(){
        applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
            @Override
            public void onPermissionResult(boolean b) {
                if (b){
                    AppUtil.openPhoto(getApplication(),FreeEditActivity.this,CODE_OPEN_PIC);
                }else {
                    Toast.show(getApplication(),"请允许该应用打开相册");
                }
            }
        });
    }

    private void changeToArticle(final int type){
        final Article article = new Article();
        webview_edit.evaluateJavascript("getTitleHTML()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                if (TextUtil.isEmpty(s) || s.equals("文章标题") || s.equals("null") || s.equals("\"\"")){
                    Toast.show(getApplication(),"请输入标题");
                    return;
                }else {
                    article.title = s.substring(1,s.length()-1);
                    webview_edit.evaluateJavascript("getImgFirstUrl()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            if (TextUtil.isEmpty(s) || s.equals("null") || s.equals("\"\"")){
                                Toast.show(getApplication(),"请至少添加一张图片");
                                return;
                            }else {
                                article.pic = s.substring(1,s.length()-1);
                                article.articles_type = Constant.SELF_ARTICLE;//99代表自编
                                article.articles_id = RandomStringUtil.getOrderId(3,"abcdefghijklmnopqrstuvwxyz");
                                getHtml(type,article);
                            }
                        }
                    });
                }
            }
        });

    }

    //区别toSelectGoods，toSelectGoods是JS主动调用Android选择商品，而selectGoodsInput是Android主动往文章插入商品
    private void selectGoodsInput(){
        Intent intent = new Intent();
        intent.setClass(getApplication(),AdGoodsActivity.class);
        startActivityForResult(intent,CODE_INPUT_GOODS);
    }

    private void inputGoods(Goods goods){
        showLoading();
        ArticleDataManager.getInstance().getGoodsPic(this, goods.goods_id, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                GoodsMovePicture picture = new Gson().fromJson((String)result,GoodsMovePicture.class);
                insertPic(picture.picture_url);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }

    @JavascriptInterface
    public void toSelectGoods(){
        Intent intent = new Intent();
        intent.setClass(getApplication(),AdGoodsActivity.class);
        startActivityForResult(intent,CODE_SELECT_GOODS);
    }

    /**
     * 调用JS插入图片
     */
    private void insertPic(String pic) {
        webview_edit.loadUrl("javascript:appendImgUrl('" + pic + "')");
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
     * 加载模版
     */
    private void getTemplateGroups() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView_template.setLayoutManager(linearLayoutManager);
        mTemplateAdapter = new ADTemplateAdapter(this,templates);
        recyclerView_template.setAdapter(mTemplateAdapter);

        showLoading();
        ModelGroupManager.getModelGroupManager().defaultTemplates(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                DefaultTemplates defaultTemplates = (DefaultTemplates) result;
                if (defaultTemplates != null && defaultTemplates.list != null) {
                    templates = defaultTemplates.list;
                    mTemplateAdapter.notifyDataSetChanged(templates,templateBean == null ? "" : templateBean.template_id);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
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

    //添加到稿件箱
    private void addToRenewals(Article article) {
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsAdd(this, article, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(getApplication(),"添加成功");
                FreeEditActivity.this.finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getApplication(),msg);
            }
        });
    }

    //跳转切换模板
    private void toChangeTemplate(){
        if (templateBean == null){
            Toast.show(getApplication(),"您当前还没选择模板");
            return;
        }
        TemplateGroup group = ModelGroupManager.getModelGroupManager().findTemplateGroup(templateBean.group_id);
        if (group == null){
            showLoading();
            ModelGroupManager.getModelGroupManager().modelGroups(this, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    toChangeTemplate();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(getApplication(),"获取数据失败");
                }
            });
        }else {
            Intent intent = new Intent();
            intent.setClass(getApplication(), ModelGroupInfoActivity.class);
            intent.putExtra(ModelGroupInfoActivity.TEMPLATE_GROUP_DATA,group);
            intent.putExtra(ModelGroupInfoActivity.TAG_CHANGE_TEMPLATE,true);
            startActivityForResult(intent,CODE_CHANGE_MODEL);
        }
    }

}
