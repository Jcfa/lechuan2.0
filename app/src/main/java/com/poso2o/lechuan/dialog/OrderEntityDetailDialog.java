package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoEntityDetailBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 * 实体店 订单详情界面
 * 自下而上的
 */

public class OrderEntityDetailDialog extends BaseDialog {

    private View view;
    //店铺  订单号  人名 价格
    private TextView tvName, tvDnumber, tvPopleName, tvPrice;
    private ImageView ivClose, ivHead;
    //数量 金额  合计数量 合计金额
    private TextView tvNum, tvMoney, tvTotalNum, tvTotalMoney;
    //打折 支付方式 应收金额  实收金额
    private TextView tvdis, tvWay, tvAccountMoney, tvPaidMoney;
    private ScrollView svView;

    public OrderEntityDetailDialog(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_order_entity_detail, null);
        return view;
    }

    @Override
    public void initView() {
        tvName = (TextView) findViewById(R.id.tv_paper_detail_name);
        tvDnumber = (TextView) findViewById(R.id.tv_entity_dnumber);
        ivClose = (ImageView) findViewById(R.id.iv_paper_click_close);
        ivHead = (ImageView) findViewById(R.id.iv_entity_head);
        tvPopleName = (TextView) findViewById(R.id.tv_entity_name);
        tvPrice = (TextView) findViewById(R.id.tv_entity_price);
        tvNum = (TextView) findViewById(R.id.tv_entity_num);
        tvMoney = (TextView) findViewById(R.id.tv_entity_money);
        tvTotalNum = (TextView) findViewById(R.id.tv_entity_total_num);
        tvTotalMoney = (TextView) findViewById(R.id.tv_entity_total_money);
        tvdis = (TextView) findViewById(R.id.tv_entity_dis);
        tvWay = (TextView) findViewById(R.id.tv_entity_way);
        tvAccountMoney = (TextView) findViewById(R.id.tv_entity_account_money);
        tvPaidMoney = (TextView) findViewById(R.id.tv_entity_paid_money);
        svView = (ScrollView) findViewById(R.id.sv_view);

    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowDispalay(1.0f, 0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);

    }

    public void setNetData(String order_id) {
        Log.d("cbf", "order_id= " + order_id);
        OrderInfoManager.getInfoManager().orderEntityDetailApi((BaseActivity) context, order_id,
                new IRequestCallBack<OrderInfoEntityDetailBean>() {
                    @Override
                    public void onResult(int tag, OrderInfoEntityDetailBean detailBean) {
                        ((BaseActivity) context).dismissLoading();
                        tvName.setText("店铺:" + detailBean.getShopname());
                        tvDnumber.setText("订单号:" + detailBean.getOrder_id());
                        tvPopleName.setText(detailBean.getProducts().get(0).getName());
                        //1.0 设置文本中一些字段颜色
//                        SpannableStringBuilder ssb = new SpannableStringBuilder();
//                        ssb.append("-/-\n");
//                        ssb.append(detailBean.getProducts().get(0).getPrice());
//                        ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(254, 58, 54));
//                        ssb.setSpan(span, 3, 10, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        //2.0 设置文本中一些字段颜色
                        String price =  "<font color=\"#ba2323\" > " + detailBean.getProducts().get(0).getPrice() + " </font >";
                        tvPrice.setText(Html.fromHtml(price));
                        tvNum.setText(detailBean.getProducts().get(0).getNum());
                        tvMoney.setText(detailBean.getProducts().get(0).getFprice());
                        tvTotalNum.setText(detailBean.getOrder_num());
                        tvTotalMoney.setText(detailBean.getPayment_amount());
                        tvdis.setText(detailBean.getOrder_discount());
                        tvWay.setText(detailBean.getPayment_jsfs());
                        tvAccountMoney.setText(detailBean.getOrder_amount());
                        tvPaidMoney.setText(detailBean.getOrder_discount());
                        Glide.with(context).load(detailBean.getProducts().get(0).getImage222()).error(R.drawable.expicture).into(ivHead);

                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        ((BaseActivity) context).dismissLoading();
                        Toast.show(context, msg);

                    }
                });
    }

    @Override
    public void initListener() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
