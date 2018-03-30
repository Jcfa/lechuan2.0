package com.poso2o.lechuan.activity.vdian;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.adapter.CaseViewPagerAdapter;
import com.poso2o.lechuan.adapter.ViewPagerAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.MerchantItemBean;
import com.poso2o.lechuan.bean.vdian.CasePerformanceDTO;
import com.poso2o.lechuan.bean.vdian.CasePerformancePicDTO;
import com.poso2o.lechuan.bean.vdian.CasePerformanceShopDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.CasePerformanceManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.ResourceSetUtil;
import com.poso2o.lechuan.view.ViewPagerImageView;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 微店公众号论证，精选案例
 * Created by Administrator on 2018-03-24.
 */

public class CasePerformanceActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private CasePerformanceDTO mCasePerformanceDTO = new CasePerformanceDTO();
    private List<View> dotViews = new ArrayList<>();
    private List<View> mViews = new ArrayList<>();
    private ViewPager viewPager;
    private BaseAdapter mCaseAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_case_performance;
    }

    @Override
    protected void initView() {
        setTitle("演示及案例");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        mViews = new ArrayList<>();
        dotViews = new ArrayList<>();
        RecyclerView recyclerView = findView(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mCaseAdapter = new BaseAdapter(activity, mCasePerformanceDTO.shops) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.item_case_performance_shop_view, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                final CasePerformanceShopDTO shopDTO = (CasePerformanceShopDTO) item;
                LinearLayout item_main = holder.getView(R.id.item_main);
                ImageView logo = holder.getView(R.id.case_performance_logo);
//                Glide.with(context).load(shopDTO.logo).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(logo);
                Glide.with(context).load(shopDTO.logo).transform(new GlideCircleTransform(context)).into(logo);
                TextView shopName = holder.getView(R.id.case_performance_name);
                TextView shop_weixin = holder.getView(R.id.case_performance_wx);
                TextView shop_num = holder.getView(R.id.case_performance_num);
                TextView shop_function = holder.getView(R.id.case_performance_function);
                TextView shop_auth = holder.getView(R.id.case_performance_auth);
                shopName.setText(shopDTO.shop_nick);
                shop_weixin.setText("微信号：" + shopDTO.wechar_id);
                shop_num.setText("月发文" + shopDTO.news_num + "篇");
                shop_function.setText(shopDTO.remark);
                shop_auth.setText(shopDTO.shop_name);
                item_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, CasePerformanceDetailActivity.class);
                        intent.putExtra(CasePerformanceDetailActivity.KEY_PICS, shopDTO);
                        activity.startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(mCaseAdapter);
    }

    @Override
    protected void initData() {
        getCasePerformanceList();
    }

    @Override
    protected void initListener() {

    }

    private void initViewPager() {
        final LinearLayout layoutDot = findView(R.id.layout_dot);
        layoutDot.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (CasePerformanceShopDTO shopDTO : mCasePerformanceDTO.shops) {
            mViews.add(inflater.inflate(R.layout.layout_login_pager1, null));
            View dotView = inflater.inflate(R.layout.view_dot, null);
            dotViews.add(dotView);
            layoutDot.addView(dotView);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(mViews);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
//        float leftMargin = mDistance * (position + positionOffset);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDot.getLayoutParams();
//        params.leftMargin = (int) leftMargin;
//        ivDot.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
//        //页面跳转时，设置小圆点的margin
//        float leftMargin = mDistance * position;
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDot.getLayoutParams();
//        params.leftMargin = (int) leftMargin;
//        ivDot.setLayoutParams(params);
        setViewpagerPic(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getCasePerformanceList() {
        showLoading();
        CasePerformanceManager.getInstance().casePerformanceList(activity, new IRequestCallBack<CasePerformanceDTO>() {
            @Override
            public void onResult(int tag, CasePerformanceDTO result) {
                dismissLoading();
                mCasePerformanceDTO = result;
                initViewPager();
                setViewpagerPic(0);
                if (mCasePerformanceDTO != null && mCasePerformanceDTO.shops != null) {
                    mCaseAdapter.notifyDataSetChanged(mCasePerformanceDTO.shops);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
            }
        });
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
            Glide.with(activity).load(mCasePerformanceDTO.main_pics.get(position).url).error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(pic);
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
