package com.poso2o.lechuan.activity.official;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.oa.FreeEditActivity;
import com.poso2o.lechuan.activity.realshop.AdGoodsActivity;
import com.poso2o.lechuan.activity.realshop.ArtPreviewActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.oa.Template;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.ModelGroupManager;
import com.poso2o.lechuan.manager.oa.TemplateDataManager;
import com.poso2o.lechuan.util.Toast;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by mr zhang on 2018/2/10.
 */

public class ModelEditActivity extends BaseActivity implements View.OnClickListener{

    private int AD_GOODS_CODE = 2601;
    private int PICTURE = 2602;
    private int CAMERA_ID = 2603;

    public static final String TEMPLATE_INFO = "template_info";
    //模板组ID
    public static final String TEMPLATE_GROUP_ID = "template_group_id";

    //默认模板
    private TextView set_default_model;
    //网页
    private WebView webview;
    private TemplateBean template ;
    private String group_id = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_bought_info;
    }

    @Override
    protected void initView() {
        set_default_model = findView(R.id.set_default_model);
        webview = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initData() {
        initWebView();
    }

    @Override
    protected void initListener() {
        findViewById(R.id.bought_info_back).setOnClickListener(this);
        findViewById(R.id.edit_model_preview).setOnClickListener(this);

        findViewById(R.id.model_goods).setOnClickListener(this);
        findViewById(R.id.model_pic).setOnClickListener(this);

        set_default_model.setOnClickListener(this);
        findViewById(R.id.model_edit_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bought_info_back:
                finish();
                break;
            case R.id.edit_model_preview:
                Intent preview = new Intent();
                preview.setClass(ModelEditActivity.this, ArtPreviewActivity.class);
                startActivity(preview);
                break;
            case R.id.model_goods:
                Intent intent = new Intent();
                intent.setClass(this,AdGoodsActivity.class);
                startActivityForResult(intent,AD_GOODS_CODE);
                break;
            case R.id.model_pic:
                selectPicture();
                break;
            case R.id.model_edit_save:
                break;
            case R.id.set_default_model:
                setDefaultTemplate();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)return;
        if (requestCode == AD_GOODS_CODE){
            Goods goods = (Goods) data.getExtras().get(AdGoodsActivity.AD_ARTICLE_GOODS);
            Toast.show(this,"选择了商品 " + goods.goods_name);
        }else if (requestCode == PICTURE){

        }
    }

    private void initWebView(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)return;
        template = (TemplateBean) bundle.get(TEMPLATE_INFO);
        group_id = (String)bundle.get(TEMPLATE_GROUP_ID);
        if (template == null)return;

        if (template.has_default == 1){
            setDrawableLeft(true);
        }else {
            setDrawableLeft(false);
        }

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl("http://wechat.poso2o.com/editor/?v=2.0");
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    webview.loadUrl("javascript:emptyHtml()");
                    webview.loadUrl("javascript:appendBase64HTML('" + template.content + "')");
            }
        });
    }

    //选择照片
    private void selectPicture() {
        //相机权限
        applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
            @Override
            public void onPermissionResult(boolean b) {
                if (b){
                    Intent intent = new Intent(ModelEditActivity.this, SelectImagesActivity.class);
                    intent.putExtra(SelectImagesActivity.MAX_NUM, 1);
                    startActivityForResult(intent, PICTURE);
                }else {
                    Toast.show(ModelEditActivity.this,"获取不到相关权限，无法进行操作");
                }
            }
        });
    }

    private void setDrawableLeft(boolean b){
        if (b){
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_ad_selected_blue_48);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            set_default_model.setCompoundDrawables(drawable,null,null,null);
        }else {
            Drawable drawable = getResources().getDrawable(R.mipmap.item_no_select);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            set_default_model.setCompoundDrawables(drawable,null,null,null);
        }
    }

    private void setDefaultTemplate(){
        if (template.has_default == 1){
            Toast.show(this,"直接设置其他模板为默认后该模板自动取消默认");
            return;
        }else {
            showLoading();
            TemplateDataManager.getInstance().setDefaultModel(this,group_id, template.template_id, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    Toast.show(getApplication(),"设置成功");
                    ModelEditActivity.this.finish();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(getApplication(),msg);
                }
            });
        }
    }
}
