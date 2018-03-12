package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.fragment.poster.OpenEvaluateFragment;
import com.poso2o.lechuan.fragment.poster.OpenFansFragment;
import com.poso2o.lechuan.fragment.poster.OpenFollowFragment;
import com.poso2o.lechuan.fragment.poster.OpenPublishFragment;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.FragmentChangeManager;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 公开的商家首页页面
 */
public class OpenHomeActivity extends BaseActivity {
    //网络管理
    private PosterDataManager mPosterDataManager;
    private MineDataManager mMineDataManager;
    //用户ID
    private String uid;
    // 0=商家,1=分销商,2=商家+分销商 */
    private int user_type = 0;
    //作者名称 */
    private String nick = "";
    // 作者LOGO */
    private String logo = "";
    // 是否已关注  1=已关注 ,0=未关注  */
    private int has_flow = 0;
    //返回
    private ImageView user_home_back;
    //用户
    private ImageView user_home_logo;
    private TextView user_home_title;
    private TextView user_home_follow_btn;
    //店评、发布、粉丝、关注
    private TextView user_home_evaluate, user_home_publish, user_home_fans, user_home_follow;
    //
    private FragmentChangeManager mFragmentChangeManager;
    private ArrayList<Fragment> mFragments;

    private OpenEvaluateFragment mOpenEvaluateFragment = null;//店评
    private OpenPublishFragment mOpenPublishFragment = null;//发布
    private OpenFansFragment mOpenFansFragment = null;//粉丝
    private OpenFollowFragment mOpenFollowFragment = null;//关注
    private boolean OpenEvaluate = false;
    private boolean OpenPublish = false;
    private boolean OpenFans = false;
    private boolean OpenFollow = false;

    private TextView selected_tab;

    private RequestManager glideRequest;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_home;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        user_home_back = (ImageView) findViewById(R.id.user_home_back);
        //用户
        user_home_logo = (ImageView) findViewById(R.id.user_home_logo);
        user_home_title = (TextView) findViewById(R.id.user_home_title);
        user_home_follow_btn = (TextView) findViewById(R.id.user_home_follow_btn);
        //店评、发布、粉丝、关注
        user_home_evaluate = (TextView) findViewById(R.id.user_home_evaluate);
        user_home_publish = (TextView) findViewById(R.id.user_home_publish);
        user_home_fans = (TextView) findViewById(R.id.user_home_fans);
        user_home_follow = (TextView) findViewById(R.id.user_home_follow);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mPosterDataManager = PosterDataManager.getPosterDataManager();
        mMineDataManager = MineDataManager.getMineDataManager();
        //加载图片
        glideRequest = Glide.with(activity);

        // 上一级页面传来的数据
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        user_type = intent.getIntExtra("user_type", 0);//0=商家,1=分销商,2=商家+分销商
        nick = intent.getStringExtra("nick");
        logo = intent.getStringExtra("logo");
        has_flow = intent.getIntExtra("has_flow", 0);

        glideRequest.load("" + logo).transform(new GlideCircleTransform(activity)).into(user_home_logo);
        user_home_title.setText("" + nick);

        if (has_flow == 0) {//1=已关注 ,0=未关注
            user_home_follow_btn.setText("+ 关注");
        } else {
            user_home_follow_btn.setText("- 关注");
        }

        Bundle args = new Bundle();
        args.putString("uid", uid);

        mFragments = new ArrayList<>();

        if (user_type == 0) {//0=商家,1=分销商
            mOpenEvaluateFragment = new OpenEvaluateFragment();//店评
            mOpenEvaluateFragment.setArguments(args);
            mFragments.add(mOpenEvaluateFragment);
            user_home_evaluate.setTag(0);
            user_home_publish.setTag(1);
            user_home_fans.setTag(2);
            user_home_follow.setTag(3);
        } else {
            user_home_evaluate.setVisibility(View.GONE);
            user_home_publish.setTag(0);
            user_home_fans.setTag(1);
            user_home_follow.setTag(2);
        }

        mOpenPublishFragment = new OpenPublishFragment();//发布
        mOpenPublishFragment.setArguments(args);
        mFragments.add(mOpenPublishFragment);

        mOpenFansFragment = new OpenFansFragment();//粉丝
        mOpenFansFragment.setArguments(args);
        mFragments.add(mOpenFansFragment);

        mOpenFollowFragment = new OpenFollowFragment();//关注
        mOpenFollowFragment.setArguments(args);
        mFragments.add(mOpenFollowFragment);

        mFragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.user_home_container, mFragments);

        getUserInfo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user_type == 0) {//0=商家,1=分销商
                    //mFragmentChangeManager.setFragments(0);
                    //mOpenEvaluateFragment.loadPosterData(1);
                    //selected_tab = user_home_evaluate;

                    mFragmentChangeManager.setFragments(1);
                    mOpenPublishFragment.loadPosterData(1,null);
                    selected_tab = user_home_publish;

                } else {
                    mFragmentChangeManager.setFragments(0);
                    mOpenPublishFragment.loadPosterData(1,null);
                    selected_tab = user_home_publish;
                }
                selected_tab.setTextColor(Color.parseColor("#FF6537"));
            }
        }, 500);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        user_home_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });

        //关注
        user_home_follow_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                toFollow();
            }
        });
        //店评
        user_home_evaluate.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mFragmentChangeManager.setFragments(0);
                if (!OpenEvaluate) {
                    OpenEvaluate = true;
                    mOpenEvaluateFragment.loadPosterData(1,null);
                }
                selected_tab.setTextColor(Color.parseColor("#5e5e5e"));
                selected_tab = user_home_evaluate;
                user_home_evaluate.setTextColor(Color.parseColor("#FF6537"));
            }
        });
        //发布
        user_home_publish.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mFragmentChangeManager.setFragments((Integer) user_home_publish.getTag());
                if (!OpenPublish) {
                    OpenPublish = true;
                    mOpenPublishFragment.loadPosterData(1,null);
                }
                selected_tab.setTextColor(Color.parseColor("#5e5e5e"));
                selected_tab = user_home_publish;
                user_home_publish.setTextColor(Color.parseColor("#FF6537"));
            }
        });
        //粉丝
        user_home_fans.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mFragmentChangeManager.setFragments((Integer) user_home_fans.getTag());
                if (!OpenFans) {
                    OpenFans = true;
                    mOpenFansFragment.loadPosterData( 1,null);
                }
                selected_tab.setTextColor(Color.parseColor("#5e5e5e"));
                selected_tab = user_home_fans;
                user_home_fans.setTextColor(Color.parseColor("#FF6537"));
            }
        });
        //关注
        user_home_follow.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mFragmentChangeManager.setFragments((Integer) user_home_follow.getTag());
                if (!OpenFollow) {
                    OpenFollow = true;
                    mOpenFollowFragment.loadPosterData(1,null);
                }
                selected_tab.setTextColor(Color.parseColor("#5e5e5e"));
                selected_tab = user_home_follow;
                user_home_follow.setTextColor(Color.parseColor("#FF6537"));
            }
        });
    }

    /**
     * 关注
     */
    private void toFollow() {
        if(uid.equals(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID))){
            Toast.show(activity,"亲，没办法关注自己噢。。。");
            return;
        }
        if (has_flow == 0) {//1=已关注 ,0=未关注
            mPosterDataManager.loadFollowPoster(activity, "" + uid, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//关注失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "关注成功");
                    user_home_follow_btn.setText("- 关注");
                    has_flow = 1;
                }
            });
        } else {
            mPosterDataManager.loadUnFollowPoster(activity, "" + uid, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//取消点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "取消关注成功");
                    user_home_follow_btn.setText("+ 关注");
                    has_flow = 0;
                }
            });
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        mMineDataManager.getUserInfo(activity, uid, new IRequestCallBack<UserInfoBean>() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, UserInfoBean userInfoBean) {
                if (userInfoBean.getHas_flow().equals("1")) {//1=已关注 ,0=未关注
                    user_home_follow_btn.setText("- 关注");
                } else {
                    user_home_follow_btn.setText("+ 关注");
                }
                user_home_title.setText("" + userInfoBean.getNick());
                //glideRequest.load("" + userInfoBean.getLogo()).transform(new GlideCircleTransform(activity)).into(user_home_logo);
            }
        });
    }

}
