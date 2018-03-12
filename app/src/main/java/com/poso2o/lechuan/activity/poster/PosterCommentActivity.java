package com.poso2o.lechuan.activity.poster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PosterCommentAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.bean.poster.PosterCommentsQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterCommentsTotalBean;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.bean.poster.RedbagDetailsBean;
import com.poso2o.lechuan.dialog.RedbagDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterCommentDataManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 文章评论视图
 */
public class PosterCommentActivity extends BaseActivity implements View.OnLayoutChangeListener {
    //网络管理
    private PosterCommentDataManager mPosterCommentDataManager;
    //页码数据
    private PosterCommentsTotalBean mPosterTotalBean = null;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    //返回
    private ImageView poster_details_back;
    //加载图片
    private RequestManager glideRequest;
    //文章ID
    private String news_id;
    //写评论内容
    private EditText poster_details_comment_et;
    private TextView poster_details_publish_comment;
    /**
     * 列表
     */
    private RecyclerView my_fans_recycler;
    private RecyclerView.LayoutManager my_fans_manager;
    private PosterCommentAdapter myFollowAdapter;
    private SwipeRefreshLayout my_follow_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_poster_comment;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        //滚动控制
        activityRootView = findViewById(R.id.poster_details_root_layout);
        poster_details_back = (ImageView) findViewById(R.id.poster_details_back);

        //写评论内容
        poster_details_comment_et = (EditText) findViewById(R.id.poster_details_comment_et);
        poster_details_publish_comment = (TextView) findViewById(R.id.poster_details_publish_comment);

        //列表
        my_fans_recycler = (RecyclerView) findViewById(R.id.poster_details_comment_recycler);
        my_fans_manager = new LinearLayoutManager(activity);
        my_fans_recycler.setLayoutManager(my_fans_manager);
        my_follow_refresh = (SwipeRefreshLayout) findViewById(R.id.poster_details_comment_refresh);
        my_follow_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mPosterCommentDataManager = PosterCommentDataManager.getPosterDataManager();

        //加载图片
        glideRequest = Glide.with(activity);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        // 上一级页面传来的数据
        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");
        //列表
        myFollowAdapter = new PosterCommentAdapter(activity, new PosterCommentAdapter.OnItemListener() {
            @Override
            public void onClickItem(PosterCommentsDTO posterData) {
                //itemOnClickListenner(posterData);
                //显示评论内容
                showCommentLayout("回复@" + posterData.nick + ":");
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_fans_recycler.setLayoutManager(linearLayoutManager);
        my_fans_recycler.setAdapter(myFollowAdapter);

        //获取数据
        loadPosterData(1);

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
        // 刷新
        my_follow_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        //发布评论
        poster_details_publish_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                addPosterComment();
            }
        });
        // 滚动监听
        my_fans_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (my_follow_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && myFollowAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
                    Print.println("XXX:" + mPosterTotalBean.currPage);
                    Print.println("XXX:" + mPosterTotalBean.pages);
                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
                        loadPosterData(mPosterTotalBean.currPage + 1);
                    } else {
                        Toast.show(activity, "没有更多数据了");
                    }
                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });

        //评论内容
        poster_details_comment_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    // 每次editText有变化的时候，则移除上次发出的延迟线程
                    search_handler.removeCallbacks(delayRun);
                }
                // 延迟800ms，如果不再输入字符，则执行该线程的run方法
                search_handler.postDelayed(delayRun, 500);
            }
        });
    }

    /**
     * 显示评论内容
     */
    private void showCommentLayout(String comment) {
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
            //poster_details_publish_comment_layout.setVisibility(View.VISIBLE);
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
    public void loadPosterData(int currPage) {
        my_follow_refresh.setRefreshing(true);
        if (currPage == 1) {
            //my_fans_recycler.smoothScrollToPosition(0);
        }
        mPosterCommentDataManager.loadPosterCommentData(activity, news_id, "" + currPage, new IRequestCallBack<PosterCommentsQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                my_follow_refresh.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, PosterCommentsQueryDTO posterCommentsQueryDTO) {
                my_follow_refresh.setRefreshing(false);
                mPosterTotalBean = posterCommentsQueryDTO.total;
                if (posterCommentsQueryDTO.list != null) {
                    refreshItem(posterCommentsQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterCommentsQueryDTO.list.size() + " 条商品数据" + posterCommentsQueryDTO.total.currPage);
                } else {
                    Toast.show(activity, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterCommentsDTO> posterCommentsDTO) {
        if (mPosterTotalBean.currPage == 1) {
            myFollowAdapter.notifyData(posterCommentsDTO);
        } else {
            myFollowAdapter.addItems(posterCommentsDTO);
        }
        //myFollowAdapter.notifyData(posterData);
    }

    /**
     * 添加广告评论
     */
    private void addPosterComment() {
        if (poster_details_comment_et.getText().toString().isEmpty()) {
            Toast.show(activity, "请输入评论内容！");
            return;
        }
        my_follow_refresh.setRefreshing(true);
        mPosterCommentDataManager.addPosterComment(activity, news_id, poster_details_comment_et.getText().toString(), new IRequestCallBack<PosterCommentsDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                my_follow_refresh.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, PosterCommentsDTO posterCommentsDTO) {
                EventBus.getDefault().post(new EventBean(EventBean.COMMENT_ID));
                my_follow_refresh.setRefreshing(false);
                poster_details_comment_et.setText("");
                KeyboardUtils.hideSoftInput(poster_details_comment_et);
                my_fans_recycler.smoothScrollToPosition(0);
                loadPosterData(1);
            }
        });
    }

    private Handler search_handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            // 在这里调用服务器的接口，获取数据
            //评论内容
            if (poster_details_comment_et.getText().toString().isEmpty()) {
                poster_details_publish_comment.setEnabled(false);
                poster_details_publish_comment.setTextColor(getResources().getColor(R.color.color_DDDDDD));
            } else {
                poster_details_publish_comment.setEnabled(true);
                poster_details_publish_comment.setTextColor(getResources().getColor(R.color.orange_frame));
            }
        }
    };

}
