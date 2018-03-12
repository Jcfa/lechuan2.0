package com.poso2o.lechuan.activity.realshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.UploadImageBean;
import com.poso2o.lechuan.bean.main_menu.MainMenuBean;
import com.poso2o.lechuan.bean.shopdata.CompanyAuthorityBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.UpLoadImageManager;
import com.poso2o.lechuan.manager.rshopmanager.CompanyAuthorityManager;
import com.poso2o.lechuan.tool.image.ImageCompressTool;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.ImageUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/5.
 */

public class CompanyDefineActivity extends BaseActivity implements View.OnClickListener{

    //营业执照
    private static final int PICTURE_LICENCE_CODE = 1001;
    //店内悬挂营业执照照片
    private static final int PICTURE_IN_SHOP_CODE = 1002;
    //店铺铺面照片
    private static final int PICTURE_OUT_SHOP_CODE = 1003;
    //绑定微信公众号页面跳转码
    private static final int AUTHORITY_WC_CODE = 1004;

    private Context context;

    //返回
    private ImageView company_commit_back;

    //企业名称
    private EditText company_name;
    //名称状态
    private TextView company_name_state;
    //审核中展示
    private TextView company_name_checking;

    //法人
    private EditText company_corporate;
    //法人状态
    private TextView company_corporate_state;
    //法人审核中展示
    private TextView company_corporate_checking;

    //法人电话
    private EditText company_licence_mobile;
    //法人电话状态
    private TextView company_licence_mobile_state;
    //法人电话审核状态
    private TextView company_licence_mobile_checking;

    //营业执照号
    private EditText company_licence_no;
    //执照号状态
    private TextView company_licence_state;
    //执照号审核中展示
    private TextView company_licence_checking;

    //营业地址
    private EditText company_address;
    //营业地址状态
    private TextView company_address_state;
    //地址审核中展示
    private TextView company_address_checking;

    //营业执照照片
    private ImageView up_licence;
    //营业执照照片状态
    private TextView img_license_state;

    //悬挂营业执照照片
    private ImageView up_inshop;
    //悬挂营业执照照片状态
    private TextView img_inshop_state;

    //店铺界面
    private ImageView up_outshop;
    //店铺界面状态
    private TextView img_out_shop_state;

    //下一步
    private TextView company_commit_next;

    //营业执照照片
    private String licenceB ;
    //悬挂营业执照照片
    private String inshopB;
    //店面照片
    private String outshopB;

    //认证信息
    private CompanyAuthorityBean authorityBean;
    private int mType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_company_commit;
    }

    @Override
    protected void initView() {
        context = this;

        company_commit_back = (ImageView) findViewById(R.id.company_commit_back);

        company_name = (EditText) findViewById(R.id.company_name);
        company_corporate = (EditText) findViewById(R.id.company_corporate);
        company_licence_mobile = (EditText) findViewById(R.id.company_licence_mobile);
        company_licence_no = (EditText) findViewById(R.id.company_licence_no);
        company_address = (EditText) findViewById(R.id.company_address);

        up_licence = (ImageView) findViewById(R.id.up_licence);
        up_inshop = (ImageView) findViewById(R.id.up_inshop);
        up_outshop = (ImageView) findViewById(R.id.up_outshop);

        company_name_state = (TextView) findViewById(R.id.company_name_state);
        company_corporate_state = (TextView) findViewById(R.id.company_corporate_state);
        company_licence_mobile_state = (TextView) findViewById(R.id.company_licence_mobile_state);
        company_licence_state = (TextView) findViewById(R.id.company_licence_state);
        company_address_state = (TextView) findViewById(R.id.company_address_state);
        img_license_state = (TextView) findViewById(R.id.img_license_state);
        img_inshop_state = (TextView) findViewById(R.id.img_inshop_state);
        img_out_shop_state = (TextView) findViewById(R.id.img_out_shop_state);

        company_name_checking = (TextView) findViewById(R.id.company_name_checking);
        company_corporate_checking = (TextView) findViewById(R.id.company_corporate_checking);
        company_licence_mobile_checking = (TextView) findViewById(R.id.company_licence_mobile_checking);
        company_licence_checking = (TextView) findViewById(R.id.company_licence_checking);
        company_address_checking = (TextView) findViewById(R.id.company_address_checking);

        company_commit_next = (TextView) findViewById(R.id.company_commit_next);
        company_commit_next.setSelected(true);
    }

    @Override
    protected void initData() {
        mType = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY);
        if (mType == 4){
            initDefineData();
        }else if (mType == 2){
            initDefineData();
        }
    }

    @Override
    protected void initListener() {
        company_commit_back.setOnClickListener(this);

        up_licence.setOnClickListener(this);
        up_inshop.setOnClickListener(this);
        up_outshop.setOnClickListener(this);

        company_commit_next.setOnClickListener(this);

        company_name.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                if (mType == 4){
                    if (!company_name.getText().toString().equals(authorityBean.corporate_name))company_name_state.setText("");
                }
            }
        });

        company_corporate.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                if (mType == 4){
                    if (!company_corporate.getText().toString().equals(authorityBean.legal_person))company_corporate_state.setText("");
                }
            }
        });

        company_licence_no.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                if (mType == 4){
                    if (!company_licence_no.getText().toString().equals(authorityBean.business_license))company_licence_state.setText("");
                }
            }
        });

        company_licence_mobile.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                if (mType == 4){
                    if (!company_licence_mobile.getText().toString().equals(authorityBean.mobile))company_licence_mobile_state.setText("");
                }
            }
        });

        company_address.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                if (mType == 4){
                    if (!company_address.getText().toString().equals(authorityBean.corporate_address))company_address_state.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.company_commit_back:
                finish();
                break;
            case R.id.up_licence:
                toSelectPicture(PICTURE_LICENCE_CODE);
                break;
            case R.id.up_inshop:
                toSelectPicture(PICTURE_IN_SHOP_CODE);
                break;
            case R.id.up_outshop:
                toSelectPicture(PICTURE_OUT_SHOP_CODE);
                break;
            case R.id.company_commit_next:
                companyDefineNext();
                break;
        }
    }

    private void toSelectPicture(final int code) {
        //相机权限
        applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
            @Override
            public void onPermissionResult(boolean b) {
                if (b){
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, code);
                    Intent intent = new Intent(context, SelectImagesActivity.class);
                    intent.putExtra(SelectImagesActivity.MAX_NUM, 1);
                    startActivityForResult(intent, code);
                }else {
                    Toast.show(context,"获取不到相关权限，无法进行操作");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK )return;
        if (requestCode == AUTHORITY_WC_CODE){
            AppUtil.exitApp(this,true);
            return;
        }
        if (data == null)return;
        ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
        if (selectedImages == null || selectedImages.size() < 1) return;
        String picturePath = ImageCompressTool.getInstance(context).compressImage(selectedImages.get(0),720,1280,200*1024);
        if (requestCode == PICTURE_LICENCE_CODE){
            img_license_state.setVisibility(View.INVISIBLE);
            uploadImg(1,picturePath);
        }else if (requestCode == PICTURE_IN_SHOP_CODE){
            img_inshop_state.setVisibility(View.INVISIBLE);
            uploadImg(2,picturePath);
        }else if (requestCode == PICTURE_OUT_SHOP_CODE){
            img_out_shop_state.setVisibility(View.INVISIBLE);
            uploadImg(3,picturePath);
        }else if (requestCode == AUTHORITY_WC_CODE){
            setResult(RESULT_OK);
            finish();
        }
    }

    //下一步
    private void companyDefineNext(){
        String companyname = company_name.getText().toString();
        if (companyname == null || companyname.equals("")){
            Toast.show(context,"请输入企业名称");
            company_name.requestFocus();
            return;
        }

        String corporate = company_corporate.getText().toString();
        if (corporate == null || corporate.equals("")){
            Toast.show(context,"请输入法人名称");
            company_corporate.requestFocus();
            return;
        }

        String mobile = company_licence_mobile.getText().toString();
        if (TextUtil.isEmpty(mobile)){
            Toast.show(context,"请填写法人电话号码");
            company_licence_mobile.requestFocus();
            return;
        }

        String no = company_licence_no.getText().toString();
        if (no == null || no.equals("")){
            Toast.show(context,"请输入执照号");
            company_licence_no.requestFocus();
            return;
        }

        String address = company_address.getText().toString();
        if (address == null || address.equals("")){
            Toast.show(context,"请输入营业地址");
            company_address.requestFocus();
            return;
        }

        if (licenceB == null || licenceB.equals("")){
            Toast.show(context,"请上传营业执照");
            return;
        }

        if (inshopB == null || inshopB.equals("")){
            Toast.show(context,"请上传悬挂营业执照照片");
            return;
        }

        if (outshopB == null || outshopB.equals("")){
            Toast.show(context,"请上传店面照片");
            return;
        }

        showLoading("正在提交数据...");
        CompanyAuthorityManager.getAuthorityManager().authorityCommit(this, companyname, corporate,mobile, no,address, licenceB, inshopB, outshopB, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                Toast.show(context,"信息提交成功，请绑定微信公众号");
                Intent intent = new Intent();
                intent.setClass(context,WCAuthorityActivity.class);
                startActivityForResult(intent,AUTHORITY_WC_CODE);
            }
        });

    }

    private void uploadImg(final int type, String path){
        showLoading("正在上传图片...");
        UpLoadImageManager.getUpLoadImageManager().uploadImage(this, path, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                UploadImageBean bean = (UploadImageBean) object;
                if (type == 1){
                    licenceB = bean.picture_url;
                    Glide.with(context).load(licenceB).placeholder(R.mipmap.background_image).into(up_licence);
                }else if (type == 2){
                    inshopB = bean.picture_url;
                    Glide.with(context).load(inshopB).placeholder(R.mipmap.background_image).into(up_inshop);
                }else if (type == 3){
                    outshopB = bean.picture_url;
                    Glide.with(context).load(outshopB).placeholder(R.mipmap.background_image).into(up_outshop);
                }
            }
        });
    }

    //加载认证信息
    private void initDefineData(){
        showLoading("正在加载认证信息...");
        CompanyAuthorityManager.getAuthorityManager().authorityInfo(this, new IRequestCallBack() {

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                authorityBean = (CompanyAuthorityBean) object;
                if (authorityBean == null)return;
                if (mType == 2){
                    initChecking();
                }else if (mType == 4){
                    initViewData();
                }
            }
        });
    }

    //初始化认证信息
    private void initViewData(){
        company_commit_next.setText("提交认证");
        licenceB = authorityBean.business_license_img;
        inshopB = authorityBean.shop_business_license_img;
        outshopB = authorityBean.shop_scene_img;
        company_name.setText(authorityBean.corporate_name);
        company_name.setSelection(authorityBean.corporate_name.length());
        if (authorityBean.corporate_name_verify.equals("0")){
            company_name_state.setText("不符合");
            company_name_state.setTextColor(0xFFF00000);
            setTextDrawable(company_name_state,R.mipmap.icon_no_pass);
        }else {
            company_name_state.setText("符合");
            company_name_state.setTextColor(0xFF00BCB4);
            setTextDrawable(company_name_state,R.mipmap.icon_pass_48);
        }

        company_corporate.setText(authorityBean.legal_person);
        if (authorityBean.legal_person_verify.equals("0")){
            company_corporate_state.setText("不符合");
            company_corporate_state.setTextColor(0xFFF00000);
            setTextDrawable(company_corporate_state,R.mipmap.icon_no_pass);
        }else {
            company_corporate_state.setText("符合");
            company_corporate_state.setTextColor(0xFF00BCB4);
            setTextDrawable(company_corporate_state,R.mipmap.icon_pass_48);
        }

        company_licence_mobile.setText(authorityBean.mobile);
        if (authorityBean.mobile_verify.equals("0")){
            company_licence_mobile_state.setText("不符合");
            company_licence_mobile_state.setTextColor(0xFFF00000);
            setTextDrawable(company_licence_mobile_state,R.mipmap.icon_no_pass);
        }else {
            company_licence_mobile_state.setText("符合");
            company_licence_mobile_state.setTextColor(0xFF00BCB4);
            setTextDrawable(company_licence_mobile_state,R.mipmap.icon_pass_48);
        }

        company_licence_no.setText(authorityBean.business_license);
        if (authorityBean.business_license_verify.equals("0")){
            company_licence_state.setText("不符合");
            company_licence_state.setTextColor(0xFFF00000);
            setTextDrawable(company_licence_state,R.mipmap.icon_no_pass);
        }else {
            company_licence_state.setText("符合");
            company_licence_state.setTextColor(0xFF00BCB4);
            setTextDrawable(company_licence_state,R.mipmap.icon_pass_48);
        }

        company_address_checking.setText(authorityBean.corporate_address);
        if (authorityBean.corporate_address_verify.equals("0")){
            company_address_state.setText("不符合");
            company_address_state.setTextColor(0xFFF00000);
            setTextDrawable(company_address_state,R.mipmap.icon_no_pass);
        }else {
            company_address_state.setText("符合");
            company_address_state.setTextColor(0xFF00BCB4);
            setTextDrawable(company_address_state,R.mipmap.icon_pass_48);
        }

        Glide.with(context).load(authorityBean.business_license_img).into(up_licence);
        if (authorityBean.business_license_img_verify.equals("0")){
            img_license_state.setText("不符合");
            img_license_state.setTextColor(0xFFF00000);
            setTextDrawable(img_license_state,R.mipmap.icon_no_pass);
        }else {
            img_license_state.setText("符合");
            img_license_state.setTextColor(0xFF00BCB4);
            setTextDrawable(img_license_state,R.mipmap.icon_pass_48);
        }

        Glide.with(context).load(authorityBean.shop_business_license_img).into(up_inshop);
        if (authorityBean.shop_business_license_img_verify.equals("0")){
            img_inshop_state.setText("不符合");
            img_inshop_state.setTextColor(0xFFF00000);
            setTextDrawable(img_inshop_state,R.mipmap.icon_no_pass);
        }else {
            img_inshop_state.setText("符合");
            img_inshop_state.setTextColor(0xFF00BCB4);
            setTextDrawable(img_inshop_state,R.mipmap.icon_pass_48);
        }

        Glide.with(context).load(authorityBean.shop_scene_img).into(up_outshop);
        if (authorityBean.shop_scene_img_verify.equals("0")){
            img_out_shop_state.setText("不符合");
            img_out_shop_state.setTextColor(0xFFF00000);
            setTextDrawable(img_out_shop_state,R.mipmap.icon_no_pass);
        }else {
            img_out_shop_state.setText("符合");
            img_out_shop_state.setTextColor(0xFF00BCB4);
            setTextDrawable(img_out_shop_state,R.mipmap.icon_pass_48);
        }
    }

    private void initChecking(){
        company_name.setVisibility(View.GONE);
        company_name_checking.setVisibility(View.VISIBLE);
        company_name_checking.setText(authorityBean.corporate_name);

        company_corporate.setVisibility(View.GONE);
        company_corporate_checking.setVisibility(View.VISIBLE);
        company_corporate_checking.setText(authorityBean.legal_person);

        company_licence_no.setVisibility(View.GONE);
        company_licence_checking.setVisibility(View.VISIBLE);
        company_licence_checking.setText(authorityBean.business_license);

        company_address.setVisibility(View.GONE);
        company_address_checking.setVisibility(View.VISIBLE);
        company_address_checking.setText(authorityBean.corporate_address);

        up_licence.setClickable(false);
        Glide.with(context).load(authorityBean.business_license_img).into(up_licence);

        up_inshop.setClickable(false);
        Glide.with(context).load(authorityBean.shop_business_license_img).into(up_inshop);

        up_outshop.setClickable(false);
        Glide.with(context).load(authorityBean.shop_scene_img).into(up_outshop);

        company_commit_next.setClickable(false);
        company_commit_next.setText("资质审核中...");
        company_commit_next.setSelected(false);
    }

    //设置TextView的DrawableLeft
    private void setTextDrawable(TextView textDrawable,int img){
        Drawable drawable= getResources().getDrawable(img);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textDrawable.setCompoundDrawables(drawable,null,null,null);
    }

}
