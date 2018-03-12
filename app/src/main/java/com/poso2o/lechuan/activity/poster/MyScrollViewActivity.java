package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MyFragmentStateAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.fragment.poster.OpenEvaluateFragment;
import com.poso2o.lechuan.fragment.poster.OpenFansFragment;
import com.poso2o.lechuan.fragment.poster.OpenFollowFragment;
import com.poso2o.lechuan.fragment.poster.OpenPublishFragment;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ResourceSetUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PullZoomScrollView;
import com.poso2o.lechuan.views.MyViewPager;

import java.util.ArrayList;

/**
 * 广告我的收藏页面
 */
public class MyScrollViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    //网络管理
    private PosterDataManager mPosterDataManager;
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
    private ImageView user_home_logo, user_home_logo2;
    private TextView user_home_title, user_home_title2, user_home_description;
    private TextView user_home_follow_btn;
    //店评、发布、粉丝、关注
//    private TextView user_home_evaluate_num, user_home_publish_num, user_home_fans_num, user_home_follow_num;
//    private TextView user_home_evaluate, user_home_publish, user_home_fans, user_home_follow;
    private ArrayList<TextView> numArrays = new ArrayList<>();
    private ArrayList<TextView> tagArrays = new ArrayList<>();
    private LinearLayout layout_home_evaluate, layout_home_publish, layout_home_fans, layout_home_follow;
    //
//    private FragmentChangeManager mFragmentChangeManager;
    private ArrayList<Fragment> mFragments;

    private OpenEvaluateFragment mOpenEvaluateFragment = null;//店评
    private OpenPublishFragment mOpenPublishFragment = null;//发布
    private OpenFansFragment mOpenFansFragment = null;//粉丝
    private OpenFollowFragment mOpenFollowFragment = null;//关注
    private BaseFragment mSelectedFragment;
    private RequestManager glideRequest;
    private PullZoomScrollView mView;
    private RelativeLayout mImageContainer;
    private ImageView mImage;
    private MyViewPager viewPager;
    private RelativeLayout layoutTitle;
    private TextView tvSelectTag, tvSelectNum;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_scroll_view;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        user_home_back = (ImageView) findViewById(R.id.user_home_back);
        //用户
        user_home_logo = (ImageView) findViewById(R.id.user_home_logo);
        user_home_logo2 = (ImageView) findViewById(R.id.user_home_logo2);
        user_home_title = (TextView) findViewById(R.id.user_home_title);
        user_home_title2 = (TextView) findViewById(R.id.user_home_title2);
        user_home_description = findView(R.id.user_home_description);
        user_home_follow_btn = (TextView) findViewById(R.id.user_home_follow_btn);
        //店评、发布、粉丝、关注
        TextView user_home_evaluate = findView(R.id.user_home_evaluate);
        tagArrays.add(user_home_evaluate);
        TextView user_home_evaluate_num = (TextView) findViewById(R.id.user_home_evaluate_num);
        numArrays.add(user_home_evaluate_num);
        layout_home_evaluate = findView(R.id.layout_home_evaluate);
        TextView user_home_publish = (TextView) findViewById(R.id.user_home_publish);
        tagArrays.add(user_home_publish);
        TextView user_home_publish_num = (TextView) findViewById(R.id.user_home_publish_num);
        numArrays.add(user_home_publish_num);
        layout_home_publish = findView(R.id.layout_home_publish);
        TextView user_home_fans = (TextView) findViewById(R.id.user_home_fans);
        tagArrays.add(user_home_fans);
        TextView user_home_fans_num = (TextView) findViewById(R.id.user_home_fans_num);
        numArrays.add(user_home_fans_num);
        layout_home_fans = findView(R.id.layout_home_fans);
        TextView user_home_follow = (TextView) findViewById(R.id.user_home_follow);
        tagArrays.add(user_home_follow);
        TextView user_home_follow_num = (TextView) findViewById(R.id.user_home_follow_num);
        numArrays.add(user_home_follow_num);
        layout_home_follow = findView(R.id.layout_home_follow);
        layoutTitle = findView(R.id.layout_title);
        viewPager = findView(R.id.viewpager);

        mImageContainer = (RelativeLayout) findViewById(R.id.topimagecontainer);
        mView = (PullZoomScrollView) findViewById(R.id.scrollview);
        mImage = (ImageView) findViewById(R.id.topimage);
//        mImage.setImageBitmap(ImageCrop(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
        mView.mImageView = mImageContainer;
        setAlpha(0);
        mView.setOnScrollTitleListener(new PullZoomScrollView.OnScrollTitleListener() {
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int alpha = scrollY;
                if (scrollY > 255) {
                    alpha = 255;
                } else if (scrollY < 0) {
                    alpha = 0;
                }
                setAlpha(alpha);
            }
        });
        mPosterDataManager = PosterDataManager.getPosterDataManager();
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
        args.putInt("user_type", user_type);
        Log.i("MyScrollViewActivity", "MyScrollViewActivity=uid:" + uid);
        mFragments = new ArrayList<>();

        if (user_type == Constant.MERCHANT_TYPE) {//0=商家,1=分销商
            mOpenEvaluateFragment = new OpenEvaluateFragment();//店评
            mOpenEvaluateFragment.setArguments(args);
            mOpenEvaluateFragment.setViewPager(viewPager);
            mFragments.add(mOpenEvaluateFragment);
            user_home_evaluate.setTag(0);
            user_home_publish.setTag(1);
            user_home_fans.setTag(2);
            user_home_follow.setTag(3);
        } else {
            layout_home_evaluate.setVisibility(View.GONE);
            findView(R.id.user_home_evaluate_line).setVisibility(View.GONE);
            user_home_publish.setTag(0);
            user_home_fans.setTag(1);
            user_home_follow.setTag(2);
        }

        mOpenPublishFragment = new OpenPublishFragment();//发布
        mOpenPublishFragment.setArguments(args);
        mOpenPublishFragment.setViewPager(viewPager);
        mFragments.add(mOpenPublishFragment);

        mOpenFansFragment = new OpenFansFragment();//粉丝
        mOpenFansFragment.setArguments(args);
        mOpenFansFragment.setViewPager(viewPager);
        mFragments.add(mOpenFansFragment);

        mOpenFollowFragment = new OpenFollowFragment();//关注
        mOpenFollowFragment.setArguments(args);
        mOpenFollowFragment.setViewPager(viewPager);
        mFragments.add(mOpenFollowFragment);

//        mFragmentChangeManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.user_home_container, mFragments);
        viewPager.setAdapter(new MyFragmentStateAdapter(getSupportFragmentManager(), mFragments));
        viewPager.addOnPageChangeListener(this);
    }

    private void setAlpha(int alpha) {
        findView(R.id.title_line).getBackground().setAlpha(alpha);
        layoutTitle.getBackground().setAlpha(alpha);
        user_home_logo.setAlpha((alpha / 255.00f));
        user_home_back.getBackground().setAlpha(alpha);
        user_home_title.setTextColor(Color.argb(alpha, 94, 94, 94));
        user_home_follow_btn.getBackground().setAlpha(alpha);
        user_home_follow_btn.setTextColor(Color.argb(alpha, 94, 94, 94));
        user_home_follow_btn.setVisibility(alpha <= 5 ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        viewPager.setCurrentItem(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user_type == 0) {//0=商家,1=分销商
//                    mFragmentChangeManager.setFragments(0);
                    tvSelectTag = tagArrays.get(0);
                    tvSelectNum = numArrays.get(0);
                    setTagColor(0);
                    mSelectedFragment = mOpenEvaluateFragment;
                    mOpenEvaluateFragment.loadPosterData(1, mView);
                } else {
//                    mFragmentChangeManager.setFragments(0);
                    tvSelectTag = tagArrays.get(1);
                    tvSelectNum = numArrays.get(1);
                    setTagColor(1);
                    mSelectedFragment = mOpenPublishFragment;
                    mOpenPublishFragment.loadPosterData(1, mView);
                }
            }
        }, 500);
        getUserInfo();
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
        layout_home_evaluate.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                viewPager.setCurrentItem(0);
                setTagColor(0);
                mSelectedFragment = mOpenEvaluateFragment;
                mOpenEvaluateFragment.loadPosterData(1, mView);
            }
        });
        //发布
        layout_home_publish.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                viewPager.setCurrentItem(user_type == Constant.MERCHANT_TYPE ? 1 : 0);
                setTagColor(1);
                mSelectedFragment = mOpenPublishFragment;
                mOpenPublishFragment.loadPosterData(1, mView);
            }
        });
        //粉丝
        layout_home_fans.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                viewPager.setCurrentItem(user_type == Constant.MERCHANT_TYPE ? 2 : 1);
                setTagColor(2);
                mSelectedFragment = mOpenFansFragment;
                mOpenFansFragment.loadPosterData(1, mView);
            }
        });
        //关注
        layout_home_follow.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                viewPager.setCurrentItem(user_type == Constant.MERCHANT_TYPE ? 3 : 2);
                setTagColor(3);
                mSelectedFragment = mOpenFollowFragment;
                mOpenFollowFragment.loadPosterData(1, mView);
            }
        });
        mView.setOnScrollListener(new PullZoomScrollView.OnScrollListener() {

            @Override
            public void onScrollBottom() {
                loadNext();
            }
        });
    }

    private void setTagColor(int position) {
        ResourceSetUtil.setTextColor(activity, tvSelectTag, R.color.textBlack);
        ResourceSetUtil.setTextColor(activity, tvSelectNum, R.color.textBlack);
        ResourceSetUtil.setTextColor(activity, tagArrays.get(position), R.color.colorRed);
        ResourceSetUtil.setTextColor(activity, numArrays.get(position), R.color.colorRed);
        tvSelectTag = tagArrays.get(position);
        tvSelectNum = numArrays.get(position);
    }

    /**
     * 关注
     */
    private void toFollow() {
        if (has_flow == 0) {//1=已关注 ,0=未关注
            if (SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID).equals(uid + "")) {
                Toast.show(activity, "不能关注自己哦！");
                return;
            }
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
     * 将图片裁减为宽高比例符合 上部imageview的宽高比例,这个例子中，高度固定为200，这个自己可以调整
     *
     * @param bitmap
     * @return
     */
    public Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX = 0;
        int retY = 0;
        int height = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float rate = (float) dp2px(200, this) / (float) displayMetrics.widthPixels;
        height = (int) (h * rate);
        if (h > height) {
            retY = (h - height) / 2;
            retX = 0;
        } else {
            retY = 0;
            retX = (w - (int) (h / rate)) / 2;
        }
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, w, height, null, false);
    }

    public int dp2px(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        if (user_type == Constant.MERCHANT_TYPE) {
        viewPager.resetHeight(position);
        setTagColor(position);
//        } else {
//            viewPager.resetHeight(position + 1);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 加载下一页
     */
    private void loadNext() {
        if (mSelectedFragment instanceof OpenEvaluateFragment) {
            mOpenEvaluateFragment.loadNext();
        } else if (mSelectedFragment instanceof OpenPublishFragment) {
            mOpenPublishFragment.loadNext();
        } else if (mSelectedFragment instanceof OpenFansFragment) {
            mOpenFansFragment.loadNext();
        } else if (mSelectedFragment instanceof OpenFollowFragment) {
            mOpenFollowFragment.loadNext();
        }
    }


    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        MineDataManager.getMineDataManager().getUserInfo(activity, uid, new IRequestCallBack<UserInfoBean>() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, UserInfoBean userInfoBean) {
                setUserInfo(userInfoBean);
            }
        });
    }

    /**
     * 设置信息
     */
    private void setUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            glideRequest.load(userInfoBean.getLogo()).transform(new GlideCircleTransform(activity)).error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(user_home_logo);
            glideRequest.load(userInfoBean.getLogo()).transform(new GlideCircleTransform(activity)).error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(user_home_logo2);
            glideRequest.load(userInfoBean.background_logo).error(R.mipmap.logo_g).placeholder(R.mipmap.logo_g).into(mImage);
            user_home_title.setText(userInfoBean.getNick());
            user_home_title2.setText(userInfoBean.getNick());
            user_home_description.setText(userInfoBean.getRemark());
            numArrays.get(0).setText(userInfoBean.getOrder_appraise_number() + "");
            numArrays.get(1).setText(userInfoBean.getSend_news_number() + "");
            numArrays.get(2).setText(userInfoBean.getMyfans_number() + "");
            numArrays.get(3).setText(userInfoBean.getFlowme_number() + "");
            ImageView ivTag = findView(R.id.user_home_tag);
            if (userInfoBean.getUser_type() == Constant.MERCHANT_TYPE) {
                ivTag.setImageResource(R.mipmap.poster_seller_icon);
            } else if (userInfoBean.getUser_type() == Constant.DISTRIBUTION_TYPE) {
                ivTag.setImageResource(R.mipmap.poster_seller_icon_b);
            } else {
                ivTag.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onEvent(EventBean event) {
        Print.println("MyScrollViewActivity_onEvent():"+event.code);
        if (event.code == EventBean.COMMENT_ID) {//评论成功刷新数量
            mOpenPublishFragment.refreshCommentNumber();
        } else if (event.code == EventBean.REDBAG_STATUS_ID) {//抢红包后刷新状态
            mOpenPublishFragment.refreshRedBagStatus();
        } else if (event.code == EventBean.PRAISE_ID) {//刷新点赞
            mOpenPublishFragment.refreshPraiseNumber(event.id == 1 ? true : false);
        }
    }
}
