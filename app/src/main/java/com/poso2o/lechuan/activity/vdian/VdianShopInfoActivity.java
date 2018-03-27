package com.poso2o.lechuan.activity.vdian;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kevin.crop.UCrop;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.order.AddressSelectActivity;
import com.poso2o.lechuan.activity.realshop.CropActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.SetupBindAccountsDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.tool.image.ImageCompressTool;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.ImageUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jaydon on 2018/3/14.
 */
public class VdianShopInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int PICTURE = 10086;

    private static final int CODE_SELECT_ADDRESS = 10087;
    private static final int SERVICE_RENEW = 10088;//服务续费
    private static final int WEIXIN_SCANNING_CODE = 10090;//调用微信扫码

    /**
     * 保存
     */
    private ImageView shop_info_save;

    /**
     * 店招
     */
    private ImageView shop_info_picture;

    /**
     * 店名
     */
    private EditText shop_info_name;

    /**
     * 简介
     */
    private EditText shop_info_description;

    /**
     * 联系人
     */
    private EditText shop_info_contacts;

    /**
     * 手机
     */
    private EditText shop_info_phone;

    /**
     * 电话
     */
    private EditText shop_info_tel;

    /**
     * 省市区
     */
    private TextView shop_info_area;

    /**
     * 定位
     */
    private View shop_info_location;

    /**
     * 具体地址
     */
    private EditText shop_info_address;

    /**
     * 收款帐号
     */
    private TextView shop_info_accounts;

    /**
     * 套餐名称
     */
    private TextView shop_info_taocan;

    /**
     * 到期时间
     */
    private TextView shop_info_expire;

    /**
     * 店铺数据
     */
    private ShopData shopData;

    /**
     * 剪切后图像文件
     */
    private Uri destinationUri;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian_shop_info;
    }

    @Override
    protected void initView() {
        setTitle("店铺信息");
        shop_info_save = findView(R.id.shop_info_save);
        shop_info_picture = findView(R.id.shop_info_picture);
        shop_info_name = findView(R.id.shop_info_name);
        shop_info_description = findView(R.id.shop_info_description);
        shop_info_contacts = findView(R.id.shop_info_contacts);
        shop_info_phone = findView(R.id.shop_info_phone);
        shop_info_tel = findView(R.id.shop_info_tel);
        shop_info_area = findView(R.id.shop_info_area);
        shop_info_location = findView(R.id.shop_info_area_group);
        shop_info_address = findView(R.id.shop_info_address);
        shop_info_accounts = findView(R.id.shop_info_accounts);
        shop_info_taocan = findView(R.id.shop_info_taocan);
        shop_info_expire = findView(R.id.shop_info_expire);
    }

    @Override
    protected void initData() {
        destinationUri = Uri.fromFile(new File(activity.getCacheDir(), "cropImage.jpeg"));
        shopData = (ShopData) getIntent().getSerializableExtra(Constant.SHOP);
        if (shopData == null) {
            showLoading();
            loadShopData();
        } else {
            refreshView();
        }
    }

    private void refreshView() {
        Glide.with(activity).load(shopData.shop_logo).placeholder(R.mipmap.background_image).into(shop_info_picture);
        shop_info_name.setText(shopData.shop_name);
        shop_info_name.setSelection(shopData.shop_name.length());
        shop_info_description.setText(shopData.shop_introduction);
        shop_info_contacts.setText(shopData.shop_contacts);
        shop_info_phone.setText(shopData.shop_mobile);
        shop_info_tel.setText(shopData.shop_tel);
        shop_info_area.setText(shopData.province_name + " " + shopData.city_name + " " + shopData.area_name);
        shop_info_address.setText(shopData.address);
        // 收款账号
//        StringBuilder accounts = new StringBuilder(shopData.shop_bank_account_name).append("    ");
//        if (shopData.shop_bank_account_no != null && shopData.shop_bank_account_no.length() > 11) {
//            StringBuilder no = new StringBuilder(shopData.shop_bank_account_no);
//            no.replace(4, 11, "*******");
//            accounts.append(no.toString());
//        }
//        if (TextUtils.isNotEmpty(shopData.shop_bank_name)) {
//            accounts.append("(").append(shopData.shop_bank_name).append(")");
//        }
//        shop_info_accounts.setText(accounts.toString());
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_BIND_WX_ACCOUNT, 0) == 1) {
            shop_info_accounts.setHint("已绑定");
        }
        // 套餐
//        if (shopData.has_webshop == 1) {
//            shop_info_taocan.setText(shopData.webshop_service_name);
//            shop_info_expire.setText(shopData.webshop_service_date);
//        } else if (shopData.has_webshop_try == 1) {
//            shop_info_taocan.setText("试用中");
//            shop_info_expire.setText("剩余 " + shopData.webshop_service_days + " 天");
//        }
        shop_info_taocan.setText(shopData.buy_service_name);
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS_OA) > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS_OA, 0));
            calendar.getTimeInMillis();
            shop_info_expire.setText(TimeUtil.longToDateString(calendar.getTimeInMillis(), "yyyy-MM-dd"));
        } else {
            shop_info_expire.setText("已到期");
        }
    }

    private void loadShopData() {
        WShopManager.getrShopManager().wShopInfo(this, new IRequestCallBack<ShopData>() {
            @Override
            public void onResult(int tag, ShopData result) {
                if (result == null) {
                    Toast.show(activity, "加载数据出错");
                } else {
                    shopData = result;
                    refreshView();
                }
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    @Override
    protected void initListener() {
        shop_info_save.setOnClickListener(this);

        shop_info_picture.setOnClickListener(this);
//        shop_info_area.setOnClickListener(this);
        shop_info_location.setOnClickListener(this);
        findView(R.id.shop_info_accounts_group).setOnClickListener(this);
        findView(R.id.shop_info_renew).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_info_save:// 保存
                saveShop();
                break;

            case R.id.shop_info_picture:
                selectPicture();
                break;

//            case R.id.shop_info_area:
//                Intent intent = new Intent();
//                intent.setClass(activity, AddressSelectActivity.class);
//                startActivityForResult(intent, CODE_SELECT_ADDRESS);
//                break;

            case R.id.shop_info_area_group:
                Intent selectArea = new Intent();
                selectArea.setClass(activity, AddressSelectActivity.class);
                startActivityForResult(selectArea, CODE_SELECT_ADDRESS);
                break;

            case R.id.shop_info_accounts_group:
                applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
                    @Override
                    public void onPermissionResult(boolean b) {
                        if (b) {
                            showSetupAccountDialog();
                        } else {
                            Toast.show(activity, "获取不到相关权限，无法进行操作");
                        }
                    }
                });
                break;

            case R.id.shop_info_renew:
                startActivityForResult(new Intent(activity, ServiceOrderingActivity.class), SERVICE_RENEW);
                break;
        }
    }

    /**
     * 显示设置收款帐号
     */
    private void showSetupAccountDialog() {
        SetupBindAccountsDialog bindAccountsDialog = new SetupBindAccountsDialog(activity);
        bindAccountsDialog.show(new SetupBindAccountsDialog.Callback() {
            @Override
            public void onResult() {
                openWeixinScanning();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 打开微信扫一扫
     */
    private void openWeixinScanning() {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.show(activity, "打开微信失败！");
        }
    }

    /**
     * 保存
     */
    private void saveShop() {
        shopData.shop_branch_name = shop_info_name.getText().toString();
        if (shopData.shop_branch_name.equals("")) {
            Toast.show(this, "请输入店铺名称");
            shop_info_name.requestFocus();
            return;
        }
        shopData.shop_branch_contacts = shop_info_contacts.getText().toString();
        if (shopData.shop_branch_contacts.equals("")) {
            Toast.show(this, "请输入联系人");
            shop_info_contacts.requestFocus();
            return;
        }
        shopData.shop_branch_mobile = shop_info_phone.getText().toString();
        if (shopData.shop_branch_mobile.equals("")) {
            Toast.show(this, "请输入手机号码");
            shop_info_phone.requestFocus();
            return;
        }
        showLoading("正在提交店铺信息...");
        shopData.shop_name = shop_info_name.getText().toString();
        shopData.shop_introduction = shop_info_description.getText().toString();
        shopData.shop_contacts = shop_info_contacts.getText().toString();
        shopData.shop_mobile = shop_info_phone.getText().toString();
        shopData.shop_tel = shop_info_tel.getText().toString();
        shopData.address = shop_info_address.getText().toString();

        WShopManager.getrShopManager().editWShop(this, shopData, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                Intent intent = new Intent();
                intent.putExtra(Constant.SHOP, shopData);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    /**
     * 选择照片
     */
    private void selectPicture() {
        //相机权限
        applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {

            @Override
            public void onPermissionResult(boolean b) {
                if (b) {
                    Intent intent = new Intent(activity, SelectImagesActivity.class);
                    intent.putExtra(SelectImagesActivity.MAX_NUM, 1);
                    startActivityForResult(intent, PICTURE);
                } else {
                    Toast.show(activity, "获取不到相关权限，无法进行操作");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (WEIXIN_SCANNING_CODE == requestCode) {
            Toast.show(activity, "onActivityResult:" + resultCode);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICTURE:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        startCropActivity(getImageContentUri(activity, selectedImages.get(0)));
                    }
                    break;
                case CODE_SELECT_ADDRESS:
                    onAddressResult(data);
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;
                case SERVICE_RENEW://续费页返回刷新
                    loadShopData();
                    break;
            }
        }
    }

    /**
     * 绝对路径转URI
     *
     * @param context
     * @param filePath
     * @return
     */
    public Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropActivity.class)
                .start(this);
    }

    /**
     * 地址选择返回
     */
    private void onAddressResult(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            String provinceId = bundle.getString(AddressSelectActivity.KEY_PROVINCE_ID);
            String provinceName = bundle.getString(AddressSelectActivity.KEY_PROVINCE_NAME);
            String cityId = bundle.getString(AddressSelectActivity.KEY_CITY_ID);
            String cityName = bundle.getString(AddressSelectActivity.KEY_CITY_NAME);
            String areaId = bundle.getString(AddressSelectActivity.KEY_AREA_ID);
            String areaName = bundle.getString(AddressSelectActivity.KEY_AREA_NAME);
            shopData.province_id = provinceId;
            shopData.province_name = provinceName;
            shopData.city_id = cityId;
            shopData.city_name = cityName;
            shopData.area_id = areaId;
            shopData.area_name = areaName;
            shop_info_area.setText(provinceName + " " + cityName + " " + areaName);
        }
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri) {
            String path = ImageCompressTool.getInstance(activity).compressImage(resultUri.getEncodedPath(), 720, 1080, 100 * 1024);
            editShopLogo(path);
        } else {
            Toast.show(activity, "无法剪切选择图片");
        }
    }

    /**
     * 上传照片
     */
    private void editShopLogo(final String path) {
        showLoading("正在上传头像...");
        final Bitmap bitmap = ImageUtils.getBitmapFromFile(path);
        WShopManager.getrShopManager().updateWShopLogo(this, ImageManage.bitmapToBase64(bitmap), new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(activity, "头像上传成功");
                shop_info_picture.setImageBitmap(bitmap);
                setResult(RESULT_OK);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.show(activity, cropError.getMessage());
        } else {
            Toast.show(activity, "无法剪切选择图片");
        }
    }
}
