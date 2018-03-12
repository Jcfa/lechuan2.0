package com.poso2o.lechuan.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.mine.AcceptDetailActivity;
import com.poso2o.lechuan.activity.mine.CommissionDetailActivity;
import com.poso2o.lechuan.activity.mine.InvitationFriendsActivity;
import com.poso2o.lechuan.activity.mine.PosterDetailActivity;
import com.poso2o.lechuan.activity.mine.RedPacketDetailActivity;
import com.poso2o.lechuan.activity.mine.UserInfoActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.event.InviteEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.MerchantData;
import com.poso2o.lechuan.bean.mine.MerchantItemBean;
import com.poso2o.lechuan.bean.mine.PosterData;
import com.poso2o.lechuan.bean.mine.PosterItemBean;
import com.poso2o.lechuan.bean.mine.RedPacketData;
import com.poso2o.lechuan.bean.mine.RedPacketItemBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.ResourceSetUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

//import com.poso2o.lechuan.bean.mine.PosterAllData;

/**
 * Created by Administrator on 2017-11-25.
 */

public class MineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout layoutRedpacketPayment;//红包余额充值提现
    private View layoutRedpacketLine;
    private ArrayList<MerchantItemBean> brokerageList = new ArrayList<>();//佣金数据
    private ArrayList<RedPacketItemBean> redPacketList = new ArrayList<>();//红包数据
    private ArrayList<PosterItemBean> posterBeanList = new ArrayList<>();//广告数据
    private MineDataManager mMineDataManager;
    private BaseAdapter posterAdapter;
    private BaseAdapter redpacketAdapter;
    private BaseAdapter brokerageAdapter;
    private int CURRPAGE_POSTER = 1;//广告转发当前页
    //    private int CURRPAGE_BROKERAGE = 1;//未结佣金当前页
    private int CURRPAGE_REDPACKET = 1;//红包余额当前页
    private int TOTALPAGE_POSTER = 1;
    private int TOTALPAGE_REDPACKET = 1;
    //    private int TOTALPAGE_BROKERAGE = 1;
    private int CURRENT_TYPE = 1;
    public static final int BROKETAGE_ID = 1;//佣金页
    public static final int RED_PACKET_ID = 2;//红包页
    public static final int POSTER_ID = 3;//广告页
    private TextView tvBrokerage_amount, tvBrokerage_tag, tvRedpacket_amount, tvRedpacket_tag, tvPoster_times, tvPoster_tag;
    private TextView tvSelect, tvSelectTag;
    private ImageView ivLogo;
    private TextView tvNick, tvDescribe;
    private TextView tvCommissionTotal, tvCommissionWj, tvCommissionYj;//合计佣金

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void initView() {
        mMineDataManager = MineDataManager.getMineDataManager();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                context, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1)) {
                    loadData(true);
                }
            }
        });
        layoutRedpacketPayment = findView(R.id.layout_redpacket_payment);
        layoutRedpacketLine = findView(R.id.layout_redpacket_payment_line);
        tvBrokerage_amount = (TextView) view.findViewById(R.id.tv_brokerage_amount);
        tvBrokerage_tag = (TextView) view.findViewById(R.id.tv_brokerage_tag);
        tvRedpacket_amount = (TextView) view.findViewById(R.id.tv_redpacket_amount);
        tvRedpacket_tag = (TextView) view.findViewById(R.id.tv_redpacket_tag);
        tvPoster_times = (TextView) view.findViewById(R.id.tv_poster_times);
        tvPoster_tag = (TextView) view.findViewById(R.id.tv_poster_tag);
        tvCommissionTotal = findView(R.id.tv_commission_amount);
        tvCommissionWj = findView(R.id.tv_commission_wj);
        tvCommissionYj = findView(R.id.tv_commission_yj);
    }

    @Override
    public void initData() {
        ivLogo = findView(R.id.iv_userlogo);
        tvNick = findView(R.id.tv_usernick);
        tvDescribe = findView(R.id.tv_describe);
        Glide.with(context).load(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO)).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
        tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
//        tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.DISTRIBUTION_TYPE) {
            findView(R.id.iv_user_icon).setVisibility(View.VISIBLE);
        }else{
            findView(R.id.iv_user_icon).setVisibility(View.GONE);
        }
        /**
         * 默认显示未结佣金
         */
        setCommissionView();
    }

    @Override
    public void initListener() {
        findView(R.id.ll_brokerage).setOnClickListener(this);//未结佣金
        findView(R.id.ll_redpacket).setOnClickListener(this);//红包余额
        findView(R.id.ll_poster).setOnClickListener(this);//转发广告
        findView(R.id.tv_invitation).setOnClickListener(this);//邀请按钮
        findView(R.id.layout_userinfo).setOnClickListener(this);//用户信息
        layoutRedpacketPayment.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getUserInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUserInfo();
        }
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }


    private void loadData(boolean next) {
        switch (CURRENT_TYPE) {
            case BROKETAGE_ID://刷新未结佣金
//                if (next) {
//                    if (CURRPAGE_BROKERAGE < TOTALPAGE_BROKERAGE) {
//                        ++CURRPAGE_BROKERAGE;
//                        getMyMerchantList();
//                    }
//                } else {
//                    CURRPAGE_BROKERAGE = 1;
                if (!next) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getMyMerchantList();
                }
//                }
//                getMyDistributionList();
                break;
            case RED_PACKET_ID://刷新红包余额
                if (next) {
                    if (CURRPAGE_REDPACKET < TOTALPAGE_REDPACKET) {
                        ++CURRPAGE_REDPACKET;
                        getRedPacketList();
                    }
                } else {
                    CURRPAGE_REDPACKET = 1;
                    mSwipeRefreshLayout.setRefreshing(true);
                    getRedPacketList();
                }
                break;
            case POSTER_ID://刷新转发广告
                if (next) {
                    if (CURRPAGE_POSTER < TOTALPAGE_POSTER) {
                        ++CURRPAGE_POSTER;
                        getMyPosterList();
                    }
                } else {
                    CURRPAGE_POSTER = 1;
                    mSwipeRefreshLayout.setRefreshing(true);
                    getMyPosterList();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_brokerage://未结佣金
                setCommissionView();
                break;
            case R.id.ll_redpacket://红包余额
                setRedpacketView();
                break;
            case R.id.ll_poster://广告转发
                setPosterView();
                break;
            case R.id.layout_redpacket_payment://红包详情
                startActivity(new Intent(getContext(), RedPacketDetailActivity.class));
                break;
            case R.id.tv_invitation://邀请
                invitation();
                break;
            case R.id.layout_userinfo://用户信息
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
        }
    }

    /**
     * 广告转发列表
     */
    private void getMyPosterList() {
        mMineDataManager.getMyForwardEssayList((BaseActivity) getActivity(), CURRPAGE_POSTER, new IRequestCallBack<PosterData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onResult(int tag, PosterData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (object == null || object.total == null) {
                    return;
                }
                TOTALPAGE_POSTER = object.total.getPages();
                if (CURRPAGE_POSTER != 1) {
                    posterBeanList.addAll(object.list);
                } else {
                    if (object.list != null) {
                        posterBeanList.clear();
                        posterBeanList.addAll(object.list);
                    }
                }
                posterAdapter.notifyDataSetChanged(posterBeanList);
                if (posterBeanList.size() <= 0) {
                    TextView tvNoData = findView(R.id.tv_no_data);
                    tvNoData.setText("您还没有转发过一条广告呢~");
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    /**
//     * 我的分销商列表
//     */
//    private void getMyDistributionList() {
//        mMineDataManager.getMyDistributionList((BaseActivity) getActivity(), 1, new IRequestCallBack<PosterData>() {
//            @Override
//            public void onFailed(int tag, String msg) {
//                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onResult(int tag, PosterData object) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                if (object == null || object.total == null) {
//                    return;
//                }
////                TOTALPAGE_BROKERAGE = object.total.getPages();
////                if (CURRPAGE_BROKERAGE != 1) {
////                    posterBeanList.addAll(object.list);
////                } else {
////                    if (object.list != null) {
//                posterBeanList.clear();
//                posterBeanList.addAll(object.list);
////                    }
////                }
//                posterAdapter.notifyDataSetChanged(posterBeanList);
//            }
//        });
//    }

    /**
     * 我的商家列表
     */
    private void getMyMerchantList() {
        mMineDataManager.getMyMerchantList((BaseActivity) getActivity(), 1, new IRequestCallBack<MerchantData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onResult(int tag, MerchantData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (object == null) {
                    return;
                }
                setTotalView(object.getTotal());
                brokerageList.clear();
                brokerageList.addAll(object.list);
                brokerageAdapter.notifyDataSetChanged(brokerageList);
                if (object.list != null && object.list.size() <= 0) {
                    TextView tvNoData = findView(R.id.tv_no_data);
                    tvNoData.setText("您暂时还没有未结的佣金哦~");
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getRedPacketList() {
        mMineDataManager.getMyRedPacketList((BaseActivity) getActivity(), CURRPAGE_REDPACKET, new IRequestCallBack<RedPacketData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onResult(int tag, RedPacketData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (object == null || object.total == null) {
                    return;
                }

                TOTALPAGE_REDPACKET = object.total.getPages();
                if (CURRPAGE_REDPACKET != 1) {
                    redPacketList.addAll(object.list);
                } else {
                    if (object.list != null) {
                        redPacketList.clear();
                        redPacketList.addAll(object.list);
                    }
                }
                redpacketAdapter.notifyDataSetChanged(redPacketList);
                if (redPacketList.size() <= 0) {
                    TextView tvNoData = findView(R.id.tv_no_data);
                    tvNoData.setText("没有发现您的红包记录呢~");
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void invitation() {
        float rate = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE);
        float discount = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT);
        WaitDialog.showLoaddingDialog(context);
        mMineDataManager.doInvitationDistribution((BaseActivity) getActivity(), rate, discount, new IRequestCallBack<InvitationBean>() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, InvitationBean object) {
                if (object != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(InvitationFriendsActivity.KEY_TYPE, InvitationFriendsActivity.VALUE_FRIENDS);
                    bundle.putString(InvitationFriendsActivity.KEY_TITLE, getString(R.string.share_friends_title));
                    bundle.putString(InvitationFriendsActivity.KEY_DESCRIPTION, getString(R.string.share_friends_description));
                    bundle.putString(InvitationFriendsActivity.KEY_URL, object.url);
                    startActivity(new Intent(getActivity(), InvitationFriendsActivity.class).putExtras(bundle));
                }
            }
        });
    }

    /**
     * 设置显示未结佣金
     */
    public void setCommissionView() {
        CURRENT_TYPE = BROKETAGE_ID;
        showRedPacketBalanceView(false);
        findView(R.id.layout_total).setVisibility(View.VISIBLE);//显示合计佣金
        setTextViewColor(tvBrokerage_amount, tvBrokerage_tag);
        brokerageAdapter = new BaseAdapter(getContext(), brokerageList) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_mine_commission_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                final MerchantItemBean bean = (MerchantItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                Glide.with(context).load(bean.getShop_logo()).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                TextView tvShopName = holder.getView(R.id.tv_shop_name);
                tvShopName.setText(bean.getShop_name());
                TextView tvAmount = holder.getView(R.id.tv_total_amount);
                tvAmount.setText("总佣 " + NumberUtils.format2(bean.getGeneral_commission()));
                TextView tvYjAmount = holder.getView(R.id.tv_yj_amount);
                tvYjAmount.setText("已结 " + NumberUtils.format2(bean.getSettle_amount()));
                TextView tvWjAmount = holder.getView(R.id.tv_wj_amount);
                tvWjAmount.setText("未结 " + NumberUtils.format2(bean.getNot_settle_amount()));
                TextView tv_relieve = holder.getView(R.id.tv_relieve);
                if (bean.getNot_settle_amount() > 0) {//未结金额显示橙色
                    tvWjAmount.setSelected(true);
                    tv_relieve.setVisibility(View.GONE);
                } else {
                    tvWjAmount.setText("已结清");
                    tvWjAmount.setSelected(false);
                    tv_relieve.setVisibility(View.VISIBLE);
                    tv_relieve.setText("解除");
                    ResourceSetUtil.setBackground(context, tv_relieve, R.drawable.selector_gray_radius5_bg);
                    ResourceSetUtil.setTextColor(context, tv_relieve, R.color.colorOrange);
                    ResourceSetUtil.setTextColor(context, tvWjAmount, R.color.colorOrange);
                }
                tv_relieve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRelieveDialog(bean);
                    }
                });
                if (position == 0) {
                    holder.setPadding(0, 5, 0, 0);
                } else {
                    holder.setPadding(0, 0, 0, 0);
                }
            }
        };
        brokerageAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<MerchantItemBean>() {
            @Override
            public void onItemClick(MerchantItemBean item) {
                Bundle bundle = new Bundle();
                bundle.putString(CommissionDetailActivity.KEY_ID, item.getShop_id());
                bundle.putSerializable(CommissionDetailActivity.KEY_BEAN_INFO, item);
                startActivity(new Intent(getContext(), CommissionDetailActivity.class).putExtras(bundle));
            }
        });
        mRecyclerView.setAdapter(brokerageAdapter);
        loadData(false);
    }

    /**
     * 是否显示红包余额，和分隔线
     */
    private void showRedPacketBalanceView(boolean show) {
        layoutRedpacketPayment.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutRedpacketLine.setVisibility(show ? View.VISIBLE : View.GONE);
        findView(R.id.tv_no_data).setVisibility(View.GONE);
    }

    /**
     * 设置显示红包余额
     */
    public void setRedpacketView() {
        CURRENT_TYPE = RED_PACKET_ID;
        showRedPacketBalanceView(true);
        findView(R.id.layout_total).setVisibility(View.GONE);//隐藏合计佣金
        setTextViewColor(tvRedpacket_amount, tvRedpacket_tag);
        redpacketAdapter = new BaseAdapter(getContext(), redPacketList) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_mine_redpacket_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, final Object item, int position) {
                final RedPacketItemBean bean = (RedPacketItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvDate = holder.getView(R.id.tv_date);
                TextView tvDetail = holder.getView(R.id.tv_detail);
                TextView tvAmount = holder.getView(R.id.tv_amount);
                Glide.with(context).load(bean.getNews_img()).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                tvTitle.setText(bean.getNews_title());
                tvDate.setText(TimeUtil.longToDateString(bean.getBuild_time(), "yyyy-MM-dd"));
                if (bean.getAmount_type() == RedPacketItemBean.EXPEND_TYPE) {
                    tvAmount.setText("-" + NumberUtils.format2(bean.getAmount()));
                    tvAmount.setSelected(false);
                    tvDetail.setVisibility(View.VISIBLE);
                    tvDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//领取详情
                            Bundle bundle = new Bundle();
                            bundle.putString(PosterDetailActivity.KEY_ID, bean.getNews_id());
                            bundle.putSerializable(PosterDetailActivity.KEY_BEAN_INFO, bean);
                            startActivity(new Intent(getContext(), AcceptDetailActivity.class).putExtras(bundle));
                        }
                    });
                } else if (bean.getAmount_type() == RedPacketItemBean.INCOME_TYPE) {
                    tvAmount.setText("+" + NumberUtils.format2(bean.getAmount()));
                    tvAmount.setSelected(true);//金额大于0显示橙色
                    tvDetail.setVisibility(View.GONE);
                }
                if (position == 0) {
                    holder.setPadding(0, 5, 0, 0);
                } else {
                    holder.setPadding(0, 0, 0, 0);
                }
            }
        };
        mRecyclerView.setAdapter(redpacketAdapter);
        loadData(false);
    }

    /**
     * 设置显示广告转发
     */
    private void setPosterView() {
        CURRENT_TYPE = POSTER_ID;
        showRedPacketBalanceView(false);
        findView(R.id.layout_total).setVisibility(View.GONE);//隐藏合计佣金
        setTextViewColor(tvPoster_times, tvPoster_tag);
        posterAdapter = new BaseAdapter(getContext(), posterBeanList) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_mine_poster_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                PosterItemBean bean = (PosterItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                Glide.with(context).load(bean.getNews_img()).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                TextView tvTitle = holder.getView(R.id.tv_title);
                tvTitle.setText(bean.getNews_title());
                TextView tvDate = holder.getView(R.id.tv_date);
                tvDate.setText(TimeUtil.longToDateString(bean.getBuild_time(), "yyyy-MM-dd HH:mm"));
                TextView tvAmount = holder.getView(R.id.tv_amount);
                tvAmount.setText(NumberUtils.format2(bean.getGoods_commission_amount()));
                if (position == 0) {
                    holder.setPadding(0, 5, 0, 0);
                } else {
                    holder.setPadding(0, 0, 0, 0);
                }
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.COMMON_TYPE || bean.getHas_commission() != 1) {//普通用户和没佣金的不显示佣金
                    holder.getView(R.id.tv_tag).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.tv_amount).setVisibility(View.INVISIBLE);
                }else{
                    holder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
                }
            }
        };
        posterAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<PosterItemBean>() {
            @Override
            public void onItemClick(PosterItemBean item) {
                Bundle bundle = new Bundle();
                bundle.putString(PosterDetailActivity.KEY_ID, item.getNews_id());
                bundle.putSerializable(PosterDetailActivity.KEY_BEAN_INFO, item);
                startActivity(new Intent(getContext(), PosterDetailActivity.class).putExtras(bundle));
            }
        });
        mRecyclerView.setAdapter(posterAdapter);
        loadData(false);
    }


    /**
     * 解除确认提示
     */
    private void showRelieveDialog(final MerchantItemBean bean) {
        CommonHintDialog dialog = new CommonHintDialog(context, "是否确认解除与" + bean.getShop_name() + "的分销关系？\n关系解除后无法再获得该商家佣金收入。", new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                doRelieveDistribution(bean);
            }
        });
        dialog.show();
    }

    /**
     * 解除分销商
     *
     * @param
     */
    private void doRelieveDistribution(MerchantItemBean bean) {
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        MineDataManager.getMineDataManager().doRelieveDistribution((BaseActivity) getActivity(), bean.getShop_id(), uid, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, "解除失败：" + msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                Toast.show(context, "解除成功！");
//                getMyDistributionList();
                loadData(false);
            }
        });
    }


    private void setTextViewColor(TextView tv, TextView tvTag) {
        if (tvSelect != null) {
            tvSelect.setSelected(false);
        }
        if (tvSelectTag != null) {
            tvSelectTag.setSelected(false);
        }
        tv.setSelected(true);
        tvTag.setSelected(true);
        tvSelect = tv;
        tvSelectTag = tvTag;
    }

    public void getUserInfo() {
        String id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        MineDataManager.getMineDataManager().getUserInfo((BaseActivity) getActivity(), id, null);
    }

    public void setUserInfo(UserInfoBean object) {
        Print.println("setUserInfo(UserInfoBean object)");
        tvBrokerage_amount.setText(NumberUtils.format2(object.getCommission_amount()));
        tvRedpacket_amount.setText(NumberUtils.format2(object.getRed_envelopes_amount()));
        tvPoster_times.setText(object.getShare_news_number() + "");
        tvNick.setText(object.getNick());
        tvDescribe.setText(object.getRemark());
        TextView tvBalance = findView(R.id.tv_balance);
        tvBalance.setText(object.getRed_envelopes_amount() + "");
        Glide.with(this).load(object.getLogo()).thumbnail(0.1f).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
    }

//    /**
//     * 即时刷新红包余额,支付成功
//     */
//    public void refreshData() {
//        tvRedpacket_amount.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
//        TextView tvBalance = findView(R.id.tv_balance);
//        tvBalance.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
//    }

    /**
     * 设置显示合计的佣金
     */
    private void setTotalView(TotalBean totalBean) {
        if (totalBean == null) {
            return;
        }
        tvCommissionTotal.setText("总佣 " + NumberUtils.format2(totalBean.getTotal_general_commission()));
        tvCommissionYj.setText("已结 " + NumberUtils.format2(totalBean.getTotal_settle_amount()));
        tvCommissionWj.setText("未结 " + NumberUtils.format2(totalBean.getTotal_not_settle_amount()));
    }
    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCommissionView(InviteEvent event) {
        setCommissionView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
