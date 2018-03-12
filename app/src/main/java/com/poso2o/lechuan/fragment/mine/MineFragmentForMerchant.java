package com.poso2o.lechuan.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
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
import com.poso2o.lechuan.activity.mine.CommissionSettingActivity;
import com.poso2o.lechuan.activity.mine.InvitationFriendsActivity;
import com.poso2o.lechuan.activity.mine.PosterDetailActivity;
import com.poso2o.lechuan.activity.mine.RedPacketDetailActivity;
import com.poso2o.lechuan.activity.mine.UserInfoActivity;
import com.poso2o.lechuan.activity.topup.CommissionSettleActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.TotalBean;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.MerchantData;
import com.poso2o.lechuan.bean.mine.MerchantItemBean;
import com.poso2o.lechuan.bean.mine.PosterItemBean;
import com.poso2o.lechuan.bean.mine.RedPacketData;
import com.poso2o.lechuan.bean.mine.RedPacketItemBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.dialog.CommissionSettingDialog;
import com.poso2o.lechuan.dialog.CommonHintDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.SettleAccountsDayDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.GoodsDataManager;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.ResourceSetUtil;
import com.poso2o.lechuan.util.SettingSP;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TimeUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.Calendar;

//import com.poso2o.lechuan.bean.mine.PosterAllData;

/**
 * Created by Administrator on 2017-11-25.
 */

public class MineFragmentForMerchant extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout layoutRedpacketPayment, layoutComissionSet;//红包余额充值提现,佣金设置
    private View layoutRedpacketLine;
    private ArrayList<MerchantItemBean> brokerageList = new ArrayList<>();//佣金数据
    private ArrayList<RedPacketItemBean> redPacketList = new ArrayList<>();//红包数据
    private ArrayList<PosterItemBean> posterBeanList = new ArrayList<>();//广告数据
    private MineDataManager mMineDataManager;
//    private BaseAdapter posterAdapter;
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
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_merchant, container, false);
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
        layoutComissionSet = findView(R.id.layout_commission_set);
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
        Glide.with(context).load(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
        tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
//      tvNick.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
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
        layoutComissionSet.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        getUserInfo();
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
                if (!next) {//否则会触发加载下一页，出现加载2次情况
                    getMyDistributionList();
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
                    getRedPacketList();
                }
                break;
            case POSTER_ID://刷新转发广告
//                if (next) {
//                    if (CURRPAGE_POSTER < TOTALPAGE_POSTER) {
//                        ++CURRPAGE_POSTER;
//                        getMyPosterList();
//                    }
//                } else {
//                    CURRPAGE_POSTER = 1;
//                    getMyPosterList();
//                }
                if (!next) {//否则会触发加载下一页，出现加载2次情况
                    getMyDistributionList();
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
            case R.id.ll_poster://分销商
                setDistributionView();
                break;
            case R.id.layout_redpacket_payment://红包详情
                startActivity(new Intent(getContext(), RedPacketDetailActivity.class));
                break;
            case R.id.tv_invitation://邀请分销
                invitation();
                break;
            case R.id.layout_userinfo://用户信息
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.layout_commission_set://佣金设置
                startActivity(new Intent(getActivity(), CommissionSettingActivity.class));
                break;
        }
    }

    private void invitation() {
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION) != 1) {
            //未设置佣金
            showCommissionSettingDialog();
            return;
        }
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
                    bundle.putInt(InvitationFriendsActivity.KEY_TYPE, InvitationFriendsActivity.VALUE_DISTRIBUTION);
                    bundle.putString(InvitationFriendsActivity.KEY_TITLE, getString(R.string.share_distribution_title));
                    bundle.putString(InvitationFriendsActivity.KEY_DESCRIPTION, getString(R.string.share_distribution_description));
                    bundle.putString(InvitationFriendsActivity.KEY_URL, object.url);
                    startActivity(new Intent(getActivity(), InvitationFriendsActivity.class).putExtras(bundle));
                }
            }
        });
    }

    /**
     * 显示佣金设置对话框
     */
    private void showCommissionSettingDialog() {
        CommissionSettingDialog dialog = new CommissionSettingDialog(getContext(), CommissionSettingDialog.COMMON_TYPE, new CommissionSettingDialog.SettingCallBack() {
            @Override
            public void setFinish(final float rate, final float discount) {
                GoodsDataManager manager = GoodsDataManager.getGoodsDataManager();
                manager.setCommissionRate((BaseActivity) getActivity(), rate + "", discount + "", new IRequestCallBack() {
                    @Override
                    public void onFailed(int tag, String msg) {

                    }

                    @Override
                    public void onResult(int tag, Object object) {
                        Toast.show(getContext(), "设置成功！");
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE, rate);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT, discount);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION, 1);
                        isCommissionSet();
                    }
                });
            }
        });
        dialog.show();
    }

    /**
     * 我的商家列表
     */
    private void getMyDistributionList() {
        mMineDataManager.getMyDistributionList((BaseActivity) getActivity(), 1, new IRequestCallBack<MerchantData>() {
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
                tvPoster_times.setText(brokerageList.size()+"");
                if (brokerageList.size() <= 0) {
                    TextView tvNoData = findView(R.id.tv_no_data);
                    tvNoData.setText("您还没有分销商哦，点右上角“邀请分销”邀请吧~");
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

    /**
     * 设置显示未结佣金
     */
    private void setCommissionView() {
        CURRENT_TYPE = BROKETAGE_ID;
        showHeaderView();
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
                Glide.with(context).load(bean.getDistributor_logo()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                TextView tvShopName = holder.getView(R.id.tv_shop_name);
                tvShopName.setText(bean.getDistributor_name());
                TextView tvAmount = holder.getView(R.id.tv_total_amount);
                tvAmount.setText("总佣 " + NumberUtils.format2(bean.getGeneral_commission()));
                TextView tvYjAmount = holder.getView(R.id.tv_yj_amount);
                tvYjAmount.setText("已结 " + NumberUtils.format2(bean.getSettle_amount()));
                TextView tvWjAmount = holder.getView(R.id.tv_wj_amount);
                TextView tv_relieve = holder.getView(R.id.tv_relieve);
                if (bean.getNot_settle_amount() > 0) {//未结金额显示橙色
                    tvWjAmount.setSelected(true);
                    tvWjAmount.setText("未结 " + NumberUtils.format2(bean.getNot_settle_amount()));
                    tv_relieve.setText("结算");
                    ResourceSetUtil.setBackground(context, tv_relieve, R.drawable.selector_green_radius5_bg);
                    ResourceSetUtil.setTextColor(context, tv_relieve, R.color.colorWhite);
                } else {
                    tvWjAmount.setText("已结清");
                    tv_relieve.setText("解除");
                    ResourceSetUtil.setBackground(context, tv_relieve, R.drawable.selector_gray_radius5_bg);
                    ResourceSetUtil.setTextColor(context, tv_relieve, R.color.colorOrange);
                }
                tv_relieve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.getNot_settle_amount() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(CommissionDetailActivity.KEY_BEAN_INFO, bean);
                            startActivity(new Intent(getContext(), CommissionSettleActivity.class).putExtras(bundle));
                        } else {
                            showRelieveDialog(bean);
                        }
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
                bundle.putInt(CommissionDetailActivity.KEY_TYPE, CommissionDetailActivity.VALUE_COMMISSION);
                bundle.putSerializable(CommissionDetailActivity.KEY_BEAN_INFO, item);
                startActivity(new Intent(getContext(), CommissionDetailActivity.class).putExtras(bundle));
            }
        });
        mRecyclerView.setAdapter(brokerageAdapter);
        WaitDialog.showLoaddingDialog(context);
        getMyDistributionList();
    }

    /**
     * 显示红包余额,佣金设置，和分隔线
     */
    private void showHeaderView() {
        if (CURRENT_TYPE == BROKETAGE_ID) {
            findView(R.id.layout_total).setVisibility(View.VISIBLE);//显示底部合
            layoutComissionSet.setVisibility(View.VISIBLE);//佣金设置
            layoutRedpacketPayment.setVisibility(View.GONE);//红包余额
            layoutRedpacketLine.setVisibility(View.VISIBLE);//分隔线
            isCommissionSet();
        } else if (CURRENT_TYPE == RED_PACKET_ID) {
//            findView(R.id.layout_total).setVisibility(View.GONE);//隐藏底部合
            layoutComissionSet.setVisibility(View.GONE);//佣金设置
            layoutRedpacketPayment.setVisibility(View.VISIBLE);//红包余额
            layoutRedpacketLine.setVisibility(View.VISIBLE);//分隔线

        } else {
            findView(R.id.layout_total).setVisibility(View.VISIBLE);//显示底部合
            layoutComissionSet.setVisibility(View.GONE);//佣金设置
            layoutRedpacketPayment.setVisibility(View.GONE);//红包余额
            layoutRedpacketLine.setVisibility(View.GONE);//分隔线
        }
        findView(R.id.tv_no_data).setVisibility(View.GONE);
    }

    /**
     * 是否设置佣金判断
     */
    private void isCommissionSet() {
        TextView tvCommissionDis = findView(R.id.tv_commission_discount);
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION) == 1) {
            tvCommissionDis.setText((int) SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE) + "%");
            //已设置佣金
        } else {
            //未设置
            tvCommissionDis.setText("未设置");
        }
    }

    /**
     * 设置显示红包余额
     */
    public void setRedpacketView() {
        CURRENT_TYPE = RED_PACKET_ID;
        showHeaderView();
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
                Glide.with(context).load(bean.getNews_img()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
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
        WaitDialog.showLoaddingDialog(context);
        getRedPacketList();
    }

    /**
     * 设置显示分销商
     */
    private void setDistributionView() {
        CURRENT_TYPE = POSTER_ID;
        showHeaderView();
        setTextViewColor(tvPoster_times, tvPoster_tag);
        brokerageAdapter = new BaseAdapter(getContext(), brokerageList) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_mine_distribution_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                final MerchantItemBean bean = (MerchantItemBean) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                Glide.with(context).load(bean.getDistributor_logo()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                TextView tvShopName = holder.getView(R.id.tv_shop_name);
                tvShopName.setText(bean.getShop_nick());
                TextView tvAmount = holder.getView(R.id.tv_total_amount);
                tvAmount.setText("总佣 " + NumberUtils.format2(bean.getGeneral_commission()));
                TextView tvYjAmount = holder.getView(R.id.tv_yj_amount);
                tvYjAmount.setText("已结 " + NumberUtils.format2(bean.getSettle_amount()));
                TextView tvWjAmount = holder.getView(R.id.tv_wj_amount);
                tvWjAmount.setText("未结 " + NumberUtils.format2(bean.getNot_settle_amount()));
                TextView tv_relieve = holder.getView(R.id.tv_relieve);
                if (bean.getNot_settle_amount() > 0) {//未结金额显示橙色
                    tvWjAmount.setSelected(true);
                    tvWjAmount.setText("未结 " + NumberUtils.format2(bean.getNot_settle_amount()));
                    tv_relieve.setVisibility(View.GONE);
                } else {
                    tvWjAmount.setText("已结清");
                    tv_relieve.setText("解除");
                    tv_relieve.setVisibility(View.VISIBLE);
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
                bundle.putInt(CommissionDetailActivity.KEY_TYPE, CommissionDetailActivity.VALUE_DISTRIBUTION);
                bundle.putSerializable(CommissionDetailActivity.KEY_BEAN_INFO, item);
                startActivity(new Intent(getContext(), CommissionDetailActivity.class).putExtras(bundle));
            }
        });
        mRecyclerView.setAdapter(brokerageAdapter);
        getMyDistributionList();
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

    /**
     * 解除确认提示
     */
    private void showRelieveDialog(final MerchantItemBean bean) {
        CommonHintDialog dialog = new CommonHintDialog(context, "是否确认解除与" + bean.getDistributor_name() + "的分销关系？", new InvitationDistributionDialog.DialogClickCallBack() {
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
        String shopId = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        MineDataManager.getMineDataManager().doRelieveDistribution((BaseActivity) getActivity(), shopId, bean.getDistributor_id(), new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, "解除失败：" + msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                Toast.show(context, "解除成功！");
                getMyDistributionList();
            }
        });
    }

    public void getUserInfo() {
        String id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        MineDataManager.getMineDataManager().getUserInfo((BaseActivity) getActivity(), id, null);
    }

    public void setUserInfo(UserInfoBean object) {
        Print.println("setUserInfo(UserInfoBean object)");
        tvBrokerage_amount.setText(NumberUtils.format2(object.getCommission_amount()));
        tvRedpacket_amount.setText(NumberUtils.format2(object.getRed_envelopes_amount()));
        tvPoster_times.setText(object.getDistributor_number() + "");
        tvNick.setText(object.getNick());
        tvDescribe.setText(object.getRemark());
        TextView tvBalance = findView(R.id.tv_balance);
        tvBalance.setText(object.getRed_envelopes_amount() + "");
        Glide.with(this).load(object.getLogo()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
        isCommissionSet();
    }

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
        if (totalBean.getTotal_not_settle_amount() > 0) {//有未结算佣金
            if (totalBean.currPage == 1) {
                showSettleAccountsDayDialog();
            }
        }
    }

    /**
     * 显示结算日提示
     */
    private void showSettleAccountsDayDialog() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);//今天是这个月的第几天
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//这个月的最后一天
        final int dayForYear = calendar.get(Calendar.DAY_OF_YEAR);//今天是今年的第几天
//        int lastDayForYear=calendar.getActualMaximum(Calendar.DAY_OF_YEAR);//今年的最后一天
        int saveDay = SettingSP.getSettleAccountsDayInt();//上次提醒的时候是今年第几天
        if (max - day < 3 && (saveDay < dayForYear || dayForYear == 1)) {//每月最后三天 并且未提示过，则提示有未结算佣金//
            SettleAccountsDayDialog dayDialog = new SettleAccountsDayDialog(context, "", "", new SettleAccountsDayDialog.DialogClickCallBack() {
                @Override
                public void setAffirm() {
                    SettingSP.putSettleAccountsDayInt(dayForYear);
                }
            });
            dayDialog.show();
        }
    }

//    /**
//     * 即时刷新红包余额,支付成功
//     */
//    public void refreshData() {
//        tvRedpacket_amount.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
//        TextView tvBalance = findView(R.id.tv_balance);
//        tvBalance.setText(SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT) + "");
//        isCommissionSet();
//    }

}
