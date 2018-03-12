package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.CouponRecordAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.marketdata.CouponData;
import com.poso2o.lechuan.dialog.CommonDelDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by mr zhang on 2017/11/3.
 */

public class CouponInfoActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    //优惠券信息
    public static final String COUPON_INFO_DOTA = "coupon_info";

    //返回
    private ImageView coupon_info_back;
    //优惠券金额
    private TextView coupon_info_amount;
    //优惠券使用条件
    private TextView coupon_info_use_need;
    //优惠券数量
    private TextView coupon_info_total;
    //已领优惠券数量
    private TextView coupon_receive;
    //优惠券时间
    private TextView coupon_info_time;
    //删除
    private ImageButton coupon_info_del;
    //分享
    private ImageButton coupon_info_share;

    //历史数量
    private TextView coupon_record_num;
    //领取记录列表
    private RecyclerView receive_record_recycler;
    //列表适配器
    private CouponRecordAdapter adapter;

    //优惠券数据
    private CouponData couponData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_coupon_info;
    }

    @Override
    public void initView() {
        context = this;

        coupon_info_back = (ImageView) findViewById(R.id.coupon_info_back);
        coupon_info_amount = (TextView) findViewById(R.id.coupon_info_amount);
        coupon_info_use_need = (TextView) findViewById(R.id.coupon_info_use_need);
        coupon_info_total = (TextView) findViewById(R.id.coupon_info_total);
        coupon_receive = (TextView) findViewById(R.id.coupon_receive);
        coupon_info_time = (TextView) findViewById(R.id.coupon_info_time);

        coupon_info_del = (ImageButton) findViewById(R.id.coupon_info_del);
        coupon_info_share = (ImageButton) findViewById(R.id.coupon_info_share);

        coupon_record_num = (TextView) findViewById(R.id.coupon_record_num);
        receive_record_recycler = (RecyclerView) findViewById(R.id.receive_record_recycler); 
    }

    @Override
    public void initData() {
        initAdapter();
        initInfoData();
    }

    @Override
    public void initListener() {
        coupon_info_back.setOnClickListener(this);

        coupon_info_del.setOnClickListener(this);
        coupon_info_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.coupon_info_back:
                finish();
                break;
            case R.id.coupon_info_del:
                showTipsDelCoupon();
                break;
            case R.id.coupon_info_share:
                shareWeb();
                break;
        }
    }

    //初始化优惠券信息
    private void initInfoData(){
        couponData = (CouponData) getIntent().getExtras().get(COUPON_INFO_DOTA);
        if (couponData == null)return;

        coupon_info_amount.setText(couponData.card_amount);
        coupon_info_use_need.setText("满" + couponData.use_limit + "元可用。");
        coupon_info_total.setText("总数" + couponData.card_nums);
        coupon_receive.setText("已领" + couponData.card_issued_num);

        coupon_record_num.setText("（" + couponData.card_issued_num + "）");
    }

    private void initAdapter(){
        adapter = new CouponRecordAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        receive_record_recycler.setLayoutManager(linearLayoutManager);
        receive_record_recycler.setAdapter(adapter);
    }

    //弹窗提示删除优惠券
    private void showTipsDelCoupon(){
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

    //删除优惠券
    private void delCouponData(String card_id){

        /*SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context,LoginMemory.LOGIN_INFO);
        showReadDialog();
        CouponManager.getCouponManager().delCoupon(this, sharedPreferencesUtil.getString(LoginMemory.SHOP_ID), card_id, new CouponManager.OnDelCouponListener() {
            @Override
            public void onDelCouponSuccess(BaseActivity baseActivity) {
                dismissReadDialog();
                finish();
            }

            @Override
            public void onDelCouponFail(BaseActivity baseActivity, String failStr) {
                dismissReadDialog();
                new ToastView(baseActivity,failStr).show();
            }
        });*/
    }

    //分享优惠券
    private void shareWeb() {
        if (couponData == null)return;
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
}
