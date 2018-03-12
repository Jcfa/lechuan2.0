package com.poso2o.lechuan.activity.realshop;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kevin.crop.UCrop;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.activity.order.AddressSelectActivity;
import com.poso2o.lechuan.activity.wshop.WBindPayActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.addressdata.AreaData;
import com.poso2o.lechuan.bean.addressdata.CityData;
import com.poso2o.lechuan.bean.addressdata.ProvinceData;
import com.poso2o.lechuan.bean.shopdata.BindPayData;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealShopManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.tool.image.ImageCompressTool;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.ImageUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/30.
 */

public class RShopInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int PICTURE = 10086; //requestcode
    private static final int CODE_SELECT_ADDRESS = 10087;
    public static final String SHOP_TYPE = "is_online";
    //绑定微信支付
    public static final int CODE_TO_BIND_PAY = 10088;

    //店铺信息
    public static final String NEW_SHOP_DATA = "new_shop_data";

    private Context context;

    //返回按钮
    private ImageView new_real_shop_back;
    //修改
    private Button new_real_shop_next;
    //店招
    private ImageView new_shop_picture;
    //店名
    private EditText new_shop_name;
    //简介
    private EditText new_real_shop_description;
    //联系人
    private EditText new_shop_contacts;
    //手机
    private EditText new_shop_phone;
    //电话
    private EditText new_shop_tel;
    //省市区
    private TextView new_shop_area;
    //定位
    private ImageView new_real_shop_location;
    //具体地址
    private EditText new_real_shop_address;

    //绑定支付
    private View bind_layout;
    //绑定信息
    private TextView bind_pay_state;

    //是否微店
    private boolean is_online;

    private ShopData shopData;

    //修改后的图片地址
    private String new_img = "";
    //修改后的省份
    private ProvinceData provinceData;
    //修改后的城市
    private CityData cityData;
    //修改后的区县
    private AreaData areaData;

    // 剪切后图像文件
    private Uri mDestinationUri;
    private BindPayData bindPayData;

    public RShopInfoActivity() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_real_shop;
    }

    @Override
    protected void initView() {
        new_real_shop_back = (ImageView) findViewById(R.id.new_real_shop_back);

        new_real_shop_next = (Button) findViewById(R.id.new_real_shop_next);

        new_shop_picture = (ImageView) findViewById(R.id.new_shop_picture);

        new_shop_name = (EditText) findViewById(R.id.new_shop_name);

        new_real_shop_description = (EditText) findViewById(R.id.new_real_shop_description);

        new_shop_contacts = (EditText) findViewById(R.id.new_shop_contacts);

        new_shop_phone = (EditText) findViewById(R.id.new_shop_phone);

        new_shop_tel = (EditText) findViewById(R.id.new_shop_tel);

        new_shop_area = (TextView) findViewById(R.id.new_shop_area);

        new_real_shop_location = (ImageView) findViewById(R.id.new_real_shop_location);

        new_real_shop_address = (EditText) findViewById(R.id.new_real_shop_address);

        bind_layout = findViewById(R.id.bind_layout);

        bind_pay_state = (TextView) findViewById(R.id.bind_pay_state);
    }

    @Override
    protected void initData() {
        context = this;
        is_online = getIntent().getExtras().getBoolean(SHOP_TYPE);
        mDestinationUri = Uri.fromFile(new File(activity.getCacheDir(), "cropImage.jpeg"));
        forRShopInfo();
    }

    @Override
    protected void initListener() {
        new_real_shop_back.setOnClickListener(this);
        new_real_shop_next.setOnClickListener(this);

        new_shop_picture.setOnClickListener(this);
        new_shop_area.setOnClickListener(this);
        new_real_shop_location.setOnClickListener(this);

        bind_pay_state.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_real_shop_back:
                finish();
                break;
            case R.id.new_real_shop_next:
                nextStep();
                break;
            case R.id.new_shop_picture:
                selectPicture();
                break;
            case R.id.new_shop_area:
                Intent intent = new Intent();
                intent.setClass(context, AddressSelectActivity.class);
                startActivityForResult(intent, CODE_SELECT_ADDRESS);
                break;
            case R.id.new_real_shop_location:
                Intent selectArea = new Intent();
                selectArea.setClass(context, AddressSelectActivity.class);
                startActivityForResult(selectArea, CODE_SELECT_ADDRESS);
                break;
            case R.id.bind_pay_state:
                Intent bind_pay = new Intent();
                bind_pay.setClass(context, WBindPayActivity.class);
                bind_pay.putExtra(WBindPayActivity.HAD_BIND_PAY,bindPayData);
                startActivityForResult(bind_pay,CODE_TO_BIND_PAY);
                break;
        }
    }

    private void forRShopInfo() {
        showLoading("正在加载店铺信息...");
        if (is_online) {
            bind_layout.setVisibility(View.VISIBLE);
            WShopManager.getrShopManager().wShopInfo(this, new IRequestCallBack<ShopData>() {

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, ShopData result) {
                    dismissLoading();
                    if (result == null) {
                        Toast.show(context, "加载出错");
                        return;
                    } else {
                        shopData = result;
                        if (shopData == null)return;
                        Glide.with(context).load(shopData.shop_logo).placeholder(R.mipmap.background_image).into(new_shop_picture);
                        new_shop_name.setText(shopData.shop_name);
                        new_shop_name.setSelection(shopData.shop_name.length());
                        new_real_shop_description.setText(shopData.shop_introduction);
                        new_shop_contacts.setText(shopData.shop_contacts);
                        new_shop_phone.setText(shopData.shop_mobile);
                        new_shop_tel.setText(shopData.shop_tel);
                        new_shop_area.setText(shopData.province_name + " " + shopData.city_name + " " + shopData.area_name);
                        new_real_shop_address.setText(shopData.address);
                    }
                }
            });
            //请求支付绑定状态
            requestBindInfo();
        } else {
            RealShopManager.getRealShopManager().rShopInfo(this, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    if (object == null) {
                        Toast.show(context, "加载出错");
                        return;
                    } else {
                        shopData = (ShopData) object;
                        if (shopData == null) return;
                        Glide.with(context).load(shopData.pic222_222).placeholder(R.mipmap.background_image).into(new_shop_picture);
                        new_shop_name.setText(shopData.shopname);
                        new_shop_name.setSelection(shopData.shopname.length());
                        new_real_shop_description.setText(shopData.remark);
                        new_shop_contacts.setText(shopData.attn);
                        new_shop_phone.setText(shopData.mobile);
                        new_shop_tel.setText(shopData.tel);
                        new_shop_area.setText(shopData.provincename + " " + shopData.cityname + " " + shopData.areaname);
                        new_real_shop_address.setText(shopData.address);
                    }
                }
            });
        }
    }

    //修改
    private void nextStep() {
        shopData.shop_branch_name = new_shop_name.getText().toString();
        if (shopData.shop_branch_name == null || shopData.shop_branch_name.equals("")) {
            Toast.show(this, "请输入店铺名称");
            new_shop_name.requestFocus();
            return;
        }
        shopData.shop_branch_contacts = new_shop_contacts.getText().toString();
        if (shopData.shop_branch_contacts == null || shopData.shop_branch_contacts.equals("")) {
            Toast.show(this, "请输入联系人");
            new_shop_contacts.requestFocus();
            return;
        }
        shopData.shop_branch_mobile = new_shop_phone.getText().toString();
        if (shopData.shop_branch_mobile == null || shopData.shop_branch_mobile.equals("")) {
            Toast.show(this, "请输入手机号码");
            new_shop_phone.requestFocus();
            return;
        }
        showLoading("正在提交店铺信息...");
        if (is_online) {
            shopData.shop_name = new_shop_name.getText().toString();
            shopData.shop_introduction = new_real_shop_description.getText().toString();
            shopData.shop_contacts = new_shop_contacts.getText().toString();
            shopData.shop_mobile = new_shop_phone.getText().toString();
            shopData.shop_tel = new_shop_tel.getText().toString();
            shopData.address = new_real_shop_address.getText().toString();

            WShopManager.getrShopManager().editWShop(this, shopData, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    Intent intent = new Intent();
                    intent.putExtra("shop_data", shopData);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }
            });
        } else {
            shopData.shopname = new_shop_name.getText().toString();
            shopData.description = new_real_shop_description.getText().toString();
            shopData.remark = new_real_shop_description.getText().toString();
            shopData.attn = new_shop_contacts.getText().toString();
            shopData.mobile = new_shop_phone.getText().toString();
            shopData.tel = new_shop_tel.getText().toString();
            shopData.address = new_real_shop_address.getText().toString();

            RealShopManager.getRealShopManager().rEditShopInfo(this, shopData, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    Intent intent = new Intent();
                    intent.putExtra("shop_data", shopData);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    //选择照片
    private void selectPicture() {
        //相机权限
        applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
            @Override
            public void onPermissionResult(boolean b) {
                if (b){
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, PICTURE);
                    Intent intent = new Intent(context, SelectImagesActivity.class);
                    intent.putExtra(SelectImagesActivity.MAX_NUM, 1);
                    startActivityForResult(intent, PICTURE);
                }else {
                    Toast.show(context,"获取不到相关权限，无法进行操作");
                }
            }
        });
    }

    //上传照片
    private void editShopLogo(final String path) {
        showLoading("正在上传头像...");
        if (is_online) {
            final Bitmap bitmap = ImageUtils.getBitmapFromFile(path);
            WShopManager.getrShopManager().updateWShopLogo(this, ImageManage.bitmapToBase64(bitmap), new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }

                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    Toast.show(context, "头像上传成功");
                    new_shop_picture.setImageBitmap(bitmap);
                    setResult(RESULT_OK);
                }

            });
        } else {
            final Bitmap bitmap = ImageUtils.getBitmapFromFile(path);
            RealShopManager.getRealShopManager().upRShoLogo(this, path, new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    Toast.show(context, "头像上传成功");
                    new_shop_picture.setImageBitmap(bitmap);
                    setResult(RESULT_OK);
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(context, msg);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICTURE:
//                    Uri selectedImage = data.getData();
//                    startCropActivity(selectedImage);
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        startCropActivity(getImageContentUri(context,selectedImages.get(0)));
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
                case CODE_TO_BIND_PAY:     //绑定支付
                    forRShopInfo();
                    break;
            }
        }
    }

    /**
     * 地址选择返回
     */
    private void onAddressResult(Intent data){
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            String provinceId = bundle.getString(AddressSelectActivity.KEY_PROVINCE_ID);
            String provinceName = bundle.getString(AddressSelectActivity.KEY_PROVINCE_NAME);
            String cityId = bundle.getString(AddressSelectActivity.KEY_CITY_ID);
            String cityName = bundle.getString(AddressSelectActivity.KEY_CITY_NAME);
            String areaId = bundle.getString(AddressSelectActivity.KEY_AREA_ID);
            String areaName = bundle.getString(AddressSelectActivity.KEY_AREA_NAME);
            if (is_online) {
                shopData.province_id = provinceId;
                shopData.province_name = provinceName;
                shopData.city_id = cityId;
                shopData.city_name = cityName;
                shopData.area_id = areaId;
                shopData.area_name = areaName;
            } else {
                shopData.province = provinceId;
                shopData.provincename = provinceName;
                shopData.city = cityId;
                shopData.cityname = cityName;
                shopData.area = areaId;
                shopData.areaname = areaName;
            }
            new_shop_area.setText(provinceName + " " + cityName + " " + areaName);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropActivity.class)
                .start(RShopInfoActivity.this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri ) {
            String path = ImageCompressTool.getInstance(context).compressImage(resultUri.getEncodedPath(),720,1080,100*1024);
            editShopLogo(path);
        } else {
            Toast.show(context, "无法剪切选择图片");
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.show(context, cropError.getMessage());
        } else {
            Toast.show(context, "无法剪切选择图片");
        }
    }

    /**
     * 绝对路径转URI
     * @param context
     * @param filePath
     * @return
     */
    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
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

    //请求绑定支付信息
    private void requestBindInfo(){
        showLoading();
        WShopManager.getrShopManager().bindPayInfo(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                bindPayData = (BindPayData) result;
                if (bindPayData == null){
                    bind_pay_state.setText("前往绑定支付");
                }else {
                    if (bindPayData.has_wxpay == 1){
                        bind_pay_state.setText("已绑定");
                    }else {
                        bind_pay_state.setText("前往绑定支付");
                    }
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }
        });
    }
}
