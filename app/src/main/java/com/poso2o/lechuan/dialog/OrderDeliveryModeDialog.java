package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 发货方式窗口
 * Created by Luo on 2017/12/11.
 */
public class OrderDeliveryModeDialog extends BaseDialog {
    private Context context;
    private View view;
    private TextView dialog_delivery_mode_yes,dialog_delivery_mode_no,dialog_delivery_mode_close;

    /**
     * 回调
     */
    private OnOrderDeliveryModeListener onOrderDeliveryModeListener;

    public void show(OnOrderDeliveryModeListener onOrderDeliveryModeListener) {
        this.onOrderDeliveryModeListener = onOrderDeliveryModeListener;
        super.show();
    }

    public OrderDeliveryModeDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_delivery_mode, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1, OUT_TO);

        dialog_delivery_mode_yes = (TextView) view.findViewById(R.id.dialog_delivery_mode_yes);
        dialog_delivery_mode_no = (TextView) view.findViewById(R.id.dialog_delivery_mode_no);
        dialog_delivery_mode_close = (TextView) view.findViewById(R.id.dialog_delivery_mode_close);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //关闭
        dialog_delivery_mode_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //物流
        dialog_delivery_mode_yes.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onOrderDeliveryModeListener!=null){
                    onOrderDeliveryModeListener.onConfirm(0);
                }
                dismiss();
            }
        });
        //无需物流
        dialog_delivery_mode_no.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onOrderDeliveryModeListener!=null){
                    onOrderDeliveryModeListener.onConfirm(1);
                }
                dismiss();
            }
        });

    }

    public interface OnOrderDeliveryModeListener {
        void onConfirm(int type);
    }
}
