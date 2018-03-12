package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.other.ConvertUtil;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * 修改一键改价窗口
 * Created by Luo on 2017/12/11.
 */
public class OrderModifyTotalDialog extends BaseDialog {
    private Context context;
    private View view;
    private OrderDTO orderDTO;
    private double price = 0;
    private double oldPrice = 0;
    private double discount = 0;
    private EditText dialog_modify_price_et,dialog_modify_discount_et;
    private TextView dialog_modify_price_cancel,dialog_modify_price_confirm;

    /**
     * 回调
     */
    private OnModifyPriceListener onModifyPriceListener;

    public void show(OrderDTO orderDTO, OnModifyPriceListener onModifyPriceListener) {
        this.orderDTO = orderDTO;
        this.onModifyPriceListener = onModifyPriceListener;
        super.show();
        dialog_modify_price_et.setText("" + NumberFormatUtils.format(orderDTO.order_amount));
        if (orderDTO.order_discount<=0){
            orderDTO.order_discount = 100.00;
        }
        dialog_modify_discount_et.setText("" + NumberFormatUtils.format(orderDTO.order_discount));
        oldPrice = orderDTO.order_amount;
        KeyboardUtils.showSoftInput(dialog_modify_price_et);
    }

    public OrderModifyTotalDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_modify_total, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(0.8f, OUT_TO);

        dialog_modify_price_et = (EditText) view.findViewById(R.id.dialog_modify_price_et);
        dialog_modify_discount_et = (EditText) view.findViewById(R.id.dialog_modify_discount_et);
        dialog_modify_price_cancel = (TextView) view.findViewById(R.id.dialog_modify_price_cancel);
        dialog_modify_price_confirm = (TextView) view.findViewById(R.id.dialog_modify_price_confirm);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //取消
        dialog_modify_price_cancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //确定
        dialog_modify_price_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onModifyPriceListener!=null){
                    String price = dialog_modify_price_et.getText().toString();
                    if (price.isEmpty()){
                        price = "0.00";
                    }
                    String discount = dialog_modify_discount_et.getText().toString();
                    if (discount.isEmpty()){
                        discount = "0.00";
                    }
                    onModifyPriceListener.onConfirm(price, discount);
                }
                dismiss();
            }
        });
        //售价
        dialog_modify_price_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Print.println("1_price=========================");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Print.println("2_price=========================");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Print.println("3_price=========================");

                if (!dialog_modify_price_et.isFocused())
                    return;

                price = ConvertUtil.convertToDouble(dialog_modify_price_et.getText().toString(),0);
                discount = ConvertUtil.convertToDouble(dialog_modify_discount_et.getText().toString(),0);

                /*if (price > oldPrice) {
                    dialog_modify_price_et.setText("" + NumberFormatUtils.format(oldPrice));
                    dialog_modify_discount_et.setText("100.00");
                } else {
                    discount = price / orderDTO.order_payable_amount * 100;
                    if (discount > 100){
                        discount = 100;
                    }
                    dialog_modify_discount_et.setText("" + NumberFormatUtils.format(discount));
                }*/
                discount = price / orderDTO.order_amount * 100;
                dialog_modify_discount_et.setText("" + NumberFormatUtils.format(discount));

                dialog_modify_price_et.setSelection(dialog_modify_price_et.getText().toString().length());//将光标移至文字末尾
                dialog_modify_discount_et.setSelection(dialog_modify_discount_et.getText().toString().length());//将光标移至文字末尾

            }
        });
        //折扣
        dialog_modify_discount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Print.println("1_discount=========================");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Print.println("2_discount=========================");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Print.println("3_discount=========================");

                if (!dialog_modify_discount_et.isFocused())
                    return;

                price = ConvertUtil.convertToDouble(dialog_modify_price_et.getText().toString(),0);
                discount = ConvertUtil.convertToDouble(dialog_modify_discount_et.getText().toString(),0);

                /*if (discount > 100){
                    discount = 100;
                    dialog_modify_discount_et.setText("100.00");
                    dialog_modify_discount_et.setSelection(dialog_modify_discount_et.getText().toString().length());//将光标移至文字末尾
                }*/
                price = orderDTO.order_amount * discount * 0.01;
                dialog_modify_price_et.setText("" + NumberFormatUtils.format(price));
                dialog_modify_price_et.setSelection(dialog_modify_price_et.getText().toString().length());//将光标移至文字末尾

            }
        });

    }

    public interface OnModifyPriceListener {
        void onConfirm(String price, String freight);
    }
}
