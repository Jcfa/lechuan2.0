package com.poso2o.lechuan.activity.poster;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.ShopCommentAdapter;
import com.poso2o.lechuan.adapter.ShopCommentImgAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.GoodsAppraiseDTO;
import com.poso2o.lechuan.bean.poster.PosterCommentsDTO;
import com.poso2o.lechuan.bean.poster.ShopDetailsCommentsDTO;
import com.poso2o.lechuan.bean.poster.ShopDetailsCommentsQueryDTO;
import com.poso2o.lechuan.bean.poster.ShopDetailsCommentsTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.ShopCommentDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import android.view.LayoutInflater;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

/**
 * 店评详情视图
 */
public class ShopCommentActivity extends BaseActivity implements View.OnLayoutChangeListener {
    //店评数据
    private GoodsAppraiseDTO posterData;
    //页码数据
    private ShopDetailsCommentsTotalBean mPosterTotalBean = null;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    //返回
    private ImageView shop_comment_back;
    private LinearLayout shop_comment_img_layout;
    //加载图片
    private RequestManager glideRequest;
    //写评论内容
    private EditText shop_comment_comment_et;
    private TextView shop_comment_publish_comment;
    //网络管理
    private ShopCommentDataManager mShopCommentDataManager;
    private ImageView shop_comment_user_logo;
    private TextView shop_comment_user_name;
    private TextView shop_comment_title;
    private NestedScrollView mNestedScrollView;

    /**
     * 图片列表
     */
    private RecyclerView shop_comment_img_recycler;
    private RecyclerView.LayoutManager shop_comment_img_manager;
    private ShopCommentImgAdapter mShopCommentImgAdapter;

    /**
     * 列表
     */
    private RecyclerView my_fans_recycler;
    private RecyclerView.LayoutManager my_fans_manager;
    private ShopCommentAdapter myFollowAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_shop_comment;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        //滚动控制
        activityRootView = findViewById(R.id.shop_comment_root_layout);
        shop_comment_back = (ImageView) findViewById(R.id.shop_comment_back);
        shop_comment_img_layout = (LinearLayout) findViewById(R.id.shop_comment_img_layout);

        //写评论内容
        shop_comment_comment_et = (EditText) findViewById(R.id.shop_comment_comment_et);
        shop_comment_publish_comment = (TextView) findViewById(R.id.shop_comment_publish_comment);

        shop_comment_user_logo = (ImageView) findViewById(R.id.shop_comment_user_logo);
        shop_comment_user_name = (TextView) findViewById(R.id.shop_comment_user_name);
        shop_comment_title = (TextView) findViewById(R.id.shop_comment_title);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.shop_comment_scroll_view);

        //图片列表
        shop_comment_img_recycler = (RecyclerView) findViewById(R.id.shop_comment_img_recycler);
        shop_comment_img_manager = new LinearLayoutManager(activity);
        shop_comment_img_recycler.setLayoutManager(my_fans_manager);

        //列表
        my_fans_recycler = (RecyclerView) findViewById(R.id.shop_comment_comment_recycler);
        my_fans_manager = new LinearLayoutManager(activity);
        my_fans_recycler.setLayoutManager(my_fans_manager);

    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        my_fans_recycler.setNestedScrollingEnabled(false);

        mShopCommentDataManager = ShopCommentDataManager.getShopCommentDataManager();

        //加载图片
        glideRequest = Glide.with(activity);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        // 上一级页面传来的数据
        Intent intent = getIntent();
        posterData = (GoodsAppraiseDTO) intent.getSerializableExtra("ShopComment");

        glideRequest.load("" + posterData.buyer_logo).centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e)
                .transform(new GlideCircleTransform(activity)).into(shop_comment_user_logo);
        shop_comment_user_name.setText("" + posterData.buyer_nick);
        shop_comment_title.setText("" + posterData.appraise_remark);

        //图片列表
        mShopCommentImgAdapter = new ShopCommentImgAdapter(activity);
        LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(activity);
        linearLayoutManagerX.setOrientation(LinearLayout.VERTICAL);
        shop_comment_img_recycler.setLayoutManager(linearLayoutManagerX);
        shop_comment_img_recycler.setAdapter(mShopCommentImgAdapter);

        if (!posterData.appraise_imgs.isEmpty()) {
            String[] appraise_imgs = posterData.appraise_imgs.split(",");
            for (int i = 0; i < appraise_imgs.length; i++) {
                //System.out.println(appraise_imgs[i]);
                //myFollowAdapter.notifyData(posterCommentsDTO);
                //glideRequest.load(appraise_imgs[i]).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(imageView);

                //获取图片真正的宽高
                final ImageView imageView = new ImageView(activity);
                //final ImageView imageView = (ImageView) addView().findViewById(R.id.view_shop_comment_imageview_iv);
                shop_comment_img_layout.addView(imageView);

                glideRequest
                        .load(appraise_imgs[i])
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (imageView == null) {
                                    return false;
                                }
                                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                                float scale = (float) vw / (float) resource.getIntrinsicWidth();
                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                                imageView.setLayoutParams(params);
                                return false;
                            }
                        })
                        .placeholder(R.mipmap.logo_e)
                        .error(R.mipmap.logo_e)
                        .into(imageView);

                /*glideRequest
                        .load(appraise_imgs[i])
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();

                                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
                                imageView.setImageBitmap(bitmap);

                            }
                        });*/

                //View newView = addView();
                //glideRequest.load(appraise_imgs[i]).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into((ImageView) newView.findViewById(R.id.view_shop_comment_imageview_iv));
                //shop_comment_img_layout.addView(newView);
                }

        }

        //列表
        myFollowAdapter = new ShopCommentAdapter(activity, new ShopCommentAdapter.OnItemListener() {
            @Override
            public void onClickItem(ShopDetailsCommentsDTO posterData) {
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

    private View addView() {
        // TODO 动态添加布局(xml方式)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//      LayoutInflater inflater1=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      LayoutInflater inflater2 = getLayoutInflater();
        LayoutInflater inflater3 = LayoutInflater.from(activity);
        View view = inflater3.inflate(R.layout.view_shop_comment_imageview, null);
        view.setLayoutParams(lp);
        return view;

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
        //返回
        shop_comment_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //发布评论
        shop_comment_publish_comment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                addPosterComment();
            }
        });
        // 滚动监听
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                    //Toast.show(activity,"顶部");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    //Toast.show(activity,"底部");

                    // 滚动到底部加载更多
                    if (mPosterTotalBean == null) return;
                    if (myFollowAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
                        Print.println("XXX:" + mPosterTotalBean.currPage);
                        Print.println("XXX:" + mPosterTotalBean.pages);
                        if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
                            loadPosterData(mPosterTotalBean.currPage + 1);
                        } else {
                            Toast.show(activity, "没有更多数据了");
                        }
                    }

                }
            }
        });
        //评论内容
        shop_comment_comment_et.addTextChangedListener(new TextWatcher() {
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
    private void showCommentLayout(String comment){
        shop_comment_comment_et.setText("" + comment);
        shop_comment_comment_et.setSelection(shop_comment_comment_et.getText().toString().length());
        shop_comment_comment_et.requestFocus();
        KeyboardUtils.showSoftInput(shop_comment_comment_et);
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
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            //Toast.show(context,"监听到软键盘弹起...");
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            //Toast.show(context,"监听到软件盘关闭...");
            //shop_comment_publish_comment_layout.setVisibility(View.VISIBLE);
            //scrollToBottom(shop_comment_scrollView);
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
    public void loadPosterData(int currPage){
        if (currPage == 1){
            //my_fans_recycler.smoothScrollToPosition(0);
        }
        mShopCommentDataManager.loadShopDetailsCommentData(activity, "" + posterData.appraise_id, "" + currPage, new IRequestCallBack<ShopDetailsCommentsQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, ShopDetailsCommentsQueryDTO posterCommentsQueryDTO) {
                mPosterTotalBean = posterCommentsQueryDTO.total;
                if (posterCommentsQueryDTO.list != null) {
                    refreshItem(posterCommentsQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterCommentsQueryDTO.list.size() + " 条商品数据" + posterCommentsQueryDTO.total.currPage);
                }else{
                    Toast.show(activity,"没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<ShopDetailsCommentsDTO> posterCommentsDTO){
        if (mPosterTotalBean.currPage == 1) {
            myFollowAdapter.notifyData(posterCommentsDTO);
        }else{
            myFollowAdapter.addItems(posterCommentsDTO);
        }
    }

    /**
     * 添加广告评论
     */
    private void addPosterComment(){
        if (shop_comment_comment_et.getText().toString().isEmpty()){
            Toast.show(activity,"请输入评论内容！");
            return;
        }
        mShopCommentDataManager.addShopDetailsComment(activity, "" + posterData.appraise_id, shop_comment_comment_et.getText().toString(), new IRequestCallBack<PosterCommentsDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
            }
            @Override
            public void onResult(int tag, PosterCommentsDTO posterCommentsDTO) {
                //Toast.show(activity, "添加广告评论OK");
                shop_comment_comment_et.setText("");
                KeyboardUtils.hideSoftInput(shop_comment_comment_et);
                my_fans_recycler.smoothScrollToPosition(0);
                loadPosterData(1);
                mNestedScrollView.smoothScrollTo(0,my_fans_recycler.getTop());
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
            if (shop_comment_comment_et.getText().toString().isEmpty()){
                shop_comment_publish_comment.setEnabled(false);
                shop_comment_publish_comment.setTextColor(getResources().getColor(R.color.color_DDDDDD));
            }else{
                shop_comment_publish_comment.setEnabled(true);
                shop_comment_publish_comment.setTextColor(getResources().getColor(R.color.orange_frame));
            }
        }
    };

}
