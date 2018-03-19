package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderInfoSellDetailBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoGoodsManager;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by ${cbf} on 2018/3/17 0017.
 * 畅销商品详情界面
 * 自下而上的弹出
 */

public class OrderInfoSellDialog extends BaseDialog {

    private View view;
    private ImageView ivHead, ivClickClose;
    private TextView tvName, tvHnumber, tvSpf, tvSpfNum, tvSpfMoney, tvSpfProfits, tvSpfKc, tvPrice;

    public OrderInfoSellDialog(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_order_sell, null);
        return view;
    }

    @Override
    public void initView() {
        ivHead = (ImageView) findViewById(R.id.iv_paper_detail_head);
        tvName = (TextView) findViewById(R.id.tv_paper_detail_name);
        tvPrice = (TextView) findViewById(R.id.tv_paper_detail_price);
        ivClickClose = (ImageView) findViewById(R.id.iv_paper_click_close);
        tvHnumber = (TextView) findViewById(R.id.tv_sell_hnumber);
        tvSpf = (TextView) findViewById(R.id.tv_sell_spf);
        tvSpfNum = (TextView) findViewById(R.id.tv_sell_spf_num);
        tvSpfMoney = (TextView) findViewById(R.id.tv_sell_spf_money);
        tvSpfProfits = (TextView) findViewById(R.id.tv_sell_spf_profits);
        tvSpfKc = (TextView) findViewById(R.id.tv_sell_spf_kc);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowDispalay(1.0f, 0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);
    }

    @Override
    public void initListener() {
        ivClickClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setData(String beginTime, String endTime, String guid, String colorid, String sizeid) {
        OrderInfoGoodsManager.getOrderInfo().orderInfoDetailSell((BaseActivity) context, beginTime,
                endTime, guid, colorid, sizeid, new IRequestCallBack<OrderInfoSellDetailBean>() {
                    @Override
                    public void onResult(int tag, OrderInfoSellDetailBean sellDetailBean) {
                        ((BaseActivity) context).dismissLoading();
                        tvName.setText(sellDetailBean.getName());
                        tvHnumber.setText(sellDetailBean.getBh());//编号
                        Glide.with(context).load(sellDetailBean.getImage222()).error(R.drawable.expicture).into(ivHead);
                        tvSpf.setText(sellDetailBean.getSizeid());//规格
                        tvPrice.setText(sellDetailBean.getPrice() + "  成本:" + sellDetailBean.getFprice());//价格
                        tvSpfNum.setText(sellDetailBean.getTotal_num());
                        tvSpfKc.setText(sellDetailBean.getKcnum());//库存
                        tvSpfProfits.setText(sellDetailBean.getTotal_amount());
                        String total_amount = sellDetailBean.getTotal_amount();//销售金额
                        String fprice = sellDetailBean.getFprice();//成本价
                        String kcnum = sellDetailBean.getKcnum();//库存数量
                        //利润=销售金额-（成本价*库存数量）
                        Double spfMoney = Double.parseDouble(total_amount) - (Double.parseDouble(fprice) * Integer.parseInt(kcnum));
                        tvSpfMoney.setText(spfMoney + "");//利润


                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        ((BaseActivity) context).dismissLoading();
                        Toast.show(context, msg);

                    }
                });
    }
}
