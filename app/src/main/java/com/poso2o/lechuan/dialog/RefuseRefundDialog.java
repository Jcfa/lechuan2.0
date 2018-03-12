package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.Toast;

/**
 * 拒绝退款窗口
 * Created by Luo on 2017/12/11.
 */
public class RefuseRefundDialog extends BaseDialog {
    private Context context;
    private View view;
    private EditText dialog_refuse_refund_et;
    private TextView dialog_refuse_refund_cancel,dialog_refuse_refund_confirm;

    /**
     * 回调
     */
    private OnRefuseRefundListener onRefuseRefundListener;

    public void show(OrderDTO orderDTOs, OnRefuseRefundListener onRefuseRefundListener) {
        this.onRefuseRefundListener = onRefuseRefundListener;
        super.show();
    }

    public RefuseRefundDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_refuse_refund, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.8f, OUT_TO);
        dialog_refuse_refund_et = (EditText) view.findViewById(R.id.dialog_refuse_refund_et);
        dialog_refuse_refund_cancel = (TextView) view.findViewById(R.id.dialog_refuse_refund_cancel);
        dialog_refuse_refund_confirm = (TextView) view.findViewById(R.id.dialog_refuse_refund_confirm);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //取消
        dialog_refuse_refund_cancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //提交
        dialog_refuse_refund_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onRefuseRefundListener!=null){
                    if (dialog_refuse_refund_et.getText().toString().isEmpty()){
                        Toast.show(context,"请填写拒绝退款原因");
                        dialog_refuse_refund_et.requestFocus();//获取焦点 光标出现
                        KeyboardUtils.showSoftInput(dialog_refuse_refund_et);
                        return;
                    }
                    onRefuseRefundListener.onConfirm(dialog_refuse_refund_et.getText().toString());
                }
            }
        });

    }

    public interface OnRefuseRefundListener {
        void onConfirm(String remark);
    }
}
