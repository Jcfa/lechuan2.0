package com.poso2o.lechuan.activity.official;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.realshop.AdGoodsActivity;
import com.poso2o.lechuan.activity.realshop.ArtPreviewActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.util.Toast;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by mr zhang on 2018/2/10.
 */

public class ModelEditActivity extends BaseActivity implements View.OnClickListener{

    private int AD_GOODS_CODE = 2601;
    private int PICTURE = 2602;

    //网页
    private WebView webview;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_model_bought_info;
    }

    @Override
    protected void initView() {
        webview = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        findViewById(R.id.bought_info_back).setOnClickListener(this);
        findViewById(R.id.edit_model_preview).setOnClickListener(this);

        findViewById(R.id.model_goods).setOnClickListener(this);
        findViewById(R.id.model_pic).setOnClickListener(this);

        findViewById(R.id.model_edit_back).setOnClickListener(this);
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
            case R.id.model_edit_back:
                finish();
                break;
            case R.id.model_edit_save:
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
}
