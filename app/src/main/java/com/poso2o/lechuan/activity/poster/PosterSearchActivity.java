package com.poso2o.lechuan.activity.poster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.PosterAdapter;
import com.poso2o.lechuan.adapter.PosterSearchAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterHistoryDTO;
import com.poso2o.lechuan.bean.poster.PosterHistoryQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterQueryDTO;
import com.poso2o.lechuan.bean.poster.PosterTotalBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterDataManager;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 广告搜索页面
 */
public class PosterSearchActivity extends BaseActivity {
    //网络管理
    private PosterDataManager mPosterDataManager;
    //页码数据
    private PosterTotalBean mPosterTotalBean = null;

    //返回
    private ImageView poster_search_back;

    //搜索
    private EditText poster_search_search;
    private ImageView poster_search_search_del;
    private TextView poster_search_search_tv;

    //历史关键字
    private PosterHistoryQueryDTO mPosterHistoryQueryDTO = new PosterHistoryQueryDTO();
    private LinearLayout poster_search_history_layout;

    //历史记录列表
    private RecyclerView poster_search_history_recycler;
    private RecyclerView.LayoutManager poster_search_history_manager;
    private PosterSearchAdapter mPosterSearchAdapter;

    /**
     * 列表
     */
    private RecyclerView poster_search_recycler;
    private RecyclerView.LayoutManager poster_search_manager;
    private PosterAdapter myPublishAdapter;
    private SwipeRefreshLayout poster_search_refresh;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_poster_search;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        poster_search_back = (ImageView) findViewById(R.id.poster_search_back);

        //搜索
        poster_search_search = (EditText) findViewById(R.id.poster_search_search);
        poster_search_search_del = (ImageView) findViewById(R.id.poster_search_search_del);
        poster_search_search_tv = (TextView) findViewById(R.id.poster_search_search_tv);

        poster_search_history_layout = (LinearLayout) findViewById(R.id.poster_search_history_layout);

        //历史记录列表
        poster_search_history_recycler = (RecyclerView) findViewById(R.id.poster_search_history_recycler);
        poster_search_history_manager = new LinearLayoutManager(activity);
        poster_search_history_recycler.setLayoutManager(poster_search_manager);

        //列表
        poster_search_recycler = (RecyclerView) findViewById(R.id.poster_search_recycler);
        poster_search_manager = new LinearLayoutManager(activity);
        poster_search_recycler.setLayoutManager(poster_search_manager);
        poster_search_refresh = (SwipeRefreshLayout) findViewById(R.id.poster_search_refresh);
        poster_search_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mPosterDataManager = PosterDataManager.getPosterDataManager();
        String posterHistorys = SharedPreferencesUtils.getString("PosterHistory");
        Print.println("posterHistorys:" + posterHistorys);
        Gson gson = new Gson();
        mPosterHistoryQueryDTO = gson.fromJson(posterHistorys, PosterHistoryQueryDTO.class);

        mPosterSearchAdapter = new PosterSearchAdapter(activity, new PosterSearchAdapter.OnItemListener() {
            @Override
            public void onClickItem(int type, PosterHistoryDTO fansBeen) {
                if (type == 0) {
                    poster_search_search.setText("" + fansBeen.keywords);
                    poster_search_search.setSelection(poster_search_search.getText().toString().length());//将光标移至文字末尾
                    poster_search_history_layout.setVisibility(View.GONE);
                    poster_search_refresh.setVisibility(View.VISIBLE);
                    //获取广告数据
                    loadPosterData(1);
                } else {
                    mPosterHistoryQueryDTO.list.remove(fansBeen);
                    mPosterSearchAdapter.notifyData(mateHistory(getSearchKey()),getSearchKey());
                    Gson g = new Gson();
                    String result = g.toJson(mPosterHistoryQueryDTO);
                    SharedPreferencesUtils.put("PosterHistory", result);
                }
            }
        });
        LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(activity);
        linearLayoutManagerX.setOrientation(LinearLayout.VERTICAL);
        poster_search_history_recycler.setLayoutManager(linearLayoutManagerX);
        poster_search_history_recycler.setAdapter(mPosterSearchAdapter);

        refreshHistoryItem();

        //我的发布列表
        myPublishAdapter = new PosterAdapter(activity, new PosterAdapter.OnItemListener() {
            @Override
            public void onClickItem(int position, PosterDTO posterData) {
                itemOnClickListenner(posterData);
            }

            @Override
            public void onClickUser(PosterDTO posterData) {
                itemOnClickUserListenner(posterData);
            }

            @Override
            public void onClickReward(int position, PosterDTO posterData) {
                itemOnClickRewardListenner(posterData);
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
                itemOnClickCommentListenner(posterData);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        poster_search_recycler.setLayoutManager(linearLayoutManager);
        poster_search_recycler.setAdapter(myPublishAdapter);

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        poster_search_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //搜索
        poster_search_search_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                saveHistory();
            }
        });
        //关键字搜索
        poster_search_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<PosterHistoryDTO> list = mateHistory(s.toString().trim());
                mPosterSearchAdapter.notifyData(list,getSearchKey());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    poster_search_search_del.setVisibility(View.GONE);
                    poster_search_search_tv.setEnabled(false);
                    poster_search_search_tv.setTextColor(getResources().getColor(R.color.color_DDDDDD));
                } else {
                    poster_search_search_del.setVisibility(View.VISIBLE);
                    poster_search_search_tv.setEnabled(true);
                    poster_search_search_tv.setTextColor(getResources().getColor(R.color.color_00BCB4));
                }
            }
        });
        //搜索框
        poster_search_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    if (v.getText().toString().isEmpty()) return false;

                    // 强制隐藏键盘
                    KeyboardUtils.hideSoftInput(activity);

                    poster_search_search.requestFocus();
                    poster_search_search.selectAll();

                    saveHistory();

                    final Handler handlerXXXX = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            poster_search_search.requestFocus();
                            poster_search_search.selectAll();
                            // 强制隐藏键盘
                            KeyboardUtils.hideSoftInput(activity);
                        }
                    };
                    handlerXXXX.postDelayed(runnable, 100);// 打开定时器，执行操作
                }
                return false;
            }
        });
        //清空输入框内容
        poster_search_search_del.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                poster_search_search_del.setVisibility(View.GONE);
                poster_search_search.setText("");
                poster_search_search.requestFocus();
                poster_search_search.selectAll();
                // 强制隐藏键盘
                KeyboardUtils.hideSoftInput(activity);
                poster_search_history_layout.setVisibility(View.VISIBLE);
                poster_search_refresh.setVisibility(View.GONE);
            }
        });
        // 刷新
        poster_search_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosterData(1);
            }
        });
        // 滚动监听
        poster_search_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 滚动到底部加载更多
                if (poster_search_refresh.isRefreshing() != true && isSlideToBottom(recyclerView)
                        && myPublishAdapter.getItemCount() >= 20 * mPosterTotalBean.currPage) {
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
    }

    /**
     * 匹配历史记录
     */
    private ArrayList<PosterHistoryDTO> mateHistory(String key) {
        ArrayList<PosterHistoryDTO> list = new ArrayList<>();
        if (mPosterHistoryQueryDTO != null && mPosterHistoryQueryDTO.list != null) {
            for (PosterHistoryDTO dto : mPosterHistoryQueryDTO.list) {
                if (dto.keywords.contains(key)) {
                    list.add(dto);
                }
            }
        }
        return list;
    }

    /**
     * 保存历史记录
     */
    private void saveHistory() {
        loadPosterData(1);
        if (poster_search_search.getText().toString().isEmpty()) {
            poster_search_search_del.setVisibility(View.GONE);
            poster_search_search_tv.setEnabled(false);
            poster_search_search_tv.setTextColor(getResources().getColor(R.color.color_DDDDDD));
        } else {
            poster_search_search_del.setVisibility(View.VISIBLE);
            poster_search_search_tv.setEnabled(true);
            poster_search_search_tv.setTextColor(getResources().getColor(R.color.color_00BCB4));
        }
        String keywords = poster_search_search.getText().toString();

        if (!keywords.isEmpty()) {
            if (mPosterHistoryQueryDTO != null) {
                boolean isTrue = true;
                for (int i = 0; i < mPosterHistoryQueryDTO.list.size(); i++) {
                    if (mPosterHistoryQueryDTO.list.get(i).keywords.equals(keywords)) {
                        mPosterHistoryQueryDTO.list.remove(i);
                        mPosterHistoryQueryDTO.list.add(0, new PosterHistoryDTO(keywords));
                        isTrue = false;
                        break;
                    }
                }
                if (isTrue) {
                    PosterHistoryDTO posterHistoryDTO = new PosterHistoryDTO(keywords);
//                    posterHistoryDTO.keywords = keywords;
                    mPosterHistoryQueryDTO.list.add(0, posterHistoryDTO);
                }
                Gson g = new Gson();
                String result = g.toJson(mPosterHistoryQueryDTO);
                SharedPreferencesUtils.put("PosterHistory", result);
            } else {
                PosterHistoryDTO posterHistoryDTO = new PosterHistoryDTO(keywords);
//                posterHistoryDTO.keywords = keywords;
                mPosterHistoryQueryDTO = new PosterHistoryQueryDTO();
                mPosterHistoryQueryDTO.list.add(0, posterHistoryDTO);
                Gson g = new Gson();
                String result = g.toJson(mPosterHistoryQueryDTO);
                SharedPreferencesUtils.put("PosterHistory", result);
            }
        }
        mPosterSearchAdapter.notifyData(mPosterHistoryQueryDTO.list,getSearchKey());
    }

    /**
     * 刷新历史记录列表信息
     */
    private void refreshHistoryItem() {
        if (mPosterHistoryQueryDTO == null) return;
        mPosterSearchAdapter.notifyData(mPosterHistoryQueryDTO.list,getSearchKey());
    }

    private String getSearchKey(){
        return poster_search_search.getText().toString().trim();
    }

    /**
     * 获取我的发布数据
     */
    public void loadPosterData(final int currPage) {
        Print.println("loadPosterData_" + currPage);
        poster_search_refresh.setRefreshing(true);
        String keywords = poster_search_search.getText().toString();
        // 强制隐藏键盘
        KeyboardUtils.hideSoftInput(activity);
        mPosterDataManager.loadPosterData(activity, currPage + "", keywords, new IRequestCallBack<PosterQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                poster_search_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, PosterQueryDTO posterQueryDTO) {
                if (currPage == 1) {
                    myPublishAdapter.notifyData(null);
                }
                poster_search_refresh.setRefreshing(false);
                mPosterTotalBean = posterQueryDTO.total;
                if (posterQueryDTO.list != null) {
                    refreshItem(posterQueryDTO.list);
                    Print.println(mPosterTotalBean.currPage + "从网络拿到 " + posterQueryDTO.list.size() + " 条商品数据" + posterQueryDTO.total.currPage);
                } else {
                    Toast.show(activity, "没有任何信息！");
                }
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<PosterDTO> posterData) {
        if (mPosterTotalBean.currPage == 1) {
            myPublishAdapter.notifyData(posterData);
        } else {
            myPublishAdapter.addItems(posterData);
        }

        poster_search_history_layout.setVisibility(View.GONE);
        poster_search_refresh.setVisibility(View.VISIBLE);
    }

    /**
     * 点击广告列表监听
     */
    public void itemOnClickListenner(PosterDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("news_id", posterData.news_id.toString());
        startActivity(new Intent(activity, PosterDetailsActivity.class).putExtras(bundle));
    }

    /**
     * 点击用户监听
     */
    public void itemOnClickUserListenner(PosterDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", posterData.uid.toString());
        bundle.putInt("user_type", posterData.user_type);
        bundle.putString("nick", posterData.nick.toString());
        bundle.putString("logo", posterData.logo.toString());
        bundle.putInt("has_flow", posterData.has_flow);
        startActivity(new Intent(activity, OpenHomeActivity.class).putExtras(bundle));
    }

    /**
     * 点击佣金、红包监听
     */
    public void itemOnClickRewardListenner(PosterDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("news_id", posterData.news_id.toString());
        bundle.putString("news_img", posterData.news_img.toString());
        bundle.putString("news_title", posterData.news_title.toString());
        bundle.putLong("build_time", posterData.build_time);
        if (posterData.has_red_envelopes > 0) {//0=没有红包,1=有红包
            bundle.putInt("has_myget_red_envelopes", posterData.has_myget_red_envelopes);// 是否已抢红包 1=已抢红包,0=未抢红包
            startActivity(new Intent(activity, PosterRedBagActivity.class).putExtras(bundle));
        } else {
            if (posterData.has_commission > 0) {//0=没有佣金,1=有佣金
                bundle.putDouble("goods_price", posterData.goods_price);
                bundle.putDouble("goods_commission_rate", posterData.goods_commission_rate);
                bundle.putDouble("goods_commission_amount", posterData.goods_commission_amount);
                startActivity(new Intent(activity, PosterCommissionActivity.class).putExtras(bundle));
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
            mPosterDataManager.loadLikePoster(activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "点赞成功");
                    posterData.has_like = 1;
                    posterData.like_num++;
                    myPublishAdapter.notifyItemChanged(position, "poster");
                }
            });
        } else {
            mPosterDataManager.loadUnLikePoster((BaseActivity) activity, "" + posterData.news_id, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg + "");//取消点赞失败
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, "取消点赞成功");
                    posterData.has_like = 0;
                    posterData.like_num--;
                    myPublishAdapter.notifyItemChanged(position, "poster");
                }
            });
        }
    }

    /**
     * 点击评价监听
     */
    public void itemOnClickCommentListenner(PosterDTO posterData) {
        Bundle bundle = new Bundle();
        bundle.putString("news_id", "" + posterData.news_id);
        startActivity(new Intent(activity, PosterCommentActivity.class).putExtras(bundle));
    }

}
