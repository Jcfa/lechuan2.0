package com.poso2o.lechuan.activity.wshop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.adapter.ViewPagerAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wshopmanager.ShopServiceManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr zhang on 2017/12/8.
 */

public class LechuanServiceActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Context context;

    //返回
    private ImageView service_back;
    //试用七天
    private TextView use_seven_days;
    //立即购买
    private TextView to_buy_service;

    private List<ImageView> dotViews = new ArrayList<>();
    private ImageView ivDot;//白色圆点
    private float mDistance;
    //剩余使用天数
    private int days;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lechuan_service;
    }

    @Override
    protected void initView() {
        context = this;

        service_back = (ImageView) findViewById(R.id.service_back);
        use_seven_days = (TextView) findViewById(R.id.use_seven_days);
        to_buy_service = (TextView) findViewById(R.id.to_buy_service);

    }

    @Override
    protected void initData() {
        days = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS);
        int haveTry = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_LECHUAN_TRY);
        if (haveTry == 0) {
            use_seven_days.setVisibility(View.VISIBLE);
            use_seven_days.setText("试用七天");
            use_seven_days.setTextSize(16);
        } else if (days < 1) {
            use_seven_days.setVisibility(View.GONE);
        } else {
            use_seven_days.setText("继续使用（剩余" + days + "天）");
            use_seven_days.setTextSize(11);
        }
        initViewpager();
    }

    @Override
    protected void initListener() {
        service_back.setOnClickListener(this);
        use_seven_days.setOnClickListener(this);
        to_buy_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_back:
                finish();
                break;
            case R.id.use_seven_days:
                if (days < 8 && days > 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tryToUse();
                }
                break;
            case R.id.to_buy_service:
                toBuyService();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
        float leftMargin = mDistance * (position + positionOffset);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDot.getLayoutParams();
        params.leftMargin = (int) leftMargin;
        ivDot.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        //页面跳转时，设置小圆点的margin
        float leftMargin = mDistance * position;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDot.getLayoutParams();
        params.leftMargin = (int) leftMargin;
        ivDot.setLayoutParams(params);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initViewpager() {
        LayoutInflater inflater = getLayoutInflater();
        ViewPager viewPager = (ViewPager) findViewById(R.id.service_viewpager);
        viewPager.addOnPageChangeListener(this);
        List<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        dotViews.add((ImageView) findView(R.id.service_iv_dot1));
        dotViews.add((ImageView) findView(R.id.service_iv_dot2));
        dotViews.add((ImageView) findView(R.id.service_iv_dot3));
        ivDot = findView(R.id.service_iv_dot);
        final LinearLayout layoutDot = findView(R.id.service_layout_dot);
        ivDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDistance = layoutDot.getChildAt(1).getLeft() - layoutDot.getChildAt(0).getLeft();
            }
        });

    }

    private void tryToUse() {
        showLoading("正在激活试用...");
        ShopServiceManager.getShopServiceManager().tryUseService(this, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();
                Toast.show(context, "激活成功,您将可以免费试用该功能七天");
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS, 7);//开始试用后保存天数，否则回到商家页再次进入乐传又要激活并失败，
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void toBuyService() {
        Intent intent = new Intent();
        intent.setClass(context, ServiceSelectActivity.class);
        startActivity(intent);
    }

}
