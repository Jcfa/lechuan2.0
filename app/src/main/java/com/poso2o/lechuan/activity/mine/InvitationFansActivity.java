package com.poso2o.lechuan.activity.mine;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.MyFansQueryDTO;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.FansPopupMenu;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.MyFansDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-19.
 */

public class InvitationFansActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tvInvitation;
    private ArrayList<MyFansDTO> myFansDTOs = new ArrayList<>();
    //网络管理
    private MyFansDataManager mMyFansDataManager;
    private ArrayList<MyFansDTO> checkedFans = new ArrayList<>();
    /**
     * 列表
     */
    private RecyclerView my_fans_recycler;
    private RecyclerView.LayoutManager my_fans_manager;
    private BaseAdapter myFollowAdapter;
    private SwipeRefreshLayout my_fans_refresh;
    private TextView tvTotalSelected;//, tvTotalInvitation;
    //    private LinearLayout layoutFansType;
    private int totalFans = 0;
    private int currPage = 1;
    private int totalPage = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invitation_fans;
    }

    @Override
    protected void initView() {
        setTitle("邀请会员");
        tvInvitation = findView(R.id.tv_invitation);
        //列表
        my_fans_recycler = (RecyclerView) findViewById(R.id.my_fans_recycler);
        my_fans_recycler.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        my_fans_manager = new LinearLayoutManager(activity);
        my_fans_recycler.setLayoutManager(my_fans_manager);
        my_fans_refresh = (SwipeRefreshLayout) findViewById(R.id.my_fans_refresh);
        my_fans_refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);
        my_fans_refresh.setOnRefreshListener(this);
        tvTotalSelected = findView(R.id.tv_total_select);
//        tvTotalInvitation = findView(R.id.tv_total_invitation);
//        layoutFansType = findView(R.id.layout_type);
    }

    @Override
    protected void initData() {
        mMyFansDataManager = MyFansDataManager.getMyFansDataManager();
//        mInvitingDialog = new InvitingDialog(activity);
        //列表
        myFollowAdapter = new BaseAdapter(activity, myFansDTOs) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, final int position) {
                final MyFansDTO bean = (MyFansDTO) item;
                ImageView checkBox = holder.getView(R.id.checkbox);
                ImageView ivLogo = holder.getView(R.id.my_follow_item_img);
                ImageView ivType = holder.getView(R.id.my_follow_item_type);
                TextView tvName = holder.getView(R.id.my_follow_item_name);
                TextView tvPublishNum = holder.getView(R.id.tv_publish_num);
                TextView tvFansNum = holder.getView(R.id.tv_fans_num);
                TextView tvInvitation = holder.getView(R.id.my_follow_item_distribution_btn);
                checkBox.setSelected(bean.checked);
                if (bean.user_type == Constant.MERCHANT_TYPE) {
                    if (bean.has_distributor != 1) {//只有商家，并且还不是你的分销商才显示
                        tvInvitation.setVisibility(View.VISIBLE);
                    }
                    ivType.setImageResource(R.mipmap.poster_seller_icon);
                } else {
                    ivType.setImageResource(R.mipmap.poster_seller_icon_b);
                }
                tvName.setText(bean.nick);
                tvPublishNum.setText(bean.news_number + "");
                tvFansNum.setText(bean.fans_number + "");
                Glide.with(activity).load(bean.logo).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(ivLogo);
                tvInvitation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showInvitingDialog(bean.uid+"", bean.nick);
                    }
                });
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.layout_invitation_fans_item, parent));
            }
        };
        myFollowAdapter.setOnItemClickListener2(new BaseAdapter.OnItemClickListener2() {
            @Override
            public void onItemClick(Object item, int position) {
                MyFansDTO dto = (MyFansDTO) item;
                if (dto.has_distributor == 1) {
                    Toast.show(activity, "已经是你的分销商了！");
                    return;
                }
                myFansDTOs.get(position).checked = !dto.checked;
                myFollowAdapter.notifyDataSetChanged();
                setTotalInfo();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        my_fans_recycler.setLayoutManager(linearLayoutManager);
        my_fans_recycler.setAdapter(myFollowAdapter);

        //获取我的关注数据
        loadPosterData(1);
    }

    @Override
    protected void initListener() {
        tvInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckedNickAndUid();
                showInvitingDialog(checkUid, checkNick);
            }
        });
//        layoutFansType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (fansPopupMenu != null) {
//                    fansPopupMenu.showAsDropDown(layoutFansType);
//                }
//            }
//        });
    }

    int mInvitation = 0;//分销商数量

    /**
     * 获取选中的粉丝
     *
     * @return
     */
    private ArrayList<MyFansDTO> getCheckedSize() {
        mInvitation = 0;
        checkedFans = new ArrayList<>();
        for (MyFansDTO dto : myFansDTOs) {
            if (dto.has_distributor == 1) {
                ++mInvitation;
            }
            if (dto.checked) {
                checkedFans.add(dto);
            }
        }
        return checkedFans;
    }

//    private ArrayList<MyFansDTO> getInvitationFans(boolean bool){
//
//    }

    private String checkUid = "", checkNick = "";

    /**
     * 获取选中粉丝的昵称和UID
     *
     * @return
     */
    private void getCheckedNickAndUid() {
        StringBuilder uidBuilder = new StringBuilder();
        StringBuilder nickBuilder = new StringBuilder();
        for (MyFansDTO dto : checkedFans) {
            uidBuilder.append(dto.uid + ",");
            nickBuilder.append(dto.nick + ",");
        }
        checkNick = nickBuilder.toString();
        checkUid = uidBuilder.toString();
    }

    /**
     * 未成为我分销商的粉丝,0全部，1分销商，2不是分销商
     */
    public void loadPosterData(int currPage) {
        my_fans_refresh.setRefreshing(true);
        mMyFansDataManager.loadFansData(activity, "" + currPage, 2, new IRequestCallBack<MyFansQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                my_fans_refresh.setRefreshing(false);
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, MyFansQueryDTO myFansQueryDTO) {
                my_fans_refresh.setRefreshing(false);
                if (myFansQueryDTO != null && myFansQueryDTO.total != null) {
                    totalFans = myFansQueryDTO.total.rowcount;
                }
                if (myFansQueryDTO.list != null) {
                    refreshItem(myFansQueryDTO.list);
                } else {
                    Toast.show(activity, "没有任何信息！");
                }
                initFansTypeDialog();
                setTotalInfo();
            }
        });
    }

    /**
     * 刷新列表信息
     */
    private void refreshItem(ArrayList<MyFansDTO> posterData) {
        if (currPage == 1) {
            myFansDTOs = posterData;
        } else {
            myFansDTOs.addAll(posterData);
        }
        myFollowAdapter.notifyDataSetChanged(myFansDTOs);
    }

    private void setTotalInfo() {
        getCheckedSize();
        int notInvitation = totalFans - mInvitation;
        tvTotalSelected.setText(checkedFans.size() + "/" + totalFans);
//        tvTotalInvitation.setText("(已邀请" + mInvitation + "，未邀请" + notInvitation + ")");
        tvInvitation.setSelected(checkedFans.size() <= 0 ? true : false);
        tvInvitation.setEnabled(checkedFans.size() <= 0 ? false : true);
    }

    @Override
    public void onRefresh() {
        my_fans_refresh.setRefreshing(true);
        currPage = 1;
        loadPosterData(1);
    }


    /**
     * 邀请确认窗口
     */
    private void showInvitingDialog(final String uid, String nick) {
        InvitationDistributionDialog mInvitingDialog = new InvitationDistributionDialog(activity, nick, new InvitationDistributionDialog.DialogClickCallBack() {
            @Override
            public void setAffirm() {
                toInviting(uid);
            }
        });
        mInvitingDialog.show();
    }

    /**
     * 邀请分销
     */
    private void toInviting(String uid) {
        WaitDialog.showLoaddingDialog(activity);
        float rate = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE);
        float discount = SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT);
        mMyFansDataManager.loadFansInvitingData(activity, uid, rate + "", discount + "", new IRequestCallBack<InvitationBean>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, InvitationBean invitationBean) {
                Toast.show(activity, "发送邀请成功！");
            }
        });
    }

    private FansPopupMenu fansPopupMenu;

    /**
     * 弹窗，粉丝筛选
     */
    private void initFansTypeDialog() {
        String[] items = new String[]{"全部（" + totalFans + "）", "已邀请（" + mInvitation + "）", "未邀请（" + (totalFans - mInvitation) + "）"};
        fansPopupMenu = new FansPopupMenu(activity, items);
        fansPopupMenu.setOnItemClickListener(new FansPopupMenu.OnMenuItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

}
