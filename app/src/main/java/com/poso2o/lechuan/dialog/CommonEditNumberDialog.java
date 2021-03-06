package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 通用编辑对话框
 * Created by lenovo on 2016/12/19.
 */
public class CommonEditNumberDialog extends BaseDialog {
    private Context context;
    private View view;
    private OnEditListener onEditListener;

    private EditText common_edit_edit;
    private TextView common_edit_cancel;
    private TextView common_edit_confirm;

    private String hint;//hint文字
    private String tipText;//按确定时候提示的文字
    private String value;//传过来的值

    public CommonEditNumberDialog(Context context, String tipText) {
        super(context);
        this.context = context;
        this.tipText = tipText;
    }

    public CommonEditNumberDialog(Context context, String hint, String tipText) {
        super(context);
        this.context = context;
        this.hint = hint;
        this.tipText = tipText;
    }

    public CommonEditNumberDialog(Context context, String hint, String tipText, String value) {
        super(context);
        this.context = context;
        this.hint = hint;
        this.tipText = tipText;
        this.value = value;
    }

    public void show(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
        super.show();
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_common_number_edit, null);
    }

    @Override
    public void initView() {
        common_edit_edit = (EditText) view.findViewById(R.id.common_edit_edit);
        common_edit_cancel = (TextView) view.findViewById(R.id.common_edit_cancel);
        common_edit_confirm = (TextView) view.findViewById(R.id.common_edit_confirm);
//        common_edit_edit.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
        if (TextUtil.isNotEmpty(value)) {
            common_edit_edit.setText(value);
            common_edit_confirm.setText("确定");
        }
        if (TextUtil.isNotEmpty(hint)) {
            common_edit_edit.setHint(hint);

        }
        if (TextUtil.isNotEmpty(tipText)) {
            tipText = "请输入内容";
        }

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) common_edit_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(common_edit_edit, 0);
                }
            }
        }, 200);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initListener() {
        common_edit_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });

        common_edit_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (common_edit_edit.getText().toString().equals("") || common_edit_edit.getText() == null) {
                    Toast.show(context, tipText);
                    return;
                }

                if (onEditListener != null) {
                    onEditListener.onConfirm(common_edit_edit.getText().toString());
                }
                dismiss();
            }
        });

    }

    public interface OnEditListener {

        void onConfirm(String name);
    }
}
