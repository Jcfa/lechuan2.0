package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 关闭订单窗口
 * Created by Luo on 2017/12/11.
 */
public class CloseOrderDialog extends BaseDialog {
    private Context context;
    private View view;
    private OrderDTO orderDTOs;
    private TextView dialog_close_order_a,dialog_close_order_b,dialog_close_order_c,dialog_close_order_close;

    /**
     * 回调
     */
    private OnContactBuyerListener onContactBuyerListener;

    public void show(OrderDTO orderDTOs, OnContactBuyerListener onContactBuyerListener) {
        this.onContactBuyerListener = onContactBuyerListener;
        this.orderDTOs = orderDTOs;
        super.show();
    }

    public CloseOrderDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_close_order, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1, OUT_TO);

        dialog_close_order_close = (TextView) view.findViewById(R.id.dialog_close_order_close);
        dialog_close_order_a = (TextView) view.findViewById(R.id.dialog_close_order_a);
        dialog_close_order_b = (TextView) view.findViewById(R.id.dialog_close_order_b);
        dialog_close_order_c = (TextView) view.findViewById(R.id.dialog_close_order_c);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //关闭
        dialog_close_order_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //缺货
        dialog_close_order_a.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(orderDTOs,"缺货");
                }
            }
        });
        //买家不想买了
        dialog_close_order_b.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(orderDTOs,"买家不想买了");
                }
            }
        });
        //其他原因
        dialog_close_order_c.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(orderDTOs,"其他原因");
                }
            }
        });

    }

    public interface OnContactBuyerListener {
        void onConfirm(OrderDTO orderDTOs,String remark);
    }
}
