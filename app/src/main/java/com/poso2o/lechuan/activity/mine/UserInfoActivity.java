package com.poso2o.lechuan.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.login.UpdatePasswordActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.BottomDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.CustomHelper;
import com.poso2o.lechuan.views.HeadZoomScrollView;

import org.raphets.roundimageview.RoundImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-02.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
    private RoundImageView ivLogo;
    private ImageView ivBackgroundLogo;
    private TextView tvNick, tvDescription, tvPhone, tvNick2;
    private static final int LOGO_RESULT = 5;
    private InvokeParam invokeParam;
    private static final int SET_LOGO_ID = 11;//设置头像
    private static final int SET_BACKGROUND_ID = 12;//设置背景
    private int CURRENT_ID = SET_LOGO_ID;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void onCreate2(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate2(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_userinfo);
//        initView();
//        initData();
//        initListener();
//    }

    @Override
    protected void initView() {
//        final RelativeLayout layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
//        final View titleLine = findViewById(R.id.title_line);
//        layoutTitle.getBackground().setAlpha(0);
//        titleLine.getBackground().setAlpha(0);
//        HeadZoomScrollView zoomScrollView = (HeadZoomScrollView) findViewById(R.id.scrollView);
//        zoomScrollView.setOnScrollListener(new HeadZoomScrollView.OnScrollListener() {
//            @Override
//            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int alpha = scrollY;
//                if (scrollY > 255) {
//                    alpha = 255;
//                } else if (scrollY < 0) {
//                    alpha = 0;
//                }
//                layoutTitle.getBackground().setAlpha(alpha);
//                titleLine.getBackground().setAlpha(alpha);
//            }
//        });
        ivLogo = (RoundImageView) findViewById(R.id.iv_logo);
        ivBackgroundLogo = findView(R.id.iv_background_logo);
        tvNick = (TextView) findViewById(R.id.tv_usernick);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvNick2 = (TextView) findViewById(R.id.tv_nick);
        tvNick = (TextView) findViewById(R.id.tv_usernick);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvNick2 = (TextView) findViewById(R.id.tv_nick);
        ivLogo.setOnClickListener(this);
        findViewById(R.id.layout_phone).setOnClickListener(this);
        findViewById(R.id.layout_usernick).setOnClickListener(this);
        findView(R.id.layout_password).setOnClickListener(this);
        findViewById(R.id.layout_feedback).setOnClickListener(this);
        findViewById(R.id.layout_explain).setOnClickListener(this);
        findViewById(R.id.layout_about_us).setOnClickListener(this);
        findViewById(R.id.layout_loginout).setOnClickListener(this);
        findView(R.id.layout_background_logo).setOnClickListener(this);
        tvDescription.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
        tvNick2.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
        tvPhone.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE));
        tvDescription.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_DESCRIPTION));
        ivLogo.setBorderWidth(0);
        Print.println("KEY_USER_BACKBROUND_LOGO="+SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_BACKBROUND_LOGO)+",KEY_USER_LOGO="+SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO));
        Glide.with(activity).load(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO)).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
        Glide.with(activity).load(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_BACKBROUND_LOGO)).thumbnail(0.1f).into(ivBackgroundLogo);
    }

    @Override
    protected void initListener() {
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                showBottumDialog();
                break;
            case R.id.layout_phone://手机
                Bundle phoneBundle = new Bundle();
                phoneBundle.putString(EditPhoneActivity.KEY_PHONE, tvPhone.getText().toString());
                startActivityForResult(EditPhoneActivity.class, phoneBundle, EditPhoneActivity.PHONE_CODE);
                break;
            case R.id.layout_usernick://昵称
                Bundle nickBundle = new Bundle();
                nickBundle.putString(EditTextActivity.KEY_TITLE, "昵称");
                nickBundle.putString(EditTextActivity.KEY_VALUE, tvNick.getText().toString());
                nickBundle.putInt(EditTextActivity.KEY_TYPE, EditTextActivity.NICK_RESULT);
                startActivityForResult(EditTextActivity.class, nickBundle, EditTextActivity.NICK_RESULT);
                break;
            case R.id.layout_password://修改密码
                startActivity(UpdatePasswordActivity.class);
                break;
            case R.id.layout_feedback://意见反馈

                break;
            case R.id.layout_explain://使用说明

                break;
            case R.id.layout_about_us://关于我们

                break;
            case R.id.tv_description://简介
                Bundle descBundle = new Bundle();
                descBundle.putString(EditTextActivity.KEY_TITLE, "简介");
                descBundle.putString(EditTextActivity.KEY_VALUE, tvDescription.getText().toString());
                descBundle.putInt(EditTextActivity.KEY_TYPE, EditTextActivity.DESCRIPTION_RESULT);
                startActivityForResult(EditTextActivity.class, descBundle, EditTextActivity.DESCRIPTION_RESULT);
                break;
            case R.id.layout_background_logo://相册封面图
                setBackgroundPhotoDialog();
                break;
            case R.id.layout_loginout://退出登录
                AppUtil.exitApp(activity, true);
                break;
        }
    }


    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EditTextActivity.NICK_RESULT:
                if (resultCode == RESULT_OK) {
                    tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
                    tvNick2.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
                }
                break;
            case EditTextActivity.DESCRIPTION_RESULT:
                if (resultCode == RESULT_OK) {
                    tvDescription.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_DESCRIPTION));
                }
                break;
            case EditPhoneActivity.PHONE_CODE:
                if (resultCode == RESULT_OK) {
                    tvPhone.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE));
                }
                break;
            case LOGO_RESULT:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        Glide.with(activity).load(new File(selectedImages.get(0))).into(ivLogo);
                        MineDataManager.getMineDataManager().editLogo(activity, selectedImages.get(0), new IRequestCallBack() {
                            @Override
                            public void onFailed(int tag, String msg) {

                            }

                            @Override
                            public void onResult(int tag, Object object) {

                            }
                        });
                    }
                }
                break;
        }
    }

    /**
     * 选择相册，相机拍照
     */
    private void showBottumDialog() {
        BottomDialog dialog = new BottomDialog(activity);
        dialog.setChooseCallBack(new BottomDialog.IChooseCallBack() {
            @Override
            public void onPhoto() {
                CURRENT_ID = SET_LOGO_ID;
                CustomHelper.of().setmMaxSize(10).setmCropSize(80, 80).pickBySelect(getTakePhoto(), 1);//压缩30K以内，剪切尺寸100*100，选择图片数量1
            }

            @Override
            public void onCamera() {
                CURRENT_ID = SET_LOGO_ID;
                CustomHelper.of().setmMaxSize(10).setmCropSize(80, 80).pickByTake(getTakePhoto());//压缩30K以内，剪切尺寸100*100
            }
        });
        dialog.show();
    }

    private void setBackgroundPhotoDialog() {
        final Dialog dialog = new Dialog(activity);
        View view = getLayoutInflater().inflate(R.layout.dialog_set_background_view, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置对话框背景为透明
        dialog.show();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CURRENT_ID = SET_BACKGROUND_ID;
                CustomHelper.of().setmMaxSize(10).setmCropSize(200, 200).setmCompressSize(300, 300).pickBySelect(getTakePhoto(), 1);//压缩100K以内，剪切尺寸200*200，压缩分辨率600*600
            }
        });
    }

    /**
     * 上传头像
     *
     * @param path
     */
    private void updateLogo(final String path) {
//        Glide.with(this).load(new File(path)).into(ivLogo);
        WaitDialog.showLoaddingDialog(activity, "正在上传...");
        MineDataManager.getMineDataManager().editLogo(this, path, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, Object object) {
                Toast.show(activity, "设置成功！");
                Glide.with(activity).load(new File(path)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
            }
        });
    }

    /**
     * 上传相册封面图
     *
     * @param path
     */
    private void updateBackgroundLogo(final String path) {
//        Glide.with(this).load(new File(path)).into(ivLogo);
        WaitDialog.showLoaddingDialog(activity, "正在上传...");
        MineDataManager.getMineDataManager().editBackgroundLogo(this, path, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, Object object) {
                Toast.show(activity, "设置成功！");
                Glide.with(activity).load(new File(path)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivBackgroundLogo);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        if (CURRENT_ID == SET_LOGO_ID) {//上传头像
            updateLogo(images.get(0).getCompressPath());
        } else if (CURRENT_ID == SET_BACKGROUND_ID) {//上传背景图
            updateBackgroundLogo(images.get(0).getCompressPath());
        }

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    TakePhoto takePhoto;

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
//        TakePhoto takePhoto=new TakePhotoImpl(activity,this);
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

}
