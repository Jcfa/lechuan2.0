package com.poso2o.lechuan.activity.vdian;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.ViewPagerAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.vdian.CasePerformancePicDTO;
import com.poso2o.lechuan.bean.vdian.CasePerformanceShopDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.view.ViewPagerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-26.
 */

public class CasePerformanceDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String KEY_PICS = "pics";
    private List<View> dotViews = new ArrayList<>();
    private List<View> mViews = new ArrayList<>();
    //    private ImageView ivDot;//白色圆点
//    private float mDistance;
    private ViewPager viewPager;
    private CasePerformanceShopDTO casePerformanceShopDTO = new CasePerformanceShopDTO();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_case_performance_detail;
    }

    @Override
    protected void initView() {
        casePerformanceShopDTO = (CasePerformanceShopDTO) getIntent().getSerializableExtra(KEY_PICS);
        final LinearLayout layoutDot = findView(R.id.layout_dot);
        layoutDot.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        mViews = new ArrayList<>();
        dotViews = new ArrayList<>();
        for (CasePerformancePicDTO picDTO : casePerformanceShopDTO.pics) {
            mViews.add(inflater.inflate(R.layout.layout_login_pager1, null));
            View dotView = inflater.inflate(R.layout.view_dot, null);
            dotViews.add(dotView);
            layoutDot.addView(dotView);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(mViews);
        viewPager.setAdapter(adapter);
        setTitle(casePerformanceShopDTO.shop_nick);
        ImageView titleLogo = findView(R.id.iv_title_logo);
        Glide.with(activity).load(casePerformanceShopDTO.logo).transform(new GlideCircleTransform(activity)).into(titleLogo);
    }

    @Override
    protected void initData() {
        setViewpagerPic(0);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
//        float leftMargin = mDistance * (position + positionOffset);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivDot.getLayoutParams();
//        params.leftMargin = (int) leftMargin;
//        ivDot.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        //页面跳转时，设置小圆点的margin
//        float leftMargin = mDistance * position;
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivDot.getLayoutParams();
//        params.leftMargin = (int) leftMargin;
//        ivDot.setLayoutParams(params);
        setViewpagerPic(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setViewpagerPic(int position) {
        for (int i = 0; i < dotViews.size(); i++) {
            ImageView imageView = (ImageView) dotViews.get(i).findViewById(R.id.iv_dot);
            if (i == position) {
                imageView.setImageResource(R.mipmap.white_dot);
            } else {
                imageView.setImageResource(R.mipmap.black_dot);
            }
        }
        View view = mViews.get(position);
        ImageView pic = (ImageView) view.findViewById(R.id.pic);
        try {
            Glide.with(activity).load(casePerformanceShopDTO.pics.get(position).url).error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(pic);
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
