package com.poso2o.lechuan.fragment.poster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.poster.PosterCommentActivity;
import com.poso2o.lechuan.activity.poster.PosterCommissionActivity;
import com.poso2o.lechuan.activity.poster.PosterDetailsActivity;
import com.poso2o.lechuan.activity.poster.PosterRedBagActivity;
import com.poso2o.lechuan.activity.poster.OpenHomeActivity;
import com.poso2o.lechuan.adapter.PosterAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OpenPublishDataManager;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PullZoomScrollView;
import com.poso2o.lechuan.views.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luo on 2017-11-25.
 */

public class OpenPublishFragment extends BaseFragment {
    //网络管理
    private OpenPublishDataManager mOpenPublishDataManager;
    private PosterDataManager mPosterDataManager;
    //    //页码数据
//    private PosterTotalBean mPosterTotalBean = null;
    private int currentPage = 1;
    private int totalPage = 1;
    private String uid = "";
    /**
     * 列表
     */
    private RecyclerView open_publish_recycler;
    private RecyclerView.LayoutManager open_publish_manager;
    private PosterAdapter posterAdapter;
    private ArrayList<PosterDTO> posterDTOs = new ArrayList<>();
    private MyViewPager myViewPager;

    public void setViewPager(MyViewPager myViewPager) {
        this.myViewPager = myViewPager;
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_publish, container, false);
    }

    /**
     * 初始化视图
     */
    public void initView() {
        //列表
        open_publish_recycler = (RecyclerView) view.findViewById(R.id.open_publish_recycler);
        open_publish_manager = new LinearLayoutManager(context);
        open_publish_recycler.setLayoutManager(open_publish_manager);
//        open_publish_refresh = (SwipeRefreshLayout) view.findViewById(R.id.open_publish_refresh);
//        open_publish_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mOpenPublishDataManager = OpenPublishDataManager.getOpenPublishDataManager();
        mPosterDataManager = PosterDataManager.getPosterDataManager();
        uid = getArguments().getString("uid");
        int userType = getArguments().getInt("user_type");
        myViewPager.setObjectForPosition(view, userType == Constant.MERCHANT_TYPE ? 1 : 0);

        //广告列表
        posterAdapter = new PosterAdapter(context, new PosterAdapter.OnItemListener() {
            @Override
            public void onClickItem(int position,PosterDTO posterData) {
                itemOnClickListenner(position,posterData);
            }

            @Override
            public void onClickUser(PosterDTO posterData) {
//                itemOnClickUserListenner(posterData);
            }

            @Override
            public void onClickReward(int position,PosterDTO posterData) {
                itemOnClickRewardListenner(position,posterData);
            }

            @Override
            public void onClickBrowse(PosterDTO posterData) {
                itemOnClickBrowseListenner(posterData);
            }

            @Override
            public void onClickLike(PosterDTO posterData, int position) {
                itemOnClickLikeListenner(posterData, position);
            }

            @Override
            public void onClickComment(int position,PosterDTO posterData) {
                itemOnClickCommentListenner(position,posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        open_publish_recycler.setLayoutManager(linearLayoutManager);
        open_publish_recycler.setAdapter(posterAdapter);

        //获取广告数据
        //loadPosterData(1);

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
//        // 刷新
//        open_publish_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPosterData(1);
//            }
//        });
        // 滚动监听
        open_publish_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 滚动到底部加载更多
//                if (open_publish_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
//                        && posterAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
//                    Print.println("XXX:" + mPosterTotalBean.currPage);
//                    Print.println("XXX:" + mPosterTotalBean.pages);
//                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
//                        loadPosterData(mPosterTotalBean.currPage + 1);
//                    } else {
//                        Toast.show(context, "没有更多数据了");
//                    }
//                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });
    }

    public void loadNext() {
        if (currentPage < totalPage) {
            ++currentPage;
            loadPosterData(currentPage);
        }
    }

    public void loadPosterData(int currPage) {
        loadPosterData(currPage, null);
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData(int currPage, final PullZoomScrollView scrollView) {
        WaitDialog.showLoaddingDialog(context);
        currentPage = currPage;
        mOpenPublishDataManager.loadOpenPublishData((BaseActivity) context, currPage + "", uid, new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
//                open_publish_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
//                open_publish_refresh.setRefreshing(false);
                totalPage = posterQueryDTO.total.pages;
                if (posterQueryDTO.list != null) {
                    refreshItem(posterQueryDTO.list);
//                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterQueryDTO.list.size() + " 条商品数据" + posterQueryDTO.total.currPage);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
                if (scrollView != null) {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterDTO> posterData) {
        if (currentPage == 1) {
            posterDTOs.clear();
//            posterAdapter.notifyData(posterData);
//        } else {
//            posterAdapter.addItems(posterData);
        }
        posterDTOs.addAll(posterData);
        posterAdapter.notifyData(posterDTOs);
    }

    /**
     * 点击广告列表监听
     */
    public void itemOnClickListenner(int position,PosterDTO posterData) {
        this.position = position;
        this.posterData = posterData;
        Bundle bundle = new Bundle();
        bundle.putString("news_id", posterData.news_id.toString());
        startActivity(new Intent(context, PosterDetailsActivity.class).putExtras(bundle));
    }

    /**
     * 点击用户监听
     */
    public void itemOnClickUserListenner(PosterDTO posterData) {
        //Toast.show(context,"跳转用户中心");
        Bundle bundle = new Bundle();
        bundle.putString("uid", posterData.uid.toString());
        bundle.putInt("user_type", posterData.user_type);
        bundle.putString("nick", posterData.nick.toString());
        bundle.putString("logo", posterData.logo.toString());
        bundle.putInt("has_flow", posterData.has_flow);
        startActivity(new Intent(context, OpenHomeActivity.class).putExtras(bundle));
    }

    /**
     * 点击佣金、红包监听
     */
    public void itemOnClickRewardListenner(int position,PosterDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("news_id", posterData.news_id.toString());
        bundle.putString("news_img", posterData.news_img.toString());
        bundle.putString("news_title", posterData.news_title.toString());
        bundle.putLong("build_time", posterData.build_time);
        if (posterData.has_red_envelopes > 0) {//0=没有红包,1=有红包
            this.position = position;
            this.posterData = posterData;
            bundle.putInt("has_myget_red_envelopes", posterData.has_myget_red_envelopes);// 是否已抢红包 1=已抢红包,0=未抢红包
            bundle.putInt("red_envelopes_number", posterData.red_envelopes_number);// 红包总个数
            bundle.putInt("red_envelopes_surplus_number", posterData.red_envelopes_surplus_number);// 红包剩余个数
            startActivity(new Intent(context, PosterRedBagActivity.class).putExtras(bundle));
        } else {
            if (posterData.has_commission > 0) {//0=没有佣金,1=有佣金
                this.position = position;
                this.posterData = posterData;
                bundle.putDouble("goods_price", posterData.goods_price);
                bundle.putDouble("goods_commission_rate", posterData.goods_commission_rate);
                bundle.putDouble("goods_commission_amount", posterData.goods_commission_amount);
                startActivity(new Intent(context, PosterCommissionActivity.class).putExtras(bundle));
            }
        }
    }

    /**
     * 点击浏览监听
     */
    public void itemOnClickBrowseListenner(PosterDTO posterData) {
        //Toast.show(context,"点击浏览监听");
    }

    /**
     * 点击赞监听
     */
    public void itemOnClickLikeListenner(final PosterDTO posterData, final int position) {
        //Toast.show(context,"点击赞监听");
        if (posterData.has_like == 0) {//1=已点赞 ,0=未点赞
            mPosterDataManager.loadLikePoster((BaseActivity) context, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg + "");//点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(context, "点赞成功");
                    posterData.has_like = 1;
                    posterData.like_num++;
                    posterAdapter.notifyItemChanged(position, "poster");
                }
            });
        } else {
            mPosterDataManager.loadUnLikePoster((BaseActivity) context, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(context, msg + "");//取消点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(context, "取消点赞成功");
                    posterData.has_like = 0;
                    posterData.like_num--;
                    posterAdapter.notifyItemChanged(position, "poster");
                }
            });
        }
    }

    private int position = -1;
    private PosterDTO posterData;
    /**
     * 点击评价监听
     */
    public void itemOnClickCommentListenner(int position,PosterDTO posterData) {
        this.position = position;
        this.posterData = posterData;
        Bundle bundle = new Bundle();
        bundle.putString("news_id", "" + posterData.news_id);
        startActivity(new Intent(context, PosterCommentActivity.class).putExtras(bundle));
    }


    /**
     * 增加一个评论数量后刷新
     */
    public void refreshCommentNumber() {
        if (position != -1 && posterData != null) {
            posterData.comment_num += 1;
            posterAdapter.notifyItemChanged(position, posterData);
        }
    }

    /**
     * 刷新赞
     */
    public void refreshPraiseNumber(boolean bool) {
        if (position != -1 && posterData != null) {
            if (bool) {//赞
                posterData.like_num += 1;
                posterData.has_like = 1;
            } else if (posterData.like_num > 0) {//取消赞
                posterData.like_num -= 1;
                posterData.has_like = 0;
            }
            posterAdapter.notifyItemChanged(position, posterData);
        }
    }

    /**
     * 刷新红包状态，抢过后即时刷新成灰色
     */
    public void refreshRedBagStatus() {
        if (position != -1 && posterData != null) {
            posterData.has_myget_red_envelopes = 1;
            if (posterData.red_envelopes_surplus_number > 0) {
                posterData.red_envelopes_surplus_number -= 1;
            }
            posterAdapter.notifyItemChanged(position, posterData);
        }
    }

}
