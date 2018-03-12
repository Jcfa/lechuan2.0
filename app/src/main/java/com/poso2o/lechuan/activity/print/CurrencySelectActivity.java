package com.poso2o.lechuan.activity.print;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.CurrencySelectAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.printer.CurrencyItemData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;

import java.util.ArrayList;

/**
 * Created by luo on 2017/09/19.
 * 货币符号选择界面
 */
public class CurrencySelectActivity extends BaseActivity {
    public static final int CURRENCY_RESULT_CODE = 1002;// 返回码

    private Context context;
    // 返回
    private ImageView size_select_back;
    //确定
    private FrameLayout size_select_confirm;

    //货币符号
    private TextView lable_print_text_view_present_price;
    private String currency_id;

    // 尺码列表展示
    private RecyclerView size_select_recycle;
    private LinearLayoutManager linearLayoutManager;
    private CurrencySelectAdapter mCurrencySelectAdapter;
    private ArrayList<CurrencyItemData> mCurrencyItemData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_currency_select;
    }

    @Override
    public void initView() {
        context = this;
        // 返回
        size_select_back = (ImageView) findViewById(R.id.size_select_back);
        //确定
        size_select_confirm = (FrameLayout) findViewById(R.id.size_select_confirm);
        //货币符号
        lable_print_text_view_present_price = (TextView) findViewById(R.id.lable_print_text_view_present_price);

        // 尺码列表展示
        size_select_recycle = (RecyclerView) findViewById(R.id.size_select_recycle);
        linearLayoutManager = new LinearLayoutManager(context);
        size_select_recycle.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void initData() {

        // 上一级页面传来的数据
        Intent intent = getIntent();
        if (intent.getStringExtra("currency_id") != null) {
            currency_id = intent.getStringExtra("currency_id").toString().trim();// 打印类型0：票据，1：标签
            lable_print_text_view_present_price.setText(currency_id + "128.00");
        }

        // 尺码列表展示
        mCurrencyItemData = new ArrayList<>();//¥

        String currencyDatas = "售价-,￥-(人民币),HK$-(港币),MOP$-(澳门元),NT$-(新台币),$-(美元),EUR-(欧元)";
        String[] strarray=currencyDatas.split("[,]");
        for (int i = 0; i < strarray.length; i++){
            String [] temp = null;
            temp = strarray[i].split("-");
            CurrencyItemData currencyItemData = new CurrencyItemData();
            currencyItemData.currency_id = temp[0];
            if (temp.length == 2){
                currencyItemData.currency_name = temp[1];
                if (currency_id.equals(temp[0])){
                    currencyItemData.currency_ing = true;
                }else{
                    currencyItemData.currency_ing = false;
                }
            }else{
                if (currency_id.equals("售价")){
                    currencyItemData.currency_ing = true;
                }
                currencyItemData.currency_name = "";
            }
            mCurrencyItemData.add(currencyItemData);
        }

        mCurrencySelectAdapter = new CurrencySelectAdapter(context, mCurrencyItemData);
        size_select_recycle.setAdapter(mCurrencySelectAdapter);


    }

    @Override
    public void initListener() {
        // 返回
        size_select_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //确定
        size_select_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("currency_id", currency_id);
                setResult(2001, intent);

                finish();
            }
        });
    }

    /**
     * 选择尺寸
     */
    public void selectCurrency(CurrencyItemData currencyItemData) {
        Print.println("currency========:"+currencyItemData.currency_id);
        currency_id = currencyItemData.currency_id;
        lable_print_text_view_present_price.setText(currencyItemData.currency_id + "128.00");
    }
}
