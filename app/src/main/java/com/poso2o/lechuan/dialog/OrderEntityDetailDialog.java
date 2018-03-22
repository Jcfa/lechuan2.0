package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.poso2o.lechuan.orderInfoAdapter.DialogEntityDetailAdapter;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 * 实体店 订单详情界面
 * 自下而上的
 */

public class OrderEntityDetailDialog extends BaseDialog {

    private View view;
    //店铺  订单号  人名 价格
    private TextView tvName;
    private ImageView ivClose;
    //数量 金额  合计数量 合计金额
    private TextView tvTotalNum, tvTotalMoney;
    //打折 支付方式 应收金额  实收金额
    private TextView tvdis, tvWay, tvAccountMoney, tvPaidMoney, tvDnumber, tv_entity_time;
    private ScrollView svView;
    private RecyclerView rlvDia;

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
        tvTotalNum = (TextView) findViewById(R.id.tv_entity_total_num);
        tvTotalMoney = (TextView) findViewById(R.id.tv_entity_total_money);
        tvdis = (TextView) findViewById(R.id.tv_entity_dis);
        tvWay = (TextView) findViewById(R.id.tv_entity_way);
        tvAccountMoney = (TextView) findViewById(R.id.tv_entity_account_money);
        tvPaidMoney = (TextView) findViewById(R.id.tv_entity_paid_money);
        svView = (ScrollView) findViewById(R.id.sv_view);
        rlvDia = (RecyclerView) findViewById(R.id.rlv_dialog);
        tv_entity_time = (TextView) findViewById(R.id.tv_entity_time);

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
                        List<OrderInfoEntityDetailBean.ProductsBean> list = detailBean.getProducts();
                        rlvDia.setLayoutManager(new LinearLayoutManager(context));
                        DialogEntityDetailAdapter adapter = new DialogEntityDetailAdapter(context, list);
                        rlvDia.setAdapter(adapter);
                        tvTotalNum.setText(detailBean.getOrder_num());
                        tvTotalMoney.setText(detailBean.getOrder_amount());
                        tvdis.setText(detailBean.getOrder_discount() + "%");
                        tvWay.setText(detailBean.getPayment_jsfs());
                        tvAccountMoney.setText(detailBean.getOrder_amount());
                        tvPaidMoney.setText(detailBean.getPayment_amount());
                        String time = detailBean.getOrder_date().substring(0, 19);
                        tv_entity_time.setText(time);

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
