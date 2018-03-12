package com.poso2o.lechuan.fragment.publish;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.SelectGoodsActivity;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.news.News;
import com.poso2o.lechuan.bean.redbag.Redbag;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.dialog.PublishSucceedDialog;
import com.poso2o.lechuan.fragment.redbag.AddRedbagFragment;
import com.poso2o.lechuan.fragment.redbag.AddRedbagManager;
import com.poso2o.lechuan.manager.news.NewsManager;
import com.poso2o.lechuan.manager.rshopmanager.CompressImageAsyncTask;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.LogUtils;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UUIDUtils;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.util.UserUtils;
import com.poso2o.lechuan.view.rich.DataImageView;
import com.poso2o.lechuan.view.rich.RichTextEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.activity.publish.PublishActivity.IMAGE_TEXT;
import static com.poso2o.lechuan.activity.publish.PublishActivity.PUBLISH_TYPE;

/**
 * 图文发布
 * Created by Jaydon on 2017/12/1.
 */
public class ImageTextFragment extends BaseFragment implements OnClickListener {

    /**
     * 活动请求码
     */
    private static final int REQUEST_CAMERA = 8;// 拍照
    private static final int REQUEST_PHOTO = 9;// 相册
    private static final int REQUEST_GOODS = 10;// 选商品
    private static final int REQUEST_SHEAR = 11;// 进入剪切页面请求码

    /**
     * 照相机拍照得到的图片
     */
    private File mCameraImageFile;

    /**
     * 添加图片横条
     */
    private LinearLayout image_text_add_image;

    /**
     * 正文编辑器
     */
    private RichTextEditor image_text_rich_text;

    /**
     * 可滚动布局
     */
    private ScrollView image_text_content;

    /**
     * 文章标题
     */
    private EditText image_text_title;

    /**
     * 商品和红包布局
     */
    private View image_text_goods_info;// 商品
    private ImageView image_text_goods_icon;// 商品图片
    private TextView image_text_goods_name;// 商品名称
    private TextView image_text_goods_price;// 商品价格
    private ImageView image_text_goods_cancel;// 商品价格
    private View image_text_redbag_info;// 红包
    private TextView image_text_redbag_tag;// 红包标签
    private ImageView image_text_redbag_cancel;// 取消红包

    /**
     * 红包模块管理
     */
    private AddRedbagManager addRedbagManager;
    private Redbag redbag;

    /**
     * 发布资讯数据
     */
    private News news;

    /**
     * 是否上传图片结束
     */
    private boolean isUploadImageFinish = true;

    /**
     * 被点击的图片下标
     */
    private int mClickImagePosition = -1;

    /**
     * 事件处理
     */
    private Handler mHandler = new Handler();

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_text, container, false);
    }

    @Override
    public void initView() {
        image_text_add_image = findView(R.id.image_text_add_image);
        image_text_rich_text = findView(R.id.image_text_rich_text);
        image_text_content = findView(R.id.image_text_content);
        image_text_title = findView(R.id.image_text_title);
        image_text_goods_info = findView(R.id.image_text_goods_info);
        image_text_goods_icon = findView(R.id.image_text_goods_icon);
        image_text_goods_name = findView(R.id.image_text_goods_name);
        image_text_goods_price = findView(R.id.image_text_goods_price);
        image_text_goods_cancel = findView(R.id.image_text_goods_cancel);
        image_text_redbag_info = findView(R.id.image_text_redbag_info);
        image_text_redbag_tag = findView(R.id.image_text_redbag_tag);
        image_text_redbag_cancel = findView(R.id.image_text_redbag_cancel);
        // 动态计算可滚动布局高度以适应各种屏幕
        LayoutParams lp = image_text_content.getLayoutParams();
        lp.height = ScreenInfo.HEIGHT - ScreenInfo.STATUS_BAR_HEIGHT - ScreenInfo.dpCpx(282);
        image_text_content.setLayoutParams(lp);
    }

    @Override
    public void initData() {
        news = new News();
    }

    @Override
    public void initListener() {
        findView(R.id.image_text_photo).setOnClickListener(this);
        findView(R.id.image_text_camera).setOnClickListener(this);
        findView(R.id.image_text_goods).setOnClickListener(this);
        findView(R.id.image_text_goods_info).setOnClickListener(this);
        findView(R.id.image_text_redbag).setOnClickListener(this);
        findView(R.id.image_text_redbag_info).setOnClickListener(this);
        image_text_goods_cancel.setOnClickListener(this);
        image_text_redbag_cancel.setOnClickListener(this);

        // 可滚动布局触摸事件
        image_text_content.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (image_text_rich_text.getHeight() < v.getHeight()) {
                            image_text_rich_text.editTextRequestFocus();
                            image_text_add_image.setVisibility(View.VISIBLE);
                            return false;
                        }
                        break;
                }
                return false;
            }
        });

        // 设置图片点击监听
        image_text_rich_text.setOnClickImageListener(new RichTextEditor.OnClickImageListener() {

            @Override
            public void onClickImage(final int position, final DataImageView view) {
                // 获取可滚动布局在屏幕中底部的位置
                int[] svL = new int[2];
                image_text_content.getLocationOnScreen(svL);
                int svB = image_text_content.getHeight() + svL[1];
                // 获取图片在屏幕中底部的位置
                int[] vL = new int[2];
                view.getLocationOnScreen(vL);
                int vB = view.getHeight() + vL[1];
                // 如果图片底部已经低出滚动布局外，则将图片底部回滚到跟滚动布局底部齐平
                if (vB > svB) {
                    // 获取图片到布局顶部的距离
                    View vp = (View) view.getParent();
                    int top = vp.getTop();
                    // 计算y坐标
                    int y = top + ScreenInfo.dpCpx(48);
                    LogUtils.i("top = " + y);
                    // 滚动到指定位置
                    image_text_content.scrollTo(0, y);
                }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        showUpdateImagePopupWindow(position, view);
                    }
                }, 100);
            }
        });
    }

    public void hideRedbagManager() {
        if (addRedbagManager != null) {
            addRedbagManager.hide(true);
        }
    }

    public boolean isRedbagManagerShow() {
        return addRedbagManager != null && addRedbagManager.isShow();
    }

    /**
     * 内容不为空
     *
     * @return
     */
    public boolean isContentNotNull() {
        if (TextUtil.isNotEmpty(image_text_title.getText().toString())) {
            return true;
        }
        if (image_text_rich_text.isContent()) {
            return true;
        }
        if (news.has_goods == 1) {
            return true;
        }
        if (news.has_red_envelopes == 1) {
            return true;
        }
        return false;
    }

    /**
     * 发布
     */
    public void publish() {
        if (addRedbagManager != null && addRedbagManager.isShow()) {
            if (!addRedbagManager.confirm()) {
                return;
            }
        }
        String title = image_text_title.getText().toString();
        if (TextUtil.isEmpty(title)) {
            Toast.show(context, R.string.toast_title_not_null);
            return;
        }
        if (ListUtils.isEmpty(image_text_rich_text.buildEditData())) {
            Toast.show(context, R.string.toast_body_not_null);
            return;
        }
        news.news_title = title;
        news.articles = getArticles();
        if (isUploadImageFinish) {
            addNews();
        } else {
            CommonDialog dialog = new CommonDialog(context, R.string.dialog_upload_image_unfinished);
            dialog.setOnCancelListener(new CommonDialog.OnCancelListener() {

                @Override
                public void onCancel() {
                    news.news_describe = "";
                    news.news_img = "";
                }
            });
            dialog.show(new CommonDialog.OnConfirmListener() {

                @Override
                public void onConfirm() {
                    addNews();
                }
            });
        }
    }

    /**
     * 新增资讯
     */
    private void addNews() {
        NewsManager.getInstance().add((BaseActivity) context, news, new NewsManager.OnNewsAddCallback() {

            @Override
            public void onSucceed() {
                new PublishSucceedDialog(context).show(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        SharedPreferencesUtils.put(PUBLISH_TYPE, IMAGE_TEXT);
                        getActivity().setResult(RESULT_OK);
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onFail(String failMsg) {
                news.news_describe = "";
                news.news_img = "";
                Toast.show(context, failMsg);
            }
        });
    }

    /**
     * 获取正文内容
     *
     * @return
     */
    private String getArticles() {
        StringBuilder buffer = new StringBuilder();
        List<RichTextEditor.EditData> data = image_text_rich_text.buildEditData();
        for (int i = 0; i < data.size(); i++) {
            RichTextEditor.EditData ed = data.get(i);
            if (i == 0 && TextUtil.isEmpty(ed.inputStr)) {
                buffer.append("</p>");
            } else if (i == data.size() - 1 && TextUtil.isEmpty(ed.inputStr)) {
                buffer.append("<p>");
            } else if (TextUtil.isNotEmpty(ed.inputStr)) {
                if (TextUtil.isEmpty(news.news_describe)) {
                    // 默认第一段文字为文章描述
                    news.news_describe = ed.inputStr;
                }
                if (i == 0) {
                    buffer.append(ed.inputStr).append("</p>");
                } else if (i == data.size() - 1) {
                    buffer.append("<p>").append(ed.inputStr);
                } else {
                    buffer.append("<p>").append(ed.inputStr).append("</p>");
                }
            } else if (TextUtil.isNotEmpty(ed.imageUrl)) {
                if (TextUtil.isEmpty(news.news_img)) {
                    // 默认第一张图片为封面
                    news.news_img = ed.imageUrl;
                }
                buffer.append("<img src='").append(ed.imageUrl).append("'>");
            }
        }
        return buffer.toString();
    }

    /**
     * 显示修改图片按钮
     *
     * @param view
     * @author Zheng Jie Dong
     * @date 2016-11-19
     */
    @SuppressWarnings("deprecation")
    private void showUpdateImagePopupWindow(final int position, View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = View.inflate(getActivity(), R.layout.popup_image_update, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                ScreenInfo.dpCpx(32), true);
        // 设置按钮的点击事件
        contentView.findViewById(R.id.iv_photo).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickImagePosition = position;
                openCamera();
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.iv_select).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickImagePosition = position;
                toSelectImagesActivity();
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");// 相片类型
//                startActivityForResult(intent, Common.REQUEST_CODE_PICK_IMAGE);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.iv_delete).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                image_text_rich_text.removeImage(position);
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));

        // 计算偏移量
        int x = view.getWidth() - ScreenInfo.dpCpx(128);
        if (x < 0) x = 0;
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, x, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_text_goods:
            case R.id.image_text_goods_info:
                if (UserUtils.isNotCommission()) {
                    UserUtils.showCommissionSettingDialog(context, new UserUtils.OnCommissionSettingListener() {

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

            case R.id.image_text_redbag:
            case R.id.image_text_redbag_info:
                if (addRedbagManager == null) {
                    addRedbagManager = new AddRedbagManager(getActivity());
                }
                addRedbagManager.show(redbag, new AddRedbagFragment.Callback() {

                    @Override
                    public boolean callback(Redbag redbag, boolean isSend) {
                        ImageTextFragment.this.redbag = redbag;
                        news.has_red_envelopes = 1;
                        news.red_envelopes_amount = redbag.money.doubleValue();
                        news.red_envelopes_number = redbag.number;
                        news.has_random_red = redbag.has_random_red;
                        refreshView();
                        return true;
                    }
                });
                break;

            case R.id.image_text_photo:
                if (applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PHOTO)) {
                    toSelectImagesActivity();
                }
                break;

            case R.id.image_text_camera:
                // 判断拍照权限
                if (applyForPermission(Manifest.permission.CAMERA, REQUEST_CAMERA)) {
                    openCamera();
                }
                break;

            case R.id.image_text_goods_cancel:// 取消商品
                news.goods_id = "";
                news.goods_name = "";
                news.goods_price_section = "";
                news.goods_price = 0;
                news.goods_img = "";
                news.goods_url = "";
                news.goods_commission_rate = 100;
                news.goods_commission_amount = 0;
                news.has_goods = 0;
                refreshView();
                break;

            case R.id.image_text_redbag_cancel:// 取消红包
                news.has_red_envelopes = 0;
                news.red_envelopes_amount = 0;
                news.red_envelopes_number = 0;
                refreshView();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CAMERA) {
                openCamera();
            } else if (requestCode == REQUEST_PHOTO) {
                toSelectImagesActivity();
            }
        }
    }

    /**
     * 拍照
     */
    public void openCamera() {
        try {
            File PHOTO_DIR = new File(FileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            mCameraImageFile = new File(PHOTO_DIR, UUIDUtils.generate() + ".jpg");// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCameraImageFile);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 进入相册
     */
    private void toSelectImagesActivity() {
        Intent intent = new Intent(context, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, mClickImagePosition == -1 ? 9 : 1);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.show(context, R.string.toast_sd_error);
                        return;
                    }
                    ArrayList<String> paths = new ArrayList<>();
                    paths.add(mCameraImageFile.getAbsolutePath());
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

                    refreshView();
                    break;

                case REQUEST_SHEAR:// 裁剪图片
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Bitmap photo = extras.getParcelable("data");
//                        String path = ImageManage.saveBitmap(context, photo);
//                        images.set(shearPosition, path);
//                        showLoading(R.string.loading_upload_image);
//                        isUploadImageFinish = false;
//                        uploadImage(path);
//                        uploadImageAdapter.notifyDataSetChanged(images);
//                    }
                    break;
            }
        }
    }

    /**
     * 有相关信息则显示，如果没有就隐藏
     */
    private void refreshView() {
        if (news.has_goods == 1) {
            image_text_goods_info.setVisibility(VISIBLE);
            Glide.with(this).load(news.goods_img).into(image_text_goods_icon);
            image_text_goods_name.setText(TextUtil.trimToEmpty(news.goods_name));
            image_text_goods_price.setText(TextUtil.trimToEmpty(news.goods_price_section));
        } else {
            image_text_goods_info.setVisibility(GONE);
        }
        if (news.has_red_envelopes == 1) {
            image_text_redbag_info.setVisibility(VISIBLE);
            String text;
            if (news.has_random_red == 0) {
                text = getString(R.string.add_redbag_random_number_money);
            } else {
                text = getString(R.string.add_redbag_number_money);
            }
            text = String.format(text, news.red_envelopes_amount, news.red_envelopes_number);
            image_text_redbag_tag.setText(text);
        } else {
            image_text_redbag_info.setVisibility(GONE);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                image_text_content.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
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
                    Toast.show(context, "压缩图片失败");
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
     * 批量添加图片的回调函数
     */
    private RichTextEditor.Callback mCallback = new RichTextEditor.Callback() {

        @Override
        public void complete() {
            // 500毫秒后滚动到底部
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    int height = image_text_rich_text.getHeight();
                    if (height < 0) {
                        height = 0;
                    }
                    image_text_content.scrollTo(0, height);
                }
            }, 500);
        }
    };

    /**
     * 记录上传图片下标
     */
    private int index;

    /**
     * 多张图片上传
     *
     * @param paths
     */
    private void uploadImages(final ArrayList<String> paths) {
        setLoadingMessage("正在上传图片(" + (index + 1) + "/" + paths.size() + ")");
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(context, paths.get(index), new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                if (mClickImagePosition == -1) {
                    // 添加图片
                    image_text_rich_text.insertImage(paths.get(index), url, mCallback);
                } else {
                    // 修改图片
                    image_text_rich_text.updateImage(mClickImagePosition, paths.get(index), url);
                    mClickImagePosition = -1;
                    dismissLoading();
                }
                index++;
                if (index < paths.size()) {
                    uploadImages(paths);
                } else {
                    isUploadImageFinish = true;
                    Toast.show(context, "上传图片成功");
                    dismissLoading();
                }
            }

            @Override
            public void fail() {
                isUploadImageFinish = true;
                Toast.show(context, "上传图片失败");
                dismissLoading();
            }
        });
        asyncTask.execute();
    }
}
