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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.activity.poster.MyScrollViewActivity;
import com.poso2o.lechuan.activity.poster.OpenHomeActivity;
import com.poso2o.lechuan.activity.poster.PosterCommentActivity;
import com.poso2o.lechuan.activity.poster.PosterCommissionActivity;
import com.poso2o.lechuan.activity.poster.PosterDetailsActivity;
import com.poso2o.lechuan.activity.poster.PosterRedBagActivity;
import com.poso2o.lechuan.activity.poster.PosterSearchActivity;
import com.poso2o.lechuan.activity.realshop.OpenShopActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.adapter.PosterAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-25.
 */

public class PosterFragment extends BaseFragment {
    //返回、搜索、菜单
    private ImageView poster_back, poster_search, poster_left_menu;
    //网络管理
    private PosterDataManager mPosterDataManager;
    //页码数据
    private PosterTotalBean mPosterTotalBean = null;

    /**
     * 列表
     */
    private RecyclerView poster_recycler;
    private RecyclerView.LayoutManager poster_manager;
    private PosterAdapter posterAdapter;
    private SwipeRefreshLayout poster_refresh;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_poster, container, false);
    }

    /**
     * 初始化视图
     */
    public void initView() {
        //返回、搜索、菜单
        poster_back = (ImageView) view.findViewById(R.id.poster_back);
        poster_search = (ImageView) view.findViewById(R.id.poster_search);
        poster_left_menu = (ImageView) view.findViewById(R.id.poster_left_menu);

        //列表
        poster_recycler = (RecyclerView) view.findViewById(R.id.poster_recycler);
        poster_manager = new LinearLayoutManager(context);
        poster_recycler.setLayoutManager(poster_manager);
        poster_refresh = (SwipeRefreshLayout) view.findViewById(R.id.poster_refresh);
        poster_refresh.setColorSchemeColors(Color.RED);

    }

    /**
     * 初始化数据
     */
    public void initData() {
        mPosterDataManager = PosterDataManager.getPosterDataManager();
        //广告列表
        posterAdapter = new PosterAdapter(context, new PosterAdapter.OnItemListener() {
            @Override
            public void onClickItem(int position, PosterDTO posterData) {
                itemOnClickListenner(position, posterData);
            }

            @Override
            public void onClickUser(PosterDTO posterData) {
                itemOnClickUserListenner(posterData);
            }

            @Override
            public void onClickReward(int position, PosterDTO posterData) {
                itemOnClickRewardListenner(position, posterData);
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
            public void onClickComment(int position, PosterDTO posterData) {
                itemOnClickCommentListenner(position, posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        poster_recycler.setLayoutManager(linearLayoutManager);
        poster_recycler.setAdapter(posterAdapter);

        //获取广告数据
        loadPosterData(1);

    }

    /**
     * 初始化监听
     */
    @Override
    public void initListener() {
        //返回
        poster_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //((MainActivity) getActivity()).finish();
                Intent intent = new Intent();
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 1 || (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP) == 1 && SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY) == 3)) {
                    intent.setClass(context, RShopMainActivity.class);
                } else {
                    intent.setClass(context, OpenShopActivity.class);
                }
                startActivity(intent);
//                Toast.show(context,"打开主菜单页面");
            }
        });
        //搜索
        poster_search.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(context,"搜索");
//                Bundle bundle=new Bundle();
//                startActivity(new Intent(context, MyScrollViewActivity.class).putExtra("uid","13423678930"));
                Bundle bundle = new Bundle();
                startActivity(new Intent(context, PosterSearchActivity.class).putExtras(bundle));
            }
        });
        //菜单
        poster_left_menu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //DrawerLayout.openDrawer(GravityCompat.START);//打开侧滑菜单
                ((MainActivity) getActivity()).openRightDrawer();
            }
        });
        // 刷新
        poster_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        poster_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (poster_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && posterAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
                    Print.println("XXX:" + mPosterTotalBean.currPage);
                    Print.println("XXX:" + mPosterTotalBean.pages);
                    if (mPosterTotalBean.pages >= mPosterTotalBean.currPage + 1) {
                        loadPosterData(mPosterTotalBean.currPage + 1);
                    } else {
                        Toast.show(context, "没有更多数据了");
                    }
                }
            }

            private boolean isSlideToBottom(RecyclerView recyclerView) {
                if (recyclerView == null) return false;
                return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
            }
        });
    }

    /**
     * 获取广告数据
     */
    public void loadPosterData(int currPage) {
        Print.println("loadPosterData_" + currPage);
        poster_refresh.setRefreshing(true);
        mPosterDataManager.loadPosterData((BaseActivity) context, currPage + "", "", new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                poster_refresh.setRefreshing(false);
                Toast.show(context, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                poster_refresh.setRefreshing(false);
                mPosterTotalBean = posterQueryDTO.total;
                if (posterQueryDTO.list != null) {
                    refreshItem(posterQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterQueryDTO.list.size() + " 条商品数据" + posterQueryDTO.total.currPage);
                } else {
                    Toast.show(context, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterDTO> posterData) {
        if (mPosterTotalBean.currPage == 1) {
            posterAdapter.notifyData(posterData);
        } else {
            posterAdapter.addItems(posterData);
        }
    }

    /**
     * 点击广告列表监听
     */
    public void itemOnClickListenner(int position, PosterDTO posterData) {
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
        startActivity(new Intent(context, MyScrollViewActivity.class).putExtras(bundle));
    }

    /**
     * 点击佣金、红包监听
     */
    public void itemOnClickRewardListenner(int position, PosterDTO posterData) {
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
    public void itemOnClickCommentListenner(int position, PosterDTO posterData) {
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
