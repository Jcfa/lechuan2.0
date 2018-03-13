package com.poso2o.lechuan.activity.login;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.activity.orderinfo.OrderInfoMainActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.adapter.ViewPagerAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.version.VersionBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.version.VersionUpdate;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_SERVICE_DAYS;

/**
 * Created by Administrator on 2017-11-25.
 */

public class StartActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private List<ImageView> dotViews = new ArrayList<>();
    private ImageView ivDot;//白色圆点
    private float mDistance;
    private boolean mExist = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        mExist = getIntent().getBooleanExtra(SharedPreferencesUtils.TAG_EXIT, false);
        findView(R.id.btn_login).setOnClickListener(this);
        findView(R.id.btn_register).setOnClickListener(this);
        findView(R.id.tv_weixin_login).setOnClickListener(this);

        initViewpager();
    }

    @Override
    protected void initData() {
        // TODO 测试代码
        SharedPreferencesUtils.put(KEY_USER_SERVICE_DAYS, 50);
        activity.checkNewVersion();
    }

    @Override
    protected void initListener() {

    }

    private void initViewpager() {
        LayoutInflater inflater = getLayoutInflater();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        List<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        views.add(inflater.inflate(R.layout.layout_login_pager1, null));
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        dotViews.add((ImageView) findView(R.id.iv_dot1));
        dotViews.add((ImageView) findView(R.id.iv_dot2));
        dotViews.add((ImageView) findView(R.id.iv_dot3));
        ivDot = findView(R.id.iv_dot);
        final LinearLayout layoutDot = findView(R.id.layout_dot);
        ivDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDistance = layoutDot.getChildAt(1).getLeft() - layoutDot.getChildAt(0).getLeft();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        String token = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN);
        if (!TextUtil.isEmpty(uid) && !TextUtil.isEmpty(token) && !mExist) {//已经登录过直接去首页
//            if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
                toRShopMainActivity();
//            } else {
//                toMainActivity();
//            }
        }
    }


    /**
     * 乐传首页
     */
    private void toMainActivity() {
//        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.DISTRIBUTION_TYPE);//保存当前登录身份为分销员
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    /**
     * 老板管理首页
     */
    private void toRShopMainActivity() {
//        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.MERCHANT_TYPE);//保存当前登录身份为商家
        startActivity(new Intent(activity, OrderInfoMainActivity.class));
//        startActivity(new Intent(activity, RShopMainActivity.class));

        finish();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_weixin_login:
                WaitDialog.showLoaddingDialog(activity, "正在调起微信...");
                UserDataManager.getUserDataManager().weixinAuth(this);
                break;
        }
    }

    @Override
    public void onStop() {
        WaitDialog.dismissLoaddingDialog();
        super.onStop();
    }
}
