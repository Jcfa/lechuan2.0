package com.poso2o.lechuan.fragment.publish;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.SelectGoodsActivity;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.adapter.UploadImageAdapter;
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
import com.poso2o.lechuan.tool.recycler.ItemTouchCallback;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.util.UploadImageAsyncTask;
import com.poso2o.lechuan.util.UserUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.activity.publish.PublishActivity.PUBLISH_TYPE;
import static com.poso2o.lechuan.activity.publish.PublishActivity.SHOW_IMAGE;

/**
 * 晒图
 * Created by Jaydon on 2017/12/1.
 */
public class ShowImageFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 活动请求码
     */
    private static final int REQUEST_PHOTO = 9;// 相册
    private static final int REQUEST_GOODS = 10;// 选商品
    private static final int REQUEST_SHEAR = 11;// 进入剪切页面请求码

    /**
     * 标题
     */
    private EditText show_image_title;

    /**
     * 内容
     */
    private EditText show_image_content;

    /**
     * 图片列表
     */
    private RecyclerView show_image_recycler;
    private UploadImageAdapter uploadImageAdapter;

    /**
     * 商品信息：图片、名称、价格
     */
    private View show_image_goods_info;
    private View show_image_goods_tag;
    private TextView show_image_goods_next;
    private ImageView show_image_goods_icon;
    private TextView show_image_goods_name;
    private TextView show_image_goods_price;

    /**
     * 红包数据
     */
    private TextView show_image_redbag_tag;
    private TextView show_image_redbag_next;

    /**
     * 添加红包模块管理
     */
    private AddRedbagManager addRedbagManager;

    /**
     * 图片数据
     */
    private ArrayList<String> images;

    /**
     * 保存链接地址
     */
    private ArrayList<String> urls;

    /**
     * 是否上传图片结束
     */
    private boolean isUploadImageFinish = true;

    /**
     * 发布资讯数据
     */
    private News news;

    /**
     * 红包数据
     */
    private Redbag redbag;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_image, container, false);
    }

    @Override
    public void initView() {
        show_image_title = findView(R.id.show_image_title);
        show_image_content = findView(R.id.show_image_content);
        show_image_recycler = findView(R.id.show_image_recycler);
        show_image_recycler.setLayoutManager(new GridLayoutManager(context, 4));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback() {

            @Override
            public void movePhotoPosition(int fromPosition, int toPosition) {
                Collections.swap(urls, fromPosition, toPosition);
                Collections.swap(images, fromPosition, toPosition);
                // 改变显示排序
                uploadImageAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void moveFinish() {
                uploadImageAdapter.notifyDataSetChanged(images);
            }
        });
        itemTouchHelper.attachToRecyclerView(show_image_recycler);
        show_image_goods_info = findView(R.id.show_image_goods_info);
        show_image_goods_tag = findView(R.id.show_image_goods_tag);
        show_image_goods_next = findView(R.id.show_image_goods_next);
        show_image_goods_icon = findView(R.id.show_image_goods_icon);
        show_image_goods_name = findView(R.id.show_image_goods_name);
        show_image_goods_price = findView(R.id.show_image_goods_price);
        show_image_redbag_tag = findView(R.id.show_image_redbag_tag);
        show_image_redbag_next = findView(R.id.show_image_redbag_next);
    }

    @Override
    public void initData() {
        news = new News();
        images = new ArrayList<>();
        urls = new ArrayList<>();
        uploadImageAdapter = new UploadImageAdapter(context, images);
        show_image_recycler.setAdapter(uploadImageAdapter);
    }

    @Override
    public void initListener() {
        uploadImageAdapter.setOnCallbackListener(new UploadImageAdapter.OnCallbackListener() {

            @Override
            public void openPhoto() {
                if (applyForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    toSelectImagesActivity();
                }
            }

            @Override
            public void deleteItem(String item, int position) {
                if (isUploadImageFinish) {
                    images.remove(position);
                    urls.remove(position);
                    uploadImageAdapter.notifyDataSetChanged(images);
                } else {
                    Toast.show(getActivity(), "正在上传图片，无法进行此操作");
                }
            }

            @Override
            public void shearImage(int position) {
                if (isUploadImageFinish) {
                    shearPhoto(position);
                } else {
                    Toast.show(getActivity(), "正在上传图片，无法进行此操作");
                }
            }
        });

        findView(R.id.show_image_goods).setOnClickListener(this);
        findView(R.id.show_image_redbag).setOnClickListener(this);
        show_image_goods_next.setOnClickListener(this);
        show_image_redbag_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_image_goods_next:// 取消选择商品
                if (news.has_goods == 1) {
                    show_image_goods_info.setVisibility(GONE);
                    show_image_goods_tag.setVisibility(VISIBLE);
                    show_image_goods_next.setText(R.string.add);
                    show_image_goods_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_arrow, 0);
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

            case R.id.show_image_goods:
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

            case R.id.show_image_redbag_next:// 取消红包
                if (news.has_red_envelopes == 1) {
                    show_image_redbag_tag.setText(R.string.redbag);
                    show_image_redbag_tag.setTextColor(getColorValue(R.color.textBlack));
                    show_image_redbag_next.setText(R.string.add);
                    show_image_redbag_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_arrow, 0);
                    news.has_red_envelopes = 0;
                    news.red_envelopes_amount = 0;
                    news.red_envelopes_number = 0;
                    redbag = null;
                    break;
                }

            case R.id.show_image_redbag:
                if (addRedbagManager == null) {
                    addRedbagManager = new AddRedbagManager(getActivity());
                }
                addRedbagManager.show(redbag, new AddRedbagFragment.Callback() {
                    @Override
                    public boolean callback(Redbag redbag, boolean isSend) {
                        ShowImageFragment.this.redbag = redbag;
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

    /**
     * 发布
     */
    public void publish() {
        if (addRedbagManager != null && addRedbagManager.isShow()) {
            if (!addRedbagManager.confirm()) {
                return;
            }
        }
        String title = show_image_title.getText().toString();
        String content = show_image_content.getText().toString();
        if (TextUtil.isEmpty(title)) {
            Toast.show(context, R.string.toast_title_not_null);
            return;
        }
        news.news_title = title;
        if (TextUtil.isEmpty(content)) {
            Toast.show(context, R.string.toast_content_not_null);
            return;
        }
        news.news_describe = content;
        news.articles = content;
        if (urls.size() < 1) {
            Toast.show(context, R.string.toast_select_image);
            return;
        }
        news.news_img = urls.get(0);
        news.news_imgs = new Gson().toJson(urls);
        if (isUploadImageFinish) {
            addNews();
        } else {
            CommonDialog dialog = new CommonDialog(context, R.string.dialog_upload_image_unfinished);
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
                        SharedPreferencesUtils.put(PUBLISH_TYPE, SHOW_IMAGE);
                        getActivity().setResult(RESULT_OK);
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onFail(String failMsg) {
                Toast.show(context, failMsg);
            }
        });
    }

    /**
     * 剪切图片
     */
    private int shearPosition = -1;

    public void shearPhoto(int position) {
        shearPosition = position;
        File tempFile = new File(images.get(position));
        ImageManage.startPhotoZoom(this, Uri.fromFile(tempFile), REQUEST_SHEAR);
    }

    public void hideRedbagManager() {
        if (addRedbagManager != null) {
            addRedbagManager.hide(true);
        }
    }

    public boolean isRedbagManagerShow() {
        return addRedbagManager != null && addRedbagManager.isShow();
    }

    @Override
    protected void onRequestPermissionsResult(boolean isSucceed) {
        if (isSucceed) {
            toSelectImagesActivity();
        }
    }

    private void toSelectImagesActivity() {
        Intent intent = new Intent(context, SelectImagesActivity.class);
        intent.putExtra(SelectImagesActivity.MAX_NUM, 8 - images.size());
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * 内容不为空
     *
     * @return
     */
    public boolean isContentNotNull() {
        if (TextUtil.isNotEmpty(show_image_title.getText().toString())) {
            return true;
        }
        if (TextUtil.isNotEmpty(show_image_content.getText().toString())) {
            return true;
        }
        if (ListUtils.isNotEmpty(urls)) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_PHOTO:// 选择图片
                    ArrayList<String> selectedImages = data.getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
                    if (selectedImages != null && selectedImages.size() > 0) {
                        compressImages(selectedImages);
                    }
                    break;
                case REQUEST_GOODS:// 选择商品
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
                case REQUEST_SHEAR:// 裁剪图片
                    Bitmap photo = data.getParcelableExtra("data");
                    String path = ImageManage.saveBitmap(context, photo);
                    images.set(shearPosition, path);
                    showLoading(R.string.loading_upload_image);
                    isUploadImageFinish = false;
                    uploadImage(path);
                    uploadImageAdapter.notifyDataSetChanged(images);
                    break;
            }
        }
    }

    /**
     * 加载并压缩图片
     */
    private void compressImages(final ArrayList<String> selectedImages) {
        showLoading(R.string.loading_images);
        final ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            urls.add(null);
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
                    images.addAll(paths);
                    uploadImageAdapter.notifyDataSetChanged(images);
                } else if (result > 0) {
                    setLoadingMessage("正在压缩图片(" + result.toString() + "/" + paths.size() + ")");
                }
            }
        });
        task.execute();
    }

    /**
     * 上传单张图片
     */
    private void uploadImage(final String path) {
        UploadImageAsyncTask asyncTask = new UploadImageAsyncTask(context, path, new UploadImageAsyncTask.AsyncTaskCallback() {

            @Override
            public void action(String url, String type) {
                urls.set(shearPosition, url);
                Toast.show(context, "上传图片成功");
                shearPosition = -1;
                isUploadImageFinish = true;
                dismissLoading();
            }

            @Override
            public void fail() {
                Toast.show(context, "上传图片失败");
                shearPosition = -1;
                isUploadImageFinish = true;
                dismissLoading();
            }
        });
        asyncTask.execute();
    }

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
                urls.set(index, url);
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

    /**
     * 刷新商品信息
     *
     * @param main_picture
     * @param goods_name
     * @param goods_price_section
     */
    private void refreshGoodsInfo(String main_picture, String goods_name, String goods_price_section) {
        show_image_goods_info.setVisibility(VISIBLE);
        show_image_goods_tag.setVisibility(GONE);
        Glide.with(this).load(main_picture).into(show_image_goods_icon);
        show_image_goods_next.setText(R.string.cancel);
        show_image_goods_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.size_color_delete_icon, 0);
        show_image_goods_name.setText(TextUtil.trimToEmpty(goods_name));
        show_image_goods_price.setText(TextUtil.trimToEmpty(goods_price_section));
    }

    /**
     * 刷新红包信息
     *
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
        show_image_redbag_tag.setText(text);
        show_image_redbag_tag.setTextColor(getColorValue(R.color.textGray));
        show_image_redbag_next.setText(R.string.cancel);
        show_image_redbag_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.size_color_delete_icon, 0);
    }

}
