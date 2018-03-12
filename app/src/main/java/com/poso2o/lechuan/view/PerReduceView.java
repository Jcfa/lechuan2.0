package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.marketdata.PromotionDetailData;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;

/**
 * Created by mr zhang on 2017/11/1.
 * 具体满减条件
 */

public class PerReduceView extends BaseView {

    private View view;
    private Context context;

    //消费金额
    private EditText per_reduce_to_amount;
    //减去金额
    private EditText per_reduce_amount;
    //删除按钮
    private ImageButton per_reduce_del;

    //满减
    private PromotionDetailData promotionDetailData;

    public PerReduceView(Context context,PromotionDetailData promotionDetailData) {
        super(context);
        this.context = context;
        this.promotionDetailData = promotionDetailData;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_per_reduce,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return view;
    }

    @Override
    public void initView() {
        per_reduce_to_amount = (EditText) view.findViewById(R.id.per_reduce_to_amount);
        per_reduce_amount = (EditText) view.findViewById(R.id.per_reduce_amount);
        per_reduce_del = (ImageButton) view.findViewById(R.id.per_reduce_del);
    }

    @Override
    public void initData() {
        initMyListener();
    }

    private void initMyListener(){
        per_reduce_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelPerReduceListener != null)onDelPerReduceListener.onDelPerReduce(view,promotionDetailData);
            }
        });

        per_reduce_to_amount.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                String str = per_reduce_to_amount.getText().toString();
                if (str == null )str ="0";
                if (str.startsWith("."))per_reduce_to_amount.setText("0" + str);
                if (str.endsWith("."))str = str.substring(0,str.length()-1);
                promotionDetailData.amount_moq = str;
            }
        });

        per_reduce_amount.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void input(String s, int start, int before, int count) {
                String str = per_reduce_amount.getText().toString();
                if (str == null )str ="0";
                if (str.startsWith("."))per_reduce_amount.setText("0" + str);
                if (str.endsWith("."))str = str.substring(0,str.length()-1);
                promotionDetailData.delete_amount = str;
            }
        });
    }

    public void setAmount(String to_amount,String reduce_amount){
        per_reduce_to_amount.setText(to_amount);
        per_reduce_amount.setText(reduce_amount);
    }

    private OnDelPerReduceListener onDelPerReduceListener;
    public interface OnDelPerReduceListener{
        void onDelPerReduce(View view, PromotionDetailData promotionDetailData);
    }
    public void setOnDelPerReduceListener(OnDelPerReduceListener onDelPerReduceListener){
        this.onDelPerReduceListener = onDelPerReduceListener;
    }
}
