package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 输入快递单号窗口
 * Created by Luo on 2017/12/11.
 */
public class OrderExpressIdDialog extends BaseDialog {
    private Context context;
    private View view;
    private String freight;
    private EditText dialog_order_express_et;
    private TextView dialog_order_express_cancel,dialog_order_express_confirm;

    /**
     * 回调
     */
    private OrderExpressIdListener orderExpressIdListener;

    public void show(String freight, OrderExpressIdListener orderExpressIdListener) {
        this.orderExpressIdListener = orderExpressIdListener;
        super.show();
        dialog_order_express_et.setText(freight);
    }

    public OrderExpressIdDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_order_express_id, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(0.8f, OUT_TO);

        dialog_order_express_et = (EditText) view.findViewById(R.id.dialog_order_express_et);
        dialog_order_express_cancel = (TextView) view.findViewById(R.id.dialog_order_express_cancel);
        dialog_order_express_confirm = (TextView) view.findViewById(R.id.dialog_order_express_confirm);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //取消
        dialog_order_express_cancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //确定
        dialog_order_express_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (orderExpressIdListener!=null){
                    String freight = dialog_order_express_et.getText().toString();
                    orderExpressIdListener.onConfirm(freight);
                }
                dismiss();
            }
        });

    }

    public interface OrderExpressIdListener {
        void onConfirm(String freight);
    }
}
