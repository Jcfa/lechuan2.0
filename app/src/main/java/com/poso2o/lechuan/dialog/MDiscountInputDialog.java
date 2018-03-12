package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2017/11/17.
 */

public class MDiscountInputDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //关闭
    private ImageView input_discount_close;
    //输入
    private EditText batch_discount_input;
    //返回
    private TextView batch_discount_back;
    //确定
    private TextView batch_discount_ok;

    public MDiscountInputDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_md_discount,null);
        return view;
    }

    @Override
    public void initView() {
        input_discount_close = (ImageView) view.findViewById(R.id.input_discount_close);
        batch_discount_input = (EditText) view.findViewById(R.id.batch_discount_input);
        batch_discount_back = (TextView) view.findViewById(R.id.batch_discount_back);
        batch_discount_ok = (TextView) view.findViewById(R.id.batch_discount_ok);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.8,0.4);
    }

    @Override
    public void initListener() {
        input_discount_close.setOnClickListener(this);
        batch_discount_back.setOnClickListener(this);
        batch_discount_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.input_discount_close:
                dismiss();
                break;
            case R.id.batch_discount_back:
                dismiss();
                break;
            case R.id.batch_discount_ok:
                clickOk();
                break;
        }
    }

    private void clickOk(){
        String str = batch_discount_input.getText().toString();
        if (str == null || str.equals("")){
            Toast.show(context,"请输入折扣");
        }else if (str.startsWith(".") || str.endsWith(".")){
            Toast.show(context,"请输入完整的折扣");
        }else if (Double.parseDouble(str) > 100){
            Toast.show(context,"折扣不能大于100");
        }else {
            if (onDiscountListener != null)onDiscountListener.onDiscount(Double.parseDouble(str));
            dismiss();
        }
    }

    private OnDiscountListener onDiscountListener;
    public interface OnDiscountListener{
        void onDiscount(double discount);
    }
    public void setOnDiscountListener(OnDiscountListener onDiscountListener){
        this.onDiscountListener = onDiscountListener;
    }
}
