package com.poso2o.lechuan.activity.wshop;

import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.fragment.vdian.VdianSelectGoodsFragment;
import com.poso2o.lechuan.fragment.vdian.VdianUploadGoodsFragment;

/**
 * 微店导入商品
 *
 * Created by Jaydon on 2018/3/16.
 */
public class VdianImportGoodsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 商品列表
     */
    private TextView import_goods_list;

    /**
     * 上传界面
     */
    private TextView import_goods_upload;

    /**
     * 选择商品
     */
    private VdianSelectGoodsFragment vdianSelectGoodsFragment;

    /**
     * 上传商品
     */
    private VdianUploadGoodsFragment vdianUploadGoodsFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian_import_goods;
    }

    @Override
    protected void initView() {
        import_goods_list = findView(R.id.import_goods_list);
        import_goods_upload = findView(R.id.import_goods_upload);
    }

    @Override
    protected void initData() {
        vdianSelectGoodsFragment = new VdianSelectGoodsFragment();
        vdianUploadGoodsFragment = new VdianUploadGoodsFragment();
        addFragment(R.id.import_goods_content, vdianSelectGoodsFragment);
    }

    @Override
    protected void initListener() {
        import_goods_list.setOnClickListener(this);
        import_goods_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.import_goods_list:
                import_goods_list.setTextColor(getColorValue(R.color.colorOrange));
                import_goods_list.setBackgroundResource(R.drawable.shape_oa_title_tag_select);
                import_goods_upload.setTextColor(getColorValue(R.color.textGray));
                import_goods_upload.setBackgroundColor(0x00000000);
                replaceFragment(R.id.import_goods_content, vdianSelectGoodsFragment);
                break;

            case R.id.import_goods_upload:
                import_goods_upload.setTextColor(getColorValue(R.color.colorOrange));
                import_goods_upload.setBackgroundResource(R.drawable.shape_oa_title_tag_select);
                import_goods_list.setTextColor(getColorValue(R.color.textGray));
                import_goods_list.setBackgroundColor(0x00000000);
                replaceFragment(R.id.import_goods_content, vdianUploadGoodsFragment);
                break;
        }
    }
}
