package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.CouponAdapter;
import com.poso2o.lechuan.adapter.MarketingDGAdapter;
import com.poso2o.lechuan.adapter.PerReduceAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.marketdata.CouponData;
import com.poso2o.lechuan.bean.marketdata.MarketingDGData;
import com.poso2o.lechuan.bean.marketdata.MemberBirthdayData;
import com.poso2o.lechuan.bean.marketdata.PerReduceData;
import com.poso2o.lechuan.dialog.CommonDelDialog;
import com.poso2o.lechuan.layoutmanager.MyLinearLayoutManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by mr zhang on 2017/11/1.
 * 营销活动
 */

public class RMarketActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //满减活动页面跳转请求码
    public static final int PER_REDUCE_CODE = 1000;
    //商家优惠券页面跳转请求码
    public static final int COUPON_CODE = 1001;
    //折扣商品页面跳转请求码
    public static final int DISCOUNT_GOODS_CODE = 1010;
    //会员生日页面跳转请求码
    public static final int MEMBER_BIRTHDAY_CODE = 1011;
    //满减活动编辑页跳转请求码
    public static final int EDIT_REDUCE_CODE = 1100;
    //商家优惠券详情页面跳转请求码
    public static final int COUPON_INFO_COUDE = 1101;
    //会员生日专享详情跳转请求码
    public static final int MEMBER_BIR_INFO_CODE = 1110;

    //满减活动详情
    public static final String REDUCE_INFO = "reduce_info";
    //会员生日专享详情
    public static final String MEMBER_BIRTHDAY_INFO = "memebr_birthday_info";
    //折扣商品活动详情
    public static final String DISCOUNT_GOODS_DATA = "discount_goods_data";

    //返回按钮
    private ImageView marketing_back;

    //满减活动
    private RelativeLayout per_reduce_layout;
    //满减活动列表
    private RecyclerView per_reduce_recycler;
    //满减列表适配器
    private PerReduceAdapter reduceAdapter;

    //商家优惠
    private RelativeLayout coupon_layout;
    //优惠列表
    private RecyclerView coupon_recycler;
    //优惠券列表适配器
    private CouponAdapter couponAdapter;

    //折扣商品
    private RelativeLayout discount_goods_layout;
    //折扣商品列表
    private RecyclerView discount_goods_recycler;
    //折扣商品列表适配器
    private MarketingDGAdapter dgAdapter;

    //会员活动
    private RelativeLayout member_birthday_add;
    //会员添加
    private TextView member_add_tag;

    //会员活动具体版面
    private RelativeLayout member_birthday;
    //会员活动具体描述
    private TextView member_birthday_des;
    //会员活动有效时间
    private TextView member_birthday_during;
    //编辑会员活动
    private ImageButton member_birthday_edit;
    //删除会员活动
    private ImageButton member_birthday_del;
//    private SharedPreferencesUtil sharedPreferencesUtil;

    //会员数据
    private MemberBirthdayData birthdayData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_marketing;
    }

    @Override
    public void initView() {
        context = this;

        marketing_back = (ImageView) findViewById(R.id.marketing_back);

        per_reduce_layout = (RelativeLayout) findViewById(R.id.per_reduce_layout);
        per_reduce_recycler = (RecyclerView) findViewById(R.id.per_reduce_recycler);

        coupon_layout = (RelativeLayout) findViewById(R.id.coupon_layout);
        coupon_recycler = (RecyclerView) findViewById(R.id.coupon_recycler);

        discount_goods_layout = (RelativeLayout) findViewById(R.id.discount_goods_layout);
        discount_goods_recycler = (RecyclerView) findViewById(R.id.discount_goods_recycler);

        member_birthday_add = (RelativeLayout) findViewById(R.id.member_birthday_add);
        member_add_tag = (TextView) findViewById(R.id.member_add_tag);

        member_birthday = (RelativeLayout) findViewById(R.id.member_birthday);
        member_birthday_des = (TextView) findViewById(R.id.member_birthday_des);
        member_birthday_during = (TextView) findViewById(R.id.member_birthday_during);
        member_birthday_edit = (ImageButton) findViewById(R.id.member_birthday_edit);
        member_birthday_del = (ImageButton) findViewById(R.id.member_birthday_del);
    }

    @Override
    public void initData() {
//        sharedPreferencesUtil = new SharedPreferencesUtil(context, LoginMemory.LOGIN_INFO);

        initReduceAdapter();
        initPerReduceData();

        initCouponAdapter();
        initCouponData();

        initDGAdapter();
        initDGData();

        loadBirthday();
    }

    @Override
    public void initListener() {
        marketing_back.setOnClickListener(this);

        per_reduce_layout.setOnClickListener(this);
        coupon_layout.setOnClickListener(this);
        discount_goods_layout.setOnClickListener(this);
        member_add_tag.setOnClickListener(this);

        member_birthday_edit.setOnClickListener(this);
        member_birthday_del.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.marketing_back:
                finish();
                break;
            case R.id.per_reduce_layout:
                goToIntent(PerReduceActivity.class,PER_REDUCE_CODE);
                break;
            case R.id.coupon_layout:
                goToIntent(CouponActivity.class,COUPON_CODE);
                break;
            case R.id.discount_goods_layout:
                goToIntent(DiscountGoodsActivity.class,DISCOUNT_GOODS_CODE);
                break;
            case R.id.member_add_tag:
                goToIntent(AddMemberSpecialActivity.class,MEMBER_BIRTHDAY_CODE);
                break;
            case R.id.member_birthday_edit:
                memberBirthdayInfo();
                break;
            case R.id.member_birthday_del:
                tipsDelMemberBirthday();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)return;
        switch (requestCode){
            case PER_REDUCE_CODE:
                showLoading("正在加载数据...");
                initPerReduceData();
                break;
            case COUPON_CODE:
                showLoading("正在加载数据...");
                initCouponData();
                break;
            case DISCOUNT_GOODS_CODE:
                showLoading("正在加载数据...");
                initDGData();
                break;
            case MEMBER_BIRTHDAY_CODE:
                showLoading("正在加载数据...");
                loadBirthday();
                break;
            case EDIT_REDUCE_CODE:
                showLoading("正在加载数据...");
                initPerReduceData();
                break;
        }
    }

    //跳转页面
    private void goToIntent(Class a,int code){
        Intent intent = new Intent();
        intent.setClass(context,a);
        startActivityForResult(intent,code);
    }

    private void initReduceAdapter(){
        reduceAdapter = new PerReduceAdapter(context);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setScrollEnabled(false);
        per_reduce_recycler.setLayoutManager(linearLayoutManager);
        per_reduce_recycler.setAdapter(reduceAdapter);

        reduceAdapter.setOnPerReduceListener(new PerReduceAdapter.OnPerReduceListener() {
            @Override
            public void onPerReduceEdit(PerReduceData perReduceData) {
                Intent intent = new Intent();
                intent.setClass(context,PerReduceInfoActivity.class);
                intent.putExtra(REDUCE_INFO,perReduceData);
                startActivityForResult(intent,EDIT_REDUCE_CODE);
            }

            @Override
            public void onPerReduceDel(final PerReduceData perReduceData) {
                final CommonDelDialog commonDelDialog = new CommonDelDialog(context);
                commonDelDialog.show();
                commonDelDialog.setTips("确定删除 " + perReduceData.promotion_name + " 活动吗？");
                commonDelDialog.setOnCommonOkListener(new CommonDelDialog.OnCommonOkListener() {
                    @Override
                    public void onOkClick() {
                        delPerReduce(perReduceData.promotion_id);
                        commonDelDialog.dismiss();
                    }
                });
            }
        });
    }

    private void initCouponAdapter(){
        couponAdapter = new CouponAdapter(context);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setScrollEnabled(false);
        coupon_recycler.setLayoutManager(linearLayoutManager);
        coupon_recycler.setAdapter(couponAdapter);

        couponAdapter.setOnCouponListener(new CouponAdapter.OnCouponListener() {
            @Override
            public void onCouponInfoClick(final CouponData couponData) {
                Intent intent = new Intent();
                intent.setClass(context,CouponInfoActivity.class);
                intent.putExtra(CouponInfoActivity.COUPON_INFO_DOTA,couponData);
                startActivityForResult(intent,COUPON_INFO_COUDE);
            }

            @Override
            public void onCouponDelClick(final CouponData couponData) {
                final CommonDelDialog commonDelDialog = new CommonDelDialog(context);
                commonDelDialog.show();
                commonDelDialog.setTips("确定删除 " + couponData.title + " 优惠券吗？");
                commonDelDialog.setOnCommonOkListener(new CommonDelDialog.OnCommonOkListener() {
                    @Override
                    public void onOkClick() {
                        delCouponData(couponData.card_id);
                        commonDelDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCouponShareClick(CouponData couponData) {
                shareWeb(couponData);
            }
        });
    }

    private void initDGAdapter(){
        dgAdapter = new MarketingDGAdapter(context);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setScrollEnabled(false);
        discount_goods_recycler.setLayoutManager(linearLayoutManager);
        discount_goods_recycler.setAdapter(dgAdapter);

        dgAdapter.setOnMDGListener(new MarketingDGAdapter.OnMDGListener() {
            @Override
            public void onEditMDG(MarketingDGData marketingDGData) {
                Intent intent = new Intent();
                intent.setClass(context,DiscountGoodsActivity.class);
                intent.putExtra(DISCOUNT_GOODS_DATA,marketingDGData);
                startActivityForResult(intent,DISCOUNT_GOODS_CODE);
            }

            @Override
            public void onDelMDG(final MarketingDGData marketingDGData) {
                final CommonDelDialog commonDelDialog = new CommonDelDialog(context);
                commonDelDialog.show();
                commonDelDialog.setTips("确定删除 " + marketingDGData.promotion_name + " 折扣活动吗？");
                commonDelDialog.setOnCommonOkListener(new CommonDelDialog.OnCommonOkListener() {
                    @Override
                    public void onOkClick() {
                        delDG(marketingDGData);
                        commonDelDialog.dismiss();
                    }
                });
            }
        });
    }

    //满减活动数据
    private void initPerReduceData(){
        /*MarketPRManager.getMarketPRManager().listPerReduce(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new MarketPRManager.OnListPerReduceListener() {
            @Override
            public void onListPerReduceSuccess(BaseActivity baseActivity, PerReduceListData perReduceListData) {
                dismissReadDialog();
                if (perReduceListData == null)return;
                reduceAdapter.notifyAdapterData(perReduceListData.list);
            }

            @Override
            public void onListPerReduceFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(context,failStr).show();
            }
        });*/
    }

    //删除满减活动
    private void delPerReduce(String promotion_id){
        /*showReadDialog();
        MarketPRManager.getMarketPRManager().delPerReduce(this,sharedPreferencesUtil.getString(LoginMemory.SHOP_ID),promotion_id, new MarketPRManager.OnDelPerReduceListener() {
            @Override
            public void onDelReduceSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                initPerReduceData();
            }

            @Override
            public void onDelReduceFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //优惠券列表数据
    private void initCouponData(){
        /*CouponManager.getCouponManager().listCoupon(this,sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new CouponManager.OnListCouponListener() {
            @Override
            public void onListCouponSuccess(BaseActivity baseActivity, CouponListData couponListData) {
                dismissReadDialog();
                if (couponListData != null && couponAdapter != null){
                    couponAdapter.notifyListData(couponListData.list);
                }
            }

            @Override
            public void onListCouponFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(context,failStr).show();
            }
        });*/
    }

    //删除优惠券
    private void delCouponData(String card_id){
        /*showReadDialog();
        CouponManager.getCouponManager().delCoupon(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), card_id, new CouponManager.OnDelCouponListener() {
            @Override
            public void onDelCouponSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                initCouponData();
            }

            @Override
            public void onDelCouponFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //分享优惠券
    private void shareWeb(CouponData couponData) {
        UMImage thumb = new UMImage(this, couponData.cardimg_url);
        UMWeb web = new UMWeb("http://baidu.com");
        web.setThumb(thumb);
        web.setDescription("满" + couponData.use_limit + "元可用");
        web.setTitle(couponData.title);
        new ShareAction(this).withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        }).share();
    }

    //折扣商品活动数据
    private void initDGData(){
        /*DiscountGoodsManager.getDiscountGoodsManager().listDiscountGoods(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new DiscountGoodsManager.OnListDGListener() {
            @Override
            public void onListDGSuccess(BaseActivity baseActivity, MarketingDGListData marketingDGListData) {
                dismissReadDialog();
                if (dgAdapter != null && marketingDGListData != null){
                    dgAdapter.notifyAdapterData(marketingDGListData.list);
                }
            }

            @Override
            public void onListDGFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //删除折扣商品活动
    private void delDG(MarketingDGData marketingDGData){
        /*showReadDialog();
        DiscountGoodsManager.getDiscountGoodsManager().delDiscountGoods(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), marketingDGData.promotion_id, new DiscountGoodsManager.OnDelDGListener() {
            @Override
            public void onDelDGSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                initDGData();
            }

            @Override
            public void onDelDGFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //请求会员专享数据
    private void loadBirthday(){
        /*MemberBirthdayManager.getBirthdayManager().infoMemBirthday(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new MemberBirthdayManager.OnInfoMemBirthdayListener() {
            @Override
            public void onInfoBirthdaySuc(BaseActivity baseActivity, MemberBirthdayData memberBirthdayData) {
                dismissReadDialog();
                if (memberBirthdayData == null || memberBirthdayData.promotion_name == null){
                    member_birthday.setVisibility(View.GONE);
                    member_add_tag.setVisibility(View.VISIBLE);
                    return;
                }
                birthdayData = memberBirthdayData;
                member_add_tag.setVisibility(View.GONE);
                member_birthday.setVisibility(View.VISIBLE);
                member_birthday_des.setText(memberBirthdayData.promotion_name);
                member_birthday_during.setText("自会员生日起" + memberBirthdayData.days + "天内有效。");
            }

            @Override
            public void onInfoBirthdayFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(context,failStr).show();
            }
        });*/
    }

    //删除会员生日活动提示弹窗
    private void tipsDelMemberBirthday(){
        final CommonDelDialog commonDelDialog = new CommonDelDialog(context);
        commonDelDialog.show();
        commonDelDialog.setTips("确定删除 " + birthdayData.promotion_name + " 会员专享吗？");
        commonDelDialog.setOnCommonOkListener(new CommonDelDialog.OnCommonOkListener() {
            @Override
            public void onOkClick() {
                delMemberBirthday();
                commonDelDialog.dismiss();
            }
        });
    }

    //删除会员生日专享
    private void delMemberBirthday(){
        /*showReadDialog();
        MemberBirthdayManager.getBirthdayManager().delMemBirthday(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), new MemberBirthdayManager.OnDelMemBirthdayListener() {
            @Override
            public void onDelBirthdaySuc(BaseActivity baseActivity) {
                dismissReadDialog();
                member_birthday.setVisibility(View.GONE);
                member_add_tag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDelBirthdayFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //跳转会员专享编辑页
    private void memberBirthdayInfo(){
        Intent intent = new Intent();
        intent.setClass(context,AddMemberSpecialActivity.class);
        intent.putExtra(MEMBER_BIRTHDAY_INFO,birthdayData);
        startActivityForResult(intent,MEMBER_BIR_INFO_CODE);
    }
}
