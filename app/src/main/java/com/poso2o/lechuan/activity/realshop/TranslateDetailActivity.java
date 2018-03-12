package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.TranslateCashierTypeAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.transfer.Transfer;
import com.poso2o.lechuan.bean.transfer.TransferPaymentMethod;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/7.
 * 交接详情页
 */

public class TranslateDetailActivity extends BaseActivity implements View.OnClickListener{

    private Context context;

    public static final String TRANSFER_DATA = "transfer_data";

    //返回按钮
    private ImageView translate_cashier_detail_back;
    //收银员
    private TextView translate_detail_value_cashier;
    //收银时间
    private TextView translate_detail_cashier_time;
    //现金总额
    private TextView translate_detail_total_cashier;
    //现金列表
    private RecyclerView translate_detail_cashier_recycler;
    //支付宝总额
    private TextView translate_detail_ali_total;
    //支付宝列表
    private RecyclerView translate_detail_ali_recycler;
    //微信总额
    private TextView translate_detail_wechat_total;
    //微信列表
    private RecyclerView translate_detail_wechat_recycler;
    //余额总额
    private TextView translate_detail_balance_total;
    //余额列表
    private RecyclerView translate_detail_balance_recycler;
    //刷卡总额
    private TextView translate_detail_card_total;
    //刷卡列表
    private RecyclerView translate_detail_card_recycler;

    //现金订单列表适配器
    private TranslateCashierTypeAdapter cashierAdapter;
    //支付宝订单列表适配器
    private TranslateCashierTypeAdapter aliPayAdapter;
    //微信订单列表适配器
    private TranslateCashierTypeAdapter wechatAdapter;
    //余额订单列表适配器
    private TranslateCashierTypeAdapter balanceAdapter;
    //刷卡订单列表适配器
    private TranslateCashierTypeAdapter cardAdapter;
//    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_translate_cashier_detail;
    }

    @Override
    public void initView() {

        context = this;

        translate_cashier_detail_back = (ImageView) findViewById(R.id.translate_cashier_detail_back);

        translate_detail_value_cashier = (TextView) findViewById(R.id.translate_detail_value_cashier);

        translate_detail_cashier_time = (TextView) findViewById(R.id.translate_detail_cashier_time);

        translate_detail_total_cashier = (TextView) findViewById(R.id.translate_detail_total_cashier);

        translate_detail_cashier_recycler = (RecyclerView) findViewById(R.id.translate_detail_cashier_recycler);

        translate_detail_ali_total = (TextView) findViewById(R.id.translate_detail_ali_total);

        translate_detail_ali_recycler = (RecyclerView) findViewById(R.id.translate_detail_ali_recycler);

        translate_detail_wechat_total = (TextView) findViewById(R.id.translate_detail_wechat_total);

        translate_detail_wechat_recycler = (RecyclerView) findViewById(R.id.translate_detail_wechat_recycler);

        translate_detail_balance_total = (TextView) findViewById(R.id.translate_detail_balance_total);

        translate_detail_balance_recycler = (RecyclerView) findViewById(R.id.translate_detail_balance_recycler);

        translate_detail_card_total = (TextView) findViewById(R.id.translate_detail_card_total);

        translate_detail_card_recycler = (RecyclerView) findViewById(R.id.translate_detail_card_recycler); 
    }

    @Override
    public void initData() {
        initAdapter();
        getData();
    }

    @Override
    public void initListener() {
        translate_cashier_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.translate_cashier_detail_back:
                finish();
                break;
        }
    }

    private void initAdapter(){

        cashierAdapter = new TranslateCashierTypeAdapter(context);
        LinearLayoutManager cashierManager = new LinearLayoutManager(context);
        cashierManager.setOrientation(LinearLayoutManager.VERTICAL);
        translate_detail_cashier_recycler.setLayoutManager(cashierManager);
        translate_detail_cashier_recycler.setAdapter(cashierAdapter);

        aliPayAdapter = new TranslateCashierTypeAdapter(context);
        LinearLayoutManager aliPayManager = new LinearLayoutManager(context);
        aliPayManager.setOrientation(LinearLayoutManager.VERTICAL);
        translate_detail_ali_recycler.setLayoutManager(aliPayManager);
        translate_detail_ali_recycler.setAdapter(aliPayAdapter);

        wechatAdapter = new TranslateCashierTypeAdapter(context);
        LinearLayoutManager wechatManager = new LinearLayoutManager(context);
        wechatManager.setOrientation(LinearLayoutManager.VERTICAL);
        translate_detail_wechat_recycler.setLayoutManager(wechatManager);
        translate_detail_wechat_recycler.setAdapter(wechatAdapter);

        balanceAdapter = new TranslateCashierTypeAdapter(context);
        LinearLayoutManager balanceManager = new LinearLayoutManager(context);
        balanceManager.setOrientation(LinearLayoutManager.VERTICAL);
        translate_detail_balance_recycler.setLayoutManager(balanceManager);
        translate_detail_balance_recycler.setAdapter(balanceAdapter);

        cardAdapter = new TranslateCashierTypeAdapter(context);
        LinearLayoutManager cardManager = new LinearLayoutManager(context);
        cardManager.setOrientation(LinearLayoutManager.VERTICAL);
        translate_detail_card_recycler.setLayoutManager(cardManager);
        translate_detail_card_recycler.setAdapter(cardAdapter);
    }

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        Transfer transfer = (Transfer) bundle.get(TRANSFER_DATA);
        if (transfer == null)return;
        translate_detail_value_cashier.setText(transfer.czyname);
        translate_detail_cashier_time.setText(transfer.dat);

        /*for (TransferPaymentMethod method : transfer.paymentMethod){
            if (method.payment_method_type == 1 && cashierAdapter != null){
                translate_detail_total_cashier.setText(NumberFormatUtils.format(method.total_amount));
                cashierAdapter.setDatas(method.orders);
            }else if (method.payment_method_type == 2 && aliPayAdapter != null){
                translate_detail_ali_total.setText(NumberFormatUtils.format(method.total_amount));
                aliPayAdapter.setDatas(method.orders);
            }else if (method.payment_method_type == 3 && wechatAdapter != null){
                translate_detail_wechat_total.setText(NumberFormatUtils.format(method.total_amount));
                wechatAdapter.setDatas(method.orders);
            }else if (method.payment_method_type == 4 && balanceAdapter != null){
                translate_detail_balance_total.setText(NumberFormatUtils.format(method.total_amount));
                balanceAdapter.setDatas(method.orders);
            }else if (method.payment_method_type == 5 && cardAdapter != null){
                translate_detail_card_total.setText(NumberFormatUtils.format(method.total_amount));
                cardAdapter.setDatas(method.orders);
            }
        }*/

        translate_detail_total_cashier.setText(transfer.amount_cash);
        translate_detail_ali_total.setText(transfer.amount_alipay);
        translate_detail_wechat_total.setText(transfer.amount_wx);
        translate_detail_balance_total.setText(transfer.amount_user);
        translate_detail_card_total.setText(transfer.b_amount);

        initTransferInfo(transfer);
    }

    private void initTransferInfo(Transfer transfer){
        showLoading("正在加载交接详情...");
        RReportManager.getRReportManger().rTransferInfo(this, transfer.begin_date, transfer.end_date, transfer.czy, new IRequestCallBack() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }

            @Override
            public void onResult(int tag, Object object) {
                dismissLoading();

            }
        });
    }
}
