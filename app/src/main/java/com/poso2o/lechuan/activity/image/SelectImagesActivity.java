package com.poso2o.lechuan.activity.image;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.CommonAdapter;
import com.poso2o.lechuan.adapter.ViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.image.ImageFolder;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.fragment.image.ListImageDirFragment;
import com.poso2o.lechuan.fragment.image.ListImageDirManager;
import com.poso2o.lechuan.util.ImageManage;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SelectImagesActivity extends BaseActivity implements ListImageDirFragment.OnImageDirSelected, OnClickListener {

    /**
     * 最大可选图片数量
     */
    public static final String MAX_NUM = "max_num";

    /**
     * 选中的图片集合
     */
    public static final String SELECTED_IMAGES = "selected_images";

    private String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";

    private final int REQUEST_CAMERA = 1;

    private final int REQUEST_TAILOR = 2;

    private final int REQUEST_PREVIEW = 3;

    /**
     * 已选图片数量
     */
    private TextView select_image_title;

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public ArrayList<String> selectedImages = new ArrayList<>();

    /**
     * 列表对象
     */
    private GridView select_image_grid;

    /**
     * 列表适配器
     */
    private CommonAdapter<String> adapter;

    /**
     * 当前页面最后一个item的下标
     */
    private int lastPosition;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> dirPaths = new HashSet<>();
    private HashMap<String, ImageFolder> dirNames = new HashMap<>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private ArrayList<ImageFolder> imageFolders = new ArrayList<>();

    /**
     * 状态栏上面的文本
     */
    private TextView select_image_dir;

    /**
     * 相片数量 TODO 暂时隐藏
     */
//    private TextView select_image_count;

    /**
     * 预览
     */
    private TextView select_image_preview;

    /**
     * 拍照
     */
    private TextView select_image_camera;

    /**
     * 已选图片数量
     */
//    private int totalCount = 0;

    /**
     * 最大可选数量
     */
    private int maxNum;

    /**
     * 相册目录管理器
     */
    private ListImageDirManager listImageDirManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_selector_image;
    }

    /**
     * 初始化View
     */
    public void initView() {
        listImageDirManager = new ListImageDirManager(this);

        select_image_grid = findView(R.id.select_image_grid);
        select_image_title = findView(R.id.select_image_title);
        select_image_dir = findView(R.id.select_image_dir);
//        select_image_count = findView(R.id.select_image_count);
        select_image_preview = findView(R.id.select_image_preview);
        select_image_camera = findView(R.id.select_image_camera);

        select_image_grid.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastPosition = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    /**
     * 当前选中的文件夹
     */
    private ImageFolder selectFolder;

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    public void initData() {
        // 获取最大可选择图片数量
        maxNum = getIntent().getIntExtra(MAX_NUM, 0);
        // 设置标题
        setupTitleNumText();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.show(this, R.string.toast_temporary_not_storage);
            return;
        }
        // 显示加载对话框
        showLoading(R.string.loading_images);
        loadData();
    }


//    private void loadData() {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                long start = System.currentTimeMillis();
//
//                String firstImage = null;
//
//                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                ContentResolver contentResolver = SelectImagesActivity.this.getContentResolver();
//
//                // 只查询jpeg和png的图片
//                Cursor cursor = contentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
//                        MediaStore.Images.Media.DATE_ADDED + " DESC");
//
//                if (cursor == null) {
//                    return;
//                }
//                ImageFolder allImageFolder = new ImageFolder();
//                allImageFolder.setName("全部图片");
//                imageFolders.add(allImageFolder);
//                selectFolder = allImageFolder;
//
//                while (cursor.moveToNext()) {
//                    // 获取图片的路径
//                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//
//                    // 太小的图片不加载
//                    if (ImageManage.isTinyImage(path)) {
//                        continue;
//                    }
//                    allImageFolder.addPath(path);
//
//                    // 拿到第一张图片的路径
//                    if (firstImage == null) {
//                        allImageFolder.setFirstImagePath(path);
//                        firstImage = path;
//                    }
//                    // 获取该图片的父路径名
//                    File parentFile = new File(path).getParentFile();
//                    if (parentFile == null) {
//                        continue;
//                    }
//                    String dirPath = parentFile.getAbsolutePath();
//                    ImageFolder imageFolder;
//                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
//                    if (dirPaths.contains(dirPath)) {
//                        continue;
//                    } else {
//                        dirPaths.add(dirPath);
//                        // 初始化imageFolder
//                        imageFolder = new ImageFolder();
//                        imageFolder.setDir(dirPath);
//                        imageFolder.setFirstImagePath(path);
//                    }
//                    String[] fileNames = parentFile.list(new FilenameFilter() {
//
//                        @Override
//                        public boolean accept(File dir, String name) {
//                            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg");
//                        }
//                    });
//                    // 填充路径集合
//                    for (int i = fileNames.length - 1; i >= 0; i--) {
//                        String name = fileNames[i];
//                        imageFolder.addPath(parentFile.getAbsolutePath() + "/" + name);
//                    }
//                    int picSize = fileNames.length;
////                    totalCount += picSize;
//
//                    imageFolder.setCount(picSize);
//                    imageFolders.add(imageFolder);
//                }
//                cursor.close();
//
//                // 扫描完成，辅助的HashSet也就可以释放内存了
//                dirPaths = null;
//
//                LogUtils.i("原来的加载方式用时 = " + (System.currentTimeMillis() - start));
//
//                // 通知主线程扫描图片完成
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        dismissLoading();
//                        // 为View绑定数据
//                        data2View();
//                    }
//                });
//            }
//        }).start();
//    }

    private void loadData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = SelectImagesActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor cursor = contentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "+MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png", "image/gif"},
                        MediaStore.Images.Media.DATE_ADDED + " DESC");

                if (cursor == null) {
                    return;
                }
                ImageFolder allImageFolder = new ImageFolder();
                allImageFolder.setName("全部图片");
                imageFolders.add(allImageFolder);
                selectFolder = allImageFolder;

                while (cursor.moveToNext()) {
                    // 获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    // 太小的图片不加载
                    if (ImageManage.isTinyImage(path)) {
                        continue;
                    }
                    allImageFolder.addPath(path);

                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();

                    ImageFolder imageFolder;
                    if (dirNames.containsKey(dirPath)) {
                        imageFolder = dirNames.get(dirPath);
                    } else {
                        // 初始化imageFolder
                        imageFolder = new ImageFolder();
                        imageFolder.setDir(dirPath);

                        dirNames.put(dirPath, imageFolder);
                        imageFolders.add(imageFolder);
                    }
                    imageFolder.addPath(path);
//                    totalCount += picSize;

                }
                cursor.close();

                // 扫描完成，辅助的HashMap也就可以释放内存了
                dirNames = null;

                // 通知主线程扫描图片完成
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dismissLoading();
                        // 为View绑定数据
                        data2View();
                    }
                });
            }
        }).start();
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (selectFolder == null || selectFolder.getImagePaths().size() == 0) {
            Toast.show(activity, R.string.toast_not_scan_image);
            return;
        }
        select_image_dir.setText(selectFolder.getName());
        // 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        initAdapter();
        select_image_grid.setAdapter(adapter);
//        select_image_count.setText(totalCount + "张");
    }

    /**
     * 设置标题上的数量文本
     */
    private void setupTitleNumText() {
        String text = getString(R.string.selector_image_num);
        text = String.format(text, selectedImages.size(), maxNum);
        SpannableStringBuilder ssb = new SpannableStringBuilder("选择图片 " + text);
        ssb.setSpan(new ForegroundColorSpan(0xffb94f55), 5, 5 + text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        select_image_title.setText(ssb);
    }

    @Override
    public void selectFolder(ImageFolder folder) {
        selectFolder = folder;
        // 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        adapter.notifyDataSetChanged(selectFolder.getImagePaths());
//        select_image_count.setText(folder.getCount() + "张");
        select_image_dir.setText(folder.getName());
        listImageDirManager.hide(true);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<String>(getApplicationContext(), selectFolder.getImagePaths(), R.layout.item_selector_image_grid) {

            /**
             * 只刷新标签
             */
            private boolean isRefreshTag = false;

            @Override
            public void convert(int position, final ViewHolder helper, final String item) {
                final ImageView mImageView = helper.getView(R.id.iv_item_image);
                final TextView mSelect = helper.getView(R.id.tv_select_tag);
                if (!isRefreshTag) {
                    // 设置no_pic
                    helper.setImageResource(R.id.iv_item_image, R.mipmap.pictures_no);
                    // 设置图片
                    helper.setImageByUrl(R.id.iv_item_image, item);

                    // 设置ImageView的点击事件
                    mImageView.setOnClickListener(new OnClickListener() {

                        // 选择，则将图片变暗，反之则反之
                        @Override
                        public void onClick(View v) {
                            // 已经选择过该图片
                            int index = getIndex(item);
                            if (index > -1) {
                                selectedImages.remove(index);
                            } else {
                                // 未选择该图片
                                if (selectedImages.size() < maxNum) {
                                    selectedImages.add(item);
                                }
                            }
                            setupTitleNumText();
                            // 只刷新图片的标签
                            isRefreshTag = true;
                            notifyDataSetChanged();
                        }
                    });
                }
                // 已经选择过的图片，显示出选择过的效果
                int index = getIndex(item);
                if (index > -1) {
                    mSelect.setVisibility(View.VISIBLE);
                    mSelect.setText(index + 1 + "");
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                } else {
                    mSelect.setVisibility(View.GONE);
                    mImageView.setColorFilter(null);
                }
                // 如果刷新到当前页最后一条，则重置为刷新整张图片
                if (position == lastPosition) {
                    isRefreshTag = false;
                }
            }

            /**
             * item是否被选中，如果选中返回相应下标
             */
            private int getIndex(String item) {
                for (int i = 0; i < selectedImages.size(); i++) {
                    if (TextUtil.equals(selectedImages.get(i), item)) {
                        return i;
                    }
                }
                return -1;
            }
        };
    }

    @Override
    public void initListener() {
        select_image_preview.setOnClickListener(this);
        select_image_camera.setOnClickListener(this);
        findView(R.id.select_image_back).setOnClickListener(this);
        findView(R.id.select_image_confirm).setOnClickListener(this);
        findView(R.id.select_image_dir).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_image_back:// 返回
                finish();
                break;

            case R.id.select_image_confirm:// 完成
                Intent intent = new Intent();
                intent.putStringArrayListExtra(SELECTED_IMAGES, selectedImages);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.select_image_dir:// 目录
                if (listImageDirManager.isShow()) {
                    listImageDirManager.hide(true);
                } else {
                    listImageDirManager.show(imageFolders, this);
                }
                break;

            case R.id.select_image_preview:// 预览
                if (ListUtils.isNotEmpty(selectedImages)) {
                    Bundle data = new Bundle();
                    data.putStringArrayList(SELECTED_IMAGES, selectedImages);
                    startActivityForResult(ImagePreviewActivity.class, data, REQUEST_PREVIEW);
                } else {
                    Toast.show(this, "请先选择图片");
                }
                break;

            case R.id.select_image_camera:// 拍照
                // 判断拍照权限
                if (applyForPermission(Manifest.permission.CAMERA)) {
                    catchPicture();
                }
                break;
        }
    }

    @Override
    protected void onRequestPermissionsResult(boolean isSucceed) {
        super.onRequestPermissionsResult(isSucceed);
        if (isSucceed) {
            catchPicture();
        }
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
                        Toast.show(activity, "内存卡异常，请检查内存卡插入是否正确");
                        return;
                    }
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    ArrayList<String> selectedImages = new ArrayList<>();
                    selectedImages.add(tempFile.getAbsolutePath());
                    Intent intent = new Intent();
                    intent.putExtra(SELECTED_IMAGES, selectedImages);
                    setResult(RESULT_OK, intent);
                    finish();
//                    ImageManage.startPhotoZoom(this, Uri.fromFile(tempFile), REQUEST_TAILOR);
                    break;

                case REQUEST_TAILOR:
//                    Bitmap photo = data.getParcelableExtra("data");
//                    String path = ImageManage.saveBitmap(activity, photo);
//                    ArrayList<String> selectedImages = new ArrayList<>();
//                    selectedImages.add(path);
//                    Intent intent = new Intent();
//                    intent.putExtra(SELECTED_IMAGES, selectedImages);
//                    setResult(RESULT_OK, intent);
//                    finish();
                    break;

                case REQUEST_PREVIEW:
                    ArrayList<String> paths = data.getStringArrayListExtra(Constant.DATA);
                    ArrayList<String> removes = new ArrayList<>();
                    if (ListUtils.isNotEmpty(paths)) {
                        for (String p : paths) {
                            for (String selectPath : this.selectedImages) {
                                if (TextUtils.equals(p, selectPath)) {
                                    removes.add(selectPath);
                                }
                            }
                        }
                        this.selectedImages.removeAll(removes);
                        adapter.notifyDataSetChanged();
                        setupTitleNumText();
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (listImageDirManager.isShow()) {
            listImageDirManager.hide(false);
            return;
        }
        super.onBackPressed();
    }

}
