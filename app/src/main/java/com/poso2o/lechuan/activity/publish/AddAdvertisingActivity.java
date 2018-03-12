package com.poso2o.lechuan.activity.publish;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.SelectGoodsActivity;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.news.News;
import com.poso2o.lechuan.bean.redbag.Redbag;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.fragment.redbag.AddRedbagFragment;
import com.poso2o.lechuan.fragment.redbag.AddRedbagManager;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.util.UserUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 添加广告
 * Created by Jaydon on 2017/12/2.
 */
public class AddAdvertisingActivity extends BaseActivity implements OnClickListener {

    /**
     * 活动请求码
     */
    private static final int REQUEST_PHOTO = 9;// 相册

    private static final int REQUEST_CAMERA = 10;// 拍照

    private static final int REQUEST_TAILOR = 11;// 裁剪

    private static final int REQUEST_GOODS = 12;// 商品

    private String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";

    /**
     * 封面
     */
    private ImageView add_ad_image;

    /**
     * 封面删除按钮
     */
    private ImageView add_ad_image_delete;

    /**
     * 内容
     */
    private EditText add_ad_content;

    /**
     * 商品信息：图片、名称、价格
     */
    private View add_ad_goods_info;
    private View add_ad_goods_tag;
    private TextView add_ad_goods_next;
    private ImageView add_ad_goods_icon;
    private TextView add_ad_goods_name;
    private TextView add_ad_goods_price;

    /**
     * 红包数据
     */
    private TextView add_ad_redbag_tag;
    private TextView add_ad_redbag_next;

    /**
     * 添加红包模块管理
     */
    private AddRedbagManager addRedbagManager;

    /**
     * 发布资讯数据
     */
    private News news;

    /**
     * 备份数据
     */
    private News backupNews;

    /**
     * 红包数据
     */
    private Redbag redbag;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_advertising;
    }

    @Override
    protected void initView() {
        add_ad_image = findView(R.id.add_ad_image);
        add_ad_image_delete = findView(R.id.add_ad_image_delete);
        add_ad_content = findView(R.id.add_ad_content);
        add_ad_goods_info = findView(R.id.add_ad_goods_info);
        add_ad_goods_tag = findView(R.id.add_ad_goods_tag);
        add_ad_goods_next = findView(R.id.add_ad_goods_next);
        add_ad_goods_icon = findView(R.id.add_ad_goods_icon);
        add_ad_goods_name = findView(R.id.add_ad_goods_name);
        add_ad_goods_price = findView(R.id.add_ad_goods_price);
        add_ad_redbag_tag = findView(R.id.add_ad_redbag_tag);
        add_ad_redbag_next = findView(R.id.add_ad_redbag_next);
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_add_advertising);
        news = (News) getIntent().getSerializableExtra(Constant.DATA);
        backupNews = news.clone();
        if (TextUtil.isNotEmpty(news.news_img_path)) {
            Glide.with(activity).load(news.news_img_path).into(add_ad_image);
        } else if (TextUtil.isNotEmpty(news.news_img)) {
            Glide.with(activity).load(news.news_img).into(add_ad_image);
        }
        add_ad_content.setText(TextUtil.trimToEmpty(news.news_describe));
        if (news.has_goods == 1) {
            refreshGoodsInfo(news.goods_img, news.goods_name, news.goods_price_section);
        }
        if (news.has_red_envelopes == 1) {
            redbag = new Redbag();
            redbag.money = new BigDecimal(news.red_envelopes_amount);
            redbag.number = news.red_envelopes_number;
            refreshRedbagInfo(news.red_envelopes_amount, news.red_envelopes_number, news.has_random_red);
        }
    }

    @Override
    protected void initListener() {
        add_ad_image_delete.setOnClickListener(this);
        findView(R.id.add_ad_confirm).setOnClickListener(this);
        findView(R.id.add_ad_camera).setOnClickListener(this);
        findView(R.id.add_ad_photo).setOnClickListener(this);
        findView(R.id.add_ad_goods).setOnClickListener(this);
        findView(R.id.add_ad_redbag).setOnClickListener(this);
        add_ad_goods_next.setOnClickListener(this);
        add_ad_redbag_next.setOnClickListener(this);

        add_ad_content.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                news.news_describe = s;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_ad_confirm:// 确定
                if (addRedbagManager != null && addRedbagManager.isShow()) {
                    addRedbagManager.confirm();
                } else {
                    if (TextUtil.isEmpty(news.news_img)) {
                        Toast.show(activity, "请添加广告封面");
                        return;
                    }
                    if (TextUtils.isEmpty(news.news_describe)) {
                        Toast.show(activity, "广告描述不能为空");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra(Constant.DATA, news);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            case R.id.add_ad_image_delete:// 删除封面
                add_ad_image_delete.setVisibility(GONE);
                add_ad_image.setImageResource(0);
                news.news_img = "";
                news.news_img_path = "";
                break;

            case R.id.add_ad_camera:// 相机
                // 判断拍照权限
                if (applyForPermission(Manifest.permission.CAMERA, REQUEST_CAMERA)) {
                    catchPicture();
                }
                break;

            case R.id.add_ad_photo:// 相册
                if (applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PHOTO)) {
                    toSelectImagesActivity();
                }
                break;

            case R.id.add_ad_goods_next:// 取消选择商品
                if (news.has_goods == 1) {
                    add_ad_goods_info.setVisibility(GONE);
                    add_ad_goods_tag.setVisibility(VISIBLE);
                    add_ad_goods_next.setText(R.string.add);
                    add_ad_goods_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_arrow, 0);
                    news.goods_id = "";
                    news.goods_name = "";
                    news.goods_price_section = "";
                    news.goods_price = 0;
                    news.goods_img = "";
                    news.goods_url = "";
                    news.goods_commission_rate = 100;
                    news.goods_commission_amount = 0;
                    news.has_goods = 0;
                    break;
                }

            case R.id.add_ad_goods:// 商品
                if (UserUtils.isNotCommission()) {
                    UserUtils.showCommissionSettingDialog(this, new UserUtils.OnCommissionSettingListener() {

                        @Override
                        public void onResult(boolean isSucceed) {
                            if (isSucceed) {
                                startActivityForResult(SelectGoodsActivity.class, REQUEST_GOODS);
                            }
                        }
                    });
                } else {
                    startActivityForResult(SelectGoodsActivity.class, REQUEST_GOODS);
                }
                break;

            case R.id.add_ad_redbag_next:// 取消红包
                if (news.has_red_envelopes == 1) {
                    add_ad_redbag_tag.setText(R.string.redbag);
                    add_ad_redbag_tag.setTextColor(getColorValue(R.color.textBlack));
                    add_ad_redbag_next.setText(R.string.add);
                    add_ad_redbag_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_arrow, 0);
                    news.has_red_envelopes = 0;
                    news.red_envelopes_amount = 0;
                    news.red_envelopes_number = 0;
                    redbag = null;
                    break;
                }

            case R.id.add_ad_redbag:// 红包
                if (addRedbagManager == null) {
                    addRedbagManager = new AddRedbagManager(this);
                }
                addRedbagManager.show(redbag, new AddRedbagFragment.Callback() {

                    @Override
                    public boolean callback(Redbag redbag, boolean isSend) {
                        AddAdvertisingActivity.this.redbag = redbag;
                        news.has_red_envelopes = 1;
                        news.red_envelopes_amount = redbag.money.doubleValue();
                        news.red_envelopes_number = redbag.number;
                        news.has_random_red = redbag.has_random_red;
                        refreshRedbagInfo(news.red_envelopes_amount, news.red_envelopes_number, news.has_random_red);
                        return true;
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (addRedbagManager != null && addRedbagManager.isShow()) {
            addRedbagManager.hide(true);
        } else if (isUpdate()) {
            showAbandonEditDialog();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示放弃编辑对话框
     */
    private void showAbandonEditDialog() {
        CommonDialog dialog = new CommonDialog(this, R.string.dialog_abandon_edit_content);
        dialog.show(new CommonDialog.OnConfirmListener() {

            @Override
            public void onConfirm() {
                finish();
            }
        });
    }

    /**
     * 是否修改过数据
     * @return
     */
    private boolean isUpdate() {
        if (!TextUtil.equals(news.news_img, backupNews.news_img)) {
            return true;
        }
        if (!TextUtil.equals(news.news_describe, backupNews.news_describe)) {
            return true;
        }
        if (!TextUtil.equals(news.goods_id, backupNews.goods_id)) {
            return true;
        }
        if (news.red_envelopes_amount != backupNews.red_envelopes_amount) {
            return true;
        }
        if (news.red_envelopes_number != backupNews.red_envelopes_number) {
            return true;
        }
        if (news.has_random_red != backupNews.has_random_red) {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CAMERA) {
                catchPicture();
            } else if (requestCode == REQUEST_PHOTO) {
                toSelectImagesActivity();
            }
        }
    }

    /**
     * 进入相册
     */
    private void toSelectImagesActivity() {
        Intent intent = new Intent(this, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, 1);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * 拍照
     */
    private void catchPicture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        startActivityForResult(intentFromCapture, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.show(activity, R.string.toast_sd_error);
                        return;
                    }
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    ImageManage.startPhotoZoom(this, Uri.fromFile(tempFile), REQUEST_TAILOR);
                    break;

                case REQUEST_TAILOR:
                    Bitmap photo = data.getParcelableExtra(Constant.DATA);
                    String path = ImageManage.saveBitmap(activity, photo);
                    ArrayList<String> paths = new ArrayList<>();
                    paths.add(path);
                    compressImages(paths);
                    break;

                case REQUEST_PHOTO:
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        compressImages(selectedImages);
                    }
                    break;

                case REQUEST_GOODS:
                    Goods goods = (Goods) data.getSerializableExtra(Constant.DATA);
                    news.goods_id = goods.goods_id;
                    news.goods_name = goods.goods_name;
                    news.goods_price_section = goods.goods_price_section;
                    news.goods_price = goods.commission_price;
                    news.goods_img = goods.main_picture;
                    news.goods_url = goods.share_url;
                    news.goods_commission_rate = goods.commission_rate;
                    news.goods_commission_amount = goods.commission_amount;
                    news.has_goods = 1;

                    refreshGoodsInfo(goods.main_picture, goods.goods_name, goods.goods_price_section);
                    break;
            }
        }
    }

    /**
     * 刷新商品信息
     * @param main_picture
     * @param goods_name
     * @param goods_price_section
     */
    private void refreshGoodsInfo(String main_picture, String goods_name, String goods_price_section) {
        add_ad_goods_info.setVisibility(VISIBLE);
        add_ad_goods_tag.setVisibility(GONE);
        Glide.with(this).load(main_picture).into(add_ad_goods_icon);
        add_ad_goods_next.setText(R.string.cancel);
        add_ad_goods_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.size_color_delete_icon, 0);
        add_ad_goods_name.setText(TextUtil.trimToEmpty(goods_name));
        add_ad_goods_price.setText(TextUtil.trimToEmpty(goods_price_section));
    }

    /**
     * 刷新红包信息
     * @param red_envelopes_amount
     * @param red_envelopes_number
     */
    private void refreshRedbagInfo(double red_envelopes_amount, int red_envelopes_number, int has_random_red) {
        String text;
        if (has_random_red == 0) {
            text = getString(R.string.add_redbag_random_number_money);
        } else {
            text = getString(R.string.add_redbag_number_money);
        }
        text = String.format(text, red_envelopes_amount, red_envelopes_number);
        add_ad_redbag_tag.setText(text);
        add_ad_redbag_tag.setTextColor(getColorValue(R.color.textGray));
        add_ad_redbag_next.setText(R.string.cancel);
        add_ad_redbag_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.size_color_delete_icon, 0);
    }

    /**
     * 加载并压缩图片
     */
    private void compressImages(final ArrayList<String> selectedImages) {
        final ArrayList<String> urls = new ArrayList<>();
//        urls.add(FileUtils.getNewImagePath());
        final ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            urls.add(null);
            if (selectedImages.get(i).toLowerCase().contains(".gif")) {
                paths.add(selectedImages.get(i)); //如果是gif图就直接用原图
            } else {
                paths.add(FileUtils.getNewImagePath2(selectedImages.get(i)));
            }
        }
        showLoading(R.string.loading_compress_image);
        CompressImageAsyncTask task = new CompressImageAsyncTask(selectedImages, paths, new CompressImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(Integer result) {
                if (result == null) {
                    Toast.show(activity, "压缩图片失败");
                    dismissLoading();
                } else if (result == -1) {
                    news.news_img_path = paths.get(0);
                    uploadImage(paths.get(0));
                    Glide.with(activity).load(paths.get(0)).into(add_ad_image);
                    add_ad_image_delete.setVisibility(VISIBLE);
                }
            }
        });
        task.execute();
    }

    /**
     * 单张图片上传
     */
    private void uploadImage(final String path) {
        showLoading(R.string.loading_upload_image);
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(activity, path, new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                dismissLoading();
                news.news_img = url;
                Toast.show(activity,"上传图片成功");
            }

            @Override
            public void fail() {
                dismissLoading();
                Toast.show(activity,"上传图片失败");
            }
        });
        asyncTask.execute();
    }
}
