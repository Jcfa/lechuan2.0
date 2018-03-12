package com.poso2o.lechuan.activity.poster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.adapter.PosterCommentAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.broadcast.BroadcastManager;
import com.poso2o.lechuan.constant.BroadcastAction;
import com.poso2o.lechuan.dialog.RedbagDialog;
import com.poso2o.lechuan.dialog.ShareDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.ShareManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 文章详情视图
 */
public class PosterDetailsActivity extends BaseActivity implements View.OnLayoutChangeListener {
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private ShareBroadcast mShareBroadcast;

    private TextView poster_details_loading;

    //滚动控制
    private ScrollView poster_details_scrollView;
    //返回
    private ImageView poster_details_back;
    //加载图片
    private RequestManager glideRequest;
    //用户
    private ImageView poster_details_user_logo;
    private TextView poster_details_user_name;
    //关注
    private TextView poster_details_follow_btn;
    //来自评论
    private String is_comment = "0";
    //文章ID
    private String news_id;
    //文章内容
    private PosterDTO posterData;
    private WebView webView;
    //分享
    private LinearLayout poster_details_share_layout;
    private ImageView poster_details_share_icon;
    private TextView poster_details_share_text;
    //点赞、收藏、转发、评论
    private LinearLayout poster_details_like, poster_details_collection, poster_details_forward, poster_details_comment;
    private ImageView poster_details_like_iv, poster_details_collection_iv;
    private TextView poster_details_like_num, poster_details_collection_num, poster_details_forward_num, poster_details_comment_num;
    //写评论内容
    private LinearLayout poster_details_publish_comment_layout, poster_details_comment_layout;
    private EditText poster_details_comment_et;
    private TextView poster_details_publish_comment;
    //网络管理
    private PosterDataManager mPosterDataManager;
    /**
     * 列表
     */
    private RecyclerView my_fans_recycler;
    private RecyclerView.LayoutManager my_fans_manager;
    private PosterCommentAdapter myFollowAdapter;

    //分享窗口
    private ShareDialog mShareDialog;
    //红包窗口
    private RedbagDialog redbagDialog;

    private ProgressBar pbProgress;
    LinearLayout.LayoutParams statusBarHeightParams;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_poster_details;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        //滚动控制
        poster_details_scrollView = (ScrollView) findViewById(R.id.poster_details_scrollView);
        activityRootView = findViewById(R.id.poster_details_root_layout);
        poster_details_back = (ImageView) findViewById(R.id.poster_details_back);
        poster_details_loading = (TextView) findViewById(R.id.poster_details_loading);

        //用户
        poster_details_user_logo = (ImageView) findViewById(R.id.poster_details_user_logo);
        poster_details_user_name = (TextView) findViewById(R.id.poster_details_user_name);
        poster_details_follow_btn = (TextView) findViewById(R.id.poster_details_follow_btn);
        webView = (WebView) findViewById(R.id.poster_details_webView);

        //点赞、收藏、转发、评论
        poster_details_like = (LinearLayout) findViewById(R.id.poster_details_like);
        poster_details_collection = (LinearLayout) findViewById(R.id.poster_details_collection);
        poster_details_forward = (LinearLayout) findViewById(R.id.poster_details_forward);
        poster_details_comment = (LinearLayout) findViewById(R.id.poster_details_comment);
        poster_details_like_iv = (ImageView) findViewById(R.id.poster_details_like_iv);
        poster_details_collection_iv = (ImageView) findViewById(R.id.poster_details_collection_iv);
        poster_details_like_num = (TextView) findViewById(R.id.poster_details_like_num);
        poster_details_collection_num = (TextView) findViewById(R.id.poster_details_collection_num);
        poster_details_forward_num = (TextView) findViewById(R.id.poster_details_forward_num);
        poster_details_comment_num = (TextView) findViewById(R.id.poster_details_comment_num);

        poster_details_share_layout = (LinearLayout) findViewById(R.id.poster_details_share_layout);
        poster_details_share_icon = (ImageView) findViewById(R.id.poster_details_share_icon);
        poster_details_share_text = (TextView) findViewById(R.id.poster_details_share_text);

        //写评论内容
        poster_details_publish_comment_layout = (LinearLayout) findViewById(R.id.poster_details_publish_comment_layout);
        poster_details_comment_layout = (LinearLayout) findViewById(R.id.poster_details_comment_layout);
        poster_details_comment_et = (EditText) findViewById(R.id.poster_details_comment_et);
        poster_details_publish_comment = (TextView) findViewById(R.id.poster_details_publish_comment);

        //列表
        my_fans_recycler = (RecyclerView) findViewById(R.id.poster_details_comment_recycler);
        my_fans_manager = new LinearLayoutManager(activity);
        my_fans_recycler.setLayoutManager(my_fans_manager);

        pbProgress = (ProgressBar) findViewById(R.id.poster_details_pb_progress);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mPosterDataManager = PosterDataManager.getPosterDataManager();
        //加载图片
        glideRequest = Glide.with(activity);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        // 上一级页面传来的数据
        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");

        //获取webView高度
        poster_details_scrollView.fullScroll(ScrollView.FOCUS_UP);
        /** 支持javascript */
        webView.getSettings().setJavaScriptEnabled(false);
        /** 设置可以支持缩放 */
        webView.getSettings().setSupportZoom(false);
        /** 设置出现缩放工具 */
        webView.getSettings().setBuiltInZoomControls(false);
        /** 扩大比例的缩放 */
        webView.getSettings().setUseWideViewPort(false);
        /** 清除浏览器缓存 */
        webView.clearCache(true);
        /** 自适应屏幕 */
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        /** 优先使用缓存 */
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /** 不使用缓存： */
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                *//** 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器 *//*
                view.loadUrl(url);
                return true;
            }
        });*/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        /** 判断页面加载过程 */
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    /** 网页加载完成 */
                    poster_details_loading.setVisibility(View.GONE);
                    pbProgress.setVisibility(View.GONE);
                } else {
                    /** 加载中 */
                    //poster_details_loading.setVisibility(View.GONE);
                    pbProgress.setProgress(newProgress);
                }
            }
        });


        //列表
        myFollowAdapter = new PosterCommentAdapter(activity, new PosterCommentAdapter.OnItemListener() {
            @Override
            public void onClickItem(PosterCommentsDTO posterData) {
                //itemOnClickListenner(posterData);
                //显示评论内容
                showCommentLayout("回复");
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_fans_recycler.setLayoutManager(linearLayoutManager);
        my_fans_recycler.setAdapter(myFollowAdapter);

        //分享窗口
        mShareDialog = new ShareDialog(activity);

        //红包
        redbagDialog = new RedbagDialog(activity);

        //获取广告详情
        loadPosterInfo();

        //获取我的关注数据
        //loadPosterData();

        /**
         * 注册分享广播
         */
        opendShareBroadcast();


    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
        //返回
        poster_details_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //关注
        poster_details_follow_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                toFollow();
            }
        });
        //点赞
        poster_details_like.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(activity,"点赞");
                toLike();
            }
        });
        //收藏
        poster_details_collection.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(context,"收藏");
                toCollection();
            }
        });
        //转发
        poster_details_forward.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(activity,"转发");
                showShareDialog(0);
            }
        });
        //评论
        poster_details_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(activity,"评论");
                Bundle bundle = new Bundle();
                bundle.putString("news_id", "" + posterData.news_id);
                startActivity(new Intent(activity, PosterCommentActivity.class).putExtras(bundle));
            }
        });
        //分享
        poster_details_share_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (posterData.has_red_envelopes > 0) {//0=没有红包,1=有红包
                    if (posterData.red_envelopes_surplus_number > 0) {//红包剩余个数
                        if (posterData.has_myget_red_envelopes > 0) {//1=已抢红包,0=未抢红包
                            //恭喜您，红包已存入您的账户，前往查看>
                            toUserActivity();
                        } else {
                            //转发朋友圈抢红包
//                            showShareDialog(1);
                            shareWx();
                        }
                    } else {
                        //手慢了，红包被抢完了，去账户>
                        toUserActivity();
                    }
                } else {
                    showShareDialog(0);
                }
            }
        });
        //点击评论
        poster_details_publish_comment_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //显示评论内容
                showCommentLayout("");
            }
        });
        //发布评论
        poster_details_publish_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

            }
        });
    }

    /**
     * 分享朋友圈
     */
    private void shareWx() {
        if (posterData == null || TextUtil.isEmpty(posterData.news_img)) {
            ShareManager.getShareManager().weixinShareWebImage((BaseActivity) activity, posterData.url, posterData.news_title, posterData.articles, R.mipmap.ic_launcher, true);
            return;
        }
        WaitDialog.showLoaddingDialog(activity, "加载图片...");
        Glide.with(activity)
                .load(posterData.news_img)
                .asBitmap().toBytes()//强制Glide返回一个Bitmap对象
                .priority(Priority.HIGH)
                .into(new SimpleTarget<byte[]>(50, 50) {
                    @Override
                    public void onResourceReady(byte[] bitmap, GlideAnimation<? super byte[]> glideAnimation) {
                        WaitDialog.dismissLoaddingDialog();
                        ShareManager.getShareManager().weixinShareWebImageForByte((BaseActivity) activity, posterData.url, posterData.news_title, posterData.articles, bitmap, true);
                    }
                });
    }

    /**
     * 显示评论内容
     */
    private void showCommentLayout(String comment) {
        poster_details_share_layout.setVisibility(View.GONE);
        poster_details_comment_layout.setVisibility(View.VISIBLE);
        poster_details_comment_et.setText("" + comment);
        poster_details_comment_et.setSelection(poster_details_comment_et.getText().toString().length());
        poster_details_comment_et.requestFocus();
        KeyboardUtils.showSoftInput(poster_details_comment_et);
    }

    @Override
    public void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
    }

    /**
     * 监听到软件盘显示与隐藏的事件
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            //Toast.show(context,"监听到软键盘弹起...");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //Toast.show(context,"监听到软件盘关闭...");
            if (poster_details_share_layout.getTag().toString().equals("1")) {
                poster_details_share_layout.setVisibility(View.VISIBLE);
            }
            //poster_details_publish_comment_layout.setVisibility(View.VISIBLE);
            poster_details_comment_layout.setVisibility(View.GONE);
            //scrollToBottom(poster_details_scrollView);
        }
    }

    /**
     * 控制ScrollView滚动到底部
     */
    public static void scrollToBottom(final ScrollView scrollView) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * 获取我的关注数据
     */
    public void loadPosterData() {
        //refreshItem(posterData);
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterCommentsDTO> posterData) {
        myFollowAdapter.notifyData(posterData);
    }

    /**
     * 获取广告详情
     */
    public void loadPosterInfo() {
//        showLoading();
        mPosterDataManager.loadPosterInfo(activity, news_id, new IRequestCallBack<PosterDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg + "");
                poster_details_loading.setText(msg + "");
                finish();
            }

            @Override
            public void onResult(int tag, PosterDTO posterDTO) {
//                dismissLoading();
                posterData = posterDTO;
                setPosterData();
            }
        });
    }

    /**
     * 设置广告
     */
    private void setPosterData() {

        //用户
        poster_details_user_name.setText("" + posterData.nick);
        //Glide.with(context).load(posterData.logo).placeholder(R.mipmap.logo_d).error(R.mipmap.logo_d).into(poster_details_user_logo);
        glideRequest.load("" + posterData.logo).transform(new GlideCircleTransform(activity)).into(poster_details_user_logo);

        if (posterData.has_flow == 0) {//1=已关注 ,0=未关注
            poster_details_follow_btn.setText("+ 关注");
        } else {
            poster_details_follow_btn.setText("- 关注");
        }

        //private ImageView poster_details_share_icon;
        //private TextView poster_details_share_text;

        if (posterData.has_like == 0) {//1=已点赞 ,0=未点赞
            poster_details_like_iv.setImageResource(R.mipmap.ic_praise);
        } else {
            poster_details_like_iv.setImageResource(R.mipmap.ic_praise_selected);
        }
        if (posterData.has_collect == 0) {//1=已收藏,0=未收藏
            poster_details_collection_iv.setImageResource(R.mipmap.ic_collect);
        } else {
            poster_details_collection_iv.setImageResource(R.mipmap.ic_collect_selected);
        }

        poster_details_like_num.setText("" + posterData.like_num);
        poster_details_collection_num.setText("" + posterData.collect_num);
        poster_details_forward_num.setText("" + posterData.share_num);
        poster_details_comment_num.setText("" + posterData.comment_num);

        poster_details_like_num = (TextView) findViewById(R.id.poster_details_like_num);
        poster_details_collection_num = (TextView) findViewById(R.id.poster_details_collection_num);
        poster_details_forward_num = (TextView) findViewById(R.id.poster_details_forward_num);
        poster_details_comment_num = (TextView) findViewById(R.id.poster_details_comment_num);

        //分享
        if (posterData.has_red_envelopes > 0) {//0=没有红包,1=有红包
            poster_details_share_layout.setVisibility(View.VISIBLE);
            poster_details_share_layout.setTag(1);
            poster_details_share_icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_envelope_on));
            if (posterData.red_envelopes_surplus_number > 0) {//红包剩余个数
                if (posterData.has_myget_red_envelopes > 0) {//1=已抢红包,0=未抢红包
                    poster_details_share_text.setText("恭喜您，红包已存入您的账户，前往查看>");
                } else {
                    poster_details_share_text.setText("转发朋友圈抢红包");
                }
            } else {
                poster_details_share_text.setText("手慢了，红包被抢完了，去账户>");
            }
        } else {
            if (posterData.has_commission > 0) {//0=没有佣金,1=有佣金
                poster_details_share_icon.setImageResource(R.mipmap.poster_commission);
                poster_details_share_text.setText("分享赚佣");
                poster_details_share_layout.setVisibility(View.VISIBLE);
                poster_details_share_layout.setTag(1);
            } else {
                poster_details_share_layout.setVisibility(View.GONE);
                poster_details_share_layout.setTag(0);
            }
        }

        //WebView加载web资源
        webView.loadUrl("" + posterData.url);//http://mp.weixin.qq.com/s/G13jeKa7bhrHDyML9ZQ5PQ

    }

    /**
     * 关注
     */
    private void toFollow() {
        if (posterData.has_flow == 0) {//1=已关注 ,0=未关注
            if (SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID).equals(posterData.uid + "")) {
                Toast.show(activity, "不能关注自己哦！");
                return;
            }
            mPosterDataManager.loadFollowPoster(activity, "" + posterData.uid, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//关注失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "关注成功");
                    poster_details_follow_btn.setText("- 关注");
                    posterData.has_flow = 1;
                }
            });
        } else {
            mPosterDataManager.loadUnFollowPoster(activity, "" + posterData.uid, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//取消点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "取消关注成功");
                    poster_details_follow_btn.setText("+ 关注");
                    posterData.has_flow = 0;
                }
            });
        }
    }


    /**
     * 点赞
     */
    private void toLike() {
        if (posterData.has_like == 0) {//1=已点赞 ,0=未点赞
            mPosterDataManager.loadLikePoster(activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    EventBus.getDefault().post(new EventBean(EventBean.PRAISE_ID, 1));
                    Toast.show(activity, "点赞成功");
                    posterData.has_like = 1;
                    poster_details_like_iv.setImageResource(R.mipmap.ic_praise_selected);
                    posterData.like_num++;
                    poster_details_like_num.setText("" + posterData.like_num);
                }
            });
        } else {
            mPosterDataManager.loadUnLikePoster(activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//取消点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    EventBus.getDefault().post(new EventBean(EventBean.PRAISE_ID, 0));
                    Toast.show(activity, "取消点赞成功");
                    posterData.has_like = 0;
                    poster_details_like_iv.setImageResource(R.mipmap.ic_praise);
                    posterData.like_num--;
                    poster_details_like_num.setText("" + posterData.like_num);
                }
            });
        }
    }

    /**
     * 收藏
     */
    private void toCollection() {
        if (posterData.has_collect == 0) {//1=已收藏,0=未收藏
            mPosterDataManager.loadCollectPoster(activity, news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//收藏失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "收藏成功");
                    posterData.has_collect = 1;
                    poster_details_collection_iv.setImageResource(R.mipmap.ic_collect_selected);
                    posterData.collect_num++;
                    poster_details_collection_num.setText("" + posterData.collect_num);
                }
            });
        } else {
            mPosterDataManager.loadUnCollectPoster(activity, news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//取消收藏失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "取消收藏成功");
                    posterData.has_collect = 0;
                    poster_details_collection_iv.setImageResource(R.mipmap.ic_collect);
                    posterData.collect_num--;
                    poster_details_collection_num.setText("" + posterData.collect_num);
                }
            });

        }
    }

    /**
     * 进入账户界面
     */
    private void toUserActivity() {
//        Toast.show(activity, "进入账户界面");
        startActivity(new Intent(activity, MainActivity.class).putExtra(MainActivity.KEY_SKIP, MainActivity.TO_BALANCE));
    }

    /**
     * 显示分享窗口
     */
    private void showShareDialog(int type) {
        mShareDialog.show(posterData, type);
    }

    /**
     * 隐藏分享窗口
     */
    private void hideShareDialog() {
        if (mShareDialog != null) {
            mShareDialog.dismiss();
        }
    }

    /**
     * 显示红包窗口
     */
    private void showRedbagDialog() {
        redbagDialog.show(posterData, new RedbagDialog.OnRedbagListener() {
            @Override
            public void onConfirm() {
                posterData.has_myget_red_envelopes = 1;
                poster_details_share_text.setText("恭喜您，红包已存入您的账户，前往查看>");
            }
        });
    }

    /**
     * 分享订单广播
     */
    public class ShareBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            switch (intent.getAction()) {
                case BroadcastAction.WX_CANCEL_BRODCAST://微信分享 - 取消
                    Toast.show(activity, "微信分享 - 取消");
                    hideShareDialog();
                    break;
                case BroadcastAction.WX_REFUSE_BRODCAST://微信分享 - 拒绝
                    Toast.show(activity, "微信分享 - 拒绝");
                    hideShareDialog();
                    break;
                case BroadcastAction.WX_NONSUPPORT_BRODCAST://微信分享 - 不支持
                    Toast.show(activity, "微信分享 - 不支持");
                    hideShareDialog();
                    break;
                case BroadcastAction.WX_UNKNOWN_ERREOR_BRODCAST://微信分享 - 未知错误
                    Toast.show(activity, "微信分享 - 未知错误");
                    hideShareDialog();
                    break;
                case BroadcastAction.WX_SUCCESS_BRODCAST://微信分享 - 成功
                    //Toast.show(activity,"微信分享成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //分享
                            if (posterData.has_red_envelopes > 0) {//0=没有红包,1=有红包
                                if (posterData.red_envelopes_surplus_number > 0) {//红包剩余个数
                                    if (posterData.has_myget_red_envelopes > 0) {//1=已抢红包,0=未抢红包
                                        //恭喜您，红包已存入您的账户，前往查看>
                                    } else {
                                        showRedbagDialog();
                                    }
                                } else {
                                    //手慢了，红包被抢完了，去账户>
                                }
                            }
                            hideShareDialog();
                        }
                    }, 1000);
                    recordSharePoster();
                    break;
            }
        }
    }

    /**
     * 注册分享广播
     */
    public void opendShareBroadcast() {
        //注册广播
        if (mShareBroadcast == null) {
            mShareBroadcast = new ShareBroadcast();
        }
        Print.println("开启接收分享广播");
        BroadcastManager.init(this);
        BroadcastManager.registerReceiver(mShareBroadcast, BroadcastAction.WX_CANCEL_BRODCAST, BroadcastAction.WX_REFUSE_BRODCAST,
                BroadcastAction.WX_NONSUPPORT_BRODCAST, BroadcastAction.WX_UNKNOWN_ERREOR_BRODCAST, BroadcastAction.WX_SUCCESS_BRODCAST);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.unregisterReceiver(mShareBroadcast);
    }

    /**
     * 记录分享次数
     */
    private void recordSharePoster() {
        mPosterDataManager.loadRecordSharePoster(activity, news_id, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg + "");
            }

            @Override
            public void onResult(int tag, Object object) {
                //Toast.show(activity,"记录分享次数成功");
            }
        });

    }


}
