package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;


/**
 * 通用提示对话框
 * Created by lenovo on 2016/12/22.
 */
public class CommonDialog extends BaseDialog {
    private Context context;
    private View view;

    private OnConfirmListener onConfirmListener;

    private OnCancelListener onCancelListener;

    private int message;

    private int messageColor = -1;

    private TextView common_cancel;
    private TextView common_confirm;

    public CommonDialog(Context context, int message) {
        super(context);
        this.context = context;
        this.message = message;
    }

    public void show(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        super.show();
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void setMessageColor(int color) {
        messageColor = color;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_common, null);
        return view;
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        common_cancel = (TextView) view.findViewById(R.id.common_cancel);
        common_confirm = (TextView) view.findViewById(R.id.common_confirm);

        TextView common_message = (TextView) findViewById(R.id.common_message);
        common_message.setText(message);
        if (messageColor != -1) {
            common_message.setTextColor(messageColor);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        common_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onCancelListener != null) {
                    onCancelListener.onCancel();
                }
                dismiss();
            }
        });
        common_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
                dismiss();
            }
        });
    }

    public interface OnConfirmListener {

        void onConfirm();
    }

    public interface OnCancelListener {

        void onCancel();
    }
}
