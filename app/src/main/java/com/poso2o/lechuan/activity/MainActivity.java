package com.poso2o.lechuan.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.login.LoginActivity;
import com.poso2o.lechuan.activity.mine.UserInfoActivity;
import com.poso2o.lechuan.activity.poster.MyCollectionActivity;
import com.poso2o.lechuan.activity.poster.MyFansActivity;
import com.poso2o.lechuan.activity.poster.MyFollowActivity;
import com.poso2o.lechuan.activity.poster.MyPublishActivity;
import com.poso2o.lechuan.activity.publish.PublishActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.event.PushMessageEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.configs.AppConfig;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.BeInvitedDialog;
import com.poso2o.lechuan.fragment.mine.MineFragment;
import com.poso2o.lechuan.fragment.mine.MineFragmentForMerchant;
import com.poso2o.lechuan.fragment.poster.PosterFragment;
import com.poso2o.lechuan.fragment.publish.PublishFragment;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.FragmentChangeManager;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.tabview.CommonTabLayout;
import com.poso2o.lechuan.tabview.CustomTabEntity;
import com.poso2o.lechuan.tabview.OnTabSelectListener;
import com.poso2o.lechuan.tabview.TabEntity;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends BaseActivity {
    //网络管理
    private MineDataManager mMineDataManager;
    private int[] mIconSelectIds = {R.mipmap.ic_red_money_tag_on, R.mipmap.ic_moments_tag_on, R.mipmap.ic_me_tag_on};
    private int[] mIconUnselectIds = {R.mipmap.ic_red_money_tag_off, R.drawable.tab2_seletor_bg, R.mipmap.ic_me_tag_off};
    private ArrayList<Fragment> mFragments;
    private CommonTabLayout mTabLayout;
    private FragmentChangeManager mFragmentChangeManager;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //加载图片
    private RequestManager glideRequest;

    //页面
    private DrawerLayout drawerLayout;
    private LinearLayout activity_main_right;
    //用户
    private ImageView user_logo;
    private TextView user_name;
    //发布、收藏、关注、粉丝
    private LinearLayout user_menu_publish_layout, user_menu_collection_layout, user_menu_follow_layout, user_menu_fans_layout;
    private TextView user_menu_publish_num, user_menu_collection_num, user_menu_follow_num, user_menu_fans_num, user_menu_fans_label;
    private PosterFragment posterFragment;
    private MineFragment mineFragment;
    private MineFragmentForMerchant mineFragmentForMerchant;
    public static final int TO_BALANCE = 1001;//去到红包余额页面
    public static final int TO_POSTER = 1002;//去到广告页并刷新
    public static final int RESULT_MINE_USERINFO_ID = 1001;//我的-用户信息页返回刷新
    public static final String KEY_SKIP = "skip";


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {
        setAlias();
        mFragments = new ArrayList<>();
        posterFragment = new PosterFragment();
        mFragments.add(posterFragment);
        mFragments.add(new PublishFragment());
        mineFragment = new MineFragment();
        mineFragmentForMerchant = new MineFragmentForMerchant();
        mFragments.add(SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE ? mineFragmentForMerchant : mineFragment);

        mFragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.container, mFragments);
        mFragmentChangeManager.setFragments(0);
        mTabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);
        setTabView();
        mTabLayout.setCurrentTab(0);
        // 页面
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        setDrawerLeftEdgeSize(drawerLayout, 0.6f);
        activity_main_right = (LinearLayout) findViewById(R.id.activity_main_right);

        //用户
        user_logo = (ImageView) findViewById(R.id.user_logo);
        user_logo.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                Bundle bundle = new Bundle();
                startActivity(new Intent(activity, UserInfoActivity.class));

            }
        });
        user_name = (TextView) findViewById(R.id.user_name);
        //发布、收藏、关注、粉丝
        user_menu_publish_layout = (LinearLayout) findViewById(R.id.user_menu_publish_layout);
        user_menu_collection_layout = (LinearLayout) findViewById(R.id.user_menu_collection_layout);
        user_menu_follow_layout = (LinearLayout) findViewById(R.id.user_menu_follow_layout);
        user_menu_fans_layout = (LinearLayout) findViewById(R.id.user_menu_fans_layout);

        user_menu_publish_num = (TextView) findViewById(R.id.user_menu_publish_num);
        user_menu_collection_num = (TextView) findViewById(R.id.user_menu_collection_num);
        user_menu_follow_num = (TextView) findViewById(R.id.user_menu_follow_num);
        user_menu_fans_num = (TextView) findViewById(R.id.user_menu_fans_num);
        user_menu_fans_label = (TextView) findViewById(R.id.user_menu_fans_label);

    }

    @Override
    public void initData() {
        mMineDataManager = MineDataManager.getMineDataManager();
        //加载图片
        glideRequest = Glide.with(activity);

        glideRequest.load("" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO)).transform(new GlideCircleTransform(activity)).into(user_logo);
        user_name.setText("" + SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));

        if (SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN).isEmpty()) {
            Bundle bundle = new Bundle();
            startActivity(new Intent(activity, LoginActivity.class).putExtras(bundle));
            return;
        }
    }

    private void setAlias() {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        List<String> list = MiPushClient.getAllAlias(activity);
        if (list != null && list.size() > 0) {
//            for (String s : list) {
//                Print.println("setAlias_alias:" + s);
//            }
            String alias = list.get(0);
            if (!uid.equals(alias)) {
                MiPushClient.setAlias(activity, uid, "");
                return;
            }
            MiPushClient.resumePush(activity, uid);
        } else {
            MiPushClient.setAlias(activity, uid, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        Print.println("MainActivity_onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Print.println("MainActivity_onNewIntent");
        super.onNewIntent(intent);
        if (intent.getIntExtra(KEY_SKIP, 0) == TO_BALANCE) {
            toBalance();
        } else if (intent.getIntExtra(KEY_SKIP, 0) == TO_POSTER) {
            toPoster();
        }
    }

    /**
     * 跳转到红包余额界面
     */
    private void toBalance() {
        mTabLayout.setCurrentTab(2);
        mFragmentChangeManager.setFragments(2);
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            mineFragmentForMerchant.setRedpacketView();
        } else {
            mineFragment.setRedpacketView();
        }
    }

    private void toPoster() {
        mTabLayout.setCurrentTab(0);
        mFragmentChangeManager.setFragments(0);
        posterFragment.loadPosterData(0);//刷新广告
    }

    @Override
    public void initListener() {
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {//选中tab
//                if (tab.getPosition() == 1) {
//                    startActivity(PublishActivity.class);
//                    mTabLayout.getTabAt(mSelectedTab).select();
//                } else {
//                    mSelectedTab = tab.getPosition();
//                    mFragmentChangeManager.setFragments(tab.getPosition());
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {//未选中tab
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {//再次选中tab
//                if (tab.getPosition() == 1) {
//                    startActivity(PublishActivity.class);
//                }
//            }
//        });
        //发布
        user_menu_publish_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(activity, MyPublishActivity.class));
            }
        });
        //收藏
        user_menu_collection_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(activity, MyCollectionActivity.class));
            }
        });
        //关注
        user_menu_follow_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(activity, MyFollowActivity.class));
            }
        });
        //粉丝
        user_menu_fans_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(activity, MyFansActivity.class));
            }
        });
    }

    @Override
    public void onCreate2(Bundle savedInstanceState) {
        super.onCreate2(savedInstanceState);
//        MiPushClient.registerPush(this, AppConfig.XM_APPID, AppConfig.XM_APPKEY);
    }

    @Override
    public void onDestroy2() {
        super.onDestroy2();
        Print.println("MainActivity_onDestroy2");
//        MiPushClient.unregisterPush(activity);
    }


    /**
     * 打开侧滑菜单
     */
    public void openRightDrawer() {
        if (!drawerLayout.isDrawerOpen(activity_main_right)) {
            drawerLayout.openDrawer(activity_main_right);
        }
    }
//
//    private int mSelectedTab = 0;

    /**
     * 设置TabView
     */
    private void setTabView() {
        String[] titles = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE ? getResources().getStringArray(R.array.home_tag_merchant) : getResources().getStringArray(R.array.home_tag_distribution);
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
//        商家显示：广告、发布、分销，分销商显示：广告、发布、我的
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 1) {
                    startActivity(PublishActivity.class);
                } else {
                    mFragmentChangeManager.setFragments(position);
                }
                if (position == 0) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    //返回主界面
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(activity_main_right)) {
            drawerLayout.closeDrawer(activity_main_right);
        } else {
            finish();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mMineDataManager.getUserInfo(activity, uid, null);
    }

    /**
     * 设置侧滑菜单显示
     *
     * @param userInfoBean
     */
    private void setMenuInfo(UserInfoBean userInfoBean) {
        user_menu_publish_num.setText("(" + userInfoBean.getSend_news_number() + ")");
        user_menu_collection_num.setText("(" + userInfoBean.getCollect_news_number() + ")");
        user_menu_follow_num.setText("(" + userInfoBean.getFlowme_number() + ")");
        user_menu_fans_num.setText("(" + userInfoBean.getMyfans_number() + ")");
        user_name.setText("" + userInfoBean.getNick());
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            user_menu_fans_label.setText("会员");
        } else {
            user_menu_fans_label.setText("粉丝");
        }
        glideRequest.load("" + userInfoBean.getLogo()).transform(new GlideCircleTransform(activity)).into(user_logo);
    }

    /**
     * DrawerLayout
     *
     * @param drawerLayout
     * @param displayWidthPercentage
     */
    private void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mRightDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 提现或充值成功，即时刷新我的红包余额
     *
     * @param event
     */
    @Override
    public void onPayResult(PayEvent event) {
//        refreshMineUserInfo();
        getUserInfo();
    }

    @Override
    public void getUserInfo(UserInfoBean userInfoBean) {
        Print.println("getUserInfo(UserInfoBean userInfoBean)");
        if (userInfoBean == null) {
            return;
        }
        setMenuInfo(userInfoBean);
        refreshMineUserInfo(userInfoBean);
    }

    private void refreshMineUserInfo(UserInfoBean userInfoBean) {
        Print.println("refreshMineUserInfo");
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            mineFragmentForMerchant.setUserInfo(userInfoBean);
//        } else if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
        } else {
            mineFragment.setUserInfo(userInfoBean);
        }
    }

    @Override
    public void onEvent(EventBean event) {
        if (event.code == EventBean.COMMENT_ID) {//评论成功刷新数量
            posterFragment.refreshCommentNumber();
        } else if (event.code == EventBean.REDBAG_STATUS_ID) {//抢红包后刷新状态
            posterFragment.refreshRedBagStatus();
        } else if (event.code == EventBean.PRAISE_ID) {//刷新点赞
            posterFragment.refreshPraiseNumber(event.id == 1 ? true : false);
        }
    }

    private boolean onceAgain = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(activity_main_right)) {
                drawerLayout.closeDrawer(activity_main_right);
                return false;
            }
            if (!onceAgain) {
                Toast.show(activity, getString(R.string.exit_app));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onceAgain = false;
                    }
                }, 2000);
                onceAgain = true;
                return false;
            }
            AppUtil.exitApp(activity, false);
        }
        return super.onKeyDown(keyCode, event);
    }


}
