package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 同意退款窗口
 * Created by Luo on 2017/12/11.
 */
public class AgreeRefundDialog extends BaseDialog {
    private Context context;
    private View view;
    private TextView dialog_agree_refund_id,dialog_agree_refund_type,dialog_agree_refund_reason,
            dialog_agree_refund_amount,dialog_agree_refund_amount_b,dialog_agree_refund_cancel,dialog_agree_refund_agree;

    /**
     * 回调
     */
    private OnAgreeRefundListener onAgreeRefundListener;

    public void show(OrderDTO orderDTOs, OnAgreeRefundListener onAgreeRefundListener) {
        this.onAgreeRefundListener = onAgreeRefundListener;
        super.show();
        dialog_agree_refund_id.setText("" + orderDTOs.order_id);
        if (orderDTOs.orderRefund!=null){
            if (orderDTOs.orderRefund.refund_code == 0){
                dialog_agree_refund_type.setText("不需要退货");//退款类型
            }else{
                dialog_agree_refund_type.setText("需要退货");
            }
            dialog_agree_refund_reason.setText("" + orderDTOs.orderRefund.refund_remark);//退款原因
            dialog_agree_refund_amount_b.setText("" + orderDTOs.orderRefund.refund_amount);//实退金额
        }
        dialog_agree_refund_amount.setText("" + orderDTOs.order_payable_amount + "(含运费" + orderDTOs.freight + ")");//可退金额 128.00(含运费8.00)

    }

    public AgreeRefundDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_agree_refund, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.8f, OUT_TO);
        dialog_agree_refund_id = (TextView) view.findViewById(R.id.dialog_agree_refund_id);
        dialog_agree_refund_type = (TextView) view.findViewById(R.id.dialog_agree_refund_type);
        dialog_agree_refund_reason = (TextView) view.findViewById(R.id.dialog_agree_refund_reason);
        dialog_agree_refund_amount = (TextView) view.findViewById(R.id.dialog_agree_refund_amount);
        dialog_agree_refund_amount_b = (TextView) view.findViewById(R.id.dialog_agree_refund_amount_b);
        dialog_agree_refund_cancel = (TextView) view.findViewById(R.id.dialog_agree_refund_cancel);
        dialog_agree_refund_agree = (TextView) view.findViewById(R.id.dialog_agree_refund_agree);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //取消
        dialog_agree_refund_cancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //同意退款
        dialog_agree_refund_agree.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onAgreeRefundListener!=null){
                    onAgreeRefundListener.onConfirm();
                }
            }
        });

    }

    public interface OnAgreeRefundListener {
        void onConfirm();
    }
}
