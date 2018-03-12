package com.poso2o.lechuan.activity.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.util.ListUtils;

import java.util.ArrayList;

/**
 * 图片预览界面
 * Created by Jaydon on 2017/8/19.
 */
public class ImagePreviewActivity extends BaseActivity {

    private ArrayList<String> paths;

    private ArrayList<String> removePaths;

    private ViewPager images_preview_content;

    private ImageView images_preview_select;

    /**
     * 导航圆点
     */
    private LinearLayout images_preview_navigation;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initView() {
        images_preview_content = findView(R.id.images_preview_content);
        images_preview_navigation = findView(R.id.images_preview_navigation);
        images_preview_select = findView(R.id.images_preview_select);
        images_preview_select.setSelected(true);
    }

    private View selectSpot;

    private int showPosition = 0;

    @Override
    public void initData() {
        removePaths = new ArrayList<>();
        paths = getIntent().getStringArrayListExtra(SelectImagesActivity.SELECTED_IMAGES);
        if (ListUtils.isNotEmpty(paths)) {
            ImagesAdapter adapter = new ImagesAdapter();
            images_preview_content.setAdapter(adapter);
            images_preview_navigation.removeAllViews();
            for (int i = 0; i < paths.size(); i++) {
                View spotView = View.inflate(activity, R.layout.item_image_spot, null);
                ImageView iv = (ImageView) spotView.findViewById(R.id.image_spot);
                iv.setTag(i);
                images_preview_navigation.addView(spotView);
            }
            selectSpot = images_preview_navigation.findViewWithTag(0);
            selectSpot.setSelected(true);
        }
    }

    @Override
    public void initListener() {
        findView(R.id.select_image_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        images_preview_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = paths.get(showPosition);
                if (removePaths.contains(path)) {
                    images_preview_select.setSelected(true);
                    removePaths.remove(path);
                } else {
                    images_preview_select.setSelected(false);
                    removePaths.add(path);
                }
            }
        });
        images_preview_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showPosition = position;
                View v = images_preview_navigation.findViewWithTag(position);
                selectSpot.setSelected(false);
                v.setSelected(true);
                selectSpot = v;
                images_preview_select.setSelected(!removePaths.contains(paths.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Constant.DATA, removePaths);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    class ImagesAdapter extends PagerAdapter {

        private ArrayList<ImageView> imageViews = new ArrayList<>();

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView;
            if (imageViews.size() > position) {
                imageView = imageViews.get(position);
            } else {
                imageView = new ImageView(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageViews.add(imageView);
                Bitmap bitmap = BitmapFactory.decodeFile(paths.get(position));
                imageView.setImageBitmap(bitmap);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (imageViews.size() > position) {
                container.removeView(imageViews.get(position));
            }
        }
    }
}
