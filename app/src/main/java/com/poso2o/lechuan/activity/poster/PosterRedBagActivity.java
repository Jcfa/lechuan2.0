package com.poso2o.lechuan.activity.poster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PosterRedbagAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterRedBagDTO;
import com.poso2o.lechuan.bean.poster.PosterRedBagQueryDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterRedBagDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.glide.GlideRoundTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 红包领取详情页面
 */
public class PosterRedBagActivity extends BaseActivity {
    private Context context;
    //网络管理
    private PosterRedBagDataManager mPosterRedBagDataManager;
    private RequestManager glideRequest;
    //返回
    private ImageView red_bag_details_back;
    //去抢红包
    private LinearLayout poster_details_share_layout;
    private TextView poster_details_share;
    private ImageView red_bag_details_goods_img;
    private TextView red_bag_details_goods_name;
    private TextView red_bag_details_goods_dat;
    //红包总个数
    private int red_envelopes_number = 0;
    private int red_envelopes_surplus_number = 0;
    private int has_myget_red_envelopes = 0;
    //文章ID
    private String news_id;
    /**
     * 列表
     */
    private RecyclerView red_bag_details_recycler;
    private RecyclerView.LayoutManager red_bag_details_manager;
    private PosterRedbagAdapter red_bag_detailsAdapter;
    private SwipeRefreshLayout red_bag_details_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_red_bag_details;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        red_bag_details_back = (ImageView) findViewById(R.id.red_bag_details_back);
        //列表
        red_bag_details_recycler = (RecyclerView) findViewById(R.id.red_bag_details_recycler);
        red_bag_details_manager = new LinearLayoutManager(context);
        red_bag_details_recycler.setLayoutManager(red_bag_details_manager);
        red_bag_details_refresh = (SwipeRefreshLayout) findViewById(R.id.red_bag_details_refresh);
        red_bag_details_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        //去抢红包
        poster_details_share_layout = (LinearLayout) findViewById(R.id.poster_details_share_layout);
        poster_details_share = (TextView) findViewById(R.id.poster_details_share);
        red_bag_details_goods_img = (ImageView) findViewById(R.id.red_bag_details_goods_img);
        red_bag_details_goods_name = (TextView) findViewById(R.id.red_bag_details_goods_name);
        red_bag_details_goods_dat = (TextView) findViewById(R.id.red_bag_details_goods_dat);


    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        this.context = getApplicationContext();
        mPosterRedBagDataManager = PosterRedBagDataManager.getRedBagDataManager();
        glideRequest = Glide.with(context);

        // 上一级页面传来的数据
        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");
        String news_img = intent.getStringExtra("news_img");
        String news_title = intent.getStringExtra("news_title");
        Long build_time = intent.getLongExtra("build_time", 0L);
        has_myget_red_envelopes = intent.getIntExtra("has_myget_red_envelopes", 1);//1=已抢红包,0=未抢红包
        red_envelopes_number = intent.getIntExtra("red_envelopes_number", 1);// 红包总个数
        red_envelopes_surplus_number = intent.getIntExtra("red_envelopes_surplus_number", 1);// 红包剩余个数
        if (has_myget_red_envelopes == 1) {//1=已抢红包,0=未抢红包
            poster_details_share.setText("剩余" + red_envelopes_surplus_number + "个，" + (red_envelopes_number - red_envelopes_surplus_number) + "人已抢到");
        } else {
            poster_details_share.setText("剩余" + red_envelopes_surplus_number + "个，" + (red_envelopes_number - red_envelopes_surplus_number) + "人已抢到，去抢红包 >");
        }

        if (news_img.isEmpty()) {
            red_bag_details_goods_img.setImageResource(R.mipmap.logo_c);
        } else {
            glideRequest.load("" + news_img).transform(new GlideRoundTransform(context, 5)).into(red_bag_details_goods_img);
            //glideRequest.load("" + news_img).transform(new GlideCircleTransform(context)).into(red_bag_details_goods_img);
        }
        red_bag_details_goods_name.setText("" + news_title);
        String date = DateTimeUtil.LongToData(build_time, "yyyy-MM-dd HH:mm:ss");
        red_bag_details_goods_dat.setText("" + date);

        //广告列表
        red_bag_detailsAdapter = new PosterRedbagAdapter(context, new PosterRedbagAdapter.OnItemListener() {
            @Override
            public void onClickItem(PosterRedBagDTO posterData) {
                //itemOnClickListenner(posterData);
            }

            @Override
            public void onClickReward(PosterRedBagDTO posterData) {
                //itemOnonClickRewardListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        red_bag_details_recycler.setLayoutManager(linearLayoutManager);
        red_bag_details_recycler.setAdapter(red_bag_detailsAdapter);

        //获取广告数据
        loadPosterData();
    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        red_bag_details_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //去抢红包
        poster_details_share_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("news_id", news_id.toString());
                startActivity(new Intent(context, PosterDetailsActivity.class).putExtras(bundle));
            }
        });
        // 刷新
        red_bag_details_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData();
            }
        });
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData() {
        red_bag_details_refresh.setRefreshing(true);
        mPosterRedBagDataManager.loadPosterRedBagList(activity, news_id, new IRequestCallBack<PosterRedBagQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                red_bag_details_refresh.setRefreshing(false);
            }

            @Override
            public void onResult(int tag, PosterRedBagQueryDTO posterRedBagDTO) {
                red_bag_details_refresh.setRefreshing(false);
                if (posterRedBagDTO.redenvelopes != null) {
                    refreshItem(posterRedBagDTO.redenvelopes);
                } else {
                    Toast.show(activity, "没有任何信息！");
                }

            }
        });
    }

    /**
     * 刷新列表信息
     *
     * @param posterData
     */
    private void refreshItem(ArrayList<PosterRedBagDTO> posterData) {
        red_bag_details_refresh.setRefreshing(false);
        red_bag_detailsAdapter.notifyData(posterData);
        if (posterData != null && posterData.size() > 0) {//刷新列表后更新剩余数量
            red_envelopes_surplus_number = red_envelopes_number - posterData.size();
            if (has_myget_red_envelopes == 1) {//1=已抢红包,0=未抢红包
                poster_details_share.setText("剩余" + red_envelopes_surplus_number + "个，" + (red_envelopes_number - red_envelopes_surplus_number) + "人已抢到");
            } else {
                poster_details_share.setText("剩余" + red_envelopes_surplus_number + "个，" + (red_envelopes_number - red_envelopes_surplus_number) + "人已抢到，去抢红包 >");
            }
        }
    }
}
